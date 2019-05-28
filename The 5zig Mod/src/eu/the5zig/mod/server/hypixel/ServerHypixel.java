package eu.the5zig.mod.server.hypixel;

import eu.the5zig.mod.server.GameMode;

public class ServerHypixel {

	public static class Quake extends GameMode {

		public Quake() {
			super();
			setRespawnable(true);
		}

		@Override
		public String getName() {
			return "Quakecraft";
		}
	}

	public static class Blitz extends GameMode {

		private String kit;
		private long star;
		private long deathmatch;

		public Blitz() {
			super();
			star = -1;
			deathmatch = -1;
		}

		public String getKit() {
			return kit;
		}

		public void setKit(String kit) {
			this.kit = kit;
		}

		public long getStar() {
			return star;
		}

		public void setStar(long star) {
			this.star = star;
		}

		public long getDeathmatch() {
			return deathmatch;
		}

		public void setDeathmatch(long deathmatch) {
			this.deathmatch = deathmatch;
		}

		@Override
		public String getName() {
			return "BlitzSG";
		}
	}

	public static class Paintball extends GameMode {

		private String team;

		public Paintball() {
			super();
			setRespawnable(true);
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		@Override
		public String getName() {
			return "PaintBall";
		}
	}
}
