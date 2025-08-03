package xyz.sadiulhakim.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private static final List<Observer> observers = new CopyOnWriteArrayList<>();

    private EventManager() {
    }

    public static void subscribe(Observer observer) {
        observers.add(observer);
    }

    public static void unSubscribe(Observer observer) {
        observers.remove(observer);
    }

    public static void notify(Event event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }
}
