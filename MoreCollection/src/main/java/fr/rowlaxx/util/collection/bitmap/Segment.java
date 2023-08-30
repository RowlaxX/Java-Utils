package fr.rowlaxx.util.collection.bitmap;

import java.util.Optional;

/**
 * Represents a segment on the bitmap.
 * A segment can be considered as an interval.
 * @author Theo
 * @since 1.0
 * @version 1
 */
public sealed interface Segment permits SegmentImpl {

	/**
	 * Get the bitmap that contains this segment.
	 * The optional will contains nothing if this segment is removed.
	 * @return the bitmap
	 */
	public Optional<SegmentedBitmap> bitmap();
	
	/**
	 * Get the previous segment. 
	 * The optional will contains nothing if this segment is removed 
	 * or if this segment is the beginning of the bitmap.
	 * @return the previous segment
	 */
	public Optional<Segment> previous();
	
	/**
	 * Get the next segment. 
	 * The optional will contains nothing if this segment is removed 
	 * or if this segment is the end of the bitmap.
	 * @return the previous segment
	 */
	public Optional<Segment> next();
	
	/**
	 * Know if this segment is the beginning of the bitmap.
	 * If this segment is removed, this return will return false.
	 * @return true if this segment is the beginning of the bitmap
	 */
	public boolean isFirst();
	
	/**
	 * Know if this segment is the end of the bitmap.
	 * If this segment is removed, this return will return false.
	 * @return true if this segment is the end of the bitmap
	 */
	public boolean isLast();
	
	/**
	 * Get the absolute start offset of this segment.
	 * @return the start offset
	 */
	public long startsAt();
	
	/**
	 * Get the absolute end offset of this segment.
	 * @return the end offset
	 */
	public long endsAt();
	
	/**
	 * Get the length of this segment.
	 * @return the length of this segment
	 */
	public long length();
	
	/**
	 * Get the count of enabled numbers.
	 * If the segment is disabled, this method return 0, otherwise the length of this segment.
	 * @return the number of enabled numbers
	 */
	public long enabled();
	
	/**
	 * Get the count of disabled length.
	 * If the segment is enabled, this method return 0, otherwise the length of this segment.
	 * @return the number of disabled numbers
	 */
	public long disabled();
	
	/**
	 * Know if the segment is enabled.
	 * @return true if enabled
	 */
	public boolean isEnabled();
	
	/**
	 * Know if the segment is removed.
	 * A removed segment cannot be modified and do not contains
	 * references to previous, next and the bitmap.
	 * A removed segment's goal is to be collected as quickly as possible by the garbage collector.
	 * @see <code>remove()</code>
	 * @return true if removed
	 */
	public boolean isRemoved();
	
	/**
	 * Flip a number in this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * If the number is enabled, then it will be disabled, and vice versa.
	 * @param position the position to flip
	 * @return the segment that contains position after the operation
	 */
	public Segment flip(long position);
	
	/**
	 * Flip the first numbers of this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * This method is equivalent to flipAll(0, position), whereas it's a bit faster.
	 * Note that the position parameter will also be flipped.
	 * (if position = 1, then the numbers 0 and 1 will be flipped)
	 * @param position the position (inclusive)
	 * @return the segment that contains position after the operation
	 */
	public Segment flipFirst(long position);
	
	/**
	 * Flip the last numbers of this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * This method is equivalent to flipAll(position, length() - 1), whereas it's a bit faster.
	 * Note that the position parameter will also be flipped.
	 * (if position = 1, then the number 1,2,... will be flipped)
	 * @param position the position (inclusive)
	 * @return the segment that contains position after the operation
	 */
	public Segment flipLast(long position);
	
	/**
	 * Flip a portion on this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * The range is inclusive.
	 * (if start = 1 and end = 3, then the numbers 1,2,3 will be flipped).
	 * @param start the start of the range
	 * @param end the end of the range
	 * @return the segment that contains the range after the operation
	 */
	public Segment flipAll(long start, long end);
	
	/**
	 * Flip this whole segment.
	 * This method is equivalent to flippAll(0, length() - 1), whereas it's a bit faster.
	 * @return the segment that contains this segment after the operation
	 */
	public Segment flipAll();
	
	/**
	 * Set a number in this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * If the number is enabled, then it will be disabled, and vice versa.
	 * @param position the position to set
	 * @param value true for enabling the position, false for disabling
	 * @return the segment that contains position after the operation
	 */
	public Segment set(long position, boolean value);
	
	/**
	 * Set the first numbers of this segment
	 * If the length of this segment become 0 after the operation, then this segment will be removed..
	 * This method is equivalent to setAll(0, position), whereas it's a bit faster.
	 * Note that the position parameter will also be set.
	 * (if position = 1, then the numbers 0 and 1 will be set)
	 * @param position the position (inclusive)
	 * @param value true for enabling this portion, false for disabling
	 * @return the segment that contains position after the operation
	 */
	public Segment setFirst(long position, boolean value);
	
	/**
	 * Set the last numbers of this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * This method is equivalent to setAll(position, length() - 1), whereas it's a bit faster.
	 * Note that the position parameter will also be set.
	 * (if position = 1, then the number 1,2,... will be set)
	 * @param position the position (inclusive)
	 * @param value true for enabling this portion, false for disabling
	 * @return the segment that contains position after the operation
	 */
	public Segment setLast(long position, boolean value);
	
	/**
	 * Set a portion on this segment.
	 * If the length of this segment become 0 after the operation, then this segment will be removed.
	 * The range is inclusive.
	 * (if start = 1 and end = 3, then the numbers 1,2,3 will be set).
	 * @param start the start of the range
	 * @param end the end of the range
	 * @param value true for enabling this portion, false for disabling
	 * @return the segment that contains the range after the operation
	 */
	public Segment setAll(long start, long end, boolean value);
	
	/**
	 * Set this whole segment.
	 * This method is equivalent to setAll(0, length() - 1), whereas it's a bit faster.
	 * @return the segment that contains this segment after the operation
	 */
	public Segment setAll(boolean value);
	
	/**
	 * Remove this segment.
	 * This will mark the segment as removed. The segment will 
	 * have no previous or next segment, and it's bitmap reference 
	 * will be deleted.
	 * 
	 * This method will also merge the previous and next segment together (if any).
	 * If this segment is the only segment in the bitmap, this method will have no effect.
	 * @return the segment that contains this segment after the operation
	 */
	public Segment remove();
}
