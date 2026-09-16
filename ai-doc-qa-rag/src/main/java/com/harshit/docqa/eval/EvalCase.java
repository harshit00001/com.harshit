package com.harshit.docqa.eval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * One labelled test question. A null or blank {@code expectedSource} marks an out-of-scope question
 * that the service is expected to refuse.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EvalCase(String question, String expectedSource, String note) {
}
