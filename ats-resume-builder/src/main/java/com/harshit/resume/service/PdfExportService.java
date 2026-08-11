package com.harshit.resume.service;

import com.harshit.resume.model.EducationEntry;
import com.harshit.resume.model.ExperienceEntry;
import com.harshit.resume.model.ResumeDocument;
import com.harshit.resume.model.SkillCategory;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Exports by editing {@code pdf/Harshit_Raj_Resume.pdf}:
 * header (name, contact, icons) stays untouched from the template; only body text is redrawn.
 */
@Service
public class PdfExportService {

    private static final String[] TEMPLATE_PATHS = {
            "pdf/Harshit_Raj_Resume.pdf",
            "pdf/resume-template.pdf"
    };
    private static final float MIN_SCALE = 0.65f;
    private static final float SCALE_STEP = 0.02f;
    /** OpenPDF Y (from bottom) — everything below header is replaced on page 1. */
    private static final float BODY_TOP_Y = 668f;
    private static final float MARGIN = 32f;

    public byte[] export(ResumeDocument resume) throws DocumentException, IOException {
        if (!templateAvailable()) {
            throw new IOException(
                    "Missing PDF template — place Harshit_Raj_Resume.pdf in src/main/resources/pdf/");
        }

        float scale = 1.0f;
        RenderResult result = renderFromTemplate(resume, scale);
        while (result.overflow() && scale - SCALE_STEP >= MIN_SCALE) {
            scale -= SCALE_STEP;
            result = renderFromTemplate(resume, scale);
        }
        return result.bytes();
    }

    private boolean templateAvailable() {
        return resolveTemplateResource() != null;
    }

    private ClassPathResource resolveTemplateResource() {
        for (String path : TEMPLATE_PATHS) {
            ClassPathResource resource = new ClassPathResource(path);
            if (resource.exists()) {
                return resource;
            }
        }
        return null;
    }

    private RenderResult renderFromTemplate(ResumeDocument resume, float scale)
            throws IOException, DocumentException {
        ClassPathResource resource = resolveTemplateResource();
        if (resource == null) {
            throw new IOException("PDF template not found");
        }
        byte[] templateBytes;
        try (InputStream in = resource.getInputStream()) {
            templateBytes = in.readAllBytes();
        }

        PdfReader reader = new PdfReader(templateBytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, out);
        PdfContentByte canvas = stamper.getOverContent(1);

        float pageWidth = reader.getPageSize(1).getWidth();
        float columnWidth = pageWidth - (2f * MARGIN);

        Layout layout = new Layout(scale);

        canvas.saveState();
        canvas.setColorFill(Color.WHITE);
        canvas.rectangle(MARGIN, MARGIN, pageWidth - (2f * MARGIN), BODY_TOP_Y - MARGIN);
        canvas.fill();
        canvas.restoreState();

        ColumnText column = new ColumnText(canvas);
        column.setSimpleColumn(MARGIN, MARGIN, pageWidth - MARGIN, BODY_TOP_Y);
        populateBody(column, resume, layout, columnWidth);

        int status = column.go();
        boolean overflow = ColumnText.hasMoreText(status);
        float contentBottomY = column.getYLine();

        stamper.close();
        reader.close();
        return new RenderResult(out.toByteArray(), overflow, contentBottomY);
    }

