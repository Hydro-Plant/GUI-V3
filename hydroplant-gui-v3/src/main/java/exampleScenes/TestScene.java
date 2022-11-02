package exampleScenes;

import exampleSceneObjects.NewTimeLapseScroll;
import gui.Layout;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx.scene.paint.Color;
import javafx2.Rectangle2;
import sceneObjects.FlatLayout;

public class TestScene extends Scene {
	NewTimeLapseScroll ntls;

	Rectangle2 bg_rec;
	Rectangle2 second_rec;
	Layout l_rec;
	FlatLayout fl_rec;

	public TestScene() {
		bg_rec = new Rectangle2();
		bg_rec.setWidth2(2000);
		bg_rec.setHeight2(2000);
		bg_rec.setFill(Color.rgb(69, 69, 69));
		l_rec = new Layout();
		l_rec.addObject(bg_rec);
		fl_rec = new FlatLayout();
		fl_rec.setDesign(l_rec);
		addObject(fl_rec);

		second_rec = new Rectangle2();
		second_rec.setFill(Color.TRANSPARENT);
		second_rec.setStroke(Color.RED);
		second_rec.setStrokeWidth(10);

		second_rec.setX2(50);
		second_rec.setY2(100);
		second_rec.setWidth2(1200);
		second_rec.setHeight2(600);
		l_rec.addObject(second_rec);

		ntls = new NewTimeLapseScroll();
		ntls.setPosition(50, 100);
		ntls.setShape(1200, 600);
		ntls.setOutline(variables.height * constants.height_outline);
		addObject(ntls);
	}



	@Override
	public void update() {
		ntls.update();
	}

	@Override
	public int mouseClick(double mousex, double mousey) {
		ntls.mouseClick(mousex, mousey);
		return -1;
	}

	@Override
	public void mousePressed(double mousex, double mousey) {
		ntls.mousePressed(mousex, mousey);
	}

	@Override
	public void mouseReleased(double mousex, double mousey) {
		ntls.mouseReleased(mousex, mousey);
	}

	@Override
	public void mouseDragged(double mousex, double mousey) {
		ntls.mouseDragged(mousex, mousey);
	}
}
