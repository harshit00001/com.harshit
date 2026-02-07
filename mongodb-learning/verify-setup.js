/**
 * Setup Verification Script
 * 
 * This script verifies that MongoDB is properly set up and accessible.
 * Run this after installing MongoDB to ensure everything is working.
 */

const { connectToDatabase, closeConnection } = require('./config/database');

async function verifySetup() {
    let client;
    
    try {
        console.log('🔍 Verifying MongoDB Setup...\n');
        
        // Test connection
        console.log('1. Testing MongoDB connection...');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        console.log('   ✅ Connection successful!\n');
        
        // Test database operations
        console.log('2. Testing database operations...');
        const testCollection = db.collection('setup_test');
        
        // Insert
        const insertResult = await testCollection.insertOne({
            test: true,
            timestamp: new Date()
        });
        console.log('   ✅ Insert operation successful');
        
        // Find
        const found = await testCollection.findOne({ _id: insertResult.insertedId });
        if (found) {
            console.log('   ✅ Find operation successful');
        }
        
        // Update
        const updateResult = await testCollection.updateOne(
            { _id: insertResult.insertedId },
            { $set: { updated: true } }
        );
        if (updateResult.modifiedCount > 0) {
            console.log('   ✅ Update operation successful');
        }
        
        // Delete
        const deleteResult = await testCollection.deleteOne({ _id: insertResult.insertedId });
        if (deleteResult.deletedCount > 0) {
            console.log('   ✅ Delete operation successful');
        }
        
        console.log('');
        
        // Check MongoDB version
        console.log('3. Checking MongoDB version...');
        const adminDb = db.admin();
        const serverStatus = await adminDb.serverStatus();
        console.log(`   MongoDB Version: ${serverStatus.version}`);
        console.log(`   Storage Engine: ${serverStatus.storageEngine?.name || 'N/A'}\n`);
        
        // Check if replica set
        try {
            const replStatus = await adminDb.command({ replSetGetStatus: 1 });
            console.log('4. Replica Set Status:');
            console.log(`   ✅ Running in replica set mode`);
            console.log(`   Members: ${replStatus.members.length}\n`);
        } catch (error) {
            console.log('4. Replica Set Status:');
            console.log('   ℹ️  Running in standalone mode (not a replica set)');
            console.log('   Note: Transactions require replica set\n');
        }
        
        console.log('✅ Setup verification complete!');
        console.log('\n📚 Next Steps:');
        console.log('   1. Start with basic examples: node basic/Example1-Connection.js');
        console.log('   2. Read the README.md for learning path');
        console.log('   3. Explore examples in each section');
        console.log('   4. Review SCENARIO_QA.md files for real-world scenarios\n');
        
    } catch (error) {
        console.error('\n❌ Setup verification failed!\n');
        console.error('Error:', error.message);
        console.log('\n💡 Troubleshooting:');
        console.log('   1. Make sure MongoDB is running');
        console.log('   2. Check connection string in config/database.js');
        console.log('   3. For local MongoDB: Ensure mongod is running');
        console.log('   4. For Atlas: Verify connection string and IP whitelist');
        console.log('   5. Check firewall settings\n');
        process.exit(1);
    } finally {
        if (client) {
            await closeConnection(client);
        }
    }
}

// Run verification
verifySetup();

