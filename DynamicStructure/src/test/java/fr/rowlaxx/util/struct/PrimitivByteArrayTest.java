package fr.rowlaxx.util.struct;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import fr.rowlaxx.util.array.ByteArray;
import fr.rowlaxx.util.io.BytePrinter;
import fr.rowlaxx.util.struct.Item;
import fr.rowlaxx.util.struct.Structure;
import fr.rowlaxx.util.struct.StructureBuilder;

class PrimitivByteArrayTest {

	@Test
	void test() throws IOException {
		Structure personStruct = StructureBuilder.newBuilder()
				.putString("name", 32)
				.putInt("age")
				.putBoolean("male")
				.putDouble("height")
				.putByte("favoriteDigit")
				.build();
		
		System.out.println(personStruct);
		
		DataOutputStream dos = new DataOutputStream(new BytePrinter(System.out));
		personStruct.writeTo(dos);
	}
	
}
