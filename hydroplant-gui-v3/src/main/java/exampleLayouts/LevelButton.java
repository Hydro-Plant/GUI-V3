package exampleLayouts;

import gui.variables;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import standard.Positioning;

public class LevelButton extends DashboardButton {
	private final double wave_height = 0.03;
	private final double wave_width = 0.16;
	private final double speed = -1;

	private final double blur_rad = 0.005;

	double level;
	double max_level = 10f;
	double beta = 0;

	Rectangle2 wave_full_water;
	ImageView2 wave;
	WritableImage wave_img;

	int size_x, size_y;

	Color backg = Color.rgb(232, 232, 232);

	GaussianBlur blur = new GaussianBlur();
	private int wave_bottom;

	public LevelButton() {
		super();
		this.level = 0;
		wave_full_water = new Rectangle2();
		wave_full_water.setX2(0);
		wave_full_water.setY2(0);
		wave_full_water.setPos(6);
		wave_full_water.setFill(Color.AQUA);

		wave = new ImageView2();
		wave.setX2(0);
		wave.setY2(0);
		wave.setPos(0);

		title.setText2("Füllstand");
		unit.setText2("l");
		bg_rec.setFill(backg);

		additional_background.setEffect(blur);
		additional_background.getChildren().add(wave_full_water);
		additional_background.getChildren().add(wave);
	}

	public void setLevel(double level) {
		information.setText2(String.format("%.1f", level));
		this.level = level;
		updateWave();
	}

	public void setShape(int obj_width, int obj_height) {
		size_x = obj_width;
		size_y = obj_height;
		wave_full_water.setX2(Positioning.positioning(positioning, 6)[0] * obj_width);
		wave.setX2(Positioning.positioning(positioning, 6)[0] * obj_width);
		wave_full_water.setY2(Positioning.positioning(positioning, 6)[1] * obj_height);
		wave_full_water.setWidth2(obj_width);
		updateWave();
		super.setShape(obj_width, obj_height);
	}

	public void setVirtualShape(int obj_width, int obj_height, int positioning) {
		blur.setRadius(blur_rad * variables.height);
		super.setVirtualShape(obj_width, obj_height, positioning);
	}

	private void updateWave() {
		wave_bottom = (int) Math.floor((size_y + wave_height * variables.height) * (max_level - level) / max_level
				- wave_height * variables.height / 2) + (int) (wave_height * variables.height / 2);
		wave_full_water.setHeight2(size_y - wave_bottom);
		wave.setY2(wave_bottom - size_y - (wave_height * variables.height)
				+ Positioning.positioning(positioning, 6)[1] * size_y);
	}

	public void update() {
		beta = (beta + 1 / variables.frameRate * speed * (2 * Math.PI)) % (2 * Math.PI);

		wave_img = new WritableImage(size_x, (int) (wave_height * variables.height));
		PixelWriter wave_pw = wave_img.getPixelWriter();
		// boolean all_painted = false;
		for (int x = 0; x < size_x; x++) {
			for (int y = 0; y < (int) (wave_height * variables.height); y++) {
				if (y >= (int) (wave_height * variables.height / 2)
						+ Math.sin(beta + x * (2 * Math.PI) / (wave_width * variables.height)) * wave_height
								* variables.height / 2) {
					wave_pw.setColor(x, y, Color.AQUA);
				}
			}
		}

		wave.setImage2(wave_img);
	}
}
