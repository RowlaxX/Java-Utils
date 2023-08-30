package fr.rowlaxx.util.array;

import java.io.DataOutput;

/**
 * @author Theo
 * @since 1.0
 */
public final class ByteArrayWriter extends ByteArrayIO implements DataOutput {
	ByteArrayWriter(ByteArray array, int offset, int length) {
		super(array, offset, length);
	}
	
	@Override
	public void write(int b) {
		ensure(1);
		array.set(index++, (byte)b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) {
		ensure(len);
		array.set(index, b, off, len);
        index += len;
	}
	
	@Override
	public void write(byte[] b) {
		write(b, 0, b.length);
	}
	
	@Override
	public void writeBoolean(boolean v) {
		write(v ? 1 : 0);
	}

	@Override
	public void writeByte(int v) {
		write(v);
	}

	@Override
	public void writeShort(int v) {
		write(v >>> 8);
		write(v >>> 0);
	}

	@Override
	public void writeChar(int v) {
		write(v >>> 8);
		write(v >>> 0);
	}

	@Override
	public void writeInt(int v) {
		write(v >>> 24);
		write(v >>> 16);
		write(v >>> 8);
		write(v >>> 0);
	}

	@Override
	public void writeLong(long v) {
		write((byte)(v >>> 56));
		write((byte)(v >>> 48));
		write((byte)(v >>> 40));
		write((byte)(v >>> 32));
		write((byte)(v >>> 24));
		write((byte)(v >>> 16));
		write((byte)(v >>> 8));
		write((byte)(v >>> 0));
	}

	@Override
	public void writeFloat(float v) {
		writeInt(Float.floatToIntBits(v));
	}

	@Override
	public void writeDouble(double v) {
		writeLong(Double.doubleToLongBits(v));
	}

	@Override
	public void writeBytes(String s) {
		int len = s.length();
        for (int i = 0 ; i < len ; i++) {
            write((byte)s.charAt(i));
        }
	}

	@Override
	public void writeChars(String s) {
		int len = s.length();
        int v;
		for (int i = 0 ; i < len ; i++) {
        	v = s.charAt(i);
        	write(v >>> 8);
        	write(v >>> 0);
        }
	}

	@Override
	public void writeUTF(String str) {
		final int strlen = str.length();
        int utflen = strlen; // optimized for ASCII

        for (int i = 0; i < strlen; i++) {
            int c = str.charAt(i);
            if (c >= 0x80 || c == 0)
                utflen += (c >= 0x800) ? 2 : 1;
        }

        if (utflen > 65535 || /* overflow */ utflen < strlen)
            throw new IllegalStateException(tooLongMsg(str, utflen));

        final byte[] bytearr = new byte[utflen + 2];

        int count = 0;
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

        int i = 0;
        for (i = 0; i < strlen; i++) { // optimized for initial run of ASCII
            int c = str.charAt(i);
            if (c >= 0x80 || c == 0) break;
            bytearr[count++] = (byte) c;
        }

        for (; i < strlen; i++) {
            int c = str.charAt(i);
            if (c < 0x80 && c != 0) {
                bytearr[count++] = (byte) c;
            } else if (c >= 0x800) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
        }
        write(bytearr, 0, utflen + 2);
	}
	
    private static final String tooLongMsg(String s, int bits32) {
        int slen = s.length();
        String head = s.substring(0, 8);
        String tail = s.substring(slen - 8, slen);
        // handle int overflow with max 3x expansion
        long actualLength = (long)slen + Integer.toUnsignedLong(bits32 - slen);
        return "encoded string (" + head + "..." + tail + ") too long: "
            + actualLength + " bytes";
    }
}
