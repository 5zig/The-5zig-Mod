package eu.the5zig.mod.modules;

public class RegisteredItem {

	private final String key;
	private final Class<? extends AbstractModuleItem> clazz;
	private final String category;

	RegisteredItem(String key, Class<? extends AbstractModuleItem> clazz, String category) {
		this.key = key;
		this.clazz = clazz;
		this.category = category;
	}

	public String getKey() {
		return key;
	}

	public Class<? extends AbstractModuleItem> getClazz() {
		return clazz;
	}

	public String getCategory() {
		return category;
	}
}
