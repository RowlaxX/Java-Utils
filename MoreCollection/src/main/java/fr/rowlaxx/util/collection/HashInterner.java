package fr.rowlaxx.util.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Theo
 * @since 1.0
 */
public class HashInterner<E> implements Interner<E> {

	private final Map<Integer, E> map;
	
	public HashInterner(int capacity) {
		this.map = new HashMap<>(capacity);
	}
	
	public HashInterner() {
		this(64);
	}
	
	@Override
	public E intern(E e) {
		E old = map.putIfAbsent(e.hashCode(), e);
		return old == null ? e : old;
	}
}