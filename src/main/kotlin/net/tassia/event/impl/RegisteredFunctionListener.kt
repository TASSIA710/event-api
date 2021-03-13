package net.tassia.event.impl

import net.tassia.event.Event
import net.tassia.event.EventListener

class RegisteredFunctionListener<E : Event>(

	val listener: EventListener<E>

) : RegisteredListener<E>() {

	override fun dispatch(event: E) {
		listener.onEvent(event)
	}

}
