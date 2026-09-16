package com.harshit.gclab;

/**
 * One interview question, answered by code that measures the JVM instead of describing it.
 */
public interface GcLabStep {

    /** Short id used on the command line, e.g. {@code step02}. */
    String id();

    /** The interview question this step answers out loud. */
    String question();

    /** JVM flags this step needs to show its effect (documented in README and run.ps1). */
    String suggestedFlags();

    void run(String[] args) throws Exception;
}
