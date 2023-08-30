package fr.rowlaxx.util.struct.type;

import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public final class CharType implements MemberType<Character> {
	public static final byte IDENTIFIER = 0x03;
	public static final CharType INSTANCE = new CharType();
	
	private CharType() {}
	
	@Override
	public int byteSize() {
		return Character.BYTES;
	}

	@Override
	public void writeObjectTo(ByteArrayWriter out, Character value) {
		out.writeChar(value);
	}

	@Override
	public Character readObjectFrom(ByteArrayReader in) {
		return in.readChar();
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
		return "char";
	}
}
