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
import standard.Positioning;

public class Dashboard extends Scene {
	final double warning_width_factor = 0.9;
	final double warning_size_factor = 0.0003;
	final double warning_pos_factor = 0.07;

	Button temp_btn; // Temperatur Button
	TempButton temp_btn_layout;
	Warning temp_warning;

	Button light_btn; // Light Button
	LightButton light_btn_layout;
	Warning light_warning;

	Button ph_btn; // PH Button
	PHButton ph_btn_layout;
	Warning ph_warning;

	Button ec_btn; // EC Button
	PHButton ec_btn_layout;
	Warning ec_warning;

	Button level_btn; // Level Button
	LevelButton level_btn_layout;
	Warning level_warning;

	Button flow_btn; // Flow Button
	FlowButton flow_btn_layout;
	Warning flow_warning;

	boolean full_sized = false;
	boolean selecting = true;
	int button_selection = 0; // 0: Temp, 1: Light, 2: PH, 3: EC, 4: Flow, 5: Level
	double button_factor = 0;

	int btn_width;
	int btn_height;
	int full_width;
	int full_height;

	Button[] buttons;
	DashboardButton[] button_layouts;

	Warning[] warnings;

	public Dashboard() {
		// Initialisierung der Objekte
		temp_btn = new Button();
		temp_btn_layout = new TempButton();
		temp_warning = new Warning();

		light_btn = new Button();
		light_btn_layout = new LightButton();
		light_warning = new Warning();

		ph_btn = new Button();
		ph_btn_layout = new PHButton();
		ph_warning = new Warning();

		ec_btn = new Button();
		ec_btn_layout = new PHButton();
		ec_warning = new Warning();

		level_btn = new Button();
		level_btn_layout = new LevelButton();
		level_warning = new Warning();

		flow_btn = new Button();
		flow_btn_layout = new FlowButton();
		flow_warning = new Warning();

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

		warnings = new Warning[] { temp_warning, light_warning, ph_warning, ec_warning, level_warning, flow_warning };

		addObject(temp_btn);
		addObject(light_btn);
		addObject(ph_btn);
		addObject(ec_btn);
		addObject(level_btn);
		addObject(flow_btn);

		addObject(temp_warning);
		addObject(light_warning);
		addObject(ph_warning);
		addObject(ec_warning);
		addObject(level_warning);
		addObject(flow_warning);

		updateShape();
	}

	// Setting values

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

	// Warning Activation

	public void setTempWarning(boolean status) {
		temp_warning.setActive(status);
	}

	public void setLightWarning(boolean status) {
		light_warning.setActive(status);
	}

	public void setPHWarning(boolean status) {
		ph_warning.setActive(status);
	}

	public void setECWarning(boolean status) {
		ec_warning.setActive(status);
	}

	public void setFlowWarning(boolean status) {
		flow_warning.setActive(status);
	}

	public void setLevelWarning(boolean status) {
		level_warning.setActive(status);
	}

	// Warning texts

	public void setTempWarningText(String text) {
		temp_warning.setText(text);
	}

	public void setLightWarningText(String text) {
		light_warning.setText(text);
	}

	public void setPHWarningText(String text) {
		ph_warning.setText(text);
	}

	public void setECWarningText(String text) {
		ec_warning.setText(text);
	}

	public void setFlowWarningText(String text) {
		flow_warning.setText(text);
	}

	public void setLevelWarningText(String text) {
		level_warning.setText(text);
	}

	// ------ Scene activies

	public void externalButton(int button) {
		switch (button) {
		case 0:
			for (int x = 0; x < warnings.length; x++) {
				if (warnings[x].getRealStatus() != 0) {
					warnings[x].setStatus(false);
					break;
				}
			}
			break;
		}
	}

	public void loadMode(int mode) {
		full_sized = false;
		button_selection = mode;
		button_factor = 1;
		selecting = true;

		buttons[mode].design.toFront();
	}

