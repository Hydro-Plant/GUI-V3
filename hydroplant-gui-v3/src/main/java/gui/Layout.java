package gui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Layout {
	protected double positionx, positiony;
	protected int positioning;

	public Layout() {
		super.setTranslateX(0);
		super.setTranslateX(0);
	}

	public void addObject(Node new_object) {
		super.getChildren().add(new_object);
	}

	public void update() { // Executes every frame

	}
}
