# Spring DI + AOP — Shop-It Service

> **Theme**: Build a real Spring Boot service that demonstrates Dependency Injection, Bean Lifecycle, and Aspect-Oriented Programming.

---

## What We Built

A `shop-it-service` that creates orders by looking up prices from a database. The focus is on **how Spring wires things together** and **how AOP intercepts method calls**.

### Architecture

```
ShopItApplication (bootstrap)
    └── OnlineOrderService (@Service)
            ├── PriceMatrix (interface)
            │     ├── SqlDatabasePriceMatrixRepository (@Repository) ← active
            │     └── ExcelPriceMatrixRepository (@Repository)       ← alternative
            └── JdbcTemplate (auto-configured by Spring Boot)

Aspects (interceptors):
    ├── CountMetricAspect (@Order 1) — counts method executions
    └── TransactionAspect (@Order 2) — simulates transaction management
```

---

## Dependency Injection (DI)

### 1. Interface-Based Design

```java
public interface PriceMatrix {
    double getPrice(String productId);
}
```

Two implementations exist — Spring needs to know **which one** to inject.

### 2. Constructor Injection + `@Qualifier`

```java
@Service("onlineOrderService")
public class OnlineOrderService implements OrderService {

    private PriceMatrix priceMatrix;

    @Autowired
    public OnlineOrderService(@Qualifier("sqlDatabasePriceMatrix") PriceMatrix priceMatrix) {
        this.priceMatrix = priceMatrix;
    }
}
```

| Concept | What It Does |
|---------|-------------|
| `@Service` | Marks as service-layer bean (auto-discovered by `@ComponentScan`) |
| `@Autowired` | Tells Spring to inject the dependency |
| `@Qualifier` | Resolves ambiguity when multiple implementations exist |
| Constructor injection | Preferred — makes dependencies explicit, supports immutability |

### 3. `@Repository` Stereotype

```java
@Repository("sqlDatabasePriceMatrix")
public class SqlDatabasePriceMatrixRepository implements PriceMatrix {

    private final JdbcTemplate jdbcTemplate;

    public SqlDatabasePriceMatrixRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getPrice(String productId) {
        String sql = "SELECT price FROM price_matrix WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getDouble("price"),
                Integer.parseInt(productId));
    }
}
```

- `JdbcTemplate` is **auto-configured** by Spring Boot (detected `spring-boot-starter-data-jdbc` on classpath)
- No manual `DataSource` or `JdbcTemplate` bean needed — **convention over configuration**

---

## Bean Lifecycle

### Application Lifecycle — `ShopItApplication.java`

```java
// 1. INIT / BOOT PHASE
context = SpringApplication.run(ShopItApplication.class, args);

// 2. RUN PHASE
OrderService service = context.getBean(OnlineOrderService.class);
service.createOrder(List.of("1", "2", "3"));

// 3. SHUTDOWN PHASE
context.close();
```

### Bean Lifecycle Hooks

```java
@PostConstruct
public void init() {
    logger.info("OnlineOrderService init method called.");
}

@PreDestroy
public void cleanup() {
    logger.info("OnlineOrderService cleanup method called.");
}
```

| Hook | When It Runs |
|------|-------------|
| Constructor | Bean created |
| `@PostConstruct` | After dependency injection, before bean is used |
| `@PreDestroy` | During `context.close()`, before bean is destroyed |

### Advanced: BeanPostProcessor (BPP) & BeanFactoryPostProcessor (BFPP)

These are **disabled** (no `@Component`) to reduce noise, but exist in the code for discussion:

| Class | When It Runs | Use Case |
|-------|-------------|----------|
| `BFPP` | Before any beans are created — modifies **bean definitions** | Change scope, add properties |
| `BPP` | After each bean is created — wraps or modifies **bean instances** | AOP proxies, validation |

---

## Aspect-Oriented Programming (AOP)

### What is AOP?

Cross-cutting concerns (logging, transactions, metrics) that repeat across many methods. Instead of adding this code to every method, you write it **once** in an `@Aspect`.

