package eu.the5zig.mod.server.timolia;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

public class TimoliaArcadeListener extends AbstractGameListener<ServerTimolia.Arcade> {

	private boolean sentGameRotationRequest = false;
	private boolean receivedGameRotationStart = false;

	@Override
	public Class<ServerTimolia.Arcade> getGameMode() {
		return ServerTimolia.Arcade.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("arcade");
	}

	@Override
	public void onGameModeJoin(ServerTimolia.Arcade gameMode) {
		sendGameListRequest();
	}

	@Override
	public boolean onServerChat(ServerTimolia.Arcade gameMode, String message) {
		message = ChatColor.stripColor(message);
		if (message.equals("\u2502 Arcade\u00bb Spielrotation (kann abweichen):")) {
			receivedGameRotationStart = true;
			return sentGameRotationRequest;
		}
		if (receivedGameRotationStart) {
			if (message.startsWith("\u2502 Next:")) {
				gameMode.setNextMiniGame(message.replace("\u2502 Next: ", ""));
			}
			if (message.startsWith("\u2502     \u2514")) {
				boolean sent = sentGameRotationRequest;
				sentGameRotationRequest = false;
				receivedGameRotationStart = false;
				return sent;
			}
			return sentGameRotationRequest;
		}
		return false;
	}

	@Override
	public void onMatch(ServerTimolia.Arcade gameMode, String key, IPatternResult match) {
		if (key.equals("starting.actionbar")) {
			gameMode.setWinner(null);
			gameMode.setState(GameState.STARTING);
			gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
		}
		if (key.equals("arcade.end")) {
			gameMode.setState(GameState.ENDGAME);
		}
		if (key.equals("arcade.win") && gameMode.getState() == GameState.ENDGAME) {
			gameMode.setWinner(match.get(0));
		}
	}

	@Override
	public void onTitle(ServerTimolia.Arcade gameMode, String title, String subTitle) {
		if (title == null || subTitle == null || title.isEmpty())
			return;
		String miniGame = ChatColor.stripColor(title);

		if (miniGame.isEmpty() || miniGame.equals(gameMode.getCurrentMiniGame()))
			return;

		gameMode.setCurrentMiniGame(miniGame);
		gameMode.setNextMiniGame(null);

		sendGameListRequest();
	}

	@Override
	public void onTick(ServerTimolia.Arcade gameMode) {
		if (gameMode.getState() == GameState.STARTING) {
			if (System.currentTimeMillis() - gameMode.getTime() > 0) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
	}

	private void sendGameListRequest() {
		sentGameRotationRequest = true;
		receivedGameRotationStart = false;
		The5zigMod.getVars().sendMessage("/getgamerotation");
	}
}
