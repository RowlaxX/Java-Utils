package fr.rowlaxx.util.io;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class ChunkedByteArrayOutputStreamTest {

	@Test
	void test() {
		@SuppressWarnings("resource")
		ChunkedByteArrayOutputStream fbaos = new ChunkedByteArrayOutputStream(Short.MAX_VALUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(64);		
		
		long start1 = System.nanoTime();
		for (int i = 0 ; i < 10000000 ; i++)
			fbaos.write(i);
		System.out.println("Time FBAOS : " + (System.nanoTime() - start1) + "ns");
	
		
		long start2 = System.nanoTime();
		for (int i = 0 ; i < 10000000 ; i++)
			baos.write(i);
		System.out.println("Time  BAOS : " + (System.nanoTime() - start2) + "ns");
		
		
		

		
		byte[] b1 = fbaos.toByteArray();
		byte[] b2 = baos.toByteArray();
		System.out.println(b1.length);
		System.out.println(b2.length);
		
		Assert.assertArrayEquals(b1, b2);
	}

}
