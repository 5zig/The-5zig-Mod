import eu.the5zig.mod.util.IGLUtil;

import static org.lwjgl.opengl.GL11.*;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		glEnable(GL_BLEND);
	}

	@Override
	public void disableBlend() {
		glDisable(GL_BLEND);
	}

	@Override
	public void scale(float x, float y, float z) {
		glScalef(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		glTranslatef(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		glColor4f(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		glColor3f(r, g, b);
	}

	@Override
	public void pushMatrix() {
		glPushMatrix();
	}

	@Override
	public void popMatrix() {
		glPopMatrix();
	}

	@Override
	public void matrixMode(int mode) {
		glMatrixMode(mode);
	}

	@Override
	public void loadIdentity() {
		glLoadIdentity();
	}

	@Override
	public void clear(int i) {
		glClear(i);
	}

	@Override
	public void disableDepth() {
		glDisable(GL_DEPTH_TEST);
	}

	@Override
	public void enableDepth() {
		glEnable(GL_DEPTH_TEST);
	}

	@Override
	public void depthMask(boolean b) {
		glDepthMask(b);
	}

	@Override
	public void disableLighting() {
		glDisable(GL_LIGHTING);
	}

	@Override
	public void enableLighting() {
		glEnable(GL_LIGHTING);
	}

	@Override
	public void disableFog() {
		glDisable(GL_FOG);
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		buu.c(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		glDisable(GL_ALPHA_TEST);
	}
}
