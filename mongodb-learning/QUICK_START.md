# MongoDB Quick Start Guide

## ⚡ Fast Setup (5 Minutes)

### Step 1: Install MongoDB

**Windows:**
1. Download from: https://www.mongodb.com/try/download/community
2. Run installer → Choose "Complete" → Install as Service
3. Done! MongoDB starts automatically

**Mac:**
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

**Linux:**
```bash
# Ubuntu/Debian
sudo apt-get install -y mongodb

# Start MongoDB
sudo systemctl start mongod
sudo systemctl enable mongod
```

### Step 2: Install Node.js Dependencies

```bash
cd mongodb-learning
npm install
```

### Step 3: Test Connection

```bash
node basic/Example1-Connection.js
```

If you see "Connected to MongoDB successfully!", you're ready! ✅

---

## 🌐 Alternative: Use MongoDB Atlas (Cloud)

1. Sign up: https://www.mongodb.com/cloud/atlas/register
2. Create free cluster (M0)
3. Get connection string
4. Update `config/database.js` with your connection string
5. Run examples!

---

## 🎯 Run Your First Example

```bash
# Basic CRUD operations
node basic/Example2-CRUD.js
```

---

## ❓ Need Help?

- Check `README.md` for detailed instructions
- MongoDB not starting? See Troubleshooting in README.md
- Connection issues? Verify your connection string in `config/database.js`

---

**That's it! Start learning! 🚀**

