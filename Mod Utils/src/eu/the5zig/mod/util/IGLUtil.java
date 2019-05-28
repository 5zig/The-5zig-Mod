package eu.the5zig.mod.util;

public interface IGLUtil {

	void enableBlend();

	void disableBlend();

	void scale(float x, float y, float z);

	void translate(float x, float y, float z);

	void color(float r, float g, float b, float a);

	void color(float r, float g, float b);

	void pushMatrix();

	void popMatrix();

	void matrixMode(int mode);

	void loadIdentity();

	void clear(int i);

	void disableDepth();

	void enableDepth();

	void depthMask(boolean b);

	void disableLighting();

	void enableLighting();

	void disableFog();

	void tryBlendFuncSeparate(int i, int i1, int i2, int i3);

	void disableAlpha();

}