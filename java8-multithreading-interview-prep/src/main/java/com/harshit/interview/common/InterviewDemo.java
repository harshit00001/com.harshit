package com.harshit.interview.common;

/**
 * Contract for every runnable interview demo in this project.
 */
@FunctionalInterface
public interface InterviewDemo {

    void run() throws Exception;

    default String title() {
        return getClass().getSimpleName();
    }
}
