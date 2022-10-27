package exampleLayouts;

import gui.Layout;
import gui.constants;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx2.Circle2;
import javafx2.ImageView2;

public class BackButton extends Layout {
	Circle2 bg_circle;
	ImageView2 arrow;
	Image arrow_img;

	boolean clicked;

	public BackButton() {
		super();
		clicked = false;
		int circle_size = 0;

		bg_circle = new Circle2();
		arrow = new ImageView2();

		bg_circle.setRadius(circle_size);
		bg_circle.setX2(0);
		bg_circle.setY2(0);
		bg_circle.setPos(4);
		bg_circle.setFill(Color.WHITE);
		bg_circle.setStroke(constants.outline_col);

		arrow_img = new Image("file:pics/backwards_arrow.png");
		//arrow_img = new Image("file:jesus.png");
		arrow.setPreserveRatio(true);
		arrow.setFitWidth(circle_size * constants.pic_circle_value_size * arrow_img.getWidth());
		arrow.setX2((int) (-circle_size * constants.pic_circle_value_pos));
		arrow.setX2(0);
		arrow.setY2(0);
		arrow.setPos(4);
		arrow.setImage2(arrow_img);

		addObject(bg_circle);
		addObject(arrow);
	}

	BackButton(int circle_size) {
		super();
		clicked = false;

		bg_circle = new Circle2();
		arrow = new ImageView2();

		bg_circle.setRadius(circle_size);
		bg_circle.setX2(0);
		bg_circle.setY2(0);
		bg_circle.setPos(4);
		bg_circle.setFill(Color.WHITE);
		bg_circle.setStroke(constants.outline_col);

		arrow_img = new Image("file:pics/backwards_arrow.png");
		//arrow_img = new Image("file:jesus.png");
		arrow.setPreserveRatio(true);
		arrow.setFitWidth(circle_size * constants.pic_circle_value_size * arrow_img.getWidth());
		arrow.setX2((int) (-circle_size * constants.pic_circle_value_pos));
		arrow.setX2(0);
		arrow.setY2(0);
		arrow.setPos(4);
		arrow.setImage2(arrow_img);

		addObject(bg_circle);
		addObject(arrow);
	}

	public void setStrokeWidth(double size) {
		bg_circle.setStrokeWidth(size);
	}

	public void setSize(int circle_size) {
		bg_circle.setRadius(circle_size);
		arrow.setFitWidth2(circle_size * constants.pic_circle_value_size * arrow_img.getWidth());
		arrow.setX2((int) (-circle_size * constants.pic_circle_value_pos));
		arrow.setY2(0);
		arrow.setPos(4);
	}
	
	public Circle2 getCircle() {
		return bg_circle;
	}
}
