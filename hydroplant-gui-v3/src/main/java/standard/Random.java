package standard;

public class Random {
	public static double rand(double max) {
		return 0 + Math.random() * (max - 0);
	}

	public static double rand(double min, double max) {
		return min + Math.random() * (max - min);
	}
}
