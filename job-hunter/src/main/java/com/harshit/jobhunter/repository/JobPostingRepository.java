package com.harshit.jobhunter.repository;

import com.harshit.jobhunter.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Optional<JobPosting> findBySourceAndExternalId(String source, String externalId);
}
