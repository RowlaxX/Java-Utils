package fr.rowlaxx.util.array;

import java.io.DataInput;

/**
 * @author Theo
 * @since 1.0
 */
public final class ByteArrayReader extends ByteArrayIO implements DataInput {
	ByteArrayReader(ByteArray array, int index, int length) {
		super(array, index, length);
	}
	
	public int read() {
		ensure(1);
		return array.get(index++) & 255;
	}
	
	@Override
	public void readFully(byte[] b) {
		readFully(b, 0, b.length);
	}
	
	@Override
	public void readFully(byte[] b, int off, int len) {
		ensure(len);
		array.get(index, b, off, len);
        index += len;
	}
	
	@Override
	public boolean readBoolean() {
		return read() != 0;
	}

	@Override
	public byte readByte() {
		return (byte) read();
	}

	@Override
	public int readUnsignedByte() {
		return read();
	}

	@Override
	public short readShort() {
		ensure(2);
		return (short)((read() << 8) + (read() << 0));
	}

	@Override
	public int readUnsignedShort() {
		ensure(2);
		return (read() << 8) + (read() << 0);
	}

	@Override
	public char readChar() {
		ensure(2);
		return (char)((read() << 8) + (read() << 0));
	}

	@Override
	public int readInt() {
		ensure(4);
		return ((read() << 24) + (read() << 16) + (read() << 8) + (read() << 0));
	}

	@Override
	public long readLong() {
		ensure(8);
		return (((long)read() << 56) +
                ((long)(read() & 255) << 48) +
                ((long)(read()& 255) << 40) +
                ((long)(read() & 255) << 32) +
                ((long)(read() & 255) << 24) +
                ((read() & 255) << 16) +
                ((read() & 255) <<  8) +
                ((read() & 255) <<  0));
	}

	@Override
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	@Deprecated
	@Override
	public String readLine() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String readUTF() {
		int utflen = readUnsignedShort();
        byte[] bytearr = new byte[utflen];
        char[] chararr = new char[utflen];
        int c, char2, char3;
        int count = 0;
        int chararr_count=0;

        readFully(bytearr, 0, utflen);

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++]=(char)c;
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            switch (c >> 4) {
                case 0, 1, 2, 3, 4, 5, 6, 7 -> {
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++]=(char)c;
                }
                case 12, 13 -> {
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new IllegalStateException(
                            "malformed input: partial character at end");
                    char2 = (int) bytearr[count-1];
                    if ((char2 & 0xC0) != 0x80)
                        throw new IllegalStateException(
                            "malformed input around byte " + count);
                    chararr[chararr_count++]=(char)(((c & 0x1F) << 6) |
                                                    (char2 & 0x3F));
                }
                case 14 -> {
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new IllegalStateException(
                            "malformed input: partial character at end");
                    char2 = (int) bytearr[count-2];
                    char3 = (int) bytearr[count-1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new IllegalStateException(
                            "malformed input around byte " + (count-1));
                    chararr[chararr_count++]=(char)(((c     & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0));
                }
                default ->
                    /* 10xx xxxx,  1111 xxxx */
                    throw new IllegalStateException(
                        "malformed input around byte " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new String(chararr, 0, chararr_count);
	}
}
