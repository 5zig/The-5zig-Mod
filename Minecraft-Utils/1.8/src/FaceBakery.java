import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3f;

public class FaceBakery {
	private static final float fieldA = 1.0F / (float) Math.cos(0.39269909262657166D) - 1.0F;
	private static final float fieldB = 1.0F / (float) Math.cos(0.7853981852531433D) - 1.0F;

	public static final class Constants {
		public static final int SOUTH = ej.d.a();
		public static final int UP = ej.b.a();
		public static final int EAST = ej.f.a();
		public static final int NORTH = ej.c.a();
		public static final int DOWN = ej.a.a();
		public static final int WEST = ej.e.a();
	}

	public FaceBakery() {
	}

	public clt a(Vector3f vector1, Vector3f vector2, clx blockPartFace, ej enumFacing, cxf modelRotation, clz blockPartRotation, boolean b1, boolean b2) {
		int[] data = this.a(blockPartFace, enumFacing, this.a(vector1, vector2), modelRotation, blockPartRotation, b1, b2);
		ej facing = a(data);
		if (b1) {
			this.a(data, facing, blockPartFace.e);
		}

		if (blockPartRotation == null) {
			this.a(data, facing);
		}

		fillNormal(data);

		return new clt(data, blockPartFace.c, facing);
	}

	private void fillNormal(int[] faceData) {
		Vector3f v1 = new Vector3f(faceData[3 * 7 + 0], faceData[3 * 7 + 1], faceData[3 * 7 + 2]);
		Vector3f t = new Vector3f(faceData[1 * 7 + 0], faceData[1 * 7 + 1], faceData[1 * 7 + 2]);
		Vector3f v2 = new Vector3f(faceData[2 * 7 + 0], faceData[2 * 7 + 1], faceData[2 * 7 + 2]);
		v1.sub(t);
		t.set(faceData[0 * 7 + 0], faceData[0 * 7 + 1], faceData[0 * 7 + 2]);
		v2.sub(t, v1);
		v1.cross(v2, v1);
		v1.normalize();

		int x = ((byte) (v1.x * 127)) & 0xFF;
		int y = ((byte) (v1.y * 127)) & 0xFF;
		int z = ((byte) (v1.z * 127)) & 0xFF;
		for (int i = 0; i < 4; i++) {
			faceData[i * 7 + 6] = x | (y << 0x08) | (z << 0x10);
		}
	}

	private int[] a(clx blockPartFace, ej enumFacing, float[] vec, cxf modelRotation, clz blockPartRotation, boolean b1, boolean b2) {
		int[] data = new int[28];

		for (int i = 0; i < 4; ++i) {
			this.a(data, i, enumFacing, blockPartFace, vec, modelRotation, blockPartRotation, b1, b2);
		}

		return data;
	}

	private int a(ej var) {
		float var1 = this.b(var);
		int var2 = uv.a((int) (var1 * 255.0F), 0, 255);
		return -16777216 | var2 << 16 | var2 << 8 | var2;
	}

	private float b(ej enumFacing) {
		switch (enumFacing) {
			case a:
				return 0.5F;
			case b:
				return 1.0F;
			case c:
			case d:
				return 0.8F;
			case e:
			case f:
				return 0.6F;
			default:
				return 1.0F;
		}
	}

	private float[] a(Vector3f var, Vector3f var1) {
		float[] var2 = new float[ej.values().length];
		var2[Constants.WEST] = var.x / 16.0F;
		var2[Constants.DOWN] = var.y / 16.0F;
		var2[Constants.NORTH] = var.z / 16.0F;
		var2[Constants.EAST] = var1.x / 16.0F;
		var2[Constants.UP] = var1.y / 16.0F;
		var2[Constants.SOUTH] = var1.z / 16.0F;
		return var2;
	}

	private void a(int[] dataIn, int count, ej enumFacing, clx blockPartFace, float[] vec, cxf modelRotation, clz blockPartRotation, boolean b1, boolean b2) {
		ej var10 = modelRotation.a(enumFacing);
		int var11 = b2 ? this.a(var10) : -1;
		cjh var12 = cje.a(enumFacing).a(count);
		Vector3f var13 = new Vector3f(vec[var12.a], vec[var12.b], vec[var12.c]);
		this.a(var13, blockPartRotation);
		int var14 = this.a(var13, enumFacing, count, modelRotation, b1);
		this.a(dataIn, var14, count, var13, var11, blockPartFace.e);
	}

