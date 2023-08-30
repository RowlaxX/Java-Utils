package fr.rowlaxx.util.struct;

import java.util.HashMap;

import fr.rowlaxx.util.struct.type.BooleanType;
import fr.rowlaxx.util.struct.type.ByteArrayType;
import fr.rowlaxx.util.struct.type.ByteType;
import fr.rowlaxx.util.struct.type.DoubleType;
import fr.rowlaxx.util.struct.type.FloatType;
import fr.rowlaxx.util.struct.type.IntType;
import fr.rowlaxx.util.struct.type.LongType;
import fr.rowlaxx.util.struct.type.MemberType;
import fr.rowlaxx.util.struct.type.ShortType;
import fr.rowlaxx.util.struct.type.StringType;

public final class StructureBuilder {
	public static StructureBuilder newBuilder() {
		return new StructureBuilder();
	}
	
	private HashMap<String, Member<?>> types = new HashMap<>();
	private int offset = 0;
	private Structure result = null;
	
	public Structure build() {
		if (result == null) {
			result = Structure.create(types, offset);
			types = null;
		}
		
		return result;
	}
	
	public StructureBuilder put(String name, MemberType<?> type, boolean nullable) {
		if (result != null)
			throw new IllegalStateException("This builder has been finished");
		if (types.containsKey(name))
			throw new IllegalStateException("This builder already contains the member name \"" + name + "\"");
		
		Member<?> m = new Member<>(name, nullable, type, offset);
		offset += m.getSize();
		types.put(name, m);
		return this;
	}
	
	public StructureBuilder put(String name, MemberType<?> type) {
		return put(name, type, false);
	}
	
	public StructureBuilder putInt(String name, boolean nullable) {
		return put(name, IntType.INSTANCE, nullable);
	}
	
	public StructureBuilder putInt(String name) {
		return put(name, IntType.INSTANCE, false);
	}
	
	public StructureBuilder putByte(String name, boolean nullable) {
		return put(name, ByteType.INSTANCE, nullable);
	}
	
	public StructureBuilder putByte(String name) {
		return put(name, ByteType.INSTANCE, false);
	}
	
	public StructureBuilder putBoolean(String name, boolean nullable) {
		return put(name, BooleanType.INSTANCE, nullable);
	}
	
	public StructureBuilder putBoolean(String name) {
		return put(name, BooleanType.INSTANCE, false);
	}
	
	public StructureBuilder putShort(String name, boolean nullable) {
		return put(name, ShortType.INSTANCE, nullable);
	}
	
	public StructureBuilder putShort(String name) {
		return put(name, ShortType.INSTANCE, false);
	}
	
	public StructureBuilder putFloat(String name, boolean nullable) {
		return put(name, FloatType.INSTANCE, nullable);
	}
	
	public StructureBuilder putFloat(String name) {
		return put(name, FloatType.INSTANCE, false);
	}
	
	public StructureBuilder putLong(String name, boolean nullable) {
		return put(name, LongType.INSTANCE, nullable);
	}
	
	public StructureBuilder putLong(String name) {
		return put(name, LongType.INSTANCE, false);
	}
	
	public StructureBuilder putDouble(String name, boolean nullable) {
		return put(name, DoubleType.INSTANCE, nullable);
	}
	
	public StructureBuilder putDouble(String name) {
		return put(name, DoubleType.INSTANCE, false);
	}
	
	public StructureBuilder putArray(String name, int size, boolean nullable) {
		return put(name, new ByteArrayType(size), nullable);
	}
	
	public StructureBuilder putArray(String name, int size) {
		return put(name, new ByteArrayType(size), false);
	}
	
	public StructureBuilder putString(String name, int size, boolean nullable) {
		return put(name, new StringType(size), nullable);
	}
	
	public StructureBuilder putString(String name, int size) {
		return put(name, new StringType(size), false);
	}
}
