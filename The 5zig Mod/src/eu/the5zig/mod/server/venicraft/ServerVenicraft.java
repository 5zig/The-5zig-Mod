package eu.the5zig.mod.server.venicraft;

import eu.the5zig.mod.server.GameMode;

public class ServerVenicraft {

	public static class Mineathlon extends GameMode {

		private String discipline;
		private int round;

		public String getDiscipline() {
			return discipline;
		}

		public void setDiscipline(String discipline) {
			this.discipline = discipline;
		}

		public int getRound() {
			return round;
		}

		public void setRound(int round) {
			this.round = round;
		}

		@Override
		public String getName() {
			return "Mineathlon";
		}
	}

	public static class CrystalDefense extends GameMode {

		@Override
		public String getName() {
			return "CrystalDefense";
		}
	}

	public static class SurvivalGames extends GameMode {

		@Override
		public String getName() {
			return "SurvivalGames";
		}
	}

}
