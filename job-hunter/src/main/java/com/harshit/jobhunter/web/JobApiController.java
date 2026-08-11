package com.harshit.jobhunter.web;

import com.harshit.jobhunter.model.JobMatchResult;
import com.harshit.jobhunter.service.JobIngestionService;
import com.harshit.jobhunter.service.JobSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobApiController {

    private final JobSearchService searchService;
    private final JobIngestionService ingestionService;

    public JobApiController(JobSearchService searchService, JobIngestionService ingestionService) {
        this.searchService = searchService;
        this.ingestionService = ingestionService;
    }

    @GetMapping("/recommended")
    public List<JobMatchResult> recommended(
            @RequestParam(defaultValue = "14") int days,
            @RequestParam(defaultValue = "3") int minScore,
            @RequestParam(defaultValue = "30") int limit) {
        return searchService.recommended(days, minScore, limit);
    }

    @PostMapping("/search")
    public List<JobMatchResult> search(@RequestBody SearchRequest request) {
        int days = request.days() != null ? request.days() : 14;
        int limit = request.limit() != null ? request.limit() : 20;
        return searchService.searchByQuery(request.query(), days, limit);
    }

    @PostMapping("/fetch")
    public Map<String, Object> fetchNow() {
        int saved = ingestionService.fetchAll();
        return Map.of("message", "Fetch completed", "newJobsSaved", saved);
    }

    public record SearchRequest(String query, Integer days, Integer limit) {
    }
}
