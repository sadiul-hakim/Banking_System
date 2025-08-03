# How to Manage Connection in Your Bank App

✅ Recommendation: Use one connection per request/operation, not one global shared connection.
❌ Don’t do this:

```java
// BAD
static Connection conn = DriverManager.getConnection(...); // reused everywhere
```

Why it’s bad:

- Not thread-safe

- Prevents proper transaction boundaries

- Fails on connection timeout/network failure

- Prevents load balancing/pooling in the future

✅ Do this instead:

```java
try(Connection conn = DriverManager.getConnection(...)){
        // Do something (e.g., transfer money, create account)
        // Optionally use conn.setAutoCommit(false) for transactions
        }catch(
SQLException e){
        // Handle
        }

```

# 🏦 Connection Pooling (Optional but Highly Recommended)

Even for a CLI app, consider using HikariCP if you want realistic practice:

```java
HikariDataSource ds = new HikariDataSource();
ds.

setJdbcUrl(...);
try(
Connection conn = ds.getConnection()){
        // ...
        }
```

Great — let’s break this down clearly and practically.

---

# ✅ What is `HikariDataSource`?

`HikariDataSource` is part of **[HikariCP](https://github.com/brettwooldridge/HikariCP)** — a **high-performance JDBC
connection pool**.

Instead of manually managing database connections with `DriverManager`, you let HikariCP manage a **pool of live,
reusable connections** behind the scenes.

---

## 🚀 Why Use HikariCP?

| Without HikariCP                                    | With HikariCP                                   |
|-----------------------------------------------------|-------------------------------------------------|
| You call `DriverManager.getConnection()` every time | Hikari keeps a pool of ready-to-use connections |
| Slow, wasteful                                      | Fast, efficient                                 |
| No `PreparedStatement` cache                        | Built-in prepared statement caching             |
| Risk of connection leaks                            | Auto-closes leaked connections                  |

---

## 🧪 How to Use HikariCP (Step-by-Step)

### 1. 📦 Add HikariCP Dependency

#### If you're using **Maven**:

```xml

<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.1.0</version> <!-- or latest -->
</dependency>
```

#### If you're not using any build tool:

Download JAR from [Maven Central](https://search.maven.org/artifact/com.zaxxer/HikariCP) and add it manually.

---

### 2. ⚙️ Set Up the DataSource (Once at App Start)

```java
import com.zaxxer.hikari.HikariDataSource;

public class DB {
    private static final HikariDataSource ds = new HikariDataSource();

    static {
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/bank");
        ds.setUsername("root");
        ds.setPassword("password");
        ds.setMaximumPoolSize(10);

        // Statement caching
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts", "true");
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection(); // returns from pool
    }

    public static void shutdown() {
        ds.close(); // closes pool on app shutdown
    }
}
```

---

### 3. 🔄 Use it Everywhere Like This:

```java
try(Connection conn = DB.getConnection()){
        conn.

setAutoCommit(false);
    try(
PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?")){
        ps.

setBigDecimal(1,new BigDecimal("100.00"));
        ps.

setInt(2,1);
        ps.

executeUpdate();
        conn.

commit();
    }catch(
Exception ex){
        conn.

rollback();
        throw ex;
    }
            }
```

---

### 4. 🧹 On App Shutdown

Always shut down the pool when your app exits:

```java
DB.shutdown();
```

---

## 🧠 Notes

* **Thread-safe**: Can be used in multi-threaded apps.
* **Fast**: Known for being the fastest JDBC connection pool.
* **Realistic**: Used in real enterprise apps and Spring Boot by default.

---

## ✅ Summary

| Step | What to Do                                        |
|------|---------------------------------------------------|
| 1    | Add HikariCP to your project                      |
| 2    | Create a static `HikariDataSource`                |
| 3    | Use `getConnection()` inside `try-with-resources` |
| 4    | Shut down the pool at the end                     |

---

## 🧠 What is the Observer Pattern?

The **Observer pattern** is a **behavioral design pattern** where:

* **One object (the Subject)** holds some state or triggers events
* **Many objects (Observers)** want to be notified when that state changes

It's like **YouTube notifications**:

* You (**Observer**) subscribe to a channel (**Subject**)
* When the channel posts a new video, all subscribers get notified

---

### 🧱 Real-World Analogy in Your Bank App

You could use the observer pattern to:

* Notify audit loggers or UI components when a transaction happens
* Trigger an alert when a balance goes below a threshold
* Log whenever a new account is created

---

## ✅ Key Components of Observer Pattern

| Component              | Responsibility                               |
|------------------------|----------------------------------------------|
| `Subject` (Observable) | Maintains a list of observers, notifies them |
| `Observer`             | Gets notified when the subject changes       |

---

## ✅ Java Implementation Example

Let’s make a **bank event manager** that notifies listeners (observers) when a transaction occurs.

---

### 1. `Observer` Interface

```java
public interface Observer {
    void update(String event);
}
```

---

### 2. `Subject` (aka Publisher)

```java
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer o) {
        observers.add(o);
    }

    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}
```

---

### 3. Concrete Observers

```java
public class LoggerObserver implements Observer {
    public void update(String event) {
        System.out.println("[Logger] Event: " + event);
    }
}
```

```java
public class EmailNotifierObserver implements Observer {
    public void update(String event) {
        System.out.println("[Email] Notifying user: " + event);
    }
}
```

---

### 4. Example Usage

```java
public class Main {
    public static void main(String[] args) {
        EventManager events = new EventManager();

        // Create and register observers
        events.subscribe(new LoggerObserver());
        events.subscribe(new EmailNotifierObserver());

        // Trigger event
        events.notifyObservers("Money transferred from A to B");

        // Output:
        // [Logger] Event: Money transferred from A to B
        // [Email] Notifying user: Money transferred from A to B
    }
}
```

---

## ✅ When Should You Use Observer?

Use it when:

* You want to decouple business logic from side-effects (e.g., logging, notifying)
* Multiple parts of your app care about an event
* You want loose coupling and extensibility

---

### 🔧 What is this?

```java
Runtime.getRuntime().

addShutdownHook(new Thread(() ->{
        System.out.

println("Shutting down DB...");
    DB.

getInstance().

shutdown();
}));
```

This **registers a JVM shutdown hook**.

---

### 📌 What is a shutdown hook?

A **shutdown hook** is a piece of code (usually a thread) that the JVM runs **just before it exits** — no matter why the
application is shutting down:

* Normal completion (`main()` finishes)
* `System.exit(0)`
* User presses Ctrl+C in terminal
* OS kills the process (gracefully)
* Exceptions (if not caught but JVM exits cleanly)

It gives you a **final chance to clean up resources**, such as:

* Closing DB connections (like your `HikariDataSource`)
* Flushing logs
* Saving data to disk
* Notifying services

---

### 🧠 How does it work?

You're adding a thread to the JVM’s shutdown sequence:

```java
Thread shutdownThread = new Thread(() -> {
    // cleanup code here
});
Runtime.

getRuntime().

addShutdownHook(shutdownThread);
```

Once the JVM is about to shut down, it runs **all registered shutdown hooks**, in no guaranteed order.

---

### ✅ Why use it in your case?

Because you're using HikariCP, which maintains:

* Active DB connections
* Threads in a pool

If you don’t shut it down:

* JVM may hang at exit
* Connections may leak
* Logs might warn about unclosed resources

So you use:

```java
DB.shutdown();
```

To **gracefully close** the pool and release those resources.

---

### ⚠️ Important notes

* Don’t do **long-running work** in a shutdown hook (like waiting on network).
* Avoid throwing exceptions — JVM is exiting anyway.
* The hook won’t run if you force kill (`kill -9`), power off, or if native code crashes the JVM.

---
