/**
 * Example 4: Schema Validation
 * 
 * SIMPLE EXPLANATION:
 * Validation rules ensure data follows certain patterns.
 * Like a form validation - email must have @, age must be a number, etc.
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB allows JSON Schema validation rules.
 * Validation can be applied on insert and update operations.
 * Rules are defined when creating a collection or can be added later.
 * 
 * INTERVIEW POINT:
 * - Validation is optional (MongoDB is schema-less by default)
 * - Can validate field types, required fields, patterns, ranges
 * - ValidationLevel: "strict" (default) or "moderate"
 * - ValidationAction: "error" (default) or "warn"
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        // Drop existing collection if it exists
        try {
            await db.collection('users').drop();
        } catch (e) {
            // Collection doesn't exist, that's fine
        }
        
        // ============================================
        // CREATE COLLECTION WITH VALIDATION
        // ============================================
        console.log('📋 === CREATING COLLECTION WITH VALIDATION ===\n');
        
        const validationRules = {
            $jsonSchema: {
                bsonType: 'object',
                required: ['name', 'email', 'age'],
                properties: {
                    name: {
                        bsonType: 'string',
                        description: 'Name must be a string and is required'
                    },
                    email: {
                        bsonType: 'string',
                        pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$',
                        description: 'Email must be a valid email address'
                    },
                    age: {
                        bsonType: 'int',
                        minimum: 0,
                        maximum: 150,
                        description: 'Age must be an integer between 0 and 150'
                    },
                    phone: {
                        bsonType: 'string',
                        pattern: '^\\d{10}$',
                        description: 'Phone must be exactly 10 digits (optional)'
                    },
                    status: {
                        enum: ['active', 'inactive', 'pending'],
                        description: 'Status must be one of: active, inactive, pending'
                    }
                }
            }
        };
        
        console.log('Creating users collection with validation rules...');
        await db.createCollection('users', {
            validator: validationRules,
            validationLevel: 'strict',    // Apply to all inserts/updates
            validationAction: 'error'     // Reject invalid documents
        });
        console.log('✅ Collection created with validation\n');
        
        const usersCollection = db.collection('users');
        
        // ============================================
        // VALID INSERT OPERATIONS
        // ============================================
        console.log('✅ === VALID INSERT OPERATIONS ===\n');
        
        console.log('1. Inserting valid user...');
        const validUser = {
            name: 'John Doe',
            email: 'john@example.com',
            age: 30,
            phone: '1234567890',
            status: 'active'
        };
        
        await usersCollection.insertOne(validUser);
        console.log('   ✅ Valid user inserted successfully\n');
        
        console.log('2. Inserting user with minimal required fields...');
        const minimalUser = {
            name: 'Jane Smith',
            email: 'jane@example.com',
            age: 25
        };
        
        await usersCollection.insertOne(minimalUser);
        console.log('   ✅ Minimal user inserted (only required fields)\n');
        
        // ============================================
        // INVALID INSERT OPERATIONS
        // ============================================
        console.log('❌ === INVALID INSERT OPERATIONS ===\n');
        
        // Missing required field
        console.log('1. Trying to insert user without required field "name"...');
        try {
            await usersCollection.insertOne({
                email: 'bob@example.com',
                age: 35
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Missing required field');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // Invalid email format
        console.log('2. Trying to insert user with invalid email...');
        try {
            await usersCollection.insertOne({
                name: 'Alice',
                email: 'invalid-email', // Missing @ and domain
                age: 28
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Invalid email format');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // Invalid age (out of range)
        console.log('3. Trying to insert user with age > 150...');
        try {
            await usersCollection.insertOne({
                name: 'Bob',
                email: 'bob@example.com',
                age: 200 // Too old!
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Age out of range');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // Invalid age type
        console.log('4. Trying to insert user with age as string...');
        try {
            await usersCollection.insertOne({
                name: 'Charlie',
                email: 'charlie@example.com',
                age: 'thirty' // Should be number
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Age must be integer');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // Invalid phone format
        console.log('5. Trying to insert user with invalid phone...');
        try {
            await usersCollection.insertOne({
                name: 'Diana',
                email: 'diana@example.com',
                age: 32,
                phone: '123' // Too short, must be 10 digits
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Invalid phone format');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // Invalid status enum
        console.log('6. Trying to insert user with invalid status...');
        try {
            await usersCollection.insertOne({
                name: 'Eve',
                email: 'eve@example.com',
                age: 29,
                status: 'banned' // Not in enum
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Invalid status value');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // ============================================
        // VALIDATION ON UPDATE
        // ============================================
        console.log('✏️  === VALIDATION ON UPDATE ===\n');
        
        console.log('1. Updating user with valid data...');
        await usersCollection.updateOne(
            { email: 'john@example.com' },
            { $set: { age: 31, status: 'active' } }
        );
        console.log('   ✅ Update successful\n');
        
        console.log('2. Trying to update with invalid age...');
        try {
            await usersCollection.updateOne(
                { email: 'john@example.com' },
                { $set: { age: -5 } } // Negative age
            );
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Invalid age');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // ============================================
        // MODIFYING VALIDATION RULES
        // ============================================
        console.log('🔧 === MODIFYING VALIDATION RULES ===\n');
        
        console.log('Adding new validation rule (minimum age 18)...');
        
        const updatedValidation = {
            $jsonSchema: {
                bsonType: 'object',
                required: ['name', 'email', 'age'],
                properties: {
                    name: {
                        bsonType: 'string'
                    },
                    email: {
                        bsonType: 'string',
                        pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
                    },
                    age: {
                        bsonType: 'int',
                        minimum: 18,  // Changed from 0 to 18
                        maximum: 150
                    }
                }
            }
        };
        
        await db.command({
            collMod: 'users',
            validator: updatedValidation
        });
        console.log('   ✅ Validation rules updated\n');
        
        // Test new validation
        console.log('Testing new validation (age < 18 should fail)...');
        try {
            await usersCollection.insertOne({
                name: 'Young User',
                email: 'young@example.com',
                age: 15 // Under 18
            });
            console.log('   ❌ This should not print!');
        } catch (error) {
            console.log('   ✅ Correctly rejected: Age must be at least 18');
            console.log(`   Error: ${error.message}\n`);
        }
        
        // ============================================
        // VALIDATION WITH WARNINGS
        // ============================================
        console.log('⚠️  === VALIDATION WITH WARNINGS ===\n');
        
        // Create collection with warning mode
        try {
            await db.collection('products_warn').drop();
        } catch (e) {}
        
        await db.createCollection('products_warn', {
            validator: {
                $jsonSchema: {
                    bsonType: 'object',
                    properties: {
                        name: { bsonType: 'string' },
                        price: { bsonType: 'double', minimum: 0 }
                    }
                }
            },
            validationAction: 'warn' // Warn instead of error
        });
        
        console.log('Collection created with validationAction: "warn"');
        console.log('Invalid documents will be inserted but logged as warnings\n');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await usersCollection.deleteMany({});
        await db.collection('products_warn').drop();
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Validation examples completed successfully!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Validation ensures data quality and consistency');
        console.log('   - Can validate types, patterns, ranges, and enums');
        console.log('   - Required fields must be present');
        console.log('   - Validation applies to both inserts and updates');
        console.log('   - Use "warn" mode to log issues without rejecting');
        
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

