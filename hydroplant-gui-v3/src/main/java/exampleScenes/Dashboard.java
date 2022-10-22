package exampleScenes;

import exampleLayouts.DashboardButton;
import exampleLayouts.FlowButton;
import exampleLayouts.LevelButton;
import exampleLayouts.LightButton;
import exampleLayouts.PHButton;
import exampleLayouts.TempButton;
import exampleSceneObjects.Warning;
import gui.Layout;
import gui.Scene;
import gui.constants;
import gui.variables;
import sceneObjects.Button;
import standard.Bezier;
import standard.Deckel;
import standard.Vector;
import standard.Map;

public class Dashboard extends Scene {
	Button temp_btn; // Temperatur Button
	TempButton temp_btn_layout;

	Button light_btn; // Light Button
	LightButton light_btn_layout;

	Button ph_btn; // PH Button
	PHButton ph_btn_layout;

	Button ec_btn; // EC Button
	PHButton ec_btn_layout;

	Button level_btn; // Level Button
	LevelButton level_btn_layout;

	Button flow_btn; // Flow Button
	FlowButton flow_btn_layout;
	
	Warning warning;
	
	boolean full_sized = false;
	boolean selecting = true;
	int selection = 0; // 0: Temp, 1: Light, 2: PH, 3: EC, 4: Flow, 5: Level
	double factor = 0;

	int btn_width;
	int btn_height;
	int full_width;
	int full_height;

	Button[] buttons;
	DashboardButton[] button_layouts;

	public Dashboard() {
		// Initialisierung der Objekte
		temp_btn = new Button();
		temp_btn_layout = new TempButton();

		light_btn = new Button();
		light_btn_layout = new LightButton();

		ph_btn = new Button();
		ph_btn_layout = new PHButton();

		ec_btn = new Button();
		ec_btn_layout = new PHButton();

		level_btn = new Button();
		level_btn_layout = new LevelButton();

		flow_btn = new Button();
		flow_btn_layout = new FlowButton();
		
		warning = new Warning();
		warning.position(1000, 700);
		
		// Objektparameter werden gesetzt
		temp_btn_layout.setTemperatures(17, 21, 25, 1);
		temp_btn_layout.setTemperature(21);

		temp_btn.setDesign(temp_btn_layout);
		light_btn.setDesign(light_btn_layout);
		ph_btn.setDesign(ph_btn_layout);
		ec_btn.setDesign(ec_btn_layout);
		flow_btn.setDesign(flow_btn_layout);
		level_btn.setDesign(level_btn_layout);

		buttons = new Button[] { temp_btn, light_btn, ph_btn, ec_btn, flow_btn, level_btn };
		button_layouts = new DashboardButton[] { temp_btn_layout, light_btn_layout, ph_btn_layout, ec_btn_layout,
				flow_btn_layout, level_btn_layout };

		addObject(temp_btn);
		addObject(light_btn);
		addObject(ph_btn);
		addObject(ec_btn);
		addObject(level_btn);
		addObject(flow_btn);
		addObject(warning);
		
		warning.setSize(0.3);
		warning.setRectangle(800);
		warning.setStatus(true);
		warning.setText("Großer Lückenfüller zur Vorschau. So groß, man kann ihn kaum lejiladshjk wafhjkl dfhil shkl ehjgyvhukgafshuk.dhjkl.sahkl..ho fah.ksen. Sehr professionell was hier steht.");
		warning.setOutline(constants.height_outline * variables.height);
		updateShape();
	}

	public void loadMode(int mode) {
		full_sized = false;
		selection = mode;
		factor = 1;
		selecting = true;

		buttons[mode].design.toFront();
	}

