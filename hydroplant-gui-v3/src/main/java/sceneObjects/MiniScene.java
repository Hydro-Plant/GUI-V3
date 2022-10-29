package sceneObjects;

import java.util.ArrayList;

import gui.IDCounter;
import gui.Scene;
import javafx.scene.layout.Pane;

public class MiniScene extends SceneObject {
	protected ArrayList<SceneObject> scene_objects;
	protected int scene_event = -1;

	public MiniScene() {
		scene_objects = new ArrayList<SceneObject>();
	}
	
	public void addObject(SceneObject new_object) {
		pane.getChildren().add(new_object.getPane());
		this.scene_objects.add(new_object);
	}

	public void update() { // Wird jedes Frame ausgeführt
		for (int x = 0; x < scene_objects.size(); x++) {
			scene_objects.get(x).update();
		}
	}
	
	protected void updatePosition() { // Kinder der Klasse können in diese Funktion die Positionsänderung verarbeiten
		pane.setTranslateX(this.positionx);
		pane.setTranslateY(this.positiony);
	}
}
