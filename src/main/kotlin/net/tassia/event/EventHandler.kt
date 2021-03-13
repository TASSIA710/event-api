package net.tassia.event

/**
 * Annotate a method with this annotation to register it as an event listener.
 *
 * @since EventAPI 1.0
 * @author Tassilo
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class EventHandler
