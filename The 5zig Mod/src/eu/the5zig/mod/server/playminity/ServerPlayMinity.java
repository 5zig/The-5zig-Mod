package eu.the5zig.mod.server.playminity;

import eu.the5zig.mod.server.GameMode;

public class ServerPlayMinity {

	public static class JumpLeague extends GameMode {

		private int checkPoint;
		private int maxCheckPoints;
		private int fails;
		private int lives;

		public JumpLeague() {
			this.maxCheckPoints = 10;
			this.lives = 3;
		}

		public int getCheckPoint() {
			return checkPoint;
		}

		public void setCheckPoint(int checkPoint) {
			this.checkPoint = checkPoint;
		}

		public int getMaxCheckPoints() {
			return maxCheckPoints;
		}

		public void setMaxCheckPoints(int maxCheckPoints) {
			this.maxCheckPoints = maxCheckPoints;
		}

		public int getFails() {
			return fails;
		}

		public void setFails(int fails) {
			this.fails = fails;
		}

		public int getLives() {
			return lives;
		}

		public void setLives(int lives) {
			this.lives = lives;
		}

		@Override
		public String getName() {
			return "JumpLeague";
		}
	}

}