	void updateShape() {
		if (full_sized) {
			if (factor != 1)
				factor += 1 / variables.frameRate * constants.db_speed;
			if (factor > 1) {
				factor = 1;
				scene_event = selection;
			}
		} else {
			if (factor != 0)
				factor -= 1 / variables.frameRate * constants.db_speed;
			if (factor < 0)
				factor = 0;
		}

		for (int x = 0; x < 6; x++) {
			if (selection == x) {
				button_layouts[x]
						.setShape(
								(int) Math
										.floor(Map.map(
												Bezier.bezier_curve_2d(factor, new Vector(constants.db_curve, 0),
														new Vector(1 - constants.db_curve, 1)).y,
												0, 1, btn_width, full_width)),
								(int) Math.floor(Map.map(
										Bezier.bezier_curve_2d(factor, new Vector(constants.db_curve, 0),
												new Vector(1 - constants.db_curve, 1)).y,
										0, 1, btn_height, full_height)));
				button_layouts[x].setTextAlpha(Deckel.deckel(1 - 3 * factor, 0, 1));
			} else {
				button_layouts[x].setShape(btn_width, btn_height);
				button_layouts[x].setTextAlpha(1);
			}
		}
	}

	public void calibrateTempBtn(double v_min, double v_optimal, double v_max, double v_tol) {
		temp_btn_layout.setTemperatures(v_min, v_optimal, v_max, v_tol);
	}

	public void setTemp(double temp) {
		temp_btn_layout.setTemperature(temp);
	}

	public void setLightStatus(boolean status) {
		light_btn_layout.setStatus(status);
	}

	public void setLightValue(double value) {
		light_btn_layout.setValue(value);
	}

	public void setPHValue(double value) {
		ph_btn_layout.setValue(value);
	}

	public void setLevel(double value) {
		level_btn_layout.setLevel(value);
	}

	public void setFlowValue(double value) {
		flow_btn_layout.setValue(value);
	}

	public int mouseClick(double mousex, double mousey) { // 0: Temp, 1: Light, 2: PH, 3: EC, 4: Flow, 5: Level
		int res = -1;
		/*
		if (selecting) {
			full_sized = false;
			selecting = true;
			for (int x = 0; x < 6; x++) {
				if (buttons[x].isPressed(mousex, mousey)) {
					res = x;
					buttons[x].design.toFront();
					full_sized = true;
					selecting = false;
					selection = x;
					break;
				}
			}
		}
		*/
		return res;
	}

	public void updateSize() {
		btn_width = (variables.width - (int) (variables.height * (1 - constants.height_perc)
				* (2 * constants.edge_distance + 2 * constants.button_distance))) / 3;
		btn_height = (int) (variables.height * (1 - constants.height_perc)
				* (1 - 2 * constants.edge_distance - 1 * constants.button_distance)) / 2;

		full_width = variables.width
				- (int) (variables.height * (1 - constants.height_perc) * (2 * constants.edge_distance));
		full_height = (int) (variables.height * (1 - constants.height_perc) * (1 - 2 * constants.edge_distance));

		// Temperaur Button
		temp_btn.position((int) (variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height
						* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)),
				0);
		temp_btn.setShape(btn_width, btn_height);

		temp_btn_layout.setVirtualShape(btn_width, btn_height, 0);
		temp_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		// Light Button
		light_btn.position((int) (variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height * (1 - (1 - constants.height_perc) * constants.edge_distance)), 6);
		light_btn.setShape(btn_width, btn_height);

		light_btn_layout.setVirtualShape(btn_width, btn_height, 6);
		light_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		// PH Button

		ph_btn.position(variables.width / 2, (int) (variables.height
				* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)), 1);
		ph_btn.setShape(btn_width, btn_height);

		ph_btn_layout.setVirtualShape(btn_width, btn_height, 1);
		ph_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		// EC Button

		ec_btn.position(variables.width / 2,
				(int) (variables.height * (1 - (1 - constants.height_perc) * constants.edge_distance)), 7);
		ec_btn.setShape(btn_width, btn_height);

		ec_btn_layout.setVirtualShape(btn_width, btn_height, 7);
		ec_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		// Level Button

		level_btn.position(
				(int) (variables.width - variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height * (1 - (1 - constants.height_perc) * constants.edge_distance)), 8);
		level_btn.setShape(btn_width, btn_height);

		level_btn_layout.setVirtualShape(btn_width, btn_height, 8);
		level_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		// Flow Button

		flow_btn.position(
				(int) (variables.width - variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height
						* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)),
				2);
		flow_btn.setShape(btn_width, btn_height);

		flow_btn_layout.setVirtualShape(btn_width, btn_height, 2);
		flow_btn_layout.setStrokeWidth(constants.height_outline * variables.height);
	}

	public void update() {
		updateShape();
		super.update();
	}
}
