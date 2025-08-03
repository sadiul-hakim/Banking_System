package xyz.sadiulhakim.db;

import xyz.sadiulhakim.event.Event;
import xyz.sadiulhakim.event.EventManager;
import xyz.sadiulhakim.event.Observer;
import xyz.sadiulhakim.util.ApplicationPropertiesReader;

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
        System.out.println("From DB_Initializer" + "=>" + event.toString());
        DB db = DB.getInstance();
        ApplicationPropertiesReader reader = ApplicationPropertiesReader.getInstance();
        try (Connection connection = db.getConnection()) {
            Boolean init = reader.getPropertyValueOfType(ApplicationPropertiesReader.DATABASE_INIT_SCHEMA, Boolean.class);
            System.out.println(init);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
