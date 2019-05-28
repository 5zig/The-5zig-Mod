package eu.the5zig.mod.manager.keyboard;

import eu.the5zig.mod.util.NativeLibrary;

public class RoccatController extends KeyboardController {

	@Override
	public NativeLibrary.NativeOS getTarget() {
		return NativeLibrary.NativeOS.WINDOWS;
	}

	@Override
	public String[] getNativeNames() {
		return new String[]{"roccatSDK${arch}", "roccat${arch}"};
	}

	@Override
	public native boolean init();

	@Override
	public native void unInit();

	@Override
	public void setIlluminatedKeys(KeyGroup group, int color) {
		setIlluminatedKeys(group.ordinal(), color);
	}

	private native void setIlluminatedKeys(int group, int color);

	@Override
	public native void setShowHealth(boolean show);

	@Override
	public native void setShowArmor(boolean show);

	@Override
	public native void updateHealthAndArmor(float health, float armor);

	@Override
	public native void onDamage();

	@Override
	public native void displayPotionColor(int color);

	@Override
	public native void update();

}
