package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class DoubleType implements MemberType<Number> {
	public static final byte IDENTIFIER = 0x07;
	public static final DoubleType INSTANCE = new DoubleType();
	
	private DoubleType() {}
	
	@Override
	public int byteSize() {
		return Double.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Number value) {
		out.writeDouble(value.doubleValue());
	}

	@Override
	public Double readObjectFrom(ByteArrayReader in) {
		return in.readDouble();
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
		return "double";
	}
}
