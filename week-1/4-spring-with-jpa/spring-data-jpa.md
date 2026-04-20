# Spring Data JPA — Shop-It Service Evolved

> **Theme**: Replace manual JPA boilerplate with Spring Data JPA — write interfaces, not implementations.

---

## What Changed from Module 2 (DI + AOP) and Module 3 (Pure JPA)

### The Big Picture

| Aspect | Module 2 (DI + AOP) | Module 3 (Pure JPA) | Module 4 (Spring Data JPA) |
|--------|---------------------|---------------------|---------------------------|
| Data access | `JdbcTemplate` (raw SQL) | `EntityManager` (manual JPA) | `JpaRepository` (zero boilerplate) |
| Repository | Concrete class with SQL | Main methods with `em.persist()` | **Interface only** — Spring generates implementation |
| Transactions | AOP aspect (simulated) | Manual `em.getTransaction()` | `@Transactional` annotation |
| Configuration | `application.properties` | `persistence.xml` (XML) | `application.properties` |
| Entity classes | None (plain SQL) | `@Entity` with Hibernate | `@Entity` with Spring Data JPA |

---

## What We Built

### New Entities

#### `PriceMatrix.java` — Now a JPA Entity

```java
@Entity
@Table(name = "price_matrix")
@Data
public class PriceMatrix {
    @Id
    private String id;

    @Column(name = "product_id", nullable = false, unique = true)
    private int productId;

    private double price;
}
```

**Before (Module 2)**: `PriceMatrix` was just an interface with `getPrice()`
**Now**: It's a real JPA entity mapped to the `price_matrix` table

#### `Order.java` — Persisted to Database

```java
@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;    // PENDING, SHIPPED, DELIVERED, CANCELLED
}
```

**Before (Module 2)**: Orders were just logged, never saved
**Now**: Orders are persisted with status tracking

---

## Spring Data JPA Repositories — Zero Implementation

### `PriceMatrixRepository.java`

```java
public interface PriceMatrixRepository extends JpaRepository<PriceMatrix, Long> {

    @Query("SELECT p.price FROM PriceMatrix p WHERE p.productId = :productId")
    double getPrice(int productId);
}
```

### `OrderRepository.java`

```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    // inherits: save(), findById(), findAll(), delete(), count(), ...
}
```

| What You Write | What Spring Generates |
|---------------|----------------------|
| `extends JpaRepository<Order, Long>` | Full CRUD implementation at runtime |
| `@Query("SELECT ...")` | Custom JPQL query method |
| Method name like `findByStatus(OrderStatus s)` | Query derived from method name (no SQL needed) |

### What `JpaRepository` Gives You for Free

| Method | SQL Equivalent |
|--------|---------------|
| `save(entity)` | `INSERT` or `UPDATE` |
| `findById(id)` | `SELECT * WHERE id = ?` |
| `findAll()` | `SELECT *` |
| `deleteById(id)` | `DELETE WHERE id = ?` |
| `count()` | `SELECT COUNT(*)` |
| `existsById(id)` | `SELECT COUNT(*) > 0` |

---

## `@Transactional` — Declarative Transaction Management

### Before (Module 2): Manual AOP Aspect

```java
// TransactionAspect.java — we wrote this ourselves
@Around("execution(* com.example.service.OnlineOrderService.createOrder(..))")
public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
        logger.info("Transaction started.");
        Object result = joinPoint.proceed();
        logger.info("Transaction committed.");
        return result;
    } catch (Throwable ex) {
        logger.info("Transaction rolled back.");
        throw ex;
    }
}
```

### Now (Module 4): One Annotation

```java
@Transactional(
    rollbackFor = RuntimeException.class,
    noRollbackFor = IllegalArgumentException.class,
    isolation = Isolation.READ_COMMITTED,
    timeout = 30,
    propagation = Propagation.REQUIRED
)
public void createOrder(List<String> cart) { ... }
```

| Attribute | What It Does |
|-----------|-------------|
| `rollbackFor` | Which exceptions trigger rollback |
| `noRollbackFor` | Which exceptions do NOT trigger rollback |
| `isolation` | DB isolation level (`READ_COMMITTED`, `REPEATABLE_READ`, etc.) |
| `timeout` | Max seconds before transaction times out |
| `propagation` | How to handle nested transactions (`REQUIRED` = join existing or create new) |

