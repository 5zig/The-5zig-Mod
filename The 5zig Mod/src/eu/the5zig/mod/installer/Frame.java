package eu.the5zig.mod.installer;

import eu.the5zig.mod.Version;
import eu.the5zig.util.Utils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static eu.the5zig.mod.installer.InstallerUtils.*;

public class Frame extends JFrame {

	public static String modVersion = "Unknown", minecraftVersion = "Unknown";

	private final File thisFile;

	private JPanel mainPanel;
	private JPanel modListPanel;



	public static File installDirectory;

	public Frame() throws IOException, IllegalAccessException {
		Utils.setUI(UIManager.getSystemLookAndFeelClassName());

		thisFile = Utils.getRunningJar();
		if (thisFile == null) {
			throw new IOException("Could not find current file!");
		}

		modVersion = Version.VERSION;
		minecraftVersion = Version.MCVERSION;
		log("Loading " + InstallerNew.getVersionName(minecraftVersion) + "...");

		Images.load();
		installDirectory = getMinecraftDirectory();
		init();
	}

	public static void main(String[] args) {
		try {
			new Frame();
		} catch (Throwable throwable) {
			exitWithException(throwable, modVersion, minecraftVersion, null);
		}
	}

	private void init() {
		setTitle("The 5zig Mod v" + modVersion);
		setSize(500, 350);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIconImage(Images.iconImage.getImage());

		setContentPane(new MainPanel(this));

		setVisible(true);
	}

}
