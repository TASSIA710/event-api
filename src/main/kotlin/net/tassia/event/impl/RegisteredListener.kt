package net.tassia.event.impl

import net.tassia.event.Event

abstract class RegisteredListener<E : Event> {

	abstract fun dispatch(event: E)

}
