package exampleSceneObjects;

import gui.Layout;
import gui.constants;
import gui.variables;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;
import sceneObjects.MiniScene;
import standard.Positioning;
import timelapse.TimeLapseData;

public class NewTimeLapse extends MiniScene {
	private final double extension_width_factor = 0.15;
	private final double extension_height_factor = 0.1;
	private final double arc_factor = 0.1;
	private final double outline_factor = 4;
	
	private final double ntls_height_factor = 0.85;
	private final double ntls_width_factor = 1;
	
	private final double img_factor = 0.8;
	
	NewTimeLapseScroll ntls;
	
	Rectangle2 backg;
	Rectangle2 extension;
	ImageView2 plus_minus;
	
	Layout l;
	FlatLayout fl;
	
	Button pm;
	Layout pm_l;
	
	double sizex, sizey;
	
	boolean active = false;
	
	public NewTimeLapse() {
		backg = new Rectangle2();
		backg.setFill(Color.rgb(120, 120, 120));
		backg.setStroke(constants.outline_col);
		
		extension = new Rectangle2();
		extension.setFill(Color.rgb(120, 120, 120));
		extension.setStroke(constants.outline_col);
		extension.setPos(2);
		
		plus_minus = new ImageView2();
		plus_minus.setImage2(new Image("file:pics/plus.png"));
		plus_minus.setPos(4);
		plus_minus.setPreserveRatio2(true);
		
		ntls = new NewTimeLapseScroll();
		
		pm_l = new Layout();
		pm_l.addObject(plus_minus);
		pm = new Button();
		pm.setDesign(pm_l);
		pm.setPos(4);
		
		l = new Layout();
		l.addObject(extension);
		l.addObject(backg);
		fl = new FlatLayout();
		fl.setDesign(l);
		
		addObject(fl);
		addObject(ntls);
		addObject(pm);
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setMode(boolean plus) {
		if(plus) {
			plus_minus.setImage2(new Image("file:pics/plus.png"));
		}else {
			plus_minus.setImage2(new Image("file:pics/minus.png"));
		}
	}
	
	public void setOutline(double outline) {
		ntls.setOutline(outline);
		backg.setStrokeWidth(outline * outline_factor);
		extension.setStrokeWidth(outline * outline_factor);
	}
	
	public void setPos(int pos) {
		super.setPos(pos);
		updateShape();
	}
	
	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey / (1 + extension_height_factor);
		
		updateShape();
	}
	
	private void updateShape() {
		backg.setWidth2(sizex);
		backg.setHeight2(sizey);
		backg.setPos(this.positioning);
		backg.setArcHeight(sizey * arc_factor);
		backg.setArcWidth(sizey * arc_factor);
		
		extension.setHeight2(sizey * (1 + extension_height_factor));
		extension.setWidth2(sizex * extension_width_factor);
		extension.setX2(Positioning.positioning(positioning, 2)[0] * sizex);
		extension.setY2(Positioning.positioning(positioning, 2)[1] * sizey);
		extension.setArcHeight(sizey * arc_factor);
		extension.setArcWidth(sizey * arc_factor);
		
		plus_minus.setFitHeight2(sizey * extension_height_factor * img_factor);
		pm.setPosition(Positioning.positioning(positioning, 8)[0] * sizex - sizex * extension_width_factor / 2, Positioning.positioning(positioning, 8)[1] * sizey + sizey * extension_height_factor / 2);
		pm.setShape(sizex * extension_width_factor, sizey * extension_height_factor);
		
		ntls.setShape(sizex * ntls_width_factor, sizey * ntls_height_factor);
		ntls.setPosition(Positioning.positioning(positioning, 0)[0] * sizex + sizex * 0.5 * (1 - ntls_width_factor), Positioning.positioning(positioning, 0)[1] * sizey + sizey * 0.5 * (1 - ntls_height_factor));
	}
	
	public double getRealHeight() {
		return sizey;
	}
	
	public void reload() {
		ntls.reload();
	}
	
	public TimeLapseData getTimeLapse() {
		return ntls.getTimeLapse();
	}
	
	@Override
	public void update() {
		ntls.update();
	}

	public int mouseClick(double mousex, double mousey) {
		if(pm.isPressed(mousex - this.positionx, mousey - this.positiony)) return 1;
		if(active) return ntls.mouseClick(mousex - this.positionx, mousey - this.positiony);
		return -1;
	}

	public void mousePressed(double mousex, double mousey) {
		if(active) ntls.mousePressed(mousex - this.positionx, mousey - this.positiony);
	}

	public void mouseReleased(double mousex, double mousey) {
		if(active) ntls.mouseReleased(mousex - this.positionx, mousey - this.positiony);
	}

	public void mouseDragged(double mousex, double mousey) {
		if(active) ntls.mouseDragged(mousex - this.positionx, mousey - this.positiony);
	}
}
