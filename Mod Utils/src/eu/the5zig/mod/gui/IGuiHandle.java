package eu.the5zig.mod.gui;

import java.util.List;

public interface IGuiHandle {

	int getWidth();

	int getHeight();

	void setResolution(int width, int height);

	void drawDefaultBackground();

	void drawMenuBackground();

	void drawTexturedModalRect(int x, int y, int texX, int texY, int width, int height);

	void drawHoveringText(List<String> lines, int x, int y);

}
