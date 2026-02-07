# MongoDB Intermediate - Scenario-Based Questions & Answers

## 📚 Real-World Scenarios with Solutions

---

## Scenario 1: Slow Query Performance

**Question:** Your e-commerce site has 1 million products. Users complain that searching for products by category is very slow (taking 5+ seconds). How would you optimize this?

**Answer:**

```javascript
// Simple Explanation:
// Create an index on the category field to speed up searches
// Indexes are like a book's index - they help find data quickly

async function optimizeCategorySearch() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    // Create index on category field
    console.log('Creating index on category field...');
    await products.createIndex({ category: 1 });
    console.log('✅ Index created');
    
    // Now queries on category will be much faster
    const startTime = Date.now();
    const electronics = await products.find({ category: 'Electronics' }).toArray();
    const endTime = Date.now();
    
    console.log(`Query took ${endTime - startTime}ms`);
    console.log(`Found ${electronics.length} products`);
    
    await client.close();
}
```

**Key Points:**
- Indexes dramatically improve query performance
- Create indexes on frequently queried fields
- Use `explain()` to analyze query performance
- Monitor index usage and remove unused indexes

---

## Scenario 2: Full-Text Search for Products

**Question:** Users want to search for products by typing keywords like "laptop" or "wireless mouse". How do you implement this?

**Answer:**

```javascript
// Simple Explanation:
// Create a text index on product name and description fields
// Then use $text operator to search for keywords

async function setupProductSearch() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    // Create text index on name and description
    await products.createIndex({
        name: 'text',
        description: 'text'
    });
    
    console.log('✅ Text index created');
}

async function searchProducts(keyword) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    // Search with relevance scoring
    const results = await products
        .find(
            { $text: { $search: keyword } },
            { score: { $meta: 'textScore' } }
        )
        .sort({ score: { $meta: 'textScore' } })
        .limit(20)
        .toArray();
    
    await client.close();
    return results;
}

// Usage:
const results = await searchProducts('laptop wireless');
results.forEach(product => {
    console.log(`${product.name} (Score: ${product.score})`);
});
```

**Key Points:**
- Only one text index per collection
- Text search is case-insensitive
- Use `$meta: 'textScore'` to get relevance scores
- Sort by score to show most relevant results first

---

## Scenario 3: Money Transfer Between Accounts

**Question:** You're building a banking app. When transferring money, you need to ensure that if debiting one account succeeds but crediting the other fails, the debit is rolled back. How do you handle this?

**Answer:**

```javascript
// Simple Explanation:
// Use transactions to ensure both operations succeed or both fail
// Like a safety net - if anything goes wrong, everything is undone

async function transferMoney(fromAccountId, toAccountId, amount) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('banking');
    const accounts = db.collection('accounts');
    
    const session = client.startSession();
    
    try {
        // Start transaction
        session.startTransaction();
        
        // Step 1: Debit from sender
        const debitResult = await accounts.updateOne(
            { 
                accountId: fromAccountId, 
                balance: { $gte: amount } // Ensure sufficient balance
            },
            { $inc: { balance: -amount } },
            { session }
        );
        
        if (debitResult.modifiedCount === 0) {
            throw new Error('Insufficient balance or account not found');
        }
        
        // Step 2: Credit to receiver
        const creditResult = await accounts.updateOne(
            { accountId: toAccountId },
            { $inc: { balance: amount } },
            { session }
        );
        
        if (creditResult.modifiedCount === 0) {
            throw new Error('Receiver account not found');
        }
        
        // If both succeed, commit transaction
        await session.commitTransaction();
        console.log('✅ Transfer completed successfully');
        
    } catch (error) {
        // If anything fails, rollback everything
        await session.abortTransaction();
        console.error('❌ Transfer failed, all changes rolled back:', error.message);
        throw error;
    } finally {
        await session.endSession();
        await client.close();
    }
}

// Usage:
try {
    await transferMoney('ACC001', 'ACC002', 500);
} catch (error) {
    console.log('Transfer could not be completed');
}
```

