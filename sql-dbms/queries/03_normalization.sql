-- =====================================================
-- DATABASE NORMALIZATION - Examples and Explanations
-- =====================================================
-- This file demonstrates normalization concepts using examples

-- =====================================================
-- NORMALIZATION OVERVIEW
-- =====================================================
-- Normalization: Process of organizing data to reduce redundancy
-- Normal Forms: 1NF, 2NF, 3NF, BCNF
-- Purpose: Eliminate data duplication, improve integrity, reduce storage

-- =====================================================
-- 1. FIRST NORMAL FORM (1NF)
-- =====================================================
-- Rules:
-- - Each column must contain atomic (indivisible) values
-- - No repeating groups or arrays
-- - Each row must be unique
-- - Each cell must contain a single value

-- Example: Our current design follows 1NF
-- Students table has atomic values
SELECT * FROM students;
-- Each cell contains a single value

-- Student_courses table properly separates courses
SELECT * FROM student_courses;
-- Each row represents one student-course enrollment
-- No multiple values in single cell

-- =====================================================
-- 2. SECOND NORMAL FORM (2NF)
-- =====================================================
-- Rules:
-- - Must be in 1NF
-- - All non-key attributes must be fully functionally dependent on the primary key
-- - No partial dependency (non-key attribute depends on part of composite key)

-- Example: Our student_courses table follows 2NF
SELECT 
    sc.student_id,
    sc.course_id,
    sc.grade,
    s.first_name,
    c.course_name
FROM student_courses sc
JOIN students s ON sc.student_id = s.student_id
JOIN courses c ON sc.course_id = c.course_id;
-- grade depends on entire key (student_id, course_id) - OK
-- course_name is in separate table, depends on course_id - OK
-- This design avoids partial dependencies

-- =====================================================
-- 3. THIRD NORMAL FORM (3NF)
-- =====================================================
-- Rules:
-- - Must be in 2NF
-- - No transitive dependency (non-key attribute depends on another non-key attribute)
-- - All non-key attributes must depend only on the primary key

-- Example: Our employees table follows 3NF
SELECT 
    e.employee_id,
    e.first_name,
    e.department_id,
    d.department_name
FROM employees e
JOIN departments d ON e.department_id = d.department_id;
-- department_name is in departments table, not in employees table
-- This avoids transitive dependency
-- If department_name was in employees table, it would violate 3NF
-- because department_name would depend on department_id, not directly on employee_id

-- =====================================================
-- 4. DENORMALIZATION EXAMPLE
-- =====================================================
-- Sometimes we intentionally denormalize for performance

-- Normalized approach (current design)
SELECT 
    o.order_id,
    o.customer_id,
    c.first_name,
    c.last_name,
    o.total_amount
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id;
-- Requires JOIN to get customer name

-- Denormalized approach (if we added customer_name to orders)
-- This would be faster for reads but requires updates in multiple places
-- Example of what denormalized might look like:
/*
CREATE TABLE orders_denormalized (
    order_id INT PRIMARY KEY,
    customer_id INT,
    customer_name VARCHAR(100),  -- Denormalized: stored here for faster reads
    order_date DATE,
    total_amount DECIMAL(10, 2)
);
-- No JOIN needed, but if customer name changes, must update here too
*/

-- =====================================================
-- 5. NORMALIZATION CHECK QUERIES
-- =====================================================

-- Check for duplicate data (should be minimal in normalized design)
SELECT 
    department_id,
    COUNT(*) AS employee_count
FROM employees
GROUP BY department_id;
-- Each employee appears once per department (proper normalization)

-- Check referential integrity
SELECT 
    e.employee_id,
    e.department_id,
    d.department_id AS dept_exists
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id
WHERE d.department_id IS NULL;
-- Should return no rows if referential integrity is maintained

-- Check for transitive dependencies (should not exist in 3NF)
-- If we had department_name in employees table, this would show the issue:
/*
SELECT 
    e.employee_id,
    e.department_id,
    e.department_name,  -- This would violate 3NF
    d.department_name AS dept_name_from_dept_table
FROM employees e
JOIN departments d ON e.department_id = d.department_id
WHERE e.department_name != d.department_name;
-- Would show inconsistencies if department_name was stored in both places
*/

