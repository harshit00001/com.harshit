package com.harshit.jobpulse.repo;

import com.harshit.jobpulse.domain.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Optional<JobPosting> findByCompanyIdAndExternalId(String companyId, String externalId);

    long countByMatchedTrue();

    List<JobPosting> findTop50ByOrderByMatchScoreDescLastSeenAtDesc();

    /**
     * Free-text plus facet search.
     *
     * <p>{@code companyId}, {@code city} and {@code query} must be supplied already
     * lower-cased: Hibernate 6 refuses {@code lower(:param)} on an untyped parameter.
     */
    @Query("""
            select job from JobPosting job
            where (:companyId is null or lower(job.companyId) = :companyId)
              and (:city is null or lower(job.city) like concat('%', :city, '%'))
              and job.matchScore >= :minScore
              and (:matchedOnly is null or job.matched = :matchedOnly)
              and (:query is null
                   or lower(job.title) like concat('%', :query, '%')
                   or lower(job.descriptionText) like concat('%', :query, '%'))
              and (:since is null or job.firstSeenAt >= :since)
            """)
    Page<JobPosting> search(@Param("companyId") String companyId,
                           @Param("city") String city,
                           @Param("minScore") int minScore,
                           @Param("matchedOnly") Boolean matchedOnly,
                           @Param("query") String query,
                           @Param("since") Instant since,
                           Pageable pageable);

    @Query("select distinct job.city from JobPosting job where job.city is not null order by job.city")
    List<String> findDistinctCities();

    @Query("select job.companyName, count(job) from JobPosting job group by job.companyName")
    List<Object[]> countByCompany();
}
