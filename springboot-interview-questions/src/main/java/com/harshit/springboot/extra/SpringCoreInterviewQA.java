package com.harshit.springboot.extra;

/**
 * Spring Core interview Q&A — colocated with springboot-interview-questions (same ecosystem).
 *
 * <p><b>Q. What is Spring Framework?</b> Container + programming/model support for enterprise Java:
 * DI, AOP, declarative transactions, integration. Real life: wiring services without manual singletons.
 *
 * <p><b>Q. Dependency Injection?</b> Objects receive collaborators from outside instead of creating them.
 * Improves testability and swapping implementations. Real life: OrderService receives PaymentGateway
 * interface — unit tests inject a fake gateway.
 *
 * <p><b>Q. Types of injection?</b> Constructor (preferred), setter, field (@Autowired). Spring also
 * supports factory methods. Real life: required deps via constructor; optional flags via setter.
 *
 * <p><b>Q. Bean lifecycle?</b> Instantiate → populate properties → BeanPostProcessors → init (@PostConstruct)
 * → use → destroy (@PreDestroy) on container shutdown. Real life: open DB pool on start, close on stop.
 *
 * <p><b>Q. ApplicationContext vs BeanFactory?</b> BeanFactory is minimal container; ApplicationContext
 * adds internationalization, event propagation, AOP integration, eager singleton init by default.
 * Real life: always ApplicationContext in apps; BeanFactory is historical/low-level.
 *
 * <p><b>Q. @Component, @Service, @Repository?</b> Stereotype @Component markers; @Service for domain service
 * layer; @Repository for persistence (exception translation). Real life: clear layering for scanners/readers.
 *
 * <p><b>Q. @Autowired?</b> Injection marker (by type, then name); on constructor preferred. Real life:
 * Spring resolves NotificationService into OrderService automatically.
 *
 * <p>The runnable snippet below is plain Java showing constructor DI — Spring automates this wiring.
 */
public final class SpringCoreInterviewQA {

    interface Notifier {
        void notifyUser(String userId, String message);
    }

    static final class EmailNotifier implements Notifier {
        @Override
        public void notifyUser(String userId, String message) {
            System.out.println("EMAIL to " + userId + ": " + message);
        }
    }

    /** Domain service with injected collaborator — same idea as a Spring @Service. */
    static final class OrderService {
        private final Notifier notifier;

        OrderService(Notifier notifier) {
            this.notifier = notifier;
        }

        void placeOrder(String userId, String orderId) {
            // business logic omitted
            notifier.notifyUser(userId, "Order " + orderId + " confirmed");
        }
    }

    private SpringCoreInterviewQA() {
    }

    public static void main(String[] args) {
        OrderService orders = new OrderService(new EmailNotifier());
        orders.placeOrder("user-42", "ORD-1001");
    }
}
