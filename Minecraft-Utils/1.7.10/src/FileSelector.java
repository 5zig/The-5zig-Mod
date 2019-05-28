import com.google.common.collect.Lists;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.elements.IFileSelector;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.Callback;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class FileSelector implements IFileSelector {

	private final FileSystemView fsv = FileSystemView.getFileSystemView();
	private final Callback<File> callback;

	private File currentDir;
	private int selectedFile;
	private List<File> files = Lists.newArrayList();

	private int width, height;

	private int left;
	private int right;
	private int top;
	private int bottom;
	private int columnCount;

	private int mouseX, mouseY;

	private int selectionBoxWidth = 100;

	private float amountScrolled;

	private float initialClickY = -1, scrollMultiplier = -1;
	private long lastClicked;

	public FileSelector(File currentDir, int width, int height, int left, int right, int top, int bottom, Callback<File> callback) {
		updateDir(currentDir);
		this.width = width;
		this.height = height;
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.callback = callback;

		columnCount = (int) Math.floor((double) (right - left) / (double) selectionBoxWidth);
	}

	@Override
	public void goUp() {
		if (currentDir == null)
			return;
		File parent = currentDir.getParentFile();
		if (parent == null) {
			currentDir = null;
			files.clear();
			File[] a = File.listRoots();
			if (a == null)
				return;
			for (File file : a) {
				if (!fsv.isDrive(file) || fsv.getSystemDisplayName(file).isEmpty())
					continue;
				files.add(file);
			}
			selectedFile = files.isEmpty() ? -1 : 0;
			return;
		}
		updateDir(parent);
	}

	@Override
	public void updateDir(File dir) {
		currentDir = dir;
		files.clear();
		if (dir == null) {
			File[] a = File.listRoots();
			if (a == null)
				return;
			for (File file : a) {
				if (!fsv.isDrive(file) || fsv.getSystemDisplayName(file).isEmpty())
					continue;
				files.add(file);
			}
			selectedFile = files.isEmpty() ? -1 : 0;
			return;
		}
		File[] a = dir.listFiles();
		if (a == null)
			return;
		for (File file : a) {
			if (file.isHidden())
				continue;
			files.add(file);
		}
		selectedFile = files.isEmpty() ? -1 : 0;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;

		int scrollX0 = getScrollBarX();
		int scrollX1 = scrollX0 + 6;
		bindAmountScrolled();
		GLUtil.disableLighting();
		GLUtil.disableFog();
		bmh tessellator = bmh.a;
		MinecraftFactory.getVars().bindTexture(bbw.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		// background
		float f1 = 32.0F;
		tessellator.b();
		tessellator.c(2105376);
		tessellator.a((double) left, (double) bottom, 0.0D, (double) left / f1, ((double) bottom + (int) this.amountScrolled) / f1);
		tessellator.a((double) right, (double) bottom, 0.0D, (double) right / f1, ((double) bottom + (int) this.amountScrolled) / f1);
		tessellator.a((double) right, (double) top, 0.0D, (double) right / f1, ((double) top + (int) this.amountScrolled) / f1);
		tessellator.a((double) left, (double) top, 0.0D, (double) left / f1, ((double) top + (int) this.amountScrolled) / f1);
		tessellator.a();

		int selectionX0 = left;
		int selectionY0 = top + 4 - (int) amountScrolled;
		drawSelectionBox(selectionX0, selectionY0, mouseX, mouseY);
		GLUtil.disableDepth();
		byte var10 = 4;
		this.overlayBackground(0, top, 255, 255);
		this.overlayBackground(bottom, height, 255, 255);
		GLUtil.enableBlend();
		GLUtil.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_FALSE, GL11.GL_TRUE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		// Schatten
		tessellator.b();
		tessellator.a(0, 0);
		tessellator.a((double) left, (double) (top + var10), 0.0D, 0.0D, 1.0D);
		tessellator.a((double) right, (double) (top + var10), 0.0D, 1.0D, 1.0D);
		tessellator.a(0, 255);
		tessellator.a((double) right, top, 0.0D, 1.0D, 0.0D);
		tessellator.a((double) left, top, 0.0D, 0.0D, 0.0D);
		tessellator.a();

		tessellator.b();
		tessellator.a(0, 255);
		tessellator.a((double) left, (double) bottom, 0.0D, 0.0D, 1.0D);
		tessellator.a((double) right, (double) bottom, 0.0D, 1.0D, 1.0D);
		tessellator.a(0, 0);
		tessellator.a((double) right, (double) bottom - var10, 0.0D, 1.0D, 0.0D);
		tessellator.a((double) left, (double) bottom - var10, 0.0D, 0.0D, 0.0D);
		tessellator.a();

		int var11 = m();
		if (var11 > 0) {
			int var12 = (bottom - top) * (bottom - top) / getContentHeight();
			var12 = qh.a(var12, 32, bottom - top - 8);
			int var13 = (int) amountScrolled * (bottom - top - var12) / var11 + top;
			if (var13 < top) {
				var13 = top;
			}

			tessellator.b();
			tessellator.a(0, 255);
			tessellator.a((double) scrollX0, (double) bottom, 0.0D, 0.0D, 1.0D);
			tessellator.a((double) scrollX1, (double) bottom, 0.0D, 1.0D, 1.0D);
			tessellator.a((double) scrollX1, (double) top, 0.0D, 1.0D, 0.0D);
			tessellator.a((double) scrollX0, (double) top, 0.0D, 0.0D, 0.0D);
			tessellator.a();

			tessellator.b();
			tessellator.a(8421504, 255);
			tessellator.a((double) scrollX0, var13 + var12, 0.0D, 0.0D, 1.0D);
			tessellator.a((double) scrollX1, var13 + var12, 0.0D, 1.0D, 1.0D);
			tessellator.a((double) scrollX1, var13, 0.0D, 1.0D, 0.0D);
			tessellator.a((double) scrollX0, var13, 0.0D, 0.0D, 0.0D);
			tessellator.a();

			tessellator.b();
			tessellator.a(12632256, 255);
			tessellator.a((double) scrollX0, var13 + var12 - 1, 0.0D, 0.0D, 1.0D);
			tessellator.a((double) scrollX1 - 1, var13 + var12 - 1, 0.0D, 1.0D, 1.0D);
			tessellator.a((double) scrollX1 - 1, var13, 0.0D, 1.0D, 0.0D);
			tessellator.a((double) scrollX0, var13, 0.0D, 0.0D, 0.0D);
			tessellator.a();
		}

		//this.b(mouseX, mouseY);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GLUtil.disableBlend();

		// draw hover if slot text is too long
		int x = mouseX - left;
		int y = mouseY - top + (int) amountScrolled - 4;
		int idX = x / selectionBoxWidth;
		int idY = y / getSlotHeight();
		int id = idX + idY * columnCount;
		if (id < files.size() && id >= 0 && mouseX >= 0 && mouseY >= 0 && idX < columnCount) {
			elementHovered(id, mouseX, mouseY);
		}
	}

	protected void drawSelectionBox(int x, int y, int mouseX, int mouseY) {
		int rows = (int) Math.ceil((double) files.size() / (double) columnCount);
		int columns = columnCount;
		bmh tessellator = bmh.a;

		for (int i = 0; i < rows; ++i) {
			int ny = y + i * getSlotHeight();
			int padding = getSlotHeight() - 4;
			//if (ny > bottom || ny + padding < top) {
			//this.a(i, var, ny);
			//}
			for (int j = 0; j < columnCount; j++) {
				int nx = x + j * selectionBoxWidth;
				int nx1 = nx + selectionBoxWidth;

				int id = j + i * columns;
				if (isSelected(id)) { // isSelected
					GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_TEXTURE_2D);

					tessellator.b();
					tessellator.c(8421504);
					tessellator.a(nx, ny + padding + 2, 0.0D, 0.0D, 1.0D);
					tessellator.a(nx1, ny + padding + 2, 0.0D, 1.0D, 1.0D);
					tessellator.a(nx1, ny - 2, 0.0D, 1.0D, 0.0D);
					tessellator.a(nx, ny - 2, 0.0D, 0.0D, 0.0D);

					tessellator.c(0);
					tessellator.a(nx + 1, ny + padding + 1, 0.0D, 0.0D, 1.0D);
					tessellator.a(nx1 - 1, ny + padding + 1, 0.0D, 1.0D, 1.0D);
					tessellator.a(nx1 - 1, ny - 1, 0.0D, 1.0D, 0.0D);
					tessellator.a(nx + 1, ny - 1, 0.0D, 0.0D, 0.0D);

					tessellator.a();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}
				drawSlot(id, nx, ny, padding, mouseX, mouseY);
			}
		}

	}

	protected void drawSlot(int id, int x, int y, int padding, int mouseX, int mouseY) {
		File file = getFile(id);
		if (file == null)
			return;
		String name = file.getName();
		if (currentDir == null) {
			name = fsv.getSystemDisplayName(file);
		}
		MinecraftFactory.getVars().drawString(MinecraftFactory.getVars().shortenToWidth(name, selectionBoxWidth - 18), x + 2, y + 2);
	}

	public boolean isSelected(int id) {
		return selectedFile == id;
	}

	public File getFile(int i) {
		if (i < 0 || i >= files.size())
			return null;
		return files.get(i);
	}

	@Override
	public File getSelectedFile() {
		return getFile(selectedFile);
	}

	protected void overlayBackground(int var, int var1, int var2, int var3) {
		bmh tessellator = bmh.a;
		MinecraftFactory.getVars().bindTexture(bbw.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32.0F;
		tessellator.b();
		tessellator.a(4210752, var3);
		tessellator.a(left, var1, 0.0D, 0.0D, var1 / f1);
		tessellator.a((left + (right - left)), var1, 0.0D, (right - left) / f1, var1 / f1);
		tessellator.a(4210752, var2);
		tessellator.a((left + (right - left)), var, 0.0D, (right - left) / f1, var / f1);
		tessellator.a(left, var, 0.0D, 0.0D, var / f1);
		tessellator.a();
	}

	public int getContentHeight() {
		return (int) Math.ceil((double) files.size() / (double) columnCount) * getSlotHeight();
	}

	public int getSlotHeight() {
		return 18;
	}

	protected void bindAmountScrolled() {
		amountScrolled = qh.a(amountScrolled, 0.0F, (float) this.m());
	}

	public int m() {
		return Math.max(0, getContentHeight() - (this.bottom - this.top - 4));
	}

	public boolean isMouseWithinSlotBounds() {
		return mouseY >= top && mouseY <= bottom && mouseX >= left && mouseX <= right;
	}

	public int getScrollBarX() {
		return right - 10;
	}

	@Override
	public void handleMouseInput() {
		if (!isMouseWithinSlotBounds())
			return;

		int var;
		if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && mouseY >= top && mouseY <= bottom) {
			int x = mouseX - left;
			int y = mouseY - top + (int) amountScrolled - 4;
			int idY = y / getSlotHeight();
			int idX = x / selectionBoxWidth;
			int id = idX + idY * columnCount;
			if (id < files.size() && id >= 0 && x >= 0 && y >= 0 && idX < columnCount) {
				selectedFile = id;
				elementClicked(id, false, mouseX, mouseY);
			} else if (id < 0) {
				//this.a(mouseX - var, mouseY - top + (int) this.n - 4);
			}
		}

		if (Mouse.isButtonDown(0)) {
			if (initialClickY == -1) {
				boolean var9 = true;
				if (mouseY >= top && mouseY <= bottom) {
					int x = mouseX - left;
					int y = mouseY - top + (int) amountScrolled - 4;
					int idY = y / getSlotHeight();
					int idX = x / selectionBoxWidth;
					int id = idX + idY * columnCount;
					if (id < files.size() && id >= 0 && x >= 0 && y >= 0 && idX < columnCount) {
						boolean doubleClick = id == selectedFile && MinecraftFactory.getVars().getSystemTime() - lastClicked < 250L;
						this.selectedFile = id;
						elementClicked(id, doubleClick, mouseX, mouseY);
						this.lastClicked = MinecraftFactory.getVars().getSystemTime();
					} else if (id < 0) {
						//this.a(mouseX - var1, mouseY - top + (int) this.n - 4);
						var9 = false;
					}

					int scrollBarX0 = getScrollBarX();
					int scrollBarX1 = scrollBarX0 + 6;
					if (mouseX >= scrollBarX0 && mouseX <= scrollBarX1) {
						scrollMultiplier = -1.0F;
						int var7 = m();
						if (var7 < 1) {
							var7 = 1;
						}

						int var8 = (int) ((float) ((bottom - top) * (bottom - top)) / (float) getContentHeight());
						var8 = qh.a(var8, 32, bottom - top - 8);
						scrollMultiplier /= (float) (bottom - top - var8) / (float) var7;
					} else {
						scrollMultiplier = 1.0F;
					}

					if (var9) {
						initialClickY = mouseY;
					} else {
						initialClickY = -2;
					}
				} else {
					initialClickY = -2;
				}
			} else if (initialClickY >= 0) {
				this.amountScrolled -= (mouseY - initialClickY) * scrollMultiplier;
				initialClickY = mouseY;
			}
		} else {
			initialClickY = -1;
		}

		var = Mouse.getEventDWheel();
		if (var != 0) {
			if (var > 0) {
				var = -1;
			} else if (var < 0) {
				var = 1;
			}

			amountScrolled += (float) (var * getSlotHeight() / 2);
		}

	}

	protected void elementClicked(int id, boolean doubleClick, int mouseX, int mouseY) {
		if (doubleClick && getSelectedFile() != null && getSelectedFile().isDirectory()) {
			updateDir(getSelectedFile());
		} else if (doubleClick && getSelectedFile() != null) {
			callback.call(getSelectedFile());
		}
	}

	protected void elementHovered(int id, int mouseX, int mouseY) {
		File file = getFile(id);
		if (file == null)
			return;
		String name = file.getName();
		if (currentDir == null) {
			name = fsv.getSystemDisplayName(file);
		}
		if (MinecraftFactory.getVars().getStringWidth(name) > selectionBoxWidth - 18 - MinecraftFactory.getVars().getStringWidth("...")) {
			MinecraftFactory.getVars().getCurrentScreen().drawHoveringText(MinecraftFactory.getVars().splitStringToWidth(name, 200), mouseX, mouseY);
		}
	}

	@Override
	public File getCurrentDir() {
		return currentDir;
	}
}
