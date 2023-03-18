package exampleScenes;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exampleSceneObjects.NewTimeLapse;
import exampleSceneObjects.TimeLapse;
import exampleSceneObjects.TimeLapseList;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx.scene.effect.ColorAdjust;
import javafx2.Rectangle2;
import sceneObjects.MiniScene;
import standard.Bezier;
import standard.Map;
import standard.Vector;
import timelapse.TimeLapseData;

public class TimeLapseScene extends Scene {
	private final double scroll_factor = 0.9;
	private final double ntl_width = 0.9;
	private final double ntl_height = 0.9;
	private final double speed = 1;
	private final double bez_factor = 0.3;
	private final double extra_top_factor = 0.0;

	private final double tll_width = 0.95;
	private final double tll_height = 0.95;
	private final double tll_height_factor = 0.35;
	private final double tll_gap_factor = 0.02;

	final double scene_speed = 2;
	final double scene_bez_factor = 0.3;
	double scene_factor = 1;
	boolean scene_active = true;
	boolean scene_change = false;

	NewTimeLapse ntl;

	double ntl_first_posy;
	double ntl_second_posy;

	double ntl_factor = 0;
	boolean ntl_mode = false;
	boolean moving = false;

	ColorAdjust ca;
	TimeLapseList tll;

	Rectangle2 scroll_clip;
	MiniScene scroll;

	MqttClient timelapse_client;
	MemoryPersistence pers;

	ArrayList<TimeLapse> data_tl;
	boolean newData = false;

