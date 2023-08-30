package fr.rowlaxx.util.array;

import java.util.Objects;

/**
 * @author Theo
 * @since 1.0
 */
class ByteArrayIO {
	protected final ByteArray array;	
	protected final int limit;
	protected int index;

	ByteArrayIO(ByteArray array, int index, int length) {
		Objects.checkFromIndexSize(index, length, array.length());
		this.index = index;
		this.limit = index + length;
		this.array = array;
	}
	
	protected final void ensure(int length) {
		Objects.checkFromIndexSize(index, length, limit);
	}
	
	public final ByteArray array() {
		return array;
	}
	
	public final int index() {
		return index;
	}
	
	public final int limit() {
		return limit;
	}
	
	public final int remaining() {
		return limit - index;
	}
	
	public final int skipBytes(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n must be positive or 0");
		
		int remains = remaining();
		int skipped = remains < n ? remains : n;
		
		index += skipped;
		return skipped;
	}
}
