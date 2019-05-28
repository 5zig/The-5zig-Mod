package eu.the5zig.mod.server.mcpvp;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.LastServer;
import eu.the5zig.mod.listener.Listener;
import eu.the5zig.mod.manager.DataManager;
import eu.the5zig.mod.server.Server;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.Locale;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class CTFListener extends Listener {

	private int msgCount = 0;

	@Override
	public void onServerJoin(String host, int port) {
		DataManager dataManager = The5zigMod.getDataManager();
		LastServer config = The5zigMod.getLastServerConfig().getConfigInstance();

		if (host.toLowerCase(Locale.ROOT).endsWith("mcctf.com")) {
			if (config.getLastServer() != null && config.getLastServer().getHost().equals(host) && !host.equalsIgnoreCase("mcctf.com") && config.getLastServer() instanceof ServerCTF) {
				dataManager.setServer(config.getLastServer());
				return;
			}
			dataManager.setServer(new ServerCTF(host, port));
		}
	}

	@Override
	public boolean onServerChat(String message) {
		Server currentServer = The5zigMod.getDataManager().getServer();
		if (currentServer == null)
			return false;

		if (currentServer instanceof ServerCTF) {
			message = ChatColor.stripColor(message);
			ServerCTF server = (ServerCTF) currentServer;
			if (message.startsWith("You are on team ")) {
				if (message.equals("You are on team Red"))
					server.setTeam(ServerCTF.EnumCTFTeam.RED);
				if (message.equals("You are on team Blue"))
					server.setTeam(ServerCTF.EnumCTFTeam.BLUE);
			}
			if (message.startsWith("> Selected ")) {
				String kit = message.split("> Selected ")[1];
				server.setKit(kit);
			}
			if (message.startsWith("Captures: ") && msgCount == 0) {
				ServerCTF.CTFTeam redteam = server.getRedTeam();
				String[] args = message.split("Captures: |   Flag: |   Players: ");
				redteam.setCaptures(Integer.parseInt(args[1].split("/")[0]));
				redteam.setMaxCaptures(Integer.parseInt(args[1].split("/")[1]));
				redteam.setFlag(args[2].replace("[", "").replace("]", "").replace("", "").replace("", ""));
				msgCount = 1;
			} else if (message.startsWith("Captures: ") && msgCount == 1) {
				ServerCTF.CTFTeam blueTeam = server.getBlueTeam();
				String[] args = message.split("Captures: |   Flag: |   Players: ");
				blueTeam.setCaptures(Integer.parseInt(args[1].split("/")[0]));
				blueTeam.setMaxCaptures(Integer.parseInt(args[1].split("/")[1]));
				blueTeam.setFlag(args[2].replace("[", "").replace("]", "").replace("", "").replace("", ""));
				msgCount = 0;
			}
			if (message.startsWith("Kills: ")) {
				String[] args = message.split("Kills: | \\(| in a row\\)   Deaths: |   Recoverties: |   Captures: ");
				server.setKills(Integer.parseInt(args[1]));
				server.setKillstreak(Integer.parseInt(args[2]));
				server.setDeaths(Integer.parseInt(args[3]));
				server.setRecovers(Integer.parseInt(args[4]));
				server.setCaptures(Integer.parseInt(args[5]));
			}
			if (message.contains("recovered")) {
				ServerCTF.EnumCTFTeam team = message.endsWith("Blue's flag!") ? ServerCTF.EnumCTFTeam.BLUE : ServerCTF.EnumCTFTeam.RED;
				String player = message.split("||")[1];
				String username = The5zigMod.getDataManager().getUsername();
				if (server.getTeam() == team && player.equals(username)) {
					server.setRecovers(server.getRecovers() + 1);
				}
			}
		}
		return false;
	}
}
