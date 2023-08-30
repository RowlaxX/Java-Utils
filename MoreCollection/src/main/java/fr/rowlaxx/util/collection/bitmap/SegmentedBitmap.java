package fr.rowlaxx.util.collection.bitmap;

import java.io.IOException;
import java.util.NoSuchElementException;

import fr.rowlaxx.util.io.NumberInputStream;
import fr.rowlaxx.util.io.NumberOutputStream;

/**
 * Represents a bitmap wich a able to perform 64 bit calculation
 * A bitmap is a set of integer
 * @author Theo
 * @since 1.0
 * @version 1
 */
public sealed interface SegmentedBitmap extends Cloneable permits SegmentedBitmapImpl {

	public static SegmentedBitmap create(long capacity) {
		return new SegmentedBitmapImpl(capacity);
	}
	
	public static SegmentedBitmap create() {
		return new SegmentedBitmapImpl(Long.MAX_VALUE);
	}
	
	public static SegmentedBitmap from(NumberInputStream in) throws IOException {
		return new SegmentedBitmapImpl(in);
	}
	
	/**
	 * Add a number to the set. If the number already exists, do nothing.
	 * @param number the number to add
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public void add(long number);
	
	/**
	 * Add a range of number to the set. All the numbers in that 
	 * range will be added (if not already) to the set.
	 * @param start the start (inclusive)
	 * @param end the end (inclusive)
	 * @throws IndexOutOfBoundsException if the range is incorrect
	 */
	public void addAll(long start, long end);
	
	/**
	 * Remove a number from the set. If the number do not exists, do nothing
	 * @param number the number to remove
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public void remove(long number);
	
	/**
	 * Remove a range of number from the set. All the numbers in that 
	 * range will be removed (if not already) from the set.
	 * @param start the start (inclusive)
	 * @param end the end (inclusive)
	 * @throws IndexOutOfBoundsException if the range is incorrect	 
	 */
	public void removeAll(long start, long end);
	
	/**
	 * Set a number to the set.
	 * @param number the number to set
	 * @param present true for adding, false for removing
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public void set(long number, boolean present);
	
	/**
	 * Set a range of number to the set. All the numbers in that 
	 * range will be set to the set.
	 * @param start the start (inclusive)
	 * @param end the end (inclusive)
	 * @param present true for adding, false for removing
	 * @throws IndexOutOfBoundsException if the range is incorrect	 
	 */
	public void setAll(long start, long end, boolean present);
	
	/**
	 * Flip a number to the set.
	 * If a number is present, if become absent, and vice versa.
	 * @param number the number to flip
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public void flip(long number);
	
	/**
	 * Flip a range of number in the set. All the numbers in that 
	 * range will be flipped in the set.
	 * If a number is present, if become absent, and vice versa.
	 * @param start the start (inclusive)
	 * @param end the end (inclusive)
	 * @throws IndexOutOfBoundsException if the range is incorrect	 
	 */
	public void flipAll(long start, long end);
	
	/**
	 * Know if a number is present in the set.
	 * @param number the number to check
	 * @return true if the number is present, false otherwise
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public boolean contains(long number);
	
	/**
	 * Know if all number are present in the set
	 * @param start the start (inclusive)
	 * @param end the end (inclusive)
	 * @return true if and only if all the numbers of the range are present in the set	
	 * @throws IndexOutOfBoundsException if the range is incorrect	 
	 */
	public boolean containsAll(long start, long end);
	
	/**
	 * Get the cardinality of this set
	 * @return the number of numbers in the set
	 */
	public long size();
	
	/**
	 * Get the n-th number in the set
	 * @param index the index
	 * @return the number associated with the index
	 * @throws IndexOutOfBoundsException if index is incorrect
	 * @throws NoSuchElementException if index is greater than the size of the set
	 */
	public long select(long index);
	
	/**
	 * Get the index associated with the number.
	 * @param number the number
	 * @return the index associated with the number, or -1 if the number is not present
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public long indexOf(long number);
	
	/**
	 * Search the next number present in the set.
	 * This method is equivalent to next(number, true).
	 * @param number the number to start the search from (inclusive)
	 * @return the next present number, or -1 if none
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public long next(long number);
	
	/**
	 * Search the next number present or absent in the set.
	 * @param present true for present, false for absent
	 * @param number the number to start the search from (inclusive)
	 * @return the next present/absent number, or -1 if none
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public long next(long number, boolean present);
	
	/**
	 * Search the previous number present in the set.
	 * This method is equivalent to previous(number, true).
	 * @param number the number to start the search from (inclusive)
	 * @return the previous present number, or -1 if none
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public long previous(long number);
	
	/**
	 * Search the previous number present or absent in the set.
	 * @param present true for present, false for absent
	 * @param number the number to start the search from (inclusive)
	 * @return the previous present/absent number, or -1 if none
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public long previous(long number, boolean present);
	
	/**
	 * Get the first number of this set
	 * @return the first number, or -1 if any
	 */
	public long first();
	
	/**
	 * Get the last number of this set
	 * @return the last number, or -1 if any
	 */
	public long last();
	
	/**
	 * Get the first missing number of this set
	 * @return the first missing number, or -1 if any
	 */
	public long firstMissing();

	/**
	 * Get the last missing number of this set
	 * @return the last missing number, or -1 if any
	 */
	public long lastMissing();
	
	/**
	 * Get the segment that contains the associated number
	 * @param number the number
	 * @return the segment that contains the number
	 * @throws IndexOutOfBoundsException if number is incorrect
	 */
	public Segment getSegment(long number);
	
	/**
	 * Get the first segment of this set
	 * @return the first segment
	 */
	public Segment firstSegment();
	
	/**
	 * Get the last segment of this set
	 * @return the last segment
	 */
	public Segment lastSegment();
	
	/**
	 * Get the capacity of this set
	 * @return the capacity
	 */
	public long capacity();
	
	/**
	 * Clone this set
	 * @return the new instance of this set
	 */
	public SegmentedBitmap clone();
	
	/**
	 * Serialize this instance
	 * @param output the output
	 */
	public void serialize(NumberOutputStream output) throws IOException;
}
