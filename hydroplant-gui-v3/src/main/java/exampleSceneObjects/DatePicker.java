package exampleSceneObjects;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import exampleLayouts.ArrowLayout;
import exampleLayouts.DateButton;
import gui.Layout;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.Rectangle2;
import javafx2.Text2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;
import sceneObjects.MiniScene;
import standard.Positioning;

public class DatePicker extends MiniScene {
	private final double top_factor = 0.13;
	private final double top_text_factor = 0.7;
	private final double top_width_factor = 0.7;
	private final double outline_factor = 4;
	private final double date_outline_factor = 4;
	private final double date_button_factor = 0.7;
	private final double edge_gap_factor = 0.05;
	private final double gap_factor = 0.04;

	ArrayList<Button> number_button;

	double sizex, sizey;

	Rectangle2 backg;
	Rectangle2 top;
	Text2 month_year;

	Layout l;
	FlatLayout fl;

	Button next;
	Button last;
	ArrowLayout next_l;
	ArrowLayout last_l;

	LocalDate date;

	public DatePicker() {
		// ---------------------------- Background

		backg = new Rectangle2();
		top = new Rectangle2();
		month_year = new Text2();

		backg.setStroke(Color.WHITE);
		backg.setFill(Color.TRANSPARENT);
		top.setStroke(Color.WHITE);
		top.setFill(Color.TRANSPARENT);
		top.setPos(1);
		month_year.setHorizontalOrientation(TextAlignment.CENTER);
		month_year.setTextOrigin(VPos.CENTER);
		month_year.setFill(Color.WHITE);

		l = new Layout();
		l.addObject(backg);
		l.addObject(top);
		l.addObject(month_year);
		fl = new FlatLayout();
		fl.setDesign(l);

		// ---------------------------- Next & Last Button

		next = new Button();
		last = new Button();
		next_l = new ArrowLayout();
		next_l.setDirection(1);
		next_l.setPos(2);
		last_l = new ArrowLayout();
		last_l.setDirection(3);
		last_l.setPos(0);

		next.setDesign(next_l);
		next.setPos(2);
		last.setDesign(last_l);
		last.setPos(0);

		// ---------------------------- Date Buttons

		number_button = new ArrayList<>();
		for(int x = 0; x < 31; x++) {
			number_button.add(new Button());
			number_button.get(x).setDesign(new DateButton(x + 1));
			((DateButton) number_button.get(x).getDesign()).setPos(4);
			number_button.get(x).setPos(4);
		}

		// ---------------------------- Date

		date = LocalDate.now();
		updateDate();

		addObject(fl);
		addObject(next);
		addObject(last);

		for(int x = 0; x < 31; x++) {
			addObject(number_button.get(x));
		}
	}

	public void setDate(LocalDate newDate) {
		date = newDate;
		updateDate();
	}

	public void setOutline(double outline) {
		backg.setStrokeWidth(outline * outline_factor);
		top.setStrokeWidth(outline * outline_factor);
		next_l.setOutline(outline * outline_factor);
		last_l.setOutline(outline * outline_factor);
		for(int x = 0; x < 31; x++) {
			((DateButton)number_button.get(x).getDesign()).setOutline(outline * date_outline_factor);
		}
	}

	@Override
	public void setPos(int pos) {
		this.positioning = pos;

		updateShape();
	}

	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey;

		updateShape();
	}

	private void updateShape() {
		backg.setWidth2(sizex);
		backg.setHeight2(sizey);
		top.setWidth2(sizex);
		top.setHeight2(sizey * top_factor);
		top.setX2(Positioning.positioning(positioning, 1)[0] * sizex);
		top.setY2(Positioning.positioning(positioning, 1)[1] * sizey);
		month_year.setX2(Positioning.positioning(positioning, 1)[0] * sizex);
		month_year.setY2(Positioning.positioning(positioning, 1)[1] * sizey + sizey * top_factor / 2);
		month_year.setSize(sizey * top_factor * top_text_factor);

		next.setPosition(Positioning.positioning(positioning, 2)[0] * sizex, Positioning.positioning(positioning, 2)[1] * sizey);
		next.setShape(sizex * (1 - top_width_factor) / 2, sizey * top_factor);
		next_l.setShape(sizex * (1 - top_width_factor) / 2, sizey * top_factor);
		last.setPosition(Positioning.positioning(positioning, 0)[0] * sizex, Positioning.positioning(positioning, 0)[1] * sizey);
		last.setShape(sizex * (1 - top_width_factor) / 2, sizey * top_factor);
		last_l.setShape(sizex * (1 - top_width_factor) / 2, sizey * top_factor);

		for(int x = 0; x < 31; x++) {
			double edge_gap = sizey * (1 - top_factor) * edge_gap_factor;
			double gap = sizey * (1 - top_factor) * gap_factor;
			double date_height = (sizey * (1 - top_factor) - 2 * edge_gap - 4 * gap) / 5;
			double date_width = (sizex - 2 * edge_gap - 6 * gap) / 7;

			((DateButton) number_button.get(x).getDesign()).setShape(date_width, date_height);
			number_button.get(x).setShape(date_width, date_height);
			number_button.get(x).setPosition(date_width / 2 + edge_gap + (date_width + gap) * (x % 7), sizey * top_factor + edge_gap + date_height / 2 + Math.floor(x / 7) * (date_height + gap));
		}
	}

	public void reload() {
		date = LocalDate.now(ZoneId.of("+1"));
	}

	private void updateDate() {
		for(int x = 0; x < 31; x++) {
			((DateButton) number_button.get(x).getDesign()).setVisible(x < date.lengthOfMonth());
			((DateButton) number_button.get(x).getDesign()).setActive(x == date.getDayOfMonth() - 1);
		}

		month_year.setText2(String.format("%s %d", date.getMonth().toString(), date.getYear()));
	}

	@Override
	public void update() {
		for(int x = 0; x < 31; x++) {
			number_button.get(x).update();
		}
	}

	public void mouseClicked(double mousex, double mousey) {
		if(next.isPressed(mousex - this.positionx, mousey - this.positiony)) {
			date = date.plusMonths(1);
			updateDate();
		}
		if(last.isPressed(mousex - this.positionx, mousey - this.positiony)) {
			date = date.minusMonths(1);
			updateDate();
		}
		for(int x = 0; x < date.lengthOfMonth(); x++) {
			if(number_button.get(x).isPressed(mousex - this.positionx, mousey - this.positiony)) {
				date = LocalDate.of(date.getYear(), date.getMonthValue(), x + 1);
				updateDate();
				break;
			}
		}
	}

	public LocalDate getLocalDate() {
		return date;
	}
}
