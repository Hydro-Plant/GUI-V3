package exampleLayouts;

import java.util.ArrayList;

import gui.constants;
import gui.variables;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import poissonDisc.BoundingBox;
import poissonDisc.PoissonDisc;
import poissonDisc.ReturnPoint;
import standard.Positioning;
import standard.Vector;

public class FlowButton extends DashboardButton {
	ImageView2 bubbles;
	WritableImage bubbles_img;

	ArrayList<Image> bubble_images;
	double[] size_perc = { 0.0909f, 0.1818f, 0.2727f, 0.3636f, 0.4545f, 0.5454f, 0.6363f, 0.7272f, 0.8181f, 0.9090f,
			1f };

	BoundingBox bb;
	PoissonDisc pd;

	Color bg = Color.rgb(100, 100, 255);
	double value = 0;
	double size_x = 0;
	double size_y = 0;
	double pic_size;

	MotionBlur mb = new MotionBlur();

	public FlowButton() {
		super();
		this.value = 0;
		reloadImages();

		bb = new BoundingBox(500, 500, 0, 500, 500);
		pd = new PoissonDisc((int) variables.width + (int) Math.floor(4 * constants.r * variables.height),
				(int) variables.height + (int) Math.floor(4 * constants.r * variables.height),
				constants.r * (int) variables.height);
		pd.setBoundingBox(bb);

		bubbles = new ImageView2();

		bubbles.setX2(0);
		bubbles.setY2(0);
		bubbles.setPos(positioning);
		mb.setAngle(0);
		bubbles.setEffect(mb);
		bg_rec.setFill(Color.rgb(77, 193, 255));

		title.setText2("Durchfluss");
		unit.setText2("l/s");

		additional_background.getChildren().add(bubbles);
	}

	public void setValue(double value) {
		this.value = value;
		information.setText2(String.format("%.1f", value));
	}

	void reloadImages() {
		double max_pic = 0;
		bubble_images = new ArrayList<>();
		for (int x = 0; x < size_perc.length; x++) {
			Image im = new Image("file:pics/bubble" + x + ".png");
			if (im.getWidth() > max_pic)
				max_pic = im.getWidth();
		}

		pic_size = constants.bubble_size_max * variables.height / max_pic;

		for (int x = 0; x < size_perc.length; x++) {
			Image im = new Image("file:pics/bubble" + x + ".png");
			bubble_images.add(new Image("file:pics/bubble" + x + ".png", pic_size * im.getWidth(), 0, true, true));
		}
	}

	public void setShape(double obj_width, double obj_height) {
		size_x = obj_width;
		size_y = obj_height;
		bb.setSize((int) obj_width + (int) Math.ceil(variables.height * constants.bubble_size_max),
				(int) obj_height - (int) Math.ceil(variables.height * (2 * constants.bubble_edge_dist)));
		super.setShape(obj_width, obj_height);
	}

	public void setVirtualShape(double obj_width, double obj_height, int positioning) {
		bubbles.setPos(positioning);
		if (this.virt_sizey != obj_height || this.virt_sizex != obj_width) {
			pd = new PoissonDisc((int) variables.width + (int) Math.floor(4 * constants.r * variables.height),
					(int) variables.height + (int) Math.floor(4 * constants.r * variables.height),
					constants.r * (int) variables.height);
			pd.setBoundingBox(bb);
			pd.setFirst();
			reloadImages();
		}

		super.setVirtualShape(obj_width, obj_height, positioning);
	}

	public void update() {
		double time_passed = 1 / variables.frameRate;
		double[] pos_shift = Positioning.positioning(positioning, 4);

		bb.setPosition(
				(int) positionx + (int) Math.floor(pos_shift[0] * size_x)
						+ (int) Math.floor(2 * constants.r * variables.height),
				(int) positiony + (int) Math.floor(pos_shift[1] * size_y)
						+ (int) Math.floor(2 * constants.r * variables.height),
				4);
		if (pd.getPoints().size() == 0) {
			pd.setFirst();
			pd.calcMissing();
		}
		pd.updateGrid(new Vector(constants.speed_factor * this.value * time_passed, 0));
		pd.calcMissing();

		bubbles_img = new WritableImage((int) size_x, (int) size_y);

		double[] shift = Positioning.positioning(this.positioning, 0);
		double[] ges_shift = { positionx + shift[0] * size_x, positiony + shift[1] * size_y };
		PixelWriter bubbles_pw = bubbles_img.getPixelWriter();
		ArrayList<ReturnPoint> points = pd.getPoints();

		for (ReturnPoint point : points) {
			switch (point.getMode()) {
			case 0:
				for (int y = 0; y < size_perc.length; y++) {
					if (point.getRand() <= size_perc[y]) {
						Vector b_pos = new Vector(
								point.getPos().x - Math.floor(2 * constants.r * variables.height) - ges_shift[0],
								point.getPos().y - Math.floor(2 * constants.r * variables.height) - ges_shift[1]);

						// - Math.floor(2 * constants.r * variables.height) --> Disk grid is slightly
						// bigger than screen by 4 * constants.r * variables.heihgt

						if ((b_pos.y - bubble_images.get(y).getWidth() / 2 >= constants.bubble_edge_dist
								* variables.height
								&& b_pos.y + bubble_images.get(y).getWidth() / 2 <= bubbles_img.getHeight()
										- constants.bubble_edge_dist * variables.height)) {
							Image bubble = bubble_images.get(y);
							PixelReader pr = bubble.getPixelReader();
							int local_x = (int) (b_pos.x - (bubble.getWidth() / 2));
							int local_y = (int) (b_pos.y - (bubble.getHeight() / 2));

							if (local_x > -bubble.getWidth() && local_x < size_x) {
								int local_x_with_boundry = local_x;
								int local_width_with_boundry = (int) Math.floor(bubble.getWidth());
								int local_image_start = 0;
								if (local_x < 0) {
									local_x_with_boundry = 0;
									local_width_with_boundry += local_x;
									local_image_start = Math.abs(local_x);
								} else if (local_x > size_x - bubble.getWidth()) {
									local_width_with_boundry = ((int) size_x - local_x);
								}

								bubbles_pw.setPixels(local_x_with_boundry, local_y, local_width_with_boundry,
										(int) Math.floor(bubble.getHeight()), pr, local_image_start, 0);
							}
						}
						break;
					}
				}

				// Non-active
				break;
			case 1:
				// Active
				break;
			case 2:
				// Half-active
				break;
			}
		}
		mb.setRadius(constants.motion_blur_factor * this.value * time_passed);
		bubbles.setImage2(bubbles_img);
	}
}