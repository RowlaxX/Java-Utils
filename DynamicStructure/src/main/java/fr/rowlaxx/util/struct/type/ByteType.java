package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class ByteType implements MemberType<Number> {
	public static final byte IDENTIFIER = 0x00;
	public static final ByteType INSTANCE = new ByteType();
	
	private ByteType() {}
	
	@Override
	public int byteSize() {
		return Byte.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Number value) {
		out.writeByte(value.byteValue());
	}

	@Override
	public Byte readObjectFrom(ByteArrayReader in) {
		return in.readByte();
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
		return "byte";
	}
}
