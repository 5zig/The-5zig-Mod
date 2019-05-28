import eu.the5zig.mod.gui.elements.Row;
import eu.the5zig.mod.util.GuiListChatCallback;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GuiListChat<E extends Row> extends GuiList<E> {

	private final GuiListChatCallback callback;

	public GuiListChat(int width, int height, int top, int bottom, final int left, final int right, int scrollx, List<E> rows, GuiListChatCallback callback) {
		super(null, width, height, top, bottom, left, right, rows);
		this.callback = callback;
		setBottomPadding(6);
		setLeftbound(true);
		setDrawSelection(false);
		setScrollX(scrollx);

		setDrawDefaultBackground(callback.drawDefaultBackground());
		setBackgroundTexture(callback.getResourceLocation(), callback.getImageWidth(), callback.getImageHeight());
	}

	/**
	 * Gets the width of the gui list line
	 *
	 * @return The width of the gui list line
	 */
	@Override
	public int getRowWidth() {
		return getRight() - getLeft() - 5;
	}

	/**
	 * Handle Mouse Input
	 */
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();

		// TODO
//		if (!Display.isActive()) // Don't allow multiple URL-Clicks if Display is not focused.
//			return;
//
//		int mouseX = this.i;
//		int mouseY = this.j;
//
//		if (this.g(mouseY)) {
//			if (Mouse.isButtonDown(0) && this.q()) {
//				int y = mouseY - this.d - this.t + (int) this.n - 4;
//				int id = -1;
//				int minY = -1;
//				// Search for the right ChatLine-ID
//				for (int i1 = 0; i1 < heightMap.size(); i1++) {
//					Integer integer = heightMap.get(i1);
//					Row line = rows.get(i1);
//					if (y >= integer - 2 && y <= integer + line.getLineHeight() - 2) {
//						id = i1;
//						minY = integer;
//						break;
//					}
//				}
//
//				if (id < 0 || id >= rows.size())
//					return;
//
//				callback.chatLineClicked(rows.get(id), mouseX, y, minY, getLeft());
//			}
//		}
	}

}