**Key Points:**
- Transactions ensure atomicity (all or nothing)
- Always use try-catch and abort on errors
- Pass session to all operations in transaction
- Transactions require replica set (not standalone MongoDB)

---

## Scenario 4: Data Validation for User Registration

**Question:** You need to ensure that when users register, their email is valid, age is between 18-100, and phone number is exactly 10 digits. How do you enforce this?

**Answer:**

```javascript
// Simple Explanation:
// Create validation rules when creating the collection
// MongoDB will reject any data that doesn't match the rules

async function setupUserValidation() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('myapp');
    
    // Drop existing collection if it exists
    try {
        await db.collection('users').drop();
    } catch (e) {}
    
    // Create collection with validation rules
    await db.createCollection('users', {
        validator: {
            $jsonSchema: {
                bsonType: 'object',
                required: ['name', 'email', 'age', 'phone'],
                properties: {
                    name: {
                        bsonType: 'string',
                        minLength: 2,
                        maxLength: 50
                    },
                    email: {
                        bsonType: 'string',
                        pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
                    },
                    age: {
                        bsonType: 'int',
                        minimum: 18,
                        maximum: 100
                    },
                    phone: {
                        bsonType: 'string',
                        pattern: '^\\d{10}$' // Exactly 10 digits
                    }
                }
            }
        },
        validationLevel: 'strict',
        validationAction: 'error'
    });
    
    console.log('✅ User collection created with validation rules');
    await client.close();
}

// Now try to insert invalid data
async function testValidation() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('myapp');
    const users = db.collection('users');
    
    // Valid user - will succeed
    try {
        await users.insertOne({
            name: 'John Doe',
            email: 'john@example.com',
            age: 30,
            phone: '1234567890'
        });
        console.log('✅ Valid user inserted');
    } catch (error) {
        console.log('❌ Error:', error.message);
    }
    
    // Invalid email - will fail
    try {
        await users.insertOne({
            name: 'Jane',
            email: 'invalid-email', // Invalid format
            age: 25,
            phone: '9876543210'
        });
    } catch (error) {
        console.log('✅ Correctly rejected invalid email');
    }
    
    // Age too young - will fail
    try {
        await users.insertOne({
            name: 'Young User',
            email: 'young@example.com',
            age: 15, // Under 18
            phone: '5555555555'
        });
    } catch (error) {
        console.log('✅ Correctly rejected age < 18');
    }
    
    await client.close();
}
```

**Key Points:**
- Validation rules are defined using JSON Schema
- `required` array lists mandatory fields
- `properties` defines validation for each field
- Use `pattern` for regex validation (email, phone)
- Use `minimum`/`maximum` for number ranges

---

## Scenario 5: Compound Index for Multi-Field Queries

**Question:** Your application frequently queries products by both category and price range, and sorts by rating. How do you optimize this?

**Answer:**

```javascript
// Simple Explanation:
// Create a compound index on multiple fields that are queried together
// The order of fields in the index matters!

async function optimizeProductQueries() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    // Create compound index on category, price, and rating
    // Order matters: category first (most selective), then price, then rating
    await products.createIndex({ 
        category: 1, 
        price: 1, 
        rating: -1 
    });
    
    console.log('✅ Compound index created');
    
    // This query will use the compound index efficiently
    const results = await products
        .find({
            category: 'Electronics',
            price: { $gte: 100, $lte: 500 }
        })
        .sort({ rating: -1 })
        .toArray();
    
    console.log(`Found ${results.length} products`);
    
    // Explain the query to see if index is used
    const explain = await products
        .find({
            category: 'Electronics',
            price: { $gte: 100, $lte: 500 }
        })
        .sort({ rating: -1 })
        .explain('executionStats');
    
    console.log('Index used:', explain.executionStats.executionStages.indexName);
    
    await client.close();
}
```

**Key Points:**
- Compound indexes support queries on multiple fields
- Field order in index matters (most selective first)
- Index can support queries on prefix of fields
- Use `explain()` to verify index usage

