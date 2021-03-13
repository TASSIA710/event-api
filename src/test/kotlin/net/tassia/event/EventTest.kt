package net.tassia.event

import kotlin.test.Test

class EventTest {

	private class TestEvent : Event()

	private var test1 = false
	private var test2 = false

	@Test
	fun test() {
		val manager = EventManager.newDefault()
		manager.registerEvent(TestEvent::class)

		manager.registerListener(TestEvent::class, TestListener())
		manager.registerListener(TestEvent::class, this::testListener)

		manager.callEvent(TestEvent())

		assert(test1)
		assert(test2)
	}



	private fun testListener(event: TestEvent) {
		test2 = true
	}

	private inner class TestListener : EventListener<TestEvent> {
		override fun onEvent(event: TestEvent) {
			test1 = true
		}
	}

}
