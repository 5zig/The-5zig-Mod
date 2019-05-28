package eu.the5zig.mod.modules;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;

import java.util.Locale;

public class ActiveModuleItem implements Row {

	private AbstractModuleItem handle;

	public ActiveModuleItem(AbstractModuleItem handle) {
		this.handle = handle;
	}

	public AbstractModuleItem getHandle() {
		return handle;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("modules.item." + The5zigMod.getModuleItemRegistry().byItem(handle.getClass()).getKey().toLowerCase(
				Locale.ROOT)), 160), x + 2, y + 2);
	}

	@Override
	public int getLineHeight() {
		return 16;
	}
}
