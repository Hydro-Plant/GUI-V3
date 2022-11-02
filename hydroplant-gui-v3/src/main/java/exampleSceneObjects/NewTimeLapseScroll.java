package exampleSceneObjects;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;

import exampleLayouts.ArrowLayout;
import exampleLayouts.TextButtonLayout;
import gui.Layout;
import gui.variables;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import javafx2.Text2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;
import sceneObjects.MiniScene;
import standard.Bezier;
import standard.Vector;

public class NewTimeLapseScroll extends MiniScene {
	private final int max_mode = 2;

	private final double edge_factor = 0.1;
	private final double height_gap_factor = 0.05;
	private final double height_btn_factor = 0.1;
	private final double widht_btn_factor = 0.25;
	private final double height_title_factor = 0.15;
	private final double text_pos_factor = 0.2;

	private final double small_btn_factor = 0.05;
	private final double small_btn_gap_factor = 0.01;
	private final double value_text_factor = 0.10;
	private final double top_text_factor = 0.7;
	private final double edge_text_factor = 0.1;
	private final double mode_image_factor = 0.5;
	private final double mode_rec_outline = 4;
	private final double text_up = 0.1;
	
	private final double bez_factor = 0.3;
	private final double speed = 0.5;

	boolean speed_or_dur = false; 		// speed = false, duration = true
	boolean fps_or_pics = false;		// fps = false, pics = true

	double speed_value = 10;
	Duration duration_value = Duration.ofSeconds(100000);
	double fps_value = 60;
	int pictures_value = 420;

	double sizex, sizey;
	double normal_posx;

	// ---------- First

	DatePicker dp_from;
	TimePicker tp_from;
	Button next1;
	TextButtonLayout next1_l;
	Text2 start;

	// ---------- Second

	DatePicker dp_to;
	TimePicker tp_to;
	Button next2;
	TextButtonLayout next2_l;
	Button last2;
	TextButtonLayout last2_l;
	Text2 end;

	// ---------- Third

	int tl_mode = 0;
	ImageView2 tl_mode_img;
	Rectangle2 tl_mode_rec;
	ArrowLayout tl_mode_up_l;
	ArrowLayout tl_mode_down_l;
	Button tl_mode_up;
	Button tl_mode_down;

	Text2 speed_txt;
	Text2 speed_value_txt;
	Text2 length_txt;
	Text2 length_value_txt;
	Text2 framerate_txt;
	Text2 framerate_value_txt;
	Text2 pictures_txt;
	Text2 pictures_value_txt;

	TextButton speed_mm;
	TextButton speed_m;
	TextButton speed_p;
	TextButton speed_pp;

	TextButton length_m_h;
	TextButton length_m_m;
	TextButton length_m_s;
	TextButton length_p_s;
	TextButton length_p_m;
	TextButton length_p_h;

	TextButton frame_mm;
	TextButton frame_m;
	TextButton frame_p;
	TextButton frame_pp;

	TextButton pic_mm;
	TextButton pic_m;
	TextButton pic_p;
	TextButton pic_pp;

	Button finish3;
	TextButtonLayout finish3_l;
	Button last3;
	TextButtonLayout last3_l;

	Layout l;
	FlatLayout fl;

	boolean moving = false;
	int mode = 0;
	int last_mode = 0;
	double factor = 0;

