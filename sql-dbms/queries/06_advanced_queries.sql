-- =====================================================
-- ADVANCED SQL QUERIES
-- =====================================================
-- Subqueries, Window Functions, CTEs, and more

-- =====================================================
-- 1. SUBQUERIES
-- =====================================================

-- =====================================================
-- A. SCALAR SUBQUERY (returns single value)
-- =====================================================

-- Find employees earning more than average salary
SELECT 
    employee_id,
    first_name,
    last_name,
    salary
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
-- Subquery returns average salary, used in WHERE clause

-- Find employees in department with highest average salary
SELECT 
    employee_id,
    first_name,
    salary,
    department_id
FROM employees
WHERE department_id = (
    SELECT department_id
    FROM employees
    GROUP BY department_id
    ORDER BY AVG(salary) DESC
    LIMIT 1
);

-- =====================================================
-- B. ROW SUBQUERY (returns single row)
-- =====================================================

-- Find employee with highest salary
SELECT 
    employee_id,
    first_name,
    last_name,
    salary
FROM employees
WHERE (salary, employee_id) = (
    SELECT MAX(salary), MIN(employee_id)
    FROM employees
);

-- =====================================================
-- C. COLUMN SUBQUERY (returns single column)
-- =====================================================

-- Find employees in departments that have more than 3 employees
SELECT 
    employee_id,
    first_name,
    department_id
FROM employees
WHERE department_id IN (
    SELECT department_id
    FROM employees
    GROUP BY department_id
    HAVING COUNT(*) > 3
);
-- Subquery returns list of department_ids

-- =====================================================
-- D. TABLE SUBQUERY (returns table)
-- =====================================================

-- Use subquery as table in FROM clause
SELECT 
    dept_stats.department_id,
    dept_stats.avg_salary,
    d.department_name
FROM (
    SELECT 
        department_id,
        AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
) AS dept_stats
JOIN departments d ON dept_stats.department_id = d.department_id
ORDER BY avg_salary DESC;

-- =====================================================
-- E. CORRELATED SUBQUERY
-- =====================================================

-- Find employees earning more than their department average
SELECT 
    e1.employee_id,
    e1.first_name,
    e1.salary,
    e1.department_id
FROM employees e1
WHERE e1.salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.department_id = e1.department_id
);
-- Subquery references outer query (e1.department_id)
-- Executed once for each row in outer query

-- Find employees who have placed orders
SELECT 
    employee_id,
    first_name,
    last_name
FROM employees e
WHERE EXISTS (
    SELECT 1
    FROM orders o
    JOIN customers c ON o.customer_id = c.customer_id
    WHERE c.first_name = e.first_name
);
-- EXISTS returns true if subquery returns any rows

-- =====================================================
-- 2. COMMON TABLE EXPRESSIONS (CTEs)
-- =====================================================

-- Simple CTE
WITH high_salary_employees AS (
    SELECT 
        employee_id,
        first_name,
        last_name,
        salary
    FROM employees
    WHERE salary > 70000
)
SELECT * FROM high_salary_employees
ORDER BY salary DESC;
-- CTE acts like a temporary view for the query

-- Multiple CTEs
WITH 
department_stats AS (
    SELECT 
        department_id,
        COUNT(*) AS emp_count,
        AVG(salary) AS avg_salary
    FROM employees
    GROUP BY department_id
),
high_avg_depts AS (
    SELECT department_id
    FROM department_stats
    WHERE avg_salary > 70000
)
SELECT 
    e.employee_id,
    e.first_name,
    e.salary,
    d.department_name
FROM employees e
JOIN high_avg_depts h ON e.department_id = h.department_id
JOIN departments d ON e.department_id = d.department_id;

-- Recursive CTE (for hierarchical data)
WITH RECURSIVE employee_hierarchy AS (
    -- Anchor: Top-level managers
    SELECT 
        employee_id,
        first_name,
        last_name,
        manager_id,
        0 AS level
    FROM employees
    WHERE manager_id IS NULL
    
    UNION ALL
    
    -- Recursive: Employees reporting to managers
    SELECT 
        e.employee_id,
        e.first_name,
        e.last_name,
        e.manager_id,
        eh.level + 1
    FROM employees e
    JOIN employee_hierarchy eh ON e.manager_id = eh.employee_id
)
SELECT * FROM employee_hierarchy
ORDER BY level, employee_id;
-- Note: Recursive CTEs syntax varies by database

-- =====================================================
-- 3. WINDOW FUNCTIONS
-- =====================================================

-- =====================================================
-- A. ROW_NUMBER()
-- =====================================================

-- Rank employees by salary within each department
SELECT 
    employee_id,
    first_name,
    salary,
    department_id,
    ROW_NUMBER() OVER (
        PARTITION BY department_id 
        ORDER BY salary DESC
    ) AS salary_rank
