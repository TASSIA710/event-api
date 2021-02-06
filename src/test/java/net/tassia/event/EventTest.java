package net.tassia.event;

import org.junit.Test;

public class EventTest {

	private boolean test = false, test2 = false;

	@Test
	public void test() {
		EventManager manager = new EventManager();
		manager.registerEvent(TestEvent.class);

		manager.registerListener(TestEvent.class, new TestListener());
		manager.registerListener(TestEvent.class, this::testListener);

		manager.callEvent(new TestEvent());

		assert test;
		assert test2;
	}

	private void testListener(TestEvent event) {
		test2 = true;
	}



	private static class TestEvent extends Event {
	}

	private class TestListener implements EventListener<TestEvent> {
		@Override
		public void onEvent(TestEvent event) {
			test = true;
		}
	}

}
