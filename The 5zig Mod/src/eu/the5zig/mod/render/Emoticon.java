package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;

import java.awt.image.BufferedImage;

public class Emoticon {

	private final String unicodeCharacter;
	private final BufferedImage bufferedImage;
	private final Object resourceLocation;

	public Emoticon(String unicodeCharacter, BufferedImage bufferedImage) {
		this.unicodeCharacter = unicodeCharacter;
		this.bufferedImage = bufferedImage;
		this.resourceLocation = The5zigMod.getVars().loadDynamicImage("emoticon", bufferedImage);
	}

	public String getUnicodeCharacter() {
		return unicodeCharacter;
	}

	public Object getResourceLocation() {
		return resourceLocation;
	}

	public void render() {
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getWidth(), bufferedImage.getHeight());
	}
}
