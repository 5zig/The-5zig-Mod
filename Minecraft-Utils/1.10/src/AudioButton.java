import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.util.AudioCallback;
import eu.the5zig.mod.util.IResourceLocation;
import eu.the5zig.util.Utils;
import eu.the5zig.util.io.FileUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class AudioButton extends IconButton {

	public static final AudioFormat format = new AudioFormat(16000, 16, 2, true, true);
	private static final IResourceLocation ITEMS = MinecraftFactory.getVars().createResourceLocation("the5zigmod", "textures/items.png");

	private static final Object LOCK = new Object();
	private static TargetDataLine line;

	private final AudioCallback callback;
	private File tmpFile;

	private volatile boolean recording;
	private long recordingStarted;

	private String errorMessage;
	private int errorTicks;

	public AudioButton(int id, int x, int y, AudioCallback callback) {
		super(ITEMS, 16 * 5, 0, id, x, y);
		this.callback = callback;
		try {
			File dir = FileUtils.createDir(new File(MinecraftFactory.getClassProxyCallback().getModDirectory(), "media/" + MinecraftFactory.getVars().getGameProfile().getId().toString()));
			tmpFile = FileUtils.createFile(new File(dir, "audio.wav.tmp"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (errorTicks > 0) {
			errorTicks--;
			if (errorTicks == 0) {
				errorMessage = null;
			}
		}
		if (System.currentTimeMillis() - recordingStarted > 1000 * 60)
			stopRecording();
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		super.draw(mouseX, mouseY);

		if (errorMessage != null) {
			MinecraftFactory.getVars().getCurrentScreen().drawHoveringText(Collections.singletonList(errorMessage), (int) (getX() + MinecraftFactory.getVars().getStringWidth(errorMessage) *
					.8), getY() - 10);
		}
		if (recording) {
			String line1 = MinecraftFactory.getClassProxyCallback().translate("chat.audio.recording",
					Utils.getShortenedDouble((double) (System.currentTimeMillis() - recordingStarted) / 1000.0, 1));
			String line2 = MinecraftFactory.getClassProxyCallback().translate("chat.audio.abort");
			int stringWidth = Math.max(MinecraftFactory.getVars().getStringWidth(line1), MinecraftFactory.getVars().getStringWidth(line2));
			MinecraftFactory.getVars().getCurrentScreen().drawHoveringText(Arrays.asList(line1, line2), (int) (getX() + stringWidth * .8), getY() - 15);
		}

		if (recording && (mouseX <= getX() || mouseX >= getX() + getWidth() || mouseY <= getY() || mouseY >= getY() + getWidth())) {
			recordingStarted = System.currentTimeMillis();
			recording = false;
			synchronized (LOCK) {
				closeLine();
			}
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY) {
		boolean mouseClicked = super.mouseClicked(mouseX, mouseY);
		if (mouseClicked) {
			startRecording();
		}
		return mouseClicked;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		stopRecording();
	}

	private void error(String message) {
		this.errorMessage = message;
		this.errorTicks = 50;
	}

	private void startRecording() {
		if (recording || recordingStarted != 0)
			return;
		if (errorTicks > 0)
			errorTicks = 1;
		new Thread("Audio Record Thread") {
			@Override
			public void run() {
				try {
					DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
					if (!AudioSystem.isLineSupported(info)) {
						error(MinecraftFactory.getClassProxyCallback().translate("chat.audio.not_supported"));
						return;
					}
					synchronized (LOCK) {
						if (line != null) {
							closeLine();
						}
						line = (TargetDataLine) AudioSystem.getLine(info);
						line.open(format);
						line.start();
					}


					AudioInputStream ais = new AudioInputStream(line);
					if (tmpFile.exists())
						org.apache.commons.io.FileUtils.deleteQuietly(tmpFile);
					if (!tmpFile.createNewFile())
						throw new IOException("Could not create Audio File!");

					recording = true;
					recordingStarted = System.currentTimeMillis();

					AudioSystem.write(ais, AudioFileFormat.Type.WAVE, tmpFile);
				} catch (LineUnavailableException e) {
					error(MinecraftFactory.getClassProxyCallback().translate("chat.audio.unavailable"));
					MinecraftFactory.getClassProxyCallback().getLogger().error("Audioline currently unavailable!", e);
				} catch (Exception e) {
					MinecraftFactory.getClassProxyCallback().getLogger().error("Could not record Audio!", e);
					error(e.getMessage());
				}
			}
		}.start();
	}

	private void closeLine() {
		if (line == null)
			return;
		line.stop();
		line.close();

		if (System.currentTimeMillis() - recordingStarted > 250)
			callback.done(tmpFile);
		recordingStarted = 0;

		line = null;
	}

	private void stopRecording() {
		if (!recording)
			return;
		recording = false;
		if (System.currentTimeMillis() - recordingStarted <= 250) {
			error(MinecraftFactory.getClassProxyCallback().translate("chat.audio.hint"));
		}
		synchronized (LOCK) {
			closeLine();
		}
	}

	@Override
	public void guiClosed() {
		synchronized (LOCK) {
			closeLine();
		}
	}
}
