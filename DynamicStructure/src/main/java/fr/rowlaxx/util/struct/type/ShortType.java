package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class ShortType implements MemberType<Number> {
	public static final byte IDENTIFIER = 0x02;
	public static final ShortType INSTANCE = new ShortType();
	
	private ShortType() {}
	
	@Override
	public int byteSize() {
		return Short.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Number value) {
		out.writeShort(value.shortValue());
	}

	@Override
	public Short readObjectFrom(ByteArrayReader in) {
		return in.readShort();
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
		return "short";
	}
}
