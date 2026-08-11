package com.harshit.springboot.extra;

/**
 * Spring Boot interview Q&A — colocated with springboot-interview-questions.
 *
 * <p><b>Q. What is Spring Boot?</b> Opinionated layer on Spring: auto-configuration, starters, embedded
 * server, actuator, fat jars. Real life: stand up REST service in minutes with sensible defaults.
 *
 * <p><b>Q. Auto-configuration?</b> Classpath-conditional @Configuration classes (e.g., if JDBC on classpath,
 * configure DataSource). Real life: adding spring-boot-starter-data-jpa pulls Hibernate without XML.
 *
 * <p><b>Q. Spring Boot starter?</b> Curated dependency BOM (e.g., web, data-jpa, security). Real life:
 * one starter coordinate instead of juggling 20 library versions.
 *
 * <p><b>Q. Spring vs Spring Boot?</b> Spring is the framework; Boot is productivity/runtime packaging with
 * auto-config and starters. Real life: Spring alone needs more manual wiring; Boot is default for services.
 *
 * <p><b>Q. application.properties / application.yml?</b> Externalized config for ports, datasource URLs,
 * feature flags. Profiles (dev/prod) swap config. Real life: k8s ConfigMaps map cleanly to properties.
 *
 * <p><b>Q. Actuator?</b> Production endpoints (/health, /metrics, /info) for observability. Real life:
 * probes for Kubernetes liveness/readiness.
 *
 * <p><b>Q. How to create REST APIs?</b> @RestController + @GetMapping/@PostMapping + DTOs; return JSON via
 * Jackson; validate with @Valid. Real life: OrderController delegates to OrderService.
 *
 * <p>Run the main project {@code SpringBootInterviewApplication} to see Boot + REST in action; this class
 * stays lightweight so it does not start a second server.
 */
public final class SpringBootBasicsInterviewQA {

    private SpringBootBasicsInterviewQA() {
    }

    public static void main(String[] args) {
        System.out.println("Spring Boot Q&A (read Javadoc). Run SpringBootInterviewApplication for a live Boot app.");
    }
}
