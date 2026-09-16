package com.harshit.jobpulse.config;

/** Politeness and scheduling knobs for the crawler. */
public class CrawlConfig {

    private boolean scheduleEnabled = true;

    /** Crawl on startup, so a fresh clone shows data without waiting for the cron. */
    private boolean runOnStartup = true;

    /** Pause between upstream requests for the same company. */
    private long requestDelayMillis = 400;

    private int connectTimeoutSeconds = 15;
    private int readTimeoutSeconds = 40;
    private int maxRetries = 2;

    private String userAgent =
            "JobPulse/1.0 (personal job-search aggregator; contact: rajharshit453@gmail.com)";

    /**
     * Trust store to validate HTTPS certificates against.
     *
     * <p>{@code Windows-ROOT} makes the JVM use the Windows certificate store, which is what
     * corporate networks with TLS inspection require — the proxy's CA is trusted by Windows
     * but not by the JDK's bundled {@code cacerts}. The value is ignored when the type is
     * unavailable (Linux containers), where the JDK default is used instead.
     */
    private String trustStoreType = "Windows-ROOT";

    public boolean isScheduleEnabled() {
        return scheduleEnabled;
    }

    public void setScheduleEnabled(boolean scheduleEnabled) {
        this.scheduleEnabled = scheduleEnabled;
    }

    public boolean isRunOnStartup() {
        return runOnStartup;
    }

    public void setRunOnStartup(boolean runOnStartup) {
        this.runOnStartup = runOnStartup;
    }

    public long getRequestDelayMillis() {
        return requestDelayMillis;
    }

    public void setRequestDelayMillis(long requestDelayMillis) {
        this.requestDelayMillis = requestDelayMillis;
    }

    public int getConnectTimeoutSeconds() {
        return connectTimeoutSeconds;
    }

    public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
    }

    public int getReadTimeoutSeconds() {
        return readTimeoutSeconds;
    }

    public void setReadTimeoutSeconds(int readTimeoutSeconds) {
        this.readTimeoutSeconds = readTimeoutSeconds;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }

    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }
}
