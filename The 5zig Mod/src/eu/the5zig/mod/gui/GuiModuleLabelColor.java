package eu.the5zig.mod.gui;

import com.google.common.collect.Lists;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.ButtonRow;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.mod.modules.Module;
import eu.the5zig.mod.modules.ModuleLabelFormatting;
import eu.the5zig.mod.util.ColorSelectorCallback;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Arrays;
import java.util.List;

public class GuiModuleLabelColor extends Gui {

	private static final List<ChatColor> FORMATTINGS = Arrays.asList(ChatColor.RESET, ChatColor.BOLD, ChatColor.ITALIC, ChatColor.UNDERLINE);

	private final Module module;

	private List<ButtonRow> buttons = Lists.newArrayList();
	private List<IButton> colorButtons = Lists.newArrayList();

	public GuiModuleLabelColor(Gui lastScreen, Module module) {
		super(lastScreen);
		this.module = module;
	}

	@Override
	public void initGui() {
		addBottomDoneButton();

		IGuiList guiList = The5zigMod.getVars().createGuiList(null, getWidth(), getHeight(), 32, getHeight() - 48, 0, getWidth(), buttons);
		guiList.setDrawSelection(false);
		addGuiList(guiList);

		buttons.clear();
		buttons.add(new ButtonRow(The5zigMod.getVars().createButton(1, getWidth() / 2 - 75, 0, 150, 20,
				I18n.translate("modules.settings.item.color") + ": " + (module.getLabelFormatting() == null ? I18n.translate("modules.settings.default") : I18n.translate("modules.settings.custom"))),
				null));

		IButton button3 = The5zigMod.getVars().createButton(12, getWidth() / 2 - 155, 0, 150, 20, I18n.translate("modules.settings.item.main_formatting") + ": " +
				(module.getLabelFormatting() == null || module.getLabelFormatting().getMainFormatting() == null ? I18n.translate("modules.settings.default") : module.getLabelFormatting().getMainFormatting().name()));
		IButton button4 = The5zigMod.getVars().createColorSelector(13, getWidth() / 2 + 5, 0, 150, 20, I18n.translate("modules.settings.item.main_color"), new ColorSelectorCallback() {
			@Override
			public ChatColor getColor() {
				return module.getLabelFormatting() == null || module.getLabelFormatting().getMainColor() == null ? ChatColor.WHITE : module.getLabelFormatting().getMainColor();
			}

			@Override
			public void setColor(ChatColor color) {
				module.getLabelFormatting().setMainColor(color);
				The5zigMod.getModuleMaster().save();
			}
		});
		buttons.add(new ButtonRow(button3, button4));
		colorButtons.add(button3);
		colorButtons.add(button4);
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 1) {
			if (module.getLabelFormatting() == null) {
				module.setLabelFormatting(new ModuleLabelFormatting(null, null));
			} else {
				module.setLabelFormatting(null);
			}
			The5zigMod.getModuleMaster().save();
			button.setLabel(I18n.translate("modules.settings.item.color") + ": " +
					(module.getLabelFormatting() == null ? I18n.translate("modules.settings.default") : I18n.translate("modules.settings.custom")));
		}
		if (button.getId() == 12) {
			if (module.getLabelFormatting().getMainFormatting() == null) {
				module.getLabelFormatting().setMainFormatting(FORMATTINGS.get(0));
			} else if (module.getLabelFormatting().getMainFormatting() == FORMATTINGS.get(FORMATTINGS.size() - 1)) {
				module.getLabelFormatting().setMainFormatting(null);
			} else {
				module.getLabelFormatting().setMainFormatting(FORMATTINGS
						.get(FORMATTINGS.indexOf(module.getLabelFormatting().getMainFormatting()) + 1));
			}
			The5zigMod.getModuleMaster().save();
			button.setLabel(I18n.translate("modules.settings.item.main_formatting") + ": " +
					(module.getLabelFormatting() == null || module.getLabelFormatting().getMainFormatting() == null
							? I18n.translate("modules.settings.default") : module.getLabelFormatting().getMainFormatting().name()));
		}
	}

	@Override
	protected void tick() {
		for (IButton colorButton : colorButtons) {
			colorButton.setEnabled(module.getLabelFormatting() != null);
		}
	}

	@Override
	public String getTitleKey() {
		return "modules.settings.title";
	}
}
