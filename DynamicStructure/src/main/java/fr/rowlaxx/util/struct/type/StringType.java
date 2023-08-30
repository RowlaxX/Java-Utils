package fr.rowlaxx.util.struct.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class StringType implements MemberType<String> {
	public static final byte IDENTIFIER = 0x08;
	
	private final short length;
	
	StringType(DataInput in) throws IOException {
		this.length = in.readShort();
	}
	
	public StringType(int length) {
		if (length < 0 || length > 0xFFFF)
			throw new IllegalArgumentException();
		this.length = (short)length;
	}

	@Override
	public int byteSize() {
		return 2 + getLength();
	}

	public int getLength() {
		return length & 0xFFFF;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, String value) {
		byte[] array = value.getBytes();
		
		if (array.length > 0xFFFF)
			throw new IllegalArgumentException("String is too big");
		
		out.writeShort(array.length);
		out.write(array);
	}

	@Override
	public String readObjectFrom(ByteArrayReader in) {
		int length = in.readUnsignedShort();
		byte[] array = new byte[length];
		in.readFully(array);
		
		return new String(array);
	}

	@Override
	public void writeMemberTo(DataOutput out) throws IOException {
		out.writeByte(IDENTIFIER);
		out.writeShort(length);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(IDENTIFIER, length);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringType other = (StringType) obj;
		return length == other.length;
	}

	@Override
	public String toString() {
		return "string(" + length + ")";
	}
	
	
}
