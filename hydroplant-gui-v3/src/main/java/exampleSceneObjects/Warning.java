package exampleSceneObjects;

import exampleLayouts.WarningLayout;
import gui.variables;
import javafx2.ImageView2;
import sceneObjects.Button;
import standard.Bezier;
import standard.Vector;

public class Warning extends Button {
	WarningLayout wl;

	final double moving_speed = 1;
	final double[] bezier_space = { 0.3, 0.3 };

	double or_x = 0;
	double or_y = 0;
	double la_x = 0;
	double la_y = 0;

	double pos_factor = 0;
	boolean moving = false;
	boolean status = false;

	boolean active = false;

	public Warning() {
		wl = new WarningLayout();
		super.setPosition(0, 0);
		setDesign(wl);
		setActive(false);
		ImageView2 sign = wl.getSign();
		super.setButtonShape(sign);
	}

	public void setSize(double size) {
		wl.setSize(size);
	}

	public void setRectangle(double width) {
		wl.setRectangle(width);
	}

	public void setText(String text) {
		wl.setText(text);
	}

	public void setOutline(double size) {
		wl.setOutline(size);
	}

	public int getRealStatus() {
		return wl.getRealStatus();
	}

	public void setStatus(boolean open) {
		if (open) {
			wl.setStatus(open);
		} else {
			wl.setStatus(false);
			moving = true;
			status = false;
		}
	}

	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			wl.setAlpha(true);
		} else {
			wl.setStatus(false);
			moving = true;
			status = false;
		}
	}

	public void setOrigin(double posx, double posy) {
		or_x = posx;
		or_y = posy;
		if (pos_factor == 0) {
			super.setPosition(posx, posy);
		}
	}

	public void setSelectedPos(double posx, double posy) {
		la_x = posx;
		la_y = posy;
		if (pos_factor == 1) {
			super.setPosition(posx, posy);
		}
	}

	@Override
	public boolean isButtonPressed() {
		if (active) {
			if (super.isButtonPressed()) {
				moving = true;
				status = true;
				super.toFront();
				return true;
			}
		}
		return false;
	}

	@Override
	public void update() {
		if (moving) {
			if (status) {
				pos_factor += moving_speed / variables.frameRate;
				if (pos_factor >= 1) {
					pos_factor = 1;
					wl.setStatus(true);
					moving = false;
				}
			} else if (wl.getRealStatus() == 0) {
				pos_factor -= moving_speed / variables.frameRate;
				if (pos_factor <= 0) {
					pos_factor = 0;
					moving = false;
					wl.setAlpha(active);
				}
			}

			double bez_factor = Bezier.bezier_curve_2d(pos_factor, new Vector(bezier_space[0], 0),
					new Vector(1 - bezier_space[1], 1)).y;

			super.setPosition(la_x * bez_factor + or_x * (1 - bez_factor),
					(la_y + wl.getHeight() / 2) * bez_factor + or_y * (1 - bez_factor));
		}
		super.update();
	}
}
