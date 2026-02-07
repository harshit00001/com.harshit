# MongoDB Basics - Scenario-Based Questions & Answers

## 📚 Real-World Scenarios with Solutions

---

## Scenario 1: E-Commerce User Registration

**Question:** You're building an e-commerce website. When a new user registers, you need to store their information including name, email, password (hashed), address, and phone number. How would you do this in MongoDB?

**Answer:**

```javascript
// Simple Explanation:
// Create a document with all user information and save it to the 'users' collection

const { MongoClient } = require('mongodb');
const bcrypt = require('bcrypt');

async function registerUser(userData) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const users = db.collection('users');
    
    // Hash password before storing
    const hashedPassword = await bcrypt.hash(userData.password, 10);
    
    const newUser = {
        name: userData.name,
        email: userData.email,
        password: hashedPassword, // Never store plain passwords!
        address: {
            street: userData.street,
            city: userData.city,
            state: userData.state,
            zipCode: userData.zipCode
        },
        phone: userData.phone,
        createdAt: new Date(),
        status: 'active'
    };
    
    const result = await users.insertOne(newUser);
    await client.close();
    
    return result.insertedId;
}

// Usage:
registerUser({
    name: 'John Doe',
    email: 'john@example.com',
    password: 'securePassword123',
    street: '123 Main St',
    city: 'New York',
    state: 'NY',
    zipCode: '10001',
    phone: '555-1234'
});
```

**Key Points:**
- Always hash passwords before storing
- Store related data (address) as embedded document
- Include timestamps for tracking
- Use meaningful field names

---

## Scenario 2: Finding Products by Category and Price Range

**Question:** Your e-commerce site needs to show products in the "Electronics" category that cost between $100 and $500, sorted by price. Write the MongoDB query.

**Answer:**

```javascript
// Simple Explanation:
// Find products where category is "Electronics" AND price is between $100 and $500
// Then sort them by price from low to high

async function getElectronicsInRange() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    const results = await products
        .find({
            category: 'Electronics',
            price: { $gte: 100, $lte: 500 }
        })
        .sort({ price: 1 }) // 1 = ascending (low to high)
        .toArray();
    
    await client.close();
    return results;
}
```

**Key Points:**
- Use `$gte` (greater than or equal) and `$lte` (less than or equal) for ranges
- Chain `.sort()` after `.find()`
- `1` means ascending, `-1` means descending

---

## Scenario 3: User Login Verification

**Question:** A user tries to log in. You need to find the user by email and verify their password. How would you implement this?

**Answer:**

```javascript
// Simple Explanation:
// 1. Find user by email
// 2. Compare provided password with stored hashed password
// 3. Return user if password matches

const bcrypt = require('bcrypt');

async function loginUser(email, password) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const users = db.collection('users');
    
    // Find user by email
    const user = await users.findOne({ email: email });
    
    if (!user) {
        await client.close();
        return { success: false, message: 'User not found' };
    }
    
    // Verify password
    const passwordMatch = await bcrypt.compare(password, user.password);
    
    if (!passwordMatch) {
        await client.close();
        return { success: false, message: 'Invalid password' };
    }
    
    // Remove password from response (never send password back!)
    delete user.password;
    
    await client.close();
    return { success: true, user: user };
}

// Usage:
const result = await loginUser('john@example.com', 'securePassword123');
if (result.success) {
    console.log('Login successful!', result.user);
} else {
    console.log('Login failed:', result.message);
}
```

**Key Points:**
- Use `findOne()` to find a single document
- Always compare hashed passwords, never plain text
- Never return password in response
- Handle cases where user doesn't exist

---

## Scenario 4: Updating User Profile

**Question:** A user wants to update their profile information (name, phone, address). Some fields might be optional. How do you update only the fields that are provided?

**Answer:**

```javascript
// Simple Explanation:
// Build an update object with only the fields that need to be updated
// Use $set operator to update specific fields

async function updateUserProfile(userId, updates) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const users = db.collection('users');
    
    // Build update object with only provided fields
    const updateFields = {};
    
    if (updates.name) updateFields.name = updates.name;
    if (updates.phone) updateFields.phone = updates.phone;
    
    // Handle nested address fields
    if (updates.address) {
        if (updates.address.street) updateFields['address.street'] = updates.address.street;
        if (updates.address.city) updateFields['address.city'] = updates.address.city;
        if (updates.address.state) updateFields['address.state'] = updates.address.state;
        if (updates.address.zipCode) updateFields['address.zipCode'] = updates.address.zipCode;
    }
    
    // Add updated timestamp
    updateFields.updatedAt = new Date();
    
    const result = await users.updateOne(
        { _id: userId },
        { $set: updateFields }
    );
    
    await client.close();
    return result.modifiedCount > 0;
}

// Usage:
await updateUserProfile(
    userId,
    {
        name: 'John Smith', // Update name
        phone: '555-9999',  // Update phone
        address: {
            city: 'Boston'  // Only update city, keep other address fields
        }
    }
);
```

**Key Points:**
- Use `$set` to update specific fields without replacing entire document
- Use dot notation for nested fields: `'address.city'`
- Always include `updatedAt` timestamp
- Check which fields are provided before updating

---

## Scenario 5: Finding Active Users with Multiple Conditions

**Question:** You need to find all active users who registered in the last 30 days and have made at least one purchase. Write the query.

