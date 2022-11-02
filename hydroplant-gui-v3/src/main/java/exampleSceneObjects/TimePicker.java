package exampleSceneObjects;

import java.sql.Date;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import gui.Layout;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.Circle2;
import javafx2.Text2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;
import sceneObjects.MiniScene;
import standard.Positioning;
import standard.Vector;

public class TimePicker extends MiniScene {
	private final double hours_am_factor = 0.85;
	private final double hours_pm_factor = 0.55;
	private final double text_factor = 0.10;
	private final double top_text_factor = 0.15;
	private final double circle_factor = 0.07;

	private double sizex, sizey;
	private boolean hour_selection = true;
	private boolean choosing = false;
	// boolean changing
	private LocalTime time;

	private Text2 hour;
	private Text2 double_point;
	private Text2 minute;

	private Button hour_btn;
	private Layout hour_lyt;
	private Button minute_btn;
	private Layout minute_lyt;

	private ArrayList<Text2> hours;
	private ArrayList<Text2> minutes;
	private Circle2 backg;

	private Button selection_btn;
	private Layout selection_lyt;
	private Circle2 selection;

	private Layout l;
	private FlatLayout fl;

	private Layout numbs_l;
	private FlatLayout numbs_fl;

	public TimePicker() {
		hour = new Text2();
		hour.setHorizontalOrientation(TextAlignment.RIGHT);
		hour.setTextOrigin(VPos.TOP);
		hour.setFill(Color.WHITE);
		hour.setX2(0);
		hour.setY2(0);

		hour_lyt = new Layout();
		hour_lyt.addObject(hour);
		hour_btn = new Button();
		hour_btn.setDesign(hour_lyt);
		hour_btn.setPos(2);

		double_point = new Text2(":");
		double_point.setHorizontalOrientation(TextAlignment.CENTER);
		double_point.setTextOrigin(VPos.TOP);
		double_point.setFill(Color.WHITE);

		minute = new Text2();
		minute.setHorizontalOrientation(TextAlignment.LEFT);
		minute.setTextOrigin(VPos.TOP);
		minute.setFill(Color.WHITE);
		minute.setX2(0);
		minute.setY2(0);

		minute_lyt = new Layout();
		minute_lyt.addObject(minute);
		minute_btn = new Button();
		minute_btn.setDesign(minute_lyt);
		minute_btn.setPos(0);

		backg = new Circle2();
		backg.setFill(Color.rgb(46, 44, 71));
		backg.setPos(4);

		selection = new Circle2();
		selection.setFill(Color.ROYALBLUE);
		selection.setPos(4);
		selection_lyt = new Layout();
		selection_lyt.addObject(selection);
		selection_btn = new Button();
		selection_btn.setDesign(selection_lyt);
		selection_btn.setPos(4);

		hours = new ArrayList<>();
		for (int x = 0; x < 24; x++) {
			hours.add(new Text2(String.format("%02d", x)));
			hours.get(x).setHorizontalOrientation(TextAlignment.CENTER);
			hours.get(x).setTextOrigin(VPos.CENTER);
			hours.get(x).setFill(Color.WHITE);
		}
		minutes = new ArrayList<>();
		for (int x = 0; x < 12; x++) {
			minutes.add(new Text2(String.format("%02d", x * 5)));
			minutes.get(x).setHorizontalOrientation(TextAlignment.CENTER);
			minutes.get(x).setTextOrigin(VPos.CENTER);
			minutes.get(x).setFill(Color.WHITE);
		}

		l = new Layout();
		l.addObject(backg);
		l.addObject(double_point);

		numbs_l = new Layout();

		for (int x = 0; x < 24; x++) {
			numbs_l.addObject(hours.get(x));
		}
		for (int x = 0; x < 12; x++) {
			numbs_l.addObject(minutes.get(x));
		}

		time = LocalTime.now(ZoneId.of("+1"));
		if ((int) (Math.ceil(time.getMinute() * 0.2) / 0.2) < 60) {
			time = LocalTime.of(time.getHour(), (int) (Math.ceil(time.getMinute() * 0.2) / 0.2));
		} else {
			if (time.getHour() + 1 > 23) {
				time = LocalTime.of(0, 0);
			} else {
				time = LocalTime.of(time.getHour() + 1, 0);
			}
		}
		updateTime();

		fl = new FlatLayout();
		fl.setDesign(l);

		numbs_fl = new FlatLayout();
		numbs_fl.setDesign(numbs_l);

		addObject(fl);
		addObject(selection_btn);
		addObject(numbs_fl);
		addObject(hour_btn);
		addObject(minute_btn);
	}

	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey;

