package exampleLayouts;

import gui.Layout;
import gui.constants;
import gui.variables;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import javafx2.Text2;
import standard.Alpha;
import standard.Bezier;
import standard.Vector;

public class WarningLayout extends Layout {
	ImageView2 wrn_sign;
	Rectangle2 background;
	Text2 text;
	Image sign;

	double size = 1;
	double rec_x;
	double rec_y;

	final double arc_factor = 100;
	final double text_size_factor = 200;
	final double text_edge_size = 0.05;
	final double background_stroke_factor = 10;
	final double sign_pos = 100;
	final double[] bezier_space = { 0.3, 0.3 };

	boolean status = false; // Should the warning be open
	boolean text_status = false; // Is the text open
	double text_factor = 0; // How far the text is shown
	double text_speed = 1; // Speed of the text showing
	boolean rec_status = false; // Is the rectangle open
	double rec_factor = 0; // How far the rectangle is open
	double rec_speed = 1; // Speed of the rectangle opening
	boolean alpha_status = false; // Should the warning be visible
	double alpha_factor = 0; // How visible the warning is
	double alpha_speed = 0.5; // How fast the warning will be visible

	boolean changing_rect = false; // Is the rectangle currently opening/closing
	boolean changing_text = false; // Is the text currently appearing/disappearing

	boolean moving = false; // Is the appearance of the warning changing (rectangle, text,
							// etc) (used to reduce processing power if nothing happens)
	boolean alpha_change = false; // Is alpha changing (I don't know what this does)

	public WarningLayout() {
		wrn_sign = new ImageView2();
		background = new Rectangle2();
		text = new Text2();
		text.setFill(Color.WHITE);
		text.setStroke(constants.outline_col);
		text.setHorizontalOrientation(TextAlignment.CENTER);
		text.setTextOrigin(VPos.CENTER);
		text.setStroke(Alpha.changeAlpha((Color) text.getStroke(), 0));
		text.setAlpha(0);
		sign = new Image("file:pics/warning.png");

		background.setPos(8);
		background.setFill(Color.YELLOW);
		background.setStroke(Color.ORANGE);
		wrn_sign.setImage2(sign);
		wrn_sign.setPreserveRatio2(true);
		wrn_sign.setPos(4);

		setSize(1);

		this.setAlpha(false);

		this.addObject(background);
		this.addObject(text);
		this.addObject(wrn_sign);
	}

	public void setOutline(double width) {
		// text.setStrokeWidth(width);
		text.setStrokeWidth(width);
		background.setStrokeWidth(width * background_stroke_factor);
	}

	public void setRectangle(double rec_x) {
		this.rec_x = rec_x;
		text.setWrappingWidth(rec_x - text_edge_size * variables.height);
		updateShape();
	}

	public void setSize(double size) {
		wrn_sign.setFitHeight2(sign.getHeight() * size);
		background.setArcHeight(size * arc_factor);
		background.setArcWidth(size * arc_factor);
		text.setSize(size * text_size_factor);
		wrn_sign.setY2(-sign_pos * size);
		updateShape();
	}

	public void setText(String inp) {
		text.setText2(inp);
		updateShape();
	}

	public void setStatus(boolean open) {
		this.status = open;
		moving = true;
		if (status && !rec_status)
			changing_rect = true;
		else if (!status && text_status)
			changing_text = true;
	}

	public boolean getStatus() {
		return this.status;
	}

	public int getRealStatus() {
		if (moving)
			return 2;
		else if (!text_status && !rec_status)
			return 0;
		else if (text_status && rec_status)
			return 1;
		return -1;
	}

	public void setAlpha(boolean alpha_status) {
		this.alpha_status = alpha_status;
		alpha_change = true;
	}

	public int getAlphaStatus() {
		if (alpha_factor < 1 && alpha_factor > 0)
			return 2;
		else
			return (int) alpha_factor;
	}

	public double getHeight() {
		updateShape();
		return rec_y;
	}

	public double[] getImageDim() {
		return new double[] { sign.getWidth(), sign.getHeight() };
	}

	public void updateShape() {
		this.rec_y = text.getBoundsInLocal().getHeight() + text_edge_size * variables.height;
		text.setX2(-rec_x / 2);
		text.setY2(-rec_y / 2);
		if (this.getRealStatus() == 1) {
			background.setHeight2(rec_y);
			background.setWidth2(rec_x);
		} else if (this.getRealStatus() == 0) {
			background.setHeight2(0);
			background.setWidth2(0);
		}
	}

	@Override
	public void update() {
		if (alpha_change) {
			if (alpha_status) {
				alpha_factor += alpha_speed / variables.frameRate;
				if (alpha_factor >= 1) {
					background.setFill(Alpha.changeAlpha((Color) background.getFill(), 1));
					background.setStroke(Alpha.changeAlpha((Color) background.getStroke(), 1));
					alpha_change = false;
					alpha_factor = 1;
				}
			} else {
				background.setFill(Alpha.changeAlpha((Color) background.getFill(), 0));
				background.setStroke(Alpha.changeAlpha((Color) background.getStroke(), 0));
				alpha_factor -= alpha_speed / variables.frameRate;
				if (alpha_factor <= 0) {
					alpha_change = false;
					alpha_factor = 0;
				}
			}

			double bez_factor = Bezier.bezier_curve_2d(alpha_factor, new Vector(bezier_space[0], 0),
					new Vector(1 - bezier_space[1], 1)).y;
			wrn_sign.setOpacity(bez_factor);
		}

		if (moving) {
			if (this.status) {
				if (changing_rect) {
					rec_factor += rec_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(rec_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					background.setHeight2(rec_y * bez_factor);
					background.setWidth2(rec_x * bez_factor);
					if (rec_factor >= 1) {
						rec_factor = 1;
						rec_status = true;
						changing_rect = false;
						changing_text = true;
					}
				} else if (changing_text) {
					text_factor += text_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(text_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					text.setAlpha(bez_factor);
					text.setStroke(Alpha.changeAlpha((Color) text.getStroke(), bez_factor));
					if (text_factor >= 1) {
						text_factor = 1;
						text_status = true;
						changing_text = false;
					}
				} else
					moving = false;
			} else {
				if (changing_text) {
					text_factor -= text_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(text_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					text.setAlpha(bez_factor);
					text.setStroke(Alpha.changeAlpha((Color) text.getStroke(), bez_factor));
					if (text_factor <= 0) {
						text_factor = 0;
						text_status = false;
						changing_rect = true;
						changing_text = false;
					}
				} else if (changing_rect) {
					rec_factor -= rec_speed / variables.frameRate;
					double bez_factor = Bezier.bezier_curve_2d(rec_factor, new Vector(bezier_space[0], 0),
							new Vector(1 - bezier_space[1], 1)).y;
					background.setHeight2(rec_y * bez_factor);
					background.setWidth2(rec_x * bez_factor);
					if (rec_factor <= 0) {
						rec_factor = 0;
						rec_status = false;
						changing_rect = false;
					}
				} else
					moving = false;
			}
		}
	}
}
