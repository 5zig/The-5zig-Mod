import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		ctp.m();
	}

	@Override
	public void disableBlend() {
		ctp.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		ctp.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		ctp.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		ctp.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		ctp.G();
	}

	@Override
	public void popMatrix() {
		ctp.H();
	}

	@Override
	public void matrixMode(int mode) {
		ctp.n(mode);
	}

	@Override
	public void loadIdentity() {
		ctp.F();
	}

	@Override
	public void clear(int i) {
		ctp.m(i);
	}

	@Override
	public void disableDepth() {
		ctp.j();
	}

	@Override
	public void enableDepth() {
		ctp.k();
	}

	@Override
	public void depthMask(boolean b) {
		ctp.a(b);
	}

	@Override
	public void disableLighting() {
		ctp.g();
	}

	@Override
	public void enableLighting() {
		ctp.f();
	}

	@Override
	public void disableFog() {
		ctp.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		ctp.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		ctp.d();
	}
}
