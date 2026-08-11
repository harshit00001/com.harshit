package com.harshit.jobhunter.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.jobhunter.config.JobHunterProperties;
import com.harshit.jobhunter.model.JobPosting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Public Remotive API — good default source for software-dev roles with apply links.
 */
@Component
public class RemotiveJobAdapter implements JobSourceAdapter {

    private static final Logger log = LoggerFactory.getLogger(RemotiveJobAdapter.class);
    private static final String URL = "https://remotive.com/api/remote-jobs?category=software-dev";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public RemotiveJobAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
    }

    @Override
    public boolean supports(JobHunterProperties.Source source) {
        return "remotive".equalsIgnoreCase(source.getType());
    }

    @Override
    public List<JobPosting> fetch(JobHunterProperties.Source source) {
        List<JobPosting> jobs = new ArrayList<>();
        try {
            String body = restClient.get().uri(URL).retrieve().body(String.class);
            JsonNode root = objectMapper.readTree(body);
            JsonNode list = root.path("jobs");
            if (!list.isArray()) {
                return jobs;
            }
            for (JsonNode node : list) {
                String title = node.path("title").asText("");
                String company = node.path("company_name").asText(source.getName());
                String description = node.path("description").asText("");
                String location = node.path("candidate_required_location").asText("Remote");
                String applyUrl = node.path("url").asText("");
                String id = node.path("id").asText("");
                if (applyUrl.isBlank() || id.isBlank()) {
                    continue;
                }
                Instant posted = parseDate(node.path("publication_date").asText(null));
                jobs.add(new JobPosting("remotive", id, company, title, description, location, applyUrl, posted));
            }
        } catch (Exception e) {
            log.warn("Remotive fetch failed: {}", e.getMessage());
        }
        return jobs;
    }

    private Instant parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return Instant.now();
        }
        try {
            return Instant.parse(raw);
        } catch (Exception e) {
            return Instant.now();
        }
    }
}
