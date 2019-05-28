package eu.the5zig.mod.config.items;

public class HexColorItem extends StringItem {

	public HexColorItem(String key, String category, String defaultValue) {
		super(key, category, defaultValue, 7, 8);
		getColor();
	}

	@Override
	public void action() {
		try {
			getColor();
		} catch (NumberFormatException ignored) {
			set(getDefaultValue());
		}
	}

	public int getColor() {
		return Integer.parseInt(get().substring(2), 16);
	}

}
