# MongoDB Advanced - Scenario-Based Questions & Answers

## 📚 Real-World Scenarios with Solutions

---

## Scenario 1: Sales Report with Aggregation

**Question:** You need to generate a monthly sales report showing:
- Total sales by product category
- Average order value
- Top 5 customers by total spending
- Sales trend over last 6 months

How would you implement this using MongoDB aggregation?

**Answer:**

```javascript
// Simple Explanation:
// Use aggregation pipeline to group, calculate, and transform data
// Like SQL GROUP BY but more powerful

async function generateSalesReport() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const orders = db.collection('orders');
    
    // 1. Total sales by category
    const salesByCategory = await orders.aggregate([
        { $match: { status: 'completed' } },
        { $unwind: '$items' },
        {
            $lookup: {
                from: 'products',
                localField: 'items.productId',
                foreignField: 'productId',
                as: 'product'
            }
        },
        { $unwind: '$product' },
        {
            $group: {
                _id: '$product.category',
                totalSales: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                orderCount: { $sum: 1 }
            }
        },
        { $sort: { totalSales: -1 } }
    ]).toArray();
    
    console.log('Sales by Category:');
    salesByCategory.forEach(cat => {
        console.log(`  ${cat._id}: $${cat.totalSales.toFixed(2)} (${cat.orderCount} orders)`);
    });
    
    // 2. Average order value
    const avgOrderValue = await orders.aggregate([
        { $match: { status: 'completed' } },
        { $unwind: '$items' },
        {
            $group: {
                _id: '$orderId',
                orderTotal: { $sum: { $multiply: ['$items.quantity', '$items.price'] } }
            }
        },
        {
            $group: {
                _id: null,
                avgOrderValue: { $avg: '$orderTotal' },
                totalOrders: { $sum: 1 }
            }
        }
    ]).toArray();
    
    console.log(`\nAverage Order Value: $${avgOrderValue[0].avgOrderValue.toFixed(2)}`);
    
    // 3. Top 5 customers
    const topCustomers = await orders.aggregate([
        { $match: { status: 'completed' } },
        { $unwind: '$items' },
        {
            $group: {
                _id: '$customerId',
                customerName: { $first: '$customerName' },
                totalSpent: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                orderCount: { $sum: 1 }
            }
        },
        { $sort: { totalSpent: -1 } },
        { $limit: 5 }
    ]).toArray();
    
    console.log('\nTop 5 Customers:');
    topCustomers.forEach((customer, i) => {
        console.log(`  ${i + 1}. ${customer.customerName}: $${customer.totalSpent.toFixed(2)} (${customer.orderCount} orders)`);
    });
    
    // 4. Sales trend (last 6 months)
    const sixMonthsAgo = new Date();
    sixMonthsAgo.setMonth(sixMonthsAgo.getMonth() - 6);
    
    const salesTrend = await orders.aggregate([
        { $match: { status: 'completed', date: { $gte: sixMonthsAgo } } },
        { $unwind: '$items' },
        {
            $group: {
                _id: {
                    year: { $year: '$date' },
                    month: { $month: '$date' }
                },
                totalSales: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                orderCount: { $sum: 1 }
            }
        },
        { $sort: { '_id.year': 1, '_id.month': 1 } }
    ]).toArray();
    
    console.log('\nSales Trend (Last 6 Months):');
    salesTrend.forEach(month => {
        console.log(`  ${month._id.year}-${String(month._id.month).padStart(2, '0')}: $${month.totalSales.toFixed(2)} (${month.orderCount} orders)`);
    });
    
    await client.close();
}
```

**Key Points:**
- Use `$match` early to filter data
- `$unwind` expands arrays for processing
- `$group` summarizes data
- `$lookup` joins collections
- `$sort` and `$limit` for top N results

---

## Scenario 2: Optimizing Slow Query

**Question:** A query that finds products by category and price range is taking 5+ seconds on a collection with 1 million documents. How do you optimize it?

**Answer:**

