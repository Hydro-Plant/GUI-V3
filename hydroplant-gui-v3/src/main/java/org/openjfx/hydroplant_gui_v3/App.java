package org.openjfx.hydroplant_gui_v3;

import java.util.Timer;
import java.util.TimerTask;

import exampleScenes.Dashboard;
import exampleScenes.LightScene;
import exampleScenes.TempScene;
import exampleScenes.TestScene;
import exampleScenes.Topbar;
import gui.SceneBundle;
import gui.SceneHandler;
import gui.variables;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	boolean sizeUpdate = true;
	boolean drag = false;

	TestScene tests;
	Topbar tb;
	Dashboard db;
	TempScene ts;
	LightScene ls;

	Pane root = new Pane(); // Szene initialisieren

	boolean calc_fr = true;

	@Override
	public void start(Stage stage) {
		var javaVersion = SystemInfo.javaVersion();
		var javafxVersion = SystemInfo.javafxVersion();

		variables.height = 768; // Temporäre Lösung
		variables.width = 1366;

		SceneHandler sh;

		tests = new TestScene();
		SceneBundle tests_sb = new SceneBundle(tests);

		db = new Dashboard();
		SceneBundle db_sb = new SceneBundle(db);

		ts = new TempScene();
		ts.setTemp(0);
		SceneBundle ts_sb = new SceneBundle(ts);

		ls = new LightScene();
		ls.setLightStatus(false);
		SceneBundle ls_sb = new SceneBundle(ls);

		tb = new Topbar();
		tb.setBat(56);

		// Scene dependencies

		db_sb.addDep(0, ts_sb, 0);
		db_sb.addDep(1, ls_sb, 0);
		ts_sb.addDep(0, db_sb, 0);
		ls_sb.addDep(0, db_sb, 1);

		// Init scene handler

		sh = new SceneHandler();
		sh.setScene(tests_sb);
		root.getChildren().add(sh.getActive().root);

		root.getChildren().add(tb.root);

		Scene scene = new Scene(root, 1366, 768); // Sets up scene

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				sh.mousePressed(event.getSceneX(), event.getSceneY());

			}

		});

		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				sh.mouseReleased(event.getSceneX(), event.getSceneY());

			}

		});

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() { 									// Handles mouse
																 									// clicks
			@Override
			public void handle(MouseEvent event) {
				// calc_fr = !calc_fr;
				sh.mouseClick(event.getSceneX(), event.getSceneY());
				if (tb.mouseClick(event.getSceneX(), event.getSceneY()) == 0)
					sh.externalButton(0);
			}
		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() { 										// Handles mouse
																 										// movement
			@Override
			public void handle(MouseEvent event) {
				sh.mouseMoved(event.getSceneX(), event.getSceneY());
			}
		});

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() { 										// Handles
																 										// mouse
			// movement
			@Override
			public void handle(MouseEvent event) {
				sh.mouseDragged(event.getSceneX(), event.getSceneY());
			}
		});

		scene.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				sizeUpdate = true;

			}

		});
		scene.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				sizeUpdate = true;

			}

		});

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				sizeUpdate = true;
			}
		}, 0, 5000);

		stage.sizeToScene(); // Sets up the Stage / Window
		stage.setScene(scene);
		stage.setTitle("Hydroplant.virus.exe");
		stage.show();

		// stage.setFullScreen(true);

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

				if (sizeUpdate) {
					sizeUpdate = false;
					variables.width = scene.getWidth();
					variables.height = scene.getHeight();
					tb.updateSize();
					sh.getActive().updateSize();
				}

				tb.update();
				if (sh.handle()) {
					System.out.println("A switchd ja scene madafaka");
					root.getChildren().set(0, sh.getActive().root);
				}
				last = now;
			}
		};

		at.start(); 				// Starts the animation launcher
	}

	public static void main(String[] args) {
		System.out.println("GUI started");
		launch();
	}

}