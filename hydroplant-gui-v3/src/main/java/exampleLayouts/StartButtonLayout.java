package exampleLayouts;

import gui.Layout;
import gui.constants;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import standard.Positioning;

public class StartButtonLayout extends Layout {
	private final double image_factor = 0.8;
	private final double arc_factor = 0.2;
	private final double outline_factor = 15;
	
	Rectangle2 backg;
	ImageView2 img;
	
	double sizex, sizey;
	
	public StartButtonLayout() {
		backg = new Rectangle2();
		backg.setStroke(constants.outline_col);
		backg.setFill(Color.SKYBLUE);
		
		img = new ImageView2();
		img.setPreserveRatio2(true);
		img.setPos(4);
		
		addObject(backg);
		addObject(img);
	}
	
	public void setOutline(double outline) {
		backg.setStrokeWidth(outline * outline_factor);
	}
	
	public void setImage(Image img) {
		this.img.setImage2(img);
	}
	
	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey;
		
		updateShape();
	}
	
	public void setPos(int pos) {
		super.setPos(pos);
		
		updateShape();
	}
	
	public void updateShape() {
		backg.setPos(positioning);
		backg.setWidth2(sizex);
		backg.setHeight2(sizey);
		backg.setArcHeight(arc_factor * sizey);
		backg.setArcWidth(arc_factor * sizey);
		
		img.setX2(Positioning.positioning(positioning, 4)[0] * sizex);
		img.setY2(Positioning.positioning(positioning, 4)[1] * sizey);
		img.setFitWidth2(sizex * image_factor);
		img.setFitHeight2(sizey * image_factor);
	}
}
