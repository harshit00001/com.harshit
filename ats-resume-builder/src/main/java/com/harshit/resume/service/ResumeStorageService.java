package com.harshit.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.resume.config.ResumeProperties;
import com.harshit.resume.model.ResumeDocument;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ResumeStorageService {

    private final ResumeProperties properties;
    private final ObjectMapper objectMapper;
    private ResumeDocument current;

    public ResumeStorageService(ResumeProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        Path path = Path.of(properties.getDataFile());
        if (Files.exists(path)) {
            current = objectMapper.readValue(path.toFile(), ResumeDocument.class);
            backfillMissingSectionsFromDefault();
        } else {
            current = loadDefault();
            save(current);
        }
    }

    public synchronized ResumeDocument get() {
        return deepCopy(current);
    }

    public synchronized void save(ResumeDocument resume) throws IOException {
        this.current = deepCopy(resume);
        Path path = Path.of(properties.getDataFile());
        Files.createDirectories(path.getParent());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), current);
    }

    public synchronized void resetToDefault() throws IOException {
        current = loadDefault();
        save(current);
    }

    private ResumeDocument loadDefault() throws IOException {
        return objectMapper.readValue(
                new ClassPathResource("resume-default.json").getInputStream(),
                ResumeDocument.class
        );
    }

    /** Restores sections accidentally cleared (e.g. by a partial save) from resume-default.json. */
    private void backfillMissingSectionsFromDefault() throws IOException {
        ResumeDocument defaults = loadDefault();
        boolean changed = false;

        if (isEmpty(current.getSkillCategories()) && !isEmpty(defaults.getSkillCategories())) {
            current.setSkillCategories(defaults.getSkillCategories());
            changed = true;
        }
        if (isEmpty(current.getExperiences()) && !isEmpty(defaults.getExperiences())) {
            current.setExperiences(defaults.getExperiences());
            changed = true;
        }
        if (isEmpty(current.getEducation()) && !isEmpty(defaults.getEducation())) {
            current.setEducation(defaults.getEducation());
            changed = true;
        }
        if (isEmpty(current.getSkills()) && !isEmpty(defaults.getSkills())) {
            current.setSkills(defaults.getSkills());
            changed = true;
        }

        if (changed) {
            save(current);
        }
    }

    private static <T> boolean isEmpty(java.util.List<T> list) {
        return list == null || list.isEmpty();
    }

    private ResumeDocument deepCopy(ResumeDocument source) {
        return objectMapper.convertValue(source, ResumeDocument.class);
    }
}
