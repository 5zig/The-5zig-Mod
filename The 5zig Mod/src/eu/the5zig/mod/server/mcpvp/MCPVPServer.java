package eu.the5zig.mod.server.mcpvp;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;

public class MCPVPServer implements Row {

	private String ID;
	private boolean IsAcceptingPlayers;
	private String MOTD;
	private String ServerType;
	private int Players;
	private int MaxPlayers;
	private boolean IsOnline;
	private int PingLength;
	private String Server;
	private String Region;
	private String LastPingStart;
	private String LastOnline;

	public int getSeconds() {
		if ((getServerType().equals("mc-hg.com")) || (getServerType().equals("mc-sabotage.com"))) {
			if (this.MOTD.equals("§aStarts in 1 second")) {
				return 1;
			}
			if ((this.MOTD.startsWith("§aStarts in ")) && (this.MOTD.endsWith("seconds"))) {
				return Integer.parseInt(this.MOTD.split("Starts in | seconds")[1]);
			}
			if ((this.MOTD.startsWith("§aStarts in ")) && (this.MOTD.endsWith("minutes"))) {
				return Integer.parseInt(this.MOTD.split("Starts in | minutes")[1]) * 60;
			}
			if (this.MOTD.equals("§aStarts in 1 minute")) {
				return 60;
			}
		}
		return 9999;
	}

	public boolean isAcceptingPlayers() {
		return this.IsAcceptingPlayers;
	}

	public String getMOTD() {
		return this.MOTD;
	}

	public String getServerType() {
		return this.ServerType;
	}

	public int getPlayers() {
		return this.Players;
	}

	public int getMaxPlayers() {
		return this.MaxPlayers;
	}

	public boolean isOnline() {
		return this.IsOnline;
	}

	public MCPVPRegion getRegion() {
		return MCPVPRegion.valueOf(this.Region.toUpperCase());
	}

	public String getIP() {
		return this.Server;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString(getIP(), x + 2, y + 1, 16777215);
		The5zigMod.getVars().drawString(getMOTD(), x + 2, y + 12, 8421504);
		The5zigMod.getVars().drawString(getPlayers() + "/" + getMaxPlayers() + " " + I18n.translate("ingame.players"), x + 2, y + 12 + 10, 8421504);
	}

	@Override
	public int getLineHeight() {
		return 18;
	}

	public enum MCPVPRegion {
		EU, US, BR, NY
	}
}