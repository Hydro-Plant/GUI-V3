package sceneObjects;

import gui.Layout;

public class FlatLayout extends SceneObject {
	protected Layout design;

	public FlatLayout() {
		this.positionx = 0;
		this.positiony = 0;
		this.design = new Layout();
		pane = this.design.getPane();
		this.design.setLayoutPosition(positionx, positiony);
	}

	public void setDesign(Layout design) {
		this.design = design;
		pane = this.design.getPane();
		this.design.setLayoutPosition(positionx, positiony);
	}

	protected void updatePosition() {
		if (design != null)
			design.setLayoutPosition(positionx, positiony);
	}

	public void update() {
		if (design != null)
			design.update();
	}
}