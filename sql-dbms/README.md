# SQL and DBMS Concepts - Complete Guide

## Overview
This project contains comprehensive SQL queries and DBMS concepts from basic to advanced level, designed for interview preparation. All examples use a sample database with real data.

## Project Structure

```
sql-dbms/
├── database/
│   ├── schema.sql          # Database schema creation
│   └── sample_data.sql     # Sample data insertion
├── queries/
│   ├── 01_basic_queries.sql      # SELECT, WHERE, ORDER BY, GROUP BY, HAVING
│   ├── 02_joins.sql              # All types of JOINs with examples
│   ├── 03_normalization.sql      # Normalization concepts (1NF, 2NF, 3NF, BCNF)
│   ├── 04_views.sql              # Views (Simple, Complex, Materialized)
│   ├── 05_data_sharding.sql      # Data sharding strategies and examples
│   ├── 06_advanced_queries.sql   # Subqueries, CTEs, Window Functions
│   ├── 07_indexing.sql           # Indexing types and best practices
│   └── 08_transactions_acid.sql  # Transactions and ACID properties
└── README.md
```

## Setup Instructions

1. **Create Database Schema**
   ```sql
   -- Run schema.sql first
   source database/schema.sql;
   ```

2. **Insert Sample Data**
   ```sql
   -- Then run sample_data.sql
   source database/sample_data.sql;
   ```

3. **Run Queries**
   ```sql
   -- Execute any query file
   source queries/01_basic_queries.sql;
   ```

## Database Schema

The sample database includes:

- **employees**: Employee information with departments and managers
- **departments**: Department details with locations
- **locations**: Office locations
- **customers**: Customer information
- **products**: Product catalog
- **orders**: Customer orders
- **order_items**: Order line items
- **students**: Student information (for normalization examples)
- **courses**: Course catalog
- **student_courses**: Student-course enrollments

## Topics Covered

### SQL Basics
- SELECT, WHERE, ORDER BY, GROUP BY, HAVING
- Aggregate Functions (COUNT, SUM, AVG, MAX, MIN)
- LIMIT/TOP for pagination
- All queries use real sample data

### SQL Joins
- INNER JOIN
- LEFT JOIN / LEFT OUTER JOIN
- RIGHT JOIN / RIGHT OUTER JOIN
- FULL OUTER JOIN
- CROSS JOIN
- SELF JOIN
- Multiple table joins with real examples

### Advanced SQL
- Subqueries (Scalar, Row, Column, Table, Correlated)
- Common Table Expressions (CTEs)
- Window Functions (ROW_NUMBER, RANK, DENSE_RANK, LAG, LEAD, SUM OVER)
- CASE expressions
- UNION, INTERSECT, EXCEPT

### DBMS Concepts

#### Normalization
- First Normal Form (1NF)
- Second Normal Form (2NF)
- Third Normal Form (3NF)
- Boyce-Codd Normal Form (BCNF)
- Denormalization examples

#### Views
- Simple Views
- Complex Views (with JOINs)
- Views with Aggregation
- Views for Security
- Materialized Views

#### Data Sharding
- Range-based Sharding
- Hash-based Sharding
- Directory-based Sharding
- Geographic Sharding
- Time-based Sharding
- Cross-shard Queries

#### Indexing
- B-Tree Indexes
- Hash Indexes
- Composite Indexes
- Unique Indexes
- Partial Indexes
- Covering Indexes
- Index Best Practices

#### Transactions & ACID
- Transaction Basics
- ACID Properties (Atomicity, Consistency, Isolation, Durability)
- Isolation Levels
- Savepoints
- Deadlocks
- Best Practices

## How to Use

1. **Set up the database** by running `schema.sql` and `sample_data.sql`
2. **Explore queries** in the `queries/` directory
3. **Modify queries** to experiment with different scenarios
4. **Study concepts** with real data examples

## Interview Preparation

Each concept includes:
- **Technical definitions** with detailed explanations
- **Simple explanations** for easy understanding
- **SQL examples** using the sample database
- **Interview answers** ready to use
- **Best practices** and tips

## Database Compatibility

Queries are written to be compatible with:
- **PostgreSQL** (primary target)
- **MySQL** (with minor syntax adjustments)
- **SQL Server** (with minor syntax adjustments)

Some advanced features (like FULL OUTER JOIN, recursive CTEs) may have syntax differences.

## Notes

- All queries use the sample database created in `schema.sql`
- Sample data is inserted via `sample_data.sql`
- Queries are organized by topic for easy navigation
- Each query file is self-contained and can be run independently
- Comments explain each concept in detail