```javascript
// Simple Explanation:
// 1. Analyze the query to see what's slow
// 2. Create appropriate indexes
// 3. Verify the index is being used

async function optimizeSlowQuery() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const products = db.collection('products');
    
    // Step 1: Analyze current query performance
    console.log('Analyzing query performance...');
    const explain = await products
        .find({
            category: 'Electronics',
            price: { $gte: 100, $lte: 500 }
        })
        .sort({ rating: -1 })
        .explain('executionStats');
    
    console.log(`Current execution time: ${explain.executionStats.executionTimeMillis}ms`);
    console.log(`Documents examined: ${explain.executionStats.totalDocsExamined}`);
    console.log(`Index used: ${explain.executionStats.executionStages.stage || 'COLLSCAN'}\n`);
    
    // Step 2: Create compound index
    console.log('Creating compound index...');
    await products.createIndex({ category: 1, price: 1, rating: -1 });
    console.log('✅ Index created\n');
    
    // Step 3: Verify index is used
    const explainAfter = await products
        .find({
            category: 'Electronics',
            price: { $gte: 100, $lte: 500 }
        })
        .sort({ rating: -1 })
        .explain('executionStats');
    
    console.log(`New execution time: ${explainAfter.executionStats.executionTimeMillis}ms`);
    console.log(`Documents examined: ${explainAfter.executionStats.totalDocsExamined}`);
    console.log(`Index used: ${explainAfter.executionStats.executionStages.indexName}`);
    
    const improvement = ((explain.executionStats.executionTimeMillis - explainAfter.executionStats.executionTimeMillis) / explain.executionStats.executionTimeMillis * 100).toFixed(1);
    console.log(`⚡ Performance improvement: ${improvement}%\n`);
    
    // Step 4: Additional optimization - use projection
    console.log('Optimizing with projection...');
    const optimized = await products
        .find({
            category: 'Electronics',
            price: { $gte: 100, $lte: 500 }
        })
        .project({ name: 1, price: 1, rating: 1, _id: 0 }) // Only return needed fields
        .sort({ rating: -1 })
        .limit(20) // Limit results
        .toArray();
    
    console.log(`Returned ${optimized.length} products with only needed fields`);
    
    await client.close();
}
```

**Key Points:**
- Always use `explain()` to analyze queries
- Create compound indexes for multi-field queries
- Index field order matters (most selective first)
- Use projection to limit returned data
- Use `limit()` to restrict results

---

## Scenario 3: Replication for High Availability

**Question:** Your application needs 99.9% uptime. How do you set up MongoDB for high availability?

**Answer:**

```javascript
// Simple Explanation:
// Use replica set with multiple servers
// If one fails, others take over automatically

async function setupHighAvailability() {
    const client = new MongoClient('mongodb://localhost:27017', {
        // Connection options for replica set
        replicaSet: 'myReplicaSet',
        readPreference: 'primaryPreferred', // Read from primary, fallback to secondary
        writeConcern: { w: 'majority', j: true } // Wait for majority acknowledgment
    });
    
    await client.connect();
    const db = client.db('myapp');
    
    // Check replica set status
    const adminDb = db.admin();
    const status = await adminDb.command({ replSetGetStatus: 1 });
    
    console.log('Replica Set Status:');
    status.members.forEach(member => {
        console.log(`  ${member.name}: ${member.stateStr} (${member.health === 1 ? 'Healthy' : 'Unhealthy'})`);
    });
    
    // Write with majority concern
    const users = db.collection('users');
    await users.insertOne({
        name: 'John Doe',
        email: 'john@example.com'
    }, {
        writeConcern: { w: 'majority', j: true }
    });
    
    console.log('✅ Write completed with majority acknowledgment');
    
    // Read with primaryPreferred (high availability)
    const user = await users
        .find({ email: 'john@example.com' })
        .readPreference('primaryPreferred')
        .toArray();
    
    console.log('✅ Read completed');
    
    await client.close();
}

// Replica Set Setup (run in MongoDB shell):
/*
// Initialize replica set
rs.initiate({
    _id: "myReplicaSet",
    members: [
        { _id: 0, host: "server1:27017" },
        { _id: 1, host: "server2:27017" },
        { _id: 2, host: "server3:27017" }
    ]
})

// Check status
rs.status()
*/
```

**Key Points:**
- Replica sets provide automatic failover
- Use `primaryPreferred` for high availability reads
- Use `majority` write concern for durability
- Monitor replica set health
- Test failover scenarios

---

## Scenario 4: Sharding Large Dataset

**Question:** Your e-commerce database has 100 million products and is growing. Single server can't handle it. How do you scale horizontally?

**Answer:**

