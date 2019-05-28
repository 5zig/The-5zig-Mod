package eu.the5zig.mod.gui.ingame;

public interface IGui2ndChat {

	void draw(int updateCounter);

	void printChatMessage(String message);

	void printChatMessage(Object chatComponent);

	void clear();

	void refreshChat();

	int getLineCount();

	void scroll(int scroll);

	void resetScroll();

	void drawComponentHover(int mouseX, int mouseY);

	boolean mouseClicked(int mouseX, int mouseY, int button);

	void keyTyped(int code);

}
