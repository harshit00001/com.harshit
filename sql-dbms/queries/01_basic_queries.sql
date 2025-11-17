-- =====================================================
-- BASIC SQL QUERIES
-- =====================================================
-- This file contains fundamental SQL queries with examples
-- All queries use the sample database created in schema.sql

-- =====================================================
-- 1. SELECT STATEMENT
-- =====================================================

-- Select all columns from a table
SELECT * FROM employees;
-- Returns all columns and all rows from employees table

-- Select specific columns
SELECT employee_id, first_name, last_name, salary
FROM employees;
-- Returns only specified columns from employees table

-- Select with column aliases
SELECT 
    employee_id AS id,
    first_name AS "First Name",
    salary * 12 AS annual_salary
FROM employees;
-- AS keyword is used to rename columns in the result set
-- Useful for making column names more readable

-- Select distinct values
SELECT DISTINCT department_id
FROM employees;
-- Returns only unique values, removes duplicates
-- Useful when you want to see unique values in a column

-- Select with expressions
SELECT 
    first_name,
    salary,
    salary * 0.1 AS bonus,
    salary + (salary * 0.1) AS total_compensation
FROM employees;
-- Can perform calculations in SELECT clause

-- =====================================================
-- 2. WHERE CLAUSE
-- =====================================================

-- Simple condition
SELECT * FROM employees
WHERE salary > 50000;
-- Returns employees with salary greater than 50000

-- Multiple conditions with AND
SELECT * FROM employees
WHERE department_id = 10 AND salary > 50000;
-- Returns employees in department 10 with salary > 50000
-- Both conditions must be true

-- Multiple conditions with OR
SELECT * FROM employees
WHERE department_id = 10 OR department_id = 20;
-- Returns employees in either department 10 or 20
-- At least one condition must be true

-- IN operator
SELECT * FROM employees
WHERE department_id IN (10, 20, 30);
-- Returns employees in any of the specified departments
-- Equivalent to: department_id = 10 OR department_id = 20 OR department_id = 30

-- BETWEEN operator
SELECT * FROM employees
WHERE salary BETWEEN 40000 AND 60000;
-- Returns employees with salary between 40000 and 60000 (inclusive)
-- Equivalent to: salary >= 40000 AND salary <= 60000

-- LIKE operator (pattern matching)
SELECT * FROM employees
WHERE first_name LIKE 'J%';
-- Returns employees whose first name starts with 'J'
-- % matches any sequence of characters
-- _ matches a single character

SELECT * FROM employees
WHERE email LIKE '%@email.com';
-- Returns employees with email ending in @email.com

-- IS NULL / IS NOT NULL
SELECT * FROM employees
WHERE manager_id IS NULL;
-- Returns employees who don't have a manager (NULL values)

SELECT * FROM employees
WHERE manager_id IS NOT NULL;
-- Returns employees who have a manager

-- NOT operator
SELECT * FROM employees
WHERE department_id NOT IN (10, 20);
-- Returns employees NOT in departments 10 or 20

-- =====================================================
-- 3. ORDER BY CLAUSE
-- =====================================================

-- Sort by single column (ascending)
SELECT * FROM employees
ORDER BY salary;
-- Sorts employees by salary in ascending order (lowest to highest)

-- Sort by single column (descending)
SELECT * FROM employees
ORDER BY salary DESC;
-- Sorts employees by salary in descending order (highest to lowest)

-- Sort by multiple columns
SELECT * FROM employees
ORDER BY department_id ASC, salary DESC;
-- First sorts by department_id (ascending)
-- Then sorts by salary (descending) within each department

-- Sort by column alias
SELECT 
    first_name,
    salary * 12 AS annual_salary
FROM employees
ORDER BY annual_salary DESC;
-- Can use column alias in ORDER BY

-- =====================================================
-- 4. GROUP BY CLAUSE
-- =====================================================

-- Simple GROUP BY
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id;
-- Groups employees by department and counts how many in each department

