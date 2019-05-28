package eu.the5zig.mod.modules.items.system;

import eu.the5zig.mod.modules.StringItem;

import java.text.DateFormat;

public class Date extends StringItem {

	private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

	@Override
	protected Object getValue(boolean dummy) {
		return dateFormat.format(new java.util.Date());
	}

	@Override
	public String getTranslation() {
		return "ingame.date";
	}
}
