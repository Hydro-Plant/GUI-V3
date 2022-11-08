package exampleScenes;

import exampleLayouts.StartButtonLayout;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx.scene.image.Image;
import sceneObjects.Button;
import standard.Bezier;
import standard.Vector;

public class StartScene extends Scene {
	final double gap_factor = 0.05;

	final double scene_speed = 2;
	final double scene_bez_factor = 0.3;
	double scene_factor = 1;
	boolean scene_active = true;
	boolean scene_change = false;

	Button dashboard;
	StartButtonLayout dashboard_l;
	Button timelapse;
	StartButtonLayout timelapse_l;

	int new_scene = -1; // dashboard = 0, timelapse = 1
	boolean active = true;

	public StartScene() {
		dashboard_l = new StartButtonLayout();
		dashboard_l.setPos(0);
		dashboard_l.setImage(new Image("file:pics/dashboard.png"));
		dashboard = new Button();
		dashboard.setPos(0);
		dashboard.setDesign(dashboard_l);

		timelapse_l = new StartButtonLayout();
		timelapse_l.setPos(2);
		timelapse_l.setImage(new Image("file:pics/video.png"));
		timelapse = new Button();
		timelapse.setPos(2);
		timelapse.setDesign(timelapse_l);

		addObject(dashboard);
		addObject(timelapse);
	}

	@Override
	public void updateSize() {
		dashboard.setPosition(variables.height * (1 - constants.height_perc) * gap_factor,
				variables.height * constants.height_perc + variables.height * (1 - constants.height_perc) * gap_factor);
		dashboard.setShape((variables.width - 3 * variables.height * (1 - constants.height_perc) * gap_factor) / 2,
				variables.height * (1 - constants.height_perc) * (1 - 2 * gap_factor));
		dashboard_l.setShape((variables.width - 3 * variables.height * (1 - constants.height_perc) * gap_factor) / 2,
				variables.height * (1 - constants.height_perc) * (1 - 2 * gap_factor));
		dashboard_l.setOutline(variables.height * constants.height_outline);

		timelapse.setPosition(variables.width - variables.height * (1 - constants.height_perc) * gap_factor,
				variables.height * constants.height_perc + variables.height * (1 - constants.height_perc) * gap_factor);
		timelapse.setShape((variables.width - 3 * variables.height * (1 - constants.height_perc) * gap_factor) / 2,
				variables.height * (1 - constants.height_perc) * (1 - 2 * gap_factor));
		timelapse_l.setShape((variables.width - 3 * variables.height * (1 - constants.height_perc) * gap_factor) / 2,
				variables.height * (1 - constants.height_perc) * (1 - 2 * gap_factor));
		timelapse_l.setOutline(variables.height * constants.height_outline);
	}

	@Override
	public void loadMode(int mode) {
		switch (mode) {
		case 0:
			scene_factor = 0;
			scene_active = true;
			scene_change = true;
			break;
		}
	}

	@Override
	public int mouseClick(double mousex, double mousey) {
		if (dashboard.isPressed(mousex, mousey)) {
			new_scene = 0;
			scene_active = false;
			scene_change = true;
		} else if (timelapse.isPressed(mousex, mousey)) {
			new_scene = 1;
			scene_active = false;
			scene_change = true;
		}
		return -1;
	}

	@Override
	public void update() {
		super.update();

		if (scene_change) {
			if (scene_active) {
				scene_factor += scene_speed / variables.frameRate;
				if (scene_factor >= 1) {
					scene_factor = 1;
					scene_change = false;
				}
			} else {
				scene_factor -= scene_speed / variables.frameRate;
				if (scene_factor <= 0) {
					scene_factor = 0;
					scene_change = false;
					this.scene_event = new_scene;
				}
			}
			double scene_bez = Bezier.bezier_curve_2d(scene_factor, new Vector(scene_bez_factor, 0),
					new Vector(1 - scene_bez_factor, 1)).y;
			this.root.setOpacity(scene_bez);
		}
	}
}
