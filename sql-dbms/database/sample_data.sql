-- =====================================================
-- SAMPLE DATA INSERTION
-- =====================================================
-- This file inserts sample data into all tables
-- Run this after schema.sql to populate the database

-- =====================================================
-- CLEAR EXISTING DATA (if any)
-- =====================================================
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM products;
DELETE FROM customers;
DELETE FROM student_courses;
DELETE FROM courses;
DELETE FROM students;
DELETE FROM employees;
DELETE FROM departments;
DELETE FROM locations;

-- =====================================================
-- INSERT INTO LOCATIONS
-- =====================================================
INSERT INTO locations (location_id, street_address, city, state_province, country, postal_code) VALUES
(100, '123 Main Street', 'New York', 'NY', 'USA', '10001'),
(200, '456 Oak Avenue', 'Los Angeles', 'CA', 'USA', '90001'),
(300, '789 Pine Road', 'Chicago', 'IL', 'USA', '60601'),
(400, '321 Elm Street', 'Houston', 'TX', 'USA', '77001'),
(500, '654 Maple Drive', 'Phoenix', 'AZ', 'USA', '85001');

-- =====================================================
-- INSERT INTO DEPARTMENTS
-- =====================================================
INSERT INTO departments (department_id, department_name, location_id, manager_id) VALUES
(10, 'Engineering', 100, NULL),
(20, 'Marketing', 200, NULL),
(30, 'Sales', 300, NULL),
(40, 'HR', 400, NULL),
(50, 'Finance', 500, NULL),
(60, 'IT Support', 100, NULL);

-- =====================================================
-- INSERT INTO EMPLOYEES
-- =====================================================
INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id) VALUES
(101, 'John', 'Smith', 'john.smith@email.com', '555-0101', TO_DATE('2020-01-15', 'YYYY-MM-DD'), 'ENG001', 75000.00, NULL, NULL, 10),
(102, 'Jane', 'Doe', 'jane.doe@email.com', '555-0102', TO_DATE('2020-03-20', 'YYYY-MM-DD'), 'ENG002', 80000.00, NULL, 101, 10),
(103, 'Michael', 'Johnson', 'michael.j@email.com', '555-0103', TO_DATE('2019-06-10', 'YYYY-MM-DD'), 'MGR001', 95000.00, NULL, NULL, 20),
(104, 'Emily', 'Williams', 'emily.w@email.com', '555-0104', TO_DATE('2021-02-14', 'YYYY-MM-DD'), 'MKT001', 65000.00, NULL, 103, 20),
(105, 'David', 'Brown', 'david.brown@email.com', '555-0105', TO_DATE('2018-11-05', 'YYYY-MM-DD'), 'SAL001', 70000.00, 0.05, NULL, 30),
(106, 'Sarah', 'Davis', 'sarah.d@email.com', '555-0106', TO_DATE('2020-07-22', 'YYYY-MM-DD'), 'SAL002', 72000.00, 0.06, 105, 30),
(107, 'Robert', 'Miller', 'robert.m@email.com', '555-0107', TO_DATE('2019-09-30', 'YYYY-MM-DD'), 'HR001', 68000.00, NULL, NULL, 40),
(108, 'Lisa', 'Wilson', 'lisa.w@email.com', '555-0108', TO_DATE('2021-01-08', 'YYYY-MM-DD'), 'HR002', 62000.00, NULL, 107, 40),
(109, 'James', 'Moore', 'james.m@email.com', '555-0109', TO_DATE('2020-05-12', 'YYYY-MM-DD'), 'FIN001', 78000.00, NULL, NULL, 50),
(110, 'Patricia', 'Taylor', 'patricia.t@email.com', '555-0110', TO_DATE('2021-04-18', 'YYYY-MM-DD'), 'FIN002', 73000.00, NULL, 109, 50),
(111, 'William', 'Anderson', 'william.a@email.com', '555-0111', TO_DATE('2019-12-03', 'YYYY-MM-DD'), 'IT001', 70000.00, NULL, NULL, 60),
(112, 'Jennifer', 'Thomas', 'jennifer.t@email.com', '555-0112', TO_DATE('2020-08-25', 'YYYY-MM-DD'), 'IT002', 68000.00, NULL, 111, 60),
(113, 'Richard', 'Jackson', 'richard.j@email.com', '555-0113', TO_DATE('2021-03-15', 'YYYY-MM-DD'), 'ENG003', 77000.00, NULL, 101, 10),
(114, 'Mary', 'White', 'mary.w@email.com', '555-0114', TO_DATE('2020-11-20', 'YYYY-MM-DD'), 'MKT002', 64000.00, NULL, 103, 20),
(115, 'Joseph', 'Harris', 'joseph.h@email.com', '555-0115', TO_DATE('2018-04-07', 'YYYY-MM-DD'), 'SAL003', 69000.00, 0.04, 105, 30);

