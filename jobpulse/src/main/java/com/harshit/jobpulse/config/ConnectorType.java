package com.harshit.jobpulse.config;

/**
 * Fetch strategy for a career page. Adding a company means picking one of these
 * and supplying its options in configuration — no code changes required.
 */
public enum ConnectorType {

    /** Workday CXS JSON API (Accenture, Salesforce, many enterprises). */
    WORKDAY,

    /** Ashby public job board API (Tekion and other scale-ups). */
    ASHBY,

    /** Greenhouse public boards API. */
    GREENHOUSE,

    /** Lever public postings API. */
    LEVER,

    /** Server-rendered HTML parsed with configurable CSS selectors. */
    HTML
}
