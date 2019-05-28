package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.ChatFilter;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.mod.gui.elements.ITextfield;
import eu.the5zig.mod.manager.SearchEntry;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Locale;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GuiChatFilter extends GuiOptions {

	private IGuiList<ChatFilter.ChatFilterMessage> guiList;

	public GuiChatFilter(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 - 190, getHeight() - 38, 90, 20, I18n.translate("chat_filter.add")));
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 - 95, getHeight() - 38, 90, 20, I18n.translate("chat_filter.edit")));
		addButton(The5zigMod.getVars().createButton(3, getWidth() / 2, getHeight() - 38, 90, 20, I18n.translate("chat_filter.delete")));
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 + 95, getHeight() - 38, 95, 20, The5zigMod.getVars().translate("gui.back")));

		guiList = The5zigMod.getVars().createGuiList(null, getWidth(), getHeight(), 64, getHeight() - 50, 0, getWidth(),
				The5zigMod.getChatFilterConfig().getConfigInstance().getChatMessages());
		guiList.setRowWidth(getWidth() - 30);
		guiList.setScrollX(getWidth() / 2 + 150);
		addGuiList(guiList);

		ITextfield textfield = The5zigMod.getVars().createTextfield(I18n.translate("gui.search"), 11, 14, getHeight() - 76, 150, 20, 150);
		The5zigMod.getDataManager().getSearchManager().addSearch(new SearchEntry<ChatFilter.ChatFilterMessage>(textfield, The5zigMod.getChatFilterConfig().getConfigInstance().getChatMessages()) {
			@Override
			public boolean filter(String text, ChatFilter.ChatFilterMessage o) {
				return (o.getName() != null && o.getName().toLowerCase(Locale.ROOT).startsWith(text.toLowerCase(Locale.ROOT)))
						|| o.getMessage().toLowerCase(Locale.ROOT).startsWith(text.toLowerCase(Locale.ROOT));
			}
		});
	}

	@Override
	protected void tick() {
		boolean selected = guiList.getSelectedRow() != null;

		getButtonById(2).setEnabled(selected);
		getButtonById(3).setEnabled(selected);
	}

	@Override
	protected void actionPerformed(IButton button) {
		ChatFilter chatMessagesConfig = The5zigMod.getChatFilterConfig().getConfigInstance();
		if (button.getId() == 1) {
			ChatFilter.ChatFilterMessage chatMessage = chatMessagesConfig.new ChatFilterMessage("", ChatFilter.Action.IGNORE);
			chatMessagesConfig.getChatMessages().add(chatMessage);
			The5zigMod.getVars().displayScreen(new GuiEditChatMessage(this, chatMessage));
		}
		ChatFilter.ChatFilterMessage selectedRow = guiList.getSelectedRow();
		if (button.getId() == 2) {
			if (selectedRow == null)
				return;
			The5zigMod.getVars().displayScreen(new GuiEditChatMessage(this, selectedRow));
		}
		if (button.getId() == 3) {
			if (selectedRow == null)
				return;
			chatMessagesConfig.getChatMessages().remove(selectedRow);
			The5zigMod.getChatFilterConfig().saveConfig();
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int y = 0;
		for (String line : The5zigMod.getVars().splitStringToWidth(I18n.translate("chat_filter.help"), getWidth() / 4 * 3)) {
			drawCenteredString(ChatColor.GRAY + line, getWidth() / 2, 34 + y);
			y += 10;
		}
		The5zigMod.getDataManager().getSearchManager().draw();
	}

	@Override
	protected void onKeyType(char character, int key) {
		The5zigMod.getDataManager().getSearchManager().keyTyped(character, key);
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		The5zigMod.getDataManager().getSearchManager().mouseClicked(x, y, button);
	}

	@Override
	public String getTitleKey() {
		return "chat_filter.title";
	}
}
