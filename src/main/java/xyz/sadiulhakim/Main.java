package xyz.sadiulhakim;

import xyz.sadiulhakim.app.StartupLoader;
import xyz.sadiulhakim.db.DB;
import xyz.sadiulhakim.event.Event;
import xyz.sadiulhakim.event.EventManager;
import xyz.sadiulhakim.event.EventType;

public class Main {
    public static void main(String[] args) {

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing HikariCP pool and data sources......");
            DB.getInstance().shutdown();
        }));

        System.out.println("Wanna open a bank account!");
        StartupLoader.loadStartupListeners();
        EventManager.notify(new Event(EventType.APPLICATION_READY, "Application is Ready"));
    }
}
