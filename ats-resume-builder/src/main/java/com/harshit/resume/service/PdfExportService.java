package com.harshit.resume.service;

import com.harshit.resume.model.EducationEntry;
import com.harshit.resume.model.ExperienceEntry;
import com.harshit.resume.model.ResumeDocument;
import com.harshit.resume.model.SkillCategory;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Builds the resume PDF from scratch on every export.
 * <p>
 * Nothing is stamped over a template PDF: a stamped file keeps the template's original text layer,
 * and ATS parsers read that hidden text even when a white rectangle covers it on screen.
 * Layout stays single-column, image-free and text-only so parsers see exactly what a human sees.
 * <p>
 * Everything is a {@link Paragraph} in normal document flow — tables are avoided because OpenPDF
 * writes their cell text in a later pass, which makes extracted text list every heading after the
 * body instead of above it.
 */
@Service
public class PdfExportService {

    private static final float MARGIN = 34f;
    private static final float MIN_SCALE = 0.70f;
    private static final float SCALE_STEP = 0.02f;
    private static final float BULLET_INDENT = 11f;
    private static final float TAIL_PAD = 4f;
    /** Distance from a section heading's baseline down to its underline rule. */
    private static final float RULE_DROP = 4f;

    public byte[] export(ResumeDocument resume) throws DocumentException, IOException {
        float scale = 1.0f;
        byte[] pdf = render(resume, scale);
        while (pageCount(pdf) > 1 && scale - SCALE_STEP >= MIN_SCALE) {
            scale -= SCALE_STEP;
            pdf = render(resume, scale);
        }
        return pdf;
    }

