package com.harshit.resume.service;

import com.harshit.resume.model.AtsReport;
import com.harshit.resume.model.ResumeDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AtsScoringService {

    private final JdKeywordService jdKeywordService;

    public AtsScoringService(JdKeywordService jdKeywordService) {
        this.jdKeywordService = jdKeywordService;
    }

    public AtsReport score(ResumeDocument resume, String jobDescription) {
        List<String> keywords = jdKeywordService.extractKeywords(jobDescription);
        String resumeText = jdKeywordService.resumeAsText(resume);

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String keyword : keywords) {
            if (resumeContains(resumeText, keyword)) {
                matched.add(keyword);
            } else {
                missing.add(keyword);
            }
        }

        int keywordScore = keywords.isEmpty() ? 50 : (matched.size() * 100 / keywords.size());
        int structureScore = structureScore(resume);
        int contactScore = contactScore(resume);

        int total = (int) (keywordScore * 0.65 + structureScore * 0.20 + contactScore * 0.15);
        total = Math.min(100, Math.max(0, total));

        AtsReport report = new AtsReport();
        report.setScore(total);
        report.setGrade(grade(total));
        report.setMatchedKeywords(matched);
        report.setMissingKeywords(missing.stream().limit(15).toList());
        report.setSuggestions(buildSuggestions(resume, missing, total));
        return report;
    }

    private boolean resumeContains(String resumeText, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }
        String k = keyword.toLowerCase(Locale.ROOT);
        if (resumeText.contains(k)) {
            return true;
        }
        return switch (k) {
            case "spring" -> resumeText.contains("spring boot") || resumeText.contains("spring security")
                    || resumeText.contains("spring data");
            case "rest" -> resumeText.contains("restful") || resumeText.contains(" rest ");
            case "api" -> resumeText.contains("apis") || resumeText.contains(" apigee");
            case "ci/cd", "cicd" -> resumeText.contains("ci/cd") || resumeText.contains("gitlab ci/cd");
            case "kafka" -> resumeText.contains("apache kafka");
            case "react" -> resumeText.contains("react.js");
            case "jpa" -> resumeText.contains("spring data jpa");
            case "bdd" -> resumeText.contains("serenity bdd");
            case "devops" -> resumeText.contains("kubernetes") || resumeText.contains("jenkins")
                    || resumeText.contains("docker");
            default -> false;
        };
    }

    private int structureScore(ResumeDocument resume) {
        int score = 0;
        if (notBlank(resume.getSummary())) score += 25;
        if (resume.getSkills() != null && resume.getSkills().size() >= 5) score += 25;
        if (resume.getExperiences() != null && !resume.getExperiences().isEmpty()) score += 25;
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) score += 25;
        return score;
    }

    private int contactScore(ResumeDocument resume) {
        int score = 0;
        if (notBlank(resume.getEmail())) score += 34;
        if (notBlank(resume.getPhone())) score += 33;
        if (notBlank(resume.getLinkedin())) score += 33;
        return score;
    }

    private List<String> buildSuggestions(ResumeDocument resume, List<String> missing, int score) {
        List<String> tips = new ArrayList<>();
        if (score < 70) {
            tips.add("Add JD keywords naturally in Summary and Skills (only if you truly have that experience).");
        }
        if (!missing.isEmpty()) {
            tips.add("Missing JD terms to consider: " + String.join(", ", missing.stream().limit(8).toList()));
        }
        if (!notBlank(resume.getSummary())) {
            tips.add("Add a 3–4 line professional summary aligned to the role.");
        }
        tips.add("Use bullet points with metrics (%, latency, users) — already strong in your experience.");
        tips.add("Keep one column, standard headings: Summary, Skills, Experience, Education.");
        return tips;
    }

    private String grade(int score) {
        if (score >= 85) return "Excellent";
        if (score >= 70) return "Good";
        if (score >= 55) return "Fair";
        return "Needs improvement";
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
