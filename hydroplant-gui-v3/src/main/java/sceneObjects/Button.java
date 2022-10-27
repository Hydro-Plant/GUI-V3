package sceneObjects;

import gui.Layout;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx2.Rectangle2;
import standard.Positioning;

public class Button extends SceneObject {
	public Layout design;
	private Pane detector;
	private Node second_detector;
	private boolean hovering;
	
	private EventHandler<MouseEvent> entered = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			System.out.println("Hovering: " + id);
			hovering = true;
		}
	};
	
	private EventHandler<MouseEvent> exited = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			System.out.println("Not Hovering: " + id);
			hovering = false;
		}
	};
	
	public Button() {
		this.positionx = 0;
		this.positiony = 0;
		this.positioning = 0;
	}

	public void setShape(Node new_detector) {
		if(detector != null) {
			detector.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
			detector.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
			detector.setMouseTransparent(true);
			detector = null;
		}
		if(second_detector != null) {
			second_detector.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
			second_detector.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
			second_detector.setMouseTransparent(true);
			second_detector = null;
		}
		
		if(new_detector.getClass().equals(new Rectangle2().getClass())) {
			Rectangle2 det = ((Rectangle2) new_detector).copy();
			det.setFill(Color.RED);
			det.toFront();
			design.addObject(det);
			System.out.println("Cool node thingi");
		}
		
		second_detector = new_detector;
		second_detector.setMouseTransparent(false);
		second_detector.addEventHandler(MouseEvent.MOUSE_ENTERED, entered);
		second_detector.addEventHandler(MouseEvent.MOUSE_EXITED, exited);
	}
	
	public void setShape(Pane new_detector) {
		if(detector != null) {
			detector.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
			detector.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
			detector.setMouseTransparent(true);
			detector = null;
		}
		if(second_detector != null) {
			second_detector.removeEventHandler(MouseEvent.MOUSE_ENTERED, entered);
			second_detector.removeEventHandler(MouseEvent.MOUSE_EXITED, exited);
			second_detector.setMouseTransparent(true);
			second_detector = null;
		}
		detector = new_detector;
		detector.setMouseTransparent(false);
		detector.addEventHandler(MouseEvent.MOUSE_ENTERED, entered);
		detector.addEventHandler(MouseEvent.MOUSE_EXITED, exited);
	}

	public void setDesign(Layout design) {
		this.design = design;
		pane = this.design.getPane();
		this.design.setLayoutPosition(positionx, positiony);
		System.out.println(id + ": " + design.getClass());
	}

	public boolean isPressed(double x, double y) {
		return hovering;
	}

	protected void updatePosition() {
		if (design != null)
			design.setLayoutPosition(positionx, positiony);
	}

	public void update() {
		if (design != null)
			design.update();
	}
	
	public void toFront() {
		design.toFront();
	}
}