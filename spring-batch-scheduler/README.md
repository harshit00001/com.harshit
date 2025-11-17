# Spring Batch + Spring Scheduler - Complete Guide

A comprehensive project demonstrating Spring Batch and Spring Scheduler integration with clear explanations and code examples.

## 📚 What's Covered

### 1. **Spring Batch Basics**
- What is Spring Batch
- ItemReader, ItemProcessor, ItemWriter
- Job and Step concepts
- Chunk-based processing

### 2. **Spring Scheduler**
- @Scheduled annotation
- Cron expressions
- Fixed delay vs Fixed rate
- Integration with Batch

### 3. **Complete Example**
- CSV file processing
- Data transformation
- Database storage
- Automated scheduling

## 🎯 Interview Questions & Answers

### Q: What is Spring Batch?

**Technical:**
- Framework for processing large volumes of data
- Provides infrastructure for robust batch jobs
- Supports chunk-based processing, transaction management, job restart
- Handles ETL (Extract, Transform, Load) operations

**Simple:**
- Like an assembly line for data
- Read data → Process/Transform → Write data
- Handles large amounts of data efficiently
- Built for batch processing (not real-time)

**Key Components:**
1. **ItemReader**: Reads data (CSV, database, etc.)
2. **ItemProcessor**: Transforms/validates data
3. **ItemWriter**: Writes data (database, file, etc.)
4. **Job**: Complete workflow
5. **Step**: One unit of work

---

### Q: What is the difference between ItemReader, ItemProcessor, and ItemWriter?

**Technical:**

- **ItemReader**: 
  - Reads data from source
  - Returns one item at a time
  - Returns null when done
  - Examples: CSV file, database query, API

- **ItemProcessor**:
  - Transforms/validates items
  - Input and output can be different types
  - Can filter items (return null to skip)
  - Optional component

- **ItemWriter**:
  - Writes processed items to destination
  - Receives chunk of items (not one at a time)
  - All items in chunk written in one transaction
  - Examples: Database insert, file write, message queue

**Simple:**
- **Reader**: "Get me the next record"
- **Processor**: "Clean up this data"
- **Writer**: "Save these records"

**Flow:**
```
CSV File → Reader → Processor → Writer → Database
```

---

### Q: What is chunk-based processing?

**Technical:**
- Processes items in groups (chunks)
- Example: `chunk(10)` processes 10 items at a time
- Each chunk is one transaction
- If one item fails, entire chunk is rolled back

**Simple:**
- Instead of processing one item at a time, process 10 at once
- Like batching - more efficient
- All 10 items succeed or all fail together

