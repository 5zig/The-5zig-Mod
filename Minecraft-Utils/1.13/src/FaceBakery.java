
public class FaceBakery {
	private static final float fieldA = 1.0F / (float) Math.cos(0.39269909262657166D) - 1.0F;
	private static final float fieldB = 1.0F / (float) Math.cos(0.7853981852531433D) - 1.0F;

	public static final class Constants {
		public static final int SOUTH = ep.d.a();
		public static final int UP = ep.b.a();
		public static final int EAST = ep.f.a();
		public static final int NORTH = ep.c.a();
		public static final int DOWN = ep.a.a();
		public static final int WEST = ep.e.a();
	}

	public FaceBakery() {
	}

	public cum a(dhc vector1, dhc vector2, cuo blockPartFace, ep enumFacing, deq modelRotation, cup blockPartRotation, boolean uvLocked, boolean shade) {
		int[] data = this.makeQuadVertexData(blockPartFace, enumFacing, this.getPositionsDiv16(vector1, vector2), modelRotation, blockPartRotation, uvLocked, shade);
		ep facing = a(data);
		if (uvLocked) {
			this.a(data, facing, blockPartFace.d);
		}

		if (blockPartRotation == null) {
			this.a(data, facing);
		}

//		fillNormal(data);

		return new cum(data, blockPartFace.b, facing, null);
	}

//	private void fillNormal(int[] faceData) {
//		dhc v1 = new dhc(faceData[3 * 7 + 0], faceData[3 * 7 + 1], faceData[3 * 7 + 2]);
//		dhc t = new dhc(faceData[1 * 7 + 0], faceData[1 * 7 + 1], faceData[1 * 7 + 2]);
//		dhc v2 = new dhc(faceData[2 * 7 + 0], faceData[2 * 7 + 1], faceData[2 * 7 + 2]);
//		dhc result1 = new dhc();
//		dhc result2 = new dhc();
//		dhc result3 = new dhc();
//		dhc.sub(v1, t, result1);
//		t.set(faceData[0 * 7 + 0], faceData[0 * 7 + 1], faceData[0 * 7 + 2]);
//		dhc.sub(v2, t, result2);
//		dhc.cross(result2, result1, result3);
//		result3.normalise();
//
//		int x = ((byte) (result3.x * 127)) & 0xFF;
//		int y = ((byte) (result3.y * 127)) & 0xFF;
//		int z = ((byte) (result3.z * 127)) & 0xFF;
//		for (int i = 0; i < 4; i++) {
//			faceData[i * 7 + 6] = x | (y << 0x08) | (z << 0x10);
//		}
//	}

	private int[] makeQuadVertexData(cuo blockPartFace, ep enumFacing, float[] vec, deq modelRotation, cup blockPartRotation, boolean b1, boolean b2) {
		int[] data = new int[28];

		for (int i = 0; i < 4; ++i) {
			this.fillVertexData(data, i, enumFacing, blockPartFace, vec, modelRotation, blockPartRotation, b2);
		}

		return data;
	}

	private int a(ep var) {
		float var1 = this.getFaceBrightness(var);
		int var2 = xp.a((int) (var1 * 255.0F), 0, 255);
		return -16777216 | var2 << 16 | var2 << 8 | var2;
	}

