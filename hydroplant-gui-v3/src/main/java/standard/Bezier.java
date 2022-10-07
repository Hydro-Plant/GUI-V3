package standard;

public class Bezier {
	public static double bezier_curve_1d(double t, double p1, double p2) {
		return bezier_curve_1d(t, 0, p1, p2, 1);
	}

	public static double bezier_curve_1d(double t, double p0, double p1, double p2, double p3) {
		double res = Math.pow(1f - t, 3) * p0 + 3f * Math.pow(1f - t, 2) * t * p1 + 3f * (1 - t) * Math.pow(t, 2) * p2
				+ Math.pow(t, 3) * p3;
		return res;
	}

	public static Vector bezier_curve_2d(double t, Vector p1, Vector p2) {
		return bezier_curve_2d(t, new Vector(0, 0), p1, p2, new Vector(1, 1));
	}

	public static Vector bezier_curve_2d(double t, Vector p0, Vector p1, Vector p2, Vector p3) {
		Vector res = p0.mult(Math.pow(1f - t, 3)).add(p1.mult(3f * Math.pow(1f - t, 2) * t)
				.add(p2.mult(3f * (1 - t) * Math.pow(t, 2)).add(p3.mult(Math.pow(t, 3)))));
		return res;
	}
}
