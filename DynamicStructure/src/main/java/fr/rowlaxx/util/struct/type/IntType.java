package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class IntType implements MemberType<Number> {
	public static final byte IDENTIFIER = 0x04;
	public static final IntType INSTANCE = new IntType();
	
	private IntType() {}
	
	@Override
	public int byteSize() {
		return Integer.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Number value) {
		out.writeInt(value.intValue());
	}

	@Override
	public Integer readObjectFrom(ByteArrayReader in) {
		return in.readInt();
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
		return "int";
	}
}
