package sceneObjects;

import gui.Layout;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class Button extends SceneObject {
	private Node detector;
	boolean hovering = false;

	EventHandler<MouseEvent> entered;
	EventHandler<MouseEvent> exited;

	public Button() {
		entered = new EventHandler<>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Hovering: " + id);
				hovering = true;
			}
		};

		exited = new EventHandler<>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Not Hovering: " + id);
				hovering = false;
			}
		};
	}

	public void setDesign(Layout design) {
		if (this.getChildren().size() <= 0)
			this.getChildren().add(design);
		else
			this.getChildren().set(0, design);
	}

	public void setButtonShape(Node detection) {
		if (detector != null) {
			detector.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
			detector.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
		}
		detector = detection;
		detector.addEventHandler(MouseEvent.MOUSE_ENTERED, entered);
		detector.addEventHandler(MouseEvent.MOUSE_EXITED, exited);
	}

	public boolean isButtonPressed() {
		return hovering;
	}

	@Override
	public void update() {
		if (this.getChildren().get(0) != null)
			((Layout) this.getChildren().get(0)).update();
	}

	@Override
	public void toFront() {
		super.getChildren().get(0).toFront();
	}
}