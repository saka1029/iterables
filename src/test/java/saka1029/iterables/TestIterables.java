package saka1029.iterables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static saka1029.iterables.Iterables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;

public class TestIterables {

	@Test
	public void testIterable() {
		Iterable<Integer> iterable = iterable(Stream.of(1, 2));
		Iterator<Integer> iterator = iterable.iterator();
		assertEquals(1, (int)iterator.next());
		assertEquals(2, (int)iterator.next());
		assertFalse(iterator.hasNext());
		try {
            Iterator<Integer> iterator2 = iterable.iterator();
            fail();
            assertEquals(1, (int)iterator2.next());
		} catch (IllegalStateException e) {
			assertEquals("stream has already been operated upon or closed", e.getMessage());
		}
	}
	
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
	public void testIterableInts() {
		assertEquals(List.of(2, 3, 4), arrayList(list(2, 3, 4)));
	}
	
	@Test
	public void testIterableString() {
		assertEquals(List.of(97, 98, 99), arrayList(iterable("abc")));
		assertEquals(List.of(97, 171581, 99), arrayList(iterable("a𩸽c")));
		Iterable<Integer> it = iterable("abc");
		assertEquals(List.of(97, 98, 99), arrayList(it));
		assertEquals(List.of(97, 98, 99), arrayList(it));
		assertEquals(List.of("a", "𩸽", "c"), arrayList(map(Character::toString, iterable("a𩸽c"))));
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
	public void testReduce() {
		assertEquals((Integer)45, reduce(0, Integer::sum, rangeClosed(1, 9)));
		assertEquals((Integer)45, reduce(Integer::sum, rangeClosed(1, 9)));
		assertEquals((Integer)120, reduce((a, b) -> a * b, rangeClosed(1, 5)));
	}

	@Test
	public void testSort() {
		assertEquals(List.of(0, 1, 2, 3, 4), sort(list(2, 0, 1, 4, 3)));
		assertEquals(List.of(4, 3, 2, 1, 0), sort(reverse(), list(2, 0, 1, 4, 3)));
		assertEquals(List.of(0, 1, 2, 3, 4), sort((a, b) -> Integer.compare(a, b), list(2, 0, 1, 4, 3)));
		assertEquals(List.of(4, 3, 2, 1, 0), sort((a, b) -> Integer.compare(b, a), list(2, 0, 1, 4, 3)));
	}

	@Test
	public void testReverse() {
		assertEquals(List.of(3, 4, 1, 0, 2), reverse(list(2, 0, 1, 4, 3)));
	}
	
	@Test
	public void testCollection() {
		Collection<Integer> c = collection(HashSet::new, list(1, 2, 2, 3));
		assertEquals(HashSet.class, c.getClass());
		assertEquals(Set.of(1, 2, 3), c);
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
	
	@Test
	public void testAnd() {
		record R(String s, int i) {}
		R a0 = new R("a", 0);
		R a1 = new R("a", 1);
		R b0 = new R("b", 0);
		R b1 = new R("b", 1);
		List<R> list = List.of(a0, a1, b0, b1);
		assertEquals(List.of(a0, a1, b0, b1), arrayList(sort(and(asc(R::s), asc(R::i)), list)));
		assertEquals(List.of(a1, a0, b1, b0), arrayList(sort(and(asc(R::s), desc(R::i)), list)));
		assertEquals(List.of(b0, b1, a0, a1), arrayList(sort(and(desc(R::s), asc(R::i)), list)));
		assertEquals(List.of(b1, b0, a1, a0), arrayList(sort(and(desc(R::s), desc(R::i)), list)));
		assertEquals(List.of(b1, b0, a1, a0), arrayList(sort(and(reverse(asc(R::s)), reverse(asc(R::i))), list)));
	}
}
