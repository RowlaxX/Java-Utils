package fr.rowlaxx.util.struct.type;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import fr.rowlaxx.util.array.ByteArrayReader;
import fr.rowlaxx.util.array.ByteArrayWriter;

public interface MemberType<T> {

	@SuppressWarnings("unchecked")
	public static <T> MemberType<T> readMemberFrom(DataInput in) throws IOException {
		final byte identifier = in.readByte();
		
		return (MemberType<T>) switch (identifier) {
			case ByteType.IDENTIFIER 			-> ByteType.INSTANCE;
			case BooleanType.IDENTIFIER 		-> BooleanType.INSTANCE;
			case ShortType.IDENTIFIER			-> ShortType.INSTANCE;
			case CharType.IDENTIFIER			-> CharType.INSTANCE;
			case IntType.IDENTIFIER				-> IntType.INSTANCE;
			case FloatType.IDENTIFIER			-> FloatType.INSTANCE;
			case LongType.IDENTIFIER			-> LongType.INSTANCE;
			case DoubleType.IDENTIFIER			-> DoubleType.INSTANCE;
			case StringType.IDENTIFIER			-> new StringType(in);
			case ByteArrayType.IDENTIFIER		-> new ByteArrayType(in);
			default -> throw new IllegalArgumentException("Unexpected value: " + identifier);
		};
	}
	
	public int byteSize();
	
	public void writeObjectTo(ByteArrayWriter b, T value);
	public T readObjectFrom(ByteArrayReader b);
	
	public void writeMemberTo(DataOutput out) throws IOException;
	
}
