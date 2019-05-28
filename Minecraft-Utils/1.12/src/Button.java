import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.elements.IButton;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Button extends biy implements IButton {

	private int disabledTicks = 0;

	public Button(int id, int x, int y, String label) {
		super(id, x, y, label);
	}

	public Button(int id, int x, int y, String label, boolean enabled) {
		super(id, x, y, label);
		setEnabled(enabled);
	}

	public Button(int id, int x, int y, int width, int height, String label) {
		super(id, x, y, width, height, label);
	}

	public Button(int id, int x, int y, int width, int height, String label, boolean enabled) {
		super(id, x, y, width, height, label);
		setEnabled(enabled);
	}

	@Override
	public int getId() {
		return k;
	}

	@Override
	public String getLabel() {
		return j;
	}

	@Override
	public void setLabel(String label) {
		this.j = label;
	}

	@Override
	public int getWidth() {
		return f;
	}

	@Override
	public void setWidth(int width) {
		this.f = width;
	}

	@Override
	public int getHeight() {
		return g;
	}

	@Override
	public void setHeight(int height) {
		this.g = height;
	}

	@Override
	public boolean isEnabled() {
		return l;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.l = enabled;
	}

	@Override
	public boolean isVisible() {
		return m;
	}

	@Override
	public void setVisible(boolean visible) {
		this.m = visible;
	}

	@Override
	public boolean isHovered() {
		return n;
	}

	@Override
	public void setHovered(boolean hovered) {
		this.n = hovered;
	}

	@Override
	public int getX() {
		return h;
	}

	@Override
	public void setX(int x) {
		this.h = x;
	}

	@Override
	public int getY() {
		return i;
	}

	@Override
	public void setY(int y) {
		this.i = y;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		a(((Variables) MinecraftFactory.getVars()).getMinecraft(), mouseX, mouseY, 0);
	}

	@Override
	public void tick() {
		if (disabledTicks > 0) {
			disabledTicks--;
			if (disabledTicks == 0) {
				setEnabled(true);
			}
		}
		if (disabledTicks < 0)
			disabledTicks = 1;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY) {
		return b(((Variables) MinecraftFactory.getVars()).getMinecraft(), mouseX, mouseY);
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		a(mouseX, mouseY);
	}

	@Override
	public void playClickSound() {
		a(((Variables) MinecraftFactory.getVars()).getMinecraft().U());
	}

	@Override
	public void setTicksDisabled(int ticks) {
		setEnabled(false);
		disabledTicks = ticks;
	}

	@Override
	public void guiClosed() {
	}
}
