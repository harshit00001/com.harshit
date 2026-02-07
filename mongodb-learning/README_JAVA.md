# MongoDB Learning - Java Edition

Complete MongoDB learning project with examples from basic to advanced, converted to Java.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MongoDB installed and running (local or MongoDB Atlas)

## Setup

1. **Install Dependencies**
   ```bash
   mvn clean install
   ```

2. **Configure MongoDB Connection**
   
   Edit `src/main/java/com/harshit/mongodb/config/DatabaseConfig.java`:
   
   - For local MongoDB (default): `mongodb://localhost:27017/mongodb_learning`
   - For MongoDB Atlas: Update the `ATLAS_URI` constant with your connection string

## Running Examples

### Basic Examples

1. **Connection Example**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example1Connection"
   ```

2. **CRUD Operations**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example2CRUD"
   ```

3. **Query Basics**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example3Query"
   ```

4. **MongoDB Operators**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example4Operators"
   ```

5. **Embedded Documents**
   ```bash
   mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example5EmbeddedDocuments"
   ```

## Project Structure

```
mongodb-learning/
├── pom.xml                                    # Maven configuration
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── harshit/
│                   └── mongodb/
│                       ├── config/
│                       │   └── DatabaseConfig.java    # Database connection configuration
│                       └── basic/
│                           ├── Example1Connection.java      # Connection example
│                           ├── Example2CRUD.java           # CRUD operations example
│                           ├── Example3Query.java          # Query basics example
│                           ├── Example4Operators.java      # MongoDB operators example
│                           └── Example5EmbeddedDocuments.java # Embedded documents example
└── README_JAVA.md                            # This file
```

## Key Differences from JavaScript Version

1. **Connection Management**: Uses `MongoClient` from MongoDB Java Driver
2. **Document Handling**: Uses `Document` class instead of plain JavaScript objects
3. **Error Handling**: Uses try-catch blocks with proper resource management
4. **Logging**: Uses SLF4J/Logback for logging instead of console.log
5. **Type Safety**: Java provides compile-time type checking

## MongoDB Java Driver Features Used

- `MongoClients.create()` - Create MongoDB client
- `MongoDatabase` - Database operations
- `MongoCollection` - Collection operations
- `Document` - BSON document representation
- `Filters` - Query filter builders
- `Updates` - Update operation builders

## Troubleshooting

1. **Connection Failed**
   - Ensure MongoDB is running: `mongod` for local or check Atlas connection
   - Verify connection string in `DatabaseConfig.java`
   - Check firewall settings for Atlas

2. **Compilation Errors**
   - Ensure Java 11+ is installed: `java -version`
   - Run `mvn clean install` to download dependencies

3. **Runtime Errors**
   - Check MongoDB logs
   - Verify database permissions
   - Ensure collections exist (they're created automatically on first insert)

## Completed Examples

✅ **Basic Examples:**
- Example1Connection - MongoDB connection and basic operations
- Example2CRUD - Create, Read, Update, Delete operations
- Example3Query - Query basics, sorting, limiting, projection
- Example4Operators - Comparison, logical, and array operators
- Example5EmbeddedDocuments - Nested documents and arrays

## Next Steps

More examples will be added:
- Indexes
- Text search
- Transactions
- Aggregation pipelines
- Performance optimization

