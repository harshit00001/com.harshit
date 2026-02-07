/**
 * Example 3: Sharding Concepts
 * 
 * SIMPLE EXPLANATION:
 * Sharding splits your data across multiple servers.
 * Like dividing a large library into multiple buildings.
 * Each building (shard) holds a portion of the books (data).
 * 
 * TECHNICAL EXPLANATION:
 * Sharding distributes data across multiple servers (shards) based on shard key.
 * Enables horizontal scaling for large datasets.
 * MongoDB automatically routes queries to appropriate shards.
 * 
 * INTERVIEW POINT:
 * - Sharding requires sharded cluster (mongos, config servers, shards)
 * - Shard key determines data distribution
 * - Good shard key: high cardinality, even distribution, supports queries
 * - Chunks are automatically balanced across shards
 * - Queries without shard key are broadcast to all shards (inefficient)
 * 
 * NOTE: This example demonstrates concepts. Actual sharding setup
 * requires sharded cluster configuration (MongoDB Atlas or manual setup).
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        // ============================================
        // SHARDING CONCEPTS
        // ============================================
        console.log('📚 === SHARDING CONCEPTS ===\n');
        
        console.log('What is Sharding?');
        console.log('  - Splitting large dataset across multiple servers');
        console.log('  - Each server (shard) holds a portion of data');
        console.log('  - Enables horizontal scaling');
        console.log('  - Handles datasets too large for single server\n');
        
        console.log('Sharded Cluster Components:');
        console.log('  1. Mongos (Router): Routes queries to appropriate shards');
        console.log('  2. Config Servers: Store cluster metadata');
        console.log('  3. Shards: Store actual data (can be replica sets)');
        console.log('  4. Shard Key: Field(s) used to distribute data\n');
        
        // ============================================
        // SHARD KEY
        // ============================================
        console.log('🔑 === SHARD KEY ===\n');
        
        console.log('Shard Key Characteristics:');
        console.log('');
        console.log('1. High Cardinality:');
        console.log('   - Many unique values');
        console.log('   - Example: userId, orderId, email');
        console.log('   - Bad: status (only few values like active/inactive)\n');
        
        console.log('2. Even Distribution:');
        console.log('   - Values distributed evenly across shards');
        console.log('   - Avoids "hot shards" with most data');
        console.log('   - Example: Random UUID, hash of userId\n');
        
        console.log('3. Supports Common Queries:');
        console.log('   - Queries with shard key are efficient');
        console.log('   - Queries without shard key broadcast to all shards');
        console.log('   - Example: If shard key is userId, query by userId is fast\n');
        
        console.log('Example Shard Keys:');
        console.log('  ✅ Good: { userId: 1 }');
        console.log('  ✅ Good: { country: 1, userId: 1 } (compound)');
        console.log('  ❌ Bad: { status: 1 } (low cardinality)');
        console.log('  ❌ Bad: { createdAt: 1 } (monotonic, causes hot shard)\n');
        
        // ============================================
        // SHARDING STRATEGIES
        // ============================================
        console.log('📊 === SHARDING STRATEGIES ===\n');
        
        console.log('1. Range-Based Sharding:');
        console.log('   - Data split by value ranges');
        console.log('   - Example: userId 1-1000 on shard1, 1001-2000 on shard2');
        console.log('   - Pros: Efficient range queries');
        console.log('   - Cons: Can cause uneven distribution\n');
        
        console.log('2. Hash-Based Sharding:');
        console.log('   - Data split by hash of shard key');
        console.log('   - Example: hash(userId) determines shard');
        console.log('   - Pros: Even distribution');
        console.log('   - Cons: Inefficient range queries\n');
        
        console.log('3. Zone Sharding:');
        console.log('   - Manually assign data ranges to shards');
        console.log('   - Example: US data on shard1, EU data on shard2');
        console.log('   - Pros: Geographic distribution, compliance');
        console.log('   - Cons: Manual management\n');
        
        // ============================================
        // CHUNKS AND BALANCING
        // ============================================
        console.log('⚖️  === CHUNKS AND BALANCING ===\n');
        
        console.log('Chunks:');
        console.log('  - Data is divided into chunks (typically 64MB)');
        console.log('  - Each chunk contains range of shard key values');
        console.log('  - Chunks are distributed across shards\n');
        
        console.log('Balancing:');
        console.log('  - MongoDB automatically balances chunks');
        console.log('  - Moves chunks from overloaded to underloaded shards');
        console.log('  - Happens in background');
        console.log('  - Can be disabled for maintenance\n');
        
        // ============================================
        // QUERY ROUTING
        // ============================================
        console.log('🔍 === QUERY ROUTING ===\n');
        
        console.log('Query Types:');
        console.log('');
        console.log('1. Targeted Query (with shard key):');
        console.log('   - Routed to specific shard(s)');
        console.log('   - Very efficient');
        console.log('   - Example: find({ userId: "123" })\n');
        
        console.log('2. Scatter-Gather Query (without shard key):');
        console.log('   - Broadcast to all shards');
        console.log('   - Results merged by mongos');
        console.log('   - Less efficient');
        console.log('   - Example: find({ status: "active" })\n');
        
        console.log('3. Multi-Shard Query:');
        console.log('   - Queries multiple shards');
        console.log('   - Results merged');
        console.log('   - Example: find({ userId: { $in: ["123", "456"] } })\n');
        
        // ============================================
        // PRACTICAL EXAMPLE: SHARD KEY SELECTION
        // ============================================
        console.log('💡 === PRACTICAL EXAMPLE ===\n');
        
        console.log('Scenario: E-commerce orders collection');
        console.log('');
        console.log('Option 1: Shard by orderId');
        console.log('  ✅ High cardinality (unique per order)');
        console.log('  ✅ Even distribution');
        console.log('  ✅ Efficient: find({ orderId: "..." })');
        console.log('  ❌ Inefficient: find({ customerId: "..." })\n');
        
        console.log('Option 2: Shard by customerId');
        console.log('  ✅ High cardinality');
        console.log('  ⚠️  Distribution depends on customer order frequency');
        console.log('  ✅ Efficient: find({ customerId: "..." })');
        console.log('  ❌ Inefficient: find({ orderId: "..." })\n');
        
        console.log('Option 3: Compound shard key { customerId, orderId }');
        console.log('  ✅ High cardinality');
        console.log('  ✅ Supports both customer and order queries');
        console.log('  ✅ Better distribution');
        console.log('  ✅ Efficient: find({ customerId: "...", orderId: "..." })\n');
        
        // ============================================
        // SHARDING COMMANDS
        // ============================================
        console.log('🛠️  === SHARDING COMMANDS ===\n');
        
        console.log('Enable Sharding (run in mongos):');
        console.log('  sh.enableSharding("databaseName")\n');
        
        console.log('Create Sharded Collection:');
        console.log('  sh.shardCollection("databaseName.collectionName", { shardKey: 1 })\n');
        
        console.log('Check Sharding Status:');
        console.log('  sh.status()\n');
        
        console.log('Check Collection Distribution:');
        console.log('  db.collectionName.getShardDistribution()\n');
        
        // ============================================
        // WHEN TO SHARD
        // ============================================
        console.log('⏰ === WHEN TO SHARD ===\n');
        
        console.log('Consider Sharding When:');
        console.log('  ✅ Dataset exceeds single server capacity');
        console.log('  ✅ Write throughput exceeds single server');
        console.log('  ✅ Need geographic distribution');
        console.log('  ✅ Regulatory requirements (data locality)\n');
        
        console.log('Don\'t Shard If:');
        console.log('  ❌ Dataset fits on single server');
        console.log('  ❌ Can scale vertically (bigger server)');
        console.log('  ❌ No clear shard key');
        console.log('  ❌ Most queries don\'t include shard key\n');
        
        // ============================================
        // SHARDING BEST PRACTICES
        // ============================================
        console.log('⭐ === BEST PRACTICES ===\n');
        
        console.log('1. Choose Shard Key Carefully:');
        console.log('   - Cannot change shard key after sharding');
        console.log('   - Analyze query patterns first');
        console.log('   - Test with sample data\n');
        
        console.log('2. Monitor Chunk Distribution:');
        console.log('   - Check for uneven distribution');
        console.log('   - Monitor chunk migration');
        console.log('   - Adjust if needed\n');
        
        console.log('3. Design for Sharding:');
        console.log('   - Include shard key in common queries');
        console.log('   - Avoid queries without shard key');
        console.log('   - Consider compound shard keys\n');
        
        console.log('4. Test Before Production:');
        console.log('   - Test with production-like data');
        console.log('   - Verify query performance');
        console.log('   - Test failover scenarios\n');
        
        console.log('✅ Sharding concepts explained!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Sharding enables horizontal scaling');
        console.log('   - Shard key is critical and cannot be changed');
        console.log('   - Choose shard key based on query patterns');
        console.log('   - Targeted queries are much faster');
        console.log('   - Sharding adds complexity, use when needed');
        
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

