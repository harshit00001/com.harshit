package com.harshit.sqldbms.extra;

/**
 * SQL / relational database interview Q&A — under sql-dbms (SQL scripts live in {@code queries/}).
 *
 * <p><b>Q. Normalization?</b> Organize tables to reduce redundancy/dependency anomalies (1NF→2NF→3NF…).
 * Real life: separate Customers and Orders instead of repeating customer address on every order row.
 *
 * <p><b>Q. Joins?</b> INNER: matching rows both sides; LEFT: all left + matches; RIGHT: all right + matches;
 * FULL: union of both. Real life: orders INNER JOIN customers to print names.
 *
 * <p><b>Q. WHERE vs HAVING?</b> WHERE filters rows before grouping; HAVING filters groups after GROUP BY.
 * Real life: HAVING COUNT(*) > 5 for popular products.
 *
 * <p><b>Q. Indexing?</b> B-tree structure speeding lookups; trade faster SELECT for slower INSERT/UPDATE.
 * Real life: index on email for login lookups.
 *
 * <p><b>Q. Primary vs foreign key?</b> PK uniquely identifies row; FK references PK in another table for integrity.
 * Real life: order_line.order_id → orders.id.
 *
 * <p><b>Q. ACID?</b> Atomicity (all-or-nothing), Consistency (valid state), Isolation (concurrent txs don’t corrupt),
 * Durability (committed survives crashes). Real life: money transfer debit+credit in one transaction.
 *
 * <p><b>Q. Transaction?</b> Unit of work bounded by BEGIN/COMMIT/ROLLBACK with ACID guarantees.
 *
 * <p>Example SQL strings (run against your schema — see module {@code queries/} for more).
 */
public final class SqlDatabaseInterviewQA {

    private SqlDatabaseInterviewQA() {
    }

    /** Nth highest salary — replace :n with a number (e.g., 2 for second highest). */
    static final String NTH_HIGHEST_SALARY =
            "SELECT DISTINCT salary FROM employees e1 "
                    + "WHERE :n = (SELECT COUNT(DISTINCT salary) FROM employees e2 WHERE e2.salary >= e1.salary);";

    /** Duplicate rows by business key (here: email). */
    static final String DUPLICATE_EMAILS =
            "SELECT email, COUNT(*) AS cnt FROM customers GROUP BY email HAVING COUNT(*) > 1;";

    /** Group by example: revenue per region. */
    static final String GROUP_BY_REVENUE =
            "SELECT region, SUM(amount) AS revenue FROM orders GROUP BY region ORDER BY revenue DESC;";

    public static void main(String[] args) {
        System.out.println("Nth highest salary pattern:\n" + NTH_HIGHEST_SALARY);
        System.out.println("\nDuplicate detection:\n" + DUPLICATE_EMAILS);
        System.out.println("\nGroup by:\n" + GROUP_BY_REVENUE);
        System.out.println("\nTip: open sql-dbms/queries/*.sql for runnable scripts with sample data.");
    }
}