	public NewTimeLapseScroll() {
		l = new Layout();
		fl = new FlatLayout();
		fl.setDesign(l);
		addObject(fl);

		// --------------------------------- First

		dp_from = new DatePicker();
		tp_from = new TimePicker();
		tp_from.setPos(2);

		next1_l = new TextButtonLayout("Weiter");
		next1_l.setPos(8);
		next1 = new Button();
		next1.setPos(8);
		next1.setDesign(next1_l);

		start = new Text2("Start");
		start.setFill(Color.WHITE);
		start.setTextOrigin(VPos.TOP);
		start.setY2(0);
		l.addObject(start);

		addObject(next1);
		addObject(dp_from);
		addObject(tp_from);

		// --------------------------------- Second

		dp_to = new DatePicker();
		tp_to = new TimePicker();
		tp_to.setPos(2);

		next2_l = new TextButtonLayout("Weiter");
		next2_l.setPos(8);
		next2 = new Button();
		next2.setPos(8);
		next2.setDesign(next2_l);

		last2_l = new TextButtonLayout("Zurück");
		last2_l.setPos(6);
		last2 = new Button();
		last2.setPos(6);
		last2.setDesign(last2_l);

		end = new Text2("Ende");
		end.setFill(Color.WHITE);
		end.setTextOrigin(VPos.TOP);
		end.setY2(0);
		l.addObject(end);

		addObject(next2);
		addObject(last2);
		addObject(dp_to);
		addObject(tp_to);

		// --------------------------------- Third

		speed_mm = new TextButton("--");
		speed_mm.setPos(4);
		speed_mm.setTextUp(text_up);
		speed_m = new TextButton("-");
		speed_m.setPos(4);
		speed_m.setTextUp(text_up);
		speed_p = new TextButton("+");
		speed_p.setPos(4);
		speed_p.setTextUp(text_up);
		speed_pp = new TextButton("++");
		speed_pp.setPos(4);
		speed_pp.setTextUp(text_up);

		length_m_h = new TextButton("-h");
		length_m_h.setPos(4);
		length_m_m = new TextButton("-m");
		length_m_m.setPos(4);
		length_m_s = new TextButton("-s");
		length_m_s.setPos(4);
		length_p_s = new TextButton("+s");
		length_p_s.setPos(4);
		length_p_m = new TextButton("+m");
		length_p_m.setPos(4);
		length_p_h = new TextButton("+h");
		length_p_h.setPos(4);

		frame_mm = new TextButton("--");
		frame_mm.setPos(4);
		frame_m = new TextButton("-");
		frame_m.setPos(4);
		frame_p = new TextButton("+");
		frame_p.setPos(4);
		frame_pp = new TextButton("++");
		frame_pp.setPos(4);

		pic_mm = new TextButton("--");
		pic_mm.setPos(4);
		pic_m = new TextButton("-");
		pic_m.setPos(4);
		pic_p = new TextButton("+");
		pic_p.setPos(4);
		pic_pp = new TextButton("++");
		pic_pp.setPos(4);

		tl_mode_img = new ImageView2();
		tl_mode_img.setPos(3);
		tl_mode_img.setImage2(new Image(String.format("file:pics/mode%02d.png", tl_mode)));
		tl_mode_img.setPreserveRatio2(true);
		tl_mode_rec = new Rectangle2();
		tl_mode_rec.setPos(3);
		tl_mode_rec.setStroke(Color.WHITE);
		tl_mode_rec.setFill(Color.TRANSPARENT);
		tl_mode_up_l = new ArrowLayout();
		tl_mode_down_l = new ArrowLayout();
		tl_mode_up = new Button();
		tl_mode_up.setDesign(tl_mode_up_l);
		tl_mode_down = new Button();
		tl_mode_down.setDesign(tl_mode_down_l);

		speed_txt = new Text2("Geschwindigkeit");
		speed_txt.setFill(Color.WHITE);
		speed_value_txt = new Text2();
		speed_value_txt.setFill(Color.WHITE);
		speed_value_txt.setTextOrigin(VPos.CENTER);
		length_txt = new Text2("Videolänge");
		length_txt.setFill(Color.WHITE);
		length_value_txt = new Text2();
		length_value_txt.setFill(Color.WHITE);
		length_value_txt.setTextOrigin(VPos.CENTER);
		framerate_txt = new Text2("Framerate");
		framerate_txt.setFill(Color.WHITE);
		framerate_value_txt = new Text2();
		framerate_value_txt.setFill(Color.WHITE);
		framerate_value_txt.setTextOrigin(VPos.CENTER);
		pictures_txt = new Text2("Bilderanzahl");
		pictures_txt.setFill(Color.WHITE);
		pictures_value_txt = new Text2();
		pictures_value_txt.setFill(Color.WHITE);
		pictures_value_txt.setTextOrigin(VPos.CENTER);

		finish3_l = new TextButtonLayout("Fertig");
		finish3_l.setPos(8);
		finish3 = new Button();
		finish3.setPos(8);
		finish3.setDesign(finish3_l);

		last3_l = new TextButtonLayout("Zurück");
		last3_l.setPos(6);
		last3 = new Button();
		last3.setPos(6);
		last3.setDesign(last3_l);

		l.addObject(tl_mode_img);
		l.addObject(tl_mode_rec);
		l.addObject(speed_txt);
		l.addObject(speed_value_txt);
		l.addObject(length_txt);
		l.addObject(length_value_txt);
		l.addObject(framerate_txt);
		l.addObject(framerate_value_txt);
		l.addObject(pictures_txt);
		l.addObject(pictures_value_txt);

		addObject(speed_mm);
		addObject(speed_m);
		addObject(speed_p);
		addObject(speed_pp);
		addObject(length_m_h);
		addObject(length_m_m);
		addObject(length_m_s);
		addObject(length_p_s);
		addObject(length_p_m);
		addObject(length_p_h);
		addObject(frame_mm);
		addObject(frame_m);
		addObject(frame_p);
		addObject(frame_pp);
		addObject(pic_mm);
		addObject(pic_m);
		addObject(pic_p);
		addObject(pic_pp);
		addObject(tl_mode_up);
		addObject(tl_mode_down);
		addObject(finish3);
		addObject(last3);
		
		reload();
	}

