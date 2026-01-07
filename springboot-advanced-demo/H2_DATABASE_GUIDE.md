# H2 Database Console Access Guide

## 🌐 How to Access H2 Database in Browser

### Step 1: Start the Application
```bash
mvn spring-boot:run
```

### Step 2: Open H2 Console in Browser
**URL:** `http://localhost:8080/h2-console`

### Step 3: Login with These Credentials

**JDBC URL:** `jdbc:h2:mem:testdb`
**User Name:** `sa`
**Password:** (leave empty)

Click **Connect**

---

## 📊 What You Can Do in H2 Console

### 1. View All Tables
```sql
SHOW TABLES;
```

### 2. View Users Table
```sql
SELECT * FROM users;
```

### 3. View Orders Table (if exists)
```sql
SELECT * FROM orders;
```

### 4. Count Records
```sql
SELECT COUNT(*) FROM users;
```

### 5. View Table Structure
```sql
DESC users;
```

---

## 🔍 Quick SQL Queries to Test

### View All Users
```sql
SELECT id, name, email, age FROM users;
```

### View Users with Pagination (Manual)
```sql
SELECT * FROM users ORDER BY id LIMIT 10 OFFSET 0;
```

### View Next Page
```sql
SELECT * FROM users ORDER BY id LIMIT 10 OFFSET 10;
```

### Search Users by Email
```sql
SELECT * FROM users WHERE email LIKE '%example%';
```

---

## 📝 Connection Details Summary

| Property | Value |
|----------|-------|
| **URL** | http://localhost:8080/h2-console |
| **JDBC URL** | jdbc:h2:mem:testdb |
| **Username** | sa |
| **Password** | (empty) |
| **Database Type** | In-Memory (H2) |

---

## ⚠️ Important Notes

1. **In-Memory Database**: Data is lost when application stops
2. **Auto-Created**: Tables are created automatically on startup
3. **Test Data**: DataInitializer creates 25 test users on startup
4. **Console Enabled**: H2 console is enabled for development only

---

## 🎯 Use Cases

- **Verify Data**: Check if test users were created
- **Test Queries**: Run SQL queries manually
- **Debug**: See actual data in database
- **Learn**: Understand database structure
- **Demonstrate**: Show database contents during presentation

---

## 🔗 Related Endpoints

- **H2 Console**: http://localhost:8080/h2-console
- **Actuator**: http://localhost:8080/actuator
- **Health**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics

---

## 💡 Tips

1. Keep the console open while demonstrating
2. Run queries to show data before/after operations
3. Use it to verify caching (same data returned)
4. Show pagination by running LIMIT/OFFSET queries
5. Compare with API responses










