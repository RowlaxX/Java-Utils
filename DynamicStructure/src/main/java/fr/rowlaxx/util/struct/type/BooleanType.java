package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class BooleanType implements MemberType<Boolean> {
	public static final byte IDENTIFIER = 0x01;
	public static final BooleanType INSTANCE = new BooleanType();
	
	private BooleanType() {}
	
	@Override
	public int byteSize() {
		return 1;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Boolean value) {
		out.writeBoolean(value);
	}

	@Override
	public Boolean readObjectFrom(ByteArrayReader in) {
		return in.readBoolean();
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
		return this == obj;
	}
	
	@Override
	public String toString() {
		return "boolean";
	}
}
