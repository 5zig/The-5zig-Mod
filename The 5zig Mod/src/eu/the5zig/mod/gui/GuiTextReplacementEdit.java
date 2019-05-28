package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.TextReplacement;
import eu.the5zig.mod.gui.elements.IButton;

public class GuiTextReplacementEdit extends Gui {

	private final TextReplacement textReplacement;

	public GuiTextReplacementEdit(Gui lastScreen, TextReplacement textReplacement) {
		super(lastScreen);
		this.textReplacement = textReplacement;
	}

	@Override
	public void initGui() {
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 + 5, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.done")));
		addButton(The5zigMod.getVars().createButton(100, getWidth() / 2 - 155, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.cancel")));

		addTextField(The5zigMod.getVars().createTextfield(1, getWidth() / 2 - 100, getHeight() / 6 + 20, 200, 20, 256));
		addTextField(The5zigMod.getVars().createTextfield(2, getWidth() / 2 - 100, getHeight() / 6 + 70, 200, 20, 256));
		addButton(The5zigMod.getVars().createButton(3, getWidth() / 2 - 100, getHeight() / 6 + 105,
				I18n.translate("text_replacement.edit.ignore_commands", The5zigMod.toBoolean(textReplacement.isIgnoringCommands()))));
		addButton(The5zigMod.getVars().createButton(4, getWidth() / 2 - 100, getHeight() / 6 + 130,
				I18n.translate("text_replacement.edit.replace_inside_words", The5zigMod.toBoolean(textReplacement.isReplaceInsideWords()))));

		getTextfieldById(1).setText(textReplacement.getMessage());
		getTextfieldById(2).setText(textReplacement.getReplacement());
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 200) {
			textReplacement.setMessage(getTextfieldById(1).getText());
			textReplacement.setReplacement(getTextfieldById(2).getText());
			The5zigMod.getTextReplacementConfig().saveConfig();
		}
		if (button.getId() == 100) {
			if (textReplacement.getMessage().isEmpty() || textReplacement.getReplacement().isEmpty()) {
				The5zigMod.getTextReplacementConfig().getConfigInstance().getReplacements().remove(textReplacement);
				The5zigMod.getTextReplacementConfig().saveConfig();
			}
			The5zigMod.getVars().displayScreen(lastScreen);
		}
		if (button.getId() == 3) {
			textReplacement.setIgnoringCommands(!textReplacement.isIgnoringCommands());
			button.setLabel(I18n.translate("text_replacement.edit.ignore_commands", The5zigMod.toBoolean(textReplacement.isIgnoringCommands())));
		}
		if (button.getId() == 4) {
			textReplacement.setReplaceInsideWords(!textReplacement.isReplaceInsideWords());
			button.setLabel(I18n.translate("text_replacement.edit.replace_inside_words", The5zigMod.toBoolean(textReplacement.isReplaceInsideWords())));
		}
	}

	@Override
	protected void tick() {
		getButtonById(200).setEnabled(!getTextfieldById(1).getText().isEmpty() && !getTextfieldById(2).getText().isEmpty());
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		The5zigMod.getVars().drawString(I18n.translate("text_replacement.edit.message"), getWidth() / 2 - 100, getHeight() / 6 + 8);
		The5zigMod.getVars().drawString(I18n.translate("text_replacement.edit.replacement"), getWidth() / 2 - 100, getHeight() / 6 + 58);
	}

	@Override
	public String getTitleKey() {
		return "text_replacement.edit.title";
	}
}
