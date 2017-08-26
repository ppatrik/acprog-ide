package net.acprog.ide.utils.event;

public interface Observer {
    void onEvent(EventType eventType, Object o);
}