	private void a(int[] dataIn, int var1, int count, Vector3f var3, int var4, cma blockPartFace) {
		int var7 = var1 * 7;
		dataIn[var7] = Float.floatToRawIntBits(var3.x);
		dataIn[var7 + 1] = Float.floatToRawIntBits(var3.y);
		dataIn[var7 + 2] = Float.floatToRawIntBits(var3.z);
		dataIn[var7 + 3] = var4;
		dataIn[var7 + 4] = Float.floatToRawIntBits(blockPartFace.a(count) / 16f);
		dataIn[var7 + 4 + 1] = Float.floatToRawIntBits(blockPartFace.b(count) / 16f);
	}

	private void a(Vector3f vector, clz var1) {
		if (var1 != null) {
			Matrix4d var2 = this.a();
			Vector3f var3 = new Vector3f(0.0F, 0.0F, 0.0F);
			switch (var1.b) {
				case a:
					var2.mul(a(new AxisAngle4d(1.0D, 0.0D, 0.0D, var1.c * 0.017453292519943295D)));
					var3.set(0.0F, 1.0F, 1.0F);
					break;
				case b:
					var2.mul(a(new AxisAngle4d(0.0D, 1.0D, 0.0D, var1.c * 0.017453292519943295D)));
					var3.set(1.0F, 0.0F, 1.0F);
					break;
				case c:
					var2.mul(a(new AxisAngle4d(0.0D, 0.0D, 1.0D, var1.c * 0.017453292519943295D)));
					var3.set(1.0F, 1.0F, 0.0F);
			}

			if (var1.d) {
				if (Math.abs(var1.c) == 22.5F) {
					var3.scale(fieldA);
				} else {
					var3.scale(fieldB);
				}

				var3.add(new Vector3f(1.0F, 1.0F, 1.0F));
			} else {
				var3.set(1.0F, 1.0F, 1.0F);
			}

			this.a(vector, new Vector3f(var1.a), var2, var3);
		}
	}

	public int a(Vector3f var, ej var1, int var2, cxf var3, boolean var4) {
		if (var3 == cxf.a) {
			return var2;
		} else {
			this.a(var, new Vector3f(0.5F, 0.5F, 0.5F), var3.a(), new Vector3f(1.0F, 1.0F, 1.0F));
			return var3.a(var1, var2);
		}
	}

	private void a(Vector3f var, Vector3f var1, Matrix4d var2, Vector3f var3) {
		var.sub(var1);
		var2.transform(var);
		var.x *= var3.x;
		var.y *= var3.y;
		var.z *= var3.z;
		var.add(var1);
	}

	private Matrix4d a(AxisAngle4d paramAxisAngle4d) {
		Matrix4d localMatrix4d = a();
		localMatrix4d.setRotation(paramAxisAngle4d);
		return localMatrix4d;
	}

	private Matrix4d a() {
		Matrix4d var = new Matrix4d();
		var.setIdentity();
		return var;
	}

	public static ej a(int[] var) {
		Vector3f var1 = new Vector3f(Float.intBitsToFloat(var[0]), Float.intBitsToFloat(var[1]), Float.intBitsToFloat(var[2]));
		Vector3f var2 = new Vector3f(Float.intBitsToFloat(var[7]), Float.intBitsToFloat(var[8]), Float.intBitsToFloat(var[9]));
		Vector3f var3 = new Vector3f(Float.intBitsToFloat(var[14]), Float.intBitsToFloat(var[15]), Float.intBitsToFloat(var[16]));
		Vector3f var4 = new Vector3f();
		Vector3f var5 = new Vector3f();
		Vector3f var6 = new Vector3f();
		var4.sub(var1, var2);
		var5.sub(var3, var2);
		var6.cross(var5, var4);
		float var7 = (float) Math.sqrt((double) (var6.x * var6.x + var6.y * var6.y + var6.z * var6.z));
		var6.x /= var7;
		var6.y /= var7;
		var6.z /= var7;
		ej var8 = null;
		float var9 = 0.0F;
		ej[] var10 = ej.values();
		int var11 = var10.length;

		for (int var12 = 0; var12 < var11; ++var12) {
			ej var13 = var10[var12];
			fd var14 = var13.m();
			Vector3f var15 = new Vector3f((float) var14.n(), (float) var14.o(), (float) var14.p());
			float var16 = var6.dot(var15);
			if (var16 >= 0.0F && var16 > var9) {
				var9 = var16;
				var8 = var13;
			}
		}

		if (var8 == null) {
			return ej.b;
		} else {
			return var8;
		}
	}

