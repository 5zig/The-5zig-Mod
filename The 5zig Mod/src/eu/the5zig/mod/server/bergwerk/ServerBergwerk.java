package eu.the5zig.mod.server.bergwerk;

import eu.the5zig.mod.server.GameMode;

public class ServerBergwerk {

	public static class Flash extends GameMode {

		@Override
		public String getName() {
			return "Flash";
		}
	}

	public static class Duel extends GameMode {

		private String team;
		private boolean canRespawn;
		private long teleporterTimer;


		public Duel() {
			super();
			setRespawnable(true);
			canRespawn = true;
			teleporterTimer = -1;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public boolean isCanRespawn() {
			return canRespawn;
		}

		public void setCanRespawn(boolean canRespawn) {
			this.canRespawn = canRespawn;
		}

		public long getTeleporterTimer() {
			return teleporterTimer;
		}

		public void setTeleporterTimer(long teleporterTimer) {
			this.teleporterTimer = teleporterTimer;
		}

		@Override
		public String getName() {
			return "BedWars Duel";
		}
	}

}
