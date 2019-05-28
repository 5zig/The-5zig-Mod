import com.google.common.collect.Lists;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.*;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GuiList<E extends Row> extends cha implements IGuiList<E> {

	protected final List<E> rows;
	private final Clickable<E> clickable;

	private int rowWidth = 95;
	private int bottomPadding;
	private boolean leftbound = false;
	private int scrollX;
	private boolean hasSelected = false;
	private int mouseX, mouseY;

	private int selected;
	private IButton selectedButton;

	private String header;

	private boolean drawDefaultBackground = false;
	private Object backgroundTexture;
	private int backgroundWidth, backgroundHeight;

	protected List<Integer> heightMap = Lists.newArrayList(); // TODO: scrollbars gone?

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
	protected int d() {
		synchronized (rows) {
			return rows.size();
		}
	}

	@Override
	protected int n() {
		return getContentHeight();
	}

	/**
	 * Element clicked
	 *
	 * @param id          The id of the Row.
	 * @param button If the Row has been Double-Clicked.
	 * @param mouseX      The x-coordinate of the Mouse.
	 * @param mouseY      The y-coordinate of the Mouse.
	 */
	@Override
	protected boolean a(int id, int button, double mouseX, double mouseY) {
		selected = id;
		boolean var5 = (GuiList.this.selected >= 0) && (GuiList.this.selected < d());
		if (var5) {
			if (clickable != null) {
				synchronized (rows) {
					onSelect(id, rows.get(id), false); // TODO
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onSelect(int id, E row, boolean doubleClick) {
		setSelectedId(id);
		if (clickable != null && row != null) {
			clickable.onSelect(id, row, doubleClick);
		}
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
	public int e() {
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

	@Override
	public void handleMouseInput() {
	}

	/**
	 * Draw Screen
	 */
	@Override
	public void a(int mouseX, int mouseY, float partialTicks) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		calculateHeightMap();
		if (this.r) {
			this.a();
			int var3 = this.f();
			int var4 = var3 + 6;
			this.o();
			ctp.g();
			ctp.p();
			cub tesselator = cub.a();
			ctf worldRenderer = tesselator.c();
			ctp.c(1.0F, 1.0F, 1.0F, 1.0F);
			if (backgroundTexture != null) {
				MinecraftFactory.getVars().bindTexture(backgroundTexture);
				Gui.drawModalRectWithCustomSizedTexture(getLeft(), getTop(), 0, 0, getRight() - getLeft(), getBottom() - getTop(), backgroundWidth, backgroundHeight);
			} else if (drawDefaultBackground || MinecraftFactory.getVars().isPlayerNull()) {
				MinecraftFactory.getVars().bindTexture(cga.b);
				float var7 = 32.0F;
				worldRenderer.a(7, ddk.o);
				worldRenderer.b((double) this.k, (double) this.i, 0.0D).a((double) ((float) this.k / var7), (double) ((float) (this.i + (int) this.o) / var7)).b(32, 32, 32, 255).d();
				worldRenderer.b((double) this.j, (double) this.i, 0.0D).a((double) ((float) this.j / var7), (double) ((float) (this.i + (int) this.o) / var7)).b(32, 32, 32, 255).d();
				worldRenderer.b((double) this.j, (double) this.h, 0.0D).a((double) ((float) this.j / var7), (double) ((float) (this.h + (int) this.o) / var7)).b(32, 32, 32, 255).d();
				worldRenderer.b((double) this.k, (double) this.h, 0.0D).a((double) ((float) this.k / var7), (double) ((float) (this.h + (int) this.o) / var7)).b(32, 32, 32, 255).d();
				tesselator.b();
			}
			int var8 = this.k + this.f / 2 - this.e() / 2 + 2;
			int var9 = this.h + 4 - (int) this.o;
			glEnable(GL_SCISSOR_TEST);
			float scaleFactor = MinecraftFactory.getVars().getScaleFactor();
			glScissor((int) Math.ceil(getLeft() * scaleFactor), (int) Math.ceil((getHeight() - getBottom()) * scaleFactor), (int) Math.floor((getRight() - getLeft()) * scaleFactor),
					(int) Math.floor((getBottom() - getTop()) * scaleFactor));
			if (this.t) {
				this.a(var8, var9, tesselator);
			}
			this.a(var8, var9, mouseX, mouseY, partialTicks);
			glDisable(GL_SCISSOR_TEST);
			byte var10 = 4;
			ctp.m();
			ctp.a(ctp.r.l, ctp.l.j, ctp.r.o, ctp.l.e);
			ctp.d();
			ctp.j(7425);
			ctp.z();
			// Schatten
			int var11 = this.p();
			if (drawDefaultBackground || MinecraftFactory.getVars().isPlayerNull() || var11 > 0) {
				worldRenderer.a(7, ddk.o);
				worldRenderer.b((double) this.k, (double) (this.h + var10), 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 0).d();
				worldRenderer.b((double) this.j, (double) (this.h + var10), 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 0).d();
				worldRenderer.b((double) this.j, (double) this.h, 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) this.k, (double) this.h, 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 255).d();
				tesselator.b();
				worldRenderer.a(7, ddk.o);
				worldRenderer.b((double) this.k, (double) this.i, 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) this.j, (double) this.i, 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double) this.j, (double) (this.i - var10), 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 0).d();
				worldRenderer.b((double) this.k, (double) (this.i - var10), 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 0).d();
				tesselator.b();
			}
			if (var11 > 0) {
				int var13 = (int)((float)((this.i - this.h) * (this.i - this.h)) / (float)this.n());
				var13 = xp.a(var13, 32, this.i - this.h - 8);
				int var14 = (int)this.o * (this.i - this.h - var13) / var11 + this.h;
				if (var14 < this.h) {
					var14 = this.h;
				}

				worldRenderer.a(7, ddk.o);
				worldRenderer.b((double)var3, (double)this.i, 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double)var4, (double)this.i, 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double)var4, (double)this.h, 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 255).d();
				worldRenderer.b((double)var3, (double)this.h, 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 255).d();
				tesselator.b();
				worldRenderer.a(7, ddk.o);
				worldRenderer.b((double)var3, (double)(var14 + var13), 0.0D).a(0.0D, 1.0D).b(128, 128, 128, 255).d();
				worldRenderer.b((double)var4, (double)(var14 + var13), 0.0D).a(1.0D, 1.0D).b(128, 128, 128, 255).d();
				worldRenderer.b((double)var4, (double)var14, 0.0D).a(1.0D, 0.0D).b(128, 128, 128, 255).d();
				worldRenderer.b((double)var3, (double)var14, 0.0D).a(0.0D, 0.0D).b(128, 128, 128, 255).d();
				tesselator.b();
				worldRenderer.a(7, ddk.o);
				worldRenderer.b((double)var3, (double)(var14 + var13 - 1), 0.0D).a(0.0D, 1.0D).b(192, 192, 192, 255).d();
				worldRenderer.b((double)(var4 - 1), (double)(var14 + var13 - 1), 0.0D).a(1.0D, 1.0D).b(192, 192, 192, 255).d();
				worldRenderer.b((double)(var4 - 1), (double)var14, 0.0D).a(1.0D, 0.0D).b(192, 192, 192, 255).d();
				worldRenderer.b((double)var3, (double)var14, 0.0D).a(0.0D, 0.0D).b(192, 192, 192, 255).d();
				tesselator.b();
			}

			this.b(mouseX, mouseY);
			ctp.y();
			ctp.j(7424);
			ctp.e();
			ctp.l();
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
		cub tesselator = cub.a();
		ctf worldRenderer = tesselator.c();
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
					ctp.z();
					worldRenderer.a(7, ddk.o);
					worldRenderer.b((double) x1, (double) (newY + slotHeight + 2), 0.0D).a(0.0D, 1.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) x2, (double) (newY + slotHeight + 2), 0.0D).a(1.0D, 1.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) x2, (double) (newY - 2), 0.0D).a(1.0D, 0.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) x1, (double) (newY - 2), 0.0D).a(0.0D, 0.0D).b(128, 128, 128, 255).d();
					worldRenderer.b((double) (x1 + 1), (double) (newY + slotHeight + 1), 0.0D).a(0.0D, 1.0D).b(0, 0, 0, 255).d();
					worldRenderer.b((double) (x2 - 1), (double) (newY + slotHeight + 1), 0.0D).a(1.0D, 1.0D).b(0, 0, 0, 255).d();
					worldRenderer.b((double) (x2 - 1), (double) (newY - 1), 0.0D).a(1.0D, 0.0D).b(0, 0, 0, 255).d();
					worldRenderer.b((double) (x1 + 1), (double) (newY - 1), 0.0D).a(0.0D, 0.0D).b(0, 0, 0, 255).d();
					tesselator.b();
					ctp.y();
				}
				drawSlot(rowIndex, x, newY, slotHeight, mouseX, mouseY);
			}
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.getFocused() != null) {
			this.getFocused().mouseReleased(mouseX, mouseY, button);
		}

		this.b().forEach((var5x) -> {
			var5x.mouseReleased(mouseX, mouseY, button);
		});
		return false;
	}

	@Override
	public boolean mouseDragged(double var1, double var3, int button, double var6, double var8) {
		if ((this.getFocused() != null && this.hasSelected && button == 0) && this.getFocused().mouseDragged(var1, var3, button, var6, var8)) {
			return true;
		} else if (this.m() && button == 0 && this.hasSelected && var1 > f() && var1 < f() + 6) {
			if (var3 < (double)this.h) {
				this.o = 0.0D;
			} else if (var3 > (double)this.i) {
				this.o = (double)this.p();
			} else {
				double var10 = (double)this.p();
				if (var10 < 1.0D) {
					var10 = 1.0D;
				}

				int var12 = (int)((float)((this.i - this.h) * (this.i - this.h)) / (float)this.n());
				var12 = xp.a(var12, 32, this.i - this.h - 8);
				double var13 = var10 / (double)(this.i - this.h - var12);
				if (var13 < 1.0D) {
					var13 = 1.0D;
				}

				this.o += var8 * var13;
				this.o();
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseScrolled(double var1) {
		if (!this.m() || !(mouseX >= getLeft() && mouseX <= getRight() && mouseY >= getTop() & mouseY <= getBottom())) {
			return false;
		} else {
			this.o -= var1 * (double)this.l / 2.0D;
			return true;
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY) {
		hasSelected = mouseX >= getLeft() && mouseX <= getRight() && mouseY >= getTop() & mouseY <= getBottom();

		if (this.m() && this.b((double)mouseX, (double)mouseY)) {
			int var6 = this.a((double)mouseX, (double)mouseY);
			if (var6 == -1) {
				this.a((int)(mouseX - (double)(this.k + this.f / 2 - this.e() / 2)), (int)(mouseY - (double)this.h) + (int)this.o - 4); // TODO
			} else if (this.a(var6, 0, (double)mouseX, (double)mouseY)) {
				if (this.b().size() > var6) {
					this.a(this.b().get(var6));
				}

				this.e(true);
				this.b(var6);
			}
		}

		if (hasSelected) {
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
	protected int f() {
		return scrollX > 0 ? scrollX : super.f();
	}

	/**
	 * @return the id of the clicked row, or -1 if no row has been clicked
	 */
	@Override
	public int a(double x, double y) {
		int var3, var4;
		if (leftbound) {
			var3 = getLeft();
			var4 = getLeft() + getRowWidth();
		} else {
			var3 = getLeft() + getWidth() / 2 - getRowWidth() / 2;
			var4 = getLeft() + getWidth() / 2 + getRowWidth() / 2;
		}
		int var5 = (int) (y - getTop()) - getHeaderPadding() + (int) getCurrentScroll() - 4;
		int var6 = -1;
		for (int i1 = 0; i1 < heightMap.size(); i1++) {
			Integer integer = heightMap.get(i1);
			Row line = rows.get(i1);
			if (var5 >= integer && var5 <= integer + line.getLineHeight()) {
				var6 = i1;
				break;
			}
		}
		return x < this.f() && x >= var3 && x <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.d() ? var6 : -1;
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
		scrollTo(n());
	}

	@Override
	public float getCurrentScroll() {
		return (float)o;
	}

	@Override
	public void scrollTo(float to) {
		this.o = to;
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
		return f;
	}

	@Override
	public void setWidth(int width) {
		f = width;
	}

	@Override
	public int getHeight() {
		return g;
	}

	@Override
	public void setHeight(int height) {
		g = height;
	}

	@Override
	public int getHeight(int id) {
		return heightMap.get(id);
	}

	@Override
	public int getTop() {
		return h;
	}

	@Override
	public void setTop(int top) {
		h = top;
	}

	@Override
	public int getBottom() {
		return i;
	}

	@Override
	public void setBottom(int bottom) {
		i = bottom;
	}

	@Override
	public int getLeft() {
		return k;
	}

	@Override
	public void setLeft(int left) {
		k = left;
	}

	@Override
	public int getRight() {
		return j;
	}

	@Override
	public void setRight(int right) {
		j = right;
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
		return s;
	}

	@Override
	public void setDrawSelection(boolean drawSelection) {
		s = drawSelection;
	}

	@Override
	public int getHeaderPadding() {
		return u;
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
	protected void a(int x, int y, cub tesselator) {
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
