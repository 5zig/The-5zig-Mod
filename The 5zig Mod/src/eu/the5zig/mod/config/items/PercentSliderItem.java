package eu.the5zig.mod.config.items;

import eu.the5zig.mod.gui.GuiSettings;

public class PercentSliderItem extends SliderItem {

	/**
	 * Creates a Config Item that works as a Slider.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 * @param minValue     The lowest Value that is possible.
	 * @param maxValue     The highest Value that is possible.
	 * @param steps        The amount of steps the Slider can have (or {@code -1} if the Slider shouldn't be able to get locked).
	 */
	public PercentSliderItem(String key, String category, Float defaultValue, Float minValue, Float maxValue, int steps) {
		super(key, "%", category, defaultValue, minValue, maxValue, steps);
	}

	@Override
	public String getCustomValue(float value) {
		return Math.round((value * (getMaxValue() - getMinValue()) + getMinValue()) * 100) + getSuffix();
	}
}
