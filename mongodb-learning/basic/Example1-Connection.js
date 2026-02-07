/**
 * Example 1: MongoDB Connection
 * 
 * SIMPLE EXPLANATION:
 * This example shows how to connect to MongoDB database.
 * Think of it like connecting to WiFi - you need the right address and credentials.
 * 
 * TECHNICAL EXPLANATION:
 * We use MongoClient to establish a connection to MongoDB server.
 * The connection string (URI) specifies where MongoDB is running.
 * We use async/await for handling asynchronous operations.
 * 
 * INTERVIEW POINT:
 * - Always close connections after use to prevent memory leaks
 * - Use connection pooling for production applications
 * - Handle connection errors gracefully
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Attempting to connect to MongoDB...\n');
        
        // Connect to database
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        // Get database name
        const dbName = db.databaseName;
        console.log(`📊 Connected to database: ${dbName}\n`);
        
        // List all collections in the database
        const collections = await db.listCollections().toArray();
        console.log('📁 Collections in database:');
        if (collections.length === 0) {
            console.log('   (No collections yet - this is normal for a new database)\n');
        } else {
            collections.forEach(collection => {
                console.log(`   - ${collection.name}`);
            });
            console.log('');
        }
        
        // Test: Create a test collection and insert a document
        console.log('🧪 Testing connection with a sample operation...');
        const testCollection = db.collection('connection_test');
        
        // Insert a test document
        const testDoc = {
            message: 'Connection successful!',
            timestamp: new Date(),
            test: true
        };
        
        const result = await testCollection.insertOne(testDoc);
        console.log(`✅ Test document inserted with ID: ${result.insertedId}\n`);
        
        // Find the document we just inserted
        const foundDoc = await testCollection.findOne({ _id: result.insertedId });
        console.log('📄 Found document:');
        console.log(JSON.stringify(foundDoc, null, 2));
        console.log('');
        
        // Clean up: Delete test document
        await testCollection.deleteOne({ _id: result.insertedId });
        console.log('🧹 Test document cleaned up\n');
        
        console.log('✅ Connection test completed successfully!');
        
    } catch (error) {
        console.error('❌ Error:', error.message);
        console.log('\n💡 Troubleshooting tips:');
        console.log('   1. Make sure MongoDB is running');
        console.log('   2. Check connection string in config/database.js');
        console.log('   3. For local MongoDB: mongod should be running');
        console.log('   4. For Atlas: Check your connection string and IP whitelist');
    } finally {
        // Always close the connection
        if (client) {
            await closeConnection(client);
        }
    }
}

// Run the example
main();

