package fr.rowlaxx.util.collection.bitmap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddTest {

	@Test
	void simpleAddStart() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(0);
		assertEquals(s.toString(), "0");
	}
	
	@Test
	void simpleAddMiddle() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(1);
		assertEquals(s.toString(), "1");
	}
	
	@Test
	void simpleAddEnd() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(99);
		assertEquals(s.toString(), "99");
	}

	@Test
	void simpleAddCapacity() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		assertThrows(IndexOutOfBoundsException.class, () -> s.add(100));
	}
	
	@Test
	void simpleAddNegativ() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		assertThrows(IndexOutOfBoundsException.class, () -> s.add(-1));
	}
	
	@Test 
	void simpleAddLeftStart() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(1);
		s.add(0);
		assertEquals(s.toString(), "[0-1]");
	}
	
	@Test 
	void simpleAddLeftMiddle() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(2);
		s.add(1);
		assertEquals(s.toString(), "[1-2]");
	}
	
	@Test 
	void simpleAddLeftEnd() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(99);
		s.add(98);
		assertEquals(s.toString(), "[98-99]");
	}
	
	@Test 
	void simpleAddRightStart() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(0);
		s.add(1);
		assertEquals(s.toString(), "[0-1]");
	}
	
	@Test 
	void simpleAddRightMiddle() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(1);
		s.add(2);
		assertEquals(s.toString(), "[1-2]");
	}
	
	@Test 
	void simpleAddRightEnd() {
		SegmentedBitmap s = SegmentedBitmap.create(100);
		s.add(98);
		s.add(99);
		assertEquals(s.toString(), "[98-99]");
	}
}
