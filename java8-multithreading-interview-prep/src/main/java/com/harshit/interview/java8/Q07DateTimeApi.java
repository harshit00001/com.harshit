package com.harshit.interview.java8;

import com.harshit.interview.common.InterviewDemo;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * INTERVIEW Q: Why java.time replaced Date/Calendar? Explain key classes.
 *
 * <p><b>Immutable, thread-safe, clear API:</b> LocalDate, LocalTime, LocalDateTime, ZonedDateTime, Instant.
 *
 * <p><b>4 YOE:</b> Always mention timezone explicitly in distributed systems (Instant + ZoneId).
 */
public final class Q07DateTimeApi implements InterviewDemo {

    @Override
    public void run() {
        System.out.println("=== Q07: java.time API ===\n");

        LocalDate today = LocalDate.now();
        LocalDateTime meeting = LocalDateTime.of(2026, 6, 12, 14, 30);
        System.out.println("Today: " + today);
        System.out.println("Meeting: " + meeting);

        // Parsing / formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        System.out.println("Formatted: " + meeting.format(formatter));

        // Duration (time-based) vs Period (date-based)
        Duration callDuration = Duration.ofMinutes(45);
        System.out.println("Call ends at: " + meeting.plus(callDuration));

        LocalDate deadline = today.plus(2, ChronoUnit.WEEKS);
        System.out.println("Deadline: " + deadline);

        // Instant — UTC timeline for logs/events
        Instant event = Instant.now();
        ZonedDateTime ist = event.atZone(ZoneId.of("Asia/Kolkata"));
        System.out.println("Event in IST: " + ist);

        System.out.println("\n→ Old java.util.Date is mutable and confusing — avoid in new code.");
    }

    public static void main(String[] args) throws Exception {
        new Q07DateTimeApi().run();
    }
}
