package sceneObjects;

import java.util.ArrayList;

public class MiniScene extends SceneObject {
	protected ArrayList<SceneObject> scene_objects;
	protected int scene_event = -1;

	public MiniScene() {
		scene_objects = new ArrayList<>();
	}

	public void addObject(SceneObject new_object) {
		pane.getChildren().add(new_object.getPane());
		this.scene_objects.add(new_object);
	}

	@Override
	public void update() { // Wird jedes Frame ausgeführt
		for (SceneObject scene_object : scene_objects) {
			scene_object.update();
		}
	}

	public void removeAll() {
		scene_objects.clear();
		pane.getChildren().clear();
	}

	@Override
	protected void updatePosition() { // Kinder der Klasse können in diese Funktion die Positionsänderung verarbeiten
		pane.setTranslateX(this.positionx);
		pane.setTranslateY(this.positiony);
	}
}
