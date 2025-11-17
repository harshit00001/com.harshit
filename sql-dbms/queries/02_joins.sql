-- =====================================================
-- SQL JOINS - Complete Guide with Examples
-- =====================================================
-- All examples use the sample database created in schema.sql

-- =====================================================
-- 1. INNER JOIN
-- =====================================================

-- Basic INNER JOIN
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name,
    d.department_name
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id;
-- Returns only employees who have a department
-- AND departments that have employees
-- Excludes employees without departments
-- Excludes departments without employees

-- INNER JOIN with WHERE clause
SELECT 
    e.employee_id,
    e.first_name,
    d.department_name
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id
WHERE e.salary > 50000;
-- First joins tables, then filters results

-- Multiple INNER JOINs
SELECT 
    e.employee_id,
    e.first_name,
    d.department_name,
    l.city
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id
INNER JOIN locations l ON d.location_id = l.location_id;
-- Joins three tables: employees -> departments -> locations

-- INNER JOIN with aggregate
SELECT 
    d.department_name,
    COUNT(e.employee_id) AS employee_count
FROM departments d
INNER JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_name;
-- Counts employees per department (only departments with employees)

-- =====================================================
-- 2. LEFT JOIN (LEFT OUTER JOIN)
-- =====================================================

-- Basic LEFT JOIN
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name,
    d.department_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id;
-- Returns ALL employees
-- Shows department_name if employee has a department
-- Shows NULL for department_name if employee has no department

-- LEFT JOIN to find employees without departments
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id
WHERE d.department_id IS NULL;
-- Returns only employees who don't have a department
-- Uses LEFT JOIN + WHERE IS NULL pattern

-- LEFT JOIN with multiple tables
SELECT 
    e.employee_id,
    e.first_name,
    d.department_name,
    l.city
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id
LEFT JOIN locations l ON d.location_id = l.location_id;
-- All employees, with department and location if available

-- LEFT JOIN with aggregate
SELECT 
    d.department_name,
    COUNT(e.employee_id) AS employee_count
FROM departments d
LEFT JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_name;
-- Shows all departments with employee count (0 if no employees)

-- =====================================================
-- 3. RIGHT JOIN (RIGHT OUTER JOIN)
-- =====================================================

-- Basic RIGHT JOIN
SELECT 
    e.employee_id,
    e.first_name,
    d.department_name
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.department_id;
-- Returns ALL departments
-- Shows employee info if department has employees
-- Shows NULL for employee columns if department has no employees

-- RIGHT JOIN to find departments without employees
SELECT 
    d.department_name
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.department_id
WHERE e.employee_id IS NULL;
-- Returns only departments that have no employees

-- Note: Can achieve same result with LEFT JOIN by swapping tables
SELECT 
    d.department_name
FROM departments d
LEFT JOIN employees e ON d.department_id = e.department_id
WHERE e.employee_id IS NULL;
-- Same result as RIGHT JOIN above

-- =====================================================
-- 4. FULL OUTER JOIN
-- =====================================================

-- FULL OUTER JOIN (PostgreSQL, SQL Server, Oracle)
-- Note: MySQL doesn't support FULL OUTER JOIN directly
SELECT 
    e.employee_id,
    e.first_name,
    d.department_name
FROM employees e
FULL OUTER JOIN departments d ON e.department_id = d.department_id;
-- Returns ALL employees AND ALL departments
-- Shows matches where they exist
-- Shows NULL for missing matches

-- FULL OUTER JOIN in MySQL (workaround using UNION)
SELECT 
    e.employee_id,
    e.first_name,
    d.department_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id

UNION

SELECT 
    e.employee_id,
    e.first_name,
    d.department_name
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.department_id;
-- Simulates FULL OUTER JOIN using UNION

-- =====================================================
-- 5. CROSS JOIN
-- =====================================================

-- CROSS JOIN
SELECT 
    e.first_name,
    d.department_name
FROM employees e
CROSS JOIN departments d;
-- Every employee paired with every department
-- If 15 employees and 6 departments, result has 90 rows
-- Warning: Can produce very large result sets!

-- CROSS JOIN for generating combinations
-- Example: All size-color combinations for products
-- (This is a conceptual example - would need sizes and colors tables)

-- =====================================================
-- 6. SELF JOIN
-- =====================================================

-- SELF JOIN for employee hierarchy
SELECT 
    e.employee_id,
    e.first_name AS employee_name,
    m.first_name AS manager_name
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.employee_id;
-- Shows each employee with their manager
-- Uses LEFT JOIN to include employees without managers

-- SELF JOIN to find employees with same salary
SELECT 
    e1.employee_id AS emp1_id,
    e1.first_name AS emp1_name,
    e2.employee_id AS emp2_id,
    e2.first_name AS emp2_name,
    e1.salary
FROM employees e1
INNER JOIN employees e2 ON e1.salary = e2.salary
WHERE e1.employee_id < e2.employee_id;  -- Avoids duplicate pairs
-- Finds pairs of employees with same salary

-- SELF JOIN for organizational chart
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name,
    m.first_name AS manager_name,
    m.last_name AS manager_last_name
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.employee_id
ORDER BY m.employee_id, e.employee_id;
-- Shows organizational hierarchy

-- =====================================================
-- 7. MULTIPLE TABLE JOINS - Real World Example
-- =====================================================

-- Join customers, orders, and order_items
SELECT 
    c.first_name AS customer_name,
    c.last_name AS customer_last_name,
    o.order_id,
    o.order_date,
    o.total_amount,
    oi.product_id,
    p.product_name,
    oi.quantity,
    oi.subtotal
FROM customers c
INNER JOIN orders o ON c.customer_id = o.customer_id
INNER JOIN order_items oi ON o.order_id = oi.order_id
INNER JOIN products p ON oi.product_id = p.product_id
ORDER BY o.order_date DESC;
-- Complete order details with customer and product information

-- Aggregate with multiple joins
SELECT 
    c.first_name || ' ' || c.last_name AS customer_name,
    COUNT(DISTINCT o.order_id) AS total_orders,
    SUM(o.total_amount) AS total_spent,
    AVG(o.total_amount) AS avg_order_value
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.first_name, c.last_name
ORDER BY total_spent DESC;
-- Customer summary with order statistics

