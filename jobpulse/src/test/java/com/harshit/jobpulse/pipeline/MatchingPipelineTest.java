package com.harshit.jobpulse.pipeline;

import com.harshit.jobpulse.config.JobPulseProperties;
import com.harshit.jobpulse.connector.RawJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchingPipelineTest {

    private JobPulseProperties properties;
    private SkillExtractor skillExtractor;
    private MatchScorer matchScorer;
    private LocationFilter locationFilter;

    @BeforeEach
    void setUp() {
        properties = new JobPulseProperties();

        properties.getProfile().setMustHaveSkills(
                List.of("Java", "Spring Boot", "Microservices", "SQL"));
        properties.getProfile().setNiceToHaveSkills(
                List.of("Docker", "Kubernetes", "Kafka"));
        properties.getProfile().setTargetRoles(
                List.of("java developer", "backend engineer"));
        properties.getProfile().setExcludeTitleKeywords(List.of("intern", "fresher"));
        properties.getProfile().setMinScore(55);
        properties.getProfile().setMinMustHaveMatches(2);
        properties.getProfile().setAliases(Map.of(
                "Java", List.of("java", "core java"),
                "Spring Boot", List.of("spring boot", "springboot"),
                "Microservices", List.of("microservices", "microservice"),
                "SQL", List.of("sql", "plsql"),
                "Kubernetes", List.of("kubernetes", "k8s")));

        properties.getLocationFilter().setEnabled(true);
        properties.getLocationFilter().setCountries(List.of("India"));
        properties.getLocationFilter().setCities(List.of("pune", "bengaluru", "kochi"));
        properties.getLocationFilter().setBlockedCountries(List.of("United States", "Mexico"));
        properties.getLocationFilter().setKeepUnknownLocations(false);

        skillExtractor = new SkillExtractor(properties);
        matchScorer = new MatchScorer(properties, skillExtractor);
        locationFilter = new LocationFilter(properties);
    }

    @Test
    void acceptsPostingWhenFeedReportsIndia() {
        RawJob job = new RawJob();
        job.setCountry("India");
        job.setLocationText("Kochi");

        assertTrue(locationFilter.evaluate(job).accepted());
    }

    @Test
    void acceptsPostingWhenOnlyCityIsKnown() {
        RawJob job = new RawJob();
        job.setLocationText("Pune, Maharashtra");

        LocationFilter.Decision decision = locationFilter.evaluate(job);
        assertTrue(decision.accepted());
        assertEquals("city=pune", decision.reason());
    }

    @Test
    void rejectsPostingOutsideIndia() {
        RawJob job = new RawJob();
        job.setCountry("Mexico");
        job.setLocationText("Mexico City");

        assertFalse(locationFilter.evaluate(job).accepted());
    }

    @Test
    void rejectsPostingWithUnresolvableLocation() {
        RawJob job = new RawJob();
        job.setLocationText("Virtual - Anywhere");

        assertFalse(locationFilter.evaluate(job).accepted());
    }

    @Test
    void doesNotTreatJavaScriptAsJava() {
        Set<String> found = skillExtractor.extract(
                "Strong JavaScript and TypeScript experience required", List.of("Java"));

        assertTrue(found.isEmpty());
    }

    @Test
    void detectsSkillAliases() {
        Set<String> found = skillExtractor.extract(
                "Hands-on with SpringBoot, k8s and PLSQL", List.of("Spring Boot", "Kubernetes", "SQL"));

        assertEquals(Set.of("Spring Boot", "Kubernetes", "SQL"), found);
    }

    @Test
    void scoresRelevantIndiaBackendRoleAsMatched() {
        RawJob job = new RawJob();
        job.setTitle("Java Developer");
        job.setLocationText("Pune, India");
        job.setPublishedAt(Instant.now().minus(3, ChronoUnit.DAYS));
        job.setDescription("Build Spring Boot microservices with SQL, Docker and Kubernetes.");

        MatchResult result = matchScorer.score(job);

        assertTrue(result.matched(), "expected a match, breakdown: " + result.breakdown());
        assertTrue(result.score() >= 85, "unexpectedly low score: " + result.score());
        assertTrue(result.matchedSkills().contains("Java"));
    }

    @Test
    void rejectsInternshipEvenWhenSkillsMatch() {
        RawJob job = new RawJob();
        job.setTitle("Java Developer Intern");
        job.setDescription("Spring Boot microservices, SQL, Kubernetes");

        MatchResult result = matchScorer.score(job);

        assertFalse(result.matched());
    }

    @Test
    void reportsMissingMustHaveSkills()  {
        RawJob job = new RawJob();
        job.setTitle("Backend Engineer");
        job.setDescription("Golang services backed by SQL databases");

        MatchResult result = matchScorer.score(job);

        assertTrue(result.missingMustHaveSkills().contains("Java"));
        assertFalse(result.matched());
    }
}
