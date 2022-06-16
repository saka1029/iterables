package saka1029.iterables;

import static org.junit.Assert.assertEquals;
import static saka1029.iterables.Iterables.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

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
		assertEquals(List.of(20, 30, 40), arrayList(map(n -> n * 10, ints(2, 3, 4))));
		assertEquals(List.of(12, 23, 34), arrayList(map((i, n) -> ++i * 10 + n, ints(2, 3, 4))));
		assertEquals(List.of("0:zero", "1:one"),
            arrayList(map((i, n) -> i + ":" + n, List.of("zero", "one"))));
		assertEquals(List.of("0:zero", "1:one"),
            arrayList(map((l, r) -> l + ":" + r, range(0, 5), List.of("zero", "one"))));
	}

	@Test
	public void testFilter() {
		assertEquals(List.of(0, 2, 4), arrayList(filter(n -> n % 2 == 0, range(0, 5))));
		assertEquals(List.of(0, 4), arrayList(filter((i, n) -> i == n, ints(0, 9, 8, 7, 4))));
		assertEquals(List.of(10, 30), arrayList(map(n -> 10 * n, filter(n -> n % 2 == 1, range(0, 5)))));
	}
	
	@Test
	public void testArrayList() {
		assertEquals(List.of(2, 3, 4), arrayList(ints(2, 3, 4)));
		assertEquals(List.of(2, 3, 4), arrayList(List.of(2, 3, 4)));
		assertEquals(List.of(2, 3, 4), arrayList(() -> Stream.of(2, 3, 4).iterator()));
	}
	
	@Test
	public void testLinkedList() {
		assertEquals(List.of(2, 3, 4), linkedList(ints(2, 3, 4)));
		assertEquals(List.of(2, 3, 4), linkedList(List.of(2, 3, 4)));
	}
	
    record N(int i, String s) {}

	@Test
	public void testHashMap() {
		assertEquals(Map.of(0, "zero", 1, "one"),
			hashMap(N::i, N::s, List.of(new N(0, "zero"), new N(1, "one"))));
	}

	@Test
	public void testTreeMap() {
		assertEquals(Map.of(0, "zero", 1, "one"),
			treeMap(N::i, N::s, List.of(new N(0, "zero"), new N(1, "one"))));
	}
}
