package net.tassia.event.impl

import net.tassia.event.Event
import net.tassia.event.EventListener
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

class RegisteredReflectiveListener(

	val method: Method,
	val owner: Any

) : RegisteredListener<Event>() {

	val eventClass: Class<Event>

	init {
		// Method must be public for us to call it
		require(Modifier.isPublic(method.modifiers)) { method.toGenericString() + " is not public." }

		// Must have one argument of type Event (or sub-class of Event)
		require(method.parameterCount == 1) { "${method.toGenericString()} must have 1 parameter (${method.parameterCount} present)" }
		this.eventClass = method.parameterTypes[0] as Class<Event>
		require(Event::class.java.isAssignableFrom(eventClass)) {
			"${method.toGenericString()} should have 1 parameter of type net.tassia.Event, but ${method.parameterTypes[0].toGenericString()} is present."
		}
	}

	override fun dispatch(event: Event) {
		method.invoke(owner, event)
	}

}
