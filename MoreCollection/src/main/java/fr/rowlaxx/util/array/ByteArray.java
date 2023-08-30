package fr.rowlaxx.util.array;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author Theo
 * @since 1.0
 */
public interface ByteArray {

	public static ByteArray wrap(byte[] data) {
		return new PrimitivByteArray(data);
	}
	
	public static ByteArray wrap(ByteBuffer data) {
		return new BufferByteArray(data);
	}
	
	public static ByteArray create(int size) {
		return wrap(new byte[size]);
	}
	
	public int length();
	public byte[] copyFromSize(int offset, int length);
	public byte[] copyAll();
	
	public byte get(int index);
	public void get(int index, byte[] b, int off, int len);
	public void get(int index, byte[] b);
	public ByteArrayReader read(int index, int length);
	public ByteArrayReader read();
	
	public void set(int index, byte[] b, int off, int len);
	public void set(int index, byte[] b);
	public void set(int index, byte value);
	public ByteArrayWriter write(int index, int length);
	public ByteArrayWriter write();
	
	public void writeTo(OutputStream os) throws IOException;
	public void writeTo(ByteArray array, int offset);
	
	public ByteArray subarray(int offset, int length, boolean copy);
}
