package com.harshit;

/**
 * Entry point — prints available interview demos.
 *
 * <p>Run individual classes to study one topic at a time:
 * {@code mvn exec:java -Dexec.mainClass=com.harshit.map.HashMapInternalsDemo}
 */
public final class Main {

    private Main() {}

    public static void main(String[] args) {
        System.out.println("""
                ============================================================
                  Java Collections Interview Prep  |  package: com.harshit
                ============================================================

                Run any demo class (each has its own main method):

                  com.harshit.framework.CollectionsHierarchyDemo
                  com.harshit.list.ArrayListVsLinkedListDemo
                  com.harshit.map.HashMapInternalsDemo
                  com.harshit.map.HashMapEqualsHashCodeDemo
                  com.harshit.map.LruCacheDemo
                  com.harshit.set.SetVariantsDemo
                  com.harshit.concurrent.ConcurrentCollectionsDemo
                  com.harshit.iteration.FailFastVsFailSafeDemo
                  com.harshit.sorting.ComparableVsComparatorDemo
                  com.harshit.queue.QueueAndDequeDemo
                  com.harshit.scenarios.InterviewScenariosDemo

                Example:
                  mvn exec:java -Dexec.mainClass=com.harshit.map.HashMapInternalsDemo
                """);
    }
}
