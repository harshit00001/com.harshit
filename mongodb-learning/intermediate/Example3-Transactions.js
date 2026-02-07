/**
 * Example 3: Transactions
 * 
 * SIMPLE EXPLANATION:
 * Transactions ensure multiple operations either all succeed or all fail together.
 * Like transferring money: debit one account AND credit another, or do neither.
 * 
 * TECHNICAL EXPLANATION:
 * Transactions provide ACID guarantees:
 * - Atomicity: All or nothing
 * - Consistency: Data remains valid
 * - Isolation: Concurrent transactions don't interfere
 * - Durability: Committed changes are permanent
 * 
 * INTERVIEW POINT:
 * - Transactions require replica set (not standalone MongoDB)
 * - Use sessions to group operations
 * - Always handle errors and abort on failure
 * - Transactions have performance overhead
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const accountsCollection = db.collection('accounts');
        const transactionsCollection = db.collection('transactions');
        
        // Clean up
        await accountsCollection.deleteMany({});
        await transactionsCollection.deleteMany({});
        
        // ============================================
        // SETUP: CREATE ACCOUNTS
        // ============================================
        console.log('📝 === SETUP: CREATING ACCOUNTS ===\n');
        
        await accountsCollection.insertMany([
            { accountId: 'ACC001', name: 'Alice', balance: 1000 },
            { accountId: 'ACC002', name: 'Bob', balance: 500 },
            { accountId: 'ACC003', name: 'Charlie', balance: 2000 }
        ]);
        
        console.log('✅ Accounts created:');
        const accounts = await accountsCollection.find({}).toArray();
        accounts.forEach(acc => {
            console.log(`   ${acc.name}: $${acc.balance}`);
        });
        console.log('');
        
        // ============================================
        // TRANSACTION: MONEY TRANSFER
        // ============================================
        console.log('💸 === TRANSACTION: MONEY TRANSFER ===\n');
        
        console.log('Transferring $200 from Alice to Bob...\n');
        
        // Start a session
        const session = client.startSession();
        
        try {
            // Start transaction
            session.startTransaction();
            
            const fromAccount = 'ACC001'; // Alice
            const toAccount = 'ACC002';   // Bob
            const amount = 200;
            
            // Step 1: Debit from sender
            console.log('1. Debiting $200 from Alice...');
            const debitResult = await accountsCollection.updateOne(
                { accountId: fromAccount, balance: { $gte: amount } },
                { $inc: { balance: -amount } },
                { session }
            );
            
            if (debitResult.modifiedCount === 0) {
                throw new Error('Insufficient balance or account not found');
            }
            console.log('   ✅ Debit successful\n');
            
            // Step 2: Credit to receiver
            console.log('2. Crediting $200 to Bob...');
            const creditResult = await accountsCollection.updateOne(
                { accountId: toAccount },
                { $inc: { balance: amount } },
                { session }
            );
            
            if (creditResult.modifiedCount === 0) {
                throw new Error('Receiver account not found');
            }
            console.log('   ✅ Credit successful\n');
            
            // Step 3: Record transaction
            console.log('3. Recording transaction...');
            await transactionsCollection.insertOne({
                fromAccount: fromAccount,
                toAccount: toAccount,
                amount: amount,
                timestamp: new Date(),
                status: 'completed'
            }, { session });
            console.log('   ✅ Transaction recorded\n');
            
            // Commit transaction
            await session.commitTransaction();
            console.log('✅ Transaction committed successfully!\n');
            
        } catch (error) {
            // Abort transaction on error
            await session.abortTransaction();
            console.error('❌ Transaction aborted:', error.message);
            console.log('   All changes have been rolled back\n');
        } finally {
            // End session
            await session.endSession();
        }
        
        // Verify balances
        console.log('📊 Updated balances:');
        const updatedAccounts = await accountsCollection.find({}).toArray();
        updatedAccounts.forEach(acc => {
            console.log(`   ${acc.name}: $${acc.balance}`);
        });
        console.log('');
        
        // ============================================
        // TRANSACTION: FAILED TRANSFER (INSUFFICIENT BALANCE)
        // ============================================
        console.log('⚠️  === TRANSACTION: FAILED TRANSFER ===\n');
        
        console.log('Attempting to transfer $2000 from Bob (only has $700)...\n');
        
        const session2 = client.startSession();
        
        try {
            session2.startTransaction();
            
            const fromAccount2 = 'ACC002'; // Bob
            const toAccount2 = 'ACC003';   // Charlie
            const amount2 = 2000;
            
            console.log('1. Attempting to debit $2000 from Bob...');
            const debitResult2 = await accountsCollection.updateOne(
                { accountId: fromAccount2, balance: { $gte: amount2 } },
                { $inc: { balance: -amount2 } },
                { session: session2 }
            );
            
            if (debitResult2.modifiedCount === 0) {
                throw new Error('Insufficient balance');
            }
            
            // This won't execute because we throw error above
            await accountsCollection.updateOne(
                { accountId: toAccount2 },
                { $inc: { balance: amount2 } },
                { session: session2 }
            );
            
            await session2.commitTransaction();
            
        } catch (error) {
            await session2.abortTransaction();
            console.log('   ✅ Transaction correctly aborted due to insufficient balance');
            console.log(`   Error: ${error.message}\n`);
        } finally {
            await session2.endSession();
        }
        
        // Verify balances unchanged
        console.log('📊 Balances (should be unchanged):');
        const unchangedAccounts = await accountsCollection.find({}).toArray();
        unchangedAccounts.forEach(acc => {
            console.log(`   ${acc.name}: $${acc.balance}`);
        });
        console.log('');
        
        // ============================================
        // TRANSACTION: MULTI-COLLECTION OPERATION
        // ============================================
        console.log('🛒 === TRANSACTION: ORDER PROCESSING ===\n');
        
        const ordersCollection = db.collection('orders');
        const inventoryCollection = db.collection('inventory');
        
        await ordersCollection.deleteMany({});
        await inventoryCollection.deleteMany({});
        
        // Setup inventory
        await inventoryCollection.insertMany([
            { productId: 'PROD001', name: 'Laptop', stock: 10 },
            { productId: 'PROD002', name: 'Mouse', stock: 50 }
        ]);
        
        console.log('Processing order: 2 Laptops and 5 Mice...\n');
        
        const session3 = client.startSession();
        
        try {
            session3.startTransaction();
            
            // Step 1: Check and update inventory
            console.log('1. Updating inventory...');
            const laptopUpdate = await inventoryCollection.updateOne(
                { productId: 'PROD001', stock: { $gte: 2 } },
                { $inc: { stock: -2 } },
                { session: session3 }
            );
            
            if (laptopUpdate.modifiedCount === 0) {
                throw new Error('Insufficient laptop stock');
            }
            
            const mouseUpdate = await inventoryCollection.updateOne(
                { productId: 'PROD002', stock: { $gte: 5 } },
                { $inc: { stock: -5 } },
                { session: session3 }
            );
            
            if (mouseUpdate.modifiedCount === 0) {
                throw new Error('Insufficient mouse stock');
            }
            
            console.log('   ✅ Inventory updated\n');
            
            // Step 2: Create order
            console.log('2. Creating order...');
            await ordersCollection.insertOne({
                orderId: 'ORD001',
                items: [
                    { productId: 'PROD001', quantity: 2 },
                    { productId: 'PROD002', quantity: 5 }
                ],
                status: 'completed',
                createdAt: new Date()
            }, { session: session3 });
            console.log('   ✅ Order created\n');
            
            await session3.commitTransaction();
            console.log('✅ Order processing transaction committed!\n');
            
        } catch (error) {
            await session3.abortTransaction();
            console.error('❌ Transaction aborted:', error.message);
            console.log('   Inventory and order changes rolled back\n');
        } finally {
            await session3.endSession();
        }
        
        // Verify inventory
        console.log('📦 Updated inventory:');
        const inventory = await inventoryCollection.find({}).toArray();
        inventory.forEach(item => {
            console.log(`   ${item.name}: ${item.stock} in stock`);
        });
        console.log('');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await accountsCollection.deleteMany({});
        await transactionsCollection.deleteMany({});
        await ordersCollection.deleteMany({});
        await inventoryCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Transaction examples completed successfully!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Transactions ensure all-or-nothing operations');
        console.log('   - Always use try-catch and abort on errors');
        console.log('   - Pass session to all operations in transaction');
        console.log('   - Transactions require replica set (not standalone)');
        console.log('   - Use for critical operations requiring consistency');
        
    } catch (error) {
        console.error('❌ Error:', error.message);
        
        // Check if it's a transaction error
        if (error.message.includes('transactions') || error.message.includes('replica set')) {
            console.log('\n⚠️  Note: Transactions require a replica set.');
            console.log('   For local development, you can:');
            console.log('   1. Use MongoDB Atlas (has replica set by default)');
            console.log('   2. Set up local replica set (see MongoDB docs)');
            console.log('   3. The code will still work, but transactions won\'t execute');
        }
        
        console.error(error);
    } finally {
        if (client) {
            await closeConnection(client);
        }
    }
}

// Run the example
main();

