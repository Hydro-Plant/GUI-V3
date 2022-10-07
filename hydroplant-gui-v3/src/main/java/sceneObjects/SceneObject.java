package sceneObjects;

import gui.IDCounter;
import javafx.scene.layout.Pane;

public class SceneObject {
	protected int positionx;
	protected int positiony;
	protected int positioning;

	long id;
	Pane pane;

	public SceneObject() {
		pane = new Pane();
		this.positionx = 0;
		this.positiony = 0;
		this.positioning = 0;
		this.id = IDCounter.getSceneObjectID();
	}

	public SceneObject(int positionx, int positiony) {
		pane = new Pane();
		this.positionx = positionx;
		this.positiony = positiony;
		updatePosition();
	}

	public Pane getPane() {
		return pane;
	}

	public boolean equals(SceneObject other) {
		return id == other.id;
	}

	public void position(int positionx, int positiony, int positioning) { // Objektposition wird intern verändert
		this.positionx = positionx;
		this.positiony = positiony;
		this.positioning = positioning;
		updatePosition();
	}

	public void position(int positionx, int positiony) {
		this.positionx = positionx;
		this.positiony = positiony;
		updatePosition();
	}

	protected void updatePosition() { // Kinder der Klasse können in diese Funktion die Positionsänderung verarbeiten

	}

	public void update() {

	}
}
