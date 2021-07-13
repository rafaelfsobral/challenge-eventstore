package net.intelie.challenges;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Rafael Sobral
 */

public class InMemoryEventIterator implements EventIterator {

	// eventType is a data structure contains all events
	private final Map<String, List<Event>> eventType;
	// eventList contains a list of events as a result of the query.
	private final List<Event> eventList;
	// Event type
	private final String type;
	// index of iterator
	private int index;

	/*
	 * Constructor
	 */
	public InMemoryEventIterator(Map<String, List<Event>> state, String type, List<Event> eventList) {
		this.eventType = state;
		this.type = type;
		this.eventList = eventList;
		index = -1;
	}

	@Override
	public boolean moveNext() {
		// increment the index variable
		index++;
		// Move the iterator to the next event, if any
		return eventList.size() > index;
	}

	@Override
	public Event current() {
		// Check if the index is less than 0
		if (index < 0) {
			throw new IllegalStateException();
		}

		// Check if the size of the eventList variable is less than or equal to the
		// index
		if (eventList.size() <= index) {
			throw new IllegalStateException();
		}

		// Gets the current event referenced by this iterator.
		return eventList.get(index);
	}

	@Override
	public void remove() {
		// Check if the index < 0
		if (index < 0) {
			throw new IllegalStateException();
		}
		Event event = eventList.get(index);
		// Check if the current event contains in the data structure
		if (eventType.containsKey(type) && eventType.get(type).contains(event)) {
			// Remove current event from its store.
			eventType.get(type).remove(event);
		}
	}

	@Override
	public void close() throws Exception {
	}

}