### How `@Transactional` Works Under the Hood

```
@Transactional
    └── Spring AOP creates a proxy (same pattern as TransactionAspect!)
        └── TransactionManager
            └── DataSource
                └── Connection
                    ├── BEGIN
                    ├── SQL operations...
                    ├── COMMIT (on success)
                    └── ROLLBACK (on exception)
```

> `@Transactional` is exactly what our `TransactionAspect` did manually — Spring just automates it.

---

## The Service Layer — What Changed

### Module 2: Logging-Only Orders

```java
// Module 2 — price lookup + log
double price = priceMatrix.getPrice(item);
totalPrice += price;
logger.info("Order created successfully.");  // nothing saved!
```

### Module 4: Real Persistence

```java
// Module 4 — price lookup + save to DB
double price = priceMatrixRepository.getPrice(Integer.parseInt(item));
amount += price;

Order order = new Order();
order.setAmount(amount);
order.setOrderDate(new Date());
order.setStatus(OrderStatus.PENDING);
orderRepository.save(order);  // INSERT INTO orders
```

---

## Configuration: Spring Boot + JPA

### `application.properties`

```properties
# DataSource (same as Module 2)
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword

# JPA-specific
spring.jpa.hibernate.ddl-auto=update     # auto-create/update tables
spring.jpa.show-sql=true                  # print SQL to console
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

| Property | Purpose |
|----------|---------|
| `ddl-auto=update` | Hibernate creates/updates tables from `@Entity` classes |
| `show-sql=true` | See the generated SQL (useful for debugging) |
| `hibernate.dialect` | Tells Hibernate which SQL dialect to generate |

### Dependency Change

```xml
<!-- Module 2: JDBC -->
<artifactId>spring-boot-starter-data-jdbc</artifactId>

<!-- Module 4: JPA (includes JDBC + Hibernate + Spring Data JPA) -->
<artifactId>spring-boot-starter-data-jpa</artifactId>
```

---

## Key Spring Data JPA Concepts

| Concept | What It Does |
|---------|-------------|
| `JpaRepository<T, ID>` | Interface that gives you full CRUD for entity `T` with primary key type `ID` |
| `@Query` | Custom JPQL or native SQL on a repository method |
| `@Entity` + `@Table` | Maps a Java class to a database table |
| `@GeneratedValue` | Auto-generates primary key values |
| `@Transactional` | Declarative transaction management |
| `@EntityScan` | Tells Spring where to find `@Entity` classes |
| `spring.jpa.hibernate.ddl-auto` | Controls schema generation (`none`, `validate`, `update`, `create`, `create-drop`) |

---

## Ask the Participants

1. What happens if you remove `@Transactional` from `createOrder()`? Does the order still get saved?
2. In Module 3 we wrote `em.persist()` + `em.getTransaction().commit()`. How many lines does `orderRepository.save()` replace?
3. What SQL does `spring.jpa.show-sql=true` show when `createOrder()` runs?
4. What's the difference between `ddl-auto=update` and `ddl-auto=create`? Which is safe for production?
5. `PriceMatrixRepository` uses `@Query`. Could you write `findByProductId(int productId)` instead? What would Spring generate?

---

## Evolution Summary: Module 2 → Module 3 → Module 4

```
Module 2 (DI + AOP)          Module 3 (Pure JPA)         Module 4 (Spring Data JPA)
─────────────────────         ────────────────────         ─────────────────────────
JdbcTemplate + raw SQL        EntityManager + JPQL         JpaRepository interface
Manual aspect for txn         Manual em.getTransaction()   @Transactional annotation
No entities                   @Entity + persistence.xml    @Entity + application.properties
PriceMatrix = interface       Customer/Order entities      PriceMatrix + Order entities
Orders only logged            Insert/Select examples       Orders saved with repository
```

---

## Pre-requisite: PostgreSQL + Schema

```bash
docker run --name postgres-container -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
docker exec -it postgres-container psql -U postgres
```

```sql
-- price_matrix table (same as Module 2)
CREATE TABLE IF NOT EXISTS price_matrix (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO price_matrix (product_id, price) VALUES (1, 19.99), (2, 29.99), (3, 39.99);

-- orders table is auto-created by Hibernate (ddl-auto=update)
```

## How to Run

```bash
cd 4-spring-with-jpa/shop-it
mvn spring-boot:run
```
