import eu.the5zig.mod.util.IGLUtil;

public class GLUtilHandle implements IGLUtil {

	@Override
	public void enableBlend() {
		bni.m();
	}

	@Override
	public void disableBlend() {
		bni.l();
	}

	@Override
	public void scale(float x, float y, float z) {
		bni.b(x, y, z);
	}

	@Override
	public void translate(float x, float y, float z) {
		bni.c(x, y, z);
	}

	@Override
	public void color(float r, float g, float b, float a) {
		bni.c(r, g, b, a);
	}

	@Override
	public void color(float r, float g, float b) {
		color(r, g, b, 1.0f);
	}

	@Override
	public void pushMatrix() {
		bni.G();
	}

	@Override
	public void popMatrix() {
		bni.H();
	}

	@Override
	public void matrixMode(int mode) {
		bni.n(mode);
	}

	@Override
	public void loadIdentity() {
		bni.F();
	}

	@Override
	public void clear(int i) {
		bni.m(i);
	}

	@Override
	public void disableDepth() {
		bni.j();
	}

	@Override
	public void enableDepth() {
		bni.k();
	}

	@Override
	public void depthMask(boolean b) {
		bni.a(b);
	}

	@Override
	public void disableLighting() {
		bni.g();
	}

	@Override
	public void enableLighting() {
		bni.f();
	}

	@Override
	public void disableFog() {
		bni.p();
	}

	@Override
	public void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
		bni.a(i, i1, i2, i3);
	}

	@Override
	public void disableAlpha() {
		bni.d();
	}
}
