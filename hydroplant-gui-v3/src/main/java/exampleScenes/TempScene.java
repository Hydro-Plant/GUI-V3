package exampleScenes;

import exampleLayouts.TempButton;

public class TempScene extends InfoScene {
	TempButton tb;

	public TempScene() {
		super(new TempButton());
		tb = (TempButton) super.bg_l;
	}

	public void setTemp(double temp) {
		tb.setTemperature(temp);
	}

	public void calibrateTemp(double v_min, double v_optimal, double v_max, double v_tol) {
		tb.setTemperatures(v_min, v_optimal, v_max, v_tol);
	}
}