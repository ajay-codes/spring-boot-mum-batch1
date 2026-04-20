# V1 - Plain Java (Tight Coupling)

> **Theme**: Build a working transfer service — then identify what's wrong with it.

---

## What We Built

### Domain Model — `Account.java`
- Simple POJO with fields: `number`, `holderName`, `balance`
- Constructor-based initialization with getters/setters and `toString()`

### Repository Layer — `JdbcAccountRepository.java`
- **Concrete class** (no interface)
- Methods: `findByNumber()`, `update()`
- Returns hardcoded dummy data (no real DB yet)
- Logs every instance creation — observe this carefully in console output

### Service Layer — `ImpsTransferService.java`
- **Concrete class** (no interface)
- Transfer logic in 4 steps:
  1. Load both accounts from repository
  2. Validate (account exists? sufficient balance?)
  3. Debit source, credit destination
  4. Update both accounts
- **Key line (line 26)**: `new JdbcAccountRepository()` — creates a fresh instance on every `transfer()` call

### Application Entry Point — `Application.java`
- Three lifecycle phases: **Init** -> **Execute** -> **Shutdown**
- Calls `transfer()` twice — watch the logs to see **two** `JdbcAccountRepository` instances created

### Dependencies
- Only `slf4j` for logging — **zero framework dependencies**

---

## Ask the Participants

After running, ask them to observe the logs and identify:

### Design Problem
- `ImpsTransferService` is **hardcoded** to `JdbcAccountRepository`
- What if we need `JpaAccountRepository` or `MongoAccountRepository`?
- We must **modify** `ImpsTransferService` — violates Open/Closed Principle
- Can we unit test `ImpsTransferService` without a real database? **No** — can't mock it

### Performance Problem
- Every `transfer()` call creates a **new** `JdbcAccountRepository` instance
- In production: 1000 transfers/sec = 1000 repository objects created and discarded
- CPU + memory wasted, GC pressure under load

### Root Cause (One Line)
> The **dependent** (`ImpsTransferService`) is managing the **lifecycle** of its **dependency** (`JdbcAccountRepository`).

---

## How to Run

```bash
cd transfer-service-v1
mvn clean compile exec:java "-Dexec.mainClass=com.example.Application"
```
