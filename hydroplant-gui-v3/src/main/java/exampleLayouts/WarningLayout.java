package exampleLayouts;

import gui.Layout;
import gui.variables;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import javafx2.Text2;
import standard.Vector;
import standard.Alpha;
import standard.Bezier;

public class WarningLayout extends Layout {
	ImageView2 wrn_sign;
	Rectangle2 background;
	Text2 text;
	Image sign;

	double size = 1;
	double rec_x;
	double rec_y;

	final double arc_factor = 100;
	final double text_size_factor = 150;
	final double text_wrapping_factor = 0.9;
	final double text_rectangle_height_factor = 1.2;
	final double background_stroke_factor = 10;
	final double sign_pos = 100;

	boolean status = false;
	boolean text_status = false;
	double text_factor = 0;
	double text_speed = 0.5;
	boolean rec_status = false;
	double rec_factor = 0;
	double rec_speed = 0.5;

	double[] bezier_space = { 0.3, 0.3 };

	boolean moving = false;

	public WarningLayout() {
		wrn_sign = new ImageView2();
		background = new Rectangle2();
		text = new Text2();
		text.setFill(Color.WHITE);
		text.setStroke(Color.BLACK);
		text.setHorizontalOrientation(TextAlignment.CENTER);
		text.setTextOrigin(VPos.CENTER);
		sign = new Image("file:pics/warning.png");
		
		background.setPos(8);
		background.setFill(Color.YELLOW);
		background.setStroke(Color.ORANGE);
		wrn_sign.setImage2(sign);
		wrn_sign.setPreserveRatio2(true);
		wrn_sign.setPos(4);
		text.setStroke(Color.BLACK);
		text.setStroke(Alpha.changeAlpha((Color) text.getStroke(), 0));
		setSize(1);
		text.setAlpha(0);

		this.addObject(background);
		this.addObject(text);
		this.addObject(wrn_sign);
	}
	
	public void setOutline(double width) {
		//text.setStrokeWidth(width);
		text.setStrokeWidth(width);
		background.setStrokeWidth(width * background_stroke_factor);
	}
	
	public void setRectangle(double rec_x) {
		this.rec_x = rec_x;
		text.setWrappingWidth(rec_x * text_wrapping_factor);
		//System.out.println("Text Wrapping: " + (rec_y * text_wrapping_factor));
		updateShape();
	}

	public void setSize(double size) {
		wrn_sign.setFitHeight2(sign.getHeight() * size);
		background.setArcHeight(size * arc_factor);
		background.setArcWidth(size * arc_factor);
		text.setSize(size * text_size_factor);
		wrn_sign.setY2(-sign_pos * size);
		updateShape();
		//System.out.println("Text Size: " + (size * text_size_factor));
	}

	public void setText(String inp) {
		text.setText2(inp);
		updateShape();
	}

	public void setStatus(boolean open) {
		this.status = open;
		moving = true;
	}
	
	public boolean getStatus() {
		return this.status;
	}
	
	public void updateShape() {
		this.rec_y = text.getBoundsInLocal().getHeight() * text_rectangle_height_factor;
		text.setX2(- rec_x / 2);
		text.setY2(- rec_y / 2);
		background.setHeight2(rec_y);
		background.setWidth2(rec_x);
	}
	
	public void update() {
		if (moving) {
			if (this.status) {
				if (!rec_status) {
					rec_factor += rec_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(rec_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					background.setHeight2(rec_y * bez_factor);
					background.setWidth2(rec_x * bez_factor);
					if (rec_factor >= 1) {
						rec_factor = 1;
						rec_status = true;
					}
				} else if (!text_status) {
					text_factor += text_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(text_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					text.setAlpha(bez_factor);
					text.setStroke(Alpha.changeAlpha((Color) text.getStroke(), bez_factor));
					if (text_factor >= 1) {
						text_factor = 1;
						text_status = true;
					}
				} else
					moving = false;
			} else {
				if (text_status) {
					text_factor -= text_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(rec_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					text.setAlpha(bez_factor);
					text.setStroke(Alpha.changeAlpha((Color) text.getStroke(), bez_factor));
					if (text_factor <= 0) {
						text_factor = 0;
						text_status = false;
					}
				} else if (rec_status) {
					rec_factor -= rec_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(text_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					background.setHeight2(rec_y * bez_factor);
					background.setWidth2(rec_x * bez_factor);
					if (rec_factor <= 0) {
						rec_factor = 0;
						rec_status = false;
					}
				} else
					moving = false;
			}
		}
	}
}
