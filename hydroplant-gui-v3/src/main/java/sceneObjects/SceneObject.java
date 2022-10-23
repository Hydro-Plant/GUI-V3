package sceneObjects;

import gui.IDCounter;
import javafx.scene.layout.Pane;

public class SceneObject {
	protected double positionx;
	protected double positiony;
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

	public void setPosition(double positionx, double positiony, int positioning) { // Objektposition wird intern verändert
		this.positionx = positionx;
		this.positiony = positiony;
		this.positioning = positioning;
		updatePosition();
	}

	public void setPosition(double positionx, double positiony) {
		this.positionx = positionx;
		this.positiony = positiony;
		updatePosition();
	}
	
	public double[] getPosition() {
		return new double[] {this.positionx, this.positiony};
	}

	protected void updatePosition() { // Kinder der Klasse können in diese Funktion die Positionsänderung verarbeiten

	}

	public void update() {

	}
}
