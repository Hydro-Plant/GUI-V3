module org.openjfx.hydroplant_gui_v3 {
	exports exampleLayouts;
	exports exampleSceneObjects;
	exports standard;
	exports org.openjfx.hydroplant_gui_v3;
	exports javafx2;
	exports gui;
	exports poissonDisc;
	exports sceneObjects;
	exports exampleScenes;
	exports timelapse;

	requires com.google.gson;
	requires java.desktop;
	requires javafx.base;
	requires javafx.graphics;
	requires org.controlsfx.controls;
	requires org.eclipse.paho.client.mqttv3;
}