	public void setOutline(double outline) {
		dp_from.setOutline(outline);
		dp_to.setOutline(outline);
		tl_mode_rec.setStrokeWidth(outline * mode_rec_outline);
		
		speed_mm.setOutline(outline);
		speed_m.setOutline(outline);
		speed_p.setOutline(outline);
		speed_pp.setOutline(outline);

		length_m_h.setOutline(outline);
		length_m_m.setOutline(outline);
		length_m_s.setOutline(outline);
		length_p_s.setOutline(outline);
		length_p_m.setOutline(outline);
		length_p_h.setOutline(outline);

		frame_mm.setOutline(outline);
		frame_m.setOutline(outline);
		frame_p.setOutline(outline);
		frame_pp.setOutline(outline);

		pic_mm.setOutline(outline);
		pic_m.setOutline(outline);
		pic_p.setOutline(outline);
		pic_pp.setOutline(outline);
	}

	public void setShape(double sizex, double sizey) {
		this.sizex = sizex;
		this.sizey = sizey;

		updateShape();
	}

	public void recalculate() {
		long second_difference = dp_to.getLocalDate().toEpochSecond(tp_to.getTime(), ZoneOffset.of("+1"))
				- dp_from.getLocalDate().toEpochSecond(tp_from.getTime(), ZoneOffset.of("+1"));

		if (!speed_or_dur) {
			duration_value = duration_value.ofSeconds((long) (second_difference / speed_value));
		} else {
			speed_value = second_difference / duration_value.toSeconds();
		}

		if (!fps_or_pics) {
			pictures_value = (int) (duration_value.toSeconds() * fps_value);
		} else {
			fps_value = pictures_value / duration_value.toSeconds();
		}
		
		speed_value_txt.setText2(String.format("%.1fx", speed_value));
		length_value_txt.setText2(String.format("%02d:%02d:%02d:%02d", duration_value.toDaysPart(), duration_value.toHoursPart(), duration_value.toMinutesPart(), duration_value.toSecondsPart()));
		framerate_value_txt.setText2(String.format("%.1ffps", fps_value));
		pictures_value_txt.setText2(String.format("%d", pictures_value));
		
		/*
		 * boolean speed_or_dur = false; // speed = false, duration = true boolean fps_or_pics = false; //
		 * fps = false, pics = true
		 * 
		 * double speed_value = 10; Duration duration_value = Duration.ofSeconds(100000); double fps_value =
		 * 60; int pictures_value = 420;
		 */
	}

