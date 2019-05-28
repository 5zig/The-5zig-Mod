package eu.the5zig.mod.gui.elements;

import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.util.Callable;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class BasicRow implements Row {

	protected int HEIGHT = 14;
	protected int LINE_HEIGHT = 11;

	protected String string;
	protected int maxWidth;

	private Callable<String> callback;

	public BasicRow(String string) {
		this.string = string;
		this.maxWidth = 95;
	}

	public BasicRow(String string, int maxWidth) {
		this.string = string;
		this.maxWidth = maxWidth;
	}

	public BasicRow(String string, int maxWidth, int height) {
		this.string = string;
		this.maxWidth = maxWidth;
		HEIGHT = height;
	}

	public BasicRow(Callable<String> callback, int maxWidth) {
		this.callback = callback;
		this.maxWidth = maxWidth;
	}

	public String getString() {
		return string;
	}

	@Override
	public int getLineHeight() {
		String toDraw = callback != null ? callback.call() : string;
		return (MinecraftFactory.getVars().splitStringToWidth(toDraw, maxWidth).size() - 1) * LINE_HEIGHT + HEIGHT;
	}

	@Override
	public void draw(int x, int y) {
		x = x + 2;
		y = y + 2;
		String toDraw = callback != null ? callback.call() : string;
		List<String> split = MinecraftFactory.getVars().splitStringToWidth(toDraw, maxWidth);
		for (int i = 0, splitStringToWidthSize = split.size(); i < splitStringToWidthSize; i++) {
			String line = split.get(i);
			MinecraftFactory.getVars().drawString(line, x, y);
			if (i < splitStringToWidthSize - 1)
				y += LINE_HEIGHT;
			else
				y += HEIGHT;
		}
	}
}