    private void populateBody(ColumnText column, ResumeDocument resume, Layout layout, float tableWidth)
            throws DocumentException {
        addSectionHeading(column, "SUMMARY", layout, tableWidth, false);
        column.addElement(oneLineGap(layout));

        Paragraph summary = new Paragraph(nullToEmpty(resume.getSummary()), layout.bodyFont);
        summary.setAlignment(Element.ALIGN_JUSTIFIED);
        summary.setSpacingBefore(0f);
        summary.setSpacingAfter(0f);
        summary.setLeading(0, layout.lineMultiplier);
        column.addElement(summary);
        column.addElement(oneLineGap(layout));

        addSectionHeading(column, "TECHNICAL SKILLS", layout, tableWidth, true);
        for (SkillCategory category : resolveSkillCategories(resume)) {
            Paragraph skillLine = new Paragraph();
            skillLine.setSpacingBefore(0f);
            skillLine.setLeading(0, layout.lineMultiplier);
            skillLine.add(new Chunk(category.getLabel() + ": ", layout.bodyBoldFont));
            skillLine.add(new Chunk(String.join(", ", category.getItems()), layout.bodyFont));
            skillLine.setSpacingAfter(layout.s(0.2f));
            column.addElement(skillLine);
        }

        column.addElement(oneLineGap(layout));
        addSectionHeading(column, "PROFESSIONAL EXPERIENCE", layout, tableWidth, true);
        if (resume.getExperiences() != null) {
            for (int i = 0; i < resume.getExperiences().size(); i++) {
                addWorkExperience(column, resume.getExperiences().get(i), layout, tableWidth);
                if (i < resume.getExperiences().size() - 1) {
                    column.addElement(spacer(layout, 0.1f));
                }
            }
        }

        column.addElement(oneLineGap(layout));
        addSectionHeading(column, "EDUCATION", layout, tableWidth, true);
        if (resume.getEducation() != null) {
            for (EducationEntry ed : resume.getEducation()) {
                column.addElement(roleDateTable(ed.getInstitution(),
                        ed.getStartDate() + " – " + ed.getEndDate(), layout.companyFont, tableWidth));
                Paragraph degree = new Paragraph(nullToEmpty(ed.getDegree()), layout.degreeFont);
                degree.setLeading(0, layout.lineMultiplier);
                column.addElement(degree);
                if (ed.getDetails() != null) {
                    Paragraph details = new Paragraph(ed.getDetails(), layout.degreeFont);
                    details.setLeading(0, layout.lineMultiplier);
                    column.addElement(details);
                }
            }
        }
    }

    private void addWorkExperience(ColumnText column, ExperienceEntry exp, Layout layout, float tableWidth)
            throws DocumentException {
        column.addElement(roleDateTable(exp.getRole(),
                exp.getStartDate() + " – " + exp.getEndDate(), layout.companyFont, tableWidth));

        if (!nullToEmpty(exp.getClient()).isEmpty()) {
            column.addElement(roleDateTable(
                    exp.getCompany() + " – " + exp.getLocation(),
                    "Client ~ " + exp.getClient(),
                    layout.companyItalicFont,
                    tableWidth));
        } else {
            Paragraph company = new Paragraph(
                    exp.getCompany() + " – " + exp.getLocation(), layout.companyItalicFont);
            company.setSpacingAfter(layout.s(0.35f));
            company.setLeading(0, layout.lineMultiplier);
            column.addElement(company);
        }

        if (exp.getBullets() != null) {
            List bullets = new List(List.UNORDERED);
            bullets.setListSymbol("\u2022 ");
            bullets.setIndentationLeft(layout.s(12f));
            for (String bullet : exp.getBullets()) {
                ListItem item = new ListItem(bullet, layout.bodyFont);
                item.setAlignment(Element.ALIGN_JUSTIFIED);
                item.setLeading(0, layout.lineMultiplier);
                item.setSpacingAfter(layout.s(0.02f));
                bullets.add(item);
            }
            column.addElement(bullets);
        }
    }

