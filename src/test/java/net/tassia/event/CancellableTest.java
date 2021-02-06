package net.tassia.event;

import org.junit.Test;

public class CancellableTest {

	private int count = 0;

	@Test
	public void test() {
		EventManager manager = new EventManager();
		manager.registerEvent(TestEvent.class);

		manager.registerListener(TestEvent.class, new TestListener());
		manager.registerListener(TestEvent.class, this::testListener);

		manager.callEvent(new TestEvent());

		assert count == 1;
	}

	private void testListener(TestEvent event) {
		count++;
		event.setCancelled(true);
	}



	private static class TestEvent extends Event implements Cancellable {
		private boolean cancelled = false;
		@Override
		public boolean isCancelled() {
			return cancelled;
		}
		@Override
		public void setCancelled(boolean cancelled) {
			this.cancelled = cancelled;
		}
	}

	private class TestListener implements EventListener<TestEvent> {
		@Override
		public void onEvent(TestEvent event) {
			count++;
			event.setCancelled(true);
		}
	}

}