-- Update manager_id in departments
UPDATE departments SET manager_id = 101 WHERE department_id = 10;
UPDATE departments SET manager_id = 103 WHERE department_id = 20;
UPDATE departments SET manager_id = 105 WHERE department_id = 30;
UPDATE departments SET manager_id = 107 WHERE department_id = 40;
UPDATE departments SET manager_id = 109 WHERE department_id = 50;
UPDATE departments SET manager_id = 111 WHERE department_id = 60;

-- =====================================================
-- INSERT INTO CUSTOMERS
-- =====================================================
INSERT INTO customers (customer_id, first_name, last_name, email, phone, address, city, country, registration_date) VALUES
(1001, 'Alice', 'Johnson', 'alice.j@email.com', '555-1001', '100 First St', 'New York', 'USA', TO_DATE('2022-01-10', 'YYYY-MM-DD')),
(1002, 'Bob', 'Smith', 'bob.smith@email.com', '555-1002', '200 Second Ave', 'Los Angeles', 'USA', TO_DATE('2022-02-15', 'YYYY-MM-DD')),
(1003, 'Carol', 'Williams', 'carol.w@email.com', '555-1003', '300 Third Blvd', 'Chicago', 'USA', TO_DATE('2022-03-20', 'YYYY-MM-DD')),
(1004, 'Daniel', 'Brown', 'daniel.b@email.com', '555-1004', '400 Fourth Rd', 'Houston', 'USA', TO_DATE('2022-04-25', 'YYYY-MM-DD')),
(1005, 'Eva', 'Davis', 'eva.d@email.com', '555-1005', '500 Fifth St', 'Phoenix', 'USA', TO_DATE('2022-05-30', 'YYYY-MM-DD')),
(1006, 'Frank', 'Miller', 'frank.m@email.com', '555-1006', '600 Sixth Ave', 'New York', 'USA', TO_DATE('2022-06-05', 'YYYY-MM-DD')),
(1007, 'Grace', 'Wilson', 'grace.w@email.com', '555-1007', '700 Seventh Blvd', 'Los Angeles', 'USA', TO_DATE('2022-07-10', 'YYYY-MM-DD')),
(1008, 'Henry', 'Moore', 'henry.m@email.com', '555-1008', '800 Eighth Rd', 'Chicago', 'USA', TO_DATE('2022-08-15', 'YYYY-MM-DD'));

-- =====================================================
-- INSERT INTO PRODUCTS
-- =====================================================
INSERT INTO products (product_id, product_name, category, price, stock_quantity, supplier_name) VALUES
(2001, 'Laptop Pro', 'Electronics', 1299.99, 50, 'TechSupplier Inc'),
(2002, 'Wireless Mouse', 'Electronics', 29.99, 200, 'TechSupplier Inc'),
(2003, 'Mechanical Keyboard', 'Electronics', 89.99, 150, 'TechSupplier Inc'),
(2004, 'Office Chair', 'Furniture', 299.99, 75, 'FurnitureCo'),
(2005, 'Desk Lamp', 'Furniture', 49.99, 120, 'FurnitureCo'),
(2006, 'Notebook Set', 'Stationery', 19.99, 300, 'PaperWorks'),
(2007, 'Pen Set', 'Stationery', 12.99, 500, 'PaperWorks'),
(2008, 'Monitor 27"', 'Electronics', 399.99, 80, 'TechSupplier Inc'),
(2009, 'Webcam HD', 'Electronics', 79.99, 100, 'TechSupplier Inc'),
(2010, 'Headphones', 'Electronics', 149.99, 90, 'TechSupplier Inc');

