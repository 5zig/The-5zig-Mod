package eu.the5zig.mod.manager.keyboard;

import eu.the5zig.mod.util.NativeLibrary;

public abstract class KeyboardController {

	public abstract NativeLibrary.NativeOS getTarget();

	public abstract String[] getNativeNames();

	public abstract boolean init();

	public abstract void unInit();

	public abstract void setIlluminatedKeys(KeyGroup group, int color);

	public abstract void setShowHealth(boolean show);

	public abstract void setShowArmor(boolean show);

	public abstract void updateHealthAndArmor(float health, float armor);

	public abstract void onDamage();

	public abstract void displayPotionColor(int color);

	public abstract void update();

	public enum Device {

		NONE, RAZER, ROCCAT, LOGITECH

	}

	public enum KeyGroup {
		/**
		 * All keys
		 */
		ALL,
		/**
		 * Only WASD and SPACE
		 */
		SPECIAL
	}

}
