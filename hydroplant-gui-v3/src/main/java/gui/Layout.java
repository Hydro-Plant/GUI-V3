package gui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Layout {
	Pane pane;
	protected double positionx, positiony;
	protected int positioning;

	public Layout() {
		pane = new Pane();
	}

	public void setLayoutPosition(double positionx2, double positiony2) {
		positionx = positionx2;
		positiony = positiony2;
		pane.setTranslateX(positionx2);
		pane.setTranslateY(positiony2);
	}

	public void addObject(Node new_object) {
		pane.getChildren().add(new_object);
	}

	public Pane getPane() {
		return pane;
	}

	public void update() { // Executes every frame

	}

	public void toFront() {
		for (int x = 0; x < pane.getChildren().size(); x++) {
			pane.toFront();
		}
	}

	protected void toBack() {
		for (int x = 0; x < pane.getChildren().size(); x++) {
			pane.toBack();
		}
	}
}
