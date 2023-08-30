package fr.rowlaxx.util.struct;

import java.util.HashMap;
import java.util.Map;

import fr.rowlaxx.util.array.ByteArray;

public class Item implements Cloneable {

	private final Structure struct;
	private final ByteArray data;
	
	Item(Structure struct, ByteArray data){
		if (data.length() != struct.getItemSize())
			throw new IllegalArgumentException("data length must be " + struct.getItemSize());
		
		this.data = data;
		this.struct = struct;
	}
	
	public Item clone() {
		byte[] array = data.copyAll();
		return new Item(struct, ByteArray.wrap(array));
	}
	
	public <T> T get(String memberName) {
		Member<T> member = struct.getMember(memberName);
		return member.getFrom(data);
	}
	
	public <T> void set(String memberName, T value) {
		Member<T> member = struct.getMember(memberName);
		member.setTo(data, value);
	}
	
	public Map<String, Object> toMap() {
		Map<String, Member<?>> members = struct.getMembers();
		Map<String, Object> fields = new HashMap<>(members.size());
		members.forEach( (name, member) -> fields.put(name, member.getFrom(data)));
		return fields;
	}
	
	@Override
	public String toString() {
        final Map<String, Member<?>> members = struct.getMembers();
        if (members.size() == 0)
        	return "Item[]";
        
        StringBuilder sb = new StringBuilder(64);
        sb.append("Item[");
        
        for (var member : members.values())
            sb.append(member.getName())
              .append('=')
              .append(member.getFrom(data))
              .append(", ");
        
        int len = sb.length();
        return sb.replace(len - 2, len, "]")
        		 .toString();
	}
	
	public ByteArray toByteArray() {
		return data;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		
		if (!struct.equals(other.struct))
			return false;
		
		return data.equals(other.data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	public Structure getStructure() {
		return struct;
	}
}
