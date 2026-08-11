package com.harshit.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.resume.model.ExperienceEntry;
import com.harshit.resume.model.ResumeDocument;
import com.harshit.resume.model.SkillCategory;
import com.harshit.resume.model.TailorResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Tailors resume to JD: reorder skills, boost summary, sort bullets by keyword relevance.
 * Does NOT invent fake skills — only reorders and emphasizes existing content.
 */
@Service
public class ResumeTailorService {

    private final JdKeywordService jdKeywordService;
    private final AtsScoringService atsScoringService;
    private final ObjectMapper objectMapper;

    public ResumeTailorService(JdKeywordService jdKeywordService,
                               AtsScoringService atsScoringService,
                               ObjectMapper objectMapper) {
        this.jdKeywordService = jdKeywordService;
        this.atsScoringService = atsScoringService;
        this.objectMapper = objectMapper;
    }

    public TailorResponse tailor(ResumeDocument original, String jobDescription) {
        ResumeDocument copy = objectMapper.convertValue(original, ResumeDocument.class);

        var before = atsScoringService.score(copy, jobDescription);
        List<String> jdKeywords = jdKeywordService.extractKeywords(jobDescription);

        copy.setSkills(reorderSkills(copy.getSkills(), jdKeywords));
        copy.setSkillCategories(reorderSkillCategories(copy.getSkillCategories(), jdKeywords));
        copy.setSummary(enhanceSummary(copy.getSummary(), jdKeywords));
        copy.setExperiences(reorderExperienceBullets(copy.getExperiences(), jdKeywords));

        var after = atsScoringService.score(copy, jobDescription);

        TailorResponse response = new TailorResponse();
        response.setTailoredResume(copy);
        response.setReportBefore(before);
        response.setReportAfter(after);
        return response;
    }

    private List<String> reorderSkills(List<String> skills, List<String> jdKeywords) {
        if (skills == null) {
            return List.of();
        }
        List<String> sorted = new ArrayList<>(skills);
        sorted.sort(Comparator.comparingInt(skill -> {
            String s = skill.toLowerCase(Locale.ROOT);
            for (int i = 0; i < jdKeywords.size(); i++) {
                if (s.contains(jdKeywords.get(i)) || jdKeywords.get(i).contains(s)) {
                    return i;
                }
            }
            return 1000;
        }));
        return sorted;
    }

    private List<SkillCategory> reorderSkillCategories(List<SkillCategory> categories, List<String> jdKeywords) {
        if (categories == null || categories.isEmpty()) {
            return categories;
        }
        List<SkillCategory> result = new ArrayList<>();
        for (SkillCategory category : categories) {
            SkillCategory copy = new SkillCategory();
            copy.setLabel(category.getLabel());
            copy.setItems(reorderSkills(category.getItems(), jdKeywords));
            result.add(copy);
        }
        return result;
    }

    private String enhanceSummary(String summary, List<String> jdKeywords) {
        return summary == null ? "" : summary.trim();
    }

    private List<ExperienceEntry> reorderExperienceBullets(List<ExperienceEntry> experiences, List<String> jdKeywords) {
        if (experiences == null) {
            return List.of();
        }
        for (ExperienceEntry exp : experiences) {
            if (exp.getBullets() == null) {
                continue;
            }
            List<String> bullets = new ArrayList<>(exp.getBullets());
            bullets.sort(Comparator.comparingInt(b -> -keywordHits(b, jdKeywords)));
            exp.setBullets(bullets);
        }
        return experiences;
    }

    private int keywordHits(String text, List<String> jdKeywords) {
        String lower = text.toLowerCase(Locale.ROOT);
        int hits = 0;
        for (String k : jdKeywords) {
            if (lower.contains(k)) {
                hits++;
            }
        }
        return hits;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
