package fr.rowlaxx.util.collection.bitmap;

import java.io.IOException;
import java.util.NoSuchElementException;

import fr.rowlaxx.util.io.NumberInputStream;
import fr.rowlaxx.util.io.NumberOutputStream;

/**
 * @author Theo
 * @since 1.0
 */
final class SegmentedBitmapImpl implements SegmentedBitmap {
	private static final int MAGIC = 0x3C7D24E7;
	
	
	final long capacity;
	SegmentImpl first;
	SegmentImpl last;
	long size;
	
	/*======================================*\
	|				Constructors			 |
	\*======================================*/
	SegmentedBitmapImpl(long capacity) {
		if (capacity <= 0)
			throw new IllegalArgumentException("capacity must be greater than 0");
		
		this.first = new SegmentImpl(this);
		this.last = first;
		this.size = 0;
		this.capacity = capacity;
	}
	
	private SegmentedBitmapImpl(SegmentedBitmapImpl another) {
		this.size = another.size;
		this.capacity = another.capacity;
		
		SegmentImpl node1 = new SegmentImpl(this);
		SegmentImpl node2;
		first = node1;
		
		for (SegmentImpl s = another.first ; s != null ; s = s.next) {
			node1.offset = s.offset;
			node1.enabled = s.enabled;
			
			if (s.next != null) {
				node2 = new SegmentImpl(this);
				node1.next = node2;
				node2.previous = node1;
				node1 = node2;
			}
		}
		
		last = node1;
	}
	
	
	
	/*======================================*\
	|				Java methods			 |
	\*======================================*/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		
		for (SegmentImpl s = first ; s != null ; s = s.next)
			if (s.enabled) {
				if (s.length() == 1)
					sb.append(s.offset);
				else
					sb.append('[')
						.append(s.offset)
						.append('-')
						.append(s.endsAt())
						.append(']');
		
				if (s.next != null && s.next.next != null)
					sb.append("+");
			}
			
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		 int result = 1;

		 result = (int)(31 * result + capacity);
		 result = (int)(31 * result + size);
		 result = (int)(31 * result + (first.enabled ? 1 : 0));
		 
	     for (SegmentImpl s = first ; s != null ; s = s.next)
	    	 result = (int)(31 * result + s.offset);

	    return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SegmentedBitmapImpl other = (SegmentedBitmapImpl) obj;
		
		if (capacity != other.capacity || size != other.size)
			return false;
		
		if (first.enabled != other.first.enabled)
			return false;
		
		for (SegmentImpl s1 = first, s2 = other.first ; s1 != null && s2 != null ; s1 = s1.next, s2 = s2.next) {
			if (s1 == null || s2 == null)
				return false;
			if (s1.offset != s2.offset)
				return false;
		}
		
