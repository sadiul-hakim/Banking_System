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

