# MongoDB Learning Project - Complete Guide

## 📚 Overview

This project is designed to help you learn MongoDB from basics to advanced concepts with real-world examples and scenario-based questions. The project is divided into three sections:

1. **Basic** - Fundamentals of MongoDB
2. **Intermediate** - Complex queries and operations
3. **Advanced** - Performance optimization, aggregation, and production patterns

---

## 🚀 Quick Start Guide

### Step 1: Install MongoDB

#### Option A: Install MongoDB Community Server (Recommended for Learning)

1. **Download MongoDB:**
   - Visit: https://www.mongodb.com/try/download/community
   - Select your OS (Windows/Mac/Linux)
   - Download the installer

2. **Install MongoDB:**
   - Run the installer
   - Choose "Complete" installation
   - Install MongoDB as a Windows Service (recommended)
   - Install MongoDB Compass (GUI tool - optional but helpful)

3. **Verify Installation:**
   ```bash
   mongod --version
   mongo --version
   ```

#### Option B: Use MongoDB Atlas (Cloud - Free Tier Available)

1. **Sign up for MongoDB Atlas:**
   - Visit: https://www.mongodb.com/cloud/atlas/register
   - Create a free account
   - Create a free cluster (M0 - Free tier)

2. **Get Connection String:**
   - Click "Connect" on your cluster
   - Choose "Connect your application"
   - Copy the connection string
   - Update `config/database.js` with your connection string

### Step 2: Install Node.js Dependencies

```bash
cd mongodb-learning
npm install
```

### Step 3: Start MongoDB (If using local installation)

**Windows:**
```bash
# MongoDB should start automatically as a service
# Or manually start:
net start MongoDB
```

**Mac/Linux:**
```bash
mongod --dbpath /path/to/data/directory
```

### Step 4: Run Examples

```bash
# Run basic examples
node basic/Example1-Connection.js

# Run intermediate examples
node intermediate/Example1-Indexes.js

# Run advanced examples
node advanced/Example1-Aggregation.js
```

---

## 📁 Project Structure

```
mongodb-learning/
├── README.md                    # This file
├── QUICK_START.md              # Quick setup guide
├── package.json                # Node.js dependencies
├── config/
│   └── database.js            # Database connection configuration
├── basic/                      # Basic MongoDB concepts
│   ├── README.md
│   ├── Example1-Connection.js
│   ├── Example2-CRUD.js
│   ├── Example3-Query.js
│   ├── Example4-Operators.js
│   ├── Example5-EmbeddedDocuments.js
│   └── SCENARIO_QA.md
├── intermediate/               # Intermediate concepts
│   ├── README.md
│   ├── Example1-Indexes.js
│   ├── Example2-TextSearch.js
│   ├── Example3-Transactions.js
│   ├── Example4-Validation.js
│   └── SCENARIO_QA.md
└── advanced/                   # Advanced concepts
    ├── README.md
    ├── Example1-Aggregation.js
    ├── Example2-Replication.js
    ├── Example3-Sharding.js
    ├── Example4-Performance.js
    └── SCENARIO_QA.md
```

---

## 🎯 Learning Path

### Week 1: Basic Concepts
- Day 1-2: Connection and CRUD operations
- Day 3-4: Query operators and filtering
- Day 5-7: Embedded documents and arrays

### Week 2: Intermediate Concepts
- Day 1-2: Indexes and performance
- Day 3-4: Text search and validation
- Day 5-7: Transactions and error handling

### Week 3: Advanced Concepts
- Day 1-3: Aggregation pipeline
- Day 4-5: Replication and sharding
- Day 6-7: Performance optimization

---

## 🔧 Configuration

### Local MongoDB Connection

Default connection string: `mongodb://localhost:27017/mongodb_learning`

### MongoDB Atlas Connection

Update `config/database.js`:
```javascript
const uri = "mongodb+srv://username:password@cluster.mongodb.net/mongodb_learning";
```

---

## 📖 How to Use This Project

1. **Start with Basic Section:**
   - Read `basic/README.md`
   - Run examples in order (Example1, Example2, etc.)
   - Study `basic/SCENARIO_QA.md` for real-world scenarios

2. **Move to Intermediate:**
   - Complete all basic examples first
   - Read `intermediate/README.md`
   - Practice with intermediate examples

3. **Master Advanced:**
   - Only after mastering basic and intermediate
   - Focus on performance and production patterns

---

## 🛠️ Troubleshooting

### MongoDB Not Starting

**Windows:**
```bash
# Check if service is running
sc query MongoDB

# Start service manually
net start MongoDB
```

**Mac/Linux:**
```bash
# Check if MongoDB is running
ps aux | grep mongod

# Start MongoDB
mongod --dbpath /path/to/data
```

### Connection Errors

1. **Check if MongoDB is running:**
   ```bash
   # Windows
   net start MongoDB
   
   # Mac/Linux
   sudo systemctl status mongod
   ```

2. **Check connection string:**
   - Verify `config/database.js` has correct connection string
   - For Atlas: Check username, password, and cluster URL

3. **Firewall Issues:**
   - Ensure port 27017 is open (for local MongoDB)
   - For Atlas: Check IP whitelist in Atlas dashboard

---

## 📝 Notes

- All examples are self-contained and can be run independently
- Each example includes detailed comments explaining concepts
- Scenario-based Q&A files contain real-world interview questions
- Examples use async/await for modern JavaScript

---

## 🎓 Next Steps

After completing this project:
1. Build a real project using MongoDB
2. Learn MongoDB Compass (GUI tool)
3. Explore MongoDB Atlas features
4. Study MongoDB best practices
5. Learn about MongoDB drivers for other languages

---

## 📚 Additional Resources

- [MongoDB Official Documentation](https://docs.mongodb.com/)
- [MongoDB University (Free Courses)](https://university.mongodb.com/)
- [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
- [MongoDB Compass](https://www.mongodb.com/products/compass)

---

## 🤝 Contributing

Feel free to add more examples or improve existing ones!

---

**Happy Learning! 🚀**

