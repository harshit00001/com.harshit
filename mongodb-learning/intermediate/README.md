# MongoDB Intermediate - Learning Guide

## 📖 What You'll Learn

This section covers intermediate MongoDB concepts:

1. **Indexes** - Improving query performance
2. **Text Search** - Full-text search capabilities
3. **Transactions** - Multi-document operations
4. **Validation** - Schema validation rules

---

## 🎯 Learning Objectives

By the end of this section, you will be able to:
- ✅ Create and manage indexes for better performance
- ✅ Implement full-text search
- ✅ Use transactions for data consistency
- ✅ Apply schema validation rules

---

## 📚 Examples Overview

### Example 1: Indexes
**File:** `Example1-Indexes.js`
- Creating indexes
- Compound indexes
- Index performance comparison
- Index management

### Example 2: Text Search
**File:** `Example2-TextSearch.js`
- Creating text indexes
- Text search queries
- Search relevance and scoring

### Example 3: Transactions
**File:** `Example3-Transactions.js`
- Starting transactions
- Multi-document operations
- Error handling and rollback

### Example 4: Validation
**File:** `Example4-Validation.js`
- Schema validation rules
- Validation on insert/update
- Custom validation messages

---

## 🚀 How to Run Examples

```bash
# Run Example 1
node intermediate/Example1-Indexes.js

# Or use npm script
npm run intermediate:indexes
```

---

## 📝 Key Concepts

### Indexes

**Simple Explanation:**
Indexes are like a book's index - they help MongoDB find data quickly without scanning every document.

**Technical Explanation:**
Indexes are data structures that store a small portion of the collection's data in an easy-to-traverse form. They significantly improve query performance but use additional storage space.

### Text Search

**Simple Explanation:**
Text search lets you search for words or phrases in text fields, like Google search.

**Technical Explanation:**
MongoDB creates a text index that indexes all string content. You can search using `$text` operator and get relevance scores.

### Transactions

**Simple Explanation:**
Transactions ensure that multiple operations either all succeed or all fail together - like transferring money between accounts.

**Technical Explanation:**
Transactions provide ACID (Atomicity, Consistency, Isolation, Durability) guarantees across multiple operations on one or more collections.

### Validation

**Simple Explanation:**
Validation rules ensure data follows certain patterns - like making sure email fields contain valid email addresses.

**Technical Explanation:**
MongoDB allows you to define JSON Schema validation rules that are enforced when documents are inserted or updated.

---

## 🎓 Practice Exercises

1. **Indexes:**
   - Create indexes on frequently queried fields
   - Compare query performance with and without indexes
   - Create compound indexes for multi-field queries

2. **Text Search:**
   - Create text index on product descriptions
   - Search for products by keywords
   - Sort results by relevance score

3. **Transactions:**
   - Transfer money between accounts
   - Update inventory and create order in single transaction
   - Handle transaction errors

4. **Validation:**
   - Validate email format
   - Ensure required fields are present
   - Validate number ranges

---

## 📖 Next Steps

After completing the intermediate section:
1. Review `SCENARIO_QA.md` for real-world questions
2. Try the practice exercises above
3. Move to **Advanced** section

---

**Happy Learning! 🚀**

