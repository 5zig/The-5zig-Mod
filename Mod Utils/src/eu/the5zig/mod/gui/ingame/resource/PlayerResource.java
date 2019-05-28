package eu.the5zig.mod.gui.ingame.resource;

import java.util.List;

public class PlayerResource {

	private CapeResource capeResource;
	private List<ItemModelResource> itemModelResources;

	public PlayerResource() {
	}

	public CapeResource getCapeResource() {
		return capeResource;
	}

	public void setCapeResource(CapeResource capeResource) {
		this.capeResource = capeResource;
	}

	public List<ItemModelResource> getItemModelResources() {
		return itemModelResources;
	}

	public void setItemModelResources(List<ItemModelResource> itemModelResources) {
		this.itemModelResources = itemModelResources;
	}
}
