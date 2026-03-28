package com.harshit.preparation.topic16;

import java.util.List;

/**
 * Topic 16 — Database & JPA / Hibernate.
 */
public final class Topic16Qa {

    private Topic16Qa() {
    }

    /*
     * Q: What is the N+1 problem in Hibernate?
     *
     * SCRIPT:
     * I describe loading a list of parent entities in one query, then triggering one additional query
     * per row when a lazy association is accessed—total 1+N. In production that destroys latency. I fix
     * it with JOIN FETCH or entity graphs in the query, batch fetching, or projections so I fetch only
     * what the API needs.
     *
     * REAL LIFE:
     * Loading 100 orders and then touching order.getCustomer() inside a loop—100 extra queries.
     */

    /*
     * Q: Lazy vs eager fetching?
     *
     * SCRIPT:
     * Lazy means the association loads when first accessed, which needs an open persistence context.
     * Eager loads with the parent, which can explode row counts or pull huge graphs accidentally. I
     * default to lazy associations and fetch explicitly in repository methods when I know what the use case needs.
     */

    /*
     * Q: save() vs saveAndFlush()?
     *
     * SCRIPT:
     * save associates the entity with the persistence context; flush may batch until commit or flush
     * boundary. saveAndFlush forces SQL to the database immediately—useful when I need constraint
     * validation now or when the next step depends on database-generated IDs within the same transaction.
     */

    /*
     * Q: Indexing in SQL?
     *
     * SCRIPT:
     * Indexes speed lookups and range scans but slow writes slightly. Composite indexes help multi-column
     * WHERE clauses when column order matches query patterns. I always verify with EXPLAIN rather than
     * indexing every column blindly.
     */

    /*
     * Q: INNER JOIN vs LEFT JOIN?
     *
     * SCRIPT:
     * Inner join returns only rows that match on both sides. Left join returns all rows from the left
     * table and matching right rows or nulls—useful when the relationship is optional, like customers
     * without orders for a report.
     */

    /** Counts SQL round-trips for N+1 vs one JOIN FETCH–style batch (illustrative). */
    public static void demo() {
        int orderRows = 100;
        int nPlusOneQueries = 1 + orderRows;
        int joinFetchQueries = 1;
        System.out.println("N+1 (1 list + 1 query per row for lazy customer): ~" + nPlusOneQueries + " queries");
        System.out.println("JOIN FETCH / entity graph for same data: ~" + joinFetchQueries + " query");

        List<String> customers = List.of("Ann", "Bob");
        List<String> orders = List.of("o1", "o2");
        System.out.println("INNER join result rows (example): " + innerJoin(customers, orders));
        System.out.println("LEFT join keeps left side even with no match: " + leftJoinWithOptional(customers, List.of()));
    }

    static List<String> innerJoin(List<String> left, List<String> right) {
        List<String> out = new java.util.ArrayList<>();
        for (String a : left) {
            for (String b : right) {
                out.add(a + "|" + b);
            }
        }
        return out;
    }

    static List<String> leftJoinWithOptional(List<String> left, List<String> right) {
        List<String> out = new java.util.ArrayList<>();
        for (String a : left) {
            if (right.isEmpty()) {
                out.add(a + "|null");
            } else {
                for (String b : right) {
                    out.add(a + "|" + b);
                }
            }
        }
        return out;
    }

    public static void main(String[] args) {
        demo();
    }
}
