package org.openjfx.hydroplant_gui_v3;

import java.util.Timer;
import java.util.TimerTask;

import exampleScenes.Dashboard;
import exampleScenes.LightScene;
import exampleScenes.StartScene;
import exampleScenes.TempScene;
import exampleScenes.TestScene;
import exampleScenes.TimeLapseScene;
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
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	static final long touch_time = 500;

	long last = 0;
	boolean sizeUpdate = true;
	boolean drag = false;

	TestScene tests;
	StartScene ss;
	TimeLapseScene tls;
	Topbar tb;
	Dashboard db;
	TempScene ts;
	LightScene ls;

	Pane root = new Pane(); // Szene initialisieren

	boolean calc_fr = true;

	static long touch_start = 0;

	@Override
	public void start(Stage stage) {
		var javaVersion = SystemInfo.javaVersion();
		var javafxVersion = SystemInfo.javafxVersion();

		variables.height = 768; // Temporäre Lösung
		variables.width = 1366;

		SceneHandler sh;

		tests = new TestScene();
		SceneBundle tests_sb = new SceneBundle(tests);

		ss = new StartScene();
		SceneBundle ss_sb = new SceneBundle(ss);

		tls = new TimeLapseScene();
		SceneBundle tls_sb = new SceneBundle(tls);

		db = new Dashboard();
		SceneBundle db_sb = new SceneBundle(db);

		tb = new Topbar();
		tb.setBackButton(false);
		tb.setBat(56);

		// Scene dependencies

		ss_sb.addDep(0, db_sb, 0);
		ss_sb.addDep(1, tls_sb, 0);
		db_sb.addDep(0, ss_sb, 0);
		tls_sb.addDep(0, ss_sb, 0);

		// Init scene handler

		sh = new SceneHandler();
		sh.setScene(ss_sb);
		root.getChildren().add(sh.getActive().root);
		root.getChildren().add(tb.root);

		Scene scene = new Scene(root, 1366, 768); // Sets up scene

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				sh.mousePressed(event.getSceneX(), event.getSceneY());
			}
		});

		scene.setOnTouchPressed(new EventHandler<TouchEvent>() {

			@Override
			public void handle(TouchEvent event) {
				sh.mousePressed(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY());
				System.out.println("IS DES TATSCH");
				touch_start = System.currentTimeMillis();
			}
		});

		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				sh.mouseReleased(event.getSceneX(), event.getSceneY());

			}

		});

		scene.setOnTouchReleased(new EventHandler<TouchEvent>() {

			@Override
			public void handle(TouchEvent event) {
				if (touch_start + touch_time >= System.currentTimeMillis()) {
					sh.mouseClick(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY());
					if (tb.mouseClick(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY()) == 0)
						sh.externalButton(0);
				}
				sh.mouseReleased(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY());
			}

		});

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() { // Handles mouse
																 // clicks
			@Override
			public void handle(MouseEvent event) {
				// calc_fr = !calc_fr;
				sh.mouseClick(event.getSceneX(), event.getSceneY());
				if (tb.mouseClick(event.getSceneX(), event.getSceneY()) == 0)
					sh.externalButton(0);
			}
		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() { // Handles mouse
																 // movement
			@Override
			public void handle(MouseEvent event) {
				sh.mouseMoved(event.getSceneX(), event.getSceneY());
			}
		});

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() { // Handles
																 // mouse
			// movement
			@Override
			public void handle(MouseEvent event) {
				sh.mouseDragged(event.getSceneX(), event.getSceneY());
			}
		});

		scene.setOnTouchMoved(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				sh.mouseDragged(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY());
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
					if (sh.getActive().equals(ss))
						tb.setBackButton(false);
					else
						tb.setBackButton(true);
					root.getChildren().set(0, sh.getActive().root);
				}
				last = now;
			}
		};

		at.start(); // Starts the animation launcher
	}

	public static void main(String[] args) {
		System.out.println("GUI started");
		launch(args);
	}

}