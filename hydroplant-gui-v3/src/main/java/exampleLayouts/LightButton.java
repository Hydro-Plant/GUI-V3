package exampleLayouts;

import javafx.scene.paint.Color;

public class LightButton extends DashboardButton {
	Color light_on = Color.rgb(250, 255, 82);
	Color light_off = Color.rgb(49, 51, 70);

	double value;
	boolean status;

	public LightButton() {
		super();
		this.value = 0;
		setStatus(false);
		title.setText2("Beleuchtung");
		unit.setText2("St/T");
	}

	public void setValue(double value) {
		if (this.value != value) {
			this.value = value;
			information.setText2(String.format("%.1f", value));
		}
	}

	public void setStatus(boolean value) {
		if (this.status != value) {
			this.status = value;
			if (value) {
				bg_rec.setFill(light_on);
			} else {
				bg_rec.setFill(light_off);
			}
		}
	}
}