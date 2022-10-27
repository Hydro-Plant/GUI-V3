package sceneObjects;

import gui.IDCounter;
import javafx.scene.layout.Pane;

public class SceneObject {
	long id;

	public SceneObject() {
		this.id = IDCounter.getSceneObjectID();
	}

	public SceneObject(int positionx, int positiony) {
		updatePosition();
	}

	public boolean equals(SceneObject other) {
		return id == other.id;
	}

	public void setPosition(double positionx, double positiony) { // Objektposition wird intern verändert
		super.setTranslateX(positionx);
		super.setTranslateY(positiony);
		updatePosition();
	}

	public double[] getPosition() {
		return new double[] { super.getTranslateX(), super.getTranslateY() };
	}

	@Override
	public void toFront() {

	}

	protected void updatePosition() { // Kinder der Klasse können in diese Funktion die Positionsänderung verarbeiten

	}

	public void update() {

	}
}
