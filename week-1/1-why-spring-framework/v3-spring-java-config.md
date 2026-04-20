# V3 - Spring Framework (IoC + Component Scanning + Stereotypes)

> **Theme**: Let Spring create, discover, and wire objects — replace factory with IoC container.

---

## What Changed from V2

### 1. Spring IoC Container Replaces Manual Wiring
- `Application.main()` now bootstraps Spring:
  ```java
  ConfigurableApplicationContext context =
      new AnnotationConfigApplicationContext(Application.class);
  ```
- Beans retrieved from container: `context.getBean("impsTransferService", TransferService.class)`
- Proper shutdown: `context.close()`

### 2. Stereotype Annotations + Component Scanning
- `Application` is now `@Configuration` + `@ComponentScan(basePackages = "com.example")`
- Spring **auto-discovers** annotated classes on the classpath:
  - `@Service` on `ImpsTransferService` — marks it as a service-layer bean
  - `@Repository` on `JdbcAccountRepository` — marks it as a data-access bean
- **No `@Bean` methods needed** for application components — Spring finds them automatically
- `@Import(DatasourceConfiguration.class)` — only **infrastructure** beans (DataSource) use explicit `@Bean`

### 3. DataSource Configuration — `DatasourceConfiguration.java`
- `@Configuration` class with `@Bean` for HikariCP DataSource — this is infrastructure, not app logic
- `@Value` for externalized properties with sensible defaults:
  - `${spring.datasource.url:jdbc:postgresql://localhost:5432/postgres}`
  - `${spring.datasource.username:postgres}`
  - `${spring.datasource.password:mysecretpassword}`
  - `${spring.datasource.maximumPoolSize:15}`

### 4. Real Database Queries
- `JdbcAccountRepository` now uses **raw JDBC** (`Connection`, `PreparedStatement`, `ResultSet`)
- Accepts `DataSource` via constructor (injected by Spring)
- Manual connection management with try/finally blocks
- Real SQL: `SELECT * FROM accounts WHERE number = ?`

### 5. Factory Removed
- `AccountRepositoryFactory` deleted — Spring is the factory now
- `JpaAccountRepository` removed (not needed for this demo)
- `TransferServiceConfiguration` not needed — `@ComponentScan` discovers beans automatically

---

## Key Spring Concepts

| Concept | What It Does |
|---------|-------------|
| `@Configuration` | Marks class as a bean definition source |
| `@ComponentScan` | Auto-discovers `@Service`, `@Repository`, `@Component` on classpath |
| `@Service` | Stereotype for service-layer beans (specialization of `@Component`) |
| `@Repository` | Stereotype for data-access beans (specialization of `@Component`) |
| `@Bean` | Explicitly declares a bean — used here only for infrastructure (`DataSource`) |
| `@Import` | Composes multiple configuration classes |
| `@Value("${key:default}")` | Injects property values with fallback defaults |
| `AnnotationConfigApplicationContext` | Bootstraps the Spring IoC container |
| **Singleton scope** | Default — one instance per bean (fixes V1 performance issue for free) |

---

## When to Use `@Bean` vs `@Component`/`@Service`/`@Repository`?

| Use `@Bean` when... | Use `@Service`/`@Repository` when... |
|---------------------|--------------------------------------|
| Configuring **infrastructure** (DataSource, pools) | Defining **your application** components |
| You don't own the class (third-party library) | You own the class and can annotate it |
| Need custom initialization logic | Constructor injection is sufficient |

---

## What Spring Does for Us (vs V2)
- **Discovers** beans automatically (`@ComponentScan` + stereotypes)
- **Creates** objects and **wires** dependencies (passes `DataSource` to repo, passes repo to service)
- **Manages lifecycle** (singleton by default, proper shutdown)
- No more factory class, no more manual wiring in `main()`

---

## What's Still Painful?

- Raw JDBC in `JdbcAccountRepository` — try/finally, manual connection close (look at how much code!)
- No transaction management — what if debit succeeds but credit fails?

---

## Pre-requisite: PostgreSQL via Docker

```bash
docker run --name postgres-container -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
docker exec -it postgres-container psql -U postgres

CREATE TABLE accounts (
    number VARCHAR(20) PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);
INSERT INTO accounts VALUES ('1234567890', 'John Doe', 1000.00);
INSERT INTO accounts VALUES ('0987654321', 'Jane Smith', 2000.00);
```

## How to Run

```bash
cd transfer-service-v3
mvn clean compile exec:java "-Dexec.mainClass=com.example.Application"
```
