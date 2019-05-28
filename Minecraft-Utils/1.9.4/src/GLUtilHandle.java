import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bnf.m();
	}

	@Override
	public void disableBlend() {
		bnf.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		bnf.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bnf.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bnf.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bnf.G();
	}

	@Override
	public void popMatrix() {
		bnf.H();
	}

	@Override
	public void matrixMode(int mode) {
		bnf.n(mode);
	}

	@Override
	public void loadIdentity() {
		bnf.F();
	}

	@Override
	public void clear(int i) {
		bnf.m(i);
	}

	@Override
	public void disableDepth() {
		bnf.j();
	}

	@Override
	public void enableDepth() {
		bnf.k();
	}

	@Override
	public void depthMask(boolean b) {
		bnf.a(b);
	}

	@Override
	public void disableLighting() {
		bnf.g();
	}

	@Override
	public void enableLighting() {
		bnf.f();
	}

	@Override
	public void disableFog() {
		bnf.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bnf.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bnf.d();
	}
}
