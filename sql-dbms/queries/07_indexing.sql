-- =====================================================
-- DATABASE INDEXING - Complete Guide
-- =====================================================

-- =====================================================
-- INDEX OVERVIEW
-- =====================================================
-- Index: Data structure that improves speed of data retrieval
-- Types: B-Tree, Hash, Bitmap, Composite
-- Trade-off: Faster reads, slower writes, additional storage

-- =====================================================
-- 1. CREATING INDEXES
-- =====================================================

-- Single column index
CREATE INDEX idx_employee_email ON employees(email);
-- Speeds up queries filtering or joining on email

-- Composite index (multiple columns)
CREATE INDEX idx_emp_dept_salary ON employees(department_id, salary);
-- Useful for queries filtering on both columns
-- Order matters: leftmost columns are most important

-- Unique index
CREATE UNIQUE INDEX idx_customer_email ON customers(email);
-- Ensures uniqueness and speeds up lookups

-- Partial index (index subset of rows)
CREATE INDEX idx_high_salary ON employees(salary) 
WHERE salary > 70000;
-- Only indexes employees with salary > 70000
-- Useful when querying only high-salary employees

-- =====================================================
-- 2. INDEX TYPES
-- =====================================================

-- B-Tree Index (default, most common)
CREATE INDEX idx_employee_name ON employees(last_name);
-- Balanced tree structure
-- Good for range queries, equality, sorting
-- Works with: =, <, >, <=, >=, BETWEEN, LIKE 'prefix%'

-- Hash Index (PostgreSQL, MySQL InnoDB)
-- CREATE INDEX idx_employee_id_hash ON employees USING HASH(employee_id);
-- Very fast for equality (=) queries
-- Not good for range queries or sorting
-- Syntax varies by database

-- =====================================================
-- 3. WHEN TO CREATE INDEXES
-- =====================================================

-- Good candidates for indexing:
-- 1. Foreign keys (for JOIN performance)
CREATE INDEX idx_emp_dept_fk ON employees(department_id);
-- Already created in schema.sql

-- 2. Columns frequently used in WHERE clause
CREATE INDEX idx_emp_salary ON employees(salary);
-- If you often filter by salary

-- 3. Columns used in ORDER BY
CREATE INDEX idx_emp_hire_date ON employees(hire_date);
-- Speeds up sorting by hire_date

-- 4. Columns used in JOIN conditions
CREATE INDEX idx_order_customer ON orders(customer_id);
-- Already created in schema.sql

-- =====================================================
-- 4. INDEX USAGE EXAMPLES
-- =====================================================

-- Query that uses index on email
SELECT * FROM employees 
WHERE email = 'john.smith@email.com';
-- Index idx_employee_email speeds this up

-- Query that uses composite index
SELECT * FROM employees
WHERE department_id = 10 AND salary > 50000;
-- Can use idx_emp_dept_salary index

-- Query that uses index for sorting
SELECT * FROM employees
ORDER BY hire_date;
-- Index idx_emp_hire_date speeds up sorting

-- =====================================================
-- 5. INDEX MAINTENANCE
-- =====================================================

-- Check index usage (PostgreSQL)
-- SELECT * FROM pg_stat_user_indexes;

-- Rebuild index (if fragmented)
-- REINDEX INDEX idx_employee_email;

-- Analyze table (update statistics)
-- ANALYZE employees;
-- Helps query optimizer choose best index

-- =====================================================
-- 6. INDEX BEST PRACTICES
-- =====================================================

-- DO:
-- 1. Index foreign keys
-- 2. Index frequently queried columns
-- 3. Index columns used in WHERE, JOIN, ORDER BY
-- 4. Use composite indexes for multi-column queries
-- 5. Monitor index usage and remove unused indexes

-- DON'T:
-- 1. Over-index (too many indexes slow down writes)
-- 2. Index columns with low cardinality (few unique values)
-- 3. Index columns rarely used in queries
-- 4. Index columns that are frequently updated

-- =====================================================
-- 7. EXPLAIN PLAN (Query Execution Plan)
-- =====================================================

-- Check if index is being used
-- PostgreSQL:
-- EXPLAIN SELECT * FROM employees WHERE email = 'john.smith@email.com';

-- MySQL:
-- EXPLAIN SELECT * FROM employees WHERE email = 'john.smith@email.com';

-- SQL Server:
-- SET SHOWPLAN_ALL ON;
-- SELECT * FROM employees WHERE email = 'john.smith@email.com';

-- Look for "Index Scan" or "Index Seek" in plan
-- "Seq Scan" or "Table Scan" means index not used

-- =====================================================
-- 8. COVERING INDEX
-- =====================================================

-- Index that contains all columns needed for query
-- Query can be satisfied entirely from index (no table access)

-- Example: If we often query employee_id and salary together
CREATE INDEX idx_emp_id_salary ON employees(employee_id, salary);

-- Query that uses covering index
SELECT employee_id, salary FROM employees
WHERE employee_id BETWEEN 101 AND 110;
-- Can be satisfied entirely from index

-- =====================================================
-- 9. INDEX ON EXPRESSIONS
-- =====================================================

-- Index on calculated/transformed column
CREATE INDEX idx_emp_name_lower ON employees(LOWER(first_name));
-- Useful for case-insensitive searches

-- Query using expression index
SELECT * FROM employees
WHERE LOWER(first_name) = 'john';
-- Can use idx_emp_name_lower index

-- =====================================================
-- 10. DROP INDEXES
-- =====================================================

-- Remove unused index
-- DROP INDEX IF EXISTS idx_employee_email;

-- Check index size before dropping
-- SELECT pg_size_pretty(pg_relation_size('idx_employee_email'));

