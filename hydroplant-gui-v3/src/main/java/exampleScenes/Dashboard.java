package exampleScenes;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exampleLayouts.DashboardButton;
import exampleLayouts.FlowButton;
import exampleLayouts.LevelButton;
import exampleLayouts.LightButton;
import exampleLayouts.PHButton;
import exampleLayouts.TempButton;
import exampleSceneObjects.Warning;
import gui.Scene;
import gui.constants;
import gui.variables;
import sceneObjects.Button;
import standard.Bezier;
import standard.Deckel;
import standard.Map;
import standard.Positioning;
import standard.Vector;

public class Dashboard extends Scene {
	final double warning_width_factor = 0.9;
	final double warning_size_factor = 0.0003;
	final double warning_pos_factor = 0.07;

	final double scene_speed = 2;
	final double scene_bez_factor = 0.3;
	double scene_factor = 1;
	boolean scene_active = true;
	boolean scene_change = false;
	
	Gson gson;

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

	double btn_width;
	double btn_height;
	double full_width;
	double full_height;

	Button[] buttons;
	DashboardButton[] button_layouts;

	Warning[] warnings;

	MqttClient dashboard_client;
	MemoryPersistence pers;

	double temp = 0;
	double light = 0;
	boolean lightStatus = false;
	double ph = 0;
	double flow = 0;
	double level = 0;

	String temp_warning_text = "";
	String light_warning_text = "";
	String ph_warning_text = "";
	String ec_warning_text = "";
	String flow_warning_text = "";
	String level_warning_text = "";

	boolean temp_warning_bool = false;
	boolean light_warning_bool = false;
	boolean ph_warning_bool = false;
	boolean ec_warning_bool = false;
	boolean flow_warning_bool = false;
	boolean level_warning_bool = false;

	boolean shitChanged = true;

