-- =====================================================
-- DATABASE VIEWS - Complete Guide
-- =====================================================

-- =====================================================
-- VIEW OVERVIEW
-- =====================================================
-- View: Virtual table based on result of a SQL query
-- Types: Simple View, Complex View, Materialized View
-- Benefits: Security, Simplicity, Consistency

-- =====================================================
-- 1. SIMPLE VIEW
-- =====================================================

-- Create a simple view
CREATE OR REPLACE VIEW employee_basic_info AS
SELECT 
    employee_id,
    first_name,
    last_name,
    email,
    department_id
FROM employees;

-- Query the view
SELECT * FROM employee_basic_info;
-- Works like a regular table

-- View with WHERE clause
SELECT * FROM employee_basic_info
WHERE department_id = 10;
-- Can use WHERE with views

-- =====================================================
-- 2. COMPLEX VIEW (with JOINs)
-- =====================================================

-- Create view with JOIN
CREATE OR REPLACE VIEW employee_department_view AS
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name,
    e.salary,
    d.department_name,
    l.city
FROM employees e
JOIN departments d ON e.department_id = d.department_id
LEFT JOIN locations l ON d.location_id = l.location_id;

-- Query the complex view
SELECT * FROM employee_department_view
WHERE salary > 50000
ORDER BY salary DESC;
-- Hides complexity of multiple JOINs

-- =====================================================
-- 3. VIEW WITH AGGREGATION
-- =====================================================

-- Create view with GROUP BY
CREATE OR REPLACE VIEW department_summary AS
SELECT 
    d.department_id,
    d.department_name,
    COUNT(e.employee_id) AS employee_count,
    AVG(e.salary) AS avg_salary,
    MAX(e.salary) AS max_salary,
    MIN(e.salary) AS min_salary,
    SUM(e.salary) AS total_payroll
FROM departments d
LEFT JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_id, d.department_name;

-- Query the aggregated view
SELECT * FROM department_summary
ORDER BY total_payroll DESC;
-- Pre-calculated statistics, faster queries

-- =====================================================
-- 4. VIEW FOR SECURITY (Column-level security)
-- =====================================================

-- Create view that hides sensitive data
CREATE OR REPLACE VIEW public_employee_info AS
SELECT 
    employee_id,
    first_name,
    last_name,
    department_id
    -- Note: salary, email, etc. are NOT included
FROM employees;

-- Users can query this view without seeing sensitive data
SELECT * FROM public_employee_info;

-- =====================================================
-- 5. VIEW WITH CALCULATED COLUMNS
-- =====================================================

-- Create view with calculations
CREATE OR REPLACE VIEW employee_compensation AS
SELECT 
    employee_id,
    first_name,
    last_name,
    salary,
    salary * 12 AS annual_salary,
    salary * 0.1 AS bonus,
    salary * 12.1 AS total_compensation
FROM employees;

-- Query the view
SELECT * FROM employee_compensation
ORDER BY total_compensation DESC;
-- Calculations are done automatically

-- =====================================================
-- 6. VIEW FOR DATA TRANSFORMATION
-- =====================================================

-- Create view that transforms data
CREATE OR REPLACE VIEW customer_order_summary AS
SELECT 
    c.customer_id,
    c.first_name || ' ' || c.last_name AS full_name,
    COUNT(o.order_id) AS total_orders,
    SUM(o.total_amount) AS total_spent,
    AVG(o.total_amount) AS avg_order_value,
    MAX(o.order_date) AS last_order_date
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.first_name, c.last_name;

-- Query the transformed view
SELECT * FROM customer_order_summary
WHERE total_orders > 0
ORDER BY total_spent DESC;
-- Clean, transformed data ready for reporting

-- =====================================================
-- 7. UPDATABLE VIEW
-- =====================================================

-- Simple updatable view (no JOINs, no aggregation)
CREATE OR REPLACE VIEW updatable_employees AS
SELECT 
    employee_id,
    first_name,
    last_name,
    salary,
    department_id
FROM employees
WHERE department_id IS NOT NULL;

-- Can update through view
-- UPDATE updatable_employees SET salary = 80000 WHERE employee_id = 101;
-- Note: Updates affect underlying table

-- =====================================================
-- 8. VIEW MANAGEMENT
-- =====================================================

-- List all views
-- SELECT * FROM information_schema.views WHERE table_schema = 'your_schema';

-- View view definition
-- SHOW CREATE VIEW employee_department_view;

-- Drop a view
-- DROP VIEW IF EXISTS employee_basic_info;

-- =====================================================
-- 9. MATERIALIZED VIEW (Concept - syntax varies by DB)
-- =====================================================

-- Materialized View: Stores query results physically
-- Faster than regular views but requires refresh
-- Syntax varies by database:

-- PostgreSQL:
-- CREATE MATERIALIZED VIEW department_stats AS
-- SELECT 
--     department_id,
--     COUNT(*) AS emp_count,
--     AVG(salary) AS avg_sal
-- FROM employees
-- GROUP BY department_id;
-- 
-- REFRESH MATERIALIZED VIEW department_stats;

-- Oracle:
-- CREATE MATERIALIZED VIEW department_stats AS
-- SELECT department_id, COUNT(*) AS emp_count
-- FROM employees
-- GROUP BY department_id;

-- =====================================================
-- 10. VIEW BEST PRACTICES
-- =====================================================

-- Use views for:
-- 1. Simplifying complex queries
-- 2. Security (hiding sensitive columns)
-- 3. Consistency (standardized data access)
-- 4. Abstraction (hide table structure changes)

-- Query examples using views:
SELECT * FROM employee_department_view WHERE salary > 70000;
SELECT * FROM department_summary WHERE employee_count > 2;
SELECT * FROM customer_order_summary ORDER BY total_spent DESC LIMIT 5;

