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

import exampleSceneObjects.TimeLapse;
import exampleSceneObjects.TimeLapseList;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx2.Rectangle2;
import sceneObjects.MiniScene;
import timelapse.TimeLapseData;

public class TimeLapseScene extends Scene {
	final double scroll_factor = 0.9;

	TimeLapseList tll;

	Rectangle2 scroll_clip;
	MiniScene scroll;

	MqttClient timelapse_client;
	MemoryPersistence pers;

	ArrayList<TimeLapse> data_tl;
	boolean newData = false;

	public TimeLapseScene() {

		// Mqtt Startup

		pers = new MemoryPersistence();

		try {
			timelapse_client = new MqttClient("tcp://localhost:1883", "timelapse", pers);
			timelapse_client.connect();
			System.out.println("Timelapse-Client communication established");
			timelapse_client.subscribe(new String[] { "timelapse/data" });

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

		scroll = new MiniScene();
		scroll.setPosition(scroll_factor, scroll_factor);

		tll = new TimeLapseList();
		tll.setPos(2);
		tll.setShape(1300, 650);
		tll.setTLHeight(300);

		try {
			timelapse_client.publish("timelapse/get", new MqttMessage("true".getBytes()));
		} catch (MqttException e) {
			System.out.println("Could not get timelapse");
			e.printStackTrace();
		}

		addObject(tll);
	}

	@Override
	public void update() {
		if (newData) {
			newData = false;
			tll.setTimeLapse(data_tl);
		}
		super.update();
	}

	@Override
	public int mouseClick(double mousex, double mousey) {
		int pressed = tll.mouseClicked(mousex, mousey);
		if (pressed != -1) {
			try {
				timelapse_client.publish("timelapse/delete", new MqttMessage(Integer.toString(pressed).getBytes()));
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	@Override
	public void mousePressed(double mousex, double mousey) {
		tll.mousePressed(mousex, mousey);
	}

	@Override
	public void mouseReleased(double mousex, double mousey) {
		tll.mouseReleased(mousex, mousey);
	}

	@Override
	public void mouseDragged(double mousex, double mousey) {
		tll.mouseDragged(mousex, mousey);
	}

	@Override
	public void updateSize() {
		tll.setPosition((variables.width - 1300) / 2, 80);
		tll.setOutline(variables.height * constants.height_outline);
	}
}
