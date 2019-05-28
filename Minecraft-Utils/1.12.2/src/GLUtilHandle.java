import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bus.m();
	}

	@Override
	public void disableBlend() {
		bus.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		bus.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bus.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bus.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bus.G();
	}

	@Override
	public void popMatrix() {
		bus.H();
	}

	@Override
	public void matrixMode(int mode) {
		bus.n(mode);
	}

	@Override
	public void loadIdentity() {
		bus.F();
	}

	@Override
	public void clear(int i) {
		bus.m(i);
	}

	@Override
	public void disableDepth() {
		bus.j();
	}

	@Override
	public void enableDepth() {
		bus.k();
	}

	@Override
	public void depthMask(boolean b) {
		bus.a(b);
	}

	@Override
	public void disableLighting() {
		bus.g();
	}

	@Override
	public void enableLighting() {
		bus.f();
	}

	@Override
	public void disableFog() {
		bus.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bus.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bus.d();
	}
}