**Benefits:**
- Better performance (batch operations)
- Transaction management (all or nothing)
- Memory efficient (don't load everything at once)

**Example:**
```java
.<User, User>chunk(10)  // Process 10 users at a time
```

---

### Q: What is Spring Scheduler?

**Technical:**
- Automates task execution at defined intervals
- Uses `@Scheduled` annotation
- Supports cron expressions, fixed delay, fixed rate
- Runs in background thread pool

**Simple:**
- Like an alarm clock for your code
- Runs code automatically at specific times
- No manual trigger needed

**Key Annotations:**
- `@EnableScheduling`: Enable scheduling (in main class)
- `@Scheduled`: Schedule a method

---

### Q: What is a Cron Expression?

**Technical:**
- Defines schedule using 6 or 7 fields
- Format: `second minute hour day month weekday [year]`

**Simple:**
- A way to say "run at this time"
- Like setting an alarm

**Format:**
```
┌───────────── second (0-59)
│ ┌─────────── minute (0-59)
│ │ ┌───────── hour (0-23)
│ │ │ ┌─────── day of month (1-31)
│ │ │ │ ┌───── month (1-12)
│ │ │ │ │ ┌─── day of week (0-7, 0 and 7 = Sunday)
│ │ │ │ │ │
* * * * * *
```

**Examples:**
- `"0 0 0 * * ?"` = Every day at midnight (00:00:00)
- `"0 0 12 * * ?"` = Every day at noon (12:00:00)
- `"0 0 0 1 * ?"` = First day of every month at midnight
- `"0 0 0 ? * MON"` = Every Monday at midnight
- `"0 */5 * * * ?"` = Every 5 minutes
- `"0 0 9-17 * * MON-FRI"` = Every hour from 9 AM to 5 PM, Monday to Friday

---

### Q: What is the difference between fixedDelay and fixedRate?

**Technical:**

- **fixedDelay**:
  - Waits for specified time after previous execution completes
  - Next execution starts after delay from completion
  - Example: If job takes 2 minutes and delay is 5 minutes, next run is 7 minutes from start

- **fixedRate**:
  - Runs at fixed interval regardless of execution time
  - Next execution starts at fixed interval from previous start
  - Example: If rate is 5 minutes, runs every 5 minutes regardless of how long job takes

**Simple:**
- **fixedDelay**: "Wait 5 minutes after job finishes, then run again"
- **fixedRate**: "Run every 5 minutes, no matter how long job takes"

**Example:**
```java
@Scheduled(fixedDelay = 300000)  // 5 minutes after completion
@Scheduled(fixedRate = 300000)   // Every 5 minutes
```

---

### Q: How do Spring Batch and Spring Scheduler work together?

**Technical:**
- Scheduler triggers Batch job at scheduled time
- JobLauncher starts the batch job
- Job executes Reader → Processor → Writer flow
- Job completes and scheduler waits for next schedule

**Simple:**
- Scheduler: "It's time to run!"
- Batch: "Okay, let me process the data"
- Scheduler: "I'll check again at the next scheduled time"

**Integration Flow:**
```
Scheduler (@Scheduled)
    ↓
JobLauncher.run()
    ↓
Batch Job
    ↓
Step (Reader → Processor → Writer)
    ↓
Complete
```

**Code Example:**
```java
@Scheduled(cron = "0 0 0 * * ?")  // Midnight
public void scheduleJob() {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    jobLauncher.run(processUserJob, params);
}
```

---

### Q: What is JobLauncher?

**Technical:**
- Interface for launching batch jobs
- Starts job execution with job parameters
- Each run needs unique parameters (timestamp)
- Returns JobExecution

**Simple:**
- The "start button" for batch jobs
- Takes job and parameters, runs it

**Example:**
```java
jobLauncher.run(processUserJob, jobParameters);
```

---

### Q: What are Job Parameters?

**Technical:**
- Key-value pairs passed to job
- Used to identify job instance
- Must be unique for each run
- Stored in batch metadata tables

**Simple:**
- Information passed to job when starting
- Like command-line arguments
- Each run needs unique parameters (usually timestamp)

**Example:**
```java
JobParameters params = new JobParametersBuilder()
    .addLong("time", System.currentTimeMillis())
    .addString("source", "CSV")
    .toJobParameters();
```

---

## 📝 Project Structure

```
spring-batch-scheduler/
├── src/main/java/com/harshit/batch/
│   ├── SpringBatchSchedulerApplication.java  # Main class
│   ├── config/
│   │   └── BatchConfig.java                  # Batch configuration
│   ├── model/
│   │   └── User.java                         # Data model
│   ├── reader/
│   │   └── UserItemReader.java               # Reads CSV
│   ├── processor/
│   │   └── UserItemProcessor.java            # Transforms data
│   ├── writer/
│   │   └── UserItemWriter.java               # Writes to DB
│   └── scheduler/
│       └── BatchJobScheduler.java            # Schedules jobs
└── src/main/resources/
    ├── application.properties                # Configuration
    ├── schema.sql                            # Database schema
    └── users.csv                             # Input CSV file
```

---

## 🚀 How to Run

1. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

2. **Job will run automatically:**
   - Every minute (demo schedule)
   - Or at midnight (production schedule)

3. **View results:**
   - Check console logs
   - Access H2 console: http://localhost:8080/h2-console
   - Query: `SELECT * FROM users;`

---

## 💡 Key Concepts Summary

### Spring Batch:
- **Reader**: Gets data from source
- **Processor**: Transforms/validates data
- **Writer**: Saves data to destination
- **Chunk**: Process N items at a time
- **Job**: Complete workflow
- **Step**: One unit of work

### Spring Scheduler:
- **@Scheduled**: Schedule a method
- **Cron**: Time-based schedule
- **Fixed Delay**: Wait after completion
- **Fixed Rate**: Run at interval
- **@EnableScheduling**: Enable scheduling

### Integration:
- Scheduler triggers Batch job
- JobLauncher starts job
- Job processes data
- Completes and waits for next schedule

---

## 🎓 Best Practices

1. **Use chunk processing** for large datasets
2. **Handle errors** in processor (return null to skip)
3. **Use unique job parameters** for each run
4. **Monitor job execution** (logs, metrics)
5. **Test schedules** before production
6. **Use appropriate cron expressions** for your needs
7. **Handle job failures** (restart capability)

---

**Happy Learning! 🎓**