FROM employees;
-- Assigns unique sequential number to each row within partition

-- =====================================================
-- B. RANK() and DENSE_RANK()
-- =====================================================

-- Rank employees by salary (with ties)
SELECT 
    employee_id,
    first_name,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS rank_with_gaps,
    DENSE_RANK() OVER (ORDER BY salary DESC) AS rank_no_gaps
FROM employees;
-- RANK: Leaves gaps (1, 2, 2, 4)
-- DENSE_RANK: No gaps (1, 2, 2, 3)

-- =====================================================
-- C. LAG() and LEAD()
-- =====================================================

-- Compare employee salary with previous and next
SELECT 
    employee_id,
    first_name,
    salary,
    LAG(salary) OVER (ORDER BY salary) AS previous_salary,
    LEAD(salary) OVER (ORDER BY salary) AS next_salary
FROM employees;
-- LAG: Previous row value
-- LEAD: Next row value

-- =====================================================
-- D. SUM() OVER (Running Total)
-- =====================================================

-- Calculate running total of salaries
SELECT 
    employee_id,
    first_name,
    salary,
    SUM(salary) OVER (
        ORDER BY employee_id
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total
FROM employees;

-- Calculate cumulative salary by department
SELECT 
    employee_id,
    first_name,
    salary,
    department_id,
    SUM(salary) OVER (
        PARTITION BY department_id
        ORDER BY employee_id
    ) AS dept_running_total
FROM employees;

-- =====================================================
-- E. AVG() OVER (Moving Average)
-- =====================================================

-- Calculate average salary within department
SELECT 
    employee_id,
    first_name,
    salary,
    department_id,
    AVG(salary) OVER (PARTITION BY department_id) AS dept_avg_salary,
    salary - AVG(salary) OVER (PARTITION BY department_id) AS diff_from_avg
FROM employees;

-- =====================================================
-- F. FIRST_VALUE() and LAST_VALUE()
-- =====================================================

-- Get highest and lowest salary in department
SELECT 
    employee_id,
    first_name,
    salary,
    department_id,
    FIRST_VALUE(salary) OVER (
        PARTITION BY department_id 
        ORDER BY salary DESC
    ) AS highest_in_dept,
    LAST_VALUE(salary) OVER (
        PARTITION BY department_id 
        ORDER BY salary DESC
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS lowest_in_dept
FROM employees;

-- =====================================================
-- 4. CASE EXPRESSIONS
-- =====================================================

-- Simple CASE
SELECT 
    employee_id,
    first_name,
    salary,
    CASE 
        WHEN salary > 80000 THEN 'High'
        WHEN salary > 60000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_category
FROM employees;

-- CASE in aggregate
SELECT 
    department_id,
    COUNT(*) AS total_employees,
    SUM(CASE WHEN salary > 70000 THEN 1 ELSE 0 END) AS high_earners,
    AVG(CASE WHEN salary > 70000 THEN salary ELSE NULL END) AS avg_high_salary
FROM employees
GROUP BY department_id;

-- =====================================================
-- 5. UNION, INTERSECT, EXCEPT
-- =====================================================

-- UNION (combines results, removes duplicates)
SELECT first_name, last_name FROM employees
UNION
SELECT first_name, last_name FROM customers;
-- Combines employee and customer names, removes duplicates

-- UNION ALL (keeps duplicates)
SELECT first_name, last_name FROM employees
UNION ALL
SELECT first_name, last_name FROM customers;
-- Keeps all rows including duplicates

-- INTERSECT (common rows)
-- SELECT first_name FROM employees
-- INTERSECT
-- SELECT first_name FROM customers;
-- Returns names that exist in both tables

-- EXCEPT (rows in first but not second)
-- SELECT first_name FROM employees
-- EXCEPT
-- SELECT first_name FROM customers;
-- Returns employee names not in customers

-- =====================================================
-- 6. PIVOT (Concept - syntax varies by DB)
-- =====================================================

-- Pivot: Convert rows to columns
-- Example: Show department salaries as columns

-- PostgreSQL/MySQL approach using CASE
SELECT 
    SUM(CASE WHEN department_id = 10 THEN salary ELSE 0 END) AS dept_10_total,
    SUM(CASE WHEN department_id = 20 THEN salary ELSE 0 END) AS dept_20_total,
    SUM(CASE WHEN department_id = 30 THEN salary ELSE 0 END) AS dept_30_total
FROM employees;

-- SQL Server PIVOT syntax (conceptual)
/*
SELECT * FROM (
    SELECT department_id, salary
    FROM employees
) AS source
PIVOT (
    SUM(salary)
    FOR department_id IN ([10], [20], [30])
) AS pivot_table;
*/

