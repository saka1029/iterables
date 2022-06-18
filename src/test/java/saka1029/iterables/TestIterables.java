package saka1029.iterables;

import static org.junit.Assert.assertEquals;
import static saka1029.iterables.Iterables.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.Test;

public class TestIterables {

	@Test
	public void testRangeIntInt() {
		assertEquals(List.of(2, 3, 4), arrayList(range(2, 5)));
		assertEquals(List.of(), arrayList(range(2, 2)));
	}

	@Test
	public void testRangeIntIntInt() {
		assertEquals(List.of(2, 3, 4), arrayList(range(2, 5, 1)));
		assertEquals(List.of(2, 4), arrayList(range(2, 5, 2)));
		assertEquals(List.of(5, 3), arrayList(range(5, 2, -2)));
		assertEquals(List.of(), arrayList(range(5, 9, -2)));
	}

	@Test
	public void testInts() {
		assertEquals(List.of(2, 3, 4), arrayList(list(2, 3, 4)));
	}

	@Test
	public void testMap() {
		assertEquals(List.of(20, 30, 40), arrayList(map(n -> n * 10, list(2, 3, 4))));
		assertEquals(List.of(12, 23, 34), arrayList(map((i, n) -> ++i * 10 + n, list(2, 3, 4))));
		assertEquals(List.of("0:zero", "1:one"),
            arrayList(map((i, n) -> i + ":" + n, List.of("zero", "one"))));
		assertEquals(List.of("0:zero", "1:one"),
            arrayList(map((l, r) -> l + ":" + r, range(0, 5), List.of("zero", "one"))));
	}

	@Test
	public void testFilter() {
		assertEquals(List.of(0, 2, 4), arrayList(filter(n -> n % 2 == 0, range(0, 5))));
		assertEquals(List.of(0, 4), arrayList(filter((i, n) -> i == n, list(0, 9, 8, 7, 4))));
		assertEquals(List.of(10, 30), arrayList(map(n -> 10 * n, filter(n -> n % 2 == 1, range(0, 5)))));
	}
	
	@Test
	public void testArrayList() {
		assertEquals(List.of(2, 3, 4), arrayList(list(2, 3, 4)));
		assertEquals(ArrayList.class, arrayList(list(2, 3, 4)).getClass());
		assertEquals(List.of(2, 3, 4), arrayList(List.of(2, 3, 4)));
		assertEquals(List.of(2, 3, 4), arrayList(() -> Stream.of(2, 3, 4).iterator()));
	}

	@Test
	public void testLinkedList() {
		assertEquals(List.of(2, 3, 4), linkedList(list(2, 3, 4)));
		assertEquals(LinkedList.class, linkedList(list(2, 3, 4)).getClass());
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