	public void a(int[] data, ej facing, cma blockPartFace) {
		for (int i = 0; i < 4; ++i) {
			this.a(i, data, facing, blockPartFace);
		}

	}

	private void a(int[] var, ej var1) {
		int[] var2 = new int[var.length];
		System.arraycopy(var, 0, var2, 0, var.length);
		float[] var3 = new float[ej.values().length];
		var3[Constants.WEST] = 999.0F;
		var3[Constants.DOWN] = 999.0F;
		var3[Constants.NORTH] = 999.0F;
		var3[Constants.EAST] = -999.0F;
		var3[Constants.UP] = -999.0F;
		var3[Constants.SOUTH] = -999.0F;

		int var5;
		float var8;
		for (int var4 = 0; var4 < 4; ++var4) {
			var5 = 7 * var4;
			float var6 = Float.intBitsToFloat(var2[var5]);
			float var7 = Float.intBitsToFloat(var2[var5 + 1]);
			var8 = Float.intBitsToFloat(var2[var5 + 2]);
			if (var6 < var3[Constants.WEST]) {
				var3[Constants.WEST] = var6;
			}

			if (var7 < var3[Constants.DOWN]) {
				var3[Constants.DOWN] = var7;
			}

			if (var8 < var3[Constants.NORTH]) {
				var3[Constants.NORTH] = var8;
			}

			if (var6 > var3[Constants.EAST]) {
				var3[Constants.EAST] = var6;
			}

			if (var7 > var3[Constants.UP]) {
				var3[Constants.UP] = var7;
			}

			if (var8 > var3[Constants.SOUTH]) {
				var3[Constants.SOUTH] = var8;
			}
		}

		cje var17 = cje.a(var1);

		for (var5 = 0; var5 < 4; ++var5) {
			int var18 = 7 * var5;
			cjh var19 = var17.a(var5);
			var8 = var3[var19.a];
			float var9 = var3[var19.b];
			float var10 = var3[var19.c];
			var[var18] = Float.floatToRawIntBits(var8);
			var[var18 + 1] = Float.floatToRawIntBits(var9);
			var[var18 + 2] = Float.floatToRawIntBits(var10);

			for (int var11 = 0; var11 < 4; ++var11) {
				int var12 = 7 * var11;
				float var13 = Float.intBitsToFloat(var2[var12]);
				float var14 = Float.intBitsToFloat(var2[var12 + 1]);
				float var15 = Float.intBitsToFloat(var2[var12 + 2]);
				if (uv.a(var8, var13) && uv.a(var9, var14) && uv.a(var10, var15)) {
					var[var18 + 4] = var2[var12 + 4];
					var[var18 + 4 + 1] = var2[var12 + 4 + 1];
				}
			}
		}

	}

	private void a(int count, int[] data, ej enumFacing, cma blockPartFace) {
		int var5 = 7 * count;
		float var6 = Float.intBitsToFloat(data[var5]);
		float var7 = Float.intBitsToFloat(data[var5 + 1]);
		float var8 = Float.intBitsToFloat(data[var5 + 2]);
		if (var6 < -0.1F || var6 >= 1.1F) {
			var6 -= (float) uv.d(var6);
		}

		if (var7 < -0.1F || var7 >= 1.1F) {
			var7 -= (float) uv.d(var7);
		}

		if (var8 < -0.1F || var8 >= 1.1F) {
			var8 -= (float) uv.d(var8);
		}

		float var9 = 0.0F;
		float var10 = 0.0F;
		switch (enumFacing) {
			case a:
				var9 = var6 * 16.0F;
				var10 = (1.0F - var8) * 16.0F;
				break;
			case b:
				var9 = var6 * 16.0F;
				var10 = var8 * 16.0F;
				break;
			case c:
				var9 = (1.0F - var6) * 16.0F;
				var10 = (1.0F - var7) * 16.0F;
				break;
			case d:
				var9 = var6 * 16.0F;
				var10 = (1.0F - var7) * 16.0F;
				break;
			case e:
				var9 = var8 * 16.0F;
				var10 = (1.0F - var7) * 16.0F;
				break;
			case f:
				var9 = (1.0F - var8) * 16.0F;
				var10 = (1.0F - var7) * 16.0F;
		}

		int var11 = blockPartFace.c(count) * 7;
		data[var11 + 4] = Float.floatToRawIntBits(var9 / 16f);
		data[var11 + 4 + 1] = Float.floatToRawIntBits(var10 / 16f);
	}
}