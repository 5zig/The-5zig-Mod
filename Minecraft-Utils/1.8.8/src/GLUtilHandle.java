import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bfl.l();
	}

	@Override
	public void disableBlend() {
		bfl.k();
	}

	@Override
	public void scale(float x, float y, float z) {
		bfl.a(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bfl.b(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bfl.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bfl.E();
	}

	@Override
	public void popMatrix() {
		bfl.F();
	}

	@Override
	public void matrixMode(int mode) {
		bfl.n(mode);
	}

	@Override
	public void loadIdentity() {
		bfl.D();
	}

	@Override
	public void clear(int i) {
		bfl.m(i);
	}

	@Override
	public void disableDepth() {
		bfl.i();
	}

	@Override
	public void enableDepth() {
		bfl.j();
	}

	@Override
	public void depthMask(boolean b) {
		bfl.a(b);
	}

	@Override
	public void disableLighting() {
		bfl.f();
	}

	@Override
	public void enableLighting() {
		bfl.e();
	}

	@Override
	public void disableFog() {
		bfl.n();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bfl.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bfl.c();
	}
}
