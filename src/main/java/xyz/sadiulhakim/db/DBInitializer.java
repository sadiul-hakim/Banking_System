package xyz.sadiulhakim.db;

import xyz.sadiulhakim.event.Event;
import xyz.sadiulhakim.event.EventManager;
import xyz.sadiulhakim.event.Observer;
import xyz.sadiulhakim.util.ApplicationPropertiesReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

class DBInitializer implements Observer {

    private DBInitializer() {
    }

    static {
        EventManager.subscribe(new DBInitializer());
    }

    @Override
    public void update(Event event) {
        ApplicationPropertiesReader reader = ApplicationPropertiesReader.getInstance();
        Boolean init = reader.getPropertyValueOfType(ApplicationPropertiesReader.DATABASE_INIT_SCHEMA, Boolean.class);
        if (!init) {
            return;
        }

        DB db = DB.getInstance();
        try (Connection connection = db.getConnection()) {

            try (var stream = ApplicationPropertiesReader.class.getClassLoader().getResourceAsStream("schema.sql")) {
                if (stream == null) {
                    System.out.println("Could not read db schemas!");
                    return;
                }

                try (var bufferedReader = new BufferedReader(new InputStreamReader(stream));
                     var statement = connection.createStatement()) {

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {

                        // Skip comments and empty lines
                        String trimmed = line.trim();
                        if (trimmed.isEmpty() || trimmed.startsWith("--") || trimmed.startsWith("//") || trimmed.startsWith("/*")) {
                            continue;
                        }

                        sb.append(line).append("\n");
                        if (trimmed.endsWith(";")) {
                            String sql = sb.toString().trim();
                            sql = sql.substring(0, sql.lastIndexOf(';')); // remove last semicolon

                            try {
                                statement.execute(sql);
                            } catch (SQLException e) {
                                System.err.println("Error executing SQL: " + sql);
                                throw e;
                            }

                            sb.setLength(0); // reset buffer
                        }
                    }
                }

            } catch (Exception ex) {
                System.out.println("Unable to read schema file!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
