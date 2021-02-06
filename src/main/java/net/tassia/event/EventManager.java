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
public class EventManager implements IEventManager {

	private final Map<Class<?>, List<EventListener<?>>> listeners;

	/**
	 * Creates a new EventManager with no pre-existing listeners.
	 */
	public EventManager() {
		this.listeners = new HashMap<>();
	}



	@Override
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
				registerListenerMethod(method, obj);
			}
		}
	}

	/**
	 * Registers a new listener for the given, {@link EventHandler} annotated method.
	 * @param method the method
	 * @param owner the object to invoke the method on
	 */
	@SuppressWarnings("unchecked")
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



	@Override
	@SuppressWarnings("unchecked")
	public <E extends Event> void registerEvent(Class<E> eventClass) {
		listeners.putIfAbsent(eventClass, new ArrayList<>());
		if (Event.class.isAssignableFrom(eventClass.getSuperclass())) {
			registerEvent((Class<E>) eventClass.getSuperclass());
		}
	}



	@Override
	public <E extends Event> void callEvent(E event) {
		if (event instanceof Cancellable) {
			callCancellableEvent(event, event.getClass(), (Cancellable) event);
		} else {
			callNonCancellableEvent(event, event.getClass());
		}
	}

	@SuppressWarnings("unchecked")
	private <E extends Event> void callCancellableEvent(E event, Class<? extends Event> eventClass, Cancellable cancellable) {
		for (EventListener<?> listener : listeners.get(eventClass)) {
			EventListener<E> cast = (EventListener<E>) listener;
			cast.onEvent(event);
			if (cancellable.isCancelled()) return;
		}
		if (Event.class.isAssignableFrom(eventClass.getSuperclass())) {
			callCancellableEvent(event, (Class<E>) eventClass.getSuperclass(), cancellable);
		}
	}

	@SuppressWarnings("unchecked")
	private <E extends Event> void callNonCancellableEvent(E event, Class<? extends Event> eventClass) {
		for (EventListener<?> listener : listeners.get(eventClass)) {
			EventListener<E> cast = (EventListener<E>) listener;
			cast.onEvent(event);
		}
		if (Event.class.isAssignableFrom(eventClass.getSuperclass())) {
			callNonCancellableEvent(event, (Class<E>) eventClass.getSuperclass());
		}
	}

}
