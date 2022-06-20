package saka1029.iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
	
	/**
	 * StreamをIterableに変換します。
	 * ただし、返されるIterableは1回しかiterator()を呼び出すことができません。 
	 * 2回呼び出すとIllegalStateExceptionがスローされます。
	 */
	public static <T> Iterable<T> iterable(Stream<T> stream) {
		return () -> stream.iterator();
	}

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

	public static Iterable<Integer> rangeClosed(int start, int end) {
		return () -> new Iterator<Integer>() {

			int i = start;

			@Override
			public boolean hasNext() {
				return i <= end;
			}

			@Override
			public Integer next() {
				return i++;
			}
			
		};
	}

	public static Iterable<Integer> range(int start, int end, int step) {
		return () -> new Iterator<Integer>() {

			int i = start;

			@Override
			public boolean hasNext() {
				return step > 0 ? i < end : i > end;
			}

			@Override
			public Integer next() {
				int result = i;
				i += step;
				return result;
			}
			
		};
	}

	public static List<Integer> list(int... elements) {
		List<Integer> list = new ArrayList<>();
		for (int element : elements)
			list.add(element);
		return list;
	}
	
	public static Iterable<Integer> iterable(String s) {
		return () -> s.codePoints().iterator();
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
	
	public static <T> List<T> sort(Comparator<T> comparator, Iterable<T> source) {
		List<T> list = arrayList(source);
		list.sort(comparator);
		return list;
	}
	
	public static <T extends Comparable<T>> List<T> sort(Iterable<T> source) {
		return sort(Comparator.naturalOrder(), source);
	}

	public static <T> List<T> reverse(Iterable<T> source) {
		List<T> list = arrayList(source);
		Collections.reverse(list);
		return list;
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
	
	public static <T> T reduce(BinaryOperator<T> reducer, Iterable<T> source) {
		T result = null;
		boolean first = true;
		for (T t : source) {
			result = first ? t : reducer.apply(result, t);
			first = false;
		}
		return result;
	}

	public static <T, U> U reduce(U unit, BiFunction<U, T, U> reducer, Iterable<T> source) {
		for (T t : source)
			unit = reducer.apply(unit, t);
		return unit;
	}
	
	public static <T> Collection<T> collection(Supplier<Collection<T>> constructor, Iterable<T> source) {
		Collection<T> result = constructor.get();
		for (T element : source)
			result.add(element);
		return result;
	}

	public static <T> ArrayList<T> arrayList(Iterable<T> source) {
		return (ArrayList<T>)collection(ArrayList::new, source);
	}
	
	public static <T> LinkedList<T> linkedList(Iterable<T> source) {
		return (LinkedList<T>)collection(LinkedList::new, source);
	}
		
	public static <T> HashSet<T> hashSet(Iterable<T> source) {
		return (HashSet<T>)collection(HashSet::new, source);
	}
	
	public static <T extends Comparable<T>> TreeSet<T> treeSet(Iterable<T> source) {
		return (TreeSet<T>)collection(TreeSet::new, source);
	}
	
	public static <T> TreeSet<T> treeSet(Comparator<T> comparator, Iterable<T> source) {
		return (TreeSet<T>)collection(() -> new TreeSet<>(comparator), source);
	}
	
	public static <T> T[] array(IntFunction<T[]> constructor, Iterable<T> source) {
		return arrayList(source).toArray(constructor);
	}
	
	public static int[] array(Iterable<Integer> source) {
		ArrayList<Integer> list = arrayList(source);
		int size = list.size();
		int[] result = new int[size];
		for (int i = 0; i < size; ++i)
			result[i] = list.get(i);
		return result;
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
	
	public static <T, U extends Comparable<U>> Comparator<T> asc(Function<T, U> extractor) {
		return Comparator.comparing(extractor);
	}

	public static <T, U extends Comparable<U>> Comparator<T> desc(Function<T, U> extractor) {
		return Comparator.comparing(extractor).reversed();
	}

	public static <T> Comparator<T> reverse(Comparator<T> comparator) {
		return comparator.reversed();
	}

	public static <T extends Comparable<T>> Comparator<T> reverse() {
		return Comparator.reverseOrder();
	}

    @SafeVarargs
    public static <T> Comparator<T> and(Comparator<T> first, Comparator<T>... rest) {
        for (Comparator<T> c : rest)
            first = first.thenComparing(c);
        return first;
    }

}
