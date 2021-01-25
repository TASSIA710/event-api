package net.tassia.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manages registering listeners and calling events.
 * @since EventAPI 1.0
 * @author Tassilo
 */
public class EventManager {

	private final Map<Class<?>, List<EventListener<?>>> listeners;

	/**
	 * Creates a new EventManager with no pre-existing listeners.
	 */
	public EventManager() {
		this.listeners = new HashMap<>();
	}



	/**
	 * Registers a new event listener for the given event class.
	 * @param eventClass the event class
	 * @param listener the event listener
	 * @param <E> the event
	 */
	public <E extends Event> void registerListener(Class<E> eventClass, EventListener<E> listener) {
		List<EventListener<?>> list = listeners.getOrDefault(eventClass, new ArrayList<>());
		list.add(listener);
		listeners.put(eventClass, list);
	}



	/**
	 * Calls the event and propagates it to all listeners.
	 * @param event the event
	 * @param <E> the event
	 */
	@SuppressWarnings("unchecked")
	public <E extends Event> void callEvent(E event) {
		if (event instanceof Cancellable) {
			callCancellableEvent(event);
			return;
		}
		List<EventListener<?>> list = listeners.get(event.getClass());
		if (list == null) return;
		for (EventListener<?> listener : list) {
			EventListener<E> cast = (EventListener<E>) listener;
			cast.onEvent(event);
		}
	}

	/**
	 * Calls the event and propagates it to all listeners, while checking that it has not been cancelled.
	 * @param event the event, MUST implement {@link Cancellable}
	 * @param <E> the event
	 * @throws ClassCastException if the event does not implement {@link Cancellable}
	 */
	@SuppressWarnings("unchecked")
	private <E extends Event> void callCancellableEvent(E event) {
		Cancellable cancellable = (Cancellable) event;
		List<EventListener<?>> list = listeners.get(event.getClass());
		if (list == null) return;
		for (EventListener<?> listener : list) {
			EventListener<E> cast = (EventListener<E>) listener;
			cast.onEvent(event);
			if (cancellable.isCancelled()) break;
		}
	}

}
