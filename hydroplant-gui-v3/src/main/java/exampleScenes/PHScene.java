package exampleScenes;

import exampleLayouts.PHButton;

public class PHScene extends InfoScene {
	PHButton phb;

	PHScene() {
		super(new PHButton());
		phb = (PHButton) super.bg_l;
	}

}
