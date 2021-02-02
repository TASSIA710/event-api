package net.tassia.event;

import org.junit.Test;

public class EventHandlerTestFailure {

	@Test
	public void test() {
		EventManager manager = new EventManager();

		manager.registerListeners(new TestSuccess1());

		manager.registerListeners(new TestSuccess2());

		try {
			manager.registerListeners(new TestFailure1());
			// We should have encountered an exception here
			throw new AssertionError("TestFailure1 failed.");
		} catch (IllegalArgumentException ignored) {
			// This is desired behavior
		}

		try {
			manager.registerListeners(new TestFailure2());
			// We should have encountered an exception here
			throw new AssertionError("TestFailure2 failed.");
		} catch (IllegalArgumentException ignored) {
			// This is desired behavior
		}
	}



	private static class TestSuccess1 {
		@EventHandler
		public void onEvent(Event event) {
		}
	}

	private static class TestSuccess2 {
		@EventHandler
		public float onEvent(Event event) {
			return 3.14F;
		}
	}

	private static class TestFailure1 {
		@EventHandler
		public void onEvent(Event event, String str) {
		}
	}

	private static class TestFailure2 {
		@EventHandler
		public void onEvent(String str) {
		}
	}

}
