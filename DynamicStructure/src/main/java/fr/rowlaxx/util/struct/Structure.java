package fr.rowlaxx.util.struct;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.rowlaxx.util.array.ByteArray;
import fr.rowlaxx.util.collection.HashInterner;
import fr.rowlaxx.util.collection.Interner;

public class Structure {
	private static final int MAGIC = 0x53F863C2;
	private static final Interner<Structure> INTERNER = new HashInterner<>();
	
	public static Structure readFrom(DataInput in) throws IOException {
		if (in.readInt() != MAGIC)
			throw new IOException("Structure is corrupted : bad magic number");
		
		final int memberCount = in.readInt();
		final int size = in.readInt();
		final HashMap<String, Member<?>> temp = new HashMap<>(memberCount);
		
		Member<?> member;
		
		for (int i = 0 ; i < memberCount ; i++) {
			member = new Member<>(in);
			temp.put(member.getName(), member);
		}
		
		final Structure struct = new Structure(temp, size);
		
		if (in.readInt() != struct.hashCode())
			throw new IOException("Structure is corrupted : wrong hash code");
		
		return INTERNER.intern(struct);
	}
	
	static Structure create(HashMap<String, Member<?>> members, int size) {
		return INTERNER.intern(new Structure(members, size));
	}
	
	private final Map<String, Member<?>> members;
	private final int size;
	
	private transient final Map<String, Member<?>> nonNullableMembers;
	private transient final Map<String, Member<?>> nullableMembers;

	private Structure(HashMap<String, Member<?>> members, int size){
		this.members = Collections.unmodifiableMap(members);
		this.size = size;
		
		final Map<String, Member<?>> nnm = new HashMap<>(members.size() / 2);
		final Map<String, Member<?>> nm = new HashMap<>(members.size() / 2);
	
		members.forEach((name, member) ->  {
			if (member.isNullable())
				nm.put(name, member);
			else
				nnm.put(name, member);
		});
		
		this.nonNullableMembers = Collections.unmodifiableMap(members);
		this.nullableMembers = Collections.unmodifiableMap(nm);
	}
	
	public void writeTo(DataOutput out) throws IOException {
		out.writeInt(MAGIC);
		out.writeInt(members.size());
		out.writeInt(size);
		
		for (var member : members.values())
			member.writeTo(out);
		
		out.writeInt(hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
	
	@Override
	public int hashCode() {
		int result = 31 + size;

        for (Member<?> member : members.values())
            result = 31 * result + member.hashCode();

        return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append("Structure [size=")
		  .append(size)
		  .append(", members={\n");
		
		members.values().forEach(member ->
			sb.append("\t")
			  .append(member.getName())
			  .append(" : [")
			  .append(member)
			  .append("],\n"));
		
		sb.replace(sb.length() - 2, sb.length(), "}]");
		
		return sb.toString();
	}
	
	
	

	public int getItemSize() {
		return size;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Member<T> getMember(String memberName) {
		Member<T> member = (Member<T>) members.get(memberName);
		if (member == null)
			throw new NoSuchElementException("No member named \"" + memberName + "\" exists in this structure");
		return member;
	}
	
	public Map<String, Member<?>> getMembers() {
		return members;
	}
	
	Map<String, Member<?>> getNonNullableMembers() {
		return nonNullableMembers;
	}
	
	Map<String, Member<?>> getNullableMembers() {
		return nullableMembers;
	}
	

	
	public ItemBuilder createItem() {
		return new ItemBuilder(this);
	}
	
	public Item createItem(Map<String, Object> map) {
		for (String member : nonNullableMembers.keySet())
			if (map == null || map.get(member) == null)
				throw new IllegalArgumentException("The member \"" + member + "\" is non nullable and not given.");
	
		Item item = new Item(this, ByteArray.create(size));
		
		if (map != null)
			for (var entry : map.entrySet())
				item.set(entry.getKey(), entry.getValue());
		
		return item;
	}
	
	public Item createBlankItem() {
		return new Item(this, ByteArray.create(size));
	}
	
	public Item readItemFrom(ByteArray array, int offset, boolean copy) {
		return new Item(this, array.subarray(offset, size, copy));
	}
	
	public Item readItemFrom(InputStream is) throws IOException {
		return new Item(this, ByteArray.wrap(is.readNBytes(size)));
	}
}
