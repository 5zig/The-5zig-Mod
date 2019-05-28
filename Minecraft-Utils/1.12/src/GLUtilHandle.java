import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		buq.m();
	}

	@Override
	public void disableBlend() {
		buq.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		buq.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		buq.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		buq.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		buq.G();
	}

	@Override
	public void popMatrix() {
		buq.H();
	}

	@Override
	public void matrixMode(int mode) {
		buq.n(mode);
	}

	@Override
	public void loadIdentity() {
		buq.F();
	}

	@Override
	public void clear(int i) {
		buq.m(i);
	}

	@Override
	public void disableDepth() {
		buq.j();
	}

	@Override
	public void enableDepth() {
		buq.k();
	}

	@Override
	public void depthMask(boolean b) {
		buq.a(b);
	}

	@Override
	public void disableLighting() {
		buq.g();
	}

	@Override
	public void enableLighting() {
		buq.f();
	}

	@Override
	public void disableFog() {
		buq.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		buq.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		buq.d();
	}
}
