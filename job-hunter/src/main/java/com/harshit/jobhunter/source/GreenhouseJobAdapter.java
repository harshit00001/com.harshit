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
 * Greenhouse public job board API — add board-token in application.yml per company.
 */
@Component
public class GreenhouseJobAdapter implements JobSourceAdapter {

    private static final Logger log = LoggerFactory.getLogger(GreenhouseJobAdapter.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GreenhouseJobAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.create();
    }

    @Override
    public boolean supports(JobHunterProperties.Source source) {
        return "greenhouse".equalsIgnoreCase(source.getType());
    }

    @Override
    public List<JobPosting> fetch(JobHunterProperties.Source source) {
        List<JobPosting> jobs = new ArrayList<>();
        String token = source.getBoardToken();
        if (token == null || token.isBlank()) {
            return jobs;
        }
        String url = "https://boards-api.greenhouse.io/v1/boards/" + token + "/jobs?content=true";
        try {
            String body = restClient.get().uri(url).retrieve().body(String.class);
            JsonNode root = objectMapper.readTree(body);
            JsonNode list = root.path("jobs");
            if (!list.isArray()) {
                return jobs;
            }
            String company = source.getName() != null ? source.getName() : token;
            for (JsonNode node : list) {
                String id = node.path("id").asText("");
                String title = node.path("title").asText("");
                String location = node.path("location").path("name").asText("Unknown");
                String applyUrl = node.path("absolute_url").asText("");
                String description = node.path("content").asText("");
                if (id.isBlank() || applyUrl.isBlank()) {
                    continue;
                }
                jobs.add(new JobPosting("greenhouse:" + token, id, company, title, description, location, applyUrl, Instant.now()));
            }
        } catch (Exception e) {
            log.warn("Greenhouse fetch failed for {}: {}", token, e.getMessage());
        }
        return jobs;
    }
}
