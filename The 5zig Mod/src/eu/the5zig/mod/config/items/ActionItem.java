package eu.the5zig.mod.config.items;

import eu.the5zig.mod.gui.GuiSettings;

public class ActionItem extends NonConfigItem {

	private final Runnable runnable;

	/**
	 * Creates an Item that cannot be serialized or deserialized.
	 *
	 * @param key      Der Key of the Item. Used in config File and to translate the Item.
	 * @param category The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param runnable The action that should be executed.
	 */
	public ActionItem(String key, String category, Runnable runnable) {
		super(key, category);
		this.runnable = runnable;
	}

	@Override
	public void action() {
		runnable.run();
	}
}
