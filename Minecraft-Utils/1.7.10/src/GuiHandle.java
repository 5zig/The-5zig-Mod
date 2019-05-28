import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.IGuiHandle;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.util.GLUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

public class GuiHandle extends bdw implements IGuiHandle {

	private Gui child;

	public GuiHandle(Gui child) {
		this.child = child;
	}

	@Override
	public void b() {
		child.initGui0();
	}

	@Override
	protected void a(bcb button) {
		child.actionPerformed0((IButton) button);
	}

	@Override
	public void a(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		child.drawScreen0(mouseX, mouseY, partialTicks);
		super.a(mouseX, mouseY, partialTicks);
	}

	@Override
	public void e() {
		child.tick0();
	}

	@Override
	public void k() {
		child.handleMouseInput0();
		super.k();
	}

	@Override
	public void m() {
		child.guiClosed0();
	}

	@Override
	protected void a(char c, int i) {
		child.keyTyped0(c, i);
	}

	@Override
	protected void a(int mouseX, int mouseY, int button) {
		super.a(mouseX, mouseY, button);
		child.mouseClicked0(mouseX, mouseY, button);
	}

	@Override
	protected void b(int mouseX, int mouseY, int state) {
		super.b(mouseX, mouseY, state);
		child.mouseReleased0(mouseX, mouseY, state);
	}

	@Override
	public int getWidth() {
		return l;
	}

	@Override
	public int getHeight() {
		return m;
	}

	@Override
	public void setResolution(int width, int height) {
		a(((Variables) MinecraftFactory.getVars()).getMinecraft(), width, height);
	}

	@Override
	public void drawDefaultBackground() {
		c();
	}

	@Override
	public void drawMenuBackground() {
		c(0);
	}

	@Override
	public void drawTexturedModalRect(int x, int y, int texX, int texY, int width, int height) {
		b(x, y, texX, texY, width, height);
	}

	@Override
	public void drawHoveringText(List<String> lines, int x, int y) {
		if (!lines.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			bam.a();
			GLUtil.disableLighting();
			int maxWidth = 0;

			for (String line : lines) {
				int width = MinecraftFactory.getVars().getStringWidth(line);
				if (width > maxWidth) {
					maxWidth = width;
				}
			}

			int x1 = x + 12;
			int y1 = y - 12;
			int height = 8;
			if (lines.size() > 1) {
				height += 2 + (lines.size() - 1) * 10;
			}

			if (x1 + maxWidth > getWidth()) {
				x1 -= 28 + maxWidth;
			}

			if (y1 + height + 6 > getHeight()) {
				y1 = getHeight() - height - 6;
			}

			this.a(x1 - 3, y1 - 4, x1 + maxWidth + 3, y1 - 3, -267386864, -267386864);
			this.a(x1 - 3, y1 + height + 3, x1 + maxWidth + 3, y1 + height + 4, -267386864, -267386864);
			this.a(x1 - 3, y1 - 3, x1 + maxWidth + 3, y1 + height + 3, -267386864, -267386864);
			this.a(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
			this.a(x1 + maxWidth + 3, y1 - 3, x1 + maxWidth + 4, y1 + height + 3, -267386864, -267386864);
			this.a(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 1347420415, 1344798847);
			this.a(x1 + maxWidth + 2, y1 - 3 + 1, x1 + maxWidth + 3, y1 + height + 3 - 1, 1347420415, 1344798847);
			this.a(x1 - 3, y1 - 3, x1 + maxWidth + 3, y1 - 3 + 1, 1347420415, 1347420415);
			this.a(x1 - 3, y1 + height + 2, x1 + maxWidth + 3, y1 + height + 3, 1344798847, 1344798847);

			for (int i = 0; i < lines.size(); ++i) {
				String line = lines.get(i);
				MinecraftFactory.getVars().drawString(line, x1, y1, -1);
				if (i == 0) {
					y1 += 2;
				}

				y1 += 10;
			}

			bam.b();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GLUtil.disableLighting();
		}
	}

	public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
		a(x, y, u, v, width, height, textureWidth, textureHeight);
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		double i;
		if (left < right) {
			i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			i = top;
			top = bottom;
			bottom = i;
		}

		float a = (float) (color >> 24 & 255) / 255.0F;
		float b = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float r = (float) (color & 255) / 255.0F;
		bmh tesselator = bmh.a;
		GL11.glEnable(3042);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		buu.c(770, 771, 1, 0);
		GL11.glColor4f(b, g, r, a);
		tesselator.b();
		tesselator.a(left, bottom, 0.0D);
		tesselator.a(right, bottom, 0.0D);
		tesselator.a(right, top, 0.0D);
		tesselator.a(left, top, 0.0D);
		tesselator.a();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(3042);
	}

	public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor, boolean vertical) {
		float a1 = (float) (startColor >> 24 & 255) / 255.0F;
		float r1 = (float) (startColor >> 16 & 255) / 255.0F;
		float g1 = (float) (startColor >> 8 & 255) / 255.0F;
		float b1 = (float) (startColor & 255) / 255.0F;
		float a2 = (float) (endColor >> 24 & 255) / 255.0F;
		float r2 = (float) (endColor >> 16 & 255) / 255.0F;
		float g2 = (float) (endColor >> 8 & 255) / 255.0F;
		float b2 = (float) (endColor & 255) / 255.0F;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GLUtil.enableBlend();
		GLUtil.disableAlpha();
		GLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		bmh tesselator = bmh.a;
		tesselator.b();
		if (!vertical) {
			tesselator.a(r1, g1, b1, a1);
			tesselator.a(right, top, 1.0);
			tesselator.a(left, top, 1.0);
			tesselator.a(r2, g2, b2, a2);
			tesselator.b(left, bottom, 1.0);
			tesselator.b(right, bottom, 1.0);
		} else {
			tesselator.a(r1, g1, b1, a1);
			tesselator.a(left, top, 1.0);
			tesselator.a(left, bottom, 1.0);
			tesselator.a(r2, g2, b2, a2);
			tesselator.a(right, bottom, 1.0);
			tesselator.a(right, top, 1.0);
		}
		tesselator.a();

		GL11.glShadeModel(GL11.GL_FLAT);
		GLUtil.disableBlend();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public Gui getChild() {
		return child;
	}
}
