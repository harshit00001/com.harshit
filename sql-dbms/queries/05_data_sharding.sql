-- =====================================================
-- DATA SHARDING - Concepts and Examples
-- =====================================================

-- =====================================================
-- DATA SHARDING OVERVIEW
-- =====================================================
-- Sharding: Horizontal partitioning of data across multiple databases/servers
-- Purpose: Distribute load, improve performance, enable horizontal scaling
-- Each shard contains a subset of data

-- =====================================================
-- 1. SHARDING STRATEGIES
-- =====================================================

-- =====================================================
-- A. RANGE-BASED SHARDING
-- =====================================================
-- Shard data based on ranges of a key value
-- Example: Shard customers by customer_id ranges

-- Shard 1: customer_id 1-1000
-- CREATE TABLE customers_shard1 (
--     customer_id INT PRIMARY KEY CHECK (customer_id BETWEEN 1 AND 1000),
--     first_name VARCHAR(50),
--     last_name VARCHAR(50),
--     ...
-- );

-- Shard 2: customer_id 1001-2000
-- CREATE TABLE customers_shard2 (
--     customer_id INT PRIMARY KEY CHECK (customer_id BETWEEN 1001 AND 2000),
--     first_name VARCHAR(50),
--     last_name VARCHAR(50),
--     ...
-- );

-- Query specific shard
-- SELECT * FROM customers_shard1 WHERE customer_id = 500;
-- SELECT * FROM customers_shard2 WHERE customer_id = 1500;

-- =====================================================
-- B. HASH-BASED SHARDING
-- =====================================================
-- Shard data based on hash of a key value
-- Example: Shard by hash of customer_id

-- Shard selection function (conceptual)
-- shard_number = customer_id % number_of_shards

-- Example with 3 shards:
-- customer_id 1001: 1001 % 3 = 2 -> Shard 2
-- customer_id 1002: 1002 % 3 = 0 -> Shard 0
-- customer_id 1003: 1003 % 3 = 1 -> Shard 1

-- Query (need to determine shard first)
-- shard = customer_id % 3
-- SELECT * FROM customers_shard{shard} WHERE customer_id = 1001;

-- =====================================================
-- C. DIRECTORY-BASED SHARDING
-- =====================================================
-- Use a lookup table to determine which shard contains data
-- More flexible but requires lookup overhead

-- Shard directory table
-- CREATE TABLE shard_directory (
--     customer_id INT PRIMARY KEY,
--     shard_id INT,
--     shard_location VARCHAR(100)
-- );

-- Query with directory lookup
-- SELECT shard_id FROM shard_directory WHERE customer_id = 1001;
-- Then query: SELECT * FROM customers_shard{shard_id} WHERE customer_id = 1001;

-- =====================================================
-- 2. SHARDING BY GEOGRAPHY
-- =====================================================
-- Shard data based on geographic location

-- Example: Shard customers by country
-- CREATE TABLE customers_usa (...);
-- CREATE TABLE customers_europe (...);
-- CREATE TABLE customers_asia (...);

-- Query based on location
-- SELECT * FROM customers_usa WHERE customer_id = 1001;
-- SELECT * FROM customers_europe WHERE customer_id = 2001;

-- =====================================================
-- 3. SHARDING BY DATE/TIME
-- =====================================================
-- Shard data based on time periods

-- Example: Shard orders by year
-- CREATE TABLE orders_2022 (...);
-- CREATE TABLE orders_2023 (...);
-- CREATE TABLE orders_2024 (...);

-- Query specific time period
-- SELECT * FROM orders_2023 WHERE order_id = 3001;
-- SELECT * FROM orders_2024 WHERE order_date >= '2024-01-01';

-- =====================================================
-- 4. CROSS-SHARD QUERIES
-- =====================================================
-- Queries that need data from multiple shards

-- Example: Find all customers with orders > $1000
-- Need to query all shards and combine results

-- UNION approach (conceptual)
/*
SELECT customer_id, SUM(total_amount) AS total
FROM orders_shard1
GROUP BY customer_id
HAVING SUM(total_amount) > 1000

UNION ALL

SELECT customer_id, SUM(total_amount) AS total
FROM orders_shard2
GROUP BY customer_id
HAVING SUM(total_amount) > 1000

-- Then aggregate again across shards
*/
-- Note: Cross-shard queries are expensive and should be minimized

-- =====================================================
-- 5. SHARDING CHALLENGES
-- =====================================================

-- 1. Data Distribution
-- Ensure even distribution across shards
-- SELECT 
--     CASE 
--         WHEN customer_id BETWEEN 1 AND 1000 THEN 'Shard1'
--         WHEN customer_id BETWEEN 1001 AND 2000 THEN 'Shard2'
--         ELSE 'Shard3'
--     END AS shard,
--     COUNT(*) AS customer_count
-- FROM customers
-- GROUP BY shard;
-- Check distribution balance

-- 2. Rebalancing
-- Moving data between shards when distribution becomes uneven
-- Requires careful planning and downtime

-- 3. Joins Across Shards
-- Very expensive, often avoided
-- May need to denormalize or duplicate reference data

-- 4. Transactions Across Shards
-- Complex, requires distributed transaction management
-- Often avoided or handled with eventual consistency

-- =====================================================
-- 6. SHARDING BEST PRACTICES
-- =====================================================

-- 1. Choose shard key carefully
-- Should distribute data evenly
-- Should minimize cross-shard queries
-- Should align with query patterns

-- 2. Keep reference data in all shards
-- Small lookup tables can be replicated
-- Avoids cross-shard joins

-- 3. Monitor shard sizes
-- Regular monitoring to detect imbalance
-- Plan for rebalancing

-- 4. Design for shard locality
-- Keep related data in same shard when possible
-- Reduces cross-shard operations

-- =====================================================
-- 7. EXAMPLE: SHARDING ORDERS TABLE
-- =====================================================

-- Conceptual sharding by customer_id ranges
-- In practice, this would be separate databases/servers

-- Shard 1: customer_id 1001-1004
-- CREATE TABLE orders_shard1 AS
-- SELECT * FROM orders WHERE customer_id BETWEEN 1001 AND 1004;

-- Shard 2: customer_id 1005-1008
-- CREATE TABLE orders_shard2 AS
-- SELECT * FROM orders WHERE customer_id BETWEEN 1005 AND 1008;

-- Query specific customer's orders
-- SELECT * FROM orders_shard1 WHERE customer_id = 1001;
-- SELECT * FROM orders_shard2 WHERE customer_id = 1005;

-- =====================================================
-- 8. SHARDING vs PARTITIONING
-- =====================================================

-- Partitioning: Within single database
-- Sharding: Across multiple databases/servers

-- Partitioning example (within same DB):
-- CREATE TABLE orders_partitioned (
--     order_id INT,
--     customer_id INT,
--     order_date DATE,
--     ...
-- ) PARTITION BY RANGE (customer_id) (
--     PARTITION p1 VALUES LESS THAN (1005),
--     PARTITION p2 VALUES LESS THAN (1009)
-- );

-- Sharding: Separate physical databases/servers
-- More complex but enables true horizontal scaling

