package eu.the5zig.mod.gui.ts;

import java.awt.image.BufferedImage;

public class TeamSpeakIcon {

	private BufferedImage icon;
	private String id;

	public TeamSpeakIcon(BufferedImage icon, String id) {
		this.icon = icon;
		this.id = id;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
