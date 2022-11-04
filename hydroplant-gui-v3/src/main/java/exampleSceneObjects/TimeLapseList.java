package exampleSceneObjects;

import java.util.ArrayList;

import javafx2.Rectangle2;
import sceneObjects.MiniScene;
import standard.Deckel;

public class TimeLapseList extends MiniScene {
	private double tl_height = 200;
	private double tl_gap = 20;
	private double height = 0, width = 0;
	private double outline = 0;

	private boolean moving = false;
	private double last_mousey = 0;
	private double max_scroll = 0;
	private double scroll = 0;

	MiniScene scene;
	Rectangle2 clip;

	ArrayList<TimeLapse> tl;

	public TimeLapseList() {
		scene = new MiniScene();
		clip = new Rectangle2();
		tl = new ArrayList<>();
		scene.getPane().setClip(clip);

		this.addObject(scene);
	}

	public void setOutline(double outline) {
		this.outline = outline;
		reload();
	}

	public void setTLHeight(double height) {
		if (this.tl_height != height) {
			moving = false;
			max_scroll = tl.size() * tl_height + (tl.size() - 1) * tl_gap - this.height;
			if (max_scroll < 0)
				max_scroll = 0;
			scroll = 0;
		}
		this.tl_height = height;
		reload();
	}

	public void setTLGap(double gap) {
		if (this.tl_gap != gap) {
			moving = false;
			max_scroll = tl.size() * tl_height + (tl.size() - 1) * tl_gap - this.height;
			if (max_scroll < 0)
				max_scroll = 0;
			scroll = 0;
		}
		this.tl_gap = gap;
		reload();
	}

	public void setShape(double width, double height) {
		if (this.height != height || this.width != width) {
			moving = false;
			max_scroll = tl.size() * tl_height + (tl.size() - 1) * tl_gap - height;
			if (max_scroll < 0)
				max_scroll = 0;
			scroll = 0;
		}

		this.height = height;
		this.width = width;

		clip.setWidth2(width);
		clip.setHeight2(height);

		reload();
	}

	public void setTimeLapse(ArrayList<TimeLapse> tl) {
		this.tl = tl;
		reloadArray();
	}

	public void addTimeLapse(TimeLapse tl) {
		this.tl.add(tl);
		reloadArray();
	}

	private void reloadArray() {
		moving = false;
		max_scroll = tl.size() * tl_height + (tl.size() - 1) * tl_gap - this.height;
		if (max_scroll < 0)
			max_scroll = 0;
		scroll = 0;

		scene.removeAll();
		for (TimeLapse element : tl) {
			scene.addObject(element);
		}
		reload();
	}

	public double[] getShape() {
		return new double[] {width, height};
	}
	
	private void reload() {
		for (int x = 0; x < tl.size(); x++) {
			this.tl.get(x).setShape(width, tl_height);
			this.tl.get(x).setPosition(0, x * tl_height + x * tl_gap);
			this.tl.get(x).setOutline(outline);
		}
	}

	public int mouseClicked(double mousex, double mousey) {
		for (TimeLapse timel : tl) {
			if (mousex - this.positionx >= 0 && mousey - this.positiony >= 0 && mousex - this.positionx <= width && mousey - this.positiony <= height) {
				if (timel.isPressed(mousex - this.positionx, mousey - this.positiony - scene.getPosition()[1])) {
					return timel.getId();
				}
			}
		}
		return -1;
	}

	public void mousePressed(double mousex, double mousey) {
		double real_mousex = mousex - this.positionx;
		double real_mousey = mousey - this.positiony;

		if (tl.size() > 0) {
			if (real_mousex >= 0 && real_mousex <= tl.get(0).getDragWidth() && real_mousey >= 0
					&& real_mousey <= height) {
				last_mousey = mousey;
				moving = true;
			}
		}
	}

	public void mouseReleased(double mousex, double mousey) {
		moving = false;
	}

	public void mouseDragged(double mousex, double mousey) {
		if (moving) {
			scroll += last_mousey - mousey;
			scroll = Deckel.deckel(scroll, 0, max_scroll);
			last_mousey = mousey;
		}
	}

	@Override
	public void update() {
		for (int x = 0; x < tl.size(); x++) {
			this.tl.get(x).update();
		}
		scene.setPosition(0, -scroll);
		clip.setY2(scroll);
	}
}
