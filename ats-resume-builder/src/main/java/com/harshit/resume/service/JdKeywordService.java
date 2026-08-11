package com.harshit.resume.service;

import com.harshit.resume.model.ResumeDocument;
import com.harshit.resume.model.SkillCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts likely ATS keywords from a job description.
 */
@Service
public class JdKeywordService {

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "and", "for", "with", "you", "your", "our", "will", "are", "have", "has", "this", "that",
            "from", "into", "able", "work", "team", "role", "job", "years", "year", "experience", "required",
            "preferred", "including", "using", "use", "must", "should", "can", "all", "any", "who", "what"
    );

    private static final List<String> TECH_DICTIONARY = List.of(
            "java", "spring", "spring boot", "microservices", "microservice", "kafka", "hibernate", "jpa",
            "rest", "restful", "api", "sql", "mysql", "postgresql", "mongodb", "docker", "kubernetes", "aws",
            "azure", "gcp", "jenkins", "ci/cd", "cicd", "git", "maven", "gradle", "junit", "oauth", "jwt",
            "redis", "elasticsearch", "react", "angular", "node", "python", "agile", "scrum", "devops",
            "terraform", "ansible", "linux", "unix", "splunk", "apigee", "sonarqube", "backend", "full stack",
            "software engineer", "software developer", "design patterns", "system design", "multithreading",
            "concurrency", "distributed systems", "message queue", "rabbitmq", "graphql", "typescript",
            "xml", "serenity", "bdd", "soapui", "gitlab", "oss", "bss", "telecom", "agile", "mockito"
    );

    private static final Pattern WORD = Pattern.compile("[a-zA-Z+#/]{2,}");

    public List<String> extractKeywords(String jobDescription) {
        if (jobDescription == null || jobDescription.isBlank()) {
            return List.of();
        }
        String jd = jobDescription.toLowerCase(Locale.ROOT);
        LinkedHashSet<String> keywords = new LinkedHashSet<>();

        for (String term : TECH_DICTIONARY) {
            if (jd.contains(term)) {
                keywords.add(normalize(term));
            }
        }

        Matcher matcher = WORD.matcher(jd);
        while (matcher.find()) {
            String word = matcher.group().toLowerCase(Locale.ROOT);
            if (word.length() >= 3 && !STOP_WORDS.contains(word)) {
                keywords.add(word);
            }
        }

        return new ArrayList<>(keywords).stream().limit(40).toList();
    }

    public String normalize(String keyword) {
        return keyword.trim().toLowerCase(Locale.ROOT);
    }

    public String resumeAsText(ResumeDocument resume) {
        StringBuilder sb = new StringBuilder();
        if (resume.getTitle() != null) {
            sb.append(resume.getTitle()).append(' ');
        }
        if (resume.getSummary() != null) {
            sb.append(resume.getSummary()).append(' ');
        }
        if (resume.getSkillCategories() != null && !resume.getSkillCategories().isEmpty()) {
            for (SkillCategory category : resume.getSkillCategories()) {
                if (category.getItems() != null) {
                    sb.append(String.join(" ", category.getItems())).append(' ');
                }
            }
        } else if (resume.getSkills() != null) {
            sb.append(String.join(" ", resume.getSkills())).append(' ');
        }
        if (resume.getExperiences() != null) {
            resume.getExperiences().forEach(exp -> {
                sb.append(exp.getRole()).append(' ').append(exp.getCompany()).append(' ');
                if (exp.getClient() != null) {
                    sb.append(exp.getClient()).append(' ');
                }
                if (exp.getBullets() != null) {
                    sb.append(String.join(" ", exp.getBullets())).append(' ');
                }
            });
        }
        if (resume.getEducation() != null) {
            resume.getEducation().forEach(ed -> {
                sb.append(ed.getDegree()).append(' ').append(ed.getInstitution()).append(' ');
            });
        }
        return sb.toString().toLowerCase(Locale.ROOT);
    }
}
