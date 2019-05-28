package eu.the5zig.mod.chat;

import eu.the5zig.mod.The5zigMod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChatBackgroundManager {

	private Object chatBackgroundLocation;
	private int imageWidth, imageHeight;

	public ChatBackgroundManager() {
		reloadBackgroundImage();
	}

	public void reloadBackgroundImage() {
		String location = (String) The5zigMod.getConfig().get("chatBackgroundLocation").get();
		if (location == null) {
			resetBackgroundImage();
			return;
		}
		File file = new File(location);
		if (!file.exists()) {
			resetBackgroundImage();
			return;
		}
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			imageWidth = bufferedImage.getWidth();
			imageHeight = bufferedImage.getHeight();
			chatBackgroundLocation = The5zigMod.getVars().loadDynamicImage("chat_background", bufferedImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetBackgroundImage() {
		chatBackgroundLocation = null;
		imageWidth = 0;
		imageHeight = 0;
	}

	public Object getChatBackground() {
		return chatBackgroundLocation;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
}
