package Practise;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * Debug tips (IntelliJ):
 * - Breakpoint on equals(...) and hashCode()
 * - Breakpoint on map.put(...) and map.get(...)
 * - "Step Into" to see HashMap calling hashCode/equals
 */
public class StudentEqualsHashCodeDebug {
    public static void main(String[] args) {
        Map<Student, String> grades = new HashMap<>();
        Student keyAtPut = new Student(101, "Riya");   // object #1
        Student keyAtGet = new Student(101, "Riya");   // object #2 (different address)
        System.out.println("== : " + (keyAtPut == keyAtGet));                 // usually false
        System.out.println("equals : " + keyAtPut.equals(keyAtGet));           // should be true
        System.out.println("hashCode1: " + keyAtPut.hashCode());
        System.out.println("hashCode2: " + keyAtGet.hashCode());
        // BREAKPOINT HERE
        grades.put(keyAtPut, "Grade A");
        // BREAKPOINT HERE (Step Into to watch HashMap use hashCode + equals)
        String found = grades.get(keyAtGet);
        System.out.println("get result: " + found); // should print Grade A if equals/hashCode are correct
    }
    static final class Student {
        private final int roll;
        private final String name;
        Student(int roll, String name) {
            this.roll = roll;
            this.name = Objects.requireNonNull(name);
        }
        @Override
        public boolean equals(Object o) {
            // BREAKPOINT HERE
            if (this == o) return true;
            if (!(o instanceof Student)) return false;
            Student s = (Student) o;
            return roll == s.roll; // "same student" means same roll number
        }
        @Override
        public int hashCode() {
            // BREAKPOINT HERE (must depend on the same fields used in equals — here: roll)
            return Integer.hashCode(roll);
        }
        @Override
        public String toString() {
            return "Student{roll=" + roll + ", name='" + name + "'}";
        }
    }
}