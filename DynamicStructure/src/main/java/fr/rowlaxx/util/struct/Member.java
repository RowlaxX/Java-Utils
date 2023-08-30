package fr.rowlaxx.util.struct;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import fr.rowlaxx.util.array.ByteArray;
import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;
import fr.rowlaxx.util.struct.type.MemberType;

public class Member<T> {
	private static final short MAGIC = 0x35C2;
	
	private final String name;
	private final boolean nullable;
	private final MemberType<T> type;
	private final int offset;
	private transient final int size;

	Member(String name, boolean nullable, MemberType<T> type, int offset) {
		this.name = name;
		this.nullable = nullable;
		this.offset = offset;
		this.type = type;
		this.size = type.byteSize() + (nullable ? 1 : 0);
	}
	
	Member(DataInput in) throws IOException {
		if (in.readShort() != MAGIC)
			throw new IOException("Member is corrupted : bad magic number");
		
		this.name = in.readUTF();
		this.nullable = in.readBoolean();
		this.offset = in.readInt();
		this.type = MemberType.readMemberFrom(in);
		
		if (in.readShort() != (short)hashCode())
			throw new IOException("Member is corrupted : wrong hash code");
		
		this.size = type.byteSize() + (nullable ? 1 : 0);
	}
	
	void writeTo(DataOutput out) throws IOException {
		out.writeShort(MAGIC);
		out.writeUTF(name);
		out.writeBoolean(nullable);
		out.writeInt(offset);
		type.writeMemberTo(out);
		out.writeShort(hashCode());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, nullable, offset, type);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member<?> other = (Member<?>) obj;
		return  nullable == other.nullable && 
				offset == other.offset && 
				Objects.equals(type, other.type) &&
				Objects.equals(name, other.name);
	}
	
	@Override
	public String toString() {
		return "offset=" + offset + ", size=" + size + ", type=" + type + ", nullable=" + nullable;
	}

	public boolean isNullable() {
		return nullable;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getSize() {
		return size;
	}
	
	public MemberType<T> getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	T getFrom(ByteArray data) {
		ByteArrayReader reader = data.read(offset, size);
		
		if (nullable && !reader.readBoolean())
			return null;
		
		return type.readObjectFrom(reader);
	}
	
	void setTo(ByteArray data, T value) {
		ByteArrayWriter writer = data.write(offset, size);
		
		if (nullable)
			if (value == null)
				writer.writeBoolean(false);
			else {
				writer.writeBoolean(true);
				type.writeObjectTo(writer, value);
			}
		else if (value == null)
			throw new IllegalArgumentException("The member \"" + name + "\" do not accept null values");
		else
			type.writeObjectTo(writer, value);
	}
}
