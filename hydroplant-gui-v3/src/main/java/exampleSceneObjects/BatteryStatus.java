package exampleSceneObjects;

import javafx.scene.image.Image;
import javafx2.ImageView2;
import sceneObjects.FlatLayout;

public class BatteryStatus extends FlatLayout {
	Image[] batImages;
	ImageView2 imgView;
	int bat_level;

	public BatteryStatus() {
		super();
		batImages = new Image[5];
		imgView = new ImageView2();
		imgView.test_id = 1;
		imgView.setPreserveRatio2(true);
		imgView.setPos(4);
		imgView.setX2(0);
		imgView.setY2(0);
		this.design.addObject(imgView);
		for (int x = 0; x < 5; x++) {
			batImages[x] = new Image("file:pics/battery_icon_" + x + ".png");
		}
	}

	public void setSize(double d) {
		this.imgView.setFitWidth2(d * batImages[0].getWidth());
	}

	public void setBatLevel(int bat_level) {
		this.bat_level = bat_level;
		for (int x = 0; x < 5; x++) {
			if (bat_level <= (x + 1) * 20) {
				imgView.setImage2(batImages[x]);
				break;
			}
		}
	}
}
