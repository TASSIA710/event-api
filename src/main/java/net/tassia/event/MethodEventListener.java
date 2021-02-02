package net.tassia.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An MethodEventListener is an {@link EventListener} that instead of directly calling an interface,
 * invokes a given method.
 * @since EventAPI 1.0
 * @author Tassilo
 */
class MethodEventListener implements EventListener<Event> {

	private final Object owner;
	private final Method method;

	/**
	 * Creates a new MethodEventListener with the given owner and method. The owner is the object to call the method
	 * on and should be non-null for non-static methods (and null for statics).
	 * @param owner the owner
	 * @param method the method
	 */
	protected MethodEventListener(Object owner, Method method) {
		// Check annotation
		if (!method.isAnnotationPresent(EventHandler.class)) {
			throw new IllegalArgumentException(method.toGenericString() + " is not annotated with EventHandler.");
		}

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

		// Static?
		if (Modifier.isStatic(method.getModifiers()) && owner != null) {
			throw new IllegalArgumentException(method.toGenericString() + " is static and thus shouldn't have an owner.");
		}
		if (!Modifier.isStatic(method.getModifiers()) && owner == null) {
			throw new IllegalArgumentException(method.toGenericString() + " is not static and thus needs an owner.");
		}

		// Store into fields
		this.owner = owner;
		this.method = method;
	}

	@Override
	public void onEvent(Event event) {
		try {
			method.invoke(owner, event);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new RuntimeException("Invoking EventHandler failed.", ex);
		}
	}

}
