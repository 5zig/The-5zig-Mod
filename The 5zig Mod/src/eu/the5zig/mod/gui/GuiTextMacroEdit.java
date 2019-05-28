package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.TextMacro;
import eu.the5zig.mod.gui.elements.IButton;

import java.util.Collections;

public class GuiTextMacroEdit extends Gui {

	private final TextMacro textMacro;
	private boolean pressed;
	private int tickCount;
	private long lastPressed;

	public GuiTextMacroEdit(Gui lastScreen, TextMacro textMacro) {
		super(lastScreen);
		this.textMacro = textMacro;
	}

	@Override
	public void initGui() {
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 + 5, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.done")));
		addButton(The5zigMod.getVars().createButton(100, getWidth() / 2 - 155, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.cancel")));

		addTextField(The5zigMod.getVars().createTextfield(1, getWidth() / 2 - 100, getHeight() / 6 + 20, 200, 20, 256));
		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 - 100, getHeight() / 6 + 70, 200, 20, I18n.translate("text_macros.edit.macro.press")));

		getTextfieldById(1).setText(textMacro.getMessage());
		if (!textMacro.getKeys().isEmpty()) {
			getButtonById(1).setLabel(textMacro.getKeysAsString());
		}
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 - 100, getHeight() / 6 + 100, 200, 20,
				I18n.translate("text_macros.edit.macro.auto_send") + ": " + The5zigMod.toBoolean(textMacro.isAutoSend())));
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 200) {
			textMacro.setMessage(getTextfieldById(1).getText());
			The5zigMod.getTextMacroConfiguration().saveConfig();
		}
		if (button.getId() == 100) {
			if (textMacro.getMessage().isEmpty() || textMacro.getKeys().isEmpty()) {
				The5zigMod.getTextMacroConfiguration().getConfigInstance().getMacros().remove(textMacro);
				The5zigMod.getTextMacroConfiguration().saveConfig();
			}
			The5zigMod.getVars().displayScreen(lastScreen);
		}
		if (button.getId() == 1) {
			pressed = !pressed;
			if (pressed) {
				textMacro.getKeys().clear();
				button.setLabel(I18n.translate("text_macros.edit.macro.listening"));
			} else {
				if (textMacro.getKeys().isEmpty()) {
					button.setLabel(I18n.translate("text_macros.edit.macro.press"));
				} else {
					button.setLabel(textMacro.getKeysAsString());
					The5zigMod.getTextMacroConfiguration().saveConfig();
				}
			}
		}
		if (button.getId() == 2) {
			textMacro.setAutoSend(!textMacro.isAutoSend());
			button.setLabel(I18n.translate("text_macros.edit.macro.auto_send") + ": " + The5zigMod.toBoolean(textMacro.isAutoSend()));
			The5zigMod.getTextMacroConfiguration().saveConfig();
		}
	}

	@Override
	protected void tick() {
		getButtonById(200).setEnabled(!getTextfieldById(1).getText().isEmpty() && !textMacro.getKeys().isEmpty());

		if (pressed && System.currentTimeMillis() - lastPressed > 1000) {
			tickCount++;
			if (tickCount / 50 % 2 == 0) {
				getButtonById(1).setLabel(I18n.translate("text_macros.edit.macro.listening"));
			} else {
				getButtonById(1).setLabel(I18n.translate("text_macros.edit.macro.stop"));
			}
		}
	}

	@Override
	protected void onKeyType(char character, int eventKey) {
		if (!pressed) {
			return;
		}
		if (!textMacro.getKeys().contains(eventKey)) {
			textMacro.getKeys().add(eventKey);
			lastPressed = System.currentTimeMillis();
			getButtonById(1).setLabel(I18n.translate("text_macros.edit.macro.listening"));
			tickCount = 0;
			Collections.sort(textMacro.getKeys());
			if (textMacro.getKeys().size() >= 5) {
				pressed = false;
				if (textMacro.getKeys().isEmpty()) {
					getButtonById(1).setLabel(I18n.translate("text_macros.edit.macro.press"));
				} else {
					getButtonById(1).setLabel(textMacro.getKeysAsString());
				}
			}
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		The5zigMod.getVars().drawString(I18n.translate("text_macros.edit.message"), getWidth() / 2 - 100, getHeight() / 6 + 8);
		The5zigMod.getVars().drawString(I18n.translate("text_macros.edit.macro"), getWidth() / 2 - 100, getHeight() / 6 + 58);

		if (pressed) {
			Gui.drawCenteredString(textMacro.getKeysAsString(), getWidth() / 2, getHeight() / 6 + 100);
		}
	}

	@Override
	public String getTitleKey() {
		return "text_macros.edit.title";
	}
}
