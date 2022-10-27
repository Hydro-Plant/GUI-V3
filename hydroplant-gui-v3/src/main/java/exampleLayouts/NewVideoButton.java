package exampleLayouts;

import gui.Layout;
import gui.constants;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import javafx2.Rectangle2;

public class NewVideoButton extends Layout {
	final double image_fit_factor = 0.8;

	Rectangle2 background;
	ImageView2 plus;

	Image plus_img;

	NewVideoButton() {
		background = new Rectangle2();
		plus = new ImageView2();
		plus_img = new Image("file:pics/plus.png");

		background.setFill(Color.GREY);
		background.setStroke(constants.outline_col);
		background.setPos(1);
		plus.setImage2(plus_img);
		plus.setPos(4);
		plus.setPreserveRatio2(true);

		addObject(background);
		addObject(plus);
	}

	public void setSize(int sizex, int sizey) {
		background.setWidth2(sizex);
		background.setHeight2(sizey);
		plus.setFitWidth2(sizex * image_fit_factor);
		plus.setFitHeight2(sizey * image_fit_factor);
		plus.setY2(sizey / 2);
	}
}
