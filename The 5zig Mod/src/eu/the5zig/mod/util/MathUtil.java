package eu.the5zig.mod.util;

import java.math.BigDecimal;

public class MathUtil {

	private MathUtil() {
	}

	public static BigDecimal binomcdf(int n, double p, int k) {
		BigDecimal result = new BigDecimal(0);
		for (int i = 0; i <= k; i++) {
			result = result.add(binompdf(n, p, i));
		}
		return result;
	}

	public static BigDecimal binompdf(int n, double p, int k) {
		BigDecimal binomialkoeffizient = binomalcoefficient(n, k);
		BigDecimal pp = new BigDecimal(p).pow(k);
		BigDecimal pq = new BigDecimal(1 - p).pow(n - k);

		return binomialkoeffizient.multiply(pp).multiply(pq);
	}

	private static BigDecimal binomalcoefficient(int n, int k) {
		if (k == 0) return new BigDecimal(1);
		if (2 * k > n) return binomalcoefficient(n, n - k);
		BigDecimal result = new BigDecimal(n - k + 1);
		for (int i = 2; i <= k; i++) {
			result = result.multiply(new BigDecimal(n - k + i));
			result = result.divide(new BigDecimal(i));
		}
		return result;
	}

}
