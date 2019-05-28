package eu.the5zig.mod.manager;

import com.google.common.collect.Lists;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TickEvent;
import eu.the5zig.mod.util.Keyboard;

import java.util.Collections;
import java.util.List;

public class SearchManager {

	private List<SearchEntry> entries = Lists.newArrayList();

	public SearchManager() {
		The5zigMod.getListener().registerListener(this);
	}

	public void onGuiClose() {
		Keyboard.enableRepeatEvents(false);
	}

	public void addSearch(SearchEntry searchEntry, SearchEntry... searchEntries) {
		for (SearchEntry entry : entries) {
			entry.reset();
		}
		entries.clear();
		Keyboard.enableRepeatEvents(true);
		entries.add(searchEntry);
		Collections.addAll(entries, searchEntries);
	}

	public void draw() {
		for (SearchEntry entry : entries) {
			entry.draw();
		}
	}

	public void keyTyped(char character, int code) {
		boolean atLeastOneFocused = false;
		for (SearchEntry entry : entries) {
			if (entry.getTextfield().isFocused()) {
				atLeastOneFocused = true;
				break;
			}
		}
		for (SearchEntry entry : entries) {
			if ((!entry.isVisible() || !entry.getTextfield().isFocused()) && !atLeastOneFocused && !entry.isAlwaysVisible()) {
				entry.setVisible(true);
				// only set visible if key has been successfully typed
				if (!entry.keyTyped(character, code))
					entry.setVisible(false);
				else
					entry.setLastInteractTime(System.currentTimeMillis());
			} else if (entry.isVisible()) {
				if (entry.keyTyped(character, code))
					entry.setLastInteractTime(System.currentTimeMillis());
			}
		}
	}

	public void keyTyped(int key, int scanCode, int modifiers) {
		boolean atLeastOneFocused = false;
		for (SearchEntry entry : entries) {
			if (entry.getTextfield().isFocused()) {
				atLeastOneFocused = true;
				break;
			}
		}
		for (SearchEntry entry : entries) {
			if ((!entry.isVisible() || !entry.getTextfield().isFocused()) && !atLeastOneFocused && !entry.isAlwaysVisible()) {
				entry.setVisible(true);
				// only set visible if key has been successfully typed
				if (!entry.keyTyped(key, scanCode, modifiers))
					entry.setVisible(false);
				else
					entry.setLastInteractTime(System.currentTimeMillis());
			} else if (entry.isVisible()) {
				if (entry.keyTyped(key, scanCode, modifiers))
					entry.setLastInteractTime(System.currentTimeMillis());
			}
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		for (SearchEntry entry : entries) {
			entry.getTextfield().mouseClicked(mouseX, mouseY, button);
		}
	}

	@EventHandler
	public void onTick(TickEvent event) {
		for (SearchEntry entry : entries) {
			if (!entry.getTextfield().isFocused() && entry.getTextfield().getText().isEmpty() && System.currentTimeMillis() - entry.getLastInteractTime() > 1000 * 5 &&
					!entry.isAlwaysVisible()) {
				entry.setVisible(false);
			}
		}
	}
}
