package fr.rowlaxx.util.array;

/**
 * @author Theo
 * @since 1.0
 */
abstract class AbstractByteArray implements ByteArray {

	@Override
	public int hashCode() {
		int result = 1;
        int len = length();
        int e;
        
        for (int i = 0 ; i < len ; i++) {
            e = get(i);
            result = 31 * result + (int)(e ^ (e >>> 32));
        }

        return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof ByteArray other))
			return false;
		
		int len = other.length();
		
		if (length() != len)
			return false;
		
		for (int i = 0 ; i < len ; i++)
			if (get(i) != other.get(i))
				return false;
		return true;
	}
	
	@Override
	public String toString() {
		int len = length();
        if (len == 0)
            return "[]";
        
        int to = len - 1;

        StringBuilder b = new StringBuilder(len * 4 + 2);
        b.append('[');
        
        for (int i = 0; i < to ; i++)
            b.append(get(i))
             .append(", ");
        
        b.append(get(to))
         .append(']');
        
        return b.toString();
	}
	
	@Override
	public byte[] copyAll() {
		byte[] b = new byte[length()];
		get(0, b, 0, b.length);
		return b;
	}
	
	@Override
	public byte[] copyFromSize(int offset, int length) {
		byte[] b = new byte[length];
		get(offset, b, 0, length);
		return b;
	}
	
	@Override
	public ByteArray subarray(int offset, int length, boolean copy) {
		if (copy)
			return new PrimitivByteArray(copyFromSize(offset, length));
		if (offset == 0 && length == length())
			return this;
		return new SubByteArray(this, offset, length);
	}
	
	@Override
	public void get(int index, byte[] b) {
		get(index, b, 0, b.length);
	}
	
	@Override
	public void set(int index, byte[] b) {
		set(index, b, 0, b.length);
	}
	
	@Override
	public ByteArrayReader read() {
		return new ByteArrayReader(this, 0, length());
	}
	
	@Override
	public ByteArrayReader read(int index, int length) {
		return new ByteArrayReader(this, index, length);
	}
	
	@Override
	public ByteArrayWriter write() {
		return new ByteArrayWriter(this, 0, length());
	}
	
	@Override
	public ByteArrayWriter write(int index, int length) {
		return new ByteArrayWriter(this, index, length);
	}
}
