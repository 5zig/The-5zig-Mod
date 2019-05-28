import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.mod.util.IResourceLocation;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class IconButton extends Button {

	private IResourceLocation resourceLocation;
	private int u;
	private int v;

	public IconButton(IResourceLocation resourceLocation, int u, int v, int id, int x, int y) {
		super(id, x, y, 20, 20, "");
		this.resourceLocation = resourceLocation;
		this.u = u;
		this.v = v;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);
		if (isVisible()) {
			MinecraftFactory.getVars().bindTexture(resourceLocation);
			GLUtil.color(1, 1, 1, 1);
			Gui.drawModalRectWithCustomSizedTexture(getX() + 2, getY() + 2, u, v, 16, 16, 128, 128);
		}
	}
}
