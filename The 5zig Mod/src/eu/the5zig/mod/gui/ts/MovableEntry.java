package eu.the5zig.mod.gui.ts;

import eu.the5zig.teamspeak.api.Channel;

public interface MovableEntry {

	void renderDragging(int x, int y, int width, int height);

	void renderDraggingIcons(int x, int y, int width, int height);

	boolean canBeMovedTo(Channel to, DragLocation location);

	void moveEntryTo(Channel to, DragLocation location);

	enum DragLocation {

		ABOVE, INSIDE, BELOW

	}

}
