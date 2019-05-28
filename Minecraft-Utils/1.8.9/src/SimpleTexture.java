import java.awt.image.BufferedImage;
import java.io.IOException;

public class SimpleTexture extends bme {

	private boolean textureUploaded;
	private BufferedImage bufferedImage;

	public SimpleTexture() {
		super(null);
	}

	void checkTextureUploaded() {
		if (!this.textureUploaded) {
			if (this.bufferedImage != null) {
				bml.a(super.b(), this.bufferedImage);
				this.textureUploaded = true;
			}
		}
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.textureUploaded = false;
		this.bufferedImage = bufferedImage;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	@Override
	public int b() {
		checkTextureUploaded();
		return super.b();
	}

	@Override
	public void a(bni resourceManager) throws IOException {
	}
}
