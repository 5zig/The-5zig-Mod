package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.util.IResourceLocation;

import java.util.Calendar;

public class SnowRenderer {

	private IResourceLocation resourceLocation = The5zigMod.getVars().createResourceLocation("textures/environment/snow.png");
	private int yOffset;
	private boolean render = true;

	public SnowRenderer() {
		Calendar calendar = Calendar.getInstance();

		render = (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) >= 15) || (calendar.get(Calendar.MONTH) == Calendar.JANUARY && calendar.get(
				Calendar.DAY_OF_MONTH) <= 15);
	}

	public void render(int width, int height) {
		if (!render)
			return;
		The5zigMod.getVars().bindTexture(resourceLocation);
		for (int y = -256; y <= Math.ceil(height / 256.0); y++) {
			for (int x = 0; x <= width; x += 64) {
				Gui.drawModalRectWithCustomSizedTexture(x, y * 256 + yOffset, 0, 0, 64, 256, 64, 256);
			}
		}
		yOffset = (yOffset + 1) % 256;
	}

}
