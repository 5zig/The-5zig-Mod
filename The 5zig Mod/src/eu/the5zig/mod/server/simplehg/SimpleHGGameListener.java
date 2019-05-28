package eu.the5zig.mod.server.simplehg;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.mod.util.Vector2i;
import eu.the5zig.mod.util.Vector3i;
import eu.the5zig.util.minecraft.ChatColor;

public class SimpleHGGameListener extends AbstractGameListener<ServerSimpleHG.SimpleHG> {

	@Override
	public Class<ServerSimpleHG.SimpleHG> getGameMode() {
		return ServerSimpleHG.SimpleHG.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return !lobby.startsWith("hub");
	}

	@Override
	public void onMatch(ServerSimpleHG.SimpleHG gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("start.min")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000 * 60);
			}
			if (key.equals("start.sec")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("started")) {
				gameMode.setState(GameState.PREGAME);
				gameMode.setTime(System.currentTimeMillis());
			}
			if (key.equals("kit")) {
				gameMode.setKit(match.get(0));
			}
		}
		if (gameMode.getState() == GameState.PREGAME || gameMode.getState() == GameState.GAME) {
			if (key.equals("invincibility.min")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000 * 60);
			}
			if (key.equals("invincibility.sec")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("invincibility.off")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis() - 1000 * 60 * 2);
			}
			if (key.equals("kill.1") || key.equals("kill.2") || key.equals("kill.3")) {
				if (The5zigMod.getDataManager().getUsername().equals(match.get(1))) {
					gameMode.setKills(gameMode.getKills() + 1);
					gameMode.setKillStreak(gameMode.getKillStreak() + 1);
				}
				gameMode.getKitMap().put(match.get(1), match.get(2));
			}
			if (key.equals("compass.target") && The5zigMod.getConfig().getBool("showCompassTarget")) {
				String target = match.get(0);
				String str = ChatColor.YELLOW + "Compass pointing at " + target;
				if (gameMode.getKitMap().containsKey(target)) {
					str += " (" + gameMode.getKitMap().get(target) + ")";
				}
				The5zigMod.getGuiIngame().showTextAboveHotbar(str);
				match.ignoreMessage(true);
			}
			if (key.equals("compass.feast") && The5zigMod.getConfig().getBool("showCompassTarget")) {
				The5zigMod.getGuiIngame().showTextAboveHotbar(ChatColor.YELLOW + "Compass pointing at feast");
			}
			if (key.equals("compass.no_targets") && The5zigMod.getConfig().getBool("showCompassTarget")) {
				The5zigMod.getGuiIngame().showTextAboveHotbar(ChatColor.YELLOW + "Compass pointing towards spawn");
			}
			if (key.equals("win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
			if (key.equals("minifeast")) {
				gameMode.getMiniFeasts().add(new ServerSimpleHG.MiniFeast(new Vector2i(Integer.parseInt(match.get(0)), Integer.parseInt(match.get(2))),
						new Vector2i(Integer.parseInt(match.get(1)), Integer.parseInt(match.get(3)))));
			}
			if (key.equals("feast.min")) {
				gameMode.setFeast(new ServerSimpleHG.Feast(new Vector3i(Integer.parseInt(match.get(0)), Integer.parseInt(match.get(1)), Integer.parseInt(match.get(2))), System
						.currentTimeMillis() + 1000 * 60 * Integer.parseInt(match.get(3))));
			}
			if (key.equals("feast.sec")) {
				gameMode.setFeast(new ServerSimpleHG.Feast(new Vector3i(Integer.parseInt(match.get(0)), Integer.parseInt(match.get(1)), Integer.parseInt(match.get(2))), System
						.currentTimeMillis() + 1000 * Integer.parseInt(match.get(3))));
			}
		}
	}
}
