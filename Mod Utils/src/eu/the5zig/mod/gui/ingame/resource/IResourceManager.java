package eu.the5zig.mod.gui.ingame.resource;

public interface IResourceManager {

	void updateOwnPlayerTextures();

	Object getOwnCapeLocation();

	void cleanupTextures();

	Object getCapeLocation(Object player);

}
