package fr.rowlaxx.util.array;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Theo
 * @since 1.0
 */
class SubByteArray extends AbstractByteArray {

	private final ByteArray array;
	private final int offset;
	private final int length;
	
	SubByteArray(ByteArray array, int offset, int length) {
		Objects.checkFromIndexSize(offset, length, array.length());
		this.array = array;
		this.offset = offset;
		this.length = length;
	}
	
	@Override
	public int length() {
		return length;
	}

	@Override
	public byte get(int index) {
		Objects.checkIndex(index, length);
		return array.get(offset + index);
	}

	@Override
	public void set(int index, byte value) {
		Objects.checkIndex(index, length);
		array.set(offset + index, value);
	}

	@Override
	public void get(int index, byte[] b, int off, int len) {
		Objects.checkFromIndexSize(off, len, length);
		array.get(offset + index, b, off, len);
	}

	@Override
	public void set(int index, byte[] b, int off, int len) {
		Objects.checkFromIndexSize(off, len, length);
		array.set(offset + index, b, off, len);
	}
	
	@Override
	public void writeTo(ByteArray array, int offset) {
		byte[] data = array.copyAll();
		array.set(offset, data, 0, data.length);
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException {
		os.write(copyAll());
	}
}