	void updateShape() {
		if (full_sized) {
			if (button_factor != 1)
				button_factor += 1 / variables.frameRate * constants.db_speed;
			if (button_factor > 1) {
				button_factor = 1;
				scene_event = button_selection;
			}
		} else {
			if (button_factor != 0)
				button_factor -= 1 / variables.frameRate * constants.db_speed;
			if (button_factor < 0)
				button_factor = 0;
		}

		for (int x = 0; x < 6; x++) {
			if (button_selection == x) {
				button_layouts[x]
						.setShape(
								(int) Math
										.floor(Map.map(
												Bezier.bezier_curve_2d(button_factor, new Vector(constants.db_curve, 0),
														new Vector(1 - constants.db_curve, 1)).y,
												0, 1, btn_width, full_width)),
								(int) Math.floor(Map.map(
										Bezier.bezier_curve_2d(button_factor, new Vector(constants.db_curve, 0),
												new Vector(1 - constants.db_curve, 1)).y,
										0, 1, btn_height, full_height)));
				button_layouts[x].setTextAlpha(Deckel.deckel(1 - 3 * button_factor, 0, 1));
			} else {
				button_layouts[x].setShape(btn_width, btn_height);
				button_layouts[x].setTextAlpha(1);
			}
		}
	}

	public int mouseClick(double mousex, double mousey) { // 0: Temp, 1: Light, 2: PH, 3: EC, 4: Flow, 5: Level
		int res = -1;

		boolean warning_active = false;
		for (int x = 0; x < warnings.length; x++)
			if (warnings[x].getRealStatus() != 0) {
				warning_active = true;
				break;
			}

		if (!warning_active) {
			for (int x = 0; x < warnings.length; x++)
				if (warnings[x].isPressed(mousex, mousey)) {

				}
		}

		/*
		 * if (selecting) { full_sized = false; selecting = true; for (int x = 0; x < 6;
		 * x++) { if (buttons[x].isPressed(mousex, mousey)) { res = x;
		 * buttons[x].design.toFront(); full_sized = true; selecting = false;
		 * button_selection = x; break; } } }
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
		temp_btn.setPosition((int) (variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height
						* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)),
				0);
		temp_btn.setShape(btn_width, btn_height);

		temp_btn_layout.setVirtualShape(btn_width, btn_height, 0);
		temp_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		temp_warning.setOrigin(
				temp_btn.getPosition()[0] + (Positioning.positioning(0, 6)[0] + warning_pos_factor) * btn_width,
				temp_btn.getPosition()[1] + (Positioning.positioning(0, 6)[1] - warning_pos_factor) * btn_height);

		// Light Button
		light_btn.setPosition((int) (variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height * (1 - (1 - constants.height_perc) * constants.edge_distance)), 6);
		light_btn.setShape(btn_width, btn_height);

		light_btn_layout.setVirtualShape(btn_width, btn_height, 6);
		light_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		light_warning.setOrigin(
				light_btn.getPosition()[0] + (Positioning.positioning(6, 6)[0] + warning_pos_factor) * btn_width,
				light_btn.getPosition()[1] + (Positioning.positioning(6, 6)[1] - warning_pos_factor) * btn_height);

		// PH Button

		ph_btn.setPosition(variables.width / 2, (int) (variables.height
				* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)), 1);
		ph_btn.setShape(btn_width, btn_height);

		ph_btn_layout.setVirtualShape(btn_width, btn_height, 1);
		ph_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		ph_warning.setOrigin(
				ph_btn.getPosition()[0] + (Positioning.positioning(1, 6)[0] + warning_pos_factor) * btn_width,
				ph_btn.getPosition()[1] + (Positioning.positioning(1, 6)[1] - warning_pos_factor) * btn_height);

		// EC Button

		ec_btn.setPosition(variables.width / 2,
				(int) (variables.height * (1 - (1 - constants.height_perc) * constants.edge_distance)), 7);
		ec_btn.setShape(btn_width, btn_height);

		ec_btn_layout.setVirtualShape(btn_width, btn_height, 7);
		ec_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		ec_warning.setOrigin(
				ec_btn.getPosition()[0] + (Positioning.positioning(7, 6)[0] + warning_pos_factor) * btn_width,
				ec_btn.getPosition()[1] + (Positioning.positioning(7, 6)[1] - warning_pos_factor) * btn_height);

		// Level Button

		level_btn.setPosition(
				(int) (variables.width - variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height * (1 - (1 - constants.height_perc) * constants.edge_distance)), 8);
		level_btn.setShape(btn_width, btn_height);

		level_btn_layout.setVirtualShape(btn_width, btn_height, 8);
		level_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		level_warning.setOrigin(
				level_btn.getPosition()[0] + (Positioning.positioning(8, 6)[0] + warning_pos_factor) * btn_width,
				level_btn.getPosition()[1] + (Positioning.positioning(8, 6)[1] - warning_pos_factor) * btn_height);

		// Flow Button

		flow_btn.setPosition(
				(int) (variables.width - variables.height * (1 - constants.height_perc) * constants.edge_distance),
				(int) (variables.height
						* (constants.height_perc + (1 - constants.height_perc) * constants.edge_distance)),
				2);
		flow_btn.setShape(btn_width, btn_height);

		flow_btn_layout.setVirtualShape(btn_width, btn_height, 2);
		flow_btn_layout.setStrokeWidth(constants.height_outline * variables.height);

		flow_warning.setOrigin(
				flow_btn.getPosition()[0] + (Positioning.positioning(2, 6)[0] + warning_pos_factor) * btn_width,
				flow_btn.getPosition()[1] + (Positioning.positioning(2, 6)[1] - warning_pos_factor) * btn_height);

		temp_warning.setSelectedPos((warning_width_factor + (1 - warning_width_factor) / 2) * variables.width,
				variables.height * (constants.height_perc + (1 - constants.height_perc) / 2));
		light_warning.setSelectedPos((warning_width_factor + (1 - warning_width_factor) / 2) * variables.width,
				variables.height * (constants.height_perc + (1 - constants.height_perc) / 2));
		ph_warning.setSelectedPos((warning_width_factor + (1 - warning_width_factor) / 2) * variables.width,
				variables.height * (constants.height_perc + (1 - constants.height_perc) / 2));
		ec_warning.setSelectedPos((warning_width_factor + (1 - warning_width_factor) / 2) * variables.width,
				variables.height * (constants.height_perc + (1 - constants.height_perc) / 2));
		level_warning.setSelectedPos((warning_width_factor + (1 - warning_width_factor) / 2) * variables.width,
				variables.height * (constants.height_perc + (1 - constants.height_perc) / 2));
		flow_warning.setSelectedPos((warning_width_factor + (1 - warning_width_factor) / 2) * variables.width,
				variables.height * (constants.height_perc + (1 - constants.height_perc) / 2));

		temp_warning.setSize(warning_size_factor * variables.height);
		light_warning.setSize(warning_size_factor * variables.height);
		ph_warning.setSize(warning_size_factor * variables.height);
		ec_warning.setSize(warning_size_factor * variables.height);
		level_warning.setSize(warning_size_factor * variables.height);
		flow_warning.setSize(warning_size_factor * variables.height);

		temp_warning.setOutline(constants.height_outline * variables.height);
		light_warning.setOutline(constants.height_outline * variables.height);
		ph_warning.setOutline(constants.height_outline * variables.height);
		ec_warning.setOutline(constants.height_outline * variables.height);
		level_warning.setOutline(constants.height_outline * variables.height);
		flow_warning.setOutline(constants.height_outline * variables.height);

		temp_warning.setRectangle(warning_width_factor * variables.width);
		light_warning.setRectangle(warning_width_factor * variables.width);
		ph_warning.setRectangle(warning_width_factor * variables.width);
		ec_warning.setRectangle(warning_width_factor * variables.width);
		level_warning.setRectangle(warning_width_factor * variables.width);
		flow_warning.setRectangle(warning_width_factor * variables.width);
	}

	public void update() {
		updateShape();
		super.update();
	}
}
