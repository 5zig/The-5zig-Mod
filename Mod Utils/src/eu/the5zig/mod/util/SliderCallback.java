package eu.the5zig.mod.util;

public interface SliderCallback {

	String translate();

	float get();

	void set(float value);

	float getMinValue();

	float getMaxValue();

	int getSteps();

	String getCustomValue(float value);

	String getSuffix();

	void action();

}
