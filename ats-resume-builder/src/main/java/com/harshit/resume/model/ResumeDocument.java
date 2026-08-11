package com.harshit.resume.model;

import java.util.ArrayList;
import java.util.List;

public class ResumeDocument {

    private String fullName;
    private String title;
    private String location;
    private String phone;
    private String email;
    private String linkedin;
    private String linkedinUrl;
    private String summary;
    private List<String> skills = new ArrayList<>();
    private List<SkillCategory> skillCategories = new ArrayList<>();
    private List<ExperienceEntry> experiences = new ArrayList<>();
    private List<EducationEntry> education = new ArrayList<>();

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<SkillCategory> getSkillCategories() {
        return skillCategories;
    }

    public void setSkillCategories(List<SkillCategory> skillCategories) {
        this.skillCategories = skillCategories;
    }

    public List<ExperienceEntry> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceEntry> experiences) {
        this.experiences = experiences;
    }

    public List<EducationEntry> getEducation() {
        return education;
    }

    public void setEducation(List<EducationEntry> education) {
        this.education = education;
    }
}
