package eu.the5zig.mod.installer;

import javax.swing.*;

public class Images {

	public static ImageIcon iconImage;
	public static ImageIcon backgroundImage;
	public static ImageIcon installImage;
	public static ImageIcon installHoverImage;
	public static ImageIcon installDisabledImage;
	public static ImageIcon installImage2;
	public static ImageIcon installHoverImage2;
	public static ImageIcon installDisabledImage2;
	public static ImageIcon installBox;

	public static void load() {
		InstallerUtils.log("Loading Images...");
		iconImage = new ImageIcon(Images.class.getResource("/images/5zig-icon.png"));
		backgroundImage = new ImageIcon(Images.class.getResource("/images/background.jpg"));
		installImage = new ImageIcon(Images.class.getResource("/images/install.png"));
		installHoverImage = new ImageIcon(Images.class.getResource("/images/install_hover.png"));
		installDisabledImage = new ImageIcon(Images.class.getResource("/images/install_disabled.png"));
		installImage2 = new ImageIcon(Images.class.getResource("/images/install2.png"));
		installHoverImage2 = new ImageIcon(Images.class.getResource("/images/install_hover2.png"));
		installDisabledImage2 = new ImageIcon(Images.class.getResource("/images/install_disabled2.png"));
		installBox = new ImageIcon(Images.class.getResource("/images/install-box.png"));
	}

}
