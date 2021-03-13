package net.tassia.event

import kotlin.test.Test

class EventHandlerTestFailure {

	@Test
	fun test() {
		val manager = EventManager.newDefault()

		manager.registerListeners(TestSuccess1())
		manager.registerListeners(TestSuccess2())

		try {
			manager.registerListeners(TestFailure1())
			throw AssertionError("TestFailure1 failed.")
		} catch (ignored: IllegalArgumentException) {
			// This is desired behavior
		}

		try {
			manager.registerListeners(TestFailure2())
			throw AssertionError("TestFailure2 failed.")
		} catch (ignored: IllegalArgumentException) {
			// This is desired behavior
		}
	}



	private class TestSuccess1 {
		@EventHandler
		fun onEvent(event: Event) {
		}
	}

	private class TestSuccess2 {
		@EventHandler
		fun onEvent(event: Event): Float {
			return 3.14f
		}
	}

	private class TestFailure1 {
		@EventHandler
		fun onEvent(event: Event, str: String) {
		}
	}

	private class TestFailure2 {
		@EventHandler
		fun onEvent(str: String) {
		}
	}

}
