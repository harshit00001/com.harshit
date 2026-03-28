package com.harshit.preparation.topic10;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Topic 10 — Spring Boot (concepts + tiny plain-Java analogues you can run).
 */
public final class Topic10Qa {

    private Topic10Qa() {
    }

    /*
     * Q: How does Spring Boot auto-configuration work internally?
     *
     * SCRIPT:
     * I explain that Spring Boot brings in starter dependencies on the classpath, and auto-configuration
     * classes—annotated with conditions like @ConditionalOnClass or @ConditionalOnMissingBean—register
     * beans only when it makes sense. If JDBC is present and I have not defined a DataSource, Boot can
     * create an embedded one for development. The key idea is convention over configuration with
     * escape hatches: my beans override defaults when I define them.
     */

    /*
     * Q: @Component vs @Service vs @Repository?
     *
     * SCRIPT:
     * Technically they are all stereotypes that register a Spring bean. The difference is semantic
     * and a bit of behavior: @Service marks application services, @Repository marks persistence and
     * enables exception translation to DataAccessException, @Component is the generic bucket. In an
     * interview I say I use the right stereotype so the codebase reads clearly and tools understand intent.
     */

    /*
     * Q: @SpringBootApplication?
     *
     * SCRIPT:
     * It is a convenience annotation that combines @Configuration, @EnableAutoConfiguration, and
     * @ComponentScan so my main class bootstraps the application in one line. I mention that scan
     * picks up beans in the package and below, so I keep the main class in a sensible root package.
     */

    /*
     * Q: Spring Boot Starter?
     *
     * SCRIPT:
     * A starter is a dependency that pulls a curated set of libraries with compatible versions—
     * for example spring-boot-starter-web brings Spring MVC, Tomcat, and Jackson together. It saves
     * me from managing twenty version numbers manually and reduces “dependency hell” in production apps.
     */

    /*
     * Q: How does Spring decide which beans to create?
     *
     * SCRIPT:
     * Component scanning finds @Component, @Service, @Configuration classes; @Bean methods register
     * explicit beans; auto-configuration adds more based on classpath; @Conditional annotations
     * gate beans on properties or missing beans. Order and @Primary matter when multiple candidates
     * exist—I would mention I resolve conflicts explicitly rather than relying on accident.
     */

    /*
     * Q: Actuator?
     *
     * SCRIPT:
     * Actuator exposes operational endpoints like health and metrics. In Kubernetes I wire liveness
     * and readiness probes to /actuator/health; in production I lock down endpoints and expose only
     * what monitoring needs. It is how the app becomes observable without custom boilerplate for every service.
     */

    /*
     * Q: @Value vs @ConfigurationProperties?
     *
     * SCRIPT:
     * @Value injects a single property, often with SpEL. @ConfigurationProperties binds a whole prefix
     * to a typed class, supports validation, and scales when I have many related keys like app.payment.*.
     * I prefer @ConfigurationProperties for structured config and @Value for one-offs or feature flags.
     */

    /*
     * Q: @ConditionalOnProperty?
     *
     * SCRIPT:
     * It creates a bean only when a property matches—for example enable a mock client in dev when
     * feature.mock=true. That keeps one codebase with environment-specific behavior without if-else
     * in every class.
     */

    /**
     * Plain-Java sketch: “beans” registered only when a condition holds (like @ConditionalOnClass / @ConditionalOnProperty).
     */
    public static void demo() {
        boolean jdbcOnClasspath = true;
        boolean mockPayments = Boolean.parseBoolean(System.getProperty("feature.mock", "false"));

        Map<String, Supplier<String>> beans = new LinkedHashMap<>();
        beans.put("orderService", () -> "OrderService @Service");
        if (jdbcOnClasspath) {
            beans.put("dataSource", () -> "DataSource (auto-config if missing custom bean)");
        }
        if (mockPayments) {
            beans.put("paymentClient", () -> "MockPaymentClient");
        }

        System.out.println("Registered beans: " + beans.keySet());
        System.out.println("Tip: run with -Dfeature.mock=true to register mockPayments.");
    }

    public static void main(String[] args) {
        demo();
    }
}
