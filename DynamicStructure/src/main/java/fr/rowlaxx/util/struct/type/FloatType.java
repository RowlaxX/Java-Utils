package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class FloatType implements MemberType<Number> {
	public static final byte IDENTIFIER = 0x05;
	public static final FloatType INSTANCE = new FloatType();
	
	private FloatType() {}
	
	@Override
	public int byteSize() {
		return Float.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Number value) {
		out.writeFloat(value.floatValue());
	}

	@Override
	public Float readObjectFrom(ByteArrayReader in) {
		return in.readFloat();
	}

	@Override
	public void writeMemberTo(DataOutput out) throws IOException {
		out.writeByte(IDENTIFIER);
	}
	
	@Override
	public int hashCode() {
		return IDENTIFIER;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}
	
	@Override
	public String toString() {
		return "float";
	}
}