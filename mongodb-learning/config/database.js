/**
 * MongoDB Database Connection Configuration
 * 
 * This file handles the connection to MongoDB database.
 * You can use either:
 * 1. Local MongoDB: mongodb://localhost:27017/mongodb_learning
 * 2. MongoDB Atlas: mongodb+srv://username:password@cluster.mongodb.net/mongodb_learning
 */

const { MongoClient } = require('mongodb');

// Connection URI
// For local MongoDB (default)
const LOCAL_URI = 'mongodb://localhost:27017/mongodb_learning';

// For MongoDB Atlas (uncomment and update with your credentials)
// const ATLAS_URI = 'mongodb+srv://username:password@cluster.mongodb.net/mongodb_learning';

// Use local by default, change to ATLAS_URI if using Atlas
const URI = LOCAL_URI;

// Database name
const DB_NAME = 'mongodb_learning';

// Create a new MongoClient
const client = new MongoClient(URI, {
    // Connection options
    serverSelectionTimeoutMS: 5000, // Timeout after 5 seconds
    socketTimeoutMS: 45000, // Close sockets after 45 seconds of inactivity
});

/**
 * Connect to MongoDB
 * @returns {Promise<Object>} Database and client objects
 */
async function connectToDatabase() {
    try {
        // Connect to MongoDB
        await client.connect();
        console.log('✅ Connected to MongoDB successfully!');
        
        // Get database
        const db = client.db(DB_NAME);
        
        return { db, client };
    } catch (error) {
        console.error('❌ Error connecting to MongoDB:', error.message);
        throw error;
    }
}

/**
 * Close MongoDB connection
 * @param {MongoClient} client - MongoDB client instance
 */
async function closeConnection(client) {
    try {
        await client.close();
        console.log('✅ MongoDB connection closed.');
    } catch (error) {
        console.error('❌ Error closing MongoDB connection:', error.message);
        throw error;
    }
}

module.exports = {
    connectToDatabase,
    closeConnection,
    client
};

