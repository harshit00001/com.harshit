package com.harshit.resume.model;

public class TailorResponse {

    private ResumeDocument tailoredResume;
    private AtsReport reportBefore;
    private AtsReport reportAfter;

    public ResumeDocument getTailoredResume() {
        return tailoredResume;
    }

    public void setTailoredResume(ResumeDocument tailoredResume) {
        this.tailoredResume = tailoredResume;
    }

    public AtsReport getReportBefore() {
        return reportBefore;
    }

    public void setReportBefore(AtsReport reportBefore) {
        this.reportBefore = reportBefore;
    }

    public AtsReport getReportAfter() {
        return reportAfter;
    }

    public void setReportAfter(AtsReport reportAfter) {
        this.reportAfter = reportAfter;
    }
}
