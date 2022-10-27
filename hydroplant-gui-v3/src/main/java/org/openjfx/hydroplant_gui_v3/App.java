package org.openjfx.hydroplant_gui_v3;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import exampleScenes.Dashboard;
import exampleScenes.LightScene;
import exampleScenes.TempScene;
import exampleScenes.TimeLapseSelection;
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
	boolean updateSize = true;

	Topbar tb;
	Dashboard db;
	TimeLapseSelection tls;
	TempScene ts;
	LightScene ls;

	Pane root = new Pane(); // Szene initialisieren

	boolean calc_fr = true;

	@Override
	public void start(Stage stage) {
		MemoryPersistence pers = new MemoryPersistence();

		try {

			// Setting up client

			MqttClient client = new MqttClient("tcp://localhost:1883", "gui", pers);
			client.connect();

			client.subscribe(new String[] { "value/temperature", "value/light", "value/lightStatus", "value/ph",
					"value/ec", "value/flow", "value/level", "warning/temperature", "warning/light", "warning/ph",
					"warning/ec", "warning/flow", "warning/level", "warningtext/temperature", "warningtext/light",
					"warningtext/ph", "warningtext/ec", "warningtext/flow", "warningtext/level" });
			System.out.println("Client communication established");

			// MQTT Client callback

			client.setCallback(new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					switch (topic.toUpperCase()) {
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
					case "VALUE/LEVLE":
						level = Double.parseDouble(message.toString());
						break;

					case "WARNING/TEMPERATURE":
						temp_warning = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/LIGHT":
						light_warning = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/PH":
						ph_warning = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/EC":
						ec_warning = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/FLOW":
						flow_warning = Boolean.parseBoolean(message.toString());
						break;
					case "WARNING/LEVEL":
						level_warning = Boolean.parseBoolean(message.toString());
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

				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {

				}
			});

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();

			return;
		}

		var javaVersion = SystemInfo.javaVersion();
		var javafxVersion = SystemInfo.javafxVersion();

		// ------------------------------------------------------ Set up variables

		variables.height = 768; // Temporäre Lösung
		variables.width = 1366;

		SceneHandler sh;

		db = new Dashboard();
		SceneBundle db_sb = new SceneBundle(db);
		db.setTemp(0);
		db.calibrateTempBtn(17, 21, 25, 1);

		tls = new TimeLapseSelection();
		SceneBundle tls_sb = new SceneBundle(tls);

		/**
		 * Additional scenes, optional when everything else works ts = new TempScene(); ts.setTemp(0);
		 * SceneBundle ts_sb = new SceneBundle(ts); ts.calibrateTemp(17, 21, 25, 1);
		 *
		 * ls = new LightScene(); ls.setLightStatus(false); SceneBundle ls_sb = new SceneBundle(ls);
		 */

		tb = new Topbar();
		tb.setBat(56);
		root.getChildren().add(tb.root);

		// ------------------------------------------------------ Scene dependencies

		// db_sb.addDep(0, ts_sb, 0);
		// db_sb.addDep(1, ls_sb, 0);
		// ts_sb.addDep(0, db_sb, 0);
		// ls_sb.addDep(0, db_sb, 1);

		// ------------------------------------------------------ Init scene handler

		sh = new SceneHandler();
		sh.setScene(db_sb);
		root.getChildren().add(db);

		Scene scene = new Scene(root, 1366, 768); // Sets up scene

		ChangeListener<Number> stageWidthListener = (observable, oldValue, newValue) -> {
			updateSize = true;
		};

		ChangeListener<Number> stageHeightListener = (observable, oldValue, newValue) -> {
			updateSize = true;
		};

		scene.widthProperty().addListener(stageWidthListener);
		scene.heightProperty().addListener(stageHeightListener);

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() { // Handles mouse
																 // clicks
			@Override
			public void handle(MouseEvent event) {
				// calc_fr = !calc_fr;
				sh.mouseClick();
				if (tb.mouseClick() == 0)
					sh.externalButton(0);
			}
		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() { // Handles mouse
																 // movement
			@Override
			public void handle(MouseEvent event) {

			}
		});

		stage.sizeToScene(); // Sets up the Stage / Window
		stage.setScene(scene);
		stage.setTitle("Hydroplant.virus.exe");
		stage.show();

		// stage.setFullScreen(true);

		final AnimationTimer at = new AnimationTimer() { // Animation Timer will execute once every frame
			@Override
			public void handle(long now) {
				if (last == 0)
					last = now;
				if (now != last && calc_fr)
					variables.frameRate = 1000000000 / (now - last);
				else
					variables.frameRate = Integer.MAX_VALUE;
				// System.out.println(variables.frameRate);

				if (shitChanged) {													// If variable changed, all
																						// values will be updated
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

				if (updateSize) {													// Size updates are handled in
					updateSize = false;													// the Animation Timer,
					variables.width = (int) scene.getWidth();							// because the
					variables.height = (int) scene.getHeight();							// ChangeListener acts like
					sh.getActive().updateSize();										// an interrupt, which
					tb.updateSize();													// causes problems with
				}																		// objects, which heavily
																							// rely on the size, like
																							// FlowButton

				tb.update();
				if (sh.handle()) {
					System.out.println("A switchd ja scene madafaka");
					root.getChildren().set(1, sh.getActive());
				}

				last = now;
			}
		};

		final Timer sizeUpdater = new Timer();
		sizeUpdater.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Updating");
				stageWidthListener.changed(null, null, scene.getWidth());
				stageHeightListener.changed(null, null, scene.getHeight());
			}
		}, 1000, 5000);

		// loop
		at.start(); // Starts the animation launcher

	}

	public static void main(String[] args) {
		launch();
	}

}