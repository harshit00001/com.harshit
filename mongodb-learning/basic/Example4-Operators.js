/**
 * Example 4: MongoDB Operators
 * 
 * SIMPLE EXPLANATION:
 * Operators are special keywords that help you write more powerful queries.
 * Like using "greater than" (>), "less than" (<), "or", "and" in queries.
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB operators start with $ sign.
 * Comparison operators: $gt, $lt, $gte, $lte, $eq, $ne, $in, $nin
 * Logical operators: $and, $or, $not, $nor
 * Array operators: $all, $elemMatch, $size
 * 
 * INTERVIEW POINT:
 * - Operators are prefixed with $ to distinguish from field names
 * - $in is useful for matching any value in an array
 * - $or allows multiple conditions where any can be true
 * - $and is implicit when multiple conditions are in same object
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const employeesCollection = db.collection('employees');
        
        // Insert sample data
        console.log('📝 Inserting sample employees...\n');
        const employees = [
            { name: 'Alice', age: 28, salary: 75000, department: 'Engineering', skills: ['Java', 'Python', 'SQL'], active: true },
            { name: 'Bob', age: 35, salary: 95000, department: 'Engineering', skills: ['Java', 'JavaScript', 'React'], active: true },
            { name: 'Charlie', age: 42, salary: 110000, department: 'Management', skills: ['Leadership', 'Strategy'], active: true },
            { name: 'Diana', age: 29, salary: 80000, department: 'Design', skills: ['Photoshop', 'Figma', 'UI/UX'], active: true },
            { name: 'Eve', age: 31, salary: 88000, department: 'Engineering', skills: ['Python', 'Docker', 'Kubernetes'], active: false },
            { name: 'Frank', age: 26, salary: 70000, department: 'Marketing', skills: ['SEO', 'Content Writing'], active: true },
            { name: 'Grace', age: 38, salary: 100000, department: 'Engineering', skills: ['Java', 'Spring', 'Microservices'], active: true },
            { name: 'Henry', age: 33, salary: 92000, department: 'Sales', skills: ['Negotiation', 'CRM'], active: true }
        ];
        
        await employeesCollection.insertMany(employees);
        console.log('✅ Sample employees inserted\n');
        
        // ============================================
        // COMPARISON OPERATORS
        // ============================================
        console.log('🔍 === COMPARISON OPERATORS ===\n');
        
        // $gt (greater than)
        console.log('1. Employees older than 30:');
        const olderThan30 = await employeesCollection.find({ age: { $gt: 30 } }).toArray();
        olderThan30.forEach(emp => {
            console.log(`   - ${emp.name} (age: ${emp.age})`);
        });
        console.log('');
        
        // $gte (greater than or equal)
        console.log('2. Employees 30 or older:');
        const thirtyOrOlder = await employeesCollection.find({ age: { $gte: 30 } }).toArray();
        console.log(`   Found: ${thirtyOrOlder.length} employees\n`);
        
        // $lt (less than)
        console.log('3. Employees with salary less than $80,000:');
        const lowSalary = await employeesCollection.find({ salary: { $lt: 80000 } }).toArray();
        lowSalary.forEach(emp => {
            console.log(`   - ${emp.name} ($${emp.salary})`);
        });
        console.log('');
        
        // $lte (less than or equal)
        console.log('4. Employees with salary $80,000 or less:');
        const salary80kOrLess = await employeesCollection.find({ salary: { $lte: 80000 } }).toArray();
        console.log(`   Found: ${salary80kOrLess.length} employees\n`);
        
        // $ne (not equal)
        console.log('5. Inactive employees:');
        const inactive = await employeesCollection.find({ active: { $ne: true } }).toArray();
        inactive.forEach(emp => {
            console.log(`   - ${emp.name} (active: ${emp.active})`);
        });
        console.log('');
        
        // Range query (between)
        console.log('6. Employees with salary between $80,000 and $100,000:');
        const salaryRange = await employeesCollection.find({
            salary: { $gte: 80000, $lte: 100000 }
        }).toArray();
        salaryRange.forEach(emp => {
            console.log(`   - ${emp.name} ($${emp.salary})`);
        });
        console.log('');
        
        // ============================================
        // LOGICAL OPERATORS
        // ============================================
        console.log('🔗 === LOGICAL OPERATORS ===\n');
        
        // $or
        console.log('1. Employees in Engineering OR Management:');
        const engOrMgmt = await employeesCollection.find({
            $or: [
                { department: 'Engineering' },
                { department: 'Management' }
            ]
        }).toArray();
        engOrMgmt.forEach(emp => {
            console.log(`   - ${emp.name} (${emp.department})`);
        });
        console.log('');
        
        // $and (explicit)
        console.log('2. Active Engineering employees over 30:');
        const activeEngOver30 = await employeesCollection.find({
            $and: [
                { department: 'Engineering' },
                { age: { $gt: 30 } },
                { active: true }
            ]
        }).toArray();
        activeEngOver30.forEach(emp => {
            console.log(`   - ${emp.name} (age: ${emp.age})`);
        });
        console.log('');
        
        // $and (implicit - same as above)
        console.log('3. Same query using implicit AND:');
        const implicitAnd = await employeesCollection.find({
            department: 'Engineering',
            age: { $gt: 30 },
            active: true
        }).toArray();
        console.log(`   Found: ${implicitAnd.length} employees\n`);
        
        // $not
        console.log('4. Employees NOT in Engineering:');
        const notEngineering = await employeesCollection.find({
            department: { $not: { $eq: 'Engineering' } }
        }).toArray();
        notEngineering.forEach(emp => {
            console.log(`   - ${emp.name} (${emp.department})`);
        });
        console.log('');
        
        // ============================================
        // ARRAY OPERATORS
        // ============================================
        console.log('📋 === ARRAY OPERATORS ===\n');
        
        // $in (matches any value in array)
        console.log('1. Employees with Java OR Python skills:');
        const javaOrPython = await employeesCollection.find({
            skills: { $in: ['Java', 'Python'] }
        }).toArray();
        javaOrPython.forEach(emp => {
            console.log(`   - ${emp.name} (skills: ${emp.skills.join(', ')})`);
        });
        console.log('');
        
        // $nin (not in)
        console.log('2. Employees without Java or Python:');
        const noJavaOrPython = await employeesCollection.find({
            skills: { $nin: ['Java', 'Python'] }
        }).toArray();
        noJavaOrPython.forEach(emp => {
            console.log(`   - ${emp.name} (skills: ${emp.skills.join(', ')})`);
        });
        console.log('');
        
        // $all (must have all specified values)
        console.log('3. Employees with BOTH Java AND Python:');
        const javaAndPython = await employeesCollection.find({
            skills: { $all: ['Java', 'Python'] }
        }).toArray();
        if (javaAndPython.length > 0) {
            javaAndPython.forEach(emp => {
                console.log(`   - ${emp.name} (skills: ${emp.skills.join(', ')})`);
            });
        } else {
            console.log('   (No employees found with both skills)');
        }
        console.log('');
        
        // $size (array length)
        console.log('4. Employees with exactly 3 skills:');
        const threeSkills = await employeesCollection.find({
            skills: { $size: 3 }
        }).toArray();
        threeSkills.forEach(emp => {
            console.log(`   - ${emp.name} (${emp.skills.length} skills: ${emp.skills.join(', ')})`);
        });
        console.log('');
        
        // ============================================
        // COMBINING OPERATORS
        // ============================================
        console.log('🔀 === COMBINING OPERATORS ===\n');
        
        console.log('Complex query: Active Engineering employees with salary > $85k and Java skill:');
        const complex = await employeesCollection.find({
            $and: [
                { department: 'Engineering' },
                { active: true },
                { salary: { $gt: 85000 } },
                { skills: { $in: ['Java'] } }
            ]
        }).toArray();
        complex.forEach(emp => {
            console.log(`   - ${emp.name} ($${emp.salary}, skills: ${emp.skills.join(', ')})`);
        });
        console.log('');
        
        // Cleanup
        console.log('🧹 Cleaning up test data...');
        await employeesCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Operator examples completed successfully!');
        
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

