# V2 - Applying SOLID Principles (Interfaces + Factory + Constructor Injection)

> **Theme**: Fix design issues using core Java — no frameworks yet.

---

## What Changed from V1

### 1. Introduced Interfaces (Abstraction)
- **`AccountRepository`** interface — `findByNumber()`, `update()`
- **`TransferService`** interface — `transfer()`
- Now we **program to interfaces**, not concrete classes

### 2. Multiple Implementations
- `JdbcAccountRepository implements AccountRepository` — JDBC-based persistence
- `JpaAccountRepository implements AccountRepository` — JPA-based persistence (new)
- Both are interchangeable — service doesn't know or care which one it gets

### 3. Factory Pattern — `AccountRepositoryFactory`
- Static factory method: `createAccountRepository("jdbc")` or `createAccountRepository("jpa")`
- Centralizes object creation logic
- Service no longer creates its own dependency

### 4. Constructor Injection (Manual)
- `ImpsTransferService` now receives `AccountRepository` **via constructor**
- Repository instance is **created once** and **reused** across all transfer calls
- Fixes the performance problem from V1

### 5. Updated Application Entry Point
- `Application.main()` wires everything:
  ```java
  AccountRepository jdbcAccountRepository = AccountRepositoryFactory.createAccountRepository("jdbc");
  TransferService transferService = new ImpsTransferService(jdbcAccountRepository);
  ```
- Variables typed as **interfaces**, not implementations

---

## SOLID Principles Demonstrated

| Principle | How It's Applied |
|-----------|-----------------|
| **S** - Single Responsibility | Service does transfers, factory does creation |
| **O** - Open/Closed | Add `MongoAccountRepository` without touching service |
| **L** - Liskov Substitution | Swap `JdbcAccountRepository` with `JpaAccountRepository` — works fine |
| **I** - Interface Segregation | Focused interfaces: `AccountRepository`, `TransferService` |
| **D** - Dependency Inversion | Service depends on `AccountRepository` interface, not `JdbcAccountRepository` |

---

## What's Still Not Great?

- `Application.main()` has **manual wiring code** — who creates and wires in a real app?
- Factory still uses `new` internally — object creation is still our responsibility
- No lifecycle management (singleton? prototype? cleanup?)
- As app grows, `main()` becomes a **wiring nightmare**

> **Next step**: Let a **framework** handle object creation and wiring — that's Spring IoC.

---

## How to Run

```bash
cd transfer-service-v2
mvn clean compile exec:java "-Dexec.mainClass=com.example.Application"
```
