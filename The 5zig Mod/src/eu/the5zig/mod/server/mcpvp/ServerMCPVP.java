package eu.the5zig.mod.server.mcpvp;

import eu.the5zig.mod.server.Server;

public class ServerMCPVP extends Server {

	public ServerMCPVP(String host, int port) {
		super(host, port);
	}

	public enum Server {

		HUB("hub.mcpvp.com"), VIP_HUB("vip.mcpvp.com"), MVP_HUB("mvp.mcpvp.com"), PRO_HUB("pro.mcpvp.com"), HARDCORE_GAMES("mc-hg.com"), HARDCORE_GAMES_NO_SOUP("nosoup.mc-hg.com"), RAID(
				"raid" + ".mcpvp.com"), CAPTURE_THE_FLAG("mcctf.com"), KITPVP("kitpvp.us"), SABOTAGE("mc-sabotage.com"), HEADSHOT("mcheadshot.com"), MAZERUNNER("mc-maze.com"),
		MINECRAFT_BUILD("minecraftbuild.com"), PARKOUR("parkour.mcpvp.com"), PVPDOJO("hub.pvpdojo.com"), SIEGE("mcsiege.com");

		private String ip;

		Server(String ip) {
			this.ip = ip;
		}

		public String getIp() {
			return ip;
		}

		@Override
		public String toString() {
			return ip;
		}
	}

}