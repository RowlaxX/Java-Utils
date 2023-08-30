package fr.rowlaxx.util.io;

import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Theo
 * @since 1.0
 */
public class ChunkedByteArrayOutputStream extends OutputStream {
	private static record Chunk(
		Chunk previous,
		byte[] data,
		int offset,
		int length) {
		
		public Chunk {
			Objects.checkFromIndexSize(offset, length, data.length);
		}
	}
	
	private static final int DEFAULT_CHUNK_SIZE = 512;
	
	private final int chunkSize;
	
	private Chunk chunk = null;
	private byte[] data;
	private int index = 0;
	private int size = 0;
	
	
	public ChunkedByteArrayOutputStream(int chunkSize) {
		if (chunkSize <= 0)
			throw new IllegalArgumentException("chunkSize must be positiv");
		this.chunkSize = chunkSize;
		this.data = new byte[chunkSize];
		this.size = 0;
		this.index = 0;
	}
	
	public ChunkedByteArrayOutputStream() {
		this(DEFAULT_CHUNK_SIZE);
	}
	
	@Override
	public void close() {}
	
	private void sendDataToChunk() {
		if (index == 0)
			return;
		chunk = new Chunk(chunk, data, 0, index);
		data = new byte[chunkSize];
		index = 0;
	}
	
	@Override
	public void write(int b) {
		if (index == chunkSize)
			sendDataToChunk();

		data[index++] = (byte) b;
		size++;
	}
	
	
	@Override
	public void write(byte[] b, int off, int len) {
		writeUnsafe(b.clone(), off, len);
	}
	
	
	public void writeUnsafe(byte[] b, int off, int len) {
		sendDataToChunk();
		chunk = new Chunk(chunk, b.clone(), off, len);
		size += len;
	}
	
	
	private byte[] build() {
		byte[] result = new byte[size];
		
		int offset = size;
		int len;
		
		for (Chunk c = chunk ; c != null ; c = c.previous) {
			len = c.length;
			System.arraycopy(c.data, c.offset, result, offset -= len, len);
		}
		
		return result;
	}
	
	
	public byte[] toByteArray() {
		return toByteArrayUnsafe().clone();
	}
	
	
	public byte[] toByteArrayUnsafe() {
		if (size == 0)
			return new byte[0];
		
		sendDataToChunk();
		if (chunk.previous == null && chunk.length == chunk.data.length)
			return chunk.data;
		
		byte[] builded = build();
		chunk = new Chunk(null, builded, 0, builded.length);
		
		return builded;
	}
}
