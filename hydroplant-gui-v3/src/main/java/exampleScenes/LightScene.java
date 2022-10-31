package exampleScenes;

import exampleLayouts.LightButton;

public class LightScene extends InfoScene {
	LightButton lb;

	public LightScene() {
		super(new LightButton());
		lb = (LightButton) super.bg_l;
	}

	public void setLightStatus(boolean status) {
		lb.setStatus(status);
	}

}
