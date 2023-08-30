package fr.rowlaxx.util.collection.bitmap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.rowlaxx.util.io.BytePrinter;
import fr.rowlaxx.util.io.NumberInputStream;
import fr.rowlaxx.util.io.NumberOutputStream;

class SerializationTest {

	@Test
	void test() throws IOException {
		SegmentedBitmap s = SegmentedBitmap.create();
		s.add(0);
		s.add(1);
		s.add(3);
		s.addAll(5, 50);
		s.removeAll(20,25);
		
		BytePrinter printer = new BytePrinter(System.out);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		s.serialize(new NumberOutputStream(baos));
		baos.writeTo(printer);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		SegmentedBitmap s1 = SegmentedBitmap.from(new NumberInputStream(bais));
		System.out.println(s);
		System.out.println(s1);
		
		assertEquals(s, s1);
	}

}
