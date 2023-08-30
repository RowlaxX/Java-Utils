package fr.rowlaxx.util.collection.bitmap;

import java.util.Optional;

/**
 * @author Theo
 * @since 1.0
 */
final class SegmentImpl implements Segment {

	boolean enabled;
	SegmentedBitmapImpl owner;
	long offset;
	SegmentImpl previous;
	SegmentImpl next;
	
	SegmentImpl(SegmentedBitmapImpl owner) {
		this.owner = owner;
		this.enabled = false;
		this.offset = 0;
		this.previous = null;
		this.next = null;
	}
	
	private SegmentImpl(SegmentedBitmapImpl owner, SegmentImpl previous, SegmentImpl next, boolean type, long offset) {
		this.owner = owner;
		this.previous = previous;
		this.next = next;
		this.enabled = type;
		this.offset = offset;
	}
	
	private final void ensurePrevious() {
		if (previous == null) {
			previous = new SegmentImpl(owner, null, this, !enabled, offset);
			owner.first = previous;
		}
	}
	
	private final void ensureNext() {
		if (next == null) {
			next = new SegmentImpl(owner, this, null, !enabled, offset);
			owner.last = next;
		}
	}
	
	private final SegmentImpl insert() {
		SegmentImpl middle = new SegmentImpl(owner, this, null, !enabled, offset);
		SegmentImpl right = new SegmentImpl(owner, middle, next, enabled, offset);
		middle.next = right;
		
		if (next == null) //fin de la droite
			owner.last = right;
		else
			next.previous = right;
		
		next = middle;
		return middle;
	}
	
	@Override
	public boolean isFirst() {
		return previous == null && owner != null;
	}
	
	@Override
	public boolean isLast() {
		return next == null && owner != null;
	}
	
	@Override
	public Optional<SegmentedBitmap> bitmap() {
		return Optional.ofNullable(owner);
	}

	@Override
	public Optional<Segment> previous() {
		return Optional.ofNullable(previous);
	}

	@Override
	public Optional<Segment> next() {
		return Optional.ofNullable(next);
	}

	@Override
	public long startsAt() {
		return offset;
	}

	@Override
	public long endsAt() {
		return (next == null ? owner.capacity : next.offset) - 1;
	}

	@Override
	public long length() {
		return (next == null ? owner.capacity : next.offset) - offset;  //endsAt() - startsAt() + 1
	}

	@Override
	public long enabled() {
		return enabled ? length() : 0;
	}

	@Override
	public long disabled() {
		return enabled ? 0 : length();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isRemoved() {
		return owner == null;
	}
	
	private final void checkIndex(long index) {
		if (owner == null)
			throw new IllegalStateException("this segment is destroyed");
		if (index < 0)
			throw new IllegalArgumentException("index must be positiv");
		if (index > endsAt())
			throw new IllegalArgumentException("index must be less than " + length());
	}
	
	private final void checkRange(long start, long end) {
		if (owner == null)
			throw new IllegalStateException("this segment is destroyed");
		if (start < 0)
			throw new IllegalArgumentException("start must be positiv");
		if (start > end)
			throw new IllegalArgumentException("end must be greater than start");
		if (end > endsAt())
			throw new IllegalArgumentException("end must be less than " + length());
	}
	
	@Override
	public Segment flipFirst(long index) {
		checkIndex(index);
		ensurePrevious();
		index++;
		offset += index;
		owner.size += enabled ? -index : index;
		return length() == 0 ? remove() : previous;
	}
	
	@Override
	public Segment flipLast(long index) {
		checkIndex(index);
		long flipped = length() - index;
		ensureNext();
		next.offset = offset + index;
		owner.size += enabled ? -flipped : flipped;
		return offset == next.offset ? remove() : next; // fast length() == 0 check
	}
	
	@Override
	public Segment flip(long index) {
		if (index == 0)
			return flipFirst(0);
		else if (index == length() - 1)
			return flipLast(index);
		
		checkIndex(index);
		SegmentImpl middle = insert();
		owner.size += enabled ? -1 : 1;
		
		middle.offset = offset + index;
		middle.next.offset = middle.offset + 1;
		return middle;
	}
	
	@Override
	public Segment flipAll(long start, long end) {
		long l = length() - 1;
		
		if (start == 0 && end == l)
			return flipAll();
		else if (start == 0)
			return flipFirst(end);
		else if (end == l)
			return flipLast(start);
		
		checkRange(start, end);
		SegmentImpl middle = insert();
		long flipped = end - start + 1;
		owner.size += enabled ? -flipped : flipped;
		
		middle.offset += start;
		middle.next.offset += end + 1;
		return middle;
	}
	
	@Override
	public Segment flipAll() {
		if (next == null && previous == null) {
			enabled = !enabled;
			return this;
		}
		
		return remove();
	}

	@Override
	public Segment set(long index, boolean value) {
		return value == enabled ? this : flip(index);
	}

	@Override
	public Segment setLast(long index, boolean value) {
		return value == enabled ? this : flipLast(index);
	}
	
	@Override
	public Segment setFirst(long index, boolean value) {
		return value == enabled ? this : flipFirst(index);
	}

	@Override
	public Segment setAll(boolean value) {
		return value == enabled ? this : flipAll();
	}
	
	@Override
	public Segment setAll(long start, long end, boolean value) {
		return value == enabled ? this : flipAll(start, end);
	}
	
	@Override
	public Segment remove() {
		if (owner == null)
			return this;
		
		if (next == null && previous == null) //Ne peut pas être supprimé (segment initial)
			return this;

		owner.size += (enabled ? -1 : 1) * length();
		
		if (previous == null) { //uniquement next existe (debut de la droite)
			next.offset = this.offset;
			next.previous = null;
			owner.first = next;
		}
		else if (next == null) { //uniquement previous existe (fin de la droite)
			previous.next = null;
			owner.last = previous;
		}
		else {//les deux existent
			previous.next = next.next;
			
			if (next.next == null) // fin de la droite
				owner.last = previous;
			else
				next.next.previous = previous;

			next.previous = null;
			next.next = null;
		}
	
		Segment r = previous == null ? next : previous;
		previous = null;
		next = null;
		owner = null;
		return r;
	}
}