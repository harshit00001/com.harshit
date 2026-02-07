/**
 * Example 1: MongoDB Indexes
 * 
 * SIMPLE EXPLANATION:
 * Indexes are like a book's index - they help MongoDB find data quickly.
 * Without indexes, MongoDB has to check every document (like reading every page).
 * With indexes, MongoDB can jump directly to the right data (like using an index).
 * 
 * TECHNICAL EXPLANATION:
 * Indexes are data structures that improve query performance.
 * They store a sorted representation of field values with pointers to documents.
 * Trade-off: Faster queries but slower writes and more storage space.
 * 
 * INTERVIEW POINT:
 * - Indexes speed up find(), sort(), and some update operations
 * - Compound indexes support queries on multiple fields
 * - Index order matters: {a: 1, b: 1} is different from {b: 1, a: 1}
 * - Too many indexes can slow down writes
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const productsCollection = db.collection('products');
        
        // Clean up any existing data
        await productsCollection.deleteMany({});
        await productsCollection.dropIndexes(); // Remove existing indexes
        
        // ============================================
        // INSERT SAMPLE DATA
        // ============================================
        console.log('📝 Inserting sample products...\n');
        const products = [];
        const categories = ['Electronics', 'Clothing', 'Books', 'Sports', 'Home'];
        const brands = ['BrandA', 'BrandB', 'BrandC', 'BrandD'];
        
        // Create 1000 products for performance testing
        for (let i = 1; i <= 1000; i++) {
            products.push({
                name: `Product ${i}`,
                category: categories[i % categories.length],
                brand: brands[i % brands.length],
                price: Math.floor(Math.random() * 1000) + 10,
                stock: Math.floor(Math.random() * 100),
                rating: (Math.random() * 2 + 3).toFixed(1), // 3.0 to 5.0
                createdAt: new Date(Date.now() - Math.random() * 365 * 24 * 60 * 60 * 1000)
            });
        }
        
        await productsCollection.insertMany(products);
        console.log(`✅ Inserted ${products.length} products\n`);
        
        // ============================================
        // QUERY WITHOUT INDEX (SLOW)
        // ============================================
        console.log('⏱️  === PERFORMANCE TEST: WITHOUT INDEX ===\n');
        
        const startTime1 = Date.now();
        const results1 = await productsCollection
            .find({ category: 'Electronics' })
            .toArray();
        const endTime1 = Date.now();
        const timeWithoutIndex = endTime1 - startTime1;
        
        console.log(`Query time WITHOUT index: ${timeWithoutIndex}ms`);
        console.log(`Found ${results1.length} Electronics products\n`);
        
        // ============================================
        // CREATE SINGLE FIELD INDEX
        // ============================================
        console.log('📊 === CREATING INDEXES ===\n');
        
        console.log('1. Creating index on "category" field...');
        await productsCollection.createIndex({ category: 1 });
        console.log('   ✅ Index created on category\n');
        
        // ============================================
        // QUERY WITH INDEX (FAST)
        // ============================================
        console.log('⏱️  === PERFORMANCE TEST: WITH INDEX ===\n');
        
        const startTime2 = Date.now();
        const results2 = await productsCollection
            .find({ category: 'Electronics' })
            .toArray();
        const endTime2 = Date.now();
        const timeWithIndex = endTime2 - startTime2;
        
        console.log(`Query time WITH index: ${timeWithIndex}ms`);
        console.log(`Found ${results2.length} Electronics products`);
        console.log(`⚡ Speed improvement: ${((timeWithoutIndex - timeWithIndex) / timeWithoutIndex * 100).toFixed(1)}% faster\n`);
        
        // ============================================
        // LIST ALL INDEXES
        // ============================================
        console.log('📋 === LISTING INDEXES ===\n');
        
        const indexes = await productsCollection.indexes();
        console.log('Current indexes:');
        indexes.forEach((index, i) => {
            console.log(`   ${i + 1}. ${index.name}: ${JSON.stringify(index.key)}`);
        });
        console.log('');
        
        // ============================================
        // COMPOUND INDEX
        // ============================================
        console.log('🔗 === COMPOUND INDEX ===\n');
        
        console.log('2. Creating compound index on category and price...');
        await productsCollection.createIndex({ category: 1, price: 1 });
        console.log('   ✅ Compound index created\n');
        
        // Test compound index query
        console.log('Testing query on category and price (uses compound index):');
        const startTime3 = Date.now();
        const expensiveElectronics = await productsCollection
            .find({
                category: 'Electronics',
                price: { $gt: 500 }
            })
            .sort({ price: 1 })
            .toArray();
        const endTime3 = Date.now();
        
        console.log(`   Query time: ${endTime3 - startTime3}ms`);
        console.log(`   Found ${expensiveElectronics.length} expensive Electronics products\n`);
        
        // ============================================
        // UNIQUE INDEX
        // ============================================
        console.log('🔒 === UNIQUE INDEX ===\n');
        
        // Create a collection for unique emails
        const usersCollection = db.collection('users');
        await usersCollection.deleteMany({});
        
        console.log('3. Creating unique index on email field...');
        await usersCollection.createIndex({ email: 1 }, { unique: true });
        console.log('   ✅ Unique index created\n');
        
        // Insert user with unique email
        console.log('4. Inserting user with email...');
        await usersCollection.insertOne({
            name: 'John Doe',
            email: 'john@example.com'
        });
        console.log('   ✅ User inserted\n');
        
        // Try to insert duplicate email (should fail)
        console.log('5. Trying to insert duplicate email...');
        try {
            await usersCollection.insertOne({
                name: 'Jane Doe',
                email: 'john@example.com' // Duplicate!
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly prevented duplicate email');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // ============================================
        // INDEX ON NESTED FIELD
        // ============================================
        console.log('📦 === INDEX ON NESTED FIELD ===\n');
        
        const ordersCollection = db.collection('orders');
        await ordersCollection.deleteMany({});
        
        console.log('6. Creating index on nested field "user.email"...');
        await ordersCollection.createIndex({ 'user.email': 1 });
        console.log('   ✅ Index created on nested field\n');
        
        // Insert sample order
        await ordersCollection.insertOne({
            orderId: 'ORD001',
            user: {
                name: 'John',
                email: 'john@example.com'
            },
            total: 299.99
        });
        
        // Query using nested field index
        const ordersByEmail = await ordersCollection
            .find({ 'user.email': 'john@example.com' })
            .toArray();
        console.log(`   Found ${ordersByEmail.length} orders for john@example.com\n`);
        
        // ============================================
        // EXPLAIN QUERY PLAN
        // ============================================
        console.log('🔍 === QUERY EXPLANATION ===\n');
        
        console.log('7. Analyzing query execution plan...');
        const explainResult = await productsCollection
            .find({ category: 'Electronics' })
            .explain('executionStats');
        
        console.log('   Query execution stats:');
        console.log(`   - Execution time: ${explainResult.executionStats.executionTimeMillis}ms`);
        console.log(`   - Documents examined: ${explainResult.executionStats.totalDocsExamined}`);
        console.log(`   - Documents returned: ${explainResult.executionStats.nReturned}`);
        console.log(`   - Index used: ${explainResult.executionStats.executionStages.indexName || 'Collection Scan'}\n`);
        
        // ============================================
        // DROP INDEX
        // ============================================
        console.log('🗑️  === DROPPING INDEX ===\n');
        
        console.log('8. Dropping index on category...');
        await productsCollection.dropIndex('category_1');
        console.log('   ✅ Index dropped\n');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await productsCollection.deleteMany({});
        await usersCollection.deleteMany({});
        await ordersCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Index examples completed successfully!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Indexes dramatically improve query performance');
        console.log('   - Create indexes on frequently queried fields');
        console.log('   - Compound indexes support multi-field queries');
        console.log('   - Unique indexes prevent duplicate values');
        console.log('   - Use explain() to analyze query performance');
        
    } catch (error) {
        console.error('❌ Error:', error.message);
        console.error(error);
    } finally {
        if (client) {
            await closeConnection(client);
        }
    }
}

// Run the example
main();