		return true;
	}

	@Override
	public SegmentedBitmap clone() {
		return new SegmentedBitmapImpl(this);
	}
	
	
	
	/*======================================*\
	|				Private methods			 |
	\*======================================*/
	private final SegmentImpl checkless_getSegment(long number) {
		SegmentImpl seg;
		
		if ( (first.offset + last.endsAt()) >> 1 >= number )
			for (seg = first ; number > seg.endsAt() ; seg = seg.next);
		else
			for (seg = last ; number < seg.offset ; seg = seg.previous);
		
		return seg;
	}
	
	private final void checkRange(long start, long end) {
		if (end < start)
			throw new IndexOutOfBoundsException("end must be greater than start");
		if (start < 0)
			throw new IndexOutOfBoundsException("start must be greater or equals than 0");
		if (end >= capacity)
			throw new IndexOutOfBoundsException("end must be less than " + capacity);
	}
	
	private final void checkIndex(long number) {
		if (number < 0)
			throw new IndexOutOfBoundsException("number must be positiv");
		if (number >= capacity)
			throw new IndexOutOfBoundsException("number must be less than " + capacity);
	}
	
	
	
	
	/*======================================*\
	|				Getters					 |
	\*======================================*/
	@Override
	public Segment getSegment(long number) {
		checkIndex(number);
		return checkless_getSegment(number);
	}
	
	@Override
	public boolean contains(long number) {
		return getSegment(number).isEnabled();
	}

	@Override
	public boolean containsAll(long start, long end) {
		checkRange(start, end);
		SegmentImpl seg = checkless_getSegment(start);
		return seg.enabled && end <= seg.endsAt();
	}

	@Override
	public long size() {
		return size;
	}

	@Override
	public long select(long index) {
		checkIndex(index);
		if (index >= size)
			throw new NoSuchElementException();
		
		long l;
		for (SegmentImpl seg = first ; ; seg = seg.next) {
			if (!seg.enabled)
				continue;
			
			l = seg.length();
			if (index <= l)
				return seg.offset + index;
			index -= l;
		}
	}
	
	@Override
	public long indexOf(long number) {
		checkIndex(number);
		SegmentImpl seg = checkless_getSegment(number);
		
		if (!seg.enabled)
			return -1;
		
		long count = number - seg.offset;
		
		for (seg = seg.previous ; seg != null ; seg = seg.previous)
			count += seg.enabled();
		
		return count;
	}

	@Override
	public long next(long number, boolean present) {
		checkIndex(number);
		SegmentImpl seg = checkless_getSegment(number);
		
		if (seg.enabled != present) {
			if (seg.next == null)
				return -1;
			return seg.next.offset;
		}
		
		return number;
	}

	@Override
	public long next(long number) {
		return next(number, true);
	}

	@Override
	public long previous(long number, boolean present) {
		checkIndex(number);
		SegmentImpl seg = checkless_getSegment(number);
		
		if (seg.enabled != present) {
			if (seg.previous == null)
				return -1;
			return seg.offset - 1;
		}
		
		return number;
	}

	@Override
	public long previous(long number) {
		return previous(number, true);
	}

	@Override
	public long first() {
		if (size == 0)
			return -1;
		if (first.enabled)
			return first.offset;
		return first.next.offset;
	}

	@Override
	public long last() {
		if (size == 0)
			return -1;
		if (last.enabled)
			return capacity - 1;
		return last.offset - 1;
	}

	@Override
	public long firstMissing() {
		if (size == capacity)
			return -1;
		if (first.enabled)
			return first.next.offset;
		return first.offset;
	}
	
	@Override
	public long lastMissing() {
		if (size == capacity)
			return -1;
		if (last.enabled)
			return last.offset - 1;
		return capacity - 1;
	}

	@Override
	public Segment firstSegment() {
		return first;
	}

	@Override
	public Segment lastSegment() {
		return last;
	}

	@Override
	public long capacity() {
		return capacity;
	}
	
	
	
	/*======================================*\
	|				Setters					 |
	\*======================================*/
	@Override
	public void add(long number) {
		set(number, true);
	}
	
	@Override
	public void set(long number, boolean present) {
		checkIndex(number);
		SegmentImpl seg = checkless_getSegment(number);
		seg.set(number - seg.offset, present);
	}
	
	@Override
	public void flip(long number) {
		checkIndex(number);
		SegmentImpl seg = checkless_getSegment(number);
		seg.flip(number - seg.offset);
	}
	
	@Override
	public void remove(long number) {
		set(number, false);
	}
	
	@Override
	public void addAll(long start, long end) {
		setAll(start, end, true);
	}
	
	@Override
	public void removeAll(long start, long end) {
		setAll(start, end, false);
	}
	
	@Override
	public void flipAll(long start, long end) {
		checkRange(start, end);
		
		SegmentImpl seg = checkless_getSegment(start);
		long startAbsolute = start > seg.offset ? start : seg.offset;
		long endAbsolute;
		
		while (startAbsolute <= end) {
			endAbsolute = end > seg.endsAt() ? seg.endsAt() : end;
			seg = (SegmentImpl) seg.flipAll(startAbsolute - seg.offset, endAbsolute - seg.offset);
			startAbsolute = endAbsolute + 1;
		}
	}
	
	@Override
	public void setAll(long start, long end, boolean present) {
		checkRange(start, end);
		
		SegmentImpl seg = checkless_getSegment(start);
		long index;
		
		if (seg.enabled != present) {
			index = seg.endsAt() < end ? seg.endsAt() : end;
			seg = (SegmentImpl) seg.setAll(start - seg.offset, index - seg.offset, present);
		}
		
		seg = seg.next;
		if (seg == null)
			return;
		
		while (seg.offset <= end) {
			index = (seg.endsAt() < end ? seg.endsAt() : end) - seg.offset;
			seg = ((SegmentImpl) seg.setFirst(index, present)).next;
		}
	}
	
	
	
	
	
	/*======================================*\
	|				IO						 |
	\*======================================*/
	@Override
	public void serialize(NumberOutputStream out) throws IOException {
		out.writeInt(MAGIC);
		
		byte header = (byte)(
				(capacity == Long.MAX_VALUE 		? 0b1000_0000 : 0) +
				(first.enabled 						? 0b0100_0000 : 0) +
				(size == 0 || size == capacity 		? 0b0010_0000 : 0) );
		
		out.writeByte(header);
		
		if (capacity != Long.MAX_VALUE)
			out.writeNumber(capacity);
		if (size != 0 && size != capacity)
			out.writeNumber(size);
		
		
		for (SegmentImpl s = first ; s.next != null ; s = s.next)
			out.writeNumber(s.length());
		
		out.writeByte(-1);
		out.writeInt(hashCode());
		out.flush();
	}
	
	SegmentedBitmapImpl(NumberInputStream in) throws IOException {
		if (in.readInt() != MAGIC)
			throw new IOException("Not a SegmentedBitmap : bad magic number");
		
		byte header = in.readByte();
		boolean maxCapacity  = (header & 0b1000_0000) > 0;
		boolean firstEnabled = (header & 0b0100_0000) > 0;
		boolean skipSize 	 = (header & 0b0010_0000) > 0;
		
		first = new SegmentImpl(this);
		first.enabled = firstEnabled;
		last = first;
		capacity = maxCapacity ? Long.MAX_VALUE : in.unsafeReadNumber();		
		size = firstEnabled ? capacity : 0;
		long finalSize = skipSize ? size : in.unsafeReadNumber();
		
		long segSize;
		while ( (segSize = in.unsafeReadNumber()) >= 0 )
			last.flipLast(segSize);
		
		if (in.readInt() != hashCode())
			throw new IOException("Corrupted SegmentedBitmap : wrong hashcode");
		if (size != finalSize)
			throw new IOException("Corrupted SegmentedBitmap : size is " + size + " and must be " + finalSize);
	}
}
