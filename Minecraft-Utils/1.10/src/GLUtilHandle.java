import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bob.m();
	}

	@Override
	public void disableBlend() {
		bob.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		bob.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bob.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bob.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bob.G();
	}

	@Override
	public void popMatrix() {
		bob.H();
	}

	@Override
	public void matrixMode(int mode) {
		bob.n(mode);
	}

	@Override
	public void loadIdentity() {
		bob.F();
	}

	@Override
	public void clear(int i) {
		bob.m(i);
	}

	@Override
	public void disableDepth() {
		bob.j();
	}

	@Override
	public void enableDepth() {
		bob.k();
	}

	@Override
	public void depthMask(boolean b) {
		bob.a(b);
	}

	@Override
	public void disableLighting() {
		bob.g();
	}

	@Override
	public void enableLighting() {
		bob.f();
	}

	@Override
	public void disableFog() {
		bob.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bob.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bob.d();
	}
}
