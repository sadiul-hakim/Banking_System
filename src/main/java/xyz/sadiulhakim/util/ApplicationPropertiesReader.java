package xyz.sadiulhakim.util;

import xyz.sadiulhakim.pojo.ConnectionDetails;

import java.util.Properties;

public class ApplicationPropertiesReader {

    private final static Properties properties = new Properties();
    public static final String DATABASE_URL = "database.url";
    public static final String DATABASE_USERNAME = "database.username";
    public static final String DATABASE_PASSWORD = "database.password";
    public static final String DATABASE_INIT_SCHEMA = "database.init.schema";
    private ConnectionDetails connectionDetails;

    private ApplicationPropertiesReader() {
        try (var stream = ApplicationPropertiesReader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (stream == null) {
                System.out.println("Unable to find system properties!");
                return;
            }

            properties.load(stream);

            String url = properties.getProperty(DATABASE_URL, "");
            String username = properties.getProperty(DATABASE_USERNAME, "");
            String password = properties.getProperty(DATABASE_PASSWORD, "");
            connectionDetails = new ConnectionDetails(url, username,
                    password);
        } catch (Exception ex) {
            System.out.println("Unable to find system properties!");
        }
    }

    public static ApplicationPropertiesReader getInstance() {
        return new ApplicationPropertiesReader();
    }

    public String getPropertyValue(String property) {
        return properties.getProperty(property, "");
    }

    public <T> T getPropertyValueOfType(String property, Class<T> type) {
        String value = properties.getProperty(property);
        if (value == null) return null;

        try {
            if (type == String.class) {
                return type.cast(value);
            } else if (type == Integer.class) {
                return type.cast(Integer.valueOf(value));
            } else if (type == Boolean.class) {
                return type.cast(Boolean.valueOf(value));
            } else if (type == Long.class) {
                return type.cast(Long.valueOf(value));
            } else if (type == Double.class) {
                return type.cast(Double.valueOf(value));
            } else if (type == Float.class) {
                return type.cast(Float.valueOf(value));
            } else if (type == Short.class) {
                return type.cast(Short.valueOf(value));
            } else if (type == Byte.class) {
                return type.cast(Byte.valueOf(value));
            }
        } catch (Exception e) {
            // Log or print error if needed
            return null;
        }

        return null; // Unsupported type
    }


    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }
}
