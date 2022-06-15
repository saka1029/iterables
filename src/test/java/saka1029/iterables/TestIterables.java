package saka1029.iterables;

import static org.junit.Assert.assertEquals;
import static saka1029.iterables.Iterables.*;

import java.util.List;

import org.junit.Test;

public class TestIterables {

	@Test
	public void testRange() {
		assertEquals(List.of(2, 3, 4), arrayList(range(2, 5)));
	}

	@Test
	public void testInts() {
		assertEquals(List.of(2, 3, 4), arrayList(ints(2, 3, 4)));
	}

	@Test
	public void testMap() {
		assertEquals(List.of(20, 30, 40), arrayList(map(i -> i * 10, ints(2, 3, 4))));
	}

	@Test
	public void testFilter() {
		assertEquals(List.of(0, 2, 4), arrayList(filter(i -> i % 2 == 0, range(0, 5))));
	}
}
