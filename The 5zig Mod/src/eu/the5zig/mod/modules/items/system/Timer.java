package eu.the5zig.mod.modules.items.system;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer extends StringItem {

	private static final DateFormat formatterSecond = new SimpleDateFormat("ss.SSS");
	private static final DateFormat formatterMinute = new SimpleDateFormat("mm:ss.SSS");
	private static final DateFormat formatterHour = new SimpleDateFormat("HH:mm:ss.SSS");

	@Override
	protected Object getValue(boolean dummy) {
		long time = dummy ? 61500 : The5zigMod.getDataManager().getTimerManager().getTime();
		Date date = new Date(time);
		return time == 0 ? null : time < 60000 ? formatterSecond.format(date) : time < 3600000 ? formatterMinute.format(date) : formatterHour.format(date);
	}

	@Override
	public String getTranslation() {
		return "ingame.timer";
	}
}
