<!--suppress ALL -->
<h1 align="center">Event API</h1>
<p align="center">Simple event handling API for Java.</p>

<p align="center">
	<img src="https://img.shields.io/github/license/TASSIA710/event-api?label=License" alt="LICENSE">
	<img src="https://img.shields.io/github/workflow/status/TASSIA710/event-api/Java%20CI?label=Java%20CI">
	<img src="https://img.shields.io/github/v/release/TASSIA710/event-api?label=Stable">
	<img src="https://img.shields.io/github/v/release/TASSIA710/event-api?label=Preview&include_prereleases">
</p>



## Table of Contents

1. [Using the Event API](#using-the-event-api)
	1. [Registering event listeners](#registering-event-listeners)
	2. [Calling events](#calling-events)
	3. [Custom events](#custom-events)
2. [Installation](#installation)
3. [License](#license)



## Using the Event API

### Registering event listeners

Create the listener:
```kotlin
import net.tassia.event.Event
import net.tassia.event.EventListener

class YourEventListener : EventListener<Event> {

	@Override
	fun onEvent(event: Event) {
		// Do something :)
	}

}
```

Register the listener:
```kotlin
import net.tassia.event.EventManager

// ...

val manager = EventManager.newDefault()
manager.registerListener(Event::class, YourEventListener())
```



### Calling events

Call an event:
```kotlin
import net.tassia.event.EventManager

// ...

val manager = EventManager.newDefault()
manager.callEvent(Event())
```



### Custom events

Create a custom, cancellable event:
```kotlin
import net.tassia.event.Cancellable
import net.tassia.event.Event

data class SendStringEvent(

	var value: String

) : Event, Cancellable {

	override var isCancelled = false

}
```

Call your event:
```kotlin
// Call the event
val event = SendStringEvent("Hello World!")
eventManager.callEvent(event)

// Do your stuff with respect to the event
if (!event.isCancelled()) {
	YourSendStringMethod(event.value)
}
```



## Installation

1. Add the repository:
```xml
<repository>
    <id>tassia-nexus</id>
    <url>https://nexus.tassia.net/repository/maven-public/</url>
</repository>
```

2. Add dependency:
```xml
<dependency>
    <groupId>net.tassia</groupId>
    <artifactId>event-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

3. Install dependencies
```
$ mvn install
```



## License

This project is licensed under the [MIT License](https://github.com/TASSIA710/event-api/blob/main/LICENSE).
