package saka1029.iterables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Iterables {
	
	public static <T, C> Iterator<T> iterator(C context, Predicate<C> hasNext, Function<C, T> next) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return hasNext.test(context);
			}

			@Override
			public T next() {
				return next.apply(context);
			}
			
		};
	}

	public static <T, C> Iterable<T> iterable(C context, Predicate<C> hasNext, Function<C, T> next) {
		return () -> iterator(context, hasNext, next);
	}

	public static Iterable<Integer> range(int start, int end) {
		return iterable(new Object() { int i = start; }, c -> c.i < end, c -> c.i++);
//		return () -> new Iterator<Integer>() {
//			int i = start;
//
//			@Override
//			public boolean hasNext() {
//				return i < end;
//			}
//
//			@Override
//			public Integer next() {
//				return i++;
//			}
//			
//		};
	}

	public static Iterable<Integer> range(int start, int end, int step) {
		return iterable(new Object() { int i = start; }, c -> c.i < end, c -> c.i += step);
	}

	public static List<Integer> ints(int... elements) {
		List<Integer> list = new ArrayList<>();
		for (int element : elements)
			list.add(element);
		return list;
	}

	public static <T, U> Iterable<U> map(BiFunction<Integer, T, U> mapper, Iterable<T> source) {
		return () -> new Iterator<U>() {

			final Iterator<T> iterator = source.iterator();
			int i = 0;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public U next() {
				return mapper.apply(i++, iterator.next());
			}
			
		};
	}

	public static <T, U> Iterable<U> map(Function<T, U> mapper, Iterable<T> source) {
		return iterable(source.iterator(), c -> c.hasNext(), c -> mapper.apply(c.next()));
//		return () -> new Iterator<U>() {
//
//			final Iterator<T> iterator = source.iterator();
//
//			@Override
//			public boolean hasNext() {
//				return iterator.hasNext();
//			}
//
//			@Override
//			public U next() {
//				return mapper.apply(iterator.next());
//			}
//			
//		};
	}

	public static <L, R, U> Iterable<U> map(BiFunction<L, R, U> mapper, Iterable<L> leftSource, Iterable<R> rightSource) {
		return () -> new Iterator<U>() {

			final Iterator<L> left = leftSource.iterator();
			final Iterator<R> right = rightSource.iterator();

			@Override
			public boolean hasNext() {
				return left.hasNext() && right.hasNext();
			}

			@Override
			public U next() {
				return mapper.apply(left.next(), right.next());
			}
			
		};
	}

	public static <T> Iterable<T> filter(BiPredicate<Integer, T> selector, Iterable<T> source) {
		return () -> new Iterator<T>() {

			final Iterator<T> iterator = source.iterator();
			int index = 0;
			boolean hasNext = advance();
			T next;
			
			boolean advance() {
				while (iterator.hasNext())
					if (selector.test(index++, next = iterator.next()))
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
	
	public static <T> LinkedList<T> linkedList(Iterable<T> source) {
		return (LinkedList<T>)list(LinkedList::new, source);
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
		return (TreeMap<K, V>)map(TreeMap::new, key, value, source);
	}

}
