package net.tassia.event

import net.tassia.event.impl.EventManagerImpl
import kotlin.reflect.KClass

/**
 * EventManagers should implement this interface.
 *
 * @since EventAPI 1.0
 * @author Tassilo
 */
abstract class EventManager {

	/**
	 * Registers a new event listener for the given event class.
	 *
	 * @param listener the event listener
	 */
	inline fun <reified E : Event> registerListener(listener: EventListener<E>) = registerListener(E::class, listener)

	/**
	 * Registers a new event listener for the given event class.
	 *
	 * @param eventClass the event class
	 * @param listener the event listener
	 */
	abstract fun <E : Event> registerListener(eventClass: KClass<E>, listener: EventListener<E>)

	/**
	 * Registers all listeners (functions annotated with [EventHandler]) in the given object.
	 *
	 * @param listener the listener
	 */
	abstract fun registerListeners(listener: Any)

	/**
	 * Registers a new event and prepares it for calling and accepting listeners.
	 */
	inline fun <reified E : Event> registerEvent() = registerEvent(E::class)

	/**
	 * Registers a new event and prepares it for calling and accepting listeners.
	 *
	 * @param eventClass the event class
	 */
	abstract fun <E : Event> registerEvent(eventClass: KClass<E>)

	/**
	 * Calls the event and propagates it to all listeners.
	 *
	 * @param event the event
	 */
	abstract fun <E : Event> callEvent(event: E)



	companion object {

		/**
		 * Creates a new default event manager.
		 *
		 * @return the event manager
		 */
		fun newDefault(): EventManager = EventManagerImpl()

	}

}
