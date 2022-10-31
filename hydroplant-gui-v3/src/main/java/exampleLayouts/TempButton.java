package exampleLayouts;

import javafx.scene.paint.Color;

public class TempButton extends DashboardButton {
	Color c_optimal = Color.rgb(219, 255, 223);
	Color c_max = Color.rgb(255, 50, 50);
	Color c_min = Color.rgb(50, 50, 255);

	double v_optimal = 21;
	double v_tol = 2;
	double v_max = 25;
	double v_min = 17;

	double value;

	public TempButton() {
		super();
		this.value = 0;
		title.setText2("Temperatur");
		unit.setText2("°C");
	}

	public void setTemperature(double temp) {
		this.value = temp;
		information.setText2(String.format("%.1f", temp));
		if (temp <= v_optimal + v_tol && temp >= v_optimal - v_tol) {
			bg_rec.setFill(c_optimal);
		} else if (temp >= v_max) {
			bg_rec.setFill(c_max);
		} else if (temp <= v_min) {
			bg_rec.setFill(c_min);
		} else if (temp < v_max && temp > v_optimal + v_tol) {
			double temp_perc = ((temp - (v_optimal + v_tol))) / (v_max - (v_optimal + v_tol));
			bg_rec.setFill(
					Color.rgb((int) ((1 - temp_perc) * c_optimal.getRed() * 255 + temp_perc * c_max.getRed() * 255),
							(int) ((1 - temp_perc) * c_optimal.getGreen() * 255 + temp_perc * c_max.getGreen() * 255),
							(int) ((1 - temp_perc) * c_optimal.getBlue() * 255 + temp_perc * c_max.getBlue() * 255)));
		} else {
			double temp_perc = (temp - v_min) / ((v_optimal - v_tol) - v_min);
			bg_rec.setFill(
					Color.rgb((int) ((1 - temp_perc) * c_min.getRed() * 255 + temp_perc * c_optimal.getRed() * 255),
							(int) ((1 - temp_perc) * c_min.getGreen() * 255 + temp_perc * c_optimal.getGreen() * 255),
							(int) ((1 - temp_perc) * c_min.getBlue() * 255 + temp_perc * c_optimal.getBlue() * 255)));
		}
	}

	public void setTemperatures(double min, double opt, double max, double tol) {
		this.v_optimal = opt;
		this.v_min = min;
		this.v_max = max;
		this.v_tol = tol; // Toleranz der optimalen Wassertemperatur +-
	}
}
