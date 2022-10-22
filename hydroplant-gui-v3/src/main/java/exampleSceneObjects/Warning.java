package exampleSceneObjects;

import exampleLayouts.WarningLayout;
import javafx.scene.paint.Color;
import sceneObjects.Button;

public class Warning extends Button {
	WarningLayout wl;
	public Warning() {
		wl = new WarningLayout();
		setDesign(wl);
	}
	
	public void setSize(double size) {
		wl.setSize(size);
	}
	
	public void setRectangle(double width) {
		wl.setRectangle(width);
	}
	
	public void setStatus(boolean status) {
		wl.setStatus(status);
	}
	
	public void setText(String text) {
		wl.setText(text);
	}
	
	public void setOutline(double size) {
		wl.setOutline(size);
	}
}
