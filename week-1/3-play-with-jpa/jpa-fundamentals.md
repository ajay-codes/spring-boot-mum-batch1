# JPA Fundamentals — Pure Hibernate (No Spring)

> **Theme**: Understand JPA from scratch — entities, relationships, and persistence — before Spring hides it all.

---

## Why This Module?

Before using Spring Data JPA, you need to understand what's happening underneath:
- What is an `EntityManager`?
- How does `persistence.xml` configure JPA?
- How do entity relationships (`@OneToMany`, `@ManyToOne`) work?
- What does `hibernate.hbm2ddl.auto=update` actually do?

This module uses **raw JPA + Hibernate** — no Spring, no auto-configuration.

---

## What We Built

### Domain Model

```
Customer (1) ──── (*) Order          @OneToMany / @ManyToOne
    │
    └── (*) Address                  @ElementCollection (embeddable)
```

### Entity: `Customer.java`

```java
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private int id;

    @Column(name = "customer_name")
    private String name;

    @Enumerated(EnumType.ORDINAL)       // stored as 0, 1, 2
    private Gender gender;

    @Temporal(TemporalType.DATE)        // only date, no time
    private Date birthDate;

    @Lob                                // large text
    private String profile;

    @Lob                                // binary data (e.g., image)
    private byte[] profilePicture;

    @ElementCollection                  // collection of embeddable objects
    @CollectionTable(name = "customer_addresses",
        joinColumns = @JoinColumn(name = "customer_id"))
    private List<Address> addressList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orderList;
}
```

| Annotation | Purpose |
|-----------|---------|
| `@Entity` | Marks class as a JPA entity (maps to a DB table) |
| `@Table` | Specifies the table name |
| `@Id` | Primary key |
| `@Column` | Maps field to column (custom name) |
| `@Enumerated` | How to store enums — `ORDINAL` (int) or `STRING` |
| `@Temporal` | Date precision — `DATE`, `TIME`, or `TIMESTAMP` |
| `@Lob` | Large object — text (`CLOB`) or binary (`BLOB`) |
| `@ElementCollection` | Collection of embeddable/basic types (separate table) |
| `@OneToMany` | One customer has many orders |
| `cascade = ALL` | Persist/delete orders when customer is persisted/deleted |

### Embeddable: `Address.java`

```java
@Embeddable
@Data
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
}
```

- `@Embeddable` — not an entity, no `@Id`, stored as columns in the parent or a collection table
- Used with `@ElementCollection` in `Customer`

### Entity: `Order.java`

```java
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
```

| Annotation | Purpose |
|-----------|---------|
| `@ManyToOne` | Many orders belong to one customer |
| `FetchType.LAZY` | Don't load customer until accessed (performance) |
| `@JoinColumn` | Foreign key column in the `orders` table |

---

## JPA Bootstrap — How It Works

```
persistence.xml  →  EntityManagerFactory  →  EntityManager  →  Transaction
```

### `persistence.xml`

```xml
<persistence-unit name="myPU">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <class>com.example.Customer</class>
    <class>com.example.Order</class>
    <properties>
        <!-- DB Connection -->
        <property name="jakarta.persistence.jdbc.url"
                  value="jdbc:postgresql://localhost:5432/postgres"/>
        <!-- Hibernate auto-creates/updates tables -->
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        <!-- Debug: show generated SQL -->
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
    </properties>
</persistence-unit>
```

---

## Example 1: Insert a Customer

```java
Customer customer = new Customer();
customer.setId(1);
customer.setName("A");
customer.setGender(Gender.MALE);
customer.setAddressList(List.of(
    new Address("123 Main St", "Anytown", "Anystate", "12345"),
    new Address("456 Elm St", "Othertown", "Otherstate", "67890")
));

EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
em.persist(customer);              // INSERT into customers + customer_addresses
em.getTransaction().commit();
em.close();
emf.close();
```

**What Hibernate generates:**
1. `INSERT INTO customers (customer_id, customer_name, ...) VALUES (1, 'A', ...)`
2. `INSERT INTO customer_addresses (customer_id, street, city, ...) VALUES (1, '123 Main St', ...)`
3. `INSERT INTO customer_addresses (customer_id, street, city, ...) VALUES (1, '456 Elm St', ...)`

---

## Example 2: Insert an Order (with ManyToOne)

```java
Order order = new Order();
order.setOrderId("ORD123");
order.setAmount(1000.00);

Customer customer = new Customer();
customer.setId(1);                  // reference existing customer
order.setCustomer(customer);

em.persist(order);                  // INSERT into orders with customer_id = 1
```

- We don't load the full customer — just set the ID as a reference

---

## Example 3: Select and Navigate Relationships

```java
// Find by primary key
Customer customer = em.find(Customer.class, 1);

// Navigate to embedded collection
customer.getAddressList().forEach(System.out::println);

// Navigate to related orders (LAZY — SQL fires here)
customer.getOrderList().forEach(System.out::println);
```

### Three Ways to Query in JPA

| Method | Description | Example |
|--------|------------|---------|
| `em.find()` | By primary key | `em.find(Customer.class, 1)` |
| JPQL | Object-oriented SQL | `SELECT c FROM Customer c WHERE c.name = :name` |
| Criteria API | Type-safe, programmatic queries | Used for dynamic queries |

---

## What Hibernate Creates (DDL)

With `hibernate.hbm2ddl.auto=update`, Hibernate auto-generates:

```sql
CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(255),
    customer_gender INT,           -- ORDINAL: 0=MALE, 1=FEMALE, 2=OTHER
    customer_birth_date DATE,
    customer_profile TEXT,
    customer_profile_picture BYTEA
);

CREATE TABLE customer_addresses (
    customer_id INT REFERENCES customers(customer_id),
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255)
);

CREATE TABLE orders (
    order_id VARCHAR(255) PRIMARY KEY,
    amount DOUBLE PRECISION,
    customer_id INT REFERENCES customers(customer_id)
);
```

---

## Ask the Participants

1. What happens if you try `em.persist(order)` without calling `em.getTransaction().begin()` first?
2. What's the difference between `FetchType.LAZY` and `FetchType.EAGER`? When does the SQL fire?
3. What happens if you use `@Enumerated(EnumType.STRING)` instead of `ORDINAL`? Which is safer for production?
4. Why do we call `emf.close()`? What happens if we don't?

---

## What's Painful Here?

- **Boilerplate everywhere**: `EntityManagerFactory` → `EntityManager` → `begin()` → `persist()` → `commit()` → `close()`
- **Manual transaction management**: forget `commit()` and nothing is saved
- **`persistence.xml`**: XML configuration instead of properties
- **No repository pattern**: SQL/JPA calls are scattered in `main()` methods

> **Next step**: Spring Data JPA eliminates all of this (see `4-spring-with-jpa`)

---

## Pre-requisite: PostgreSQL via Docker

```bash
docker run --name postgres-container -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
```

## How to Run

```bash
cd 3-play-with-jpa

# Insert a customer
mvn compile exec:java "-Dexec.mainClass=com.example.InsertCustomerExample"

# Query the customer
mvn compile exec:java "-Dexec.mainClass=com.example.SelectCustomerExample"

# Insert an order
mvn compile exec:java "-Dexec.mainClass=com.example.InsertOrderExample"

# Query order with navigation
mvn compile exec:java "-Dexec.mainClass=com.example.SelectOrderExample"
```
