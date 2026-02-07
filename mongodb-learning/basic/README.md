# MongoDB Basics - Learning Guide

## 📖 What You'll Learn

This section covers the fundamental concepts of MongoDB:

1. **Connecting to MongoDB** - How to establish a connection
2. **CRUD Operations** - Create, Read, Update, Delete
3. **Querying Data** - Finding documents
4. **Operators** - Comparison, logical, and array operators
5. **Embedded Documents** - Working with nested data

---

## 🎯 Learning Objectives

By the end of this section, you will be able to:
- ✅ Connect to MongoDB from Node.js
- ✅ Perform basic CRUD operations
- ✅ Write queries to find documents
- ✅ Use various operators for filtering
- ✅ Work with embedded documents and arrays

---

## 📚 Examples Overview

### Example 1: Connection
**File:** `Example1-Connection.js`
- How to connect to MongoDB
- Error handling
- Connection best practices

### Example 2: CRUD Operations
**File:** `Example2-CRUD.js`
- Create documents (insert)
- Read documents (find)
- Update documents (update)
- Delete documents (delete)

### Example 3: Query Basics
**File:** `Example3-Query.js`
- Finding documents by field values
- Querying with conditions
- Limiting and sorting results

### Example 4: Operators
**File:** `Example4-Operators.js`
- Comparison operators ($gt, $lt, $eq, etc.)
- Logical operators ($and, $or, $not)
- Array operators ($in, $nin, $all)

### Example 5: Embedded Documents
**File:** `Example5-EmbeddedDocuments.js`
- Working with nested objects
- Querying embedded documents
- Updating nested fields

---

## 🚀 How to Run Examples

```bash
# Run Example 1
node basic/Example1-Connection.js

# Or use npm script
npm run basic:connection
```

---

## 📝 Key Concepts

### What is MongoDB?

**Simple Explanation:**
MongoDB is like a digital filing cabinet where you store information (documents) in folders (collections). Unlike traditional databases with tables and rows, MongoDB stores data as flexible JSON-like documents.

**Technical Explanation:**
MongoDB is a NoSQL document database that stores data in BSON (Binary JSON) format. It's schema-less, meaning documents in a collection don't need to have the same structure.

### Collections vs Documents

- **Collection** = Table (in SQL terms)
- **Document** = Row (in SQL terms)
- **Field** = Column (in SQL terms)

### Example:

```javascript
// Collection: users
// Document:
{
  _id: ObjectId("..."),
  name: "John Doe",
  age: 30,
  email: "john@example.com"
}
```

---

## 🎓 Practice Exercises

After running each example, try these:

1. **Connection:**
   - Modify connection string to use different database name
   - Add error handling for connection failures

2. **CRUD:**
   - Create 5 different user documents
   - Find users older than 25
   - Update a user's email
   - Delete a user

3. **Queries:**
   - Find documents with specific field values
   - Sort results by age
   - Limit results to 10 documents

4. **Operators:**
   - Find users with age between 20 and 30
   - Find users with specific email domains
   - Find users with multiple interests

5. **Embedded Documents:**
   - Create a user with address information
   - Query users by city
   - Update nested address fields

---

## 📖 Next Steps

After completing the basic section:
1. Review `SCENARIO_QA.md` for real-world questions
2. Try the practice exercises above
3. Move to **Intermediate** section

---

**Happy Learning! 🚀**

