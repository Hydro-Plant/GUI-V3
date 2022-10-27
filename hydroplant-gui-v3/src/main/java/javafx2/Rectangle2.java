package javafx2;

import javafx.scene.shape.Rectangle;
import standard.Positioning;

public class Rectangle2 extends Rectangle {
	private int positioning = 0;
	private double posx = 0;
	private double posy = 0;
	
	public Rectangle2() {
		super.setMouseTransparent(true);
	}

	private void updateRec() {
		super.setX(posx + Positioning.positioning(positioning, 0)[0] * super.getWidth());
		super.setY(posy + Positioning.positioning(positioning, 0)[1] * super.getHeight());
	}

	public void setPos(int positioning) {
		this.positioning = positioning;
	}

	public int getPos() {
		return positioning;
	}

	public void setX2(double x) {
		posx = x;
		updateRec();
	}

	public void setY2(double y) {
		posy = y;
		updateRec();
	}

	public void setWidth2(double width) {
		super.setWidth(width);
		updateRec();
	}

	public void setHeight2(double height) {
		super.setHeight(height);
		updateRec();
	}

	public Rectangle2 copy() {
		Rectangle2 copy = new Rectangle2();
		copy.setX(this.getX());
		copy.setY(this.getY());
		copy.setWidth(this.getWidth());
		copy.setHeight(this.getHeight());
		copy.setArcHeight(this.getArcHeight());
		copy.setArcWidth(this.getArcWidth());
		return copy;
	}
}
