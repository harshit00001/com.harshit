package com.harshit.jobhunter.source;

import com.harshit.jobhunter.config.JobHunterProperties;
import com.harshit.jobhunter.model.JobPosting;

import java.util.List;

public interface JobSourceAdapter {

    boolean supports(JobHunterProperties.Source source);

    List<JobPosting> fetch(JobHunterProperties.Source source);
}
