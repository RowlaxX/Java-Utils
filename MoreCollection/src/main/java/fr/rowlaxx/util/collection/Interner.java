package fr.rowlaxx.util.collection;

/**
 * @author Theo
 * @since 1.0
 */
public interface Interner<E> {

	public E intern(E e);
	
}
