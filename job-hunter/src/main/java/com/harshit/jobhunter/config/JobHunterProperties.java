package com.harshit.jobhunter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "job-hunter")
public class JobHunterProperties {

    private boolean fetchOnStartup = true;
    private String fetchCron = "0 0 6,18 * * *";
    private int defaultDays = 14;
    private Profile profile = new Profile();
    private List<Source> sources = new ArrayList<>();

    public boolean isFetchOnStartup() {
        return fetchOnStartup;
    }

    public void setFetchOnStartup(boolean fetchOnStartup) {
        this.fetchOnStartup = fetchOnStartup;
    }

    public String getFetchCron() {
        return fetchCron;
    }

    public void setFetchCron(String fetchCron) {
        this.fetchCron = fetchCron;
    }

    public int getDefaultDays() {
        return defaultDays;
    }

    public void setDefaultDays(int defaultDays) {
        this.defaultDays = defaultDays;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public static class Profile {
        private int experienceYears = 4;
        private int minExperienceYears = 2;
        private int maxExperienceYears = 6;
        private List<String> titleKeywords = new ArrayList<>();
        private List<String> mustHaveSkills = new ArrayList<>();
        private List<String> strongSkills = new ArrayList<>();
        private List<String> locationKeywords = new ArrayList<>();
        private List<String> excludeTitleKeywords = new ArrayList<>();

        public int getExperienceYears() {
            return experienceYears;
        }

        public void setExperienceYears(int experienceYears) {
            this.experienceYears = experienceYears;
        }

        public int getMinExperienceYears() {
            return minExperienceYears;
        }

        public void setMinExperienceYears(int minExperienceYears) {
            this.minExperienceYears = minExperienceYears;
        }

        public int getMaxExperienceYears() {
            return maxExperienceYears;
        }

        public void setMaxExperienceYears(int maxExperienceYears) {
            this.maxExperienceYears = maxExperienceYears;
        }

        public List<String> getTitleKeywords() {
            return titleKeywords;
        }

        public void setTitleKeywords(List<String> titleKeywords) {
            this.titleKeywords = titleKeywords;
        }

        public List<String> getMustHaveSkills() {
            return mustHaveSkills;
        }

        public void setMustHaveSkills(List<String> mustHaveSkills) {
            this.mustHaveSkills = mustHaveSkills;
        }

        public List<String> getStrongSkills() {
            return strongSkills;
        }

        public void setStrongSkills(List<String> strongSkills) {
            this.strongSkills = strongSkills;
        }

        public List<String> getLocationKeywords() {
            return locationKeywords;
        }

        public void setLocationKeywords(List<String> locationKeywords) {
            this.locationKeywords = locationKeywords;
        }

        public List<String> getExcludeTitleKeywords() {
            return excludeTitleKeywords;
        }

        public void setExcludeTitleKeywords(List<String> excludeTitleKeywords) {
            this.excludeTitleKeywords = excludeTitleKeywords;
        }
    }

    public static class Source {
        private String name;
        private String type;
        private boolean enabled = true;
        private String boardToken;
        private String site;
        private String searchUrl;
        private List<String> keywords = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getBoardToken() {
            return boardToken;
        }

        public void setBoardToken(String boardToken) {
            this.boardToken = boardToken;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getSearchUrl() {
            return searchUrl;
        }

        public void setSearchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }
    }
}
