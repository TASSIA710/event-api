package net.tassia.event;

/**
 * EventManagers should implement this interface.
 * @since EventAPI 1.0
 * @author Tassilo
 */
public interface IEventManager {

	/**
	 * Registers a new event listener for the given event class.
	 * @param eventClass the event class
	 * @param listener the event listener
	 * @param <E> the event
	 */
	<E extends Event> void registerListener(Class<E> eventClass, EventListener<E> listener);

	/**
	 * Registers a new event and prepares it for calling and accepting listeners.
	 * @param eventClass the event class
	 * @param <E> the event
	 */
	<E extends Event> void registerEvent(Class<E> eventClass);

	/**
	 * Calls the event and propagates it to all listeners.
	 * @param event the event
	 * @param <E> the event
	 */
	<E extends Event> void callEvent(E event);

}
