package fr.rowlaxx.util.struct.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class ByteArrayType implements MemberType<byte[]> {
	public static final byte IDENTIFIER = 0x09;
	
	private final int length;
	
	ByteArrayType(DataInput in) throws IOException {
		this.length = in.readInt();
		if (length < 0 || length >= Integer.MAX_VALUE - 4)
			throw new IllegalArgumentException("length is negativ or too big");
	}
	
	public ByteArrayType(int length) {
		if (length < 0 || length >= Integer.MAX_VALUE - 4)
			throw new IllegalArgumentException("length is negativ or too big");
		this.length = length;
	}

	@Override
	public int byteSize() {
		return 4 + length;
	}
	
	public int getLength() {
		return length;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, byte[] value) {
		out.writeInt(value.length);
		out.write(value);
	}

	@Override
	public byte[] readObjectFrom(ByteArrayReader in) {
		int length = in.readInt();
		byte[] array = new byte[length];
		in.readFully(array);
		return array;
	}

	@Override
	public void writeMemberTo(DataOutput out) throws IOException {
		out.writeByte(IDENTIFIER);
		out.writeInt(length);
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
		ByteArrayType other = (ByteArrayType) obj;
		return length == other.length;
	}

	@Override
	public String toString() {
		return "bytes(" + length + ")";
	}
}
