package exampleSceneObjects;

import gui.constants;
import javafx.scene.paint.Color;
import javafx2.Rectangle2;
import sceneObjects.FlatLayout;
import standard.Positioning;

public class LoadingBar extends FlatLayout {
	final double gap_factor = 0.2;
	
	double factor;
	double width, height, outline;
	
	Rectangle2 background;
	Rectangle2 bar;
	
	public LoadingBar() {
		background = new Rectangle2();
		background.setStroke(constants.outline_col);
		background.setFill(Color.WHITE);
		bar = new Rectangle2();
		bar.setFill(Color.GREEN);
		bar.setPos(3);
		
		this.design.addObject(background);
		this.design.addObject(bar);
	}
	
	public void setFactor(double factor) {
		this.factor = factor;
		updateSize();
	}
	
	public void setPos(int positioning) {
		this.positioning = positioning;
		background.setPos(positioning);
		updateSize();
	}
	
	public void setOutline(double stroke) {
		background.setStrokeWidth(stroke);
		outline = stroke;
		updateSize();
	}
	
	public void setShape(double width, double height) {
		this.width = width;
		this.height = height;
		updateSize();
	} 
	
	public void updateSize() {
		background.setWidth2(width - outline);
		background.setHeight2(height - outline);
		
		bar.setX2(Positioning.positioning(positioning, 3)[0] * background.getWidth() + outline / 2 + (height - 2 * outline) * gap_factor / 2);
		bar.setY2((Positioning.positioning(positioning, 3)[1]) * background.getHeight());
		
		bar.setWidth2(((width - 2 * outline) - (height - 2 * outline) * gap_factor) * factor);
		bar.setHeight2((height - 2 * outline) * (1 - gap_factor));
	}
}
