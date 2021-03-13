package net.tassia.event

/**
 * A event listener is used to listen for a specific event, or events extending the specified event.
 *
 * @since EventAPI 1.0
 * @author Tassilo
 */
fun interface EventListener<T : Event> {

	/**
	 * Invoked when the given event is called.
	 *
	 * @param event the event
	 */
	fun onEvent(event: T)

}