    private void addSectionHeading(ColumnText column, String title, Layout layout, float tableWidth,
                                   boolean drawLine) throws DocumentException {
        if (!drawLine) {
            Paragraph heading = new Paragraph(title, layout.sectionFont);
            heading.setSpacingBefore(0f);
            heading.setSpacingAfter(0f);
            column.addElement(heading);
            return;
        }

        PdfPTable ruleTable = new PdfPTable(1);
        ruleTable.setWidthPercentage(100);
        ruleTable.setTotalWidth(tableWidth);
        ruleTable.setLockedWidth(true);
        ruleTable.setSpacingBefore(0f);
        ruleTable.setSpacingAfter(0f);

        PdfPCell titleCell = new PdfPCell(new Phrase(title, layout.sectionFont));
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.setBorderWidthBottom(0.75f);
        titleCell.setBorderColorBottom(Color.BLACK);
        titleCell.setPadding(0f);
        titleCell.setPaddingBottom(1f);
        titleCell.setUseAscender(true);
        ruleTable.addCell(titleCell);
        column.addElement(ruleTable);

        column.addElement(oneLineGap(layout));
    }

    /** Exactly one line of vertical space (same as below summary). */
    private Paragraph oneLineGap(Layout layout) {
        Paragraph gap = new Paragraph(" ");
        gap.setSpacingBefore(0f);
        gap.setSpacingAfter(0f);
        gap.setLeading(oneLineSpace(layout), 0f);
        return gap;
    }

    private float oneLineSpace(Layout layout) {
        return layout.bodyFont.getSize() * 0.85f;
    }

    private Paragraph spacer(Layout layout, float pts) {
        Paragraph gap = new Paragraph(" ");
        gap.setSpacingAfter(layout.s(pts));
        return gap;
    }

    private PdfPTable roleDateTable(String left, String right, Font font, float totalWidth)
            throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setTotalWidth(totalWidth);
        table.setLockedWidth(true);
        table.setWidths(new float[]{68f, 32f});
        table.setSpacingBefore(0f);
        table.setSpacingAfter(0f);

        PdfPCell leftCell = new PdfPCell(new Phrase(nullToEmpty(left), font));
        leftCell.setBorder(PdfPCell.NO_BORDER);
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setPadding(0f);
        leftCell.setPaddingBottom(0f);

        PdfPCell rightCell = new PdfPCell(new Phrase(nullToEmpty(right), font));
        rightCell.setBorder(PdfPCell.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightCell.setPadding(0f);
        rightCell.setPaddingBottom(0f);

        table.addCell(leftCell);
        table.addCell(rightCell);
        return table;
    }

    private java.util.List<SkillCategory> resolveSkillCategories(ResumeDocument resume) {
        if (resume.getSkillCategories() != null && !resume.getSkillCategories().isEmpty()) {
            return resume.getSkillCategories();
        }
        SkillCategory all = new SkillCategory();
        all.setLabel("Skills");
        all.setItems(resume.getSkills() != null ? resume.getSkills() : java.util.List.of());
        return java.util.List.of(all);
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private record RenderResult(byte[] bytes, boolean overflow, float contentBottomY) {
        float gapAboveBottom() {
            return contentBottomY - MARGIN;
        }
    }

    private static final class Layout {
        final float scale;
        final float lineMultiplier;
        final Font sectionFont;
        final Font companyFont;
        final Font companyItalicFont;
        final Font bodyFont;
        final Font bodyBoldFont;
        final Font degreeFont;

        Layout(float scale) {
            this.scale = scale;
            this.lineMultiplier = Math.max(1.0f, 1.04f - (1.0f - scale) * 0.12f);
            this.sectionFont = sized(FontFactory.TIMES_BOLD, 12.5f);
            this.companyFont = sized(FontFactory.TIMES_BOLD, 10.75f);
            this.companyItalicFont = sized(FontFactory.TIMES_ITALIC, 10.25f);
            this.bodyFont = sized(FontFactory.TIMES, 10.25f);
            this.bodyBoldFont = sized(FontFactory.TIMES_BOLD, 10.25f);
            this.degreeFont = sized(FontFactory.TIMES_ITALIC, 10.25f);
        }

        float s(float value) {
            return value * scale;
        }

        private Font sized(String family, float basePt) {
            float pt = Math.max(5.5f, basePt * scale);
            return FontFactory.getFont(family, pt);
        }
    }
}