    private int pageCount(byte[] pdf) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        try {
            return reader.getNumberOfPages();
        } finally {
            reader.close();
        }
    }

    private byte[] render(ResumeDocument resume, float scale) throws DocumentException {
        Document document = new Document(PageSize.A4, MARGIN, MARGIN, MARGIN, MARGIN);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.addTitle(nullToEmpty(resume.getFullName()) + " - " + nullToEmpty(resume.getTitle()));
        document.addAuthor(nullToEmpty(resume.getFullName()));
        document.addSubject(nullToEmpty(resume.getTitle()));
        document.addKeywords(keywordMetadata(resume));

        document.open();
        Layout layout = new Layout(scale);
        float contentWidth = PageSize.A4.getWidth() - (2f * MARGIN);

        addHeader(document, resume, layout);
        addSummary(document, resume, layout, contentWidth);
        addSkills(document, resume, layout, contentWidth);
        addExperience(document, resume, layout, contentWidth);
        addEducation(document, resume, layout, contentWidth);
        addCertifications(document, resume, layout, contentWidth);

        document.close();
        return out.toByteArray();
    }

    private void addHeader(Document document, ResumeDocument resume, Layout layout) throws DocumentException {
        Paragraph name = new Paragraph(nullToEmpty(resume.getFullName()), layout.nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        name.setSpacingAfter(layout.s(2f));
        document.add(name);

        // Plain-text contact line: parsers pick up city, phone, email and the LinkedIn slug without
        // relying on icons or link annotations.
        Paragraph contact = new Paragraph();
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setLeading(0, layout.lineMultiplier);
        boolean first = true;
        for (String part : List.of(nullToEmpty(resume.getLocation()), nullToEmpty(resume.getPhone()),
                nullToEmpty(resume.getEmail()))) {
            if (part.isEmpty()) {
                continue;
            }
            if (!first) {
                contact.add(new Chunk("  |  ", layout.contactFont));
            }
            contact.add(new Chunk(part, layout.contactFont));
            first = false;
        }
        String linkedinLabel = displayUrl(resume.getLinkedinUrl());
        if (!linkedinLabel.isEmpty()) {
            if (!first) {
                contact.add(new Chunk("  |  ", layout.contactFont));
            }
            Chunk link = new Chunk(linkedinLabel, layout.contactFont);
            link.setAnchor(resume.getLinkedinUrl());
            contact.add(link);
        }
        contact.setSpacingAfter(layout.s(6f));
        document.add(contact);
    }

    private void addSummary(Document document, ResumeDocument resume, Layout layout, float contentWidth)
            throws DocumentException {
        if (nullToEmpty(resume.getSummary()).isEmpty()) {
            return;
        }
        addSectionHeading(document, "PROFILE SUMMARY", layout, contentWidth);
        Paragraph summary = new Paragraph(resume.getSummary(), layout.bodyFont);
        summary.setAlignment(Element.ALIGN_JUSTIFIED);
        summary.setLeading(0, layout.lineMultiplier);
        summary.setSpacingAfter(layout.s(5f));
        document.add(summary);
    }

    private void addSkills(Document document, ResumeDocument resume, Layout layout, float contentWidth)
            throws DocumentException {
        List<SkillCategory> categories = resolveSkillCategories(resume);
        if (categories.isEmpty()) {
            return;
        }
        addSectionHeading(document, "TECHNICAL SKILLS", layout, contentWidth);
        for (SkillCategory category : categories) {
            if (category.getItems() == null || category.getItems().isEmpty()) {
                continue;
            }
            Paragraph line = bulletParagraph(layout);
            line.add(new Chunk(category.getLabel() + ": ", layout.bodyBoldFont));
            line.add(new Chunk(String.join(", ", category.getItems()), layout.bodyFont));
            document.add(line);
        }
        document.add(gap(layout, 5f));
    }

    private void addExperience(Document document, ResumeDocument resume, Layout layout, float contentWidth)
            throws DocumentException {
        if (resume.getExperiences() == null || resume.getExperiences().isEmpty()) {
            return;
        }
        addSectionHeading(document, "EXPERIENCE", layout, contentWidth);
        for (ExperienceEntry exp : resume.getExperiences()) {
            String companyLine = joinNonEmpty(", ", exp.getCompany(), exp.getLocation());
            String clientLine = nullToEmpty(exp.getClient()).isEmpty() ? "" : "Client: " + exp.getClient();
            document.add(rowWithRightAlignedEnd(companyLine, clientLine, layout.companyFont,
                    layout.clientFont, contentWidth, layout));

            String dates = joinNonEmpty(" – ", exp.getStartDate(), exp.getEndDate());
            document.add(rowWithRightAlignedEnd(nullToEmpty(exp.getRole()), dates, layout.roleFont,
                    layout.roleFont, contentWidth, layout));

            if (exp.getBullets() != null) {
                for (String bullet : exp.getBullets()) {
                    Paragraph item = bulletParagraph(layout);
                    item.add(new Chunk(bullet, layout.bodyFont));
                    item.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(item);
                }
            }
            document.add(gap(layout, 4f));
        }
    }

    private void addEducation(Document document, ResumeDocument resume, Layout layout, float contentWidth)
            throws DocumentException {
        if (resume.getEducation() == null || resume.getEducation().isEmpty()) {
            return;
        }
        addSectionHeading(document, "EDUCATION", layout, contentWidth);
        for (EducationEntry ed : resume.getEducation()) {
            String dates = joinNonEmpty(" – ", ed.getStartDate(), ed.getEndDate());
            document.add(rowWithRightAlignedEnd(nullToEmpty(ed.getDegree()), dates, layout.roleFont,
                    layout.roleFont, contentWidth, layout));
            String school = joinNonEmpty(", ", ed.getInstitution(), ed.getDetails());
            if (!school.isEmpty()) {
                Paragraph institution = new Paragraph(school, layout.clientFont);
                institution.setLeading(0, layout.lineMultiplier);
                document.add(institution);
            }
        }
        document.add(gap(layout, 5f));
    }

    private void addCertifications(Document document, ResumeDocument resume, Layout layout, float contentWidth)
            throws DocumentException {
        if (resume.getCertifications() == null || resume.getCertifications().isEmpty()) {
            return;
        }
        addSectionHeading(document, "CERTIFICATIONS", layout, contentWidth);
        for (String certification : resume.getCertifications()) {
            if (nullToEmpty(certification).isBlank()) {
                continue;
            }
            Paragraph item = bulletParagraph(layout);
            item.add(new Chunk(certification, layout.bodyFont));
            document.add(item);
        }
    }

    private void addSectionHeading(Document document, String title, Layout layout, float contentWidth)
            throws DocumentException {
        Paragraph heading = new Paragraph(title, layout.sectionFont);
        heading.setLeading(0, layout.lineMultiplier);
        heading.setSpacingAfter(0f);
        document.add(heading);

        // Full-width rule drawn inside the text flow, so it costs no extraction order.
        // Offset stays 0 so the rule sits on its own baseline, RULE_DROP below the heading baseline —
        // any positive offset lifts it into the glyphs.
        LineSeparator rule = new LineSeparator(0.75f, 100f, Color.BLACK, Element.ALIGN_LEFT, 0f);
        Paragraph line = new Paragraph(new Chunk(rule));
        line.setLeading(layout.s(RULE_DROP), 0f);
        line.setSpacingAfter(layout.s(3.5f));
        document.add(line);
    }

    /** Bullet rendered as leading text so extraction order stays "• text" rather than "text •". */
    private Paragraph bulletParagraph(Layout layout) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("\u2022 ", layout.bodyFont));
        paragraph.setIndentationLeft(layout.s(BULLET_INDENT));
        paragraph.setFirstLineIndent(-layout.s(BULLET_INDENT));
        paragraph.setLeading(0, layout.lineMultiplier);
        paragraph.setSpacingAfter(layout.s(1.2f));
        return paragraph;
    }

    /**
     * One flowing line with {@code left} at the margin and {@code right} ending at the right margin,
     * using a tab stop instead of a table so the reading order stays "left then right".
     */
    private Paragraph rowWithRightAlignedEnd(String left, String right, Font leftFont, Font rightFont,
                                             float contentWidth, Layout layout) {
        Paragraph paragraph = new Paragraph();
        paragraph.setLeading(0, layout.lineMultiplier);
        paragraph.setSpacingAfter(layout.s(1f));
        paragraph.add(new Chunk(nullToEmpty(left), leftFont));

        String tail = nullToEmpty(right);
        if (!tail.isEmpty()) {
            // A tab stop leaves exactly (contentWidth - tabPosition) for the tail, so the pad keeps
            // the measured width from landing on the wrap boundary and pushing a word to a new line.
            float tailWidth = rightFont.getCalculatedBaseFont(false)
                    .getWidthPoint(tail, rightFont.getCalculatedSize()) + TAIL_PAD;
            float tabPosition = Math.max(layout.s(60f), contentWidth - tailWidth);
            paragraph.add(new Chunk(new VerticalPositionMark(), tabPosition, false));
            paragraph.add(new Chunk(tail, rightFont));
        }
        return paragraph;
    }

    private Paragraph gap(Layout layout, float points) {
        Paragraph paragraph = new Paragraph(" ", layout.bodyFont);
        paragraph.setLeading(layout.s(points), 0f);
        paragraph.setSpacingAfter(0f);
        return paragraph;
    }

    private String keywordMetadata(ResumeDocument resume) {
        StringBuilder sb = new StringBuilder();
        for (SkillCategory category : resolveSkillCategories(resume)) {
            if (category.getItems() != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(String.join(", ", category.getItems()));
            }
        }
        return sb.toString();
    }

    private List<SkillCategory> resolveSkillCategories(ResumeDocument resume) {
        if (resume.getSkillCategories() != null && !resume.getSkillCategories().isEmpty()) {
            return resume.getSkillCategories();
        }
        if (resume.getSkills() == null || resume.getSkills().isEmpty()) {
            return List.of();
        }
        SkillCategory all = new SkillCategory();
        all.setLabel("Skills");
        all.setItems(resume.getSkills());
        return List.of(all);
    }

    private String displayUrl(String url) {
        String value = nullToEmpty(url);
        return value.replaceFirst("^https?://", "").replaceFirst("^www\\.", "");
    }

    private String joinNonEmpty(String separator, String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            String value = nullToEmpty(part).trim();
            if (value.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(value);
        }
        return sb.toString();
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private static final class Layout {
        final float scale;
        final float lineMultiplier;
        final Font nameFont;
        final Font contactFont;
        final Font sectionFont;
        final Font companyFont;
        final Font roleFont;
        final Font clientFont;
        final Font bodyFont;
        final Font bodyBoldFont;

        Layout(float scale) {
            this.scale = scale;
            this.lineMultiplier = Math.max(1.0f, 1.06f - (1.0f - scale) * 0.15f);
            this.nameFont = sized(FontFactory.TIMES_BOLD, 18f, scale);
            this.contactFont = sized(FontFactory.TIMES, 9.8f, scale);
            this.sectionFont = sized(FontFactory.TIMES_BOLD, 11.5f, scale);
            this.companyFont = sized(FontFactory.TIMES_BOLD, 10.4f, scale);
            this.roleFont = sized(FontFactory.TIMES_BOLD, 10f, scale);
            this.clientFont = sized(FontFactory.TIMES_ITALIC, 9.8f, scale);
            this.bodyFont = sized(FontFactory.TIMES, 9.8f, scale);
            this.bodyBoldFont = sized(FontFactory.TIMES_BOLD, 9.8f, scale);
        }

        float s(float value) {
            return value * scale;
        }

        private static Font sized(String family, float basePt, float scale) {
            return FontFactory.getFont(family, Math.max(6f, basePt * scale));
        }
    }
}
