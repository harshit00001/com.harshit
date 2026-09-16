package com.harshit.jobpulse.api;

import com.harshit.jobpulse.domain.JobPosting;
import com.harshit.jobpulse.repo.JobPostingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobSearchController {

    private static final int SNIPPET_LENGTH = 320;

    private final JobPostingRepository repository;

    public JobSearchController(JobPostingRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/search")
    public Map<String, Object> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String skills,
            @RequestParam(defaultValue = "0") int minScore,
            @RequestParam(defaultValue = "false") boolean matchedOnly,
            @RequestParam(required = false) Integer sinceDays,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Instant since = sinceDays == null
                ? null
                : Instant.now().minus(sinceDays, ChronoUnit.DAYS);

        Sort sort = Sort.by(Sort.Direction.DESC, "matchScore")
                .and(Sort.by(Sort.Direction.DESC, "firstSeenAt"));

        Page<JobPosting> results = repository.search(
                lowerOrNull(company),
                lowerOrNull(city),
                minScore,
                matchedOnly ? Boolean.TRUE : null,
                lowerOrNull(q),
                since,
                PageRequest.of(page, Math.min(size, 100), sort));

        List<String> requiredSkills = parseSkills(skills);
        List<JobView> views = results.getContent().stream()
                .filter(posting -> hasAllSkills(posting, requiredSkills))
                .map(posting -> JobView.of(posting, SNIPPET_LENGTH))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", results.getNumber());
        response.put("size", results.getSize());
        response.put("totalElements", results.getTotalElements());
        response.put("totalPages", results.getTotalPages());
        response.put("jobs", views);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> byId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/top")
    public List<JobView> top() {
        return repository.findTop50ByOrderByMatchScoreDescLastSeenAtDesc().stream()
                .map(posting -> JobView.of(posting, SNIPPET_LENGTH))
                .toList();
    }

    @GetMapping("/cities")
    public List<String> cities() {
        return repository.findDistinctCities();
    }

    private List<String> parseSkills(String skills) {
        if (skills == null || skills.isBlank()) {
            return List.of();
        }
        return List.of(skills.toLowerCase(Locale.ROOT).split("\\s*,\\s*"));
    }

    private boolean hasAllSkills(JobPosting posting, List<String> requiredSkills) {
        if (requiredSkills.isEmpty()) {
            return true;
        }
        String matched = posting.getMatchedSkills() == null
                ? ""
                : posting.getMatchedSkills().toLowerCase(Locale.ROOT);
        return requiredSkills.stream().allMatch(matched::contains);
    }

    private String lowerOrNull(String value) {
        return value == null || value.isBlank() ? null : value.toLowerCase(Locale.ROOT).trim();
    }
}