	public void updateShape() {
		// --------------------------------- First

		start.setSize(sizey * height_title_factor);
		start.setX2(sizey * edge_factor / 2);
		start.setY2(-text_pos_factor * sizey * height_title_factor);

		double tp_width = sizey * (1 - height_btn_factor - height_gap_factor) * 47 / 60;

		tp_from.setPosition(sizex * (1 - edge_factor / 2), 0);
		tp_from.setShape(tp_width, sizey * (1 - height_btn_factor - height_gap_factor));

		dp_from.setPosition(sizey * edge_factor / 2, sizey * (height_title_factor + height_gap_factor));
		dp_from.setShape(sizex - (sizex * edge_factor + sizey * height_gap_factor + tp_width),
				sizey * (1 - height_btn_factor - 2 * height_gap_factor - height_title_factor));

		next1_l.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		next1.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		next1.setPosition(sizex * (1 - edge_factor / 2), sizey);

		// --------------------------------- Second

		end.setSize(sizey * height_title_factor);
		end.setX2(sizex + sizey * edge_factor / 2);
		end.setY2(-text_pos_factor * sizey * height_title_factor);

		tp_to.setPosition(sizex + sizex * (1 - edge_factor / 2), 0);
		tp_to.setShape(tp_width, sizey * (1 - height_btn_factor - height_gap_factor));

		dp_to.setPosition(sizex + sizey * edge_factor / 2, sizey * (height_title_factor + height_gap_factor));
		dp_to.setShape(sizex - (sizex * edge_factor + sizey * height_gap_factor + tp_width),
				sizey * (1 - height_btn_factor - 2 * height_gap_factor - height_title_factor));

		next2_l.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		next2.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		next2.setPosition(sizex + sizex * (1 - edge_factor / 2), sizey);

		last2_l.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		last2.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		last2.setPosition(sizex + sizex * edge_factor / 2, sizey);

		// --------------------------------- Third

		double btn_middle = sizex * (3 - edge_factor / 2 - 3 * small_btn_factor - 2.5 * small_btn_gap_factor);
		double btn_m1 = btn_middle - sizex * (0.5 * small_btn_gap_factor + 0.5 * small_btn_factor);
		double btn_m2 = btn_middle - sizex * (1.5 * small_btn_gap_factor + 1.5 * small_btn_factor);
		double btn_m3 = btn_middle - sizex * (2.5 * small_btn_gap_factor + 2.5 * small_btn_factor);
		double btn_1 = btn_middle + sizex * (0.5 * small_btn_gap_factor + 0.5 * small_btn_factor);
		double btn_2 = btn_middle + sizex * (1.5 * small_btn_gap_factor + 1.5 * small_btn_factor);
		double btn_3 = btn_middle + sizex * (2.5 * small_btn_gap_factor + 2.5 * small_btn_factor);
		
		double left_edge = sizex * (2 + edge_factor / 2);
		double text_edge = left_edge + sizey * mode_image_factor + sizey * height_gap_factor;

		double virt_sizey = sizey * (1 - height_gap_factor - height_btn_factor);
		double text_size = virt_sizey * (1 - edge_text_factor) * value_text_factor;
		double top_text_offset = text_size * top_text_factor;
		double text_offsets = (virt_sizey - sizey * edge_text_factor - top_text_offset - text_size) / 3;
		
		double v1 = text_size + sizey * edge_text_factor / 2 + top_text_offset;
		double v2 = text_size + sizey * edge_text_factor / 2 + top_text_offset + text_offsets;
		double v3 = text_size + sizey * edge_text_factor / 2 + top_text_offset + 2 * text_offsets;
		double v4 = text_size + sizey * edge_text_factor / 2 + top_text_offset + 3 * text_offsets;
		
		System.out.println(btn_m3);
		System.out.println(btn_m2);
		System.out.println(btn_m1);
		System.out.println(btn_1);
		System.out.println(btn_2);
		System.out.println(btn_3);
		System.out.println();
		System.out.println(sizex * (3 - edge_factor / 2));
		System.out.println(btn_middle);
		System.out.println();
		
		speed_mm.setPosition(btn_m2, v1);
		speed_mm.setShape(sizex * small_btn_factor, sizex * small_btn_factor);
		speed_m.setPosition(btn_m1, v1);
		speed_m.setShape(sizex * small_btn_factor, sizex * small_btn_factor);
		speed_p.setPosition(btn_1, v1);
		speed_p.setShape(sizex * small_btn_factor, sizex * small_btn_factor);
		speed_pp.setPosition(btn_2, v1);
		speed_pp.setShape(sizex * small_btn_factor, sizex * small_btn_factor);

		/*
		length_m_h = new TextButton("-h");
		length_m_m = new TextButton("-m");
		length_m_s = new TextButton("-s");
		length_p_s = new TextButton("+s");
		length_p_m = new TextButton("+m");
		length_p_h = new TextButton("+h");

		frame_mm = new TextButton("--");
		frame_m = new TextButton("-");
		frame_p = new TextButton("+");
		frame_pp = new TextButton("++");

		pic_mm = new TextButton("--");
		pic_m = new TextButton("-");
		pic_p = new TextButton("+");
		pic_pp = new TextButton("++");
		*/
		
		tl_mode_rec.setX2(left_edge);
		tl_mode_rec.setY2(virt_sizey / 2);
		tl_mode_rec.setWidth2(virt_sizey * mode_image_factor);
		tl_mode_rec.setHeight2(virt_sizey * mode_image_factor);
		tl_mode_img.setX2(left_edge);
		tl_mode_img.setY2(virt_sizey / 2);
		tl_mode_img.setFitHeight2(virt_sizey * mode_image_factor);
		
		speed_txt.setX2(text_edge);
		speed_txt.setY2(v1 - top_text_offset);
		speed_txt.setSize(text_size);

		speed_value_txt.setX2(text_edge);
		speed_value_txt.setY2(v1);
		speed_value_txt.setSize(text_size);

		length_txt.setX2(text_edge);
		length_txt.setY2(v2 - top_text_offset);
		length_txt.setSize(text_size);

		length_value_txt.setX2(text_edge);
		length_value_txt.setY2(v2);
		length_value_txt.setSize(text_size);

		framerate_txt.setX2(text_edge);
		framerate_txt.setY2(v3 - top_text_offset);
		framerate_txt.setSize(text_size);

		framerate_value_txt.setX2(text_edge);
		framerate_value_txt.setY2(v3);
		framerate_value_txt.setSize(text_size);
		
		pictures_txt.setX2(text_edge);
		pictures_txt.setY2(v4 - top_text_offset);
		pictures_txt.setSize(text_size);

		pictures_value_txt.setX2(text_edge);
		pictures_value_txt.setY2(v4);
		pictures_value_txt.setSize(text_size);

		finish3_l.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		finish3.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		finish3.setPosition(2 * sizex + sizex * (1 - edge_factor / 2), sizey);

		last3_l.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		last3.setShape(sizex * widht_btn_factor, sizey * height_btn_factor);
		last3.setPosition(2 * sizex + sizex * edge_factor / 2, sizey);

		/*
		 * edge_factor = 0.1; height_gap_factor = 0.05; height_btn_factor = 0.1; widht_btn_factor = 0.25;
		 * height_title_factor = 0.15; text_pos_factor = 0.2;
		 * 
		 * small_btn_factor = 0.01; small_btn_gap_factor = 0.001; value_text_factor = 0.06; top_text_factor
		 * = 0.7; edge_text_factor = 0.1; mode_image_factor = 0.3; mode_rec_outline = 4;
		 * 
		 * 
		 * 
		 * 
		 * speed_mm = new TextButton("--"); speed_m = new TextButton("-"); speed_p = new TextButton("+");
		 * speed_pp = new TextButton("++");
		 * 
		 * length_m_h = new TextButton("-h"); length_m_m = new TextButton("-m"); length_m_s = new
		 * TextButton("-s"); length_p_s = new TextButton("+s"); length_p_m = new TextButton("+m");
		 * length_p_h = new TextButton("+h");
		 * 
		 * frame_mm = new TextButton("--"); frame_m = new TextButton("-"); frame_p = new TextButton("+");
		 * frame_pp = new TextButton("++");
		 * 
		 * pic_mm = new TextButton("--"); pic_m = new TextButton("-"); pic_p = new TextButton("+"); pic_pp =
		 * new TextButton("++");
		 * 
		 * tl_mode_img = new ImageView2(); tl_mode_img.setPos(3); tl_mode_rec = new Rectangle2();
		 * tl_mode_rec.setPos(3); tl_mode_up_l = new ArrowLayout(); tl_mode_down_l = new ArrowLayout();
		 * tl_mode_up = new Button(); tl_mode_up.setDesign(tl_mode_up_l); tl_mode_down = new Button();
		 * tl_mode_down.setDesign(tl_mode_down_l);
		 * 
		 * speed_txt = new Text2("Geschwindigkeit"); speed_value_txt = new Text2(); length_txt = new
		 * Text2("Videolänge"); length_value_txt = new Text2(); framerate_txt = new Text2("Framerate");
		 * framerate_value_txt = new Text2(); pictures_txt = new Text2("Bilderanzahl"); pictures_value_txt =
		 * new Text2();
		 */

	}

