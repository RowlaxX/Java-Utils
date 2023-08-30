package fr.rowlaxx.util.io;

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Theo
 * @since 1.0
 */
public class NumberOutputStream extends FilterOutputStream implements DataOutput {
	private final long[] temp = new long[3];
	private final byte[] type = new byte[3];
	private byte index = 0;
	
	public NumberOutputStream(OutputStream out) {
		super(out);
	}
	
	private void write(long l, int bytes) throws IOException {
		for (byte i = (byte)((bytes - 1) * 8) ; i >= 0 ; i -= 8)
			out.write( (byte)(l >>> i) );
	}
	
	private static byte numberToType(long l) {
		if (l >= Byte.MIN_VALUE && l <= Byte.MAX_VALUE)
			return 0;
		else if (l >= Short.MIN_VALUE && l <= Short.MAX_VALUE)
			return 1;
		else if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE)
			return 2;
		return 3;
	}
	
	private void writeNumber(long l, byte lType) throws IOException {
		if (index == 3) 
			flush(false);
		
		temp[index] = l;
		type[index] = lType;
		index++;
	}
	
	
	public void writeNumber(long l) throws IOException {
		writeNumber(l, numberToType(l));
	}
	
	
	private void flush(boolean end) throws IOException {
		if (index == 0)
			return;
		
		byte descriptor = end ? (byte)(index << 6) : 0;
		for (byte i = 0 ; i < index ; i++)
			descriptor += type[i] << (4 - 2*i);
		
		out.write(descriptor);
		
		for (byte i = 0 ; i < index ; i++)
			write(temp[i], 1 << type[i]);
		
		index = 0;
	}
	
	public void flush() throws IOException {
		flush(true);
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		writeNumber(v ? 1 : 0, (byte)0);		
	}

	@Override
	public void writeByte(int v) throws IOException {
		writeNumber(v, (byte)0);		
	}

	@Override
	public void writeShort(int v) throws IOException {
		writeNumber(v, (byte)1);
	}

	@Override
	public void writeChar(int v) throws IOException {
		writeNumber(v, (byte)1);
	}

	@Override
	public void writeInt(int v) throws IOException {
		writeNumber(v, (byte)2);
	}

	@Override
	public void writeLong(long v) throws IOException {
		writeNumber(v, (byte)3);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeNumber(Float.floatToIntBits(v), (byte)2);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeNumber(Double.doubleToLongBits(v), (byte)4);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		throw new UnsupportedOperationException("NumberOutputStream do not support writeBytes(String s) method.");
	}

	@Override
	public void writeChars(String s) throws IOException {
		throw new UnsupportedOperationException("NumberOutputStream do not support writeChars(String s) method.");		
	}

	@Override
	public void writeUTF(String s) throws IOException {
		throw new UnsupportedOperationException("NumberOutputStream do not support writeUTF(String s) method.");
	}
}
