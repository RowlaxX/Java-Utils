package fr.rowlaxx.util.array;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Theo
 * @since 1.0
 */
class PrimitivByteArray extends  AbstractByteArray {

	private final byte[] data;
	
	PrimitivByteArray(byte[] data) {
		this.data = Objects.requireNonNull(data);
	}
	
	@Override
	public int length() {
		return data.length;
	}

	@Override
	public byte get(int index) {
		return data[index];
	}

	@Override
	public void set(int index, byte value) {
		data[index] = value;
	}

	@Override
	public void get(int index, byte[] b, int off, int len) {
        System.arraycopy(data, index, b, off, len);
	}

	@Override
	public void set(int index, byte[] b, int off, int len) {
		System.arraycopy(b, off, data, index, len);
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException {
		os.write(data);
	}
	
	@Override
	public void writeTo(ByteArray array, int offset) {
		array.set(offset, data, 0, data.length);
	}
}
