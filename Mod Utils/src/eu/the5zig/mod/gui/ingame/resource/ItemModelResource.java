package eu.the5zig.mod.gui.ingame.resource;

public class ItemModelResource {

	private Object resourceLocation;

	private Object item;
	private Render render;
	private Object bakedModel;
	private Object simpleTexture;

	public ItemModelResource(Object resourceLocation, Object item, Render render, Object bakedModel, Object simpleTexture) {
		this.resourceLocation = resourceLocation;
		this.item = item;
		this.render = render;
		this.bakedModel = bakedModel;
		this.simpleTexture = simpleTexture;
	}

	public Object getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(Object resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	public Object getItem() {
		return item;
	}

	public Render getRender() {
		return render;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public Object getBakedModel() {
		return bakedModel;
	}

	public void setBakedModel(Object bakedModel) {
		this.bakedModel = bakedModel;
	}

	public Object getSimpleTexture() {
		return simpleTexture;
	}

	public void setSimpleTexture(Object simpleTexture) {
		this.simpleTexture = simpleTexture;
	}

	public enum Render {

		BOW_PULLING_0, BOW_PULLING_1, BOW_PULLING_2, FISHING_ROD_CAST

	}

}
