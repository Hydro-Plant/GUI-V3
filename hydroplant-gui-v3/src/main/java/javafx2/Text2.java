package javafx2;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import standard.Alpha;
import standard.Positioning;

public class Text2 extends Text {
	private double posx = 0;
	private TextAlignment tal = TextAlignment.LEFT;

	public Text2() {
		super();
		setFont(Font.font("Semi-Coder", 100));
	}

	public Text2(String inp) {
		super(inp);
		setFont(Font.font("Semi-Coder", 100));
	}

	private void updateText() {
		int positioning = 0;
		switch (tal) {
		case CENTER:
			positioning = 1;
			break;
		case LEFT:
			positioning = 0;
			break;
		case RIGHT:
			positioning = 2;
			break;
		default:
			break;
		}
		super.setX(posx + Positioning.positioning(positioning, 0)[0] * super.getBoundsInLocal().getWidth());
	}

	public void setSize(double size) {
		setFont(Font.font("Semi-Coder", size));
		updateText();
	}

	public void setX2(double x) {
		posx = x;
		updateText();
	}

	public double getX2() {
		return posx;
	}

	public void setY2(double y) {
		super.setY(y);
	}

	public double getY2() {
		return super.getY();
	}

	public double getWidth() {
		return super.getBoundsInLocal().getWidth();
	}

	public void setHorizontalOrientation(TextAlignment tl) {
		tal = tl;
		updateText();
	}

	public void setText2(String text) {
		super.setText(text);
		updateText();
	}

	public void setAlpha(double alpha) {
		this.setFill(Alpha.changeAlpha((Color) getFill(), alpha));
	}
}
