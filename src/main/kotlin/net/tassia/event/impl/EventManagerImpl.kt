package net.tassia.event.impl

import net.tassia.event.Event
import net.tassia.event.EventHandler
import net.tassia.event.EventListener
import net.tassia.event.EventManager
import kotlin.reflect.KClass

/**
 * The default implementation of [EventManager].
 *
 * @since EventAPI 1.0
 * @author Tassilo
 */
class EventManagerImpl : EventManager {

	/**
	 * The internal map that holds all registered events.
	 */
	private val eventRegister: MutableMap<KClass<out Event>, RegisteredEvent<out Event>> = mutableMapOf()



	override fun <E : Event> registerListener(eventClass: KClass<E>, listener: EventListener<E>) {
		getRegisteredEvent(eventClass).listeners.add(RegisteredFunctionListener(listener))
	}

	override fun registerListeners(listener: Any) {
		for (function in listener::class.java.methods) {
			// Annotation?
			if (!function.isAnnotationPresent(EventHandler::class.java)) continue

			// Create
			val rrl = RegisteredReflectiveListener(method = function, owner = listener)
			getRegisteredEvent(rrl.eventClass.kotlin).listeners.add(rrl)
		}
	}

	override fun <E : Event> registerEvent(eventClass: KClass<E>) {
		getRegisteredEvent(eventClass)
	}

	override fun <E : Event> callEvent(event: E) {
		getRegisteredEvent(event.javaClass.kotlin).dispatch(event)
	}



	@Suppress("UNCHECKED_CAST")
	fun <E : Event> getRegisteredEvent(eventClass: KClass<E>): RegisteredEvent<E> {
		// Registered event exist, so return it
		eventRegister[eventClass]?.let { return it as RegisteredEvent<E> }

		// Registered event does not exist, so create it
		val registered = RegisteredEvent<E>()
		eventRegister[eventClass] = registered
		return registered
	}

}
