package eu.the5zig.mod.server.mcpvp;

public class MiniFeast {

	private long timeStarted;
	private int xmin, xmax, zmin, zmax;

	public MiniFeast(int xmin, int xmax, int zmin, int zmax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.zmin = zmin;
		this.zmax = zmax;
		this.timeStarted = System.currentTimeMillis();
	}

	public boolean isOver() {
		return System.currentTimeMillis() - timeStarted > 1000 * 90;
	}

	public String getCoordinates() {
		return xmin + " - " + xmax + ", " + zmin + " - " + zmax;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof MiniFeast && getCoordinates().equals(((MiniFeast) obj).getCoordinates());
	}
}