```javascript
// Simple Explanation:
// Shard the collection across multiple servers
// Choose a good shard key to distribute data evenly

async function setupSharding() {
    // Connect to mongos (router)
    const client = new MongoClient('mongodb://mongos:27017');
    await client.connect();
    const db = client.db('ecommerce');
    
    // Shard key selection is critical
    // Good shard key: { productId: 1 } or { category: 1, productId: 1 }
    // Bad shard key: { status: 1 } (low cardinality)
    
    const products = db.collection('products');
    
    // Queries with shard key are efficient
    const product = await products.findOne({ productId: 'P12345' });
    console.log('Found product:', product.name);
    
    // Queries without shard key are broadcast to all shards (slower)
    // Try to include shard key in queries when possible
    const electronics = await products.find({
        category: 'Electronics',
        productId: { $gte: 'P10000', $lte: 'P20000' } // Include shard key
    }).toArray();
    
    await client.close();
}

// Sharding Setup (run in mongos):
/*
// Enable sharding on database
sh.enableSharding("ecommerce")

// Shard collection with shard key
sh.shardCollection("ecommerce.products", { productId: 1 })

// Check sharding status
sh.status()

// Check data distribution
db.products.getShardDistribution()
*/
```

**Key Points:**
- Sharding enables horizontal scaling
- Shard key cannot be changed after sharding
- Choose shard key with high cardinality and even distribution
- Include shard key in queries when possible
- Monitor chunk distribution and balancing

---

## Scenario 5: Complex Data Transformation

**Question:** You need to transform order data to show:
- Customer purchase history with product details
- Total spending per customer
- Favorite category per customer
- Last purchase date

How do you do this with aggregation?

**Answer:**

```javascript
// Simple Explanation:
// Use aggregation pipeline to join, group, and transform data
// Multiple stages process data step by step

async function transformOrderData() {
    const client = new MongoClient('mongodb://localhost:27017');
    await client.connect();
    const db = client.db('ecommerce');
    const orders = db.collection('orders');
    const products = db.collection('products');
    
    const customerHistory = await orders.aggregate([
        // Stage 1: Filter completed orders
        { $match: { status: 'completed' } },
        
        // Stage 2: Expand order items
        { $unwind: '$items' },
        
        // Stage 3: Join with products collection
        {
            $lookup: {
                from: 'products',
                localField: 'items.productId',
                foreignField: 'productId',
                as: 'productDetails'
            }
        },
        { $unwind: '$productDetails' },
        
        // Stage 4: Group by customer
        {
            $group: {
                _id: '$customerId',
                customerName: { $first: '$customerName' },
                totalSpent: { $sum: { $multiply: ['$items.quantity', '$items.price'] } },
                orderCount: { $addToSet: '$orderId' },
                lastPurchaseDate: { $max: '$date' },
                purchases: {
                    $push: {
                        orderId: '$orderId',
                        productName: '$productDetails.name',
                        category: '$productDetails.category',
                        quantity: '$items.quantity',
                        price: '$items.price',
                        date: '$date'
                    }
                },
                categories: { $addToSet: '$productDetails.category' }
            }
        },
        
        // Stage 5: Find favorite category
        {
            $addFields: {
                orderCount: { $size: '$orderCount' },
                favoriteCategory: {
                    $arrayElemAt: [
                        {
                            $map: {
                                input: '$categories',
                                as: 'cat',
                                in: {
                                    category: '$$cat',
                                    count: {
                                        $size: {
                                            $filter: {
                                                input: '$purchases',
                                                cond: { $eq: ['$$this.category', '$$cat'] }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        0
                    ]
                }
            }
        },
        
        // Stage 6: Sort by total spending
        { $sort: { totalSpent: -1 } },
        
        // Stage 7: Limit to top customers
        { $limit: 10 }
    ]).toArray();
    
    customerHistory.forEach(customer => {
        console.log(`\n${customer.customerName}:`);
        console.log(`  Total Spent: $${customer.totalSpent.toFixed(2)}`);
        console.log(`  Orders: ${customer.orderCount}`);
        console.log(`  Last Purchase: ${customer.lastPurchaseDate.toISOString().split('T')[0]}`);
        console.log(`  Favorite Category: ${customer.favoriteCategory?.category || 'N/A'}`);
        console.log(`  Purchase History: ${customer.purchases.length} items`);
    });
    
    await client.close();
}
```

**Key Points:**
- Use `$lookup` to join collections
- `$group` to summarize by customer
- `$addFields` for calculated fields
- `$unwind` to expand arrays
- Chain stages for complex transformations

---

## Summary: Advanced Patterns

1. **Aggregation:** Use pipeline stages efficiently, `$match` early, `$project` to reduce data
2. **Performance:** Create indexes, use `explain()`, optimize queries, use projection
3. **Replication:** Use replica sets for high availability, configure read preferences and write concerns
4. **Sharding:** Choose shard key carefully, include in queries, monitor distribution
5. **Monitoring:** Use profiling, analyze slow queries, monitor indexes

---

**Remember:**
- Always analyze queries with `explain()`
- Indexes are critical for performance
- Aggregation pipelines can be optimized
- Replication and sharding add complexity but enable scale
- Monitor and profile your database regularly

