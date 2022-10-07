package standard;

public class Deckel {
	public static double deckel(double value, double min, double max) {
		return value * ((value >= min && value <= max) ? 1 : 0) + max * ((value > max) ? 1 : 0)
				+ min * ((value < min) ? 1 : 0);
	}
}
