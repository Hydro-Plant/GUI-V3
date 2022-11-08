package exampleLayouts;

import gui.Layout;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.Rectangle2;
import javafx2.Text2;
import standard.Positioning;

public class TextButtonLayout extends Layout {
	private final double text_factor = 0.8;

	Rectangle2 backg;
	Text2 text;

	double sizex, sizey;
	double size = 1;
	double test_up_factor = 0;

	public TextButtonLayout(String text_string) {
		backg = new Rectangle2();
		backg.setFill(Color.rgb(28, 28, 31));

		text = new Text2();
		text.setFill(Color.WHITE);
		text.setHorizontalOrientation(TextAlignment.CENTER);
		text.setTextOrigin(VPos.CENTER);
		text.setText2(text_string);

		addObject(backg);
		addObject(text);
	}

	public void setTextUp(double up) {
		this.test_up_factor = up;
		updateShape();
	}

	public void setSize(double size) {
		this.size = size;
		updateShape();
	}

	public void setBackg(Color back_col) {
		backg.setFill(back_col);
	}

	public void setOutline(double outline_width) {
		backg.setStroke(Color.WHITE);
		backg.setStrokeWidth(outline_width);
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

		text.setX2(Positioning.positioning(positioning, 4)[0] * sizex);
		text.setY2(Positioning.positioning(positioning, 4)[1] * sizey - sizey * text_factor * size * test_up_factor);
		text.setSize(sizey * text_factor * size);
	}
}