### Aspect 1: Metrics Counter (`@Order(1)` — runs first)

```java
@Component
@Aspect
@Order(1)
public class CountMetricAspect {

    private Map<String, Integer> methodExecutionCount = new ConcurrentHashMap<>();

    @Before("execution(* com.example.service.*.*(..))")
    public void countMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        methodExecutionCount.merge(methodName, 1, Integer::sum);
        logger.info("Execution count for {}: {}", methodName, methodExecutionCount.get(methodName));
    }
}
```

| AOP Term | In This Code |
|----------|-------------|
| **Aspect** | `CountMetricAspect` — the class containing advice |
| **Advice** | `@Before` — when to run (before the method) |
| **Pointcut** | `execution(* com.example.service.*.*(..))` — which methods to intercept |
| **JoinPoint** | The intercepted method (gives access to method name, args) |

### Aspect 2: Transaction Simulation (`@Order(2)` — runs second)

```java
@Around("execution(* com.example.service.OnlineOrderService.createOrder(..))")
public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
        logger.info("Transaction started.");
        Object result = joinPoint.proceed();       // call the actual method
        logger.info("Transaction committed.");
        return result;
    } catch (Throwable ex) {
        logger.info("Transaction rolled back.");
        throw ex;
    } finally {
        logger.info("Transaction resources closed.");
    }
}
```

### Advice Types

| Advice | When It Runs | Use Case |
|--------|-------------|----------|
| `@Before` | Before the method | Logging, auth checks, metrics |
| `@AfterReturning` | After successful return | Audit logging |
| `@AfterThrowing` | After an exception | Error logging, alerts |
| `@After` | Always (like `finally`) | Cleanup |
| `@Around` | Wraps the method entirely | Transactions, timing, caching |

### Execution Flow

```
createOrder(cart) called
  │
  ├── CountMetricAspect (@Order 1) — @Before: count++
  │
  └── TransactionAspect (@Order 2) — @Around:
        ├── "Transaction started"
        ├── proceed() → OnlineOrderService.createOrder() runs
        ├── "Transaction committed" (on success)
        └── "Transaction resources closed" (always)
```

---

## DataSource Configuration — Why It's Commented Out

```java
// @Configuration   ← disabled: Spring Boot auto-configures HikariDataSource
public class DataSourceConfiguration { ... }
```

Spring Boot reads `application.properties` and auto-creates:
- `HikariDataSource` (from `spring.datasource.*` properties)
- `JdbcTemplate` (detected `spring-boot-starter-data-jdbc` on classpath)

**This class exists to show what Spring Boot does for you** — uncomment `@Configuration` to take manual control.

---

## Key Spring Boot Concepts

| Concept | What It Does |
|---------|-------------|
| `@EnableAutoConfiguration` | Auto-configures beans based on classpath + properties |
| `@ComponentScan` | Discovers `@Service`, `@Repository`, `@Component`, `@Aspect` |
| `@EnableAspectJAutoProxy` | Enables AOP proxy generation |
| `@Qualifier` | Selects specific bean when multiple implementations exist |
| `@PostConstruct` / `@PreDestroy` | Bean lifecycle callbacks |
| `@Aspect` + `@Around` / `@Before` | AOP — intercept methods declaratively |

---

## Ask the Participants

1. What happens if you remove `@Qualifier("sqlDatabasePriceMatrix")` from the constructor? (Hint: `NoUniqueBeanDefinitionException`)
2. What if you change `@Order(1)` and `@Order(2)` — which aspect runs first?
3. What happens during `context.close()` — which lifecycle methods are called?
4. Why is `@Around` more powerful than `@Before` + `@AfterReturning`?

---

## Pre-requisite: PostgreSQL + Schema

```bash
docker run --name postgres-container -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
docker exec -it postgres-container psql -U postgres
```

```sql
CREATE TABLE IF NOT EXISTS price_matrix (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO price_matrix (product_id, price) VALUES (1, 19.99), (2, 29.99), (3, 39.99);
```

## How to Run

```bash
cd 2-spring-di-and-aop/shop-it
mvn spring-boot:run
```
