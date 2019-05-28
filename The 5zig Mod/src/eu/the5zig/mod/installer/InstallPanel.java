package eu.the5zig.mod.installer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Set;

import static eu.the5zig.mod.installer.InstallerUtils.log;

public class InstallPanel extends JPanel {

	private final Frame frame;
	private final Set<File> mods;

	private JLabel text;
	private JProgressBar progressBar;

	public InstallPanel(Frame frame, Set<File> mods) {
		this.frame = frame;
		this.mods = mods;
		init();
	}

	private void init() {
		setSize(500, 350);
		setLayout(null);

		JLabel title = new JLabel("The 5zig Mod v" + Frame.modVersion, SwingConstants.CENTER);
		title.setBounds(0, 10, 500, 35);
		title.setFont(new Font("Verdana", Font.BOLD, 26));
		add(title);

		JLabel subTitle = new JLabel("for Minecraft " + Frame.minecraftVersion, SwingConstants.CENTER);
		subTitle.setBounds(0, 36, 500, 35);
		subTitle.setFont(new Font("Verdana", Font.BOLD, 20));
		add(subTitle);

		text = new JLabel("Installing... Please wait.", SwingConstants.CENTER);
		text.setBounds(40, 50, 420, 170);
		text.setFont(new Font("Helvetica", Font.BOLD, 18));
		add(text);

		progressBar = new JProgressBar();
		progressBar.setBounds(20, 270, 450, 20);
		add(progressBar);

		JLabel backgroundImage = new JLabel(Images.backgroundImage);
		backgroundImage.setBounds(0, 0, 500, 350);
		add(backgroundImage);

		doInstall();
	}

	private void doInstall() {
		try {
			final InstallerNew installer = new InstallerNew(Frame.installDirectory, Frame.modVersion, Frame.minecraftVersion);

			final ProcessCallback callback = new ProcessCallback() {
				@Override
				public void progress(float percentage) {
					progressBar.setValue((int) (percentage * 100.0));
				}

				@Override
				public void message(String message) {
					text.setText(message);
					log(" ");
					log(message);
				}

				@Override
				public void log(String message) {
					InstallerUtils.log(message);
				}
			};


			if (!mods.isEmpty()) {
				installer.setOtherMods(mods.toArray(new File[mods.size()]));
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					final long start = System.currentTimeMillis();
					try {
						installer.install(callback);
					} catch (MinecraftNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"Minecraft version not found: " + Frame.minecraftVersion + ".\nPlease start Minecraft " + Frame.minecraftVersion + " manually via the Minecraft Launcher!",
								"The 5zig Mod v" + Frame.modVersion, JOptionPane.ERROR_MESSAGE, Images.iconImage);
						System.exit(1);
					} catch (Exception e) {
						InstallerUtils.exitWithException(e, Frame.modVersion, Frame.minecraftVersion, installer.otherMods);
					}
					text.setText("Done.");
					log("");
					log("===========================================");
					log("Finished Installation after " + (System.currentTimeMillis() - start) + "ms.");
					log("===========================================");
					JOptionPane.showMessageDialog(null, "The 5zig Mod has been successfully installed.", "The 5zig Mod v" + Frame.modVersion, JOptionPane.INFORMATION_MESSAGE,
							Images.iconImage);
					frame.dispose();
				}
			}).start();
		} catch (MinecraftNotFoundException ex) {
			JOptionPane.showMessageDialog(null,
					"Minecraft version not found: " + Frame.minecraftVersion + ".\nPlease start Minecraft " + Frame.minecraftVersion + " manually via the Minecraft Launcher!",
					"The 5zig" + " Mod v" + Frame.modVersion, JOptionPane.ERROR_MESSAGE, Images.iconImage);
			System.exit(1);
		} catch (Exception ex) {
			InstallerUtils.exitWithException(ex, Frame.modVersion, Frame.minecraftVersion, null);
		}
	}

}
