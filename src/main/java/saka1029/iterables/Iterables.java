package saka1029.iterables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Iterables {
	
	public static Iterable<Integer> range(int start, int end) {
		return () -> new Iterator<Integer>() {
			int i = start;

			@Override
			public boolean hasNext() {
				return i < end;
			}

			@Override
			public Integer next() {
				return i++;
			}
			
		};
	}

	public static List<Integer> ints(int... elements) {
		List<Integer> list = new ArrayList<>();
		for (int element : elements)
			list.add(element);
		return list;
	}

	public static <T, U> Iterable<U> map(Function<T, U> mapper, Iterable<T> source) {
		return () -> new Iterator<U>() {
			final Iterator<T> iterator = source.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public U next() {
				return mapper.apply(iterator.next());
			}
			
		};
	}

	public static <T> Iterable<T> filter(Predicate<T> selector, Iterable<T> source) {
		return () -> new Iterator<T>() {
			final Iterator<T> iterator = source.iterator();
			boolean hasNext = advance();
			T next;
			
			boolean advance() {
				while (iterator.hasNext())
					if (selector.test(next = iterator.next()))
						return true;
				return false;
			}

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public T next() {
				T result = next;
				hasNext = advance();
				return result;
			}
			
		};
	}
	
	public static <T> List<T> list(Supplier<List<T>> constructor, Iterable<T> source) {
		List<T> result = constructor.get();
		for (T element : source)
			result.add(element);
		return result;
	}

	public static <T> ArrayList<T> arrayList(Iterable<T> source) {
		return (ArrayList<T>)list(ArrayList::new, source);
	}
	
	public static <T> T[] array(IntFunction<T[]> constructor, Iterable<T> source) {
		return arrayList(source).toArray(constructor);
	}
	
	public static <T, K, V> Map<K, V> map(Supplier<Map<K, V>> constructor, Function<T, K> key, Function<T, V> value, Iterable<T> source) {
		Map<K, V> result = constructor.get();
		for (T element : source)
			result.put(key.apply(element), value.apply(element));
		return result;
	}

	public static <T, K, V> HashMap<K, V> hashMap(Function<T, K> key, Function<T, V> value, Iterable<T> source) {
		return (HashMap<K, V>)map(HashMap::new, key, value, source);
	}

	public static <T, K, V> TreeMap<K, V> treeMap(Function<T, K> key, Function<T, V> value, Iterable<T> source) {
		return (TreeMap<K, V>)map(HashMap::new, key, value, source);
	}

}
