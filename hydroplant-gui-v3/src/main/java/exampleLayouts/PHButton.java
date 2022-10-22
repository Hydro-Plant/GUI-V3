package exampleLayouts;

import javafx.scene.paint.Color;

public class PHButton extends DashboardButton {
	Color[] colors = { Color.rgb(192, 0, 0), Color.rgb(255, 43, 0), Color.rgb(255, 85, 0), Color.rgb(255, 128, 0),
			Color.rgb(255, 213, 0), Color.rgb(255, 255, 0), Color.rgb(213, 255, 0), Color.rgb(85, 255, 85),
			Color.rgb(0, 213, 128), Color.rgb(0, 170, 213), Color.rgb(0, 128, 255), Color.rgb(0, 85, 254),
			Color.rgb(0, 0, 254), Color.rgb(0, 0, 169) };

	double value;

	public PHButton() {
		super();
		this.value = 0;
		title.setText2("PH - Wert");
	}

	public void setValue(double value) {
		if (this.value != value) {
			this.value = value;
			information.setText2(String.format("%.1f", value));
			if (value <= 1) {
				bg_rec.setFill(colors[0]);
			} else if (value >= 14) {
				bg_rec.setFill(colors[13]);
			} else {
				for (int x = 0; x < 13; x++) {
					if (value >= x + 1 && value <= x + 2) {
						bg_rec.setFill(Color.rgb(
								(int) (colors[x].getRed() * 255 * (2 + x - value)
										+ colors[x + 1].getRed() * 255 * (value - x - 1)),
								(int) (colors[x].getGreen() * 255 * (2 + x - value)
										+ colors[x + 1].getGreen() * 255 * (value - x - 1)),
								(int) (colors[x].getBlue() * 255 * (2 + x - value)
										+ colors[x + 1].getBlue() * 255 * (value - x - 1))));
					}
				}
			}
		}
	}
}