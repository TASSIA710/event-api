# Event API

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
```java
import net.tassia.event.Event;
import net.tassia.event.EventListener;

public class YourEventListener implements EventListener<Event> {

	@Override
	public void onEvent(Event event) {
		// Do something :)
	}

}
```

Register the listener:
```java
import net.tassia.event.EventManager;

// ...

EventManager manager = new EventManager();
manager.registerListener(Event.class, new YourEventListener());
```



### Calling events

Call an event object:
```java
import net.tassia.event.EventManager;

// ...

EventManager manager = new EventManager();
manager.callEvent(new Event());
```



### Custom events

Create a custom, cancellable event:
```java
import net.tassia.event.Cancellable;
import net.tassia.event.Event;

public class SendStringEvent extends Event implements Cancellable {

	private boolean cancelled;
	private String value;

	public SendStringEvent(String value) {
		this.cancelled = false;
		this.value = value;
	}

	public void setValue() {
		return value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
```

Call your event:
```java
// Call the event
SendStringEvent event = new SendStringEvent("Hello World!");
eventManager.callEvent(event);

// Do your stuff with respect to the event
if (!event.isCancelled()) {
	YourSendStringMethod(event.getValue());
}
```



## Installation

1. Add dependency:
```xml
<dependency>
  <groupId>net.tassia</groupId>
  <artifactId>event-api</artifactId>
  <version>1.0.0-R0.1</version>
</dependency>
```

2. Install dependencies
```
$ mvn install
```



## License

This project is licensed under the [MIT License](https://github.com/TASSIA710/event-api/blob/main/LICENSE).
