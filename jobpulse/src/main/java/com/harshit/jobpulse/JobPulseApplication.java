package com.harshit.jobpulse;

import com.harshit.jobpulse.config.JobPulseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(JobPulseProperties.class)
public class JobPulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobPulseApplication.class, args);
    }
}
