package xyz.sadiulhakim.app;

public class StartupLoader {
    private StartupLoader() {
    }

    public static void loadStartupListeners() {
        try {
            Class.forName("xyz.sadiulhakim.db.DBInitializer");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load startup classes", e);
        }
    }
}
