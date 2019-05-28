package eu.the5zig.mod.gui.ingame.resource;

public class CapeResource {

	private Object resourceLocation;
	private Object simpleTexture;

	public CapeResource(Object resourceLocation, Object simpleTexture) {
		this.resourceLocation = resourceLocation;
		this.simpleTexture = simpleTexture;
	}

	public Object getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(Object resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	public Object getSimpleTexture() {
		return simpleTexture;
	}

	public void setSimpleTexture(Object simpleTexture) {
		this.simpleTexture = simpleTexture;
	}
}
