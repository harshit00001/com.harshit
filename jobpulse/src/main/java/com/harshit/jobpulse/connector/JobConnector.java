package com.harshit.jobpulse.connector;

import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.ConnectorType;

import java.util.List;

/** Strategy for turning one company's career page into normalised postings. */
public interface JobConnector {

    ConnectorType type();

    List<RawJob> fetch(CompanyConfig company);
}
