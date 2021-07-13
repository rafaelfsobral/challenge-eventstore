package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Rafael Sobral
 */
public class InMemoryEventStore implements EventStore {

	// I used interface map composed of key(String) and value (event list)
	private Map<String, List<Event>> state;

	/*
	 * Constructor
	 */
	public InMemoryEventStore() {
		// ConcurrentHashMap is a thread-safe class
		state = new ConcurrentHashMap<>();
	}

	@Override
	public synchronized void insert(Event event) {
		// Check if the event is null
		if (Objects.isNull(event)) {
			throw new NullPointerException("event cannot be null");
		}

		// Check if the event type is null
		if (Objects.isNull(event.type())) {
			throw new NullPointerException("event.type cannot be null");
		}

		// Check if the map contains the event type
		if (state.containsKey(event.type())) {
			state.get(event.type()).add(event);
		} else {
			// Vector is a Collection and is thread-safe
			List<Event> eventList = new Vector<>();
			eventList.add(event);
			state.put(event.type(), eventList);
		}

	}

	@Override
	public synchronized void removeAll(String type) {
		// Check if the specific type of event is null
		if (Objects.isNull(type)) {
			throw new NullPointerException("type cannot be null");
		}
		// Removes all events of specific type.
		state.remove(type);

	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		// Check if the specific type of event is null
		if (Objects.isNull(type)) {
			throw new NullPointerException("type cannot be null");
		}

		// The eventList contains the list of events of a specific type
		List<Event> eventList = state.get(type);
		List<Event> indexList = new ArrayList<>();

		// Check if the variable eventList is null
		if (eventList == null) {
			return new InMemoryEventIterator(state, type, indexList);
		}

		// The indexList contains the list of events based on their type and timestamp
		for (int i = 0; i < eventList.size(); i++) {
			Event event = eventList.get(i);

			if (event.timestamp() >= startTime && event.timestamp() < endTime) {
				indexList.add(event);
			}
		}

		return new InMemoryEventIterator(state, type, indexList);
	}

}
