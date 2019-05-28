import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		cjm.l();
	}

	@Override
	public void disableBlend() {
		cjm.k();
	}

	@Override
	public void scale(float x, float y, float z) {
		cjm.a(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		cjm.b(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		cjm.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		cjm.E();
	}

	@Override
	public void popMatrix() {
		cjm.F();
	}

	@Override
	public void matrixMode(int mode) {
		cjm.n(mode);
	}

	@Override
	public void loadIdentity() {
		cjm.D();
	}

	@Override
	public void clear(int i) {
		cjm.m(i);
	}

	@Override
	public void disableDepth() {
		cjm.i();
	}

	@Override
	public void enableDepth() {
		cjm.j();
	}

	@Override
	public void depthMask(boolean b) {
		cjm.a(b);
	}

	@Override
	public void disableLighting() {
		cjm.f();
	}

	@Override
	public void enableLighting() {
		cjm.e();
	}

	@Override
	public void disableFog() {
		cjm.n();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		cjm.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		cjm.c();
	}
}
