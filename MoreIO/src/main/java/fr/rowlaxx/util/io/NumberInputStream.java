package fr.rowlaxx.util.io;

import java.io.DataInput;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Theo
 * @since 1.0
 */
public class NumberInputStream extends FilterInputStream implements DataInput {
	private final byte[] size = new byte[3];
	private byte type;

	private byte count = 0;
	private byte index = 0;
	
	public NumberInputStream(InputStream in) {
		super(in);
	}
	
	private void readDescriptor() throws IOException {
		int descriptor = in.read();
		type    = (byte) ((descriptor & 0b11000000) >> 6);
		size[0] = (byte)(1 << ((descriptor & 0b00110000) >> 4) );
		size[1] = (byte)(1 << ((descriptor & 0b00001100) >> 2) );
		size[2] = (byte)(1 << ((descriptor & 0b00000011) >> 0) );
		count = type == 0 ? 3 : type; 
		index = 0;
	}
	
	private long read(byte bytes) throws IOException {
		byte bits = (byte)(bytes * 8);
		
		long sum = (long)((byte)in.read()) << (bits - 8);
		
		for (byte b = (byte)(bits - 16) ; b >= 0 ; b -= 8 )
			sum += (long)(in.read() & 255) << b;
		
		return sum;
	}
	
	public final long readNumber(int byteCount) throws IOException {
		if (index >= count)
			readDescriptor();
		byte bytes = size[index];
		if (bytes != byteCount)
			throw new IllegalStateException("The next number is " + bytes + " byte(s) long instead of " + byteCount + ".");
		index++;
		return read(bytes);
	}
	
	public final long unsafeReadNumber() throws IOException {
		if (index >= count)
			readDescriptor();
		
		return read(size[index++]);
	}
	
	@Override
	public boolean readBoolean() throws IOException {
		return readNumber(1) != 0;
	}
	
	public boolean unsafeReadBoolean() throws IOException {
		return unsafeReadNumber() != 0;
	}
	
	@Override
	public byte readByte() throws IOException {
		return (byte)readNumber(1);
	}
	
	public byte unsafeReadByte() throws IOException {
		return (byte) unsafeReadNumber();
	}
	
	@Override
	public char readChar() throws IOException {
		return (char)readNumber(2);
	}
	
	public char unsafeReadChar() throws IOException {
		return (char) unsafeReadNumber();
	}
	
	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readNumber(8));
	}
	
	public double unsafeReadDouble() throws IOException {
		return Double.longBitsToDouble(unsafeReadNumber());
	}
	
	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat((int)readNumber(4));
	}
	
	public float unsafeReadFloat() throws IOException {
		return Float.intBitsToFloat((int)unsafeReadNumber());
	}
	
	@Override
	public int readInt() throws IOException {
		return (int)readNumber(4);
	}
	
	public int unsafeReadInt() throws IOException {
		return (int) unsafeReadInt();
	}
	
	@Override
	public long readLong() throws IOException {
		return readNumber(8);
	}
	
	public long unsafeReadLong() throws IOException {
		return unsafeReadNumber();
	}
	
	@Override
	public short readShort() throws IOException {
		return (short)readNumber(2);
	}
	
	public short unsafeReadShort() throws IOException {
		return (short)unsafeReadNumber();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return (int)(readNumber(1) & 0xFF);
	}
	
	public int unsafeReadUnsignedByte() throws IOException {
		return (int)(unsafeReadNumber() & 0xFF);
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return (int)(readNumber(2) & 0xFFFF);
	}
	
	public int unsafeReadUnsignedShort() throws IOException {
		return (int)(unsafeReadNumber() & 0xFFFF);
	}

	@Override
	public String readLine() throws IOException {
		throw new UnsupportedOperationException("NumberInputStream do not support readLine() method.");
	}

	@Override
	public String readUTF() throws IOException {
		throw new UnsupportedOperationException("NumberInputStream do not support readUTF() method.");
	}
	
	@Override
	public void readFully(byte[] b) throws IOException {
		throw new UnsupportedOperationException("NumberInputStream do not support readFully(byte[] b) method.");
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		throw new UnsupportedOperationException("NumberInputStream do not support readFully(byte[] b, int off, int len) method.");
	}

	@Override
	public int skipBytes(int n) throws IOException {
		throw new UnsupportedOperationException("NumberInputStream do not support skipBytes(int n) method.");
	}
}
