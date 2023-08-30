package fr.rowlaxx.util.collection.bitmap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CreationTest {

	@Test
	public void normal() {
		SegmentedBitmap.create(100);
	}
	
	@Test
	public void zero() {
		assertThrows(IllegalArgumentException.class, () -> SegmentedBitmap.create(0));
	}
	
	@Test
	public void negativ() {
		assertThrows(IllegalArgumentException.class, () -> SegmentedBitmap.create(-5));
	}
	
	@Test
	public void max() {
		SegmentedBitmap.create();
	}

}