	public void reload() {
		dp_from.reload();
		tp_from.reload();
		dp_to.reload();
		tp_to.reload();
		
		speed_value = 10;
		fps_value = 60;
		
		recalculate();
	}

	public void mouseClick(double mousex, double mousey) {
		switch (mode) {
		case 0:
			dp_from.mouseClicked(mousex - this.positionx, mousey - this.positiony);
			tp_from.mouseClick(mousex - this.positionx, mousey - this.positiony);

			if (next1.isPressed(mousex - this.positionx, mousey - this.positiony)) {
				last_mode = mode;
				mode = 1;
				moving = true;
			}
			break;
		case 1:
			dp_to.mouseClicked(mousex - this.positionx, mousey - this.positiony);
			tp_to.mouseClick(mousex - this.positionx, mousey - this.positiony);

			if (next2.isPressed(mousex - this.positionx, mousey - this.positiony)) {
				last_mode = mode;
				mode = 2;
				moving = true;
			}
			if (last2.isPressed(mousex - this.positionx, mousey - this.positiony)) {
				last_mode = mode;
				mode = 0;
				moving = true;
			}
			break;
		case 2:
			if (last3.isPressed(mousex - this.positionx, mousey - this.positiony)) {
				last_mode = mode;
				mode = 1;
				moving = true;
			}
			break;
		}
		if (dp_to.getLocalDate().toEpochDay() < dp_from.getLocalDate().toEpochDay()) {
			dp_to.setDate(dp_from.getLocalDate());
		}
		
		recalculate();
	}

