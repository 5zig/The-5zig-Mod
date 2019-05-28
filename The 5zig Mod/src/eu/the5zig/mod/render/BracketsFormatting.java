package eu.the5zig.mod.render;

public enum BracketsFormatting {

	BRACKETS("[", "]"), BRACKETS_ROUND("(", ")"), COLON("", ":"), ARROW("", ">"), ARROW2("<", ">"), DASH("", " -"), NONE("", "");

	private String first;
	private String last;

	BracketsFormatting(String first, String last) {
		this.first = first;
		this.last = last;
	}

	public BracketsFormatting getNext() {
		return values()[(ordinal() + 1) % values().length];
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public boolean hasFirst() {
		return !first.isEmpty();
	}

}