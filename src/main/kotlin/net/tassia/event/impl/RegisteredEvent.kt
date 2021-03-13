package net.tassia.event.impl

import net.tassia.event.Cancellable
import net.tassia.event.Event

class RegisteredEvent<E : Event> {

	val listeners: MutableSet<RegisteredListener<E>> = mutableSetOf()

	fun dispatch(event: E) {
		if (event is Cancellable) {
			for (listener in listeners) {
				if (event.isCancelled) break
				listener.dispatch(event)
			}
		} else {
			listeners.forEach { it.dispatch(event) }
		}
	}

}
