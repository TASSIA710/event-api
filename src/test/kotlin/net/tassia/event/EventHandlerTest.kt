package net.tassia.event

import kotlin.test.Test

class EventHandlerTest {

	class TestEvent : Event()

	private var test1 = false

	@Test
	fun test() {
		val manager = EventManager.newDefault()
		manager.registerEvent(TestEvent::class)

		manager.registerListeners(this)

		manager.callEvent(TestEvent())

		assert(test1)
	}

	@EventHandler
	fun onEvent(event: TestEvent) {
		test1 = true
	}

}
