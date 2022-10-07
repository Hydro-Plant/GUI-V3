package standard;

public class Positioning {
	public static double[] positioning(int from, int to) {
		final double[][] positions = { { -0.5, -0.5 }, { 0, -0.5 }, { 0.5, -0.5 }, { -0.5, 0 }, { 0, 0 }, { 0.5, 0 },
				{ -0.5, 0.5 }, { 0, 0.5 }, { 0.5, 0.5 } };
		double[] res = { positions[to][0] - positions[from][0], positions[to][1] - positions[from][1] };
		return res;
	}
}
