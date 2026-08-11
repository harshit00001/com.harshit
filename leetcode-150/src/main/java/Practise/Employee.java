package Practise;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Employee {
    private final String empName;
    private final int empId;
    private final String city;
    private final int salary;          // or double if you need decimals
    private final String department;
    public Employee(String empName, int salary, int empId, String city, String department) {
        this.empName = empName;
        this.salary = salary;
        this.empId = empId;
        this.city = city;
        this.department = department;
    }
    public String getEmpName() {
        return empName;
    }
    public int getEmpId() {
        return empId;
    }
    public String getCity() {
        return city;
    }
    public int getSalary() {
        return salary;
    }
    public String getDepartment() {
        return department;
    }
    @Override
    public String toString() {
        return "Employee{empName='" + empName + "', empId=" + empId
                + ", city='" + city + "', salary=" + salary
                + ", department='" + department + "'}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return empId == employee.empId;
    }
    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }

    public static void main(String[] args) {

// … your Employee class …
        List<Employee> employees = Arrays.asList(
                new Employee("Harini",  12000, 6,  "Cochin",  "Accounts"),
                new Employee("Rahul",   18000, 1,  "Mumbai",  "HR"),
                new Employee("Sneha",   22000, 2,  "Cochin",  "IT"),
                new Employee("Vikram",  15000, 3,  "Pune",    "IT"),
                new Employee("Anita",   30000, 4,  "Mumbai",  "HR"),
                new Employee("Kiran",   11000, 5,  "Bengaluru", "Support"),
                new Employee("Divya",   40000, 7,  "Pune",    "Accounts")
        );
        //employees.stream().filter(x->x.getCity().equals("Pune")).forEach(System.out::println);
        employees.stream().filter(x->x.getSalary()>21000).forEach(System.out::println);
        long map= employees.stream().filter(x->x.getCity().equals("Pune")).count();
        System.out.println(map);
        String s= "avinash";
        long count=s.chars().filter(c->c=='s').count();
        System.out.println(count);

        Map<Character,Long> map2 = s.chars().mapToObj(ch->(char)ch).collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        map2.forEach((a,b)->{
            System.out.println(a+": " +b);

        });
    }
}