package standard;

public class Map {
	public static double map(double val, double from1, double from2, double to1, double to2) {
		double k = (to2 - to1) / (from2 - from1);
		double d = to1 - from1 * k;
		return k * val + d;
	}
}
