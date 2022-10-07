package standard;

import javafx.scene.paint.Color;

public class Alpha {
	public final static Color changeAlpha(Color original, double alpha) {
		return Color.rgb((int) Math.floor(original.getRed() * 255), (int) Math.floor(original.getGreen() * 255),
				(int) Math.floor(original.getBlue() * 255), alpha);
	}
}
