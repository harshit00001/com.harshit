/**
 * Example 3: Query Basics
 * 
 * SIMPLE EXPLANATION:
 * Queries are like asking questions to your database.
 * "Show me all users from New York" or "Find users older than 25"
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB queries use a JSON-like syntax to specify conditions.
 * find() method accepts a query filter object.
 * You can chain methods like sort(), limit(), skip() for result manipulation.
 * 
 * INTERVIEW POINT:
 * - Empty query {} returns all documents
 * - find() returns a cursor, not documents directly
 * - Use toArray() to get all results as array
 * - Use forEach() to iterate through cursor
 * - sort() takes object: { field: 1 } for ascending, { field: -1 } for descending
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const productsCollection = db.collection('products');
        
        // Insert sample data
        console.log('📝 Inserting sample products...\n');
        const products = [
            { name: 'Laptop', category: 'Electronics', price: 999.99, stock: 50, rating: 4.5 },
            { name: 'Smartphone', category: 'Electronics', price: 699.99, stock: 100, rating: 4.7 },
            { name: 'Headphones', category: 'Electronics', price: 149.99, stock: 200, rating: 4.3 },
            { name: 'Desk Chair', category: 'Furniture', price: 299.99, stock: 30, rating: 4.2 },
            { name: 'Coffee Table', category: 'Furniture', price: 199.99, stock: 25, rating: 4.0 },
            { name: 'Bookshelf', category: 'Furniture', price: 149.99, stock: 40, rating: 4.4 },
            { name: 'Running Shoes', category: 'Sports', price: 89.99, stock: 150, rating: 4.6 },
            { name: 'Yoga Mat', category: 'Sports', price: 29.99, stock: 300, rating: 4.1 },
            { name: 'Dumbbells', category: 'Sports', price: 79.99, stock: 80, rating: 4.5 }
        ];
        
        await productsCollection.insertMany(products);
        console.log('✅ Sample products inserted\n');
        
        // ============================================
        // BASIC QUERIES
        // ============================================
        console.log('🔍 === BASIC QUERIES ===\n');
        
        // 1. Find all documents
        console.log('1. Finding all products:');
        const allProducts = await productsCollection.find({}).toArray();
        console.log(`   Total products: ${allProducts.length}\n`);
        
        // 2. Find by exact field value
        console.log('2. Finding products in Electronics category:');
        const electronics = await productsCollection.find({ category: 'Electronics' }).toArray();
        electronics.forEach(product => {
            console.log(`   - ${product.name} ($${product.price})`);
        });
        console.log('');
        
        // 3. Find by multiple conditions (implicit AND)
        console.log('3. Finding Electronics products under $200:');
        const cheapElectronics = await productsCollection.find({
            category: 'Electronics',
            price: { $lt: 200 }
        }).toArray();
        cheapElectronics.forEach(product => {
            console.log(`   - ${product.name} ($${product.price})`);
        });
        console.log('');
        
        // ============================================
        // SORTING
        // ============================================
        console.log('📊 === SORTING ===\n');
        
        // Sort by price (ascending)
        console.log('1. Products sorted by price (low to high):');
        const sortedByPrice = await productsCollection
            .find({})
            .sort({ price: 1 })
            .toArray();
        sortedByPrice.forEach((product, index) => {
            console.log(`   ${index + 1}. ${product.name} - $${product.price}`);
        });
        console.log('');
        
        // Sort by rating (descending)
        console.log('2. Products sorted by rating (high to low):');
        const sortedByRating = await productsCollection
            .find({})
            .sort({ rating: -1 })
            .toArray();
        sortedByRating.slice(0, 5).forEach((product, index) => {
            console.log(`   ${index + 1}. ${product.name} - Rating: ${product.rating}`);
        });
        console.log('');
        
        // Sort by multiple fields
        console.log('3. Products sorted by category, then price:');
        const sortedByCategoryAndPrice = await productsCollection
            .find({})
            .sort({ category: 1, price: 1 })
            .toArray();
        sortedByCategoryAndPrice.forEach(product => {
            console.log(`   ${product.category}: ${product.name} - $${product.price}`);
        });
        console.log('');
        
        // ============================================
        // LIMITING AND SKIPPING
        // ============================================
        console.log('📄 === LIMITING AND SKIPPING ===\n');
        
        // Limit results
        console.log('1. Top 3 most expensive products:');
        const top3Expensive = await productsCollection
            .find({})
            .sort({ price: -1 })
            .limit(3)
            .toArray();
        top3Expensive.forEach((product, index) => {
            console.log(`   ${index + 1}. ${product.name} - $${product.price}`);
        });
        console.log('');
        
        // Skip and limit (pagination)
        console.log('2. Pagination example (page 2, 3 items per page):');
        const page2 = await productsCollection
            .find({})
            .sort({ name: 1 })
            .skip(3)  // Skip first 3 items
            .limit(3) // Get next 3 items
            .toArray();
        page2.forEach((product, index) => {
            console.log(`   ${index + 1}. ${product.name}`);
        });
        console.log('');
        
        // ============================================
        // PROJECTION (Select specific fields)
        // ============================================
        console.log('🎯 === PROJECTION (Selecting Fields) ===\n');
        
        // Get only name and price
        console.log('1. Products with only name and price:');
        const nameAndPrice = await productsCollection
            .find({})
            .project({ name: 1, price: 1, _id: 0 })
            .toArray();
        nameAndPrice.forEach(product => {
            console.log(`   ${product.name}: $${product.price}`);
        });
        console.log('');
        
        // Exclude specific fields
        console.log('2. Products without stock field:');
        const withoutStock = await productsCollection
            .find({})
            .project({ stock: 0 })
            .limit(3)
            .toArray();
        withoutStock.forEach(product => {
            console.log(`   ${product.name} - Has stock field: ${product.stock !== undefined}`);
        });
        console.log('');
        
        // ============================================
        // COUNT DOCUMENTS
        // ============================================
        console.log('🔢 === COUNTING ===\n');
        
        const totalProducts = await productsCollection.countDocuments({});
        console.log(`Total products: ${totalProducts}`);
        
        const electronicsCount = await productsCollection.countDocuments({ category: 'Electronics' });
        console.log(`Electronics products: ${electronicsCount}`);
        
        const expensiveCount = await productsCollection.countDocuments({ price: { $gt: 500 } });
        console.log(`Products over $500: ${expensiveCount}\n`);
        
        // Cleanup
        console.log('🧹 Cleaning up test data...');
        await productsCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Query examples completed successfully!');
        
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

