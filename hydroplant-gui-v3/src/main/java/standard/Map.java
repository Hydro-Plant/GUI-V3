package standard;

import javafx.scene.paint.Color;

public class Map {
	public static double map(double val, double from1, double from2, double to1, double to2) {
		double k = (to2 - to1) / (from2 - from1);
		double d = to1 - from1 * k;
		return k * val + d;
	}

	public static Color map(double factor, Color color1, Color color2) {
		return Color.rgb((int)((factor * color2.getRed() + (1 - factor) * color1.getRed()) * 255), (int)((factor * color2.getGreen() + (1 - factor) * color1.getGreen()) * 255), (int)((factor * color2.getBlue() + (1 - factor) * color1.getBlue()) * 255), factor * color2.getOpacity() + (1 - factor) * color1.getOpacity());
	}
}
