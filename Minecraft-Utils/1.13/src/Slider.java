import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.mod.util.SliderCallback;
import eu.the5zig.util.Utils;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Slider extends Button {

	private final String str;
	public float value;
	public boolean dragging;
	private SliderCallback sliderItem;

	public Slider(int id, int x, int y, SliderCallback sliderItem) {
		this(id, x, y, 150, 20, sliderItem);
	}

	public Slider(int id, int x, int y, int width, int height, SliderCallback sliderItem) {
		super(id, x, y, width, height, "");
		this.sliderItem = sliderItem;
		this.str = sliderItem.translate();
		this.value = normalizeValue(sliderItem.get());
		setLabel(this.str);
	}

	@Override
	public void setLabel(String label) {
		this.value = normalizeValue(sliderItem.get());
		super.setLabel(label + ": " + getValue());
	}

	/**
	 * Get Hover State
	 */
	@Override
	protected int a(boolean b) {
		return 0;
	}

	/**
	 * Mouse dragged
	 */
	@Override
	protected void a(cfi mc, int mouseX, int mouseY) {
		if (isVisible()) {
			if (this.dragging) {
				this.value = (mouseX - (getX() + 4)) / (float) (getWidth() - 8);
				this.value = Utils.clamp(value, 0f, 1f);

				float v = denormalizeValue(value);
				sliderItem.set(v);
				this.value = normalizeValue(v);

				setLabel(this.str);
			}

			MinecraftFactory.getVars().bindTexture(a);
			GLUtil.color(1, 1, 1, 1);
			b(getX() + (int) (this.value * (getWidth() - 8)), getY(), 0, 66, 4, 20);
			b(getX() + (int) (this.value * (getWidth() - 8)) + 4, getY(), 196, 66, 4, 20);
		}
	}

	public float denormalizeValue(float value) {
		return this.snapToStepClamp(sliderItem.getMinValue() + (sliderItem.getMaxValue() - sliderItem.getMinValue()) * Utils.clamp(value, 0.0F, 1.0F));
	}

	public float normalizeValue(float value) {
		return Utils.clamp((snapToStepClamp(value) - sliderItem.getMinValue()) / (sliderItem.getMaxValue() - sliderItem.getMinValue()), 0.0F, 1.0F);
	}

	public float snapToStepClamp(float value) {
		value = snapToStep(value);
		return Utils.clamp(value, sliderItem.getMinValue(), sliderItem.getMaxValue());
	}

	protected float snapToStep(float value) {
		if (sliderItem.getSteps() != -1) {
			value = sliderItem.getSteps() * (float) Math.round(value / sliderItem.getSteps());
		}

		return value;
	}

	@Override
	public boolean mouseClicked(int x, int y) {
		if (super.mouseClicked(x, y)) {
			this.value = (x - (this.h + 4)) / (float) (this.f - 8);
			if (this.value < 0.0F) {
				this.value = 0.0F;
			}
			if (this.value > 1.0F) {
				this.value = 1.0F;
			}

			setLabel(this.str + getValue());
			this.dragging = true;
			return true;
		}
		return false;
	}

	private String getValue() {
		String customValue = sliderItem.getCustomValue(value);
		if (customValue != null)
			return customValue;
		return Math.round(value * (sliderItem.getMaxValue() - sliderItem.getMinValue()) + sliderItem.getMinValue()) + sliderItem.getSuffix();
	}

	@Override
	public void mouseReleased(int x, int y) {
		if (this.dragging) {
			// play button press sound
			playClickSound();

			sliderItem.action();
		}
		this.dragging = false;
	}

}
