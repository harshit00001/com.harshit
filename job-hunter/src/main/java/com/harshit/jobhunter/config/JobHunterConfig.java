package com.harshit.jobhunter.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JobHunterProperties.class)
public class JobHunterConfig {
}
