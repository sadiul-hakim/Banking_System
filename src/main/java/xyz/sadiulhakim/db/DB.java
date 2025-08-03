package xyz.sadiulhakim.db;

import com.zaxxer.hikari.HikariDataSource;
import xyz.sadiulhakim.pojo.ConnectionDetails;
import xyz.sadiulhakim.util.ApplicationPropertiesReader;

import java.sql.Connection;
import java.sql.SQLException;

public class DB {
    private DB() {
    }

    private static final HikariDataSource DS = new HikariDataSource();

    static {
        ConnectionDetails connectionDetails = ApplicationPropertiesReader.getConnectionDetails();
        DS.setJdbcUrl(connectionDetails.url());
        DS.setUsername(connectionDetails.username());
        DS.setPassword(connectionDetails.password());
        DS.setMaximumPoolSize(10);

        // Optional performance tuning
        DS.addDataSourceProperty("cachePrepStmts", "true");
        DS.addDataSourceProperty("prepStmtCacheSize", "250");
        DS.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DS.addDataSourceProperty("useServerPrepStmts", "true"); // Enables server-side statement caching
    }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    public static void shutdown() {
        DS.close();
    }
}
