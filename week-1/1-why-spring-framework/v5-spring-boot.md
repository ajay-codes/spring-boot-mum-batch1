# V5 - Spring Boot (Auto-Configuration)

> **Theme**: Convention over configuration — Spring Boot configures what you'd otherwise write by hand.

---

## What Changed from V4

### 1. `SpringApplication.run()` Replaces Manual Context
```java
// V4 — manual bootstrap
context = new AnnotationConfigApplicationContext(Application.class);

// V5 — Spring Boot bootstrap
context = SpringApplication.run(Application.class, args);
```
- `@EnableAutoConfiguration` tells Spring Boot: "look at classpath and auto-configure beans"

### 2. Deleted All Infrastructure Configuration Classes
| Deleted Class | Spring Boot Replaces It With |
|---------------|------------------------------|
| `DatasourceConfiguration` | Auto-configures `HikariDataSource` from `application.properties` |
| `JdbcTemplateConfiguration` | Auto-configures `JdbcTemplate` (detected `spring-jdbc` on classpath) |
| `DataSourceTransactionManager` bean | Auto-configured because DataSource + `@EnableTransactionManagement` |

**Zero `@Bean` methods for infrastructure. Zero config classes.**

### 3. `application.properties` — Externalized Configuration
```properties
debug=true    # shows auto-configuration report — what Boot configured and why

spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mysecretpassword
```
- Spring Boot reads these automatically — no `@Value` annotations needed
- `debug=true` — **very useful for teaching** — shows exactly what Boot auto-configured

### 4. Simplified `pom.xml`

**Before (V4) — 6 explicit dependencies with versions:**
```xml
spring-context 6.2.17
spring-jdbc 6.2.17
HikariCP 5.1.0
postgresql 42.5.4
slf4j-api 1.7.30
slf4j-simple 1.7.30
```

**After (V5) — 2 dependencies, no version numbers:**
```xml
spring-boot-starter-data-jdbc  <!-- bundles: spring-jdbc, HikariCP, logging, etc. -->
postgresql                      <!-- version managed by spring-boot-starter-parent -->
```

- `spring-boot-starter-parent` manages all versions — no conflicts, no guesswork

### 5. Same Stereotype Annotations from V3
- `@Service` on `ImpsTransferService`, `@Repository` on `JdbcAccountRepository` — unchanged
- Spring Boot doesn't change how you annotate your beans — it only auto-configures **infrastructure**

---

## What Spring Boot Auto-Configured (run with `debug=true`)

| Bean | How Boot Decided |
|------|-----------------|
| `HikariDataSource` | Found `spring.datasource.*` properties + JDBC driver on classpath |
| `JdbcTemplate` | Found `spring-jdbc` on classpath + DataSource bean exists |
| `DataSourceTransactionManager` | Found DataSource + `@EnableTransactionManagement` |
| Logback logging | Included in `spring-boot-starter` — replaces `slf4j-simple` |

---

## Compare: V1 vs V5

| Aspect | V1 (Plain Java) | V5 (Spring Boot) |
|--------|-----------------|-------------------|
| Object creation | `new` everywhere | Auto-discovered via `@Service`, `@Repository` |
| Wiring | Manual in `main()` | Constructor injection, auto-wired |
| Lifecycle | Our responsibility | Spring manages (singleton, shutdown hooks) |
| JDBC | Not connected | `JdbcTemplate` — clean, 1-line queries |
| Transactions | None | `@Transactional` — declarative |
| Configuration | Hardcoded | `application.properties` |
| Dependencies | Manual version management | Starter BOMs, version-managed |
| Lines of infra code | N/A | **Zero** config classes |

---

## Key Spring Boot Concepts

| Concept | What It Does |
|---------|-------------|
| `@EnableAutoConfiguration` | Scans classpath and auto-configures beans with sensible defaults |
| `spring-boot-starter-*` | Curated dependency bundles — one starter = all you need |
| `spring-boot-starter-parent` | Parent POM managing all dependency versions |
| `application.properties` | Externalized config — no code changes for different environments |
| `SpringApplication.run()` | Bootstraps everything — context, beans, embedded server (if web) |
| `debug=true` | Shows the auto-configuration report — what was configured and what was skipped |

---

## How to Run

```bash
cd transfer-service-v5
mvn spring-boot:run
```
