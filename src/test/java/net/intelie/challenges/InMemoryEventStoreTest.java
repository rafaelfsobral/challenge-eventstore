package net.intelie.challenges;

import static org.junit.Assert.assertFalse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import net.intelie.challenges.Event;
import net.intelie.challenges.EventStore;
import net.intelie.challenges.InMemoryEventStore;

/**
 *
 * @author Rafael Sobral
 */

public class InMemoryEventStoreTest {

	Event eventTest1 = new Event("TestEvent", 3);
	Event eventTest2 = new Event("TestEvent", 5);
	Event eventTest3 = new Event("TestEvent1", 2);
	Event eventTest4 = new Event("TestEvent1", 7);
	Event eventTest5 = new Event("TestEvent2", 4);

	EventStore eventStore = new InMemoryEventStore();
	EventIterator eventIterator;

	@Before
	public void createEventStore() throws Exception {
		eventStore.insert(eventTest1);
		eventStore.insert(eventTest2);
		eventStore.insert(eventTest3);
		eventStore.insert(eventTest4);
		eventStore.insert(eventTest5);
	}

	@Test
	public void insertNullEvent() {
		Event event = null;
		try {
			eventStore.insert(event);
			Assert.assertTrue(false);
		} catch (NullPointerException e) {
			Assert.assertTrue(e.getMessage().equals("event cannot be null"));
		}
	}

	@Test
	public void insertNullEventType() {
		Event event = new Event(null, 0);
		try {
			eventStore.insert(event);
			Assert.assertTrue(false);
		} catch (NullPointerException e) {
			Assert.assertTrue(e.getMessage().equals("event.type cannot be null"));
		}
	}

	@Test
	public void insertSameEventType() {
		Event eventTest6 = new Event("TestEvent2", 4);
		try {
			eventStore.insert(eventTest6);
			Assert.assertTrue(true);
		} catch (NullPointerException e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void removeNullEventType() {
		try {
			eventStore.removeAll(null);
			Assert.assertTrue(false);
		} catch (NullPointerException e) {
			Assert.assertTrue(e.getMessage().equals("type cannot be null"));
		}
	}

	@Test
	public void removeEventType() {
		try {
			eventStore.removeAll("TestEvent");
			eventIterator = eventStore.query("TestEvent", 0, 10);
			Assert.assertFalse(eventIterator.moveNext());
		} catch (NullPointerException e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void queryIteratorRemove() {
		eventIterator = eventStore.query("TestEvent", 0, 3);
		while (eventIterator.moveNext()) {
			eventIterator.remove();
		}
		eventIterator = eventStore.query("TestEvent", 0, 3);
		assertFalse(eventIterator.moveNext());
	}

	@After
	public void destroy() throws Exception {
		eventStore.removeAll("type");
	}
}
