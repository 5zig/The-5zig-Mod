package eu.the5zig.mod.util;

import eu.the5zig.mod.gui.elements.Row;

public interface GuiListChatCallback {

	boolean drawDefaultBackground();

	Object getResourceLocation();

	int getImageWidth();

	int getImageHeight();

	void chatLineClicked(Row row, int mouseX, int y, int minY, int left);

}
