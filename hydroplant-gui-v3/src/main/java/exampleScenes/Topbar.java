package exampleScenes;

import exampleLayouts.BackButton;
import exampleSceneObjects.BatteryStatus;
import gui.Layout;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx.scene.paint.Color;
import javafx2.Circle2;
import javafx2.Rectangle2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;

public class Topbar extends Scene {
	BatteryStatus bs;
	Button btn;
	FlatLayout fl;

	Layout fl_lt;
	BackButton bb;

	Rectangle2 backg_rec;

	Circle2 c2 = new Circle2();

	public Topbar() {
		super();
		bs = new BatteryStatus();
		btn = new Button();
		fl = new FlatLayout();
		bb = new BackButton();
		fl_lt = new Layout();
		backg_rec = new Rectangle2();
		backg_rec.setX2(0);
		backg_rec.setY2(0);
		backg_rec.setPos(0);
		backg_rec.setFill(Color.rgb(51, 153, 255));
		fl_lt.addObject(backg_rec);
		fl.setDesign(fl_lt);
		btn.setDesign(bb);

		btn.setButtonShape(bb.getCircle());

		addObject(fl);
		addObject(bs);
		addObject(btn);
	}

	@Override
	public void updateSize() {
		double outline_size = variables.height * constants.height_outline;
		bb.setStrokeWidth(outline_size);
		backg_rec.setStroke(Color.BLACK);
		backg_rec.setStrokeWidth(outline_size);
		backg_rec.setWidth2(variables.width);
		backg_rec.setHeight2((int) (variables.height * constants.height_perc));
		bb.setSize((int) (variables.height * constants.height_perc * constants.button_topbar_perc) / 2);
		btn.setPosition(
				variables.width - (int) (variables.height * constants.height_perc / 2)
						- (int) (variables.height * constants.button_topbar_perc_pos),
				(int) (variables.height * constants.height_perc / 2));
		bs.setSize(variables.height * constants.battery_s_size);
		bs.setPosition((int) (variables.height * constants.battery_s_pos),
				(int) (variables.height * constants.height_perc / 2));
	}

	public void setBat(int bat_perc) {
		bs.setBatLevel(bat_perc);
	}

	@Override
	public int mouseClick() {
		if (btn.isPressed()) {
			return 0;
		}
		return -1;
	}
}