-- GROUP BY with multiple columns
SELECT department_id, job_id, COUNT(*) AS count
FROM employees
GROUP BY department_id, job_id;
-- Groups by both department and job, counts employees in each combination

-- GROUP BY with aggregate functions
SELECT 
    department_id,
    COUNT(*) AS total_employees,
    AVG(salary) AS avg_salary,
    MAX(salary) AS max_salary,
    MIN(salary) AS min_salary,
    SUM(salary) AS total_salary
FROM employees
GROUP BY department_id;
-- Calculates various statistics for each department

-- GROUP BY with WHERE
SELECT department_id, AVG(salary) AS avg_salary
FROM employees
WHERE salary > 30000
GROUP BY department_id;
-- First filters rows (WHERE), then groups them (GROUP BY)
-- WHERE is applied before grouping

-- =====================================================
-- 5. HAVING CLAUSE
-- =====================================================

-- HAVING with aggregate function
SELECT department_id, COUNT(*) AS employee_count
FROM employees
GROUP BY department_id
HAVING COUNT(*) > 2;
-- Only shows departments with more than 2 employees
-- HAVING filters groups, not individual rows

-- HAVING with multiple conditions
SELECT department_id, AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 50000 AND COUNT(*) > 3;
-- Shows departments with average salary > 50000 AND more than 3 employees

-- WHERE vs HAVING
SELECT department_id, AVG(salary) AS avg_salary
FROM employees
WHERE salary > 30000  -- Filters rows before grouping
GROUP BY department_id
HAVING AVG(salary) > 50000;  -- Filters groups after grouping
-- WHERE: filters individual employees with salary > 30000
-- HAVING: filters departments with average salary > 50000

-- =====================================================
-- 6. AGGREGATE FUNCTIONS
-- =====================================================

-- COUNT
SELECT COUNT(*) FROM employees;
-- Counts total number of rows (including NULLs)

SELECT COUNT(employee_id) FROM employees;
-- Counts non-NULL values in employee_id column

SELECT COUNT(DISTINCT department_id) FROM employees;
-- Counts distinct department IDs

-- SUM
SELECT SUM(salary) AS total_payroll
FROM employees;
-- Calculates sum of all salaries

SELECT department_id, SUM(salary) AS dept_total
FROM employees
GROUP BY department_id;
-- Sum of salaries for each department

-- AVG (Average)
SELECT AVG(salary) AS avg_salary
FROM employees;
-- Calculates average salary

SELECT department_id, AVG(salary) AS avg_dept_salary
FROM employees
GROUP BY department_id;
-- Average salary for each department

-- MAX and MIN
SELECT 
    MAX(salary) AS highest_salary,
    MIN(salary) AS lowest_salary
FROM employees;
-- Finds maximum and minimum salary

SELECT 
    department_id,
    MAX(salary) AS max_salary,
    MIN(salary) AS min_salary
FROM employees
GROUP BY department_id;
-- Max and min salary for each department

-- Multiple aggregate functions
SELECT 
    COUNT(*) AS total_employees,
    SUM(salary) AS total_payroll,
    AVG(salary) AS avg_salary,
    MAX(salary) AS max_salary,
    MIN(salary) AS min_salary
FROM employees;
-- Calculates multiple statistics in one query

-- =====================================================
-- 7. LIMIT / TOP (varies by database)
-- =====================================================

-- MySQL/PostgreSQL: LIMIT
SELECT * FROM employees
ORDER BY salary DESC
LIMIT 5;
-- Returns top 5 employees by salary

-- Limit with offset (pagination)
SELECT * FROM employees
ORDER BY employee_id
LIMIT 10 OFFSET 20;
-- Returns 10 rows starting from row 21 (skips first 20 rows)
-- Useful for pagination: page 3 with 10 items per page

-- SQL Server: TOP
-- SELECT TOP 10 * FROM employees;
-- SELECT TOP 10 PERCENT * FROM employees;

-- Oracle: ROWNUM
-- SELECT * FROM employees WHERE ROWNUM <= 10;

