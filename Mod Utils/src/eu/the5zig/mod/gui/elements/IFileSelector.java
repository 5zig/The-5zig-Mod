package eu.the5zig.mod.gui.elements;

import java.io.File;

public interface IFileSelector {

	File getCurrentDir();

	File getSelectedFile();

	void updateDir(File dir);

	void goUp();

	void draw(int mouseX, int mouseY, float partialTicks);

	void handleMouseInput();

}
