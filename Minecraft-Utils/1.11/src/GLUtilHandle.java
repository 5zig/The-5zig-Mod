import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bqe.m();
	}

	@Override
	public void disableBlend() {
		bqe.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		bqe.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bqe.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bqe.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bqe.G();
	}

	@Override
	public void popMatrix() {
		bqe.H();
	}

	@Override
	public void matrixMode(int mode) {
		bqe.n(mode);
	}

	@Override
	public void loadIdentity() {
		bqe.F();
	}

	@Override
	public void clear(int i) {
		bqe.m(i);
	}

	@Override
	public void disableDepth() {
		bqe.j();
	}

	@Override
	public void enableDepth() {
		bqe.k();
	}

	@Override
	public void depthMask(boolean b) {
		bqe.a(b);
	}

	@Override
	public void disableLighting() {
		bqe.g();
	}

	@Override
	public void enableLighting() {
		bqe.f();
	}

	@Override
	public void disableFog() {
		bqe.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bqe.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bqe.d();
	}
}
