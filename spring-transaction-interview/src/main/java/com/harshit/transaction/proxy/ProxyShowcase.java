package com.harshit.transaction.proxy;

import org.springframework.stereotype.Component;

/**
 * <h2>How Spring implements {@code @Transactional}: proxies (AOP)</h2>
 *
 * <p><b>Interview question:</b> "How does Spring actually apply {@code @Transactional}? What is
 * the difference between a JDK dynamic proxy and a CGLIB proxy?"
 *
 * <p><b>Answer:</b> {@code @Transactional} is implemented with <i>aspect-oriented programming</i>.
 * Spring wraps your bean in a <i>proxy object</i>. Callers hold a reference to the proxy. When they
 * invoke a method, the proxy runs "before" advice (start or join transaction), calls your real
 * target bean, then runs "after" advice (commit or rollback). Your target class is often unaware
 * of this — that is why internal {@code this} calls skip the proxy.
 *
 * <p><b>JDK dynamic proxy:</b> requires an interface. At runtime, Spring builds a class that
 * implements the same interfaces and delegates to your bean. You typically inject the interface
 * type.
 *
 * <p><b>CGLIB proxy:</b> Spring subclasses your concrete class at runtime (or generates a subclass
 * of the proxy target). Used when there is no interface, or when {@code proxy-target-class=true}
 * (the default in Spring Boot for many setups).
 *
 * <p><b>Spring Boot 3 note:</b> Default is often {@code spring.aop.proxy-target-class=true}, so you
 * may see {@code ...EnhancerBySpringCGLIB...} even for beans that implement interfaces. Setting
 * {@code spring.aop.proxy-target-class=false} prefers JDK interface proxies when applicable.
 *
 * <p>This component prints the actual runtime class names so you can see what the container
 * created on your machine with your current configuration.
 */
@Component
public class ProxyShowcase {

    private final PaymentGateway paymentGateway;
    private final LegacyBatchProcessor legacyBatchProcessor;

    public ProxyShowcase(PaymentGateway paymentGateway, LegacyBatchProcessor legacyBatchProcessor) {
        this.paymentGateway = paymentGateway;
        this.legacyBatchProcessor = legacyBatchProcessor;
    }

    /**
     * Returns a human-readable description of the two beans' proxy classes.
     */
    public String describeProxies() {
        String gateway = paymentGateway.getClass().getName();
        String batch = legacyBatchProcessor.getClass().getName();
        return String.format(
                "PaymentGateway (implements interface) -> %s%n"
                        + "LegacyBatchProcessor (concrete class) -> %s%n"
                        + "Spring Boot 3 default is CGLIB subclass for both (spring.aop.proxy-target-class=true).%n"
                        + "Set proxy-target-class=false to prefer JDK dynamic proxies for interface beans.",
                gateway, batch);
    }
}