	public void mousePressed(double mousex, double mousey) {
		switch (mode) {
		case 0:
			tp_from.mousePressed(mousex - this.positionx, mousey - this.positiony);
			break;
		case 1:
			tp_to.mousePressed(mousex - this.positionx, mousey - this.positiony);
			break;
		}
	}

	public void mouseReleased(double mousex, double mousey) {
		switch (mode) {
		case 0:
			tp_from.mouseReleased(mousex - this.positionx, mousey - this.positiony);
			break;
		case 1:
			tp_to.mouseReleased(mousex - this.positionx, mousey - this.positiony);
			break;
		}

		if (dp_from.getLocalDate().toEpochDay() == dp_to.getLocalDate().toEpochDay()) {
			if (tp_from.getTime().toSecondOfDay() > tp_to.getTime().toSecondOfDay()) {
				tp_to.setTime(tp_from.getTime());
			}
		}
	}

	public void mouseDragged(double mousex, double mousey) {
		switch (mode) {
		case 0:
			tp_from.mouseDragged(mousex - this.positionx, mousey - this.positiony);
			break;
		case 1:
			tp_to.mouseDragged(mousex - this.positionx, mousey - this.positiony);
			break;
		}
	}

	@Override
	public void setPosition(double posx, double posy) {
		normal_posx = posx;
		this.positiony = posy;
		super.setPosition(normal_posx - bezFactor(factor) * sizex, this.positiony);
		super.updatePosition();
	}

	@Override
	public void setPosition(double posx, double posy, int pos) {
		normal_posx = posx;
		this.positiony = posy;
		this.positioning = pos;
		super.setPosition(normal_posx - bezFactor(factor) * sizex, this.positiony);
		super.updatePosition();
	}

	@Override
	public void update() {
		if (moving) {
			if (mode > last_mode) {
				factor += speed / variables.frameRate;
				if (factor >= mode) {
					factor = mode;
					last_mode = mode;
					moving = false;
				}
			} else {
				factor -= speed / variables.frameRate;
				if (factor <= mode) {
					factor = mode;
					last_mode = mode;
					moving = false;
				}
			}

			super.setPosition(normal_posx - bezFactor(factor) * sizex, this.positiony);
		}

		tp_from.update();
		dp_from.update();

		tp_to.update();
		dp_to.update();
	}

	private double bezFactor(double factor) {
		boolean addOne = factor > 1;

		if (addOne)
			factor -= 1;
		double res = Bezier.bezier_curve_2d(factor, new Vector(bez_factor, 0), new Vector(1 - bez_factor, 1)).y;
		if (addOne)
			res += 1;
		return res;
	}
}
