# Proxy Pattern — Why AOP Exists

> **Theme**: Understand the manual proxy pattern, then see what Spring AOP automates for you.

---

## What We Built

### The Real Service — `TrainingService`

```java
class TrainingService implements Training {
    public void getSpringBootTraining() {
        System.out.println(">>>>Spring Boot Training");
    }
    public void getSqlTraining() {
        System.out.println(">>>>SQL Training");
    }
}
```

- Pure business logic — no awareness of logging or auth

### The Proxy — `TrainingServiceProxy`

```java
class TrainingServiceProxy implements Training {
    TrainingService trainingService = new TrainingService();
    Logger logger = new Logger();
    Auth auth = new Auth();

    public void getSpringBootTraining() {
        auth.isAuthenticated();                          // cross-cutting: security
        logger.logInfo("Spring Boot Training accessed"); // cross-cutting: logging
        trainingService.getSpringBootTraining();          // delegate to real service
    }
}
```

- Wraps the real service
- Adds **cross-cutting concerns** (auth, logging) **before** delegating to the actual method
- Implements the **same interface** as the real service — callers don't know the difference

### Client Code

```java
Training training = new TrainingServiceProxy();  // client uses the interface
training.getSpringBootTraining();
```

---

## The Problem with Manual Proxies

| Problem | Impact |
|---------|--------|
| Every method needs proxy code | If `Training` has 20 methods, you write 20 proxy methods |
| Cross-cutting logic is duplicated | Auth + logging repeated in every method |
| Adding a new concern (e.g., metrics) | Must modify every proxy method |
| Proxy class per service | 10 services = 10 proxy classes with identical patterns |

---

## How Spring AOP Solves This

| Manual Proxy | Spring AOP |
|-------------|------------|
| Write a proxy class per service | Write **one** `@Aspect` class |
| Repeat auth/logging in every method | Define a **pointcut** — Spring applies it everywhere |
| Proxy implements same interface manually | Spring **generates** the proxy at runtime (JDK dynamic proxy or CGLIB) |
| Hard to add/remove concerns | Enable/disable by adding/removing `@Component` |

---

## Key Takeaway

> The Proxy Pattern is what AOP does **manually**. Spring AOP automates proxy creation using **pointcut expressions** + **advice methods** — so you write the cross-cutting logic once and Spring weaves it in.

---

## How to Run

```bash
cd 2-spring-di-and-aop/shop-it
mvn compile exec:java "-Dexec.mainClass=com.example.ProxyPatternExample"
```
