package eu.the5zig.mod.manager;

import eu.the5zig.mod.util.Vector2i;

public class CoordinateClipboard {

	private Vector2i location;
	private CoordinateClipboard previous;
	private CoordinateClipboard next;

	public CoordinateClipboard(CoordinateClipboard previous) {
		this.previous = previous;
	}

	public Vector2i getLocation() {
		return location;
	}

	public void setLocation(Vector2i location) {
		this.location = location;
	}

	public CoordinateClipboard getPrevious() {
		return previous;
	}

	public void setPrevious(CoordinateClipboard previous) {
		this.previous = previous;
	}

	public CoordinateClipboard getNext() {
		return next;
	}

	public void setNext(CoordinateClipboard next) {
		this.next = next;
	}
}
