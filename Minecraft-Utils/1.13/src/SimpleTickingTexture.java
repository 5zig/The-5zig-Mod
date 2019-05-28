import eu.the5zig.mod.MinecraftFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleTickingTexture extends dcy implements ddh {

	private final ResourceLocation resourceLocation;

	private boolean textureUploaded;
	private List<BufferedImage> bufferedImages;
	private List<ResourceLocation> frames;
	private int index;

	public SimpleTickingTexture(ResourceLocation resourceLocation) {
		super(null);
		this.resourceLocation = resourceLocation;
	}

	private void checkTextureUploaded() {
		if (!this.textureUploaded) {
			if (this.frames != null) {
				List<BufferedImage> bufferedImages1 = this.bufferedImages;
				for (int i = 0; i < bufferedImages1.size(); i++) {
					BufferedImage bufferedImage = bufferedImages1.get(i);
					ResourceLocation resourceLocation = new ResourceLocation(this.resourceLocation.getResourceDomain(), this.resourceLocation.getResourcePath() + "_" + i);
					dde texture = ((Variables) MinecraftFactory.getVars()).getTextureManager().b(resourceLocation);
					SimpleTexture simpleTexture;
					if (texture instanceof SimpleTexture) {
						simpleTexture = (SimpleTexture) texture;
					} else {
						simpleTexture = new SimpleTexture();
						((Variables) MinecraftFactory.getVars()).getTextureManager().a(resourceLocation, simpleTexture);
					}
					simpleTexture.setBufferedImage(bufferedImage);
					simpleTexture.checkTextureUploaded();
					frames.add(resourceLocation);
				}
				this.textureUploaded = true;
			}
		}
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		if (bufferedImage == null) {
			frames = null;
			index = 0;
			textureUploaded = false;
		} else {
			int parts = bufferedImage.getHeight() / (bufferedImage.getWidth() / 2);
			int partHeight = bufferedImage.getHeight() / parts;
			bufferedImages = new ArrayList<BufferedImage>(parts);
			frames = new ArrayList<ResourceLocation>(parts);
			for (int part = 0; part < parts; part++) {
				bufferedImages.add(bufferedImage.getSubimage(0, part * partHeight, bufferedImage.getWidth(), partHeight));
			}
		}
	}

	@Override
	public void a(ve resourceManager) throws IOException {
	}

	@Override
	public void e() {
		checkTextureUploaded();
		if (textureUploaded && frames.size() > 1) {
			index = (index + 1) % (frames.size());
		}
	}

	public ResourceLocation getCurrentResource() {
		if (!textureUploaded || index < 0 || index >= frames.size()) {
			return null;
		}
		return frames.get(index);
	}
}
