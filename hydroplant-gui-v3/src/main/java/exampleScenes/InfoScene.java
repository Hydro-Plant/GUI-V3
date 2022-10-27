package exampleScenes;

import exampleLayouts.DashboardButton;
import gui.Scene;
import gui.constants;
import gui.variables;
import sceneObjects.FlatLayout;

public class InfoScene extends Scene {
	DashboardButton bg_l;
	FlatLayout bg_fl;

	InfoScene(DashboardButton background) {
		bg_fl = new FlatLayout();
		bg_l = background;
		bg_l.setTextAlpha(0);

		bg_fl.setDesign(bg_l);
		addObject(bg_fl);
	}

	@Override
	public void externalButton(int button) {
		switch (button) {
		case 0:
			scene_event = 0;
			break;
		}
	}

	@Override
	public void updateSize() {
		int btn_width = (variables.width - (int) (variables.height * (1 - constants.height_perc)
				* (2 * constants.edge_distance + 2 * constants.button_distance))) / 3;
		int btn_height = (int) (variables.height * (1 - constants.height_perc)
				* (1 - 2 * constants.edge_distance - 1 * constants.button_distance)) / 2;

		int full_width = variables.width
				- (int) (variables.height * (1 - constants.height_perc) * (2 * constants.edge_distance));
		int full_height = (int) (variables.height * (1 - constants.height_perc) * (1 - 2 * constants.edge_distance));

		bg_fl.setPosition((int) (variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height
						* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)));

		bg_l.setVirtualShape(btn_width, btn_height, 0);
		bg_l.setShape(full_width, full_height);
		bg_l.setStrokeWidth(constants.height_outline * variables.height);
	}
}
