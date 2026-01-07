@echo off
REM Quick test script to verify Kafka is running and project works

echo ========================================
echo Kafka Interview Project - Quick Test
echo ========================================
echo.

REM Check if Kafka is accessible
echo [1/5] Checking if Kafka is running...
cd C:\kafka
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092 >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Kafka is not running or not accessible!
    echo Please start Kafka first:
    echo   bin\windows\kafka-server-start.bat config\server.properties
    pause
    exit /b 1
)
echo OK: Kafka is running
echo.

REM Create topic if it doesn't exist
echo [2/5] Creating topic 'basic-topic'...
bin\windows\kafka-topics.bat --create --topic basic-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 >nul 2>&1
if %errorlevel% equ 0 (
    echo OK: Topic created
) else (
    echo INFO: Topic may already exist (this is OK)
)
echo.

REM Build project
echo [3/5] Building project...
cd "%~dp0"
call mvn clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)
echo OK: Project built successfully
echo.

REM Run producer
echo [4/5] Running producer (sending 10 messages)...
echo.
call mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q1.BasicProducerExample" -q
if %errorlevel% neq 0 (
    echo ERROR: Producer failed!
    pause
    exit /b 1
)
echo.
echo OK: Producer completed
echo.

REM Verify messages
echo [5/5] Verifying messages were sent...
echo.
echo Starting console consumer for 5 seconds...
echo You should see 10 messages below:
echo.
timeout /t 1 >nul
cd C:\kafka
start /B cmd /c "bin\windows\kafka-console-consumer.bat --topic basic-topic --from-beginning --bootstrap-server localhost:9092 --timeout-ms 5000"
timeout /t 6 >nul
echo.

echo ========================================
echo Test Complete!
echo ========================================
echo.
echo If you saw 10 messages above, everything is working!
echo.
pause

