package eu.the5zig.mod.server.cytooxien;

import com.google.common.collect.Lists;
import eu.the5zig.mod.server.GameMode;

import java.util.List;

public class ServerCytooxien {

	public static class MarioParty extends GameMode {

		private final List<String> minigameQueue = Lists.newArrayList();
		private long winTime;
		private String firstPlayer;
		private int place;
		private int remainingFields = -20;

		public boolean inventoryRequested;
		public int fieldAnnounceCount;

		public int minigames, first, second, third;

		public MarioParty() {
			super();
			winTime = -1;
		}

		public List<String> getMinigameQueue() {
			return minigameQueue;
		}

		public long getWinTime() {
			return winTime;
		}

		public void setWinTime(long winTime) {
			this.winTime = winTime;
		}

		public String getFirstPlayer() {
			return firstPlayer;
		}

		public void setFirstPlayer(String firstPlayer) {
			this.firstPlayer = firstPlayer;
		}

		public int getPlace() {
			return place;
		}

		public void setPlace(int place) {
			this.place = place;
		}

		public int getRemainingFields() {
			return remainingFields;
		}

		public void setRemainingFields(int remainingFields) {
			this.remainingFields = remainingFields;
		}

		@Override
		public String getName() {
			return "MarioParty";
		}
	}

	public static class Bedwars extends GameMode {
		public int realkills;
		public int bedsdestroyed;
		private boolean canRespawn;
		private String team;

		public Bedwars() {
			this.respawnable = true;
			this.canRespawn = true;
		}

		public String getName() {
			return "Bedwars";
		}

		public void setCanRespawn(boolean respawn) {
			this.canRespawn = respawn;
		}

		public boolean isCanRespawn() {
			return this.canRespawn;
		}

		public String getTeam() {
			return this.team;
		}

		public void setTeam(String pTeam) {
			this.team = pTeam;
		}
	}

}
