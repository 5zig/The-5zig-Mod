package eu.the5zig.mod.manager;

import eu.the5zig.mod.util.PreciseCounter;

public class CPSManager {

	private PreciseCounter leftClickCounter = new PreciseCounter();
	private PreciseCounter rightClickCounter = new PreciseCounter();

	public PreciseCounter getLeftClickCounter() {
		return leftClickCounter;
	}

	public PreciseCounter getRightClickCounter() {
		return rightClickCounter;
	}
}
