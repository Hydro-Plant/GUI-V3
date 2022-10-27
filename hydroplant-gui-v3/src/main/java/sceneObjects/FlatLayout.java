package sceneObjects;

import gui.Layout;

public class FlatLayout extends SceneObject {
	protected Layout design;

	public FlatLayout() {
		design = new Layout();
	}

	public void setDesign(Layout design) {
		this.design = design;
	}

	@Override
	public void update() {
		if (design != null)
			design.update();
	}

	@Override
	public void toFront() {
		design.toFront();
	}
}