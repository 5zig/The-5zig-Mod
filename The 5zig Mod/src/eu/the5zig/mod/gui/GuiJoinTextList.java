package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.JoinText;
import eu.the5zig.mod.config.JoinTexts;
import eu.the5zig.mod.gui.elements.Clickable;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.util.minecraft.ChatColor;

public class GuiJoinTextList extends Gui {

	private IGuiList<JoinText> guiList;

	public GuiJoinTextList(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		guiList = The5zigMod.getVars().createGuiList(new Clickable<JoinText>() {
			@Override
			public void onSelect(int id, JoinText row, boolean doubleClick) {
				if (doubleClick) {
					actionPerformed0(getButtonById(2));
				}
			}
		}, getWidth(), getHeight(), 64, getHeight() - 48, 0, getWidth(), The5zigMod.getJoinTextConfiguration().getConfigInstance().getTexts());
		guiList.setRowWidth(220);
		addGuiList(guiList);

		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 - 190, getHeight() - 38, 90, 20, I18n.translate("join_texts.add")));
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 - 95, getHeight() - 38, 90, 20, I18n.translate("join_texts.edit")));
		addButton(The5zigMod.getVars().createButton(3, getWidth() / 2, getHeight() - 38, 90, 20, I18n.translate("join_texts.delete")));
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 + 95, getHeight() - 38, 95, 20, The5zigMod.getVars().translate("gui.back")));
	}

	@Override
	protected void actionPerformed(IButton button) {
		JoinTexts joinTextsConfig = The5zigMod.getJoinTextConfiguration().getConfigInstance();
		if (button.getId() == 1) {
			JoinText joinText = new JoinText();
			joinTextsConfig.getTexts().add(joinText);
			The5zigMod.getVars().displayScreen(new GuiJoinTextEdit(this, joinText));
		}
		JoinText selectedRow = guiList.getSelectedRow();
		if (button.getId() == 2) {
			if (selectedRow == null)
				return;
			The5zigMod.getVars().displayScreen(new GuiJoinTextEdit(this, selectedRow));
		}
		if (button.getId() == 3) {
			if (selectedRow == null)
				return;
			joinTextsConfig.getTexts().remove(selectedRow);
			The5zigMod.getJoinTextConfiguration().saveConfig();
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int y = 0;
		for (String line : The5zigMod.getVars().splitStringToWidth(I18n.translate("join_texts.help"), getWidth() / 4 * 3)) {
			drawCenteredString(ChatColor.GRAY + line, getWidth() / 2, 34 + y);
			y += 10;
		}
	}

	@Override
	public String getTitleKey() {
		return "join_texts.title";
	}
}
