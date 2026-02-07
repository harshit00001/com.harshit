/**
 * Example 2: Replication Concepts
 * 
 * SIMPLE EXPLANATION:
 * Replication is like having backup copies of your data.
 * If one server fails, others can take over automatically.
 * 
 * TECHNICAL EXPLANATION:
 * Replica sets maintain multiple copies of data across servers.
 * One primary handles writes, others (secondaries) replicate data.
 * Automatic failover ensures high availability.
 * 
 * INTERVIEW POINT:
 * - Replica sets require at least 3 members (or 1 primary + 1 secondary + 1 arbiter)
 * - Primary handles all writes
 * - Secondaries replicate from primary
 * - Read preferences control where reads go
 * - Write concerns control write acknowledgment
 * 
 * NOTE: This example demonstrates concepts. Actual replica set setup
 * requires multiple MongoDB instances or MongoDB Atlas.
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        // ============================================
        // REPLICA SET CONCEPTS
        // ============================================
        console.log('📚 === REPLICA SET CONCEPTS ===\n');
        
        console.log('Replica Set Components:');
        console.log('1. Primary: Handles all write operations');
        console.log('2. Secondaries: Replicate data from primary');
        console.log('3. Arbiter: Votes in elections (no data storage)');
        console.log('4. Automatic Failover: If primary fails, secondary becomes primary\n');
        
        // ============================================
        // READ PREFERENCES
        // ============================================
        console.log('📖 === READ PREFERENCES ===\n');
        
        console.log('Read Preferences Control Where Reads Go:');
        console.log('');
        console.log('1. primary (default):');
        console.log('   - Read from primary only');
        console.log('   - Strong consistency');
        console.log('   - Use when you need latest data\n');
        
        console.log('2. primaryPreferred:');
        console.log('   - Read from primary, fallback to secondary if primary unavailable');
        console.log('   - Good for high availability\n');
        
        console.log('3. secondary:');
        console.log('   - Read from secondary only');
        console.log('   - Reduces load on primary');
        console.log('   - May have slightly stale data\n');
        
        console.log('4. secondaryPreferred:');
        console.log('   - Read from secondary, fallback to primary');
        console.log('   - Distributes read load\n');
        
        console.log('5. nearest:');
        console.log('   - Read from nearest member (lowest latency)');
        console.log('   - Good for geographically distributed replica sets\n');
        
        // Example: Setting read preference (requires replica set)
        console.log('Example Code:');
        console.log(`
const { MongoClient, ReadPreference } = require('mongodb');

const client = new MongoClient(uri, {
    readPreference: ReadPreference.SECONDARY_PREFERRED
});

// Or set per operation
const result = await collection.find({})
    .readPreference(ReadPreference.SECONDARY)
    .toArray();
        `);
        console.log('');
        
        // ============================================
        // WRITE CONCERNS
        // ============================================
        console.log('✍️  === WRITE CONCERNS ===\n');
        
        console.log('Write Concerns Control Write Acknowledgment:');
        console.log('');
        console.log('1. w: 0 (Unacknowledged):');
        console.log('   - Fire and forget');
        console.log('   - Fastest but no guarantee');
        console.log('   - Not recommended for production\n');
        
        console.log('2. w: 1 (Acknowledged - Default):');
        console.log('   - Wait for primary acknowledgment');
        console.log('   - Good balance of speed and safety\n');
        
        console.log('3. w: "majority":');
        console.log('   - Wait for majority of replica set members');
        console.log('   - Strongest durability');
        console.log('   - Slower but safest\n');
        
        console.log('4. w: <number>:');
        console.log('   - Wait for N members to acknowledge');
        console.log('   - Custom durability level\n');
        
        console.log('5. j: true (Journal):');
        console.log('   - Wait for write to be written to journal');
        console.log('   - Ensures data survives server crash\n');
        
        console.log('Example Code:');
        console.log(`
// Write with majority write concern
await collection.insertOne(document, {
    writeConcern: { w: 'majority', j: true }
});

// Or set default for client
const client = new MongoClient(uri, {
    writeConcern: { w: 'majority' }
});
        `);
        console.log('');
        
        // ============================================
        // REPLICA SET STATUS
        // ============================================
        console.log('📊 === REPLICA SET STATUS ===\n');
        
        console.log('To check replica set status, run in MongoDB shell:');
        console.log('  rs.status()');
        console.log('');
        console.log('This shows:');
        console.log('  - Current primary');
        console.log('  - Secondary members');
        console.log('  - Replication lag');
        console.log('  - Member health\n');
        
        // Try to get replica set status (will fail if not replica set)
        try {
            const adminDb = db.admin();
            const status = await adminDb.command({ replSetGetStatus: 1 });
            console.log('✅ Replica set detected!');
            console.log('Members:', status.members.length);
        } catch (error) {
            console.log('ℹ️  Not running in replica set mode');
            console.log('   This is normal for standalone MongoDB');
            console.log('   Replica sets require multiple MongoDB instances\n');
        }
        
        // ============================================
        // PRACTICAL EXAMPLE: READ PREFERENCE
        // ============================================
        console.log('💡 === PRACTICAL EXAMPLE ===\n');
        
        const usersCollection = db.collection('users');
        await usersCollection.deleteMany({});
        
        // Insert sample data
        await usersCollection.insertOne({
            name: 'John Doe',
            email: 'john@example.com',
            role: 'user'
        });
        
        console.log('Example: Reading with different preferences');
        console.log('');
        
        // Primary read (default)
        console.log('1. Reading from primary (default):');
        const primaryRead = await usersCollection
            .find({ email: 'john@example.com' })
            .toArray();
        console.log(`   Found: ${primaryRead.length} user(s)\n`);
        
        // Note: Setting read preference on standalone won't have effect
        // but demonstrates the concept
        console.log('2. Reading with secondaryPreferred (concept):');
        console.log('   In replica set, this would read from secondary');
        console.log('   Reduces load on primary\n');
        
        // ============================================
        // PRACTICAL EXAMPLE: WRITE CONCERN
        // ============================================
        console.log('✍️  === WRITE CONCERN EXAMPLE ===\n');
        
        console.log('Inserting with different write concerns:');
        console.log('');
        
        // Default write concern (w: 1)
        console.log('1. Default write concern (w: 1):');
        await usersCollection.insertOne({
            name: 'Alice',
            email: 'alice@example.com'
        });
        console.log('   ✅ Inserted (acknowledged by primary)\n');
        
        // Majority write concern
        console.log('2. Majority write concern (w: "majority"):');
        await usersCollection.insertOne({
            name: 'Bob',
            email: 'bob@example.com'
        }, {
            writeConcern: { w: 'majority', j: true }
        });
        console.log('   ✅ Inserted (acknowledged by majority)\n');
        
        // ============================================
        // REPLICATION LAG
        // ============================================
        console.log('⏱️  === REPLICATION LAG ===\n');
        
        console.log('Replication Lag Considerations:');
        console.log('  - Secondaries may be slightly behind primary');
        console.log('  - Read from secondary may show stale data');
        console.log('  - Use primary for critical reads');
        console.log('  - Use secondary for analytics/reporting\n');
        
        // ============================================
        // FAILOVER SCENARIO
        // ============================================
        console.log('🔄 === FAILOVER SCENARIO ===\n');
        
        console.log('Automatic Failover Process:');
        console.log('1. Primary becomes unavailable');
        console.log('2. Remaining members detect primary failure');
        console.log('3. Election process selects new primary');
        console.log('4. New primary handles writes');
        console.log('5. Application continues with minimal downtime\n');
        
        console.log('Best Practices:');
        console.log('  - Use primaryPreferred for critical reads');
        console.log('  - Use majority write concern for critical writes');
        console.log('  - Monitor replica set health');
        console.log('  - Test failover scenarios\n');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await usersCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Replication concepts explained!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Replica sets provide high availability');
        console.log('   - Read preferences control read distribution');
        console.log('   - Write concerns control write durability');
        console.log('   - Automatic failover ensures continuity');
        console.log('   - Replication lag is normal and acceptable for non-critical reads');
        
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

