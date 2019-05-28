package eu.the5zig.mod.manager.keyboard;

import eu.the5zig.mod.util.NativeLibrary;

public class RazerController extends KeyboardController {

	@Override
	public NativeLibrary.NativeOS getTarget() {
		return NativeLibrary.NativeOS.WINDOWS;
	}

	@Override
	public String[] getNativeNames() {
		return new String[]{"razer${arch}"};
	}

	@Override
	public boolean init() {
		RazerResult result = RazerResult.byCode(init0());
		return result == RazerResult.SUCCESS;
	}

	private native long init0();

	@Override
	public void unInit() {
		unInit0();
	}

	private native long unInit0();

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

	public enum RazerResult {

		/**
		 * Invalid
		 */
		INVALID(-1),
		/**
		 * Success
		 */
		SUCCESS(0),
		/**
		 * No permission to access device. Can be returned when calling "SetEffect"
		 */
		ACCESS_DENIED(5),
		/**
		 * Invalid handle
		 */
		INVALID_HANDLE(6),
		/**
		 * Effect not supported for the current device
		 */
		NOT_SUPPORTED(50),
		/**
		 * Invalid effect parameter
		 */
		INVALID_PARAMETER(87),
		/**
		 * Chroma SDK Service not running
		 */
		SERVICE_NOT_ACTIVE(1062),
		/**
		 * Cannot start more than one instance of the specified program
		 */
		SINGLE_INSTANCE_APP(1152),
		/**
		 * Device not connected
		 */
		DEVICE_NOT_CONNECTED(1167),
		/**
		 * Effect id not found
		 */
		NOT_FOUND(1168),
		/**
		 * Request aborted
		 */
		REQUEST_ABORTED(1235),
		/**
		 * Chroma SDK has already been initialized
		 */
		ALREADY_INITIALIZED(1247),
		/**
		 * Resource not available or disabled
		 */
		RESOURCE_DISABLED(4309),
		/**
		 * Device not available or supported
		 */
		DEVICE_NOT_AVAILABLE(4319),
		/**
		 * Chroma SDK not in a valid state and probably hasn't been initialized yet
		 */
		NOT_VALID_STATE(5023),
		/**
		 * No more items
		 */
		NO_MORE_ITEMS(259),
		/**
		 * General failure
		 */
		FAILED(2147500037L);

		private long code;

		RazerResult(long code) {
			this.code = code;
		}

		public static RazerResult byCode(long code) {
			for (RazerResult razerResult : values()) {
				if (razerResult.code == code) {
					return razerResult;
				}
			}
			return INVALID;
		}
	}

}
