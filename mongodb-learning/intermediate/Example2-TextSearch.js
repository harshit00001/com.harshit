/**
 * Example 2: Text Search
 * 
 * SIMPLE EXPLANATION:
 * Text search lets you search for words or phrases in text fields.
 * Like Google search - you type keywords and get relevant results.
 * 
 * TECHNICAL EXPLANATION:
 * MongoDB creates a text index on string fields.
 * You can search using $text operator and get relevance scores.
 * Text search is case-insensitive and supports stemming.
 * 
 * INTERVIEW POINT:
 * - Only one text index per collection
 * - Text index can include multiple fields
 * - $text search returns documents sorted by relevance score
 * - Use $meta: "textScore" to get relevance scores
 */

const { connectToDatabase, closeConnection } = require('../config/database');

async function main() {
    let client;
    
    try {
        console.log('🔄 Connecting to MongoDB...\n');
        const { db, client: dbClient } = await connectToDatabase();
        client = dbClient;
        
        const articlesCollection = db.collection('articles');
        
        // Clean up
        await articlesCollection.deleteMany({});
        try {
            await articlesCollection.dropIndex('title_text_description_text');
        } catch (e) {
            // Index might not exist
        }
        
        // ============================================
        // INSERT SAMPLE DATA
        // ============================================
        console.log('📝 Inserting sample articles...\n');
        
        const articles = [
            {
                title: 'Introduction to MongoDB',
                description: 'MongoDB is a popular NoSQL database that stores data in flexible documents.',
                content: 'MongoDB is a document database that provides high performance and scalability. It uses JSON-like documents with optional schemas.',
                author: 'John Doe',
                tags: ['database', 'mongodb', 'nosql'],
                views: 1500
            },
            {
                title: 'JavaScript Best Practices',
                description: 'Learn the best practices for writing clean and maintainable JavaScript code.',
                content: 'JavaScript is a powerful programming language. Follow these best practices to write better code and avoid common pitfalls.',
                author: 'Jane Smith',
                tags: ['javascript', 'programming', 'best-practices'],
                views: 2300
            },
            {
                title: 'Node.js Performance Optimization',
                description: 'Tips and tricks to optimize your Node.js applications for better performance.',
                content: 'Node.js applications can be optimized in many ways. Learn about caching, async operations, and database optimization.',
                author: 'Bob Johnson',
                tags: ['nodejs', 'performance', 'optimization'],
                views: 1800
            },
            {
                title: 'MongoDB Aggregation Pipeline',
                description: 'Master the MongoDB aggregation pipeline for complex data transformations.',
                content: 'The aggregation pipeline is one of MongoDB\'s most powerful features. It allows you to transform and analyze data efficiently.',
                author: 'Alice Williams',
                tags: ['mongodb', 'aggregation', 'data-analysis'],
                views: 2100
            },
            {
                title: 'Building REST APIs with Express',
                description: 'A comprehensive guide to building RESTful APIs using Express.js and MongoDB.',
                content: 'Express.js makes it easy to build REST APIs. Combine it with MongoDB for a powerful backend solution.',
                author: 'Charlie Brown',
                tags: ['express', 'api', 'rest', 'mongodb'],
                views: 1900
            }
        ];
        
        await articlesCollection.insertMany(articles);
        console.log(`✅ Inserted ${articles.length} articles\n`);
        
        // ============================================
        // CREATE TEXT INDEX
        // ============================================
        console.log('📊 === CREATING TEXT INDEX ===\n');
        
        console.log('Creating text index on title and description fields...');
        await articlesCollection.createIndex({
            title: 'text',
            description: 'text',
            content: 'text'
        });
        console.log('✅ Text index created\n');
        
        // ============================================
        // BASIC TEXT SEARCH
        // ============================================
        console.log('🔍 === BASIC TEXT SEARCH ===\n');
        
        console.log('1. Searching for "MongoDB":');
        const mongodbResults = await articlesCollection
            .find({ $text: { $search: 'MongoDB' } })
            .toArray();
        
        mongodbResults.forEach((article, index) => {
            console.log(`   ${index + 1}. ${article.title}`);
        });
        console.log(`   Found ${mongodbResults.length} articles\n`);
        
        // ============================================
        // TEXT SEARCH WITH RELEVANCE SCORE
        // ============================================
        console.log('⭐ === TEXT SEARCH WITH RELEVANCE ===\n');
        
        console.log('2. Searching for "performance optimization" with relevance scores:');
        const performanceResults = await articlesCollection
            .find(
                { $text: { $search: 'performance optimization' } },
                { score: { $meta: 'textScore' } }
            )
            .sort({ score: { $meta: 'textScore' } })
            .toArray();
        
        performanceResults.forEach((article, index) => {
            const score = article.score || 0;
            console.log(`   ${index + 1}. ${article.title} (Score: ${score.toFixed(2)})`);
        });
        console.log('');
        
        // ============================================
        // MULTI-WORD SEARCH
        // ============================================
        console.log('🔤 === MULTI-WORD SEARCH ===\n');
        
        console.log('3. Searching for "JavaScript" OR "Node.js":');
        const jsResults = await articlesCollection
            .find({ $text: { $search: 'JavaScript Node.js' } })
            .toArray();
        
        jsResults.forEach((article, index) => {
            console.log(`   ${index + 1}. ${article.title}`);
        });
        console.log(`   Found ${jsResults.length} articles\n`);
        
        // ============================================
        // PHRASE SEARCH
        // ============================================
        console.log('💬 === PHRASE SEARCH ===\n');
        
        console.log('4. Searching for exact phrase "best practices":');
        const phraseResults = await articlesCollection
            .find({ $text: { $search: '"best practices"' } })
            .toArray();
        
        phraseResults.forEach((article, index) => {
            console.log(`   ${index + 1}. ${article.title}`);
        });
        console.log(`   Found ${phraseResults.length} articles\n`);
        
        // ============================================
        // EXCLUDING WORDS
        // ============================================
        console.log('🚫 === EXCLUDING WORDS ===\n');
        
        console.log('5. Searching for "MongoDB" but excluding "aggregation":');
        const excludeResults = await articlesCollection
            .find({ $text: { $search: 'MongoDB -aggregation' } })
            .toArray();
        
        excludeResults.forEach((article, index) => {
            console.log(`   ${index + 1}. ${article.title}`);
        });
        console.log(`   Found ${excludeResults.length} articles\n`);
        
        // ============================================
        // COMBINING TEXT SEARCH WITH OTHER QUERIES
        // ============================================
        console.log('🔗 === COMBINING TEXT SEARCH ===\n');
        
        console.log('6. Searching for "MongoDB" with views > 2000:');
        const combinedResults = await articlesCollection
            .find({
                $text: { $search: 'MongoDB' },
                views: { $gt: 2000 }
            })
            .toArray();
        
        combinedResults.forEach((article, index) => {
            console.log(`   ${index + 1}. ${article.title} (${article.views} views)`);
        });
        console.log(`   Found ${combinedResults.length} articles\n`);
        
        // ============================================
        // CASE-INSENSITIVE SEARCH
        // ============================================
        console.log('🔤 === CASE-INSENSITIVE SEARCH ===\n');
        
        console.log('7. Searching for "mongodb" (lowercase):');
        const caseInsensitiveResults = await articlesCollection
            .find({ $text: { $search: 'mongodb' } })
            .toArray();
        
        caseInsensitiveResults.forEach((article, index) => {
            console.log(`   ${index + 1}. ${article.title}`);
        });
        console.log(`   Found ${caseInsensitiveResults.length} articles\n`);
        
        // ============================================
        // LANGUAGE-SPECIFIC TEXT INDEX
        // ============================================
        console.log('🌍 === LANGUAGE-SPECIFIC INDEX ===\n');
        
        // Note: MongoDB supports language-specific stemming
        // Default language is English
        console.log('8. Text index uses English language by default');
        console.log('   This enables stemming (e.g., "running" matches "run")\n');
        
        // Cleanup
        console.log('🧹 Cleaning up...');
        await articlesCollection.deleteMany({});
        console.log('✅ Cleanup complete\n');
        
        console.log('✅ Text search examples completed successfully!');
        console.log('\n💡 Key Takeaways:');
        console.log('   - Text search is case-insensitive');
        console.log('   - Use $meta: "textScore" to get relevance scores');
        console.log('   - Phrase search uses double quotes');
        console.log('   - Exclude words with minus sign (-)');
        console.log('   - Can combine text search with other query operators');
        
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