**Answer:**

```javascript
// Simple Explanation:
// Find users where:
// - status is 'active'
// - createdAt is within last 30 days
// - orders array is not empty

async function getRecentActiveCustomers() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const users = db.collection('users');
    
    // Calculate date 30 days ago
    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
    
    const customers = await users.find({
        status: 'active',
        createdAt: { $gte: thirtyDaysAgo },
        orders: { $exists: true, $ne: [] } // Has orders and orders array is not empty
    }).toArray();
    
    await client.close();
    return customers;
}
```

**Key Points:**
- Calculate dates dynamically for time-based queries
- Use `$exists: true` to check field exists
- Use `$ne: []` to check array is not empty
- Combine multiple conditions in single query

---

## Scenario 6: Adding Items to Shopping Cart

**Question:** A user adds items to their shopping cart. The cart is stored as an array in the user document. How do you add an item without creating duplicates?

**Answer:**

```javascript
// Simple Explanation:
// Use $addToSet to add item to cart array only if it doesn't already exist
// Or use $push if duplicates are allowed

async function addToCart(userId, productId, quantity) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const users = db.collection('users');
    
    const cartItem = {
        productId: productId,
        quantity: quantity,
        addedAt: new Date()
    };
    
    // Option 1: Prevent duplicates using $addToSet
    // This only works if the entire object is unique
    await users.updateOne(
        { _id: userId },
        { $addToSet: { cart: cartItem } }
    );
    
    // Option 2: Check if item exists first, then update or add
    const user = await users.findOne({ _id: userId });
    const existingItemIndex = user.cart.findIndex(
        item => item.productId === productId
    );
    
    if (existingItemIndex >= 0) {
        // Update quantity if item exists
        await users.updateOne(
            { _id: userId, 'cart.productId': productId },
            { $inc: { 'cart.$.quantity': quantity } }
        );
    } else {
        // Add new item if it doesn't exist
        await users.updateOne(
            { _id: userId },
            { $push: { cart: cartItem } }
        );
    }
    
    await client.close();
}

// Usage:
await addToCart(userId, 'product123', 2);
```

**Key Points:**
- `$addToSet` prevents duplicates but checks entire object
- `$push` always adds, even if duplicate
- Use positional operator `$` to update specific array element
- Check for existing items before adding for better control

---

## Scenario 7: Pagination for Product List

**Question:** You need to display products in pages of 20 items each. How do you implement pagination?

**Answer:**

```javascript
// Simple Explanation:
// Use skip() to skip previous pages and limit() to get only current page items
// Calculate skip value: (pageNumber - 1) * itemsPerPage

async function getProductsPage(pageNumber, itemsPerPage = 20) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    // Calculate how many documents to skip
    const skip = (pageNumber - 1) * itemsPerPage;
    
    // Get total count for pagination info
    const totalProducts = await products.countDocuments({});
    const totalPages = Math.ceil(totalProducts / itemsPerPage);
    
    // Get products for current page
    const pageProducts = await products
        .find({})
        .sort({ createdAt: -1 }) // Sort by newest first
        .skip(skip)
        .limit(itemsPerPage)
        .toArray();
    
    await client.close();
    
    return {
        products: pageProducts,
        pagination: {
            currentPage: pageNumber,
            itemsPerPage: itemsPerPage,
            totalItems: totalProducts,
            totalPages: totalPages,
            hasNextPage: pageNumber < totalPages,
            hasPreviousPage: pageNumber > 1
        }
    };
}

// Usage:
const page1 = await getProductsPage(1, 20); // First page, 20 items
const page2 = await getProductsPage(2, 20); // Second page, 20 items
```

**Key Points:**
- Always use `skip()` and `limit()` together
- Calculate `skip` as `(page - 1) * limit`
- Get total count for pagination metadata
- Consider performance: `skip()` can be slow for large offsets

---

## Scenario 8: Searching Users by Multiple Tags

**Question:** Users have tags like "premium", "verified", "active". You need to find users who have ALL of these tags. How do you query?

**Answer:**

```javascript
// Simple Explanation:
// Use $all operator to find documents where array contains all specified values

async function getUsersWithAllTags(requiredTags) {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const users = db.collection('users');
    
    const users = await usersCollection.find({
        tags: { $all: requiredTags }
    }).toArray();
    
    await client.close();
    return users;
}

// Usage:
const premiumVerifiedActive = await getUsersWithAllTags(['premium', 'verified', 'active']);

// This finds users who have ALL three tags
```

**Key Points:**
- `$all` requires ALL values to be present
- `$in` requires ANY value to be present
- Order doesn't matter with `$all`
- Useful for filtering by multiple criteria

---

## Summary: Common Patterns

1. **Registration/Login:** Insert documents, find by unique field (email), verify credentials
2. **Search/Filter:** Use comparison operators ($gt, $lt, $gte, $lte) and logical operators ($and, $or)
3. **Updates:** Use `$set` for specific fields, dot notation for nested fields
4. **Arrays:** Use `$push`, `$addToSet`, `$pull` for array operations
5. **Pagination:** Use `skip()` and `limit()` with calculated offset
6. **Multiple Conditions:** Combine operators in single query object

---

**Remember:** Always close database connections, handle errors, and never store sensitive data (like passwords) in plain text!

