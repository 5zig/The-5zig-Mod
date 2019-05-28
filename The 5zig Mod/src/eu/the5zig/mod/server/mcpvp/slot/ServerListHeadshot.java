package eu.the5zig.mod.server.mcpvp.slot;

import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.GuiMCPVPServersScroll;
import eu.the5zig.mod.server.mcpvp.MCPVPServer;
import eu.the5zig.mod.server.mcpvp.ServerMCPVP;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ServerListHeadshot extends GuiMCPVPServersScroll {

	public ServerListHeadshot(Gui lastScreen) {
		super(lastScreen);
	}

	protected void addServers(Set<MCPVPServer> set) {
		for (MCPVPServer server : set) {
			if ((server.isOnline()) && (server.isAcceptingPlayers()) && (server.getServerType().equals(ServerMCPVP.Server.HEADSHOT.getIp()))) {
				this.servers.add(server);
			}
		}
	}

	protected void sort(List<MCPVPServer> servers) {
		Collections.sort(servers, new Comparator<MCPVPServer>() {
			public int compare(MCPVPServer s1, MCPVPServer s2) {
				return s2.getPlayers() - s1.getPlayers();
			}
		});
	}
}
