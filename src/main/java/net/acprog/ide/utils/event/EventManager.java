package net.acprog.ide.utils.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager {

    private final Map<EventType, ObserverManager> m_observerManagers = new ConcurrentHashMap<>();

    public void registerObserver(EventType eventType, Observer observer) {
        synchronized (m_observerManagers) {
            if (!m_observerManagers.containsKey(eventType)) {
                m_observerManagers.put(eventType, new ObserverManager());
            }

            ObserverManager observerManager = m_observerManagers.get(eventType);
            observerManager.add(observer);
        }
    }

    public void unregisterObserver(EventType eventType, Observer observer) {
        synchronized (m_observerManagers) {
            if (!m_observerManagers.containsKey(eventType)) {
                return;
            }

            ObserverManager observerManager = m_observerManagers.get(eventType);
            observerManager.remove(observer);
        }
    }

    public void callEvent(EventType eventType) {
        callEvent(eventType, null);
    }

    public void callEvent(EventType eventType, Object o) {
        if (!m_observerManagers.containsKey(eventType)) {
            return;
        }

        ObserverManager observerManager = m_observerManagers.get(eventType);
        observerManager.callEvent(eventType, o);
    }
}