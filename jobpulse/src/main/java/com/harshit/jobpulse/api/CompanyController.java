package com.harshit.jobpulse.api;

import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.JobPulseProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final JobPulseProperties properties;

    public CompanyController(JobPulseProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        return properties.getCompanies().stream().map(this::describe).toList();
    }

    @GetMapping("/profile")
    public Map<String, Object> profile() {
        return Map.of(
                "targetRoles", properties.getProfile().getTargetRoles(),
                "mustHaveSkills", properties.getProfile().getMustHaveSkills(),
                "niceToHaveSkills", properties.getProfile().getNiceToHaveSkills(),
                "excludeTitleKeywords", properties.getProfile().getExcludeTitleKeywords(),
                "minScore", properties.getProfile().getMinScore(),
                "minMustHaveMatches", properties.getProfile().getMinMustHaveMatches(),
                "locationCountries", properties.getLocationFilter().getCountries());
    }

    private Map<String, Object> describe(CompanyConfig company) {
        return Map.of(
                "id", company.getId(),
                "name", company.getName(),
                "connector", company.getConnector().name(),
                "enabled", company.isEnabled(),
                "careerUrl", company.getCareerUrl() == null ? "" : company.getCareerUrl(),
                "maxJobs", company.getMaxJobs());
    }
}
