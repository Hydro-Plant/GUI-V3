package sceneObjects;

import gui.Layout;
import standard.Positioning;

public class Button extends SceneObject {
	double button_height, button_width;
	public Layout design;

	public Button() {
		this.button_height = 0;
		this.button_width = 0;
		this.positionx = 0;
		this.positiony = 0;
		this.positioning = 0;
	}

	public void setShape(double width, double height) {
		this.button_height = height;
		this.button_width = width;
	}

	public Layout getDesign() {
		return design;
	}

	public void setDesign(Layout design) {
		this.design = design;
		pane = this.design.getPane();
		this.design.setLayoutPosition(positionx, positiony);
	}

	public boolean isPressed(double mousex, double mousey) {
		double[] sh = Positioning.positioning(positioning, 0);
		int[] shift = { (int) Math.floor(button_width * sh[0]), (int) Math.floor(button_height * sh[1]) };
		if (mousex >= positionx + shift[0] && mousex <= positionx + shift[0] + button_width
				&& mousey >= positiony + shift[1] && mousey <= positiony + shift[1] + button_height) {
			return true;
		}
		return false;
	}

	@Override
	protected void updatePosition() {
		if (design != null)
			design.setLayoutPosition(positionx, positiony);
	}

	@Override
	public void update() {
		if (design != null)
			design.update();
	}
}