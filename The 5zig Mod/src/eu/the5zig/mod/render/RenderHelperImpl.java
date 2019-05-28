package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;

import java.util.List;

public class RenderHelperImpl implements RenderHelper {

	@Override
	public void drawString(String string, int x, int y, Object... format) {
		The5zigMod.getVars().drawString(string, x, y, format);
	}

	@Override
	public void drawString(String string, int x, int y) {
		The5zigMod.getVars().drawString(string, x, y);
	}

	@Override
	public void drawCenteredString(String string, int x, int y) {
		The5zigMod.getVars().drawCenteredString(string, x, y);
	}

	@Override
	public void drawCenteredString(String string, int x, int y, int color) {
		The5zigMod.getVars().drawCenteredString(string, x, y, color);
	}

	@Override
	public void drawString(String string, int x, int y, int color, Object... format) {
		The5zigMod.getVars().drawString(string, x, y, color, format);
	}

	@Override
	public void drawString(String string, int x, int y, int color) {
		The5zigMod.getVars().drawString(string, x, y, color);
	}

	@Override
	public void drawString(String string, int x, int y, int color, boolean withShadow) {
		The5zigMod.getVars().drawString(string, x, y, color, withShadow);
	}

	@Override
	public List<String> splitStringToWidth(String string, int maxWidth) {
		return The5zigMod.getVars().splitStringToWidth(string, maxWidth);
	}

	@Override
	public int getStringWidth(String string) {
		return The5zigMod.getVars().getStringWidth(string);
	}

	@Override
	public String shortenToWidth(String string, int width) {
		return The5zigMod.getVars().shortenToWidth(string, width);
	}

	@Override
	public void drawRect(double left, double top, double right, double bottom, int color) {
		Gui.drawRect(left, top, right, bottom, color);
	}

	@Override
	public void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor, boolean verticalGradient) {
		Gui.drawGradientRect(left, top, right, bottom, startColor, endColor, verticalGradient);
	}

	@Override
	public void drawRectOutline(int left, int top, int right, int bottom, int color) {
		Gui.drawRectOutline(left, top, right, bottom, color);
	}

	@Override
	public void drawRectInline(int left, int top, int right, int bottom, int color) {
		Gui.drawRectInline(left, top, right, bottom, color);
	}

	@Override
	public void drawScaledCenteredString(String string, int x, int y, float scale) {
		Gui.drawScaledCenteredString(string, x, y, scale);
	}

	@Override
	public void drawScaledString(String string, int x, int y, float scale) {
		Gui.drawScaledString(string, x, y, scale);
	}

	@Override
	public void drawLargeText(String string) {
		DisplayRenderer.largeTextRenderer.render(string);
	}
}
