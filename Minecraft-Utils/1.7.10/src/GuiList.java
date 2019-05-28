import com.google.common.collect.Lists;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.*;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.minecraft.ChatColor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GuiList<E extends Row> extends bcm implements IGuiList<E> {

	private static final Field scroll;
	private static final Field initialClickY;

	static {
		try {
			if (!Transformer.FORGE) {
				scroll = GuiList.class.getSuperclass().getDeclaredField("q");
				initialClickY = GuiList.class.getSuperclass().getDeclaredField("o");
			} else {
				scroll = GuiList.class.getSuperclass().getDeclaredField("field_148169_q");
				initialClickY = GuiList.class.getSuperclass().getDeclaredField("field_148157_o");
			}
			scroll.setAccessible(true);
			initialClickY.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected final List<E> rows;
	private final Clickable<E> clickable;
	private int width, height;

	private int rowWidth = 95;
	private int bottomPadding;
	private boolean leftbound = false;
	private int scrollX;
	private boolean drawSelection = true;
	private float scrollMultiplier;
	private long lastClicked;

	private int selected;
	private IButton selectedButton;

	private String header;

	private boolean drawDefaultBackground = false;
	private Object backgroundTexture;
	private int backgroundWidth, backgroundHeight;

	protected List<Integer> heightMap = Lists.newArrayList();

	public GuiList(Clickable<E> clickable, int width, int height, int top, int bottom, int left, int right, List<E> rows) {
		super(((Variables) MinecraftFactory.getVars()).getMinecraft(), width, height, top, bottom, 18);

		this.width = width;
		this.height = height;
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
	protected int e() {
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
	protected void a(int id, int x, int y, int slotHeight, bmh tesselator, int mouseX, int mouseY) {
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
	public void a(int mouseX, int mouseY, float partialTicks) {
		calculateHeightMap();
		setMouseX(mouseX);
		setMouseY(mouseY);
		this.a();
		int var3 = d();
		int var4 = var3 + 6;
		bindAmountScrolled();

		GL11.glDisable(2896);
		GL11.glDisable(2912);
		bmh tesselator = bmh.a;
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (backgroundTexture != null) {
			MinecraftFactory.getVars().bindTexture(backgroundTexture);
			Gui.drawModalRectWithCustomSizedTexture(getLeft(), getTop(), 0, 0, getRight() - getLeft(), getBottom() - getTop(), backgroundWidth, backgroundHeight);
		} else if (drawDefaultBackground || MinecraftFactory.getVars().isPlayerNull()) {
			MinecraftFactory.getVars().bindTexture(bbw.b);

			float var8 = 32.0F;
			tesselator.b();
			tesselator.c(2105376);
			tesselator.a((double) getLeft(), (double) getBottom(), 0.0D, (double) ((float) getLeft() / var8), (double) ((float) (getBottom() + (int) getCurrentScroll()) / var8));
			tesselator.a((double) getRight(), (double) getBottom(), 0.0D, (double) ((float) getRight() / var8), (double) ((float) (getBottom() + (int) getCurrentScroll()) / var8));
			tesselator.a((double) getRight(), (double) getTop(), 0.0D, (double) ((float) getRight() / var8), (double) ((float) (getTop() + (int) getCurrentScroll()) / var8));
			tesselator.a((double) getLeft(), (double) getTop(), 0.0D, (double) ((float) getLeft() / var8), (double) ((float) (getTop() + (int) getCurrentScroll()) / var8));
			tesselator.a();
		}
		int var8 = getLeft() + this.a / 2 - c() / 2 + 2;
		int var9 = getTop() + 4 - (int) getCurrentScroll();
		glEnable(GL_SCISSOR_TEST);
		float scaleFactor = MinecraftFactory.getVars().getScaleFactor();
		glScissor((int) Math.ceil(getLeft() * scaleFactor), (int) Math.ceil((getHeight() - getBottom()) * scaleFactor), (int) Math.floor((getRight() - getLeft()) * scaleFactor),
				(int) Math.floor((getBottom() - getTop()) * scaleFactor));
		if (true) {
			this.a(var8, var9, tesselator);
		}
		this.b(var8, var9, mouseX, mouseY);
		glDisable(GL_SCISSOR_TEST);
		byte var10 = 4;
		GL11.glEnable(3042);
		GLUtil.tryBlendFuncSeparate(770, 771, 0, 1);
		GL11.glDisable(3008);
		GL11.glShadeModel(7425);
		GL11.glDisable(3553);
		// Schatten
		int var11 = this.f();
		if (drawDefaultBackground || MinecraftFactory.getVars().isPlayerNull() || var11 > 0) {
			tesselator.b();
			tesselator.a(0, 0);
			tesselator.a((double) getLeft(), (double) (getTop() + var10), 0.0D, 0.0D, 1.0D);
			tesselator.a((double) getRight(), (double) (getTop() + var10), 0.0D, 1.0D, 1.0D);
			tesselator.a(0, 255);
			tesselator.a((double) getRight(), getTop(), 0.0D, 1.0D, 0.0D);
			tesselator.a((double) getLeft(), getTop(), 0.0D, 0.0D, 0.0D);
			tesselator.a();

			tesselator.b();
			tesselator.a(0, 255);
			tesselator.a((double) getLeft(), (double) getBottom(), 0.0D, 0.0D, 1.0D);
			tesselator.a((double) getRight(), (double) getBottom(), 0.0D, 1.0D, 1.0D);
			tesselator.a(0, 0);
			tesselator.a((double) getRight(), (double) getBottom() - var10, 0.0D, 1.0D, 0.0D);
			tesselator.a((double) getLeft(), (double) getBottom() - var10, 0.0D, 0.0D, 0.0D);
			tesselator.a();
		}
		if (var11 > 0) {
			int var12 = (getBottom() - getTop()) * (getBottom() - getTop()) / e();
			var12 = qh.a(var12, 32, getBottom() - getTop() - 8);
			int var13 = (int) getCurrentScroll() * (getBottom() - getTop() - var12) / var11 + getTop();
			if (var13 < getTop()) {
				var13 = getTop();
			}

			tesselator.b();
			tesselator.a(0, 255);
			tesselator.a((double) var3, (double) getBottom(), 0.0D, 0.0D, 1.0D);
			tesselator.a((double) var4, (double) getBottom(), 0.0D, 1.0D, 1.0D);
			tesselator.a((double) var4, (double) getTop(), 0.0D, 1.0D, 0.0D);
			tesselator.a((double) var3, (double) getTop(), 0.0D, 0.0D, 0.0D);
			tesselator.a();
			tesselator.b();
			tesselator.a(8421504, 255);
			tesselator.a((double) var3, (double) (var13 + var12), 0.0D, 0.0D, 1.0D);
			tesselator.a((double) var4, (double) (var13 + var12), 0.0D, 1.0D, 1.0D);
			tesselator.a((double) var4, (double) var13, 0.0D, 1.0D, 0.0D);
			tesselator.a((double) var3, (double) var13, 0.0D, 0.0D, 0.0D);
			tesselator.a();
			tesselator.b();
			tesselator.a(12632256, 255);
			tesselator.a((double) var3, (double) (var13 + var12 - 1), 0.0D, 0.0D, 1.0D);
			tesselator.a((double) (var4 - 1), (double) (var13 + var12 - 1), 0.0D, 1.0D, 1.0D);
			tesselator.a((double) (var4 - 1), (double) var13, 0.0D, 1.0D, 0.0D);
			tesselator.a((double) var3, (double) var13, 0.0D, 0.0D, 0.0D);
			tesselator.a();
		}

		b(mouseX, mouseY);
		GL11.glEnable(3553);
		GL11.glShadeModel(7424);
		GL11.glEnable(3008);
		GL11.glDisable(3042);
	}

	/**
	 * Draw Selection Box
	 * <p/>
	 * Decompiled from GuiSlot
	 */
	@Override
	protected void b(int x, int y, int mouseX, int mouseY) {
		if (leftbound) {
			x = getLeft() + 2;
		}
		bmh tesselator = bmh.a;
		for (int rowIndex = 0; rowIndex < heightMap.size(); ++rowIndex) {
			int newY = y + heightMap.get(rowIndex) + getHeaderPadding();
			int slotHeight = rows.get(rowIndex).getLineHeight() - 4;
			if ((newY > getBottom()) || (newY + slotHeight < getTop())) {
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
					GL11.glDisable(GL11.GL_TEXTURE_2D);

					tesselator.b();
					tesselator.c(8421504);
					tesselator.a(x1, newY + slotHeight + 2, 0.0D, 0.0D, 1.0D);
					tesselator.a(x2, newY + slotHeight + 2, 0.0D, 1.0D, 1.0D);
					tesselator.a(x2, newY - 2, 0.0D, 1.0D, 0.0D);
					tesselator.a(x1, newY - 2, 0.0D, 0.0D, 0.0D);

					tesselator.c(0);
					tesselator.a(x1 + 1, newY + slotHeight + 1, 0.0D, 0.0D, 1.0D);
					tesselator.a(x2 - 1, newY + slotHeight + 1, 0.0D, 1.0D, 1.0D);
					tesselator.a(x2 - 1, newY - 1, 0.0D, 1.0D, 0.0D);
					tesselator.a(x1 + 1, newY - 1, 0.0D, 0.0D, 0.0D);

					tesselator.a();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}
				drawSlot(rowIndex, x, newY, slotHeight, mouseX, mouseY);
			}
		}
	}

	public boolean isMouseYWithinSlotBounds(int mouseY) {
		return mouseY >= getTop() && mouseY <= getBottom() && getMouseX() >= getLeft() && getMouseX() <= getRight();
	}

	@Override
	public void handleMouseInput() {
		try {
			if (isMouseYWithinSlotBounds(getMouseY())) {
				if (Mouse.isButtonDown(0) && this.i()) {
					if (initialClickY.getFloat(this) == -1.0F) {
						boolean var1 = true;
						if (getMouseY() >= getTop() && getMouseY() <= getBottom()) {
							int x1, x2;
							if (leftbound) {
								x1 = getLeft();
								x2 = getLeft() + getRowWidth();
							} else {
								x1 = (getWidth() - getRowWidth()) / 2;
								x2 = (getWidth() + getRowWidth()) / 2;
							}
							int var4 = getMouseY() - getTop() - this.j + (int) getCurrentScroll();
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
								boolean var6 = var5 == initialClickY.getFloat(this) && MinecraftFactory.getVars().getSystemTime() - lastClicked < 250L;
								this.a(var5, var6, getMouseX(), getMouseY());
								initialClickY.set(this, var5);
								this.lastClicked = MinecraftFactory.getVars().getSystemTime();
							} else if (getMouseX() >= x1 && getMouseX() <= x2 && var4 < 0) {
								this.a(getMouseX() - x1, getMouseY() - getTop() + (int) getCurrentScroll() - 4);
								var1 = false;
							}

							int var11 = this.d();
							int var7 = var11 + 6;
							if (getMouseX() >= var11 && getMouseX() <= var7) {
								this.scrollMultiplier = -1.0F;
								int var8 = this.f();
								if (var8 < 1) {
									var8 = 1;
								}

								int var9 = (int) ((float) ((this.c - this.b) * (this.c - this.b)) / (float) this.e());
								var9 = qh.a(var9, 32, this.c - this.b - 8);
								this.scrollMultiplier /= (float) (this.c - this.b - var9) / (float) var8;
							} else {
								this.scrollMultiplier = 1.0F;
							}

							if (var1) {
								initialClickY.set(this, getMouseY());
							} else {
								initialClickY.set(this, -2);
							}
						} else {
							initialClickY.set(this, -2);
						}
					} else if (initialClickY.getFloat(this) >= 0.0F) {
						scrollTo(getCurrentScroll() - ((float) getMouseY() - initialClickY.getFloat(this)) * this.scrollMultiplier);
						initialClickY.set(this, getMouseY());
					}
				} else {
					initialClickY.set(this, -1);
				}

				int var10 = Mouse.getEventDWheel();
				if (var10 != 0) {
					if (var10 > 0) {
						var10 = -1;
					} else if (var10 < 0) {
						var10 = 1;
					}

					scrollTo(getCurrentScroll() + (float) (var10 * 18));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
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

	protected int getMouseX() {
		return g;
	}

	protected int getMouseY() {
		return h;
	}

	protected void setMouseX(int x) {
		this.g = x;
	}

	protected void setMouseY(int y) {
		this.h = y;
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
		int var5 = y - getTop() - this.j + (int) getCurrentScroll() - 4;
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
	 * Override, to fix rows being centered horizontally.
	 *
	 * @return the min x-value where the rows should start.
	 */
	@Override
	public int f() {
		return Math.max(0, getContentHeight() - (getBottom() - getTop() - 4));
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
		scrollTo(getContentHeight());
	}

	@Override
	public float getCurrentScroll() {
		try {
			return scroll.getFloat(this);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void scrollTo(float to) {
		try {
			scroll.set(this, to);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	protected void bindAmountScrolled() {
		int var1 = this.f();

		if (var1 < 0) {
			var1 /= 2;
		}

		if (!this.i && var1 < 0) {
			var1 = 0;
		}

		if (getCurrentScroll() < 0.0F) {
			scrollTo(0.0F);
		}

		if (this.getCurrentScroll() > (float) var1) {
			scrollTo((float) var1);
		}
	}

	@Override
	public int getContentHeight() {
		int height = bottomPadding + (getHeaderPadding() > 0 ? getHeaderPadding() + 8 : 0);
		;
		List<E> chatLines = Lists.newArrayList(rows);
		for (Row row : chatLines) {
			height += row.getLineHeight();
		}
		return height;
	}

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
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getHeight(int id) {
		return heightMap.get(id);
	}

	@Override
	public int getTop() {
		return b;
	}

	@Override
	public void setTop(int top) {
		b = top;
	}

	@Override
	public int getBottom() {
		return c;
	}

	@Override
	public void setBottom(int bottom) {
		c = bottom;
	}

	@Override
	public int getLeft() {
		return e;
	}

	@Override
	public void setLeft(int left) {
		e = left;
	}

	@Override
	public int getRight() {
		return d;
	}

	@Override
	public void setRight(int right) {
		d = right;
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
		return drawSelection;
	}

	@Override
	public void setDrawSelection(boolean drawSelection) {
		a(drawSelection);
		this.drawSelection = drawSelection;
	}

	@Override
	public int getHeaderPadding() {
		return j;
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
	protected void a(int x, int y, bmh tesselator) {
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
