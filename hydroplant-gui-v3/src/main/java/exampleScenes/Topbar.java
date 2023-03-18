package exampleScenes;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.reflect.TypeToken;

import exampleLayouts.BackButton;
import exampleSceneObjects.BatteryStatus;
import gui.Layout;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx.scene.paint.Color;
import javafx2.Rectangle2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;

public class Topbar extends Scene {
	private final double outl_factor = 1;

	BatteryStatus bs;
	Button btn;
	FlatLayout fl;

	Layout fl_lt;
	BackButton bb;

	Rectangle2 backg_rec;
	Rectangle2 backg_rec2;

	static MemoryPersistence pers;
	static MqttClient topbar_client;

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
		backg_rec2 = new Rectangle2();
		backg_rec2.setX2(0);
		backg_rec2.setY2(0);
		backg_rec2.setPos(0);
		backg_rec2.setFill(constants.outline_col);
		fl_lt.addObject(backg_rec2);
		fl_lt.addObject(backg_rec);
		btn.setDesign(bb);
		fl.setDesign(fl_lt);

		addObject(fl);
		addObject(bs);
		addObject(btn);

		// Mqtt Startup

		pers = new MemoryPersistence();

		try {
			MqttConnectOptions mqtt_opt = new MqttConnectOptions();
			mqtt_opt.setMaxInflight(50);
			topbar_client = new MqttClient("tcp://localhost:1883", "topbar", pers);
			topbar_client.connect(mqtt_opt);
			System.out.println("Topbar-Client communication established");
			try {
				topbar_client.subscribe(new String[] { "value/bat" }, new int[] { 2 });
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Topbar-Client subscriptions completed");
			topbar_client.setCallback(new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					switch (topic.toUpperCase()) {
					case "VALUE/BAT":
						bs.setBatLevel(Integer.parseInt(message.toString()));
						break;
					}
				}

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("Topbar connection lost");
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

	}

	@Override
	public void updateSize() {
		double outline_size = variables.height * constants.height_outline;
		bb.setStrokeWidth(outline_size);

		backg_rec2.setStroke(Color.BLACK);
		backg_rec2.setStrokeWidth(outline_size);
		backg_rec2.setWidth2(variables.width);
		backg_rec2.setHeight2(variables.height * constants.height_perc + outl_factor * outline_size);

		backg_rec.setStroke(Color.BLACK);
		backg_rec.setStrokeWidth(outline_size);
		backg_rec.setWidth2(variables.width);
		backg_rec.setHeight2(variables.height * constants.height_perc);
		bb.setSize((int) (variables.height * constants.height_perc * constants.button_topbar_perc) / 2);
		btn.setShape((int) (variables.height * constants.height_perc * constants.button_topbar_perc),
				(int) (variables.height * constants.height_perc * constants.button_topbar_perc));
		btn.setPosition(
				variables.width - (int) (variables.height * constants.height_perc / 2)
						- (int) (variables.height * constants.button_topbar_perc_pos),
				(int) (variables.height * constants.height_perc / 2), 4);
		bs.setSize(variables.height * constants.battery_s_size);
		bs.setPosition((int) (variables.height * constants.battery_s_pos),
				(int) (variables.height * constants.height_perc / 2));
	}

	public void setBat(int bat_perc) {
		bs.setBatLevel(bat_perc);
	}

	public void setBackButton(boolean back_button) {
		if (back_button)
			bb.getPane().setOpacity(1);
		else
			bb.getPane().setOpacity(0);
	}

	@Override
	public int mouseClick(double mousex, double mousey) {
		if (btn.isPressed(mousex, mousey)) {
			return 0;
		}
		return -1;
	}
}