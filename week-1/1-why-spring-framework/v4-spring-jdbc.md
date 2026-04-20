# V4 - Spring JDBC + Transactions

> **Theme**: Eliminate JDBC boilerplate and add transaction safety.

---

## What Changed from V3

### 1. Spring `JdbcTemplate` (No More Raw JDBC)

**Before (V3) — 25 lines of boilerplate per query:**
```java
Connection connection = null;
try {
    connection = dataSource.getConnection();
    PreparedStatement ps = connection.prepareStatement(sql);
    ps.setString(1, number);
    ResultSet rs = ps.executeQuery();
    rs.next();
    // map result...
} catch (Exception e) {
    // handle...
} finally {
    if (connection != null) connection.close();
}
```

**After (V4) — 1 line:**
```java
jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Account(...), number);
```

- `JdbcTemplateConfiguration` creates `JdbcTemplate` bean wrapping the `DataSource`
- `JdbcAccountRepository` now depends on `JdbcTemplate` instead of raw `DataSource`
- `JdbcTemplate` handles: connection acquire/release, exception translation, parameter binding

### 2. Declarative Transaction Management
- `@EnableTransactionManagement` on `Application` class
- `DataSourceTransactionManager` bean added in `DatasourceConfiguration`
- `@Transactional` on `ImpsTransferService.transfer()` method
- **Demo**: toggle `simulateFailure = true` to throw exception between debit and credit:
  - **Without** `@Transactional`: debit sticks, credit lost — data inconsistency!
  - **With** `@Transactional`: both operations roll back — data stays consistent

### 3. Updated Application Configuration
- Added `@EnableTransactionManagement`
- `@Import({DatasourceConfiguration.class, JdbcTemplateConfiguration.class})` — infrastructure beans
- Added `spring-jdbc` dependency to `pom.xml`

---

## Key Spring Concepts

| Concept | What It Does |
|---------|-------------|
| `JdbcTemplate` | Eliminates JDBC boilerplate — handles connections, exceptions, result mapping |
| `@Transactional` | Declarative transaction boundaries — auto commit/rollback |
| `@EnableTransactionManagement` | Enables Spring's annotation-driven transaction support (AOP) |
| `DataSourceTransactionManager` | Manages transactions over a JDBC DataSource |

---

## What Spring Does for Us (vs V3)
- **Eliminates** JDBC boilerplate (`JdbcTemplate`) — compare `JdbcAccountRepository` in V3 vs V4
- **Manages** transactions declaratively (`@Transactional`) — no manual commit/rollback
- Service code is now **pure business logic** — no infrastructure concerns

---

## What's Still Not Great?

- Still manually configuring `DataSource`, `JdbcTemplate`, `TransactionManager` via `@Bean`
- Still managing dependency versions in `pom.xml` (Spring 6.2.17, HikariCP 5.1.0, etc.)
- Still bootstrapping with `new AnnotationConfigApplicationContext()`

> **Question for participants**: What if Spring could auto-configure all of this based on what's on the classpath?

---

## How to Run

```bash
cd transfer-service-v4
mvn clean compile exec:java "-Dexec.mainClass=com.example.Application"
```