---

## Scenario 6: Preventing Duplicate Emails

**Question:** You need to ensure that no two users can have the same email address. How do you enforce this?

**Answer:**

```javascript
// Simple Explanation:
// Create a unique index on the email field
// MongoDB will reject any insert/update that creates a duplicate

async function preventDuplicateEmails() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('myapp');
    const users = db.collection('users');
    
    // Create unique index on email
    await users.createIndex({ email: 1 }, { unique: true });
    console.log('✅ Unique index created on email');
    
    // Insert first user - will succeed
    try {
        await users.insertOne({
            name: 'John Doe',
            email: 'john@example.com'
        });
        console.log('✅ First user inserted');
    } catch (error) {
        console.log('Error:', error.message);
    }
    
    // Try to insert duplicate email - will fail
    try {
        await users.insertOne({
            name: 'Jane Doe',
            email: 'john@example.com' // Duplicate!
        });
        console.log('❌ This should not print');
    } catch (error) {
        console.log('✅ Correctly prevented duplicate email');
        console.log('Error:', error.message);
    }
    
    await client.close();
}
```

**Key Points:**
- Unique indexes prevent duplicate values
- Works on both inserts and updates
- Can create unique compound indexes
- Null values are allowed (multiple nulls are considered unique)

---

## Scenario 7: Inventory Management with Transactions

**Question:** When a customer places an order, you need to:
1. Reduce inventory for each product
2. Create the order record
3. If any product is out of stock, cancel the entire order

How do you implement this?

**Answer:**

```javascript
// Simple Explanation:
// Use a transaction to ensure all steps succeed or all fail
// Check inventory before updating, rollback if insufficient stock

async function processOrder(orderItems) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const inventory = db.collection('inventory');
    const orders = db.collection('orders');
    
    const session = client.startSession();
    
    try {
        session.startTransaction();
        
        // Step 1: Check and update inventory for each item
        for (const item of orderItems) {
            const updateResult = await inventory.updateOne(
                {
                    productId: item.productId,
                    stock: { $gte: item.quantity } // Ensure sufficient stock
                },
                { $inc: { stock: -item.quantity } },
                { session }
            );
            
            if (updateResult.modifiedCount === 0) {
                throw new Error(`Insufficient stock for product ${item.productId}`);
            }
        }
        
        // Step 2: Create order (only if all inventory updates succeeded)
        const order = {
            orderId: `ORD${Date.now()}`,
            items: orderItems,
            status: 'completed',
            createdAt: new Date()
        };
        
        await orders.insertOne(order, { session });
        
        // Commit transaction
        await session.commitTransaction();
        console.log('✅ Order processed successfully');
        
        return order;
        
    } catch (error) {
        // Rollback all changes
        await session.abortTransaction();
        console.error('❌ Order failed, all changes rolled back:', error.message);
        throw error;
    } finally {
        await session.endSession();
        await client.close();
    }
}

// Usage:
try {
    const order = await processOrder([
        { productId: 'PROD001', quantity: 2 },
        { productId: 'PROD002', quantity: 5 }
    ]);
    console.log('Order created:', order.orderId);
} catch (error) {
    console.log('Order could not be processed:', error.message);
}
```

**Key Points:**
- Use transactions for multi-step operations
- Check conditions (like stock) before updating
- All operations in transaction use same session
- Rollback ensures data consistency

---

## Summary: Common Patterns

1. **Performance:** Create indexes on frequently queried fields
2. **Search:** Use text indexes for full-text search
3. **Consistency:** Use transactions for critical multi-step operations
4. **Data Quality:** Use validation rules to enforce schema
5. **Uniqueness:** Use unique indexes to prevent duplicates
6. **Complex Queries:** Use compound indexes for multi-field queries

---

**Remember:** 
- Indexes improve reads but slow down writes
- Transactions require replica set
- Validation adds overhead but ensures data quality
- Always test performance with explain() and monitor index usage

