package eu.the5zig.mod.gui;

import com.google.common.collect.Lists;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.mod.gui.elements.Row;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;
import java.util.Locale;

public class GuiLanguage extends GuiOptions {

	private List<LanguageRow> languages = Lists.newArrayList();
	private IGuiList languageSlot;
	private int tickCount = 20;

	public GuiLanguage(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		languages.clear();
		addButton(The5zigMod.getVars().createButton(100, getWidth() / 2 - 100, getHeight() - 40, The5zigMod.getVars().translate("gui.done")));

		for (Locale locale : I18n.getLanguages()) {
			languages.add(new LanguageRow(locale));
		}
		languageSlot = The5zigMod.getVars().createGuiList(null, getWidth(), getHeight(), 32, getHeight() - 64, 0, getWidth(), languages);
		languageSlot.setRowWidth(200);
		languageSlot.setScrollX(getWidth() / 2 + 150);
		languageSlot.setSelectedId(I18n.getLanguages().indexOf(I18n.getCurrentLanguage()));
		addGuiList(languageSlot);
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 100) {
			selectLanguage(languageSlot.getSelectedId());
			The5zigMod.getVars().displayScreen(lastScreen);
		}
	}

	private void selectLanguage(int id) {
		if (id < 0 || id >= languages.size())
			return;
		LanguageRow language = languages.get(id);
		I18n.setLanguage(language.getLocale());
		The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.YELLOW + I18n.translate("language.select", I18n.translate("author")));
	}

	@Override
	protected void tick() {
		tickCount++;
		if (tickCount >= 20) {
			tickCount = 0;
			if (I18n.loadLocales()) {
				languages.clear();
				for (Locale locale : I18n.getLanguages()) {
					languages.add(new LanguageRow(locale));
				}
				languageSlot.setSelectedId(I18n.getLanguages().indexOf(I18n.getCurrentLanguage()));
			}
		}
	}

	@Override
	public String getTitleKey() {
		return "language.title";
	}

	class LanguageRow implements Row {

		private Locale locale;

		public LanguageRow(Locale locale) {
			this.locale = locale;
		}

		@Override
		public int getLineHeight() {
			return 18;
		}

		@Override
		public void draw(int x, int y) {
			drawCenteredString(String.format("%s (%s)", locale.getDisplayLanguage(locale), locale.getDisplayCountry(locale)), getWidth() / 2, y + 2);
		}

		public Locale getLocale() {
			return locale;
		}

		@Override
		public String toString() {
			return locale.toString();
		}
	}

}
