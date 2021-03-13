package net.tassia.event

/**
 * A cancellable event can be cancelled. The unique feature this offers, is that this breaks
 * propagation to event listeners as soon as one listener cancels this event.
 *
 * @see Event
 *
 * @since EventAPI 1.0
 * @author Tassilo
 */
interface Cancellable {

	/**
	 * Whether this event is currently cancelled.
	 */
	var isCancelled: Boolean

}
