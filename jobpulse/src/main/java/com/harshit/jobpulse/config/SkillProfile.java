package com.harshit.jobpulse.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** The role/skill profile that incoming postings are scored against. */
public class SkillProfile {

    private List<String> targetRoles = new ArrayList<>();
    private List<String> mustHaveSkills = new ArrayList<>();
    private List<String> niceToHaveSkills = new ArrayList<>();

    /** Postings whose title contains any of these are rejected outright. */
    private List<String> excludeTitleKeywords = new ArrayList<>();

    /** Minimum number of must-have skills required for a posting to be considered a match. */
    private int minMustHaveMatches = 2;

    /** Score threshold (0-100) at or above which a posting is flagged as matched. */
    private int minScore = 55;

    /** Skill name to the surface forms found in job descriptions. */
    private Map<String, List<String>> aliases = new LinkedHashMap<>();

    public List<String> getTargetRoles() {
        return targetRoles;
    }

    public void setTargetRoles(List<String> targetRoles) {
        this.targetRoles = targetRoles;
    }

    public List<String> getMustHaveSkills() {
        return mustHaveSkills;
    }

    public void setMustHaveSkills(List<String> mustHaveSkills) {
        this.mustHaveSkills = mustHaveSkills;
    }

    public List<String> getNiceToHaveSkills() {
        return niceToHaveSkills;
    }

    public void setNiceToHaveSkills(List<String> niceToHaveSkills) {
        this.niceToHaveSkills = niceToHaveSkills;
    }

    public List<String> getExcludeTitleKeywords() {
        return excludeTitleKeywords;
    }

    public void setExcludeTitleKeywords(List<String> excludeTitleKeywords) {
        this.excludeTitleKeywords = excludeTitleKeywords;
    }

    public int getMinMustHaveMatches() {
        return minMustHaveMatches;
    }

    public void setMinMustHaveMatches(int minMustHaveMatches) {
        this.minMustHaveMatches = minMustHaveMatches;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public Map<String, List<String>> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, List<String>> aliases) {
        this.aliases = aliases;
    }
}
