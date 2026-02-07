# How to View Data in MongoDB Compass

After running `Example2CRUD`, here's how to view the data in MongoDB Compass:

## Step 1: Install MongoDB Compass

If you don't have it installed:
- Download from: https://www.mongodb.com/try/download/compass
- Install and launch MongoDB Compass

## Step 2: Connect to Your MongoDB

### For Local MongoDB (Default)

1. **Open MongoDB Compass**
2. **Connection String:**
   ```
   mongodb://localhost:27017
   ```
   Or simply click "Fill in connection fields individually" and enter:
   - **Host:** `localhost`
   - **Port:** `27017`
   - **Authentication:** None (if no auth is set up)

3. **Click "Connect"**

### For MongoDB Atlas

1. **Get connection string from Atlas:**
   - Go to your cluster in Atlas
   - Click "Connect" → "Connect using MongoDB Compass"
   - Copy the connection string

2. **Paste the connection string in Compass and click "Connect"**

## Step 3: Navigate to Your Database

After connecting, you'll see a list of databases:

1. **Find and click on:** `mongodb_learning`
   - This is the database name from `DatabaseConfig.java`

2. **You'll see collections in this database**

## Step 4: View the Data

### Collection: `users`

1. **Click on the `users` collection**
   - This is where `Example2CRUD` inserts the data

2. **You should see 4 documents:**
   - **John Doe** (john.doe@example.com) - Age: 31, City: New York
   - **Jane Smith** (jane.smith@example.com) - Age: 25, City: Los Angeles
   - **Bob Johnson** (bob.johnson@example.com) - Age: 36, City: Chicago
   - **Alice Williams** (alice.williams@example.com) - Age: 28, City: New York

### What You'll See

Each document will look like this:

```json
{
  "_id": ObjectId("..."),
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 31,
  "city": "New York",
  "interests": ["coding", "reading", "traveling"],
  "status": "active",
  "createdAt": ISODate("2024-..."),
  "updatedAt": ISODate("2024-...")
}
```

## Step 5: Explore the Data

### View Documents
- **List View:** See all documents in a table format
- **JSON View:** See individual documents in JSON format
- **Table View:** See data in a spreadsheet-like format

### Filter Documents
- Click the **"Filter"** button
- Enter a filter like: `{ "city": "New York" }`
- Or use the filter builder UI

### Sort Documents
- Click column headers to sort
- Or use the sort option in the filter bar

### Search
- Use the search bar to find specific text in documents

## Step 6: Verify Updates

Since you commented out the delete operations, you should see:

1. **All 4 users** are present
2. **John's age** is updated to 31 (was 30)
3. **Bob's age** is updated to 36 (was 35, then incremented by 1)
4. **All users** have `"status": "active"` field (added by updateMany)

## Quick Reference

| Item | Value |
|------|-------|
| **Database** | `mongodb_learning` |
| **Collection** | `users` |
| **Connection (Local)** | `mongodb://localhost:27017` |
| **Documents** | 4 users |
| **Fields** | name, email, age, city, interests, status, createdAt, updatedAt |

## Troubleshooting

### Can't see the database?
- Make sure MongoDB is running: `net start MongoDB` (Windows)
- Verify connection string is correct
- Check if the program ran successfully

### Can't see the collection?
- Run the example again: `mvn exec:java -Dexec.mainClass="com.harshit.mongodb.basic.Example2CRUD"`
- Collections are created automatically on first insert

### Collection is empty?
- The delete operations were commented out, so data should persist
- If empty, run the example again to insert data

## Other Collections You Might See

After running other examples:
- `products` - From Example3Query
- `employees` - From Example4Operators
- `users` (with embedded docs) - From Example5EmbeddedDocuments
- `connection_test` - Temporary test collection (may be deleted)

## Tips

1. **Refresh:** Click the refresh button to see latest data
2. **Export:** You can export data as JSON or CSV
3. **Validate:** Use the Schema tab to see document structure
4. **Indexes:** Check the Indexes tab to see created indexes

Happy exploring! 🎉

