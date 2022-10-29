package exampleLayouts;

import gui.Layout;
import gui.constants;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.Rectangle2;
import javafx2.Text2;
import standard.Positioning;

public class RemoveLayout extends Layout {
	private final double gap_factor = 0.4;
	private final double width_gap_factor = 2;
	private final double arc_height_factor = 0.3;
	
	private double size = 10;
	
	Text2 remove;
	Rectangle2 backg;
	
	public RemoveLayout() {
		remove = new Text2();
		backg = new Rectangle2();
		
		remove.setText2("Entfernen");
		remove.setHorizontalOrientation(TextAlignment.CENTER);
		remove.setTextOrigin(VPos.CENTER);
		remove.setFill(Color.BLACK);
		
		backg.setFill(Color.RED);
		backg.setStroke(constants.outline_col);
		
		addObject(backg);
		addObject(remove);
	}
	
	public void setSize(double size) {
		this.size = size;
		updateShape();
	}
	
	public void setOutline(double outline) {
		backg.setStrokeWidth(outline);
	}
	
	public double getWidth() {
		return backg.getWidth();
	}
	
	public double getHeight() {
		return backg.getHeight();
	}
	
	protected void updatePosition() {
		updateShape();
	}
	
	private void updateShape() {
		remove.setSize(size);
		
		backg.setPos(positioning);
		backg.setWidth2(remove.getBoundsInLocal().getWidth() + size * gap_factor * width_gap_factor);
		backg.setHeight2(remove.getBoundsInLocal().getHeight() + size * gap_factor);
		backg.setArcHeight(backg.getHeight() * arc_height_factor);
		backg.setArcWidth(backg.getHeight() * arc_height_factor);
		
		remove.setX2(Positioning.positioning(positioning, 4)[0] * backg.getWidth());
		remove.setY2(Positioning.positioning(positioning, 4)[1] * backg.getHeight());
	}
}
