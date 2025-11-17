-- =====================================================
-- TRANSACTIONS AND ACID PROPERTIES
-- =====================================================

-- =====================================================
-- TRANSACTION OVERVIEW
-- =====================================================
-- Transaction: Sequence of database operations executed as single unit
-- Either all operations succeed (COMMIT) or all fail (ROLLBACK)
-- Ensures data consistency and integrity

-- =====================================================
-- 1. BASIC TRANSACTION SYNTAX
-- =====================================================

-- Start transaction
BEGIN;
-- or
START TRANSACTION;

-- Perform operations
UPDATE employees SET salary = 80000 WHERE employee_id = 101;
INSERT INTO orders (order_id, customer_id, order_date, total_amount, status)
VALUES (3013, 1001, '2023-04-10', 199.99, 'Processing');

-- Commit transaction (make changes permanent)
COMMIT;

-- Or rollback transaction (undo all changes)
-- ROLLBACK;

-- =====================================================
-- 2. ACID PROPERTIES
-- =====================================================

-- =====================================================
-- A. ATOMICITY
-- =====================================================
-- All operations in transaction succeed or all fail
-- No partial execution

-- Example: Transfer money between accounts
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE account_id = 1;
UPDATE accounts SET balance = balance + 100 WHERE account_id = 2;
-- Both updates must succeed or both must fail
COMMIT;

-- If error occurs:
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE account_id = 1;
-- Error occurs here
UPDATE accounts SET balance = balance + 100 WHERE account_id = 2;
ROLLBACK;  -- Undo first update

-- =====================================================
-- B. CONSISTENCY
-- =====================================================
-- Database remains in consistent state before and after transaction
-- All constraints and rules are maintained

-- Example: Maintain referential integrity
BEGIN;
INSERT INTO orders (order_id, customer_id, order_date, total_amount, status)
VALUES (3014, 9999, '2023-04-15', 99.99, 'Processing');
-- This will fail if customer_id 9999 doesn't exist (foreign key constraint)
-- Database remains consistent
COMMIT;

-- =====================================================
-- C. ISOLATION
-- =====================================================
-- Concurrent transactions don't interfere with each other
-- Each transaction sees consistent snapshot of data

-- Transaction Isolation Levels:
-- 1. READ UNCOMMITTED (lowest isolation, fastest)
-- 2. READ COMMITTED (default in most databases)
-- 3. REPEATABLE READ
-- 4. SERIALIZABLE (highest isolation, slowest)

-- Set isolation level
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

-- Example: Two concurrent transactions
-- Transaction 1:
BEGIN;
SELECT salary FROM employees WHERE employee_id = 101;
-- Reads salary = 75000

-- Transaction 2 (concurrent):
BEGIN;
UPDATE employees SET salary = 80000 WHERE employee_id = 101;
COMMIT;

-- Transaction 1 (continues):
SELECT salary FROM employees WHERE employee_id = 101;
-- With READ COMMITTED: sees 80000 (committed value)
-- With REPEATABLE READ: sees 75000 (same as first read)
COMMIT;

-- =====================================================
-- D. DURABILITY
-- =====================================================
-- Once transaction is committed, changes are permanent
-- Survives system crashes, power failures, etc.
-- Achieved through write-ahead logging (WAL) and database backups

-- Example:
BEGIN;
UPDATE employees SET salary = 90000 WHERE employee_id = 101;
COMMIT;
-- After COMMIT, even if database crashes, salary = 90000 is preserved

-- =====================================================
-- 3. TRANSACTION EXAMPLES
-- =====================================================

-- Example 1: Order processing
BEGIN;
-- Create order
INSERT INTO orders (order_id, customer_id, order_date, total_amount, status)
VALUES (3015, 1001, CURRENT_DATE, 299.99, 'Processing');

-- Add order items
INSERT INTO order_items (order_item_id, order_id, product_id, quantity, unit_price, subtotal)
VALUES (4020, 3015, 2004, 1, 299.99, 299.99);

-- Update product stock
UPDATE products SET stock_quantity = stock_quantity - 1 WHERE product_id = 2004;

-- All operations succeed
COMMIT;

-- If any operation fails, ROLLBACK undoes all changes

-- =====================================================
-- Example 2: Employee promotion
BEGIN;
-- Update employee salary
UPDATE employees SET salary = salary * 1.1 WHERE employee_id = 102;

-- Update employee job
UPDATE employees SET job_id = 'SEN001' WHERE employee_id = 102;

-- Log promotion
-- INSERT INTO promotion_log (employee_id, old_salary, new_salary, date)
-- VALUES (102, 80000, 88000, CURRENT_DATE);

COMMIT;

-- =====================================================
-- 4. SAVEPOINTS
-- =====================================================

-- Create savepoint (partial rollback point)
BEGIN;
UPDATE employees SET salary = 85000 WHERE employee_id = 101;
SAVEPOINT sp1;

UPDATE employees SET salary = 90000 WHERE employee_id = 102;
SAVEPOINT sp2;

UPDATE employees SET salary = 95000 WHERE employee_id = 103;

-- Rollback to savepoint (undo only last update)
ROLLBACK TO SAVEPOINT sp2;
-- Now: emp 101 = 85000, emp 102 = 90000, emp 103 = original

-- Rollback to earlier savepoint
ROLLBACK TO SAVEPOINT sp1;
-- Now: emp 101 = 85000, emp 102 = original, emp 103 = original

COMMIT;  -- Commits remaining changes

-- =====================================================
-- 5. TRANSACTION BEST PRACTICES
-- =====================================================

-- 1. Keep transactions short
-- Long transactions hold locks longer, reduce concurrency

-- 2. Commit frequently
-- Don't leave transactions open unnecessarily

-- 3. Handle errors properly
BEGIN;
-- operations
-- IF error THEN
--     ROLLBACK;
-- ELSE
--     COMMIT;
-- END IF;

-- 4. Use appropriate isolation level
-- Higher isolation = more consistency but less concurrency

-- 5. Avoid long-running transactions
-- Can cause deadlocks and performance issues

-- =====================================================
-- 6. DEADLOCKS
-- =====================================================

-- Deadlock: Two transactions waiting for each other's locks
-- Database automatically detects and rolls back one transaction

-- Example of deadlock:
-- Transaction 1:
BEGIN;
UPDATE employees SET salary = 80000 WHERE employee_id = 101;
-- Holds lock on employee 101

-- Transaction 2 (concurrent):
BEGIN;
UPDATE employees SET salary = 90000 WHERE employee_id = 102;
-- Holds lock on employee 102

-- Transaction 1 tries to update employee 102 (waits for lock)
UPDATE employees SET salary = 85000 WHERE employee_id = 102;
-- Waits...

-- Transaction 2 tries to update employee 101 (waits for lock)
UPDATE employees SET salary = 95000 WHERE employee_id = 101;
-- DEADLOCK! Database rolls back one transaction

-- Prevention:
-- 1. Always acquire locks in same order
-- 2. Keep transactions short
-- 3. Use lower isolation level when possible

-- =====================================================
-- 7. AUTOCOMMIT
-- =====================================================

-- Most databases have autocommit enabled by default
-- Each statement is automatically committed

-- Disable autocommit
SET AUTOCOMMIT = 0;  -- MySQL
-- or
SET autocommit = false;  -- PostgreSQL

-- Enable autocommit
SET AUTOCOMMIT = 1;  -- MySQL
-- or
SET autocommit = true;  -- PostgreSQL

-- With autocommit OFF, must explicitly COMMIT or ROLLBACK

