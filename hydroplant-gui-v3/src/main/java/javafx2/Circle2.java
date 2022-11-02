package javafx2;

import javafx.scene.shape.Circle;
import standard.Positioning;

public class Circle2 extends Circle {
	private int positioning = 0;
	private double posx = 0;
	private double posy = 0;

	private void updateCir() {
		super.setCenterX(posx + Positioning.positioning(positioning, 4)[0] * 2 * super.getRadius());
		super.setCenterY(posy + Positioning.positioning(positioning, 4)[1] * 2 * super.getRadius());
	}

	public void setPos(int positioning) {
		this.positioning = positioning;
	}

	public void setX2(double x) {
		posx = x;
		updateCir();
	}

	public double getX2() {
		return posx;
	}

	public void setY2(double y) {
		posy = y;
		updateCir();
	}

	public double getY2() {
		return posy;
	}

	public void setRadius2(double radius) {
		super.setRadius(radius);
		updateCir();
	}
}
