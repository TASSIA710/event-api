package net.tassia.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
	 * Registers all listeners (public methods annotated with {@link EventHandler}) of the given object.
	 * @param obj the object
	 */
	public void registerListeners(Object obj) {
		for (Method method : obj.getClass().getMethods()) {
			if (method.isAnnotationPresent(EventHandler.class)) {
				System.out.println("Registering " + method.toGenericString());
				registerListenerMethod(method, obj);
			}
		}
	}

	/**
	 * Registers a new listener for the given, {@link EventHandler} annotated method.
	 * @param method the method
	 * @param owner the object to invoke the method on
	 */
	private void registerListenerMethod(Method method, Object owner) {
		// Method must be public for us to call it
		if (!Modifier.isPublic(method.getModifiers())) {
			throw new IllegalArgumentException(method.toGenericString() + " is not public.");
		}

		// Must have one argument of type Event (or sub-class of Event)
		if (method.getParameterCount() != 1) {
			throw new IllegalArgumentException(method.toGenericString() + " must have 1 parameter (" + method.getParameterCount() + " present)");
		}
		Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];
		if (!Event.class.isAssignableFrom(eventClass)) {
			throw new IllegalArgumentException(method.toGenericString() + " should have 1 parameter of type net.tassia.Event, but "
					+ method.getParameterTypes()[0].toGenericString() + " is present.");
		}

		// TODO: Use MethodHandles maybe?

		// Create EventListener
		EventListener<? extends Event> listener = (event) -> {
			try {
				method.invoke(owner, event);
			} catch (IllegalAccessException | InvocationTargetException ex) {
				throw new RuntimeException("Invoking EventHandler failed.", ex);
			}
		};

		// Register EventListener
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
