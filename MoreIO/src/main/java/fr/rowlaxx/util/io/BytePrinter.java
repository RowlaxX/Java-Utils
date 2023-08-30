package fr.rowlaxx.util.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Theo
 * @since 1.0
 */
public class BytePrinter extends OutputStream {
	private final PrintStream out;
	
	public BytePrinter(PrintStream out) {
		this.out = Objects.requireNonNull(out);
	}
	
	public void print(int b) {
		write(b);
	}
	
	public void print(byte[] bytes) {
		for (byte b : bytes)
			print(b);
	}
	
	@Override
	public final void write(int b) {
		final StringBuilder sb = new StringBuilder(21);
		b &= 255;
		
		for (byte i = 0 ; i < 8 ; i++)
			sb.append((b << i & 0x80) == 0 ? '0' : '1');
		
		sb.append('\t');
		sb.append(hex[b >>> 4]);
		sb.append(hex[b & 15]);
		
		sb.append('\t');
		sb.append(b);
		if (b < 10)
			sb.append(' ');
		if (b < 100)
			sb.append(' ');
		
		sb.append('\t');
		if (b > 32 && b < 127)
			sb.append((char)b);
		else if (b == 32)
			sb.append("SPA");
		else if (b == 127)
			sb.append("DEL");
		else
			sb.append("NULL");
			
		out.println(sb.toString());
	}
	
	private static char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
}