	private void startMqtt() {
		// Mqtt Startup

		pers = new MemoryPersistence();

		try {
			MqttConnectOptions mqtt_opt = new MqttConnectOptions();
			mqtt_opt.setMaxInflight(50);
			timelapse_client = new MqttClient("tcp://localhost:1883", "timelapse", pers);
			timelapse_client.connect(mqtt_opt);
			System.out.println("Timelapse-Client communication established");
			try {
				timelapse_client.subscribe("timelapse/data", 2);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Timelapse-Client subscriptions completed");
			timelapse_client.setCallback(new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					switch (topic.toUpperCase()) {
					case "TIMELAPSE/DATA":
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						ArrayList<TimeLapseData> data = gson.fromJson(message.toString(),
								new TypeToken<ArrayList<TimeLapseData>>() {
								}.getType());
						data_tl = new ArrayList<>();

						for (int x = 0; x < data.size(); x++) {
							data_tl.add(TimeLapse.fromData(data.get(x)));
						}

						newData = true;

						break;
					}
				}

				@Override
				public void connectionLost(Throwable cause) {

				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}
			});
			System.out.println("Timelapse-Client callback set");
		} catch (MqttException e) {
			System.out.println("Timelapse-Client failed!");
			e.printStackTrace();
		}
	}

	public TimeLapseScene() {
		startMqtt();

		scroll = new MiniScene();
		scroll.setPosition(scroll_factor, scroll_factor);

		ca = new ColorAdjust();

		tll = new TimeLapseList();
		tll.setPos(0);
		tll.getPane().setEffect(ca);

		try {
			timelapse_client.publish("timelapse/get", new MqttMessage("true".getBytes()));
		} catch (MqttException e) {
			System.out.println("Could not get timelapse");
			e.printStackTrace();
		}

		ntl = new NewTimeLapse();
		ntl.setPos(1);
		ntl.setMode(true);

		addObject(tll);
		addObject(ntl);
	}

	@Override
	public void update() {
		if (newData) {
			newData = false;
			tll.setTimeLapse(data_tl);
		}
		super.update();

		ntl.update();
		if (moving) {
			if (ntl_mode) {
				ntl_factor += speed / variables.frameRate;
				if (ntl_factor >= 1) {
					ntl.setActive(true);
					ntl_factor = 1;
					moving = false;
				}
			} else {
				ntl_factor -= speed / variables.frameRate;
				if (ntl_factor <= 0) {
					ntl.reload();
					ntl_factor = 0;
					moving = false;
				}
			}
			ca.setBrightness(ntl_factor * 0.7);
			double bez = Map.map(
					Bezier.bezier_curve_2d(ntl_factor, new Vector(bez_factor, 0), new Vector(1 - bez_factor, 1)).y, 0,
					1, this.ntl_first_posy, this.ntl_second_posy);
			ntl.setPosition(ntl.getPosition()[0], bez);
		}

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

	@Override
	public int mouseClick(double mousex, double mousey) {
		switch (ntl.mouseClick(mousex, mousey)) {
		case 0:
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String new_tl = gson.toJson(ntl.getTimeLapse());
			try {
				timelapse_client.publish("timelapse/add", new MqttMessage(new_tl.getBytes()));
			} catch (MqttException e1) {
				e1.printStackTrace();
			}
		case 1:
			ntl.setActive(false);
			ntl.setMode(ntl_mode);
			ntl_mode = !ntl_mode;
			moving = true;
			break;
		}

		if (!ntl_mode) {
			int pressed = tll.mouseClicked(mousex, mousey);
			if (pressed != -1) {
				try {
					timelapse_client.publish("timelapse/delete", new MqttMessage(Integer.toString(pressed).getBytes()));
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}

		return -1;
	}

	@Override
	public void mousePressed(double mousex, double mousey) {
		if (!ntl_mode)
			tll.mousePressed(mousex, mousey);
		ntl.mousePressed(mousex, mousey);
	}

	@Override
	public void mouseReleased(double mousex, double mousey) {
		if (!ntl_mode)
			tll.mouseReleased(mousex, mousey);
		ntl.mouseReleased(mousex, mousey);
	}

	@Override
	public void mouseDragged(double mousex, double mousey) {
		if (!ntl_mode)
			tll.mouseDragged(mousex, mousey);
		ntl.mouseDragged(mousex, mousey);
	}

	@Override
	public void externalButton(int btn) {
		switch (btn) {
		case 0:
			if (ntl_mode) {
				ntl.setActive(false);
				ntl.setMode(ntl_mode);
				ntl_mode = !ntl_mode;
				moving = true;
			} else {
				scene_active = false;
				scene_change = true;
			}
			break;
		}
	}

	@Override
	public void loadMode(int mode) {
		switch (mode) {
		case 0:
			ntl_factor = 0;
			ntl_mode = false;

			scene_factor = 0;
			scene_active = true;
			scene_change = true;
			break;
		}
	}

	@Override
	public void updateSize() {
		tll.setShape(tll_width * variables.width,
				tll_height * (variables.height * (1 - constants.height_perc) * (1 - extra_top_factor)));
		double top = variables.height * (constants.height_perc + extra_top_factor);
		tll.setPosition(variables.width / 2 - (tll_width * variables.width) / 2,
				top + (variables.height - top - tll.getShape()[1]) / 2);
		tll.setOutline(variables.height * constants.height_outline);
		tll.setTLGap(variables.height * tll_gap_factor);
		tll.setTLHeight(variables.height * tll_height_factor);

		ntl.setOutline(variables.height * constants.height_outline);
		ntl.setShape(variables.width * ntl_width, variables.height * (1 - constants.height_perc) * ntl_height);
		ntl_first_posy = variables.height * constants.height_perc - ntl.getRealHeight()
				- variables.height * constants.height_outline;
		ntl_second_posy = variables.height * constants.height_perc + (variables.height * (1 - constants.height_perc)
				- variables.height * (1 - constants.height_perc) * ntl_height) / 2;

		double bez = Map.map(
				Bezier.bezier_curve_2d(ntl_factor, new Vector(bez_factor, 0), new Vector(1 - bez_factor, 1)).y, 0, 1,
				this.ntl_first_posy, this.ntl_second_posy);
		ntl.setPosition(variables.width / 2, bez);
	}
}
