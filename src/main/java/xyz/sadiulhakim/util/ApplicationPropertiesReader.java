package xyz.sadiulhakim.util;

import xyz.sadiulhakim.pojo.ConnectionDetails;

import java.util.Properties;

public class ApplicationPropertiesReader {

    private final static Properties properties = new Properties();
    public static final String DATABASE_URL = "database.url";
    public static final String DATABASE_USERNAME = "database.username";
    public static final String DATABASE_PASSWORD = "database.password";
    private static final ConnectionDetails connectionDetails;

    static {
        connectionDetails = new ConnectionDetails(getPropertyValue(DATABASE_URL), getPropertyValue(DATABASE_USERNAME),
                getPropertyValue(DATABASE_PASSWORD));
    }

    private ApplicationPropertiesReader() {
        try (var stream = ApplicationPropertiesReader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (stream == null) {
                System.out.println("Unable to find system properties!");
                return;
            }

            properties.load(stream);
        } catch (Exception ex) {
            System.out.println("Unable to find system properties!");
        }
    }

    public static String getPropertyValue(String property) {
        return properties.getProperty(property, "");
    }

    public static ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }
}
