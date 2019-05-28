package eu.the5zig.mod.gui.ts;

import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakChannel;

public class DraggingPosition {

	public final GuiTeamSpeakChannel channel;
	public final MovableEntry.DragLocation location;

	public DraggingPosition(GuiTeamSpeakChannel channel, MovableEntry.DragLocation location) {
		this.channel = channel;
		this.location = location;
	}
}
