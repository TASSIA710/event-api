package net.tassia.event;

import org.junit.Test;

public class EventHandlerTest {

	private boolean test = false;

	@Test
	public void test() {
		EventManager manager = new EventManager();
		manager.registerEvent(TestEvent.class);

		manager.registerListeners(this);

		manager.callEvent(new TestEvent());

		assert test;
	}

	@EventHandler
	public void onEvent(TestEvent event) {
		test = true;
	}



	private static class TestEvent extends Event {
	}

}
