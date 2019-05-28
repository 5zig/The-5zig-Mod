package eu.the5zig.mod.util;

public interface ISystemPowerStatus {

	boolean isPluggedIn();

	int getBatteryPercentage();

	int getRemainingLifeTime();

}
