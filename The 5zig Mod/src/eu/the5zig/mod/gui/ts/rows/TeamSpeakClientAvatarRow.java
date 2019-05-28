package eu.the5zig.mod.gui.ts.rows;

import eu.the5zig.mod.gui.elements.Row;
import eu.the5zig.mod.render.Base64Renderer;
import eu.the5zig.teamspeak.api.Client;

import java.awt.image.BufferedImage;

public class TeamSpeakClientAvatarRow implements Row {

	private Base64Renderer renderer;
	private int width;
	private int height;

	public TeamSpeakClientAvatarRow(int width, Client client) {
		BufferedImage avatar = client.getAvatar();
		if (avatar != null) {
			this.width = avatar.getWidth();
			this.height = avatar.getHeight();
			int maxWidth = Math.min(100, width - 10);
			int maxHeight = 100;
			while (this.width > maxWidth || this.height > maxHeight) {
				this.width /= 1.2;
				this.height /= 1.2;
			}
			this.renderer = Base64Renderer.getRenderer(avatar, "ts/avatar_" + client.getUniqueId());
		}
	}

	@Override
	public void draw(int x, int y) {
		if (renderer != null) {
			renderer.renderImage(x + 5, y + 2, width, height);
		}
	}

	@Override
	public int getLineHeight() {
		return height == 0 ? 0 : height + 4;
	}
}