-- =====================================================
-- INSERT INTO ORDERS
-- =====================================================
INSERT INTO orders (order_id, customer_id, order_date, total_amount, status) VALUES
(3001, 1001, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 1329.98, 'Delivered'),
(3002, 1002, TO_DATE('2023-01-20', 'YYYY-MM-DD'), 89.99, 'Delivered'),
(3003, 1003, TO_DATE('2023-02-05', 'YYYY-MM-DD'), 349.98, 'Processing'),
(3004, 1001, TO_DATE('2023-02-10', 'YYYY-MM-DD'), 49.99, 'Delivered'),
(3005, 1004, TO_DATE('2023-02-15', 'YYYY-MM-DD'), 1429.98, 'Shipped'),
(3006, 1005, TO_DATE('2023-03-01', 'YYYY-MM-DD'), 32.98, 'Delivered'),
(3007, 1002, TO_DATE('2023-03-05', 'YYYY-MM-DD'), 479.98, 'Processing'),
(3008, 1006, TO_DATE('2023-03-10', 'YYYY-MM-DD'), 299.99, 'Delivered'),
(3009, 1007, TO_DATE('2023-03-15', 'YYYY-MM-DD'), 1299.99, 'Shipped'),
(3010, 1003, TO_DATE('2023-03-20', 'YYYY-MM-DD'), 229.98, 'Delivered'),
(3011, 1008, TO_DATE('2023-04-01', 'YYYY-MM-DD'), 79.99, 'Processing'),
(3012, 1001, TO_DATE('2023-04-05', 'YYYY-MM-DD'), 149.99, 'Delivered');

-- =====================================================
-- INSERT INTO ORDER_ITEMS
-- =====================================================
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, unit_price, subtotal) VALUES
(4001, 3001, 2001, 1, 1299.99, 1299.99),
(4002, 3001, 2002, 1, 29.99, 29.99),
(4003, 3002, 2003, 1, 89.99, 89.99),
(4004, 3003, 2004, 1, 299.99, 299.99),
(4005, 3003, 2005, 1, 49.99, 49.99),
(4006, 3004, 2005, 1, 49.99, 49.99),
(4007, 3005, 2001, 1, 1299.99, 1299.99),
(4008, 3005, 2008, 1, 129.99, 129.99),
(4009, 3006, 2006, 1, 19.99, 19.99),
(4010, 3006, 2007, 1, 12.99, 12.99),
(4011, 3007, 2008, 1, 399.99, 399.99),
(4012, 3007, 2002, 1, 29.99, 29.99),
(4013, 3007, 2003, 1, 49.99, 49.99),
(4014, 3008, 2004, 1, 299.99, 299.99),
(4015, 3009, 2001, 1, 1299.99, 1299.99),
(4016, 3010, 2009, 1, 79.99, 79.99),
(4017, 3010, 2010, 1, 149.99, 149.99),
(4018, 3011, 2009, 1, 79.99, 79.99),
(4019, 3012, 2010, 1, 149.99, 149.99);

-- =====================================================
-- INSERT INTO STUDENTS
-- =====================================================
INSERT INTO students (student_id, first_name, last_name, email, enrollment_date) VALUES
(5001, 'Alex', 'Martinez', 'alex.m@university.edu', TO_DATE('2023-01-10', 'YYYY-MM-DD')),
(5002, 'Bella', 'Garcia', 'bella.g@university.edu', TO_DATE('2023-01-10', 'YYYY-MM-DD')),
(5003, 'Charlie', 'Rodriguez', 'charlie.r@university.edu', TO_DATE('2023-01-10', 'YYYY-MM-DD')),
(5004, 'Diana', 'Lopez', 'diana.l@university.edu', TO_DATE('2023-01-10', 'YYYY-MM-DD')),
(5005, 'Edward', 'Gonzalez', 'edward.g@university.edu', TO_DATE('2023-01-10', 'YYYY-MM-DD'));

-- =====================================================
-- INSERT INTO COURSES
-- =====================================================
INSERT INTO courses (course_id, course_name, instructor_name, credits) VALUES
(6001, 'Database Systems', 'Dr. Smith', 3),
(6002, 'Data Structures', 'Dr. Johnson', 3),
(6003, 'Algorithms', 'Dr. Williams', 4),
(6004, 'Software Engineering', 'Dr. Brown', 3),
(6005, 'Computer Networks', 'Dr. Davis', 3);

-- =====================================================
-- INSERT INTO STUDENT_COURSES
-- =====================================================
INSERT INTO student_courses (student_id, course_id, enrollment_date, grade) VALUES
(5001, 6001, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'A'),
(5001, 6002, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'B'),
(5001, 6003, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'A'),
(5002, 6001, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'B'),
(5002, 6004, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'A'),
(5003, 6002, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'C'),
(5003, 6003, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'B'),
(5003, 6005, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'A'),
(5004, 6001, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'A'),
(5004, 6004, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'B'),
(5005, 6003, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'A'),
(5005, 6005, TO_DATE('2023-01-15', 'YYYY-MM-DD'), 'B');

