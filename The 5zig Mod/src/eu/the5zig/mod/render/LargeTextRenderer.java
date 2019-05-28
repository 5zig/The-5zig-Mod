package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.util.GLUtil;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class LargeTextRenderer {

	private float scaleModifyFactor = 1.0f;

	private Renderable renderable;

	public void render(String text, float scale, int y) {
		this.renderable = new Renderable(text, scale * scaleModifyFactor, y);
	}

	public void render(String text, float scale) {
		render(text, scale, The5zigMod.getVars().getScaledHeight() / 4);
	}

	public void render(String text) {
		render(text, 1.5f);
	}

	public float getScaleModifyFactor() {
		return scaleModifyFactor;
	}

	public void setScaleModifyFactor(float scaleModifyFactor) {
		this.scaleModifyFactor = scaleModifyFactor;
	}

	public void flush() {
		if (this.renderable == null)
			return;
		if (The5zigMod.getVars().isPlayerListShown())
			return;

		float displayScale = renderable.scale * The5zigMod.getConfig().getFloat("scale");
		GLUtil.pushMatrix();
		GLUtil.translate((The5zigMod.getVars().getScaledWidth() - The5zigMod.getVars().getStringWidth(renderable.text) * displayScale) / 2.0f, renderable.y, 2);
		GLUtil.scale(displayScale, displayScale, displayScale);
		The5zigMod.getVars().drawString(renderable.text, 0, 0);
		GLUtil.popMatrix();

		renderable = null;
		scaleModifyFactor = 1.0f;
	}

	private class Renderable {

		private String text;
		private float scale;
		private int y;

		public Renderable(String text, float scale, int y) {
			this.text = text;
			this.scale = scale;
			this.y = y;
		}
	}

}
