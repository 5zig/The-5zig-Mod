package eu.the5zig.mod.util;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import java.util.ArrayList;
import java.util.List;

public interface Kernel32 extends StdCallLibrary {

	Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32", Kernel32.class);

	class SYSTEM_POWER_STATUS extends Structure implements ISystemPowerStatus {

		/**
		 * The AC power status
		 */
		public byte ACLineStatus;
		/**
		 * The battery charge status
		 */
		public byte BatteryFlag;
		/**
		 * The percentage of full battery charge remaining
		 */
		public byte BatteryLifePercent;
		public byte Reserved1;
		/**
		 * The number of seconds of battery life remaining
		 */
		public int BatteryLifeTime;
		/**
		 * The number of seconds of battery life when at full charge
		 */
		public int BatteryFullLifeTime;

		@Override
		protected List<String> getFieldOrder() {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("ACLineStatus");
			fields.add("BatteryFlag");
			fields.add("BatteryLifePercent");
			fields.add("Reserved1");
			fields.add("BatteryLifeTime");
			fields.add("BatteryFullLifeTime");
			return fields;
		}

		@Override
		public boolean isPluggedIn() {
			return ACLineStatus != 0;
		}

		@Override
		public int getBatteryPercentage() {
			if (BatteryLifePercent == (byte) 255) {
				return 100;
			} else {
				return BatteryLifePercent;
			}
		}

		@Override
		public int getRemainingLifeTime() {
			return BatteryLifeTime;
		}

	}

	/**
	 * Fill the structure.
	 */
	int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);
}