package eu.the5zig.mod.modules.items.server.simplehg;

import com.google.common.collect.Lists;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.render.RenderLocation;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.simplehg.ServerSimpleHG;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SimpleHGMiniFeast extends GameModeItem<ServerSimpleHG.SimpleHG> {

	public SimpleHGMiniFeast() {
		super(ServerSimpleHG.SimpleHG.class, GameState.PREGAME, GameState.GAME);
	}

	@Override
	public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
		The5zigMod.getVars().drawString(getPrefix(I18n.translate("ingame.minifeast")), x, y);
		y += The5zigMod.getVars().getFontHeight();
		for (String line : getRenderList(dummy)) {
			The5zigMod.getVars().drawString(line, x, y);
			y += The5zigMod.getVars().getFontHeight();
		}
	}

	@Override
	public int getWidth(boolean dummy) {
		int maxWidth = 0;
		for (String friend : getRenderList(dummy)) {
			int width = The5zigMod.getVars().getStringWidth(friend);
			if (width > maxWidth) {
				maxWidth = width;
			}
		}
		return maxWidth;
	}

	@Override
	public int getHeight(boolean dummy) {
		return (getRenderList(dummy).size() + 1) * The5zigMod.getVars().getFontHeight();
	}

	private List<String> getRenderList(boolean dummy) {
		String pre = getProperties().buildPrefix("");
		if (dummy) {
			return Arrays.asList(pre + "X: 0 - 100 Z: 0 - -100", pre + "X: 200 - 300 Z: 200 - 100");
		}
		List<String> result = Lists.newArrayList();
		for (Iterator<ServerSimpleHG.MiniFeast> iterator = getGameMode().getMiniFeasts().iterator(); iterator.hasNext(); ) {
			ServerSimpleHG.MiniFeast miniFeast = iterator.next();
			if (System.currentTimeMillis() - miniFeast.getTime() > 1000 * 60 * 5) {
				iterator.remove();
				continue;
			}
			result.add(pre + "X: " + miniFeast.getStart().getX() + " - " + miniFeast.getEnd().getX() + " Z: " + miniFeast.getStart().getY() + " - " + miniFeast.getEnd().getY());
		}
		return result;
	}

	@Override
	protected Object getValue(boolean dummy) {
		return getRenderList(dummy).isEmpty() ? null : 0;
	}
}
