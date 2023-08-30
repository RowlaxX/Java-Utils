package fr.rowlaxx.util.array;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author Theo
 * @since 1.0
 */
class BufferByteArray extends AbstractByteArray {

	private final ByteBuffer buffer;
	
	BufferByteArray(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public int length() {
		return buffer.capacity();
	}

	@Override
	public byte get(int index) {
		return buffer.get(index);
	}

	@Override
	public void set(int index, byte value) {
		buffer.put(index, value);
	}

	@Override
	public void get(int index, byte[] b, int off, int len) {
		buffer.get(index, b, off, len);
	}

	@Override
	public void set(int index, byte[] b, int off, int len) {
		buffer.put(index, b, off, len);
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException {
		os.write(buffer.hasArray() ? buffer.array() : copyAll());
	}
	
	@Override
	public void writeTo(ByteArray array, int offset) {
		array.set(offset, buffer.hasArray() ? buffer.array() : copyAll(), 0, length());
	}
}
