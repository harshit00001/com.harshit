package com.harshit.jpa.extra;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA / Hibernate interview Q&A — under hibernate-jpa-interview-prep.
 *
 * <p><b>Q. ORM?</b> Maps tables/rows to objects/associations so you manipulate domain objects instead of SQL.
 * Real life: Order entity with line items instead of manual joins in every DAO.
 *
 * <p><b>Q. Hibernate?</b> Popular JPA provider + its native APIs (Session, Criteria, caching levels).
 *
 * <p><b>Q. JPA?</b> Standard persistence API; Hibernate implements it. Real life: code to interfaces (EntityManager).
 *
 * <p><b>Q. JPA vs Hibernate?</b> JPA is spec; Hibernate is implementation + extras (filters, bytecode enhancement).
 *
 * <p><b>Q. Entity?</b> Persistent domain object mapped to a table (@Entity @Table).
 *
 * <p><b>Q. Lazy vs Eager loading?</b> Lazy loads associations on access (proxies); Eager fetches immediately.
 * Trade-off: N+1 vs memory. Real life: lazy Order→Lines in lists; eager small, always-needed refs carefully.
 *
 * <p><b>Q. Cascade?</b> Propagates persistence operations (PERSIST, MERGE, REMOVE) to related entities.
 * Real life: saving Order cascades to new LineItems.
 *
 * <p><b>Q. @OneToMany / @ManyToOne?</b> Owning side usually many-side with foreign key; bidirectional needs
 * mappedBy. Real life: Customer 1—* Order.
 *
 * <p><b>Q. JPQL?</b> Object-oriented query language over entities, not tables. Real life:
 * {@code SELECT o FROM Order o WHERE o.status = :status}.
 *
 * <p>Minimal annotation demo (not wired to a live persistence.xml here — see module examples for CRUD).
 */
@Entity
@Table(name = "hq_department")
class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();
}

@Entity
@Table(name = "hq_employee")
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;
}

public final class JpaHibernateInterviewQA {

    private JpaHibernateInterviewQA() {
    }

    public static void main(String[] args) {
        System.out.println("JPA/Hibernate Q&A — entities above illustrate @OneToMany/@ManyToOne + lazy fetch.");
        System.out.println("Run Example* classes in com.harshit.jpa/hibernate packages for live persistence demos.");
    }
}
