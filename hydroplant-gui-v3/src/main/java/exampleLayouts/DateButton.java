package exampleLayouts;

import gui.Layout;
import gui.variables;
import javafx.geometry.VPos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.Rectangle2;
import javafx2.Text2;
import standard.Bezier;
import standard.Map;
import standard.Positioning;
import standard.Vector;

public class DateButton extends Layout {
	private final double text_height_factor = 0.7;
	private final double blur_height_factor = 0.05;
	private final double gray_factor = 1;
	private final double speed_factor = 2;
	private final double bezier_factor = 0.3;

	Rectangle2 backg;
	Text2 num;

	GaussianBlur blur;

	boolean active = false;
	boolean moving = true;

	double factor = 0;
	double sizex, sizey;

	public DateButton(int num) {
		blur = new GaussianBlur();

		backg = new Rectangle2();
		backg.setStroke(Color.gray(gray_factor));
		backg.setFill(Color.TRANSPARENT);
		backg.setEffect(blur);
		this.num = new Text2();
		this.num.setFill(Color.WHITE);
		this.num.setText2(Integer.toString(num));
		this.num.setTextOrigin(VPos.CENTER);
		this.num.setHorizontalOrientation(TextAlignment.CENTER);

		addObject(backg);
		addObject(this.num);
	}

	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		updateSize();
	}

	public void setActive(boolean active) {
		this.active = active;
		this.moving = true;
	}

	public void setVisible(boolean visible) {
		backg.setVisible(visible);
		num.setVisible(visible);
	}

	@Override
	public void setPos(int pos) {
		this.positioning = pos;
		updateSize();
	}

	public void setOutline(double outline) {
		backg.setStrokeWidth(outline);
	}

	private void updateSize() {
		backg.setWidth2(sizex);
		backg.setHeight2(sizey);
		backg.setPos(positioning);

		backg.setArcHeight(Math.min(sizex, sizey) / 2);
		backg.setArcWidth(Math.min(sizex, sizey) / 2);

		num.setX2(sizex * Positioning.positioning(positioning, 4)[0]);
		num.setY2(sizey * Positioning.positioning(positioning, 4)[1]);
		num.setSize(text_height_factor * sizey);

		blur.setRadius(blur_height_factor * sizey);
	}

	@Override
	public void update() {
		if(moving) {
			if(active) {
				factor += speed_factor / variables.frameRate;
				if(factor >= 1) {
					factor = 1;
					moving = false;
				}
			}else {
				factor -= speed_factor / variables.frameRate;
				if(factor <= 0) {
					factor = 0;
					moving = false;
				}
			}
			double bez_factor = Bezier.bezier_curve_2d(factor, new Vector(bezier_factor, 0), new Vector(1 - bezier_factor, 1)).y;
			backg.setStroke(Map.map(bez_factor, Color.gray(gray_factor), Color.DARKBLUE));
		}
	}
}
