# How to Connect and Run MongoDB Examples

## Prerequisites

1. **Java 11 or higher** - Check with: `java -version`
2. **Maven 3.6+** - Check with: `mvn -version`
3. **MongoDB** - Either:
   - **Local MongoDB** installed and running
   - **MongoDB Atlas** account (cloud)

---

## Step 1: Install Dependencies

Open terminal in the project directory and run:

```bash
cd "c:\Java code\com.harshit\mongodb-learning"
mvn clean install
```

This will download MongoDB Java Driver and other dependencies.

---

## Step 2: Set Up MongoDB Connection

### Option A: Local MongoDB (Recommended for Learning)

1. **Install MongoDB locally:**
   - Download from: https://www.mongodb.com/try/download/community
   - Install and start MongoDB service
   - Default connection: `mongodb://localhost:27017`

2. **Verify MongoDB is running:**
   ```bash
   # Windows PowerShell
   Get-Service MongoDB
   
   # Or check if mongod is running
   # Default port: 27017
   ```

3. **Connection is already configured** in `DatabaseConfig.java`:
   ```java
   private static final String LOCAL_URI = "mongodb://localhost:27017/mongodb_learning";
   ```

### Option B: MongoDB Atlas (Cloud)

1. **Create free account:** https://www.mongodb.com/cloud/atlas/register

2. **Create a cluster:**
   - Click "Build a Database"
   - Choose FREE tier (M0)
   - Select a cloud provider and region
   - Click "Create"

3. **Get connection string:**
   - Click "Connect" on your cluster
   - Choose "Connect your application"
   - Copy the connection string
   - It looks like: `mongodb+srv://username:password@cluster.mongodb.net/`

4. **Update DatabaseConfig.java:**
   ```java
   // Comment out LOCAL_URI and uncomment ATLAS_URI
   // private static final String LOCAL_URI = "mongodb://localhost:27017/mongodb_learning";
   private static final String ATLAS_URI = "mongodb+srv://your-username:your-password@cluster.mongodb.net/mongodb_learning";
   private static final String URI = ATLAS_URI; // Change this line
   ```

5. **Whitelist your IP:**
   - In Atlas, go to "Network Access"
   - Click "Add IP Address"
   - Click "Allow Access from Anywhere" (for testing) or add your IP

---

## Step 3: Run Examples

### Method 1: Using Maven Exec Plugin

```bash
# Connection Example
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example1Connection"

# CRUD Operations
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example2CRUD"

# Query Basics
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example3Query"

# MongoDB Operators
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example4Operators"

# Embedded Documents
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example5EmbeddedDocuments"
```

### Method 2: Using IDE (IntelliJ IDEA / Eclipse)

1. **Open the project** in your IDE
2. **Right-click on any example class** (e.g., `Example1Connection.java`)
3. **Select "Run"** or press `Shift+F10` (IntelliJ) / `F11` (Eclipse)
4. **Or run from terminal:**
   ```bash
   # Compile
   mvn compile
   
   # Run specific class
   java -cp target/classes;target/dependency/* com.harshit.mongodb.basic.Example1Connection
   ```

### Method 3: Create a JAR and Run

```bash
# Create executable JAR
mvn package

# Run (if main class is set in pom.xml)
java -jar target/mongodb-learning-1.0.0.jar
```

---

## Step 4: Verify Connection

Run the connection example first:

```bash
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example1Connection"
```

**Expected Output:**
```
✅ Connected to MongoDB successfully!
📊 Connected to database: mongodb_learning
📁 Collections in database:
🧪 Testing connection with a sample operation...
✅ Test document inserted with ID: ...
📄 Found document:
🧹 Test document cleaned up
✅ Connection test completed successfully!
✅ MongoDB connection closed.
```

---

## Troubleshooting

### Error: "Connection refused" or "Cannot connect"

**For Local MongoDB:**
1. Check if MongoDB is running:
   ```bash
   # Windows
   net start MongoDB
   
   # Or check services
   services.msc
   ```

2. Verify MongoDB is listening on port 27017:
   ```bash
   netstat -an | findstr 27017
   ```

3. Start MongoDB manually if needed:
   ```bash
   # Navigate to MongoDB bin directory
   cd "C:\Program Files\MongoDB\Server\7.0\bin"
   mongod
   ```

**For MongoDB Atlas:**
1. Check your connection string (username/password)
2. Verify IP whitelist includes your IP
3. Check if cluster is running (not paused)
4. Verify network connectivity

### Error: "ClassNotFoundException" or "NoClassDefFoundError"

```bash
# Clean and rebuild
mvn clean install
```

### Error: "Java version mismatch"

Ensure Java 11+ is installed:
```bash
java -version
# Should show version 11 or higher
```

### Error: "Maven not found"

Install Maven or use IDE's built-in Maven support.

---

## Quick Test Script

Create a simple test file to verify everything works:

```bash
# Test connection
mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example1Connection"
```

If this works, all other examples should work too!

---

## Connection String Formats

### Local MongoDB
```
mongodb://localhost:27017/database_name
mongodb://127.0.0.1:27017/database_name
```

### MongoDB Atlas
```
mongodb+srv://username:password@cluster.mongodb.net/database_name
```

### With Authentication (Local)
```
mongodb://username:password@localhost:27017/database_name
```

### With Options
```
mongodb://localhost:27017/database_name?retryWrites=true&w=majority
```

---

## Next Steps

Once connection works:
1. ✅ Run `Example1Connection` - Verify connection
2. ✅ Run `Example2CRUD` - Learn basic operations
3. ✅ Run `Example3Query` - Learn querying
4. ✅ Run `Example4Operators` - Learn operators
5. ✅ Run `Example5EmbeddedDocuments` - Learn nested documents

Happy coding! 🚀

