package fr.rowlaxx.util.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class NumberStreamTest {

	@Test
	void test() throws IOException {
		long[] totest = {10, -5, 1500, -32048, 5325453, -3762073, Long.MAX_VALUE - Integer.MAX_VALUE, Long.MIN_VALUE / 2 + 28923};
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
		NumberOutputStream out = new NumberOutputStream(baos);
		
		for (int i = 0 ; i < totest.length ; i++)
			out.writeNumber(totest[i]);
		out.flush();
		
		System.out.println(Arrays.toString(baos.toByteArray()));
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		NumberInputStream in = new NumberInputStream(bais);
		
		for (int i = 0 ; i < totest.length ; i++)
			assertEquals(totest[i], in.unsafeReadNumber());
	}

}
