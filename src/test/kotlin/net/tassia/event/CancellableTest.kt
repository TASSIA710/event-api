package net.tassia.event

import kotlin.test.Test

class CancellableTest {

	private var count = 0

	@Test
	fun test() {
		val manager = EventManager.newDefault()
		manager.registerEvent(TestEvent::class)

		manager.registerListener(TestEvent::class, TestListener())
		manager.registerListener(TestEvent::class, this::testListener)

		manager.callEvent(TestEvent())

		assert(count == 1)
	}



	private fun testListener(event: TestEvent) {
		count++
		event.isCancelled = true
	}



	private inner class TestListener : EventListener<TestEvent> {
		override fun onEvent(event: TestEvent) {
			count++
			event.isCancelled = true
		}
	}

	private class TestEvent : Event(), Cancellable {
		override var isCancelled = false
	}

}
