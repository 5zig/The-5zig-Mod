package eu.the5zig.mod.manager;

import eu.the5zig.mod.util.Vector3f;

public class DeathLocation {

	private final Vector3f coordinates;
	private final WorldType worldType;

	public DeathLocation(Vector3f coordinates, WorldType worldType) {
		this.coordinates = coordinates;
		this.worldType = worldType;
	}

	public Vector3f getCoordinates() {
		return coordinates;
	}

	public WorldType getWorldType() {
		return worldType;
	}
}
