package eu.the5zig.mod.modules;

public enum ModuleLocation {

	TOP_LEFT, TOP_RIGHT, CENTER_LEFT, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CUSTOM;

	public ModuleLocation getNext() {
		return values()[(ordinal() + 1) % values().length];
	}

}
