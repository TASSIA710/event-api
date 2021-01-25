package net.tassia.event;

/**
 * An EventListener listens for an event.
 * @param <E> the event to listen for
 * @since EventAPI 1.0
 * @author Tassilo
 */
@FunctionalInterface
public interface EventListener<E extends Event> {

	/**
	 * Invoked when the event is called.
	 * @param event the event
	 */
	void onEvent(E event);

}
