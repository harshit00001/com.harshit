/**
 * Example 5: Embedded Documents and Arrays
 * 
 * SIMPLE EXPLANATION:
 * Embedded documents are like putting a box inside another box.
 * Instead of storing related data in separate tables, you can store
 * it nested inside a single document.
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB allows documents to contain other documents (nested objects)
 * and arrays. This is called embedding and is one of MongoDB's key features.
 * Use dot notation to query nested fields: "address.city"
 * 
 * INTERVIEW POINT:
 * - Embedded documents are stored in the same document (denormalization)
 * - Good for one-to-one or one-to-few relationships
 * - Use dot notation to query nested fields
 * - Use $elemMatch for complex array queries
 * - Consider embedding vs referencing based on access patterns
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
        // CREATING DOCUMENTS WITH EMBEDDED DATA
        // ============================================
        console.log('📝 === CREATING EMBEDDED DOCUMENTS ===\n');
        
        // User with embedded address
        const user1 = {
            name: 'John Doe',
            email: 'john@example.com',
            age: 30,
            // Embedded document (address)
            address: {
                street: '123 Main St',
                city: 'New York',
                state: 'NY',
                zipCode: '10001',
                country: 'USA'
            },
            // Array of embedded documents (orders)
            orders: [
                {
                    orderId: 'ORD001',
                    date: new Date('2024-01-15'),
                    total: 299.99,
                    items: ['Laptop', 'Mouse']
                },
                {
                    orderId: 'ORD002',
                    date: new Date('2024-02-20'),
                    total: 149.99,
                    items: ['Keyboard', 'Headphones']
                }
            ],
            // Array of strings (tags)
            tags: ['premium', 'verified', 'active'],
            createdAt: new Date()
        };
        
        // User with nested embedded documents
        const user2 = {
            name: 'Jane Smith',
            email: 'jane@example.com',
            age: 28,
            address: {
                street: '456 Oak Ave',
                city: 'Los Angeles',
                state: 'CA',
                zipCode: '90001',
                country: 'USA',
                // Nested embedded document
                coordinates: {
                    latitude: 34.0522,
                    longitude: -118.2437
                }
            },
            orders: [
                {
                    orderId: 'ORD003',
                    date: new Date('2024-03-10'),
                    total: 599.99,
                    items: ['Smartphone'],
                    // Embedded document in array
                    shipping: {
                        method: 'Express',
                        carrier: 'FedEx',
                        trackingNumber: 'FX123456'
                    }
                }
            ],
            tags: ['new', 'active'],
            createdAt: new Date()
        };
        
        const user3 = {
            name: 'Bob Johnson',
            email: 'bob@example.com',
            age: 35,
            address: {
                street: '789 Pine Rd',
                city: 'Chicago',
                state: 'IL',
                zipCode: '60601',
                country: 'USA'
            },
            orders: [],
            tags: ['premium'],
            createdAt: new Date()
        };
        
        const insertResult = await usersCollection.insertMany([user1, user2, user3]);
        console.log(`✅ Inserted ${insertResult.insertedCount} users with embedded documents\n`);
        
        // ============================================
        // QUERYING EMBEDDED DOCUMENTS
        // ============================================
        console.log('🔍 === QUERYING EMBEDDED DOCUMENTS ===\n');
        
        // Query by nested field using dot notation
        console.log('1. Users from New York:');
        const nyUsers = await usersCollection.find({ 'address.city': 'New York' }).toArray();
        nyUsers.forEach(user => {
            console.log(`   - ${user.name} (${user.address.city}, ${user.address.state})`);
        });
        console.log('');
        
        // Query by nested field in nested document
        console.log('2. Users in California:');
        const caUsers = await usersCollection.find({ 'address.state': 'CA' }).toArray();
        caUsers.forEach(user => {
            console.log(`   - ${user.name} (${user.address.city})`);
        });
        console.log('');
        
        // Query by deeply nested field
        console.log('3. Users with coordinates (nested in address):');
        const withCoordinates = await usersCollection.find({
            'address.coordinates': { $exists: true }
        }).toArray();
        withCoordinates.forEach(user => {
            const coords = user.address.coordinates;
            console.log(`   - ${user.name} (lat: ${coords.latitude}, lng: ${coords.longitude})`);
        });
        console.log('');
        
        // ============================================
        // QUERYING ARRAYS
        // ============================================
        console.log('📋 === QUERYING ARRAYS ===\n');
        
        // Find documents where array contains value
        console.log('1. Users with "premium" tag:');
        const premiumUsers = await usersCollection.find({ tags: 'premium' }).toArray();
        premiumUsers.forEach(user => {
            console.log(`   - ${user.name} (tags: ${user.tags.join(', ')})`);
        });
        console.log('');
        
        // Find documents with array containing any of specified values
        console.log('2. Users with "premium" OR "verified" tag:');
        const premiumOrVerified = await usersCollection.find({
            tags: { $in: ['premium', 'verified'] }
        }).toArray();
        premiumOrVerified.forEach(user => {
            console.log(`   - ${user.name} (tags: ${user.tags.join(', ')})`);
        });
        console.log('');
        
        // Find documents with array containing all specified values
        console.log('3. Users with BOTH "premium" AND "active" tags:');
        const premiumAndActive = await usersCollection.find({
            tags: { $all: ['premium', 'active'] }
        }).toArray();
        if (premiumAndActive.length > 0) {
            premiumAndActive.forEach(user => {
                console.log(`   - ${user.name} (tags: ${user.tags.join(', ')})`);
            });
        } else {
            console.log('   (No users found)');
        }
        console.log('');
        
        // ============================================
        // QUERYING ARRAYS OF EMBEDDED DOCUMENTS
        // ============================================
        console.log('📦 === QUERYING ARRAYS OF EMBEDDED DOCUMENTS ===\n');
        
        // Find users with orders
        console.log('1. Users who have placed orders:');
        const usersWithOrders = await usersCollection.find({
            orders: { $exists: true, $ne: [] }
        }).toArray();
        usersWithOrders.forEach(user => {
            console.log(`   - ${user.name} (${user.orders.length} orders)`);
        });
        console.log('');
        
        // Query array of embedded documents
        console.log('2. Users with order total > $200:');
        const highValueOrders = await usersCollection.find({
            'orders.total': { $gt: 200 }
        }).toArray();
        highValueOrders.forEach(user => {
            user.orders.forEach(order => {
                if (order.total > 200) {
                    console.log(`   - ${user.name}: Order ${order.orderId} - $${order.total}`);
                }
            });
        });
        console.log('');
        
        // $elemMatch for complex array queries
        console.log('3. Users with Express shipping (using $elemMatch):');
        const expressShipping = await usersCollection.find({
            orders: {
                $elemMatch: {
                    'shipping.method': 'Express'
                }
            }
        }).toArray();
        expressShipping.forEach(user => {
            console.log(`   - ${user.name}`);
        });
        console.log('');
        
        // ============================================
        // UPDATING EMBEDDED DOCUMENTS
        // ============================================
        console.log('✏️  === UPDATING EMBEDDED DOCUMENTS ===\n');
        
        // Update nested field
        console.log('1. Updating John\'s zip code:');
        await usersCollection.updateOne(
            { email: 'john@example.com' },
            { $set: { 'address.zipCode': '10002' } }
        );
        const updatedJohn = await usersCollection.findOne({ email: 'john@example.com' });
        console.log(`   Updated zip code: ${updatedJohn.address.zipCode}\n`);
        
        // Add new order to array
        console.log('2. Adding new order to John\'s orders:');
        const newOrder = {
            orderId: 'ORD004',
            date: new Date(),
            total: 99.99,
            items: ['USB Cable']
        };
        await usersCollection.updateOne(
            { email: 'john@example.com' },
            { $push: { orders: newOrder } }
        );
        const johnWithNewOrder = await usersCollection.findOne({ email: 'john@example.com' });
        console.log(`   John now has ${johnWithNewOrder.orders.length} orders\n`);
        
        // Add tag to array
        console.log('3. Adding "vip" tag to Jane:');
        await usersCollection.updateOne(
            { email: 'jane@example.com' },
            { $addToSet: { tags: 'vip' } } // $addToSet prevents duplicates
        );
        const janeWithTag = await usersCollection.findOne({ email: 'jane@example.com' });
        console.log(`   Jane's tags: ${janeWithTag.tags.join(', ')}\n`);
        
        // Update nested field in array element
        console.log('4. Updating order total in array:');
        await usersCollection.updateOne(
            { email: 'john@example.com', 'orders.orderId': 'ORD001' },
            { $set: { 'orders.$.total': 349.99 } } // $ is positional operator
        );
        const johnUpdated = await usersCollection.findOne({ email: 'john@example.com' });
        const ord001 = johnUpdated.orders.find(o => o.orderId === 'ORD001');
        console.log(`   Order ORD001 new total: $${ord001.total}\n`);
        
        // ============================================
        // PROJECTION WITH EMBEDDED DOCUMENTS
        // ============================================
        console.log('🎯 === PROJECTION ===\n');
        
        console.log('Users with only name and address:');
        const usersWithAddress = await usersCollection.find({})
            .project({ name: 1, address: 1, _id: 0 })
            .toArray();
        usersWithAddress.forEach(user => {
            console.log(`   ${user.name}: ${user.address.city}, ${user.address.state}`);
        });
        console.log('');
        
        // Cleanup
        console.log('🧹 Cleaning up test data...');
        await usersCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Embedded documents examples completed successfully!');
        
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

