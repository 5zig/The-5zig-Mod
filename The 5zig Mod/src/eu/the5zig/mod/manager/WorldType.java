package eu.the5zig.mod.manager;

public enum WorldType {

	OVERWORLD, NETHER, THE_END;

	public static WorldType byName(String name) {
		if ("The End".equals(name)) {
			return THE_END;
		} else if ("Hell".equals(name)) {
			return NETHER;
		} else {
			return OVERWORLD;
		}
	}

}