	private float getFaceBrightness(ep enumFacing) {
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

	private float[] getPositionsDiv16(dhc var, dhc var1) {
		float[] var2 = new float[ep.values().length];
		var2[Constants.WEST] = var.a() / 16.0F;
		var2[Constants.DOWN] = var.b() / 16.0F;
		var2[Constants.NORTH] = var.c() / 16.0F;
		var2[Constants.EAST] = var1.a() / 16.0F;
		var2[Constants.UP] = var1.b() / 16.0F;
		var2[Constants.SOUTH] = var1.c() / 16.0F;
		return var2;
	}

	private void fillVertexData(int[] dataIn, int count, ep enumFacing, cuo blockPartFace, float[] vec, deq modelRotation, cup blockPartRotation, boolean b2) {
		ep var10 = modelRotation.a(enumFacing);
		int var11 = b2 ? this.a(var10) : -1;
		ctl.b var12 = ctl.a(enumFacing).a(count);
		dhc var13 = new dhc(vec[var12.a], vec[var12.b], vec[var12.c]);
		this.a(var13, blockPartRotation);
		int var14 = this.a(var13, enumFacing, count, modelRotation);
		this.storeVertexData(dataIn, var14, count, var13, var11, blockPartFace.d);
	}

	private void storeVertexData(int[] dataIn, int var1, int count, dhc var3, int var4, cuq blockPartFace) {
		int var7 = var1 * 7;
		dataIn[var7] = Float.floatToRawIntBits(var3.a());
		dataIn[var7 + 1] = Float.floatToRawIntBits(var3.b());
		dataIn[var7 + 2] = Float.floatToRawIntBits(var3.c());
		dataIn[var7 + 3] = var4;
		dataIn[var7 + 4] = Float.floatToRawIntBits(blockPartFace.a(count) / 16f);
		dataIn[var7 + 4 + 1] = Float.floatToRawIntBits(blockPartFace.b(count) / 16f);
	}

	private void a(dhc vector, cup blockPartRotation) {
		if (blockPartRotation != null) {
			dhc var3;
			dhc var4;
			switch(blockPartRotation.b) {
				case a:
					var3 = new dhc(1.0F, 0.0F, 0.0F);
					var4 = new dhc(0.0F, 1.0F, 1.0F);
					break;
				case b:
					var3 = new dhc(0.0F, 1.0F, 0.0F);
					var4 = new dhc(1.0F, 0.0F, 1.0F);
					break;
				case c:
					var3 = new dhc(0.0F, 0.0F, 1.0F);
					var4 = new dhc(1.0F, 1.0F, 0.0F);
					break;
				default:
					throw new IllegalArgumentException("There are only 3 axes");
			}

			dha var5 = new dha(var3, blockPartRotation.c, true);
			if (blockPartRotation.d) {
				if (Math.abs(blockPartRotation.c) == 22.5F) {
					var4.a(fieldA);
				} else {
					var4.a(fieldB);
				}

				var4.b(1.0F, 1.0F, 1.0F);
			} else {
				var4.a(1.0F, 1.0F, 1.0F);
			}

			this.rotateScale(vector, new dhc(blockPartRotation.a), var5, var4);
		}
	}

	public int a(dhc var, ep var1, int var2, deq var3) {
		if (var3 == deq.a) {
			return var2;
		} else {
			this.rotateScale(var, new dhc(0.5F, 0.5F, 0.5F), var3.a(), new dhc(1.0F, 1.0F, 1.0F));
			return var3.a(var1, var2);
		}
	}

	private void rotateScale(dhc var1, dhc var2, dha var3, dhc var4) {
		dhd var5 = new dhd(var1.a() - var2.a(), var1.b() - var2.b(), var1.c() - var2.c(), 1.0F);
		var5.a(var3);
		var5.a(var4);
		var1.a(var5.a() + var2.a(), var5.b() + var2.b(), var5.c() + var2.c());
	}

	public static ep a(int[] var0) {
		dhc var1 = new dhc(Float.intBitsToFloat(var0[0]), Float.intBitsToFloat(var0[1]), Float.intBitsToFloat(var0[2]));
		dhc var2 = new dhc(Float.intBitsToFloat(var0[7]), Float.intBitsToFloat(var0[8]), Float.intBitsToFloat(var0[9]));
		dhc var3 = new dhc(Float.intBitsToFloat(var0[14]), Float.intBitsToFloat(var0[15]), Float.intBitsToFloat(var0[16]));
		dhc var4 = new dhc(var1);
		var4.a(var2);
		dhc var5 = new dhc(var3);
		var5.a(var2);
		dhc var6 = new dhc(var5);
		var6.c(var4);
		var6.d();
		ep var7 = null;
		float var8 = 0.0F;
		ep[] var9 = ep.values();

		for (ep var12 : var9) {
			ff var13 = var12.n();
			dhc var14 = new dhc((float) var13.o(), (float) var13.p(), (float) var13.q());
			float var15 = var6.b(var14);
			if (var15 >= 0.0F && var15 > var8) {
				var8 = var15;
				var7 = var12;
			}
		}

		if (var7 == null) {
			return ep.b;
		} else {
			return var7;
		}
	}

	public void a(int[] data, ep facing, cuq var) {
		for (int i = 0; i < 4; ++i) {
			this.a(i, data, facing, var);
		}

	}

	private void a(int[] var, ep var1) {
		int[] var2 = new int[var.length];
		System.arraycopy(var, 0, var2, 0, var.length);
		float[] var3 = new float[ep.values().length];
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

		ctl var17 = ctl.a(var1);

		for (var5 = 0; var5 < 4; ++var5) {
			int var18 = 7 * var5;
			ctl.b var19 = var17.a(var5);
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
				if (xp.a(var8, var13) && xp.a(var9, var14) && xp.a(var10, var15)) {
					var[var18 + 4] = var2[var12 + 4];
					var[var18 + 4 + 1] = var2[var12 + 4 + 1];
				}
			}
		}

	}

	private void a(int count, int[] data, ep enumFacing, cuq blockPartFace) {
		int var5 = 7 * count;
		float var6 = Float.intBitsToFloat(data[var5]);
		float var7 = Float.intBitsToFloat(data[var5 + 1]);
		float var8 = Float.intBitsToFloat(data[var5 + 2]);
		if (var6 < -0.1F || var6 >= 1.1F) {
			var6 -= (float) xp.d(var6);
		}

		if (var7 < -0.1F || var7 >= 1.1F) {
			var7 -= (float) xp.d(var7);
		}

		if (var8 < -0.1F || var8 >= 1.1F) {
			var8 -= (float) xp.d(var8);
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