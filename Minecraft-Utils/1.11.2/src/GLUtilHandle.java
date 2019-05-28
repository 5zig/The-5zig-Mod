import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bqg.m();
	}

	@Override
	public void disableBlend() {
		bqg.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		bqg.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bqg.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bqg.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bqg.G();
	}

	@Override
	public void popMatrix() {
		bqg.H();
	}

	@Override
	public void matrixMode(int mode) {
		bqg.n(mode);
	}

	@Override
	public void loadIdentity() {
		bqg.F();
	}

	@Override
	public void clear(int i) {
		bqg.m(i);
	}

	@Override
	public void disableDepth() {
		bqg.j();
	}

	@Override
	public void enableDepth() {
		bqg.k();
	}

	@Override
	public void depthMask(boolean b) {
		bqg.a(b);
	}

	@Override
	public void disableLighting() {
		bqg.g();
	}

	@Override
	public void enableLighting() {
		bqg.f();
	}

	@Override
	public void disableFog() {
		bqg.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bqg.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bqg.d();
	}
}
