/**
 * Example 4: Performance Optimization
 * 
 * SIMPLE EXPLANATION:
 * Performance optimization is about making queries faster
 * and using resources efficiently.
 * 
 * TECHNICAL EXPLANATION:
 * Involves proper indexing, query optimization, connection pooling,
 * monitoring, and understanding execution plans.
 * 
 * INTERVIEW POINT:
 * - Use explain() to analyze queries
 * - Create indexes on frequently queried fields
 * - Avoid collection scans
 * - Use projection to limit returned data
 * - Monitor slow queries
 * - Optimize aggregation pipelines
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const productsCollection = db.collection('products');
        
        // Clean up
        await productsCollection.deleteMany({});
        await productsCollection.dropIndexes();
        
        // ============================================
        // INSERT SAMPLE DATA
        // ============================================
        console.log('📝 Inserting sample data for performance testing...\n');
        
        const products = [];
        const categories = ['Electronics', 'Clothing', 'Books', 'Sports', 'Home'];
        const brands = ['BrandA', 'BrandB', 'BrandC', 'BrandD', 'BrandE'];
        
        // Create 5000 products
        for (let i = 1; i <= 5000; i++) {
            products.push({
                productId: `P${String(i).padStart(5, '0')}`,
                name: `Product ${i}`,
                category: categories[i % categories.length],
                brand: brands[i % brands.length],
                price: Math.floor(Math.random() * 1000) + 10,
                stock: Math.floor(Math.random() * 100),
                rating: (Math.random() * 2 + 3).toFixed(1),
                description: `Description for product ${i}`,
                tags: [`tag${i % 10}`, `tag${(i + 1) % 10}`],
                createdAt: new Date(Date.now() - Math.random() * 365 * 24 * 60 * 60 * 1000)
            });
        }
        
        await productsCollection.insertMany(products);
        console.log(`✅ Inserted ${products.length} products\n`);
        
        // ============================================
        // ANALYZING QUERY PERFORMANCE
        // ============================================
        console.log('🔍 === ANALYZING QUERY PERFORMANCE ===\n');
        
        console.log('1. Query WITHOUT index (slow):');
        const explain1 = await productsCollection
            .find({ category: 'Electronics' })
            .explain('executionStats');
        
        console.log(`   Execution time: ${explain1.executionStats.executionTimeMillis}ms`);
        console.log(`   Documents examined: ${explain1.executionStats.totalDocsExamined}`);
        console.log(`   Documents returned: ${explain1.executionStats.nReturned}`);
        console.log(`   Index used: ${explain1.executionStats.executionStages.stage || 'COLLSCAN'}`);
        console.log('');
        
        // Create index
        console.log('2. Creating index on category...');
        await productsCollection.createIndex({ category: 1 });
        console.log('   ✅ Index created\n');
        
        console.log('3. Same query WITH index (fast):');
        const explain2 = await productsCollection
            .find({ category: 'Electronics' })
            .explain('executionStats');
        
        console.log(`   Execution time: ${explain2.executionStats.executionTimeMillis}ms`);
        console.log(`   Documents examined: ${explain2.executionStats.totalDocsExamined}`);
        console.log(`   Documents returned: ${explain2.executionStats.nReturned}`);
        console.log(`   Index used: ${explain2.executionStats.executionStages.indexName || 'COLLSCAN'}`);
        
        const improvement = ((explain1.executionStats.executionTimeMillis - explain2.executionStats.executionTimeMillis) / explain1.executionStats.executionTimeMillis * 100).toFixed(1);
        console.log(`   ⚡ Performance improvement: ${improvement}%\n`);
        
        // ============================================
        // PROJECTION FOR PERFORMANCE
        // ============================================
        console.log('📊 === PROJECTION FOR PERFORMANCE ===\n');
        
        console.log('4. Query with ALL fields (slow):');
        const start1 = Date.now();
        const allFields = await productsCollection
            .find({ category: 'Electronics' })
            .limit(100)
            .toArray();
        const time1 = Date.now() - start1;
        console.log(`   Time: ${time1}ms`);
        console.log(`   Data size: ~${JSON.stringify(allFields[0]).length} bytes per document\n`);
        
        console.log('5. Query with SELECTED fields only (fast):');
        const start2 = Date.now();
        const selectedFields = await productsCollection
            .find({ category: 'Electronics' })
            .project({ name: 1, price: 1, _id: 0 })
            .limit(100)
            .toArray();
        const time2 = Date.now() - start2;
        console.log(`   Time: ${time2}ms`);
        console.log(`   Data size: ~${JSON.stringify(selectedFields[0]).length} bytes per document`);
        console.log(`   ⚡ Improvement: ${((time1 - time2) / time1 * 100).toFixed(1)}% faster\n`);
        
        // ============================================
        // INDEX SELECTIVITY
        // ============================================
        console.log('🎯 === INDEX SELECTIVITY ===\n');
        
        console.log('6. Creating indexes on different fields...');
        
        // Low selectivity index (few unique values)
        await productsCollection.createIndex({ category: 1, brand: 1 });
        
        // High selectivity index (many unique values)
        await productsCollection.createIndex({ productId: 1 });
        
        console.log('   ✅ Indexes created\n');
        
        console.log('Index Selectivity:');
        console.log('  - High selectivity: Many unique values (productId)');
        console.log('  - Low selectivity: Few unique values (category)');
        console.log('  - High selectivity indexes are more efficient\n');
        
        // ============================================
        // COMPOUND INDEX ORDER
        // ============================================
        console.log('🔗 === COMPOUND INDEX ORDER ===\n');
        
        console.log('7. Testing compound index order...');
        
        // Create compound index: category, price, rating
        await productsCollection.createIndex({ category: 1, price: 1, rating: -1 });
        
        // Query that uses index efficiently
        const efficientQuery = await productsCollection
            .find({
                category: 'Electronics',
                price: { $gte: 100, $lte: 500 }
            })
            .sort({ rating: -1 })
            .explain('executionStats');
        
        console.log(`   Query execution time: ${efficientQuery.executionStats.executionTimeMillis}ms`);
        console.log(`   Index used: ${efficientQuery.executionStats.executionStages.indexName || 'N/A'}\n`);
        
        console.log('Compound Index Rule:');
        console.log('  - Order matters: Most selective field first');
        console.log('  - Index supports queries on prefix of fields');
        console.log('  - {a:1, b:1, c:1} supports queries on: a, (a,b), (a,b,c)\n');
        
        // ============================================
        // AGGREGATION PIPELINE OPTIMIZATION
        // ============================================
        console.log('⚙️  === AGGREGATION OPTIMIZATION ===\n');
        
        console.log('8. Optimized aggregation pipeline:');
        
        // Bad: $match after $group (processes all documents first)
        console.log('   Bad order: $group then $match');
        const badPipeline = [
            { $group: { _id: '$category', count: { $sum: 1 } } },
            { $match: { count: { $gt: 1000 } } }
        ];
        
        // Good: $match before $group (filters first)
        console.log('   Good order: $match then $group');
        const goodPipeline = [
            { $match: { category: 'Electronics' } }, // Filter early
            { $group: { _id: '$category', count: { $sum: 1 } } }
        ];
        
        const goodResult = await productsCollection.aggregate(goodPipeline).toArray();
        console.log(`   Result: ${goodResult.length} groups\n`);
        
        console.log('Pipeline Optimization Rules:');
        console.log('  1. $match early to reduce documents');
        console.log('  2. $project early to reduce data size');
        console.log('  3. $sort before $group if possible');
        console.log('  4. Use indexes with $match\n');
        
        // ============================================
        // MONITORING SLOW QUERIES
        // ============================================
        console.log('📈 === MONITORING SLOW QUERIES ===\n');
        
        console.log('9. Enabling profiling for slow queries...');
        
        // Set profiling level (0=off, 1=slow, 2=all)
        await db.setProfilingLevel(1, { slowms: 100 }); // Log queries > 100ms
        console.log('   ✅ Profiling enabled (logs queries > 100ms)\n');
        
        // Run a slow query
        await productsCollection.find({ description: /test/ }).toArray();
        
        // Check profile collection
        const profileCollection = db.collection('system.profile');
        const slowQueries = await profileCollection
            .find({})
            .sort({ ts: -1 })
            .limit(5)
            .toArray();
        
        if (slowQueries.length > 0) {
            console.log('   Recent slow queries:');
            slowQueries.forEach((query, i) => {
                console.log(`   ${i + 1}. ${query.command.find || 'aggregate'} - ${query.millis}ms`);
            });
        } else {
            console.log('   No slow queries logged yet');
        }
        console.log('');
        
        // Disable profiling
        await db.setProfilingLevel(0);
        console.log('   Profiling disabled\n');
        
        // ============================================
        // CONNECTION POOLING
        // ============================================
        console.log('🔌 === CONNECTION POOLING ===\n');
        
        console.log('10. Connection Pool Configuration:');
        console.log(`
const client = new MongoClient(uri, {
    maxPoolSize: 10,        // Maximum connections in pool
    minPoolSize: 5,         // Minimum connections in pool
    maxIdleTimeMS: 30000,  // Close idle connections after 30s
    serverSelectionTimeoutMS: 5000
});
        `);
        console.log('');
        
        console.log('Connection Pooling Benefits:');
        console.log('  - Reuse connections (faster)');
        console.log('  - Limit concurrent connections');
        console.log('  - Better resource management\n');
        
        // ============================================
        // BEST PRACTICES SUMMARY
        // ============================================
        console.log('⭐ === PERFORMANCE BEST PRACTICES ===\n');
        
        console.log('1. Indexing:');
        console.log('   ✅ Create indexes on frequently queried fields');
        console.log('   ✅ Use compound indexes for multi-field queries');
        console.log('   ✅ Monitor index usage, remove unused indexes');
        console.log('   ❌ Don\'t create too many indexes (slows writes)\n');
        
        console.log('2. Queries:');
        console.log('   ✅ Use projection to limit returned data');
        console.log('   ✅ Use limit() to restrict results');
        console.log('   ✅ Include indexed fields in queries');
        console.log('   ❌ Avoid queries without indexes on large collections\n');
        
        console.log('3. Aggregation:');
        console.log('   ✅ $match early in pipeline');
        console.log('   ✅ $project early to reduce data');
        console.log('   ✅ Use indexes with $match');
        console.log('   ❌ Don\'t process all documents unnecessarily\n');
        
        console.log('4. Monitoring:');
        console.log('   ✅ Use explain() to analyze queries');
        console.log('   ✅ Enable profiling for slow queries');
        console.log('   ✅ Monitor index usage');
        console.log('   ✅ Check connection pool metrics\n');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await productsCollection.deleteMany({});
        await profileCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Performance optimization examples completed!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Indexes are critical for performance');
        console.log('   - Use explain() to analyze queries');
        console.log('   - Project only needed fields');
        console.log('   - Optimize aggregation pipeline order');
        console.log('   - Monitor and profile slow queries');
        console.log('   - Use connection pooling');
        
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

