package eu.the5zig.mod.gui.elements;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface Clickable<E extends Row> {

	/**
	 * Called when a row in a GuiList has been selected.
	 *
	 * @param id          The id of the Row.
	 * @param row         The Row itself.
	 * @param doubleClick If the Row has been double clicked.
	 */
	void onSelect(int id, E row, boolean doubleClick);
}
