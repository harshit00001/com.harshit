-- =====================================================
-- DATABASE SCHEMA CREATION
-- =====================================================
-- This file creates all tables needed for SQL examples
-- Run this first to set up the database structure

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;

-- =====================================================
-- LOCATIONS TABLE
-- =====================================================
CREATE TABLE locations (
    location_id INT PRIMARY KEY,
    street_address VARCHAR(100),
    city VARCHAR(50),
    state_province VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(20)
);

-- =====================================================
-- DEPARTMENTS TABLE
-- =====================================================
CREATE TABLE departments (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    location_id INT,
    manager_id INT,
    FOREIGN KEY (location_id) REFERENCES locations(location_id)
);

-- =====================================================
-- EMPLOYEES TABLE
-- =====================================================
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20),
    hire_date DATE,
    job_id VARCHAR(20),
    salary DECIMAL(10, 2),
    commission_pct DECIMAL(4, 2),
    manager_id INT,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id)
);

-- =====================================================
-- CUSTOMERS TABLE
-- =====================================================
CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(200),
    city VARCHAR(50),
    country VARCHAR(50),
    registration_date DATE
);

-- =====================================================
-- PRODUCTS TABLE
-- =====================================================
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2),
    stock_quantity INT,
    supplier_name VARCHAR(100)
);

-- =====================================================
-- ORDERS TABLE
-- =====================================================
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    total_amount DECIMAL(10, 2),
    status VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- =====================================================
-- ORDER_ITEMS TABLE
-- =====================================================
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(10, 2),
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- =====================================================
-- STUDENTS TABLE (for normalization examples)
-- =====================================================
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    enrollment_date DATE
);

-- =====================================================
-- COURSES TABLE (for normalization examples)
-- =====================================================
CREATE TABLE courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    instructor_name VARCHAR(100),
    credits INT
);

-- =====================================================
-- STUDENT_COURSES TABLE (for normalization examples)
-- =====================================================
CREATE TABLE student_courses (
    student_id INT,
    course_id INT,
    enrollment_date DATE,
    grade VARCHAR(2),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================
CREATE INDEX idx_employees_department ON employees(department_id);
CREATE INDEX idx_employees_manager ON employees(manager_id);
CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_departments_location ON departments(location_id);

