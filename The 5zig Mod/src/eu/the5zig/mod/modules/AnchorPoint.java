package eu.the5zig.mod.modules;

import eu.the5zig.mod.render.RenderLocation;

public enum AnchorPoint {

	TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER_CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT;

	public AnchorPoint getNext() {
		return values()[(ordinal() + 1) % values().length];
	}

	public RenderLocation toRenderLocation() {
		switch (this) {
			case TOP_LEFT: case CENTER_LEFT: case BOTTOM_LEFT:
				return RenderLocation.LEFT;
			case TOP_CENTER: case CENTER_CENTER: case BOTTOM_CENTER:
				return RenderLocation.CENTERED;
			case TOP_RIGHT: case CENTER_RIGHT: case BOTTOM_RIGHT:
				return RenderLocation.RIGHT;
			default:
				return RenderLocation.LEFT;
		}
	}

}
