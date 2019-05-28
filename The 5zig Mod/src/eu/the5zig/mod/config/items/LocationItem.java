package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.gui.GuiSettings;
import eu.the5zig.mod.modules.ModuleLocation;

public class LocationItem extends EnumItem<ModuleLocation> {

	private float xOffset, yOffset;
	private boolean centered = false;

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public LocationItem(String key, String category, ModuleLocation defaultValue) {
		super(key, category, defaultValue, ModuleLocation.class);
	}

	@Override
	public void serialize(JsonObject object) {
		JsonObject content = new JsonObject();
		content.addProperty("type", get().toString());
		content.addProperty("x", getXOffset());
		content.addProperty("y", getYOffset());
		content.addProperty("centered", isCentered());
		object.add(getKey(), content);
	}

	@Override
	public void deserialize(JsonObject object) {
		JsonObject content = object.get(getKey()).getAsJsonObject();
		ModuleLocation location = ModuleLocation.valueOf(content.get("type").getAsString());
		set(location);
		setXOffset(content.get("x").getAsFloat());
		setYOffset(content.get("y").getAsFloat());
		setCentered(content.get("centered").getAsBoolean());
	}

	public float getXOffset() {
		return xOffset;
	}

	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public boolean isCentered() {
		return centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
	}
}
