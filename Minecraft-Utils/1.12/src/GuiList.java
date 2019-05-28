import com.google.common.collect.Lists;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.*;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.minecraft.ChatColor;
import org.lwjgl.input.Mouse;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GuiList<E extends Row> extends bjp implements IGuiList<E> {

	protected final List<E> rows;
	private final Clickable<E> clickable;

	private int rowWidth = 95;
	private int bottomPadding;
	private boolean leftbound = false;
	private int scrollX;

	private int selected;
	private IButton selectedButton;

	private String header;

	private boolean drawDefaultBackground = false;
	private Object backgroundTexture;
	private int backgroundWidth, backgroundHeight;

	protected List<Integer> heightMap = Lists.newArrayList();

	public GuiList(Clickable<E> clickable, int width, int height, int top, int bottom, int left, int right, List<E> rows) {
		super(((Variables) MinecraftFactory.getVars()).getMinecraft(), width, height, top, bottom, 18);

		this.rows = rows;
		this.clickable = clickable;
		setLeft(left);
		setRight(right);
	}

	/**
	 * @return the size of all rows
	 */
	@Override
	protected int b() {
		synchronized (rows) {
			return rows.size();
		}
	}

	@Override
	protected int k() {
		return getContentHeight();
	}

	/**
	 * Element clicked
	 *
	 * @param id          The id of the Row.
	 * @param doubleClick If the Row has been Double-Clicked.
	 * @param mouseX      The x-coordinate of the Mouse.
	 * @param mouseY      The y-coordinate of the Mouse.
	 */
	@Override
	protected void a(int id, boolean doubleClick, int mouseX, int mouseY) {
		selected = id;
		boolean var5 = (GuiList.this.selected >= 0) && (GuiList.this.selected < b());
		if (var5) {
			if (clickable != null) {
				synchronized (rows) {
					onSelect(id, rows.get(id), doubleClick);
				}
			}
		}
	}

	@Override
	public void onSelect(int id, E row, boolean doubleClick) {
		setSelectedId(id);
		if (clickable != null && row != null)
			clickable.onSelect(id, row, doubleClick);
	}

	@Override
	protected boolean a(int id) {
		return isSelected(id);
	}

	@Override
	public boolean isSelected(int id) {
		return selected == id;
	}

	/**
	 * drawBackground
	 */
	@Override
	protected void a() {
	}

	@Override
	public int c() {
		return getRowWidth();
	}

	@Override
	protected void a(int id, int x, int y, int slotHeight, int mouseX, int mouseY, float partialTicks) {
		drawSlot(id, x, y, slotHeight, mouseX, mouseY);
	}

	protected void drawSlot(int id, int x, int y, int slotHeight, int mouseX, int mouseY) {
		synchronized (rows) {
			if (id < 0 || id >= rows.size())
				return;
			Row selectedRow = rows.get(id);
			selectedRow.draw(x, y);
			if (selectedRow instanceof RowExtended) {
				((RowExtended) selectedRow).draw(x, y, slotHeight, mouseX, mouseY);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		a(mouseX, mouseY, partialTicks);
		getSelectedRow();
	}

	/**
	 * Draw Screen
	 */
	@Override
	public void a(int mouseX, int mouseY, float partialTicks) {
		calculateHeightMap();
		if (this.q) {
			this.i = mouseX;
			this.j = mouseY;
			this.a();
			int var3 = this.d();
			int var4 = var3 + 6;
			this.l();
			buq.g();
			buq.p();
			bvc tesselator = bvc.a();
			bui worldRenderer = tesselator.c();
			buq.c(1.0F, 1.0F, 1.0F, 1.0F);
			if (backgroundTexture != null) {
				MinecraftFactory.getVars().bindTexture(backgroundTexture);
				Gui.drawModalRectWithCustomSizedTexture(getLeft(), getTop(), 0, 0, getRight() - getLeft(), getBottom() - getTop(), backgroundWidth, backgroundHeight);
			} else if (drawDefaultBackground || MinecraftFactory.getVars().isPlayerNull()) {
				MinecraftFactory.getVars().bindTexture(bio.b);
				float var7 = 32.0F;
				worldRenderer.a(7, cdw.i);
				worldRenderer.b((double) this.g, (double) this.e, 0.0D).a((double) ((float) this.g / var7), (double) ((float) (this.e + (int) this.n) / var7)).b(32, 32, 32, 255).d();
				worldRenderer.b((double) this.f, (double) this.e, 0.0D).a((double) ((float) this.f / var7), (double) ((float) (this.e + (int) this.n) / var7)).b(32, 32, 32, 255).d();
				worldRenderer.b((double) this.f, (double) this.d, 0.0D).a((double) ((float) this.f / var7), (double) ((float) (this.d + (int) this.n) / var7)).b(32, 32, 32, 255).d();
				worldRenderer.b((double) this.g, (double) this.d, 0.0D).a((double) ((float) this.g / var7), (double) ((float) (this.d + (int) this.n) / var7)).b(32, 32, 32, 255).d();
				tesselator.b();
			}
			int var8 = this.g + this.b / 2 - this.c() / 2 + 2;
			int var9 = this.d + 4 - (int) this.n;
			glEnable(GL_SCISSOR_TEST);
			float scaleFactor = MinecraftFactory.getVars().getScaleFactor();
			glScissor((int) Math.ceil(getLeft() * scaleFactor), (int) Math.ceil((getHeight() - getBottom()) * scaleFactor), (int) Math.floor((getRight() - getLeft()) * scaleFactor),
					(int) Math.floor((getBottom() - getTop()) * scaleFactor));
			if (this.s) {
				this.a(var8, var9, tesselator);
			}
			this.a(var8, var9, mouseX, mouseY, partialTicks);
			glDisable(GL_SCISSOR_TEST);
			byte var10 = 4;
			buq.m();
			buq.a(770, 771, 0, 1);
			buq.d();
			buq.j(7425);
			buq.z();
			// Schatten
			int var11 = this.m();
			if (drawDefaultBackground || MinecraftFactory.getVars().isPlayerNull() || var11 > 0) {
				worldRenderer.a(7, cdw.i);
				worldRenderer.b((double) this.g, (double) (this.d + var10), 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 0).d();
				worldRenderer.b((double) this.f, (double) (this.d + var10), 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 0).d();
				worldRenderer.b((double) this.f, (double) this.d, 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) this.g, (double) this.d, 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 255).d();
				tesselator.b();
				worldRenderer.a(7, cdw.i);
				worldRenderer.b((double) this.g, (double) this.e, 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) this.f, (double) this.e, 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) this.f, (double) (this.e - var10), 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 0).d();
				worldRenderer.b((double) this.g, (double) (this.e - var10), 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 0).d();
				tesselator.b();
			}
			if (var11 > 0) {
				int var12 = (this.e - this.d) * (this.e - this.d) / this.k();
				var12 = ri.a(var12, 32, this.e - this.d - 8);
				int var13 = (int) this.n * (this.e - this.d - var12) / var11 + this.d;
				if (var13 < this.d) {
					var13 = this.d;
				}

				worldRenderer.a(7, cdw.i);
				worldRenderer.b((double) var3, (double) this.e, 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) var4, (double) this.e, 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) var4, (double) this.d, 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) var3, (double) this.d, 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 255).d();
				tesselator.b();
				worldRenderer.a(7, cdw.i);
				worldRenderer.b((double) var3, (double) (var13 + var12), 0.0D).a(0.0D, 1.0D).b(128, 128, 128, 255).d();
				worldRenderer.b((double) var4, (double) (var13 + var12), 0.0D).a(1.0D, 1.0D).b(128, 128, 128, 255).d();
				worldRenderer.b((double) var4, (double) var13, 0.0D).a(1.0D, 0.0D).b(128, 128, 128, 255).d();
				worldRenderer.b((double) var3, (double) var13, 0.0D).a(0.0D, 0.0D).b(128, 128, 128, 255).d();
				tesselator.b();
				worldRenderer.a(7, cdw.i);
				worldRenderer.b((double) var3, (double) (var13 + var12 - 1), 0.0D).a(0.0D, 1.0D).b(192, 192, 192, 255).d();
				worldRenderer.b((double) (var4 - 1), (double) (var13 + var12 - 1), 0.0D).a(1.0D, 1.0D).b(192, 192, 192, 255).d();
				worldRenderer.b((double) (var4 - 1), (double) var13, 0.0D).a(1.0D, 0.0D).b(192, 192, 192, 255).d();
				worldRenderer.b((double) var3, (double) var13, 0.0D).a(0.0D, 0.0D).b(192, 192, 192, 255).d();
				tesselator.b();
			}

			this.b(mouseX, mouseY);
			buq.y();
			buq.j(7424);
			buq.e();
			buq.j();
		}
	}

	/**
	 * Draw Selection Box
	 * <p/>
	 * Decompiled from GuiSlot
	 */
	@Override
	protected void a(int x, int y, int mouseX, int mouseY, float partialTicks) {
		if (leftbound) {
			x = getLeft() + 2;
		}
		bvc tesselator = bvc.a();
		bui worldRenderer = tesselator.c();
		for (int rowIndex = 0; rowIndex < heightMap.size(); ++rowIndex) {
			int newY = y + heightMap.get(rowIndex) + getHeaderPadding();
			int slotHeight = rows.get(rowIndex).getLineHeight() - 4;
			if ((newY > getBottom()) || (newY + slotHeight < getTop())) {
				a(rowIndex, x, newY, partialTicks);
			} else {
				if (isDrawSelection() && (isSelected(rowIndex))) {
					int x1, x2;
					if (leftbound) {
						x1 = getLeft();
						x2 = getLeft() + getRowWidth();
					} else {
						x1 = getLeft() + (getWidth() / 2 - getRowWidth() / 2);
						x2 = getLeft() + getWidth() / 2 + getRowWidth() / 2;
					}
					GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
					buq.z();
					worldRenderer.a(7, cdw.i);
					worldRenderer.b((double) x1, (double) (newY + slotHeight + 2), 0.0D).a(0.0D, 1.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) x2, (double) (newY + slotHeight + 2), 0.0D).a(1.0D, 1.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) x2, (double) (newY - 2), 0.0D).a(1.0D, 0.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) x1, (double) (newY - 2), 0.0D).a(0.0D, 0.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) (x1 + 1), (double) (newY + slotHeight + 1), 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 255).d();
					worldRenderer.b((double) (x2 - 1), (double) (newY + slotHeight + 1), 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 255).d();
					worldRenderer.b((double) (x2 - 1), (double) (newY - 1), 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 255).d();
					worldRenderer.b((double) (x1 + 1), (double) (newY - 1), 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 255).d();
					tesselator.b();
					buq.y();
				}
				drawSlot(rowIndex, x, newY, slotHeight, mouseX, mouseY);
			}
		}
	}

	@Override
	public void handleMouseInput() {
		if (this.g(getMouseY())) {
			if (Mouse.isButtonDown(0) && this.q()) {
				if (this.l == -1.0F) {
					boolean var1 = true;
					if (this.j >= this.d && this.j <= this.e) {
						int x1, x2;
						if (leftbound) {
							x1 = getLeft();
							x2 = getLeft() + getRowWidth();
						} else {
							x1 = (getWidth() - getRowWidth()) / 2;
							x2 = (getWidth() + getRowWidth()) / 2;
						}
						int var4 = getMouseY() - getTop() - this.t + (int) getCurrentScroll();
						int var5 = -1;
						for (int i1 = 0; i1 < heightMap.size(); i1++) {
							Integer integer = heightMap.get(i1);
							Row line = rows.get(i1);
							if (var4 >= integer && var4 <= integer + line.getLineHeight()) {
								var5 = i1;
								break;
							}
						}
						if (getMouseX() >= x1 && getMouseX() <= x2 && var5 >= 0 && var4 >= 0 && var5 < this.b()) {
							boolean var6 = var5 == this.o && MinecraftFactory.getVars().getSystemTime() - this.p < 250L;
							this.a(var5, var6, getMouseX(), getMouseY());
							this.o = var5;
							this.p = MinecraftFactory.getVars().getSystemTime();
						} else if (getMouseX() >= x1 && getMouseX() <= x2 && var4 < 0) {
							this.a(getMouseX() - x1, getMouseY() - this.d + (int) this.n - 4);
							var1 = false;
						}

						int var11 = this.d();
						int var7 = var11 + 6;
						if (getMouseX() >= var11 && getMouseX() <= var7) {
							this.m = -1.0F;
							int var8 = this.m();
							if (var8 < 1) {
								var8 = 1;
							}

							int var9 = (int) ((float) ((this.e - this.d) * (this.e - this.d)) / (float) this.k());
							var9 = ri.a(var9, 32, this.e - this.d - 8);
							this.m /= (float) (this.e - this.d - var9) / (float) var8;
						} else {
							this.m = 1.0F;
						}

						if (var1) {
							this.l = getMouseY();
						} else {
							this.l = -2;
						}
					} else {
						this.l = -2;
					}
				} else if (this.l >= 0.0F) {
					this.n -= ((float) getMouseY() - this.l) * this.m;
					this.l = getMouseY();
				}
			} else {
				this.l = -1;
			}

			int var10 = Mouse.getEventDWheel();
			if (var10 != 0) {
				if (var10 > 0) {
					var10 = -1;
				} else if (var10 < 0) {
					var10 = 1;
				}

				this.n += (float) (var10 * 18);
			}
		}
	}

	@Override
	public boolean mouseDragged(double v, double v1, int i, double v2, double v3) {
		return false;
	}

	@Override
	public boolean mouseScrolled(double v) {
		return false;
	}

	private int getMouseX() {
		return i;
	}

	private int getMouseY() {
		return j;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY) {
		if (mouseX >= getLeft() && mouseX <= getRight() && mouseY >= getTop() & mouseY <= getBottom()) {
			synchronized (rows) {
				for (int rowIndex = 0; rowIndex < heightMap.size(); ++rowIndex) {
					int newY = (int) (getTop() + heightMap.get(rowIndex) + getHeaderPadding() - getCurrentScroll());
					int slotHeight = rows.get(rowIndex).getLineHeight() - 4;
					if ((newY <= getBottom()) && (newY + slotHeight >= getTop())) {
						Row row = rows.get(rowIndex);
						if (row instanceof RowExtended) {
							IButton pressed = ((RowExtended) row).mousePressed(mouseX, mouseY);
							if (pressed != null) {
								if (selectedButton != null && pressed != selectedButton)
									selectedButton.mouseClicked(mouseX, mouseY);
								selectedButton = pressed;
								return;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @return x-coordinate of the scroll bar
	 */
	@Override
	protected int d() {
		return scrollX > 0 ? scrollX : super.d();
	}

	/**
	 * @return the id of the clicked row, or -1 if no row has been clicked
	 */
	@Override
	public int c(int x, int y) {
		int var3, var4;
		if (leftbound) {
			var3 = getLeft();
			var4 = getLeft() + getRowWidth();
		} else {
			var3 = getLeft() + getWidth() / 2 - getRowWidth() / 2;
			var4 = getLeft() + getWidth() / 2 + getRowWidth() / 2;
		}
		int var5 = y - getTop() - this.t + (int) getCurrentScroll() - 4;
		int var6 = -1;
		for (int i1 = 0; i1 < heightMap.size(); i1++) {
			Integer integer = heightMap.get(i1);
			Row line = rows.get(i1);
			if (y >= integer && y <= integer + line.getLineHeight()) {
				var6 = i1;
				break;
			}
		}
		return x < this.d() && x >= var3 && x <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.b() ? var6 : -1;
	}

	/**
	 * Called when the mouse has been released.
	 *
	 * @param mouseX The x coordinate of the Mouse.
	 * @param mouseY The y coordinate of the Mouse.
	 * @param state  The state.
	 */
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (this.selectedButton != null && state == 0) {
			this.selectedButton.mouseReleased(mouseX, mouseY);
			this.selectedButton = null;
		}
	}

	@Override
	public void scrollToBottom() {
		scrollTo(k());
	}

	@Override
	public float getCurrentScroll() {
		return n;
	}

	@Override
	public void scrollTo(float to) {
		this.n = to;
	}

	@Override
	public int getContentHeight() {
		int height = bottomPadding + (getHeaderPadding() > 0 ? (getHeaderPadding() + 8) : 0);
		List<E> chatLines = Lists.newArrayList(rows);
		for (Row row : chatLines) {
			height += row.getLineHeight();
		}
		return height;
	}

	@Override
	public void calculateHeightMap() {
		heightMap.clear();

		int curHeight = getHeaderPadding();
		List<E> chatLines = Lists.newArrayList(rows);
		for (Row row : chatLines) {
			heightMap.add(curHeight);
			curHeight += row.getLineHeight();
		}
	}

	@Override
	public int getRowWidth() {
		return rowWidth;
	}

	@Override
	public void setRowWidth(int rowWidth) {
		this.rowWidth = rowWidth;
	}

	@Override
	public int getSelectedId() {
		synchronized (rows) {
			if (selected < 0 || selected > rows.size())
				selected = setSelectedId(0);
		}
		return selected;
	}

	@Override
	public int setSelectedId(int selected) {
		synchronized (rows) {
			if (selected < 0 || selected > rows.size())
				selected = 0;
		}
		this.selected = selected;
		return selected;
	}

	@Override
	public E getSelectedRow() {
		synchronized (rows) {
			if (rows.isEmpty())
				return null;
			if (selected < 0) {
				selected = 0;
				return rows.get(0);
			}
			while (selected >= rows.size()) {
				selected--;
			}
			return rows.get(selected);
		}
	}

	@Override
	public int getWidth() {
		return b;
	}

	@Override
	public void setWidth(int width) {
		b = width;
	}

	@Override
	public int getHeight() {
		return c;
	}

	@Override
	public void setHeight(int height) {
		c = height;
	}

	@Override
	public int getHeight(int id) {
		return heightMap.get(id);
	}

	@Override
	public int getTop() {
		return d;
	}

	@Override
	public void setTop(int top) {
		d = top;
	}

	@Override
	public int getBottom() {
		return e;
	}

	@Override
	public void setBottom(int bottom) {
		e = bottom;
	}

	@Override
	public int getLeft() {
		return g;
	}

	@Override
	public void setLeft(int left) {
		g = left;
	}

	@Override
	public int getRight() {
		return f;
	}

	@Override
	public void setRight(int right) {
		f = right;
	}

	@Override
	public int getScrollX() {
		return scrollX;
	}

	@Override
	public void setScrollX(int scrollX) {
		this.scrollX = scrollX;
	}

	@Override
	public boolean isLeftbound() {
		return leftbound;
	}

	@Override
	public void setLeftbound(boolean leftbound) {
		this.leftbound = leftbound;
	}

	@Override
	public boolean isDrawSelection() {
		return r;
	}

	@Override
	public void setDrawSelection(boolean drawSelection) {
		r = drawSelection;
	}

	@Override
	public int getHeaderPadding() {
		return t;
	}

	@Override
	public void setHeaderPadding(int headerPadding) {
		a(headerPadding > 0, headerPadding);
	}

	@Override
	public String getHeader() {
		return header;
	}

	@Override
	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	protected void a(int x, int y, bvc tesselator) {
		if (header != null) {
			MinecraftFactory.getVars().drawCenteredString(ChatColor.UNDERLINE.toString() + ChatColor.BOLD.toString() + header, getLeft() + (getRight() - getLeft()) / 2,
					Math.min(getTop() + 5, y));
		}
	}

	@Override
	public int getBottomPadding() {
		return bottomPadding;
	}

	@Override
	public void setBottomPadding(int bottomPadding) {
		this.bottomPadding = bottomPadding;
	}

	@Override
	public E getHoverItem(int mouseX, int mouseY) {
		int x1, x2;
		if (leftbound) {
			x1 = getLeft();
			x2 = getLeft() + getRowWidth();
		} else {
			x1 = getLeft() + (getWidth() / 2 - getRowWidth() / 2);
			x2 = getLeft() + getWidth() / 2 + getRowWidth() / 2;
		}
		if (mouseX >= x1 && mouseX <= x2) {
			synchronized (rows) {
				for (int i = 0; i < heightMap.size(); i++) {
					Integer y = (int) (heightMap.get(i) + getTop() + getHeaderPadding() - getCurrentScroll());
					E element = rows.get(i);
					if (mouseY >= y && mouseY <= y + element.getLineHeight()) {
						return element;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean isDrawDefaultBackground() {
		return drawDefaultBackground;
	}

	@Override
	public void setDrawDefaultBackground(boolean drawDefaultBackground) {
		this.drawDefaultBackground = drawDefaultBackground;
	}

	@Override
	public Object getBackgroundTexture() {
		return backgroundTexture;
	}

	@Override
	public void setBackgroundTexture(Object backgroundTexture, int imageWidth, int imageHeight) {
		this.backgroundTexture = backgroundTexture;

		if (backgroundTexture != null) {
			double w = imageWidth;
			double h = imageHeight;
			int listWidth = getRight() - getLeft();
			int listHeight = getBottom() - getTop();

			while (w > listWidth && h > listHeight) {
				w -= 1;
				h -= h / w;
			}
			while (w < listWidth || h < listHeight) {
				w += 1;
				h += h / w;
			}
			this.backgroundWidth = (int) w;
			this.backgroundHeight = (int) h;
		}
	}

	@Override
	public List<E> getRows() {
		return rows;
	}
}