	public Dashboard() {
		// Gson
		gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println("Dashboard Gson created");
		
		// Mqtt Startup

		pers = new MemoryPersistence();

		try {
			dashboard_client = new MqttClient("tcp://localhost:1883", "dashboard", pers);
			dashboard_client.connect();
			System.out.println("Dashboard-Client communication established");
			dashboard_client.subscribe(new String[] { "option/temperature", "option/maxlevel", "value/temperature", "value/light", "value/lightStatus",
					"value/ph", "value/ec", "value/flow", "value/level", "warning/temperature", "warning/light",
					"warning/ph", "warning/ec", "warning/flow", "warning/level", "warningtext/temperature",
					"warningtext/light", "warningtext/ph", "warningtext/ec", "warningtext/flow", "warningtext/level" });

			System.out.println("Dashboard-Client subscriptions completed");
			dashboard_client.setCallback(new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					switch (topic.toUpperCase()) {
					case "OPTION/TEMPERATURE":
						ArrayList<Double> temp_options = gson.fromJson(new String(message.getPayload()), new TypeToken<ArrayList<Double>>() {
								}.getType());
						temp_btn_layout.setTemperatures(temp_options.get(0), temp_options.get(1), temp_options.get(3), temp_options.get(2));
						break;
					case "OPTION/MAXLEVEL":
						level_btn_layout.setMaxLevel(Double.parseDouble(new String(message.getPayload())));
						break;
						
					case "VALUE/TEMPERATURE":
						temp = Double.parseDouble(message.toString());
						break;
					case "VALUE/LIGHT":
						light = Double.parseDouble(message.toString());
						break;
					case "VALUE/LIGHTSTATUS":
						lightStatus = Boolean.parseBoolean(message.toString());
						break;
					case "VALUE/PH":
						ph = Double.parseDouble(message.toString());
						break;
					case "VALUE/EC":
						// ec = (double)Double.parseDouble(message.toString());
						break;
					case "VALUE/FLOW":
						flow = Double.parseDouble(message.toString());
						break;
					case "VALUE/LEVEL":
						level = Double.parseDouble(message.toString());
						break;

					case "WARNING/TEMPERATURE":
						temp_warning_bool = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/LIGHT":
						light_warning_bool = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/PH":
						ph_warning_bool = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/EC":
						ec_warning_bool = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/FLOW":
						flow_warning_bool = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/LEVEL":
						level_warning_bool = Boolean.parseBoolean(message.toString());
						break;

					case "WARNINGTEXT/TEMPERATURE":
						temp_warning_text = message.toString();
						break;
					case "WARNINGTEXT/LIGHT":
						light_warning_text = message.toString();
						break;
					case "WARNINGTEXT/PH":
						ph_warning_text = message.toString();
						break;
					case "WARNINGTEXT/EC":
						ec_warning_text = message.toString();
						break;
					case "WARNINGTEXT/FLOW":
						flow_warning_text = message.toString();
						break;
					case "WARNINGTEXT/LEVEL":
						level_warning_text = message.toString();
						break;
					}
					shitChanged = true;
				}

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("Dashboard connection lost");
					System.out.println(cause.toString());
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}
			});
			System.out.println("Dashboard-Client callback set");
		} catch (MqttException e) {
			System.out.println("Dashboard-Client failed!");
			e.printStackTrace();
		}

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

		warnings = new Warning[] { temp_warning, light_warning, ph_warning, ec_warning, flow_warning, level_warning };

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

	// ------ Scene activies

	@Override
	public void externalButton(int button) {
		switch (button) {
		case 0:
			for (Warning warning : warnings) {
				if (warning.getRealStatus() != 0) {
					warning.setStatus(false);
					return;
				}
			}
			scene_active = false;
			scene_change = true;
			break;
		}
	}

	/*
	 * final double scene_factor = 1; final boolean scene_active = true;
	 */

	@Override
	public void loadMode(int mode) {
		switch (mode) {
		case 0:
			scene_factor = 0;
			scene_active = true;
			scene_change = true;
			break;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			full_sized = false;
			button_selection = mode;
			button_factor = 1;
			selecting = true;

			buttons[mode].design.toFront();
			break;

		}
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
		for (int x = 0; x < button_layouts.length; x++) {
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

	@Override
	public int mouseClick(double mousex, double mousey) { // 0: Temp, 1: Light, 2: PH, 3: EC, 4: Flow, 5: Level
		int res = -1;

		boolean warning_active = false;
		for (Warning warning : warnings)
			if (warning.getRealStatus() != 0) {
				warning_active = true;
				break;
			}

		if (!warning_active) {
			for (Warning warning : warnings)
				if (warning.isPressed(mousex, mousey)) {

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

	@Override
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

	@Override
	public void update() {
		updateShape();
		super.update();

		if (shitChanged) {
			temp_btn_layout.setTemperature(temp);
			light_btn_layout.setStatus(lightStatus);
			light_btn_layout.setValue(light);
			ph_btn_layout.setValue(ph);
			// ec_btn_layout.setValue(ec);
			flow_btn_layout.setValue(flow);
			level_btn_layout.setLevel(level);

			temp_warning.setActive(temp_warning_bool);
			light_warning.setActive(light_warning_bool);
			ph_warning.setActive(ph_warning_bool);
			ec_warning.setActive(ec_warning_bool);
			flow_warning.setActive(flow_warning_bool);
			level_warning.setActive(level_warning_bool);

			temp_warning.setText(temp_warning_text);
			light_warning.setText(light_warning_text);
			ph_warning.setText(ph_warning_text);
			ec_warning.setText(ec_warning_text);
			flow_warning.setText(flow_warning_text);
			level_warning.setText(level_warning_text);

			shitChanged = false;
		}
		/*
		 * final double scene_speed = 2; final double scene_bez_factor = 0.3; final
		 * double scene_factor = 1; final boolean scene_active = true;
		 */
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
					this.scene_event = 0;
				}
			}
			double scene_bez = Bezier.bezier_curve_2d(scene_factor, new Vector(scene_bez_factor, 0),
					new Vector(1 - scene_bez_factor, 1)).y;
			this.root.setOpacity(scene_bez);
		}
	}
}
