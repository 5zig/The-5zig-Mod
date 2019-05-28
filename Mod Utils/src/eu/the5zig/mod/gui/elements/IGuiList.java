package eu.the5zig.mod.gui.elements;

import java.util.List;

public interface IGuiList<E extends Row> {

	void drawScreen(int mouseX, int mouseY, float partialTicks);

	void handleMouseInput();

	void onSelect(int id, E row, boolean doubleClick);

	void mouseClicked(int mouseX, int mouseY);

	void mouseReleased(int mouseX, int mouseY, int state);

	/**
	 * Currently only called from 1.13+
	 *
	 * @param v
	 * @param v1
	 * @param i
	 * @param v2
	 * @param v3
	 */
	boolean mouseDragged(double v, double v1, int i, double v2, double v3);

	/**
	 * Currently only called from 1.13+
	 *
	 * @param v
	 * @return
	 */
	boolean mouseScrolled(double v);

	void scrollToBottom();

	float getCurrentScroll();

	void scrollTo(float to);

	boolean isSelected(int id);

	int getContentHeight();

	int getRowWidth();

	void setRowWidth(int rowWidth);

	int getSelectedId();

	int setSelectedId(int id);

	E getSelectedRow();

	int getWidth();

	void setWidth(int width);

	int getHeight();

	void setHeight(int height);

	int getHeight(int id);

	int getTop();

	void setTop(int top);

	int getBottom();

	void setBottom(int bottom);

	int getLeft();

	void setLeft(int left);

	int getRight();

	void setRight(int right);

	int getScrollX();

	void setScrollX(int scrollX);

	boolean isLeftbound();

	void setLeftbound(boolean leftbound);

	boolean isDrawSelection();

	void setDrawSelection(boolean drawSelection);

	int getHeaderPadding();

	void setHeaderPadding(int headerPadding);

	String getHeader();

	void setHeader(String header);

	int getBottomPadding();

	void setBottomPadding(int bottomPadding);

	boolean isDrawDefaultBackground();

	void setDrawDefaultBackground(boolean drawDefaultBackground);

	Object getBackgroundTexture();

	void setBackgroundTexture(Object resourceLocation, int imageWidth, int imageHeight);

	List<E> getRows();

	void calculateHeightMap();

	E getHoverItem(int mouseX, int mouseY);

}