		updateShape();
	}

	@Override
	public void setPos(int pos) {
		this.positioning = pos;
		updateShape();
	}

	public void reload() {
		time = LocalTime.now(ZoneId.of("+1"));
		if ((int) (Math.ceil(time.getMinute() * 0.2) / 0.2) < 60) {
			time = LocalTime.of(time.getHour(), (int) (Math.ceil(time.getMinute() * 0.2) / 0.2));
		} else {
			if (time.getHour() + 1 > 23) {
				time = LocalTime.of(0, 0);
			} else {
				time = LocalTime.of(time.getHour() + 1, 0);
			}
		}
		updateTime();
	}

	public void setTime(LocalTime new_time) {
		time = new_time;
		updateTime();
	}

	public LocalTime getTime() {
		return time;
	}

	private void updateTime() {
		if (hour_selection) {
			for (int x = 0; x < 12; x++) {
				minutes.get(x).setFill(Color.TRANSPARENT);
			}
			for (int x = 0; x < 24; x++) {
				hours.get(x).setFill(Color.WHITE);
			}

			hour.setFill(Color.SKYBLUE);
			minute.setFill(Color.WHITE);

			selection_btn.setPosition(hours.get(time.getHour()).getX2(), hours.get(time.getHour()).getY2());

		} else {
			for (int x = 0; x < 24; x++) {
				hours.get(x).setFill(Color.TRANSPARENT);
			}
			for (int x = 0; x < 12; x++) {
				minutes.get(x).setFill(Color.WHITE);
			}

			hour.setFill(Color.WHITE);
			minute.setFill(Color.SKYBLUE);

			selection_btn.setPosition(minutes.get(time.getMinute() / 5).getX2(),
					minutes.get(time.getMinute() / 5).getY2());
		}

		hour.setText2(String.format("%02d", time.getHour()));
		minute.setText2(String.format("%02d", time.getMinute()));
	}

	private void updateShape() {
		double circle_pos_y = sizey - sizex / 2;

		backg.setX2(Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2);
		backg.setY2(Positioning.positioning(positioning, 0)[1] * sizey + circle_pos_y);
		backg.setRadius2(sizex / 2);

		for (int x = 0; x < 24; x++) {
			if (x >= 1 && x <= 12) {
				hours.get(x).setX2(Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2
						+ (sizex / 2) * hours_am_factor * Math.cos(2 * Math.PI / 12 * (x - 3)));
				hours.get(x).setY2(Positioning.positioning(positioning, 0)[1] * sizey + circle_pos_y
						+ (sizex / 2) * hours_am_factor * Math.sin(2 * Math.PI / 12 * (x - 3)));
			} else {
				hours.get(x).setX2(Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2
						+ (sizex / 2) * hours_pm_factor * Math.cos(2 * Math.PI / 12 * (x - 3)));
				hours.get(x).setY2(Positioning.positioning(positioning, 0)[1] * sizey + circle_pos_y
						+ (sizex / 2) * hours_pm_factor * Math.sin(2 * Math.PI / 12 * (x - 3)));
			}
			hours.get(x).setSize(text_factor * sizex);
		}

		for (int x = 0; x < 12; x++) {
			minutes.get(x).setX2(Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2
					+ (sizex / 2) * hours_am_factor * Math.cos(2 * Math.PI / 12 * (x - 3)));
			minutes.get(x).setY2(Positioning.positioning(positioning, 0)[1] * sizey + circle_pos_y
					+ (sizex / 2) * hours_am_factor * Math.sin(2 * Math.PI / 12 * (x - 3)));
			minutes.get(x).setSize(text_factor * sizex);
		}

		selection.setRadius2(circle_factor * sizex);
		selection_btn.setShape(circle_factor * sizex * 2, circle_factor * sizex * 2);

		hour.setSize(sizey * top_text_factor);
		double_point.setSize(sizey * top_text_factor);
		minute.setSize(sizey * top_text_factor);

		hour_btn.setPosition(
				Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2 - double_point.getWidth() / 2,
				Positioning.positioning(positioning, 0)[1] * sizey);
		double_point.setX2(Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2);
		double_point.setY2(Positioning.positioning(positioning, 0)[1] * sizey);
		minute_btn.setPosition(
				Positioning.positioning(positioning, 0)[0] * sizex + sizex / 2 + double_point.getWidth() / 2,
				Positioning.positioning(positioning, 0)[1] * sizey);

		hour_btn.setShape(hour.getWidth(), hour.getFont().getSize());
		minute_btn.setShape(minute.getWidth(), minute.getFont().getSize());

		updateTime();
	}

	public void mouseClick(double mousex, double mousey) {
		if (hour_btn.isPressed(mousex - this.positionx, mousey - this.positiony)) {
			hour_selection = true;
			updateTime();
		} else if (minute_btn.isPressed(mousex - this.positionx, mousey - this.positiony)) {
			hour_selection = false;
			updateTime();
		}
	}

	public void mousePressed(double mousex, double mousey) {
		if (selection_btn.isPressed(mousex - this.positionx, mousey - this.positiony)) {
			choosing = true;
		}
	}

	public void mouseDragged(double mousex, double mousey) {
		if (choosing) {
			Vector mouse = new Vector(mousex - this.positionx, mousey - this.positiony);
			Vector circle = new Vector(backg.getCenterX(), backg.getCenterY());
			double angle = (circle.sub(mouse).getAngle() + (4 - 0.5 + ((double) 1 / 12)) * Math.PI) % (2 * Math.PI);

			if (hour_selection) {
				if (mouse.sub(circle).getMag() >= sizex / 2 * (hours_am_factor + hours_pm_factor) / 2) {
					for (int x = 0; x < 12; x++) {
						if (angle <= (x + 1) * 2 * Math.PI / 12) {
							if (x == 0)
								x = 12;
							time = LocalTime.of(x, time.getMinute());
							updateTime();
							break;
						}
					}
				} else {
					for (int x = 0; x < 12; x++) {
						if (angle <= (x + 1) * 2 * Math.PI / 12) {
							if (x == 0)
								x = -12;
							time = LocalTime.of(x + 12, time.getMinute());
							updateTime();
							break;
						}
					}
				}
			} else {
				for (int x = 0; x < 12; x++) {
					if (angle <= (x + 1) * 2 * Math.PI / 12) {
						time = LocalTime.of(time.getHour(), x * 5);
						updateTime();
						break;
					}
				}
			}
		}
	}

	public void mouseReleased(double mousex, double mousey) {
		if (choosing) {
			choosing = false;
			if (hour_selection)
				hour_selection = false;
			updateTime();
		}
	}

	@Override
	public void update() {
		// if(changing) {

		// }
	}
}
