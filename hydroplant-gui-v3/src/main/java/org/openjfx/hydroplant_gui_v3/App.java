package org.openjfx.hydroplant_gui_v3;

import java.security.Timestamp;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import exampleLayouts.TempButton;
import exampleScenes.Dashboard;
import exampleScenes.LightScene;
import exampleScenes.TempScene;
import exampleScenes.Topbar;
import gui.SceneBundle;
import gui.SceneHandler;
import gui.variables;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	long last = 0;

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

	boolean temp_warning = false;
	boolean light_warning = false;
	boolean ph_warning = false;
	boolean ec_warning = false;
	boolean flow_warning = false;
	boolean level_warning = false;

	boolean shitChanged = true;

	Topbar tb;
	Dashboard db;
	TempScene ts;
	LightScene ls;

	Pane root = new Pane(); // Szene initialisieren

	boolean calc_fr = true;

	@Override
	public void start(Stage stage) {
		MemoryPersistence pers = new MemoryPersistence();

		try {
			MqttClient client = new MqttClient("tcp://localhost:1883", "gui", pers);
			client.connect();

			client.subscribe(new String[] { "value/temperature", "value/light", "value/lightStatus", "value/ph",
					"value/ec", "value/flow", "value/level", "warning/temperature", "warning/light", "warning/ph",
					"warning/ec", "warning/flow", "warning/level", "warningtext/temperature", "warningtext/light",
					"warningtext/ph", "warningtext/ec", "warningtext/flow", "warningtext/level" });
			System.out.println("Client communication established");

			var javaVersion = SystemInfo.javaVersion();
			var javafxVersion = SystemInfo.javafxVersion();

			variables.height = 768; // Temporäre Lösung
			variables.width = 1366;

			SceneHandler sh;

			db = new Dashboard();
			db.setTemp(0);
			SceneBundle db_sb = new SceneBundle(db);
			db.calibrateTempBtn(17, 21, 25, 1);

			ts = new TempScene();
			ts.setTemp(0);
			SceneBundle ts_sb = new SceneBundle(ts);
			ts.calibrateTemp(17, 21, 25, 1);

			ls = new LightScene();
			ls.setLightStatus(false);
			SceneBundle ls_sb = new SceneBundle(ls);

			tb = new Topbar();
			tb.setBat(56);
			root.getChildren().add(tb.root);

			// Scene dependencies

			db_sb.addDep(0, ts_sb, 0);
			db_sb.addDep(1, ls_sb, 0);
			ts_sb.addDep(0, db_sb, 0);
			ls_sb.addDep(0, db_sb, 1);

			// Init scene handler

			sh = new SceneHandler();
			sh.setScene(db_sb);
			root.getChildren().add(db.root);

			Scene scene = new Scene(root, 1366, 768); // Sets up scene

			ChangeListener<Number> stageWidthListener = (observable, oldValue, newValue) -> { // Handles change of stage
																								// size
				variables.width = (int) Math.floor((double) newValue);
				sh.getActive().updateSize();
				tb.updateSize();
			};

			ChangeListener<Number> stageHeightListener = (observable, oldValue, newValue) -> { // Handles change of
																								// stage
				// size
				variables.height = (int) Math.floor((double) newValue);
				sh.getActive().updateSize();
				tb.updateSize();
			};

			scene.widthProperty().addListener(stageWidthListener);
			scene.heightProperty().addListener(stageHeightListener);

			scene.setOnMouseClicked((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() { // Handles mouse
																										// clicks
				@Override
				public void handle(MouseEvent event) {
					// calc_fr = !calc_fr;
					sh.mouseClick(event.getSceneX(), event.getSceneY());
					if (tb.mouseClick(event.getSceneX(), event.getSceneY()) == 0)
						sh.externalButton(0);
				}
			});

			scene.setOnMouseMoved((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() { // Handles mouse
																										// movement
				@Override
				public void handle(MouseEvent event) {

				}
			});

			// MQTT Client callback

			client.setCallback(new MqttCallback() {
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					switch (topic.toUpperCase()) {
					case "VALUE/TEMPERATURE":
						temp = (double) Double.parseDouble(message.toString());
						break;
					case "VALUE/LIGHT":
						light = (double) Double.parseDouble(message.toString());
						break;
					case "VALUE/LIGHTSTATUS":
						lightStatus = (boolean) Boolean.parseBoolean(message.toString());
						break;
					case "VALUE/PH":
						ph = (double) Double.parseDouble(message.toString());
						break;
					case "VALUE/EC":
						// ec = (double)Double.parseDouble(message.toString());
						break;
					case "VALUE/FLOW":
						flow = (double) Double.parseDouble(message.toString());
						break;
					case "VALUE/LEVLE":
						level = (double) Double.parseDouble(message.toString());
						break;

					case "WARNING/TEMPERATURE":
						temp_warning = (boolean) Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/LIGHT":
						light_warning = (boolean) Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/PH":
						ph_warning = (boolean) Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/EC":
						ec_warning = (boolean) Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/FLOW":
						flow_warning = (boolean) Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/LEVEL":
						level_warning = (boolean) Boolean.parseBoolean(message.toString());
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

				public void connectionLost(Throwable cause) {

				}

				public void deliveryComplete(IMqttDeliveryToken token) {

				}
			});

			stage.sizeToScene(); // Sets up the Stage / Window
			stage.setScene(scene);
			stage.setTitle("Hydroplant.virus.exe");
			stage.show();

			ChangeListener<Boolean> stageSizeChange = (observable, oldValue, newValue) -> { // Handles change of stage
																							// size
				stageWidthListener.changed(null, null, scene.getWidth());
				stageHeightListener.changed(null, null, scene.getHeight());
			};

			stage.fullScreenProperty().addListener(stageSizeChange);
			stage.maximizedProperty().addListener(stageSizeChange);

			// stage.setFullScreen(true);

			sh.getActive().updateSize();
			tb.updateSize();

			AnimationTimer at = new AnimationTimer() { // Animation Timer will execute once every frame
				@Override
				public void handle(long now) {
					if (last == 0)
						last = now;
					if (now != last && calc_fr)
						variables.frameRate = 1000000000 / (now - last);
					else
						variables.frameRate = Integer.MAX_VALUE;
					// System.out.println(variables.frameRate);

					if (shitChanged) {
						db.setTemp(temp);
						db.setLightValue(light);
						db.setLightStatus(lightStatus);
						db.setPHValue(ph);
						db.setFlowValue(flow);
						db.setLevel(level);

						db.setTempWarning(temp_warning);
						db.setTempWarningText(temp_warning_text);
						db.setLightWarning(light_warning);
						db.setLightWarningText(light_warning_text);
						db.setPHWarning(ph_warning);
						db.setPHWarningText(ph_warning_text);
						db.setECWarning(ec_warning);
						db.setECWarningText(ec_warning_text);
						db.setFlowWarning(flow_warning);
						db.setFlowWarningText(flow_warning_text);
						db.setLevelWarning(level_warning);
						db.setLevelWarningText(level_warning_text);
						shitChanged = false;
					}

					tb.update();
					if (sh.handle()) {
						System.out.println("A switchd ja scene madafaka");
						root.getChildren().set(1, sh.getActive().root);
					}

					last = now;
				}
			};

			sh.getActive().updateSize();
			tb.updateSize();

			// loop
			at.start(); // Starts the animation launcher
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("GUI started");
		launch();
	}

}