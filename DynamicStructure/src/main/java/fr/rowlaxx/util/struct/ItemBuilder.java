package fr.rowlaxx.util.struct;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemBuilder {

	private final Structure struct;
	
	private Map<String, Object> values;
	private Item result = null;
	
	ItemBuilder(Structure struct) {
		this.struct = Objects.requireNonNull(struct);
		this.values = new HashMap<>();
	}
	
	public Item build() {		
		if (result == null) {
			result = struct.createItem(values);
			values = null;
		}
		
		return result;
	}
	
	public ItemBuilder set(String memberName, Object value) {
		if (result != null)
			throw new IllegalStateException("This builder has been finished");
		
		if (value == null)
			values.remove(memberName);
		else
			values.put(memberName, value);
		return this;
	}
	
	public ItemBuilder remove(String memberName) {
		if (result != null)
			throw new IllegalStateException("This builder has been finished");
		
		values.remove(memberName);
		return this;
	}
}
