package net.tassia.event;

/**
 * A cancellable event can be cancelled. The unique feature this offers, is that this breaks
 * propagation to event listeners as soon as one listener cancels this event.
 * @since EventAPI 1.0
 * @author Tassilo
 * @see Event
 */
public interface Cancellable {

	/**
	 * Returns whether the event is currently cancelled.
	 * @return is cancelled
	 */
	boolean isCancelled();

	/**
	 * Sets whether the event should be cancelled.
	 * @param cancelled should be cancelled
	 */
	void setCancelled(boolean cancelled);

}
