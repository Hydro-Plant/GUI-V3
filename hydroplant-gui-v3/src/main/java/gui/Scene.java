package gui;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import sceneObjects.SceneObject;

public class Scene {
	protected ArrayList<SceneObject> scene_objects;
	protected int scene_event = -1;
	private Pane root;

	protected long id;

	public Scene() {
		scene_objects = new ArrayList<>();
		this.id = IDCounter.getSceneObjectID();
	}

	public boolean equals(Scene other) {
		return other.id == this.id;
	}

	public void addObject(SceneObject new_object) {
		super.getChildren().add(new_object);
		this.scene_objects.add(new_object);
	}

	public void loadMode(int mode) {

	}

	public int sceneEvents() {
		int res = scene_event;
		scene_event = -1;
		return res;
	}

	public void externalButton(int button) { // Wird ausgeführt, wenn ein Knopf, außerhalb der Szene gedrückt wurde

	}

	public int mouseClick() { // Wird ausgeführt, wenn die Maus gedrückt wird
		return -1;
	}

	public void updateSize() { // Wird ausgeführt, wenn sich die Größe der Stage ändert

	}

	@Override
	public void toFront() {
		for (SceneObject scene_object : scene_objects) {
			scene_object.toFront();
		}
	}

	public void update() { // Wird jedes Frame ausgeführt
		for (SceneObject scene_object : scene_objects) {
			scene_object.update();
		}
	}
}
