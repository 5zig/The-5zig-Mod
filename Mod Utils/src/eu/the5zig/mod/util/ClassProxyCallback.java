package eu.the5zig.mod.util;

import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.manager.SearchEntry;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public interface ClassProxyCallback {

	String getVersion();

	String getMinecraftVersion();

	Logger getLogger();

	File getModDirectory();

	String getLastServer();

	String translate(String key, Object... format);

	void displayGuiSettings(Gui lastScreen);

	boolean isShowLastServer();

	int getMaxChatLines();

	boolean is2ndChatTextLeftbound();

	boolean is2ndChatVisible();

	float get2ndChatOpacity();

	float get2ndChatWidth();

	float get2ndChatHeightFocused();

	float get2ndChatHeightUnfocused();

	float get2ndChatScale();

	boolean isShowTimeBeforeChatMessage();

	boolean isChatBackgroundTransparent();

	Object getChatComponentWithTime(Object originalChatComponent);

	void resetServer();

	boolean isRenderCustomModels();

	void checkAutoreconnectCountdown(int width, int height);

	void setAutoreconnectServerData(Object serverData);

	void launchCrashHopper(Throwable cause, File crashFile);

	void addSearch(SearchEntry searchEntry, SearchEntry... searchEntries);

	void renderSnow(int width, int height);

	void disableTray();

	boolean isTrayEnabled();

	List<String> getModList();

	String getChatSearchText();

	List<String> getHighlightWords();

	int getHighlightWordsColor();

	boolean shouldCancelChatMessage(String message, Object chatComponent);

	int getOverlayTexture();

	void fireKeyPressEvent(int keyCode);

}
