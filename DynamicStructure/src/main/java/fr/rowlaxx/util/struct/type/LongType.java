package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class LongType implements MemberType<Number> {
	public static final byte IDENTIFIER = 0x06;
	public static final LongType INSTANCE = new LongType();
	
	private LongType() {}
	
	@Override
	public int byteSize() {
		return Long.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Number value) {
		out.writeLong(value.longValue());
	}

	@Override
	public Long readObjectFrom(ByteArrayReader in) {
		return in.readLong();
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
		return "long";
	}
}