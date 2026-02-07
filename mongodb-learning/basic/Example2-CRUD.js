/**
 * Example 2: CRUD Operations (Create, Read, Update, Delete)
 * 
 * SIMPLE EXPLANATION:
 * CRUD stands for the four basic operations you can do with data:
 * - CREATE: Add new data
 * - READ: Get/view data
 * - UPDATE: Change existing data
 * - DELETE: Remove data
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB provides methods for each CRUD operation:
 * - Create: insertOne(), insertMany()
 * - Read: findOne(), find()
 * - Update: updateOne(), updateMany(), replaceOne()
 * - Delete: deleteOne(), deleteMany()
 * 
 * INTERVIEW POINT:
 * - insertOne returns InsertOneResult with insertedId
 * - find() returns a cursor, use toArray() or forEach() to iterate
 * - updateOne updates first match, updateMany updates all matches
 * - Always use $set operator in updates to avoid replacing entire document
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const usersCollection = db.collection('users');
        
        // ============================================
        // CREATE OPERATIONS
        // ============================================
        console.log('📝 === CREATE OPERATIONS ===\n');
        
        // Insert a single document
        console.log('1. Inserting a single user...');
        const newUser = {
            name: 'John Doe',
            email: 'john.doe@example.com',
            age: 30,
            city: 'New York',
            interests: ['coding', 'reading', 'traveling'],
            createdAt: new Date()
        };
        
        const insertResult = await usersCollection.insertOne(newUser);
        console.log(`   ✅ User inserted with ID: ${insertResult.insertedId}\n`);
        
        // Insert multiple documents
        console.log('2. Inserting multiple users...');
        const multipleUsers = [
            {
                name: 'Jane Smith',
                email: 'jane.smith@example.com',
                age: 25,
                city: 'Los Angeles',
                interests: ['music', 'dancing'],
                createdAt: new Date()
            },
            {
                name: 'Bob Johnson',
                email: 'bob.johnson@example.com',
                age: 35,
                city: 'Chicago',
                interests: ['sports', 'cooking'],
                createdAt: new Date()
            },
            {
                name: 'Alice Williams',
                email: 'alice.williams@example.com',
                age: 28,
                city: 'New York',
                interests: ['art', 'photography', 'traveling'],
                createdAt: new Date()
            }
        ];
        
        const insertManyResult = await usersCollection.insertMany(multipleUsers);
        console.log(`   ✅ ${insertManyResult.insertedCount} users inserted\n`);
        
        // ============================================
        // READ OPERATIONS
        // ============================================
        console.log('📖 === READ OPERATIONS ===\n');
        
        // Find one document
        console.log('1. Finding one user by email...');
        const foundUser = await usersCollection.findOne({ email: 'john.doe@example.com' });
        console.log('   Found user:', JSON.stringify(foundUser, null, 2));
        console.log('');
        
        // Find all documents
        console.log('2. Finding all users...');
        const allUsers = await usersCollection.find({}).toArray();
        console.log(`   Total users: ${allUsers.length}`);
        allUsers.forEach((user, index) => {
            console.log(`   ${index + 1}. ${user.name} (${user.email})`);
        });
        console.log('');
        
        // Find with filter
        console.log('3. Finding users from New York...');
        const nyUsers = await usersCollection.find({ city: 'New York' }).toArray();
        console.log(`   Found ${nyUsers.length} users from New York:`);
        nyUsers.forEach(user => {
            console.log(`   - ${user.name}`);
        });
        console.log('');
        
        // Find with condition (age > 30)
        console.log('4. Finding users older than 30...');
        const olderUsers = await usersCollection.find({ age: { $gt: 30 } }).toArray();
        console.log(`   Found ${olderUsers.length} users older than 30:`);
        olderUsers.forEach(user => {
            console.log(`   - ${user.name} (age: ${user.age})`);
        });
        console.log('');
        
        // ============================================
        // UPDATE OPERATIONS
        // ============================================
        console.log('✏️  === UPDATE OPERATIONS ===\n');
        
        // Update one document
        console.log('1. Updating John\'s age to 31...');
        const updateResult = await usersCollection.updateOne(
            { email: 'john.doe@example.com' },
            { $set: { age: 31, updatedAt: new Date() } }
        );
        console.log(`   ✅ ${updateResult.modifiedCount} document updated\n`);
        
        // Verify update
        const updatedUser = await usersCollection.findOne({ email: 'john.doe@example.com' });
        console.log(`   Updated age: ${updatedUser.age}\n`);
        
        // Update many documents (add field to all users)
        console.log('2. Adding "status" field to all users...');
        const updateManyResult = await usersCollection.updateMany(
            {},
            { $set: { status: 'active' } }
        );
        console.log(`   ✅ ${updateManyResult.modifiedCount} documents updated\n`);
        
        // Increment a field
        console.log('3. Incrementing Bob\'s age by 1...');
        await usersCollection.updateOne(
            { email: 'bob.johnson@example.com' },
            { $inc: { age: 1 } }
        );
        const bob = await usersCollection.findOne({ email: 'bob.johnson@example.com' });
        console.log(`   Bob's new age: ${bob.age}\n`);
        
        // ============================================
        // DELETE OPERATIONS
        // ============================================
        console.log('🗑️  === DELETE OPERATIONS ===\n');
        
        // Delete one document
        console.log('1. Deleting Alice...');
        const deleteResult = await usersCollection.deleteOne({ email: 'alice.williams@example.com' });
        console.log(`   ✅ ${deleteResult.deletedCount} document deleted\n`);
        
        // Verify deletion
        const remainingUsers = await usersCollection.find({}).toArray();
        console.log(`   Remaining users: ${remainingUsers.length}\n`);
        
        // Delete many documents (cleanup - optional)
        console.log('2. Cleaning up test data...');
        const deleteManyResult = await usersCollection.deleteMany({});
        console.log(`   ✅ ${deleteManyResult.deletedCount} documents deleted\n`);
        
        console.log('✅ CRUD operations completed successfully!');
        
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

