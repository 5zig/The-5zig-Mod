import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.IColorSelector;
import eu.the5zig.mod.util.ColorSelectorCallback;
import eu.the5zig.util.minecraft.ChatColor;

public class ColorSelector extends Button implements IColorSelector {

	private final ColorSelectorCallback callback;

	private int boxWidth = 14, boxHeight = 14;

	private boolean selected = false;
	private int selectedX = -1, selectedY = -1;
	private boolean requireMoveOut = false;

	public ColorSelector(int id, int x, int y, int width, int height, String label, ColorSelectorCallback callback) {
		super(id, x, y, width, height, label);
		this.callback = callback;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);

		int boxX = getBoxX();
		int boxY = getBoxY();

		if (isEnabled() && !selected && !requireMoveOut && mouseX >= boxX && mouseX <= boxX + boxWidth && mouseY >= boxY && mouseY <= boxY + boxHeight) {
			selectedX = boxX - 16 * 12 / 2 + 7;
			selectedY = boxY + 3;
			if (boxX + 16 * 12 / 2 + 2 > MinecraftFactory.getVars().getCurrentScreen().getWidth())
				this.selectedX = MinecraftFactory.getVars().getCurrentScreen().getWidth() - 192 - 2;

			selected = true;
		} else if (requireMoveOut) {
			if (mouseX < boxX || mouseX > boxX + boxWidth || mouseY < selectedY || mouseY > selectedY + 8) {
				selectedX = selectedY = -1;
				selected = false;
				requireMoveOut = false;
			}
		} else if (!selected || (mouseX < selectedX || mouseX > selectedX + 192 || mouseY < selectedY - 8 || mouseY > selectedY + 8 + 8)) {
			selectedX = selectedY = -1;
			selected = false;
			requireMoveOut = false;
		}

		// draw Selected Color Box
		Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight,
				((selected && !requireMoveOut) || (mouseX >= boxX && mouseX <= boxX + boxWidth && mouseY >= boxY && mouseY <= boxY + boxHeight) ? 0xFF444444 : 0xFF222222));
		Gui.drawRect(boxX + 2, boxY + 2, boxX + boxWidth - 2, boxY + boxHeight - 2, (0x00FFFFFF & callback.getColor().getColor()) | 0xFF << 24);

		// draw Select Color Box
		if (selected) {
			int width = 192;
			Gui.drawRect(selectedX - 2, selectedY - 2, selectedX + width + 2, selectedY + 10, 0xFF222222);
			int x = 0;
			for (ChatColor chatColor : ChatColor.values()) {
				if (chatColor.getColor() == -1)
					continue;
				Gui.drawRect(selectedX + x, selectedY, selectedX + x + 12, selectedY + 8, (0x00FFFFFF & chatColor.getColor()) | 0xFF << 24);
				x += 12;
			}
		}
	}

	/**
	 * getHoverState
	 *
	 * @param b If the Mouse is over the button
	 * @return always 1, because afl don't want this button to be able to get hovered.
	 */
	@Override
	protected int a(boolean b) {
		setHovered(false);
		return isEnabled() ? 1 : 0;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY) {
		boolean result = super.mouseClicked(mouseX, mouseY);

		if (selected && mouseX >= selectedX && mouseX <= selectedX + 192 && mouseY >= selectedY && mouseY <= selectedY + 8) {
			callback.setColor(ChatColor.values()[((int) (16 - Math.ceil((double) (selectedX + 192 - mouseX) / 12.0)))]);
			MinecraftFactory.getVars().getCurrentScreen().actionPerformed0(this);

			requireMoveOut = true;
			selected = false;
		}
		return result;
	}


	private int getBoxX() {
		return getX() + (getWidth() + MinecraftFactory.getVars().getStringWidth(getLabel())) / 2 + 4;
	}

	private int getBoxY() {
		return getY() + (getHeight() - boxHeight) / 2;
	}

	/**
	 private void drawColor() {
	 double white = 1;
	 double d = 20;
	 for (int x = 0; x < 120; x++) {
	 for (int y = 0; y < 120; y++) {
	 int cr = 0, cg = 0, cb = 0;
	 if (x >= 0 && x <= d) {
	 cr = 255;
	 cg = (int) (255 * ((double) x / d));
	 }
	 if (x > d && x <= 2 * d) {
	 cr = (int) (255 * ((2 * d - x) / d));
	 cg = 255;
	 }
	 if (x > 2 * d && x <= 3 * d) {
	 cg = 255;
	 cb = (int) (255 * ((x - 2 * d) / d));
	 }
	 if (x > 3 * d && x <= 4 * d) {
	 cg = (int) (255 * ((4 * d - x) / d));
	 cb = 255;
	 }
	 if (x > 4 * d && x <= 5 * d) {
	 cb = 255;
	 cr = (int) (255 * ((x - 4 * d) / d));
	 }
	 if (x > 5 * d && x <= 6 * d) {
	 cb = (int) (255 * ((6 * d - x) / d));
	 cr = 255;
	 }
	 int i = ((int) (255.0 * white * (double) y / 120.0));
	 cr += i;
	 cg += i;
	 cb += i;
	 cr = Math.max(Math.min(cr, 255), 0);
	 cg = Math.max(Math.min(cg, 255), 0);
	 cb = Math.max(Math.min(cb, 255), 0);
	 int color = Utils.getARBGInt(255, cr, cg, cb);
	 Gui.drawRect(selectedX + x, selectedY + y, selectedX + x + 1, selectedY + y + 1, color);
	 }
	 }
	 }*/
}
