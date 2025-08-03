package xyz.sadiulhakim.event;

public record Event(
        EventType type,
        String message
) {
}
