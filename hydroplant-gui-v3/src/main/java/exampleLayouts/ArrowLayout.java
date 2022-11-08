package exampleLayouts;

import gui.Layout;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import standard.Positioning;

public class ArrowLayout extends Layout {
	private final double image_factor = 0.7;

	Rectangle2 backg;
	ImageView2 arrow;

	double sizex, sizey;

	public ArrowLayout() {
		backg = new Rectangle2();
		arrow = new ImageView2();

		backg.setStroke(Color.WHITE);
		backg.setFill(Color.TRANSPARENT);

		ColorAdjust ca = new ColorAdjust();
		ca.setBrightness(1);
		arrow.setEffect(ca);
		arrow.setImage2(new Image("file:pics/backwards_arrow.png"));
		arrow.setPos(4);
		arrow.setPreserveRatio2(true);

		addObject(backg);
		addObject(arrow);
	}

	public void setOutline(double outline) {
		backg.setStrokeWidth(outline);
	}

	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		updateShape();
	}

	@Override
	public void setPos(int pos) {
		super.setPos(pos);
		updateShape();
	}

	public void updateShape() {
		backg.setPos(positioning);
		backg.setWidth2(sizex);
		backg.setHeight2(sizey);

		arrow.setX2(Positioning.positioning(positioning, 4)[0] * sizex);
		arrow.setY2(Positioning.positioning(positioning, 4)[1] * sizey);
		arrow.setFitWidth2(Math.min(sizex, sizey) * image_factor);
		arrow.setFitHeight2(Math.min(sizex, sizey) * image_factor);
	}

	public void setDirection(int direction) { // 0 = Up
		arrow.setRotate(90 + 90 * direction);
	}
}
