package exampleSceneObjects;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import exampleLayouts.RemoveLayout;
import gui.Layout;
import gui.constants;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.ImageView2;
import javafx2.Rectangle2;
import javafx2.Text2;
import sceneObjects.Button;
import sceneObjects.FlatLayout;
import sceneObjects.MiniScene;
import standard.Positioning;

public class TimeLapse extends MiniScene {
	private final double mode_factor = 0.9;
	private final double text_factor = 0.7;
	private final double loading_bar_factor = 0.95;
	private final double loading_outline_factor = 5;
	private final double remove_factor = 0.5;
	private final double arc_factor = 0.1;
	
	private double height, width;
	private LocalDate local_from_date, local_to_date;
	private LocalTime local_from_time, local_to_time;
	
	private Rectangle2 backg;
	private Text2 date_from;
	private Text2 date_to;
	private Text2 date_line;
	private Text2 speed;
	private Text2 duration;
	private Text2 fps;
	private Text2 pictures;
	private ImageView2 mode_pic;
	
	Layout l;
	FlatLayout fl;
	
	RemoveLayout rl;
	Button b;
	
	LoadingBar lb;
	
	public TimeLapse() {
		backg = new Rectangle2();
		backg.setFill(Color.rgb(69, 69, 69));
		
		date_from = new Text2();
		date_from.setHorizontalOrientation(TextAlignment.LEFT);
		date_from.setTextOrigin(VPos.CENTER);
		date_from.setFill(Color.WHITE);
		date_from.setStroke(constants.outline_col);
		
		date_to = new Text2();
		date_to.setHorizontalOrientation(TextAlignment.RIGHT);
		date_to.setTextOrigin(VPos.CENTER);
		date_to.setFill(Color.WHITE);
		date_to.setStroke(constants.outline_col);
		
		date_line = new Text2();
		date_line.setText2("-");
		date_line.setHorizontalOrientation(TextAlignment.CENTER);
		date_line.setTextOrigin(VPos.CENTER);
		date_line.setFill(Color.WHITE);
		date_line.setStroke(constants.outline_col);
		
		speed = new Text2();
		speed.setHorizontalOrientation(TextAlignment.LEFT);
		speed.setTextOrigin(VPos.CENTER);
		speed.setFill(Color.WHITE);
		speed.setStroke(constants.outline_col);
		
		duration = new Text2();
		duration.setHorizontalOrientation(TextAlignment.RIGHT);
		duration.setTextOrigin(VPos.CENTER);
		duration.setFill(Color.WHITE);
		duration.setStroke(constants.outline_col);
		
		fps = new Text2();
		fps.setHorizontalOrientation(TextAlignment.LEFT);
		fps.setTextOrigin(VPos.CENTER);
		fps.setFill(Color.WHITE);
		fps.setStroke(constants.outline_col);
		
		pictures = new Text2();
		pictures.setHorizontalOrientation(TextAlignment.RIGHT);
		pictures.setTextOrigin(VPos.CENTER);
		pictures.setFill(Color.WHITE);
		pictures.setStroke(constants.outline_col);
		
		mode_pic = new ImageView2();
		mode_pic.setPos(4);
		
		l = new Layout();
		l.addObject(backg);
		l.addObject(date_from);
		l.addObject(date_line);
		l.addObject(date_to);
		l.addObject(speed);
		l.addObject(duration);
		l.addObject(fps);
		l.addObject(pictures);
		l.addObject(mode_pic);
		fl = new FlatLayout();
		fl.setDesign(l);
		
		rl = new RemoveLayout();
		rl.setPos(8);
		b = new Button();
		b.setPos(8);
		b.setDesign(rl);
		
		lb = new LoadingBar();
		lb.setPos(3);
		
		updateShape();
		
		addObject(fl);
		addObject(b);
		addObject(lb);
	}
	
	public void setOutline(double outline) {
		lb.setOutline(outline * loading_outline_factor);
		rl.setOutline(outline);
		
		date_from.setStrokeWidth(outline);
		date_to.setStrokeWidth(outline);
		date_line.setStrokeWidth(outline);
		speed.setStrokeWidth(outline);
		duration.setStrokeWidth(outline);
		fps.setStrokeWidth(outline);
		pictures.setStrokeWidth(outline);
		
	}
	
	public void setDate(LocalDate from, LocalDate to) {
		date_from.setText2(String.format("%d.%d.%d", from.getDayOfMonth(), from.getMonthValue(), from.getYear()));
		date_to.setText2(String.format("%d.%d.%d", to.getDayOfMonth(), to.getMonthValue(), to.getYear()));
		
		local_from_date = from;
		local_to_date = to;
		
		updateShape();
	}
	
	public void setTime(LocalTime from, LocalTime to) {
		local_from_time = from;
		local_to_time = to;
	}
	
	public void setSpeed(double speed) {
		this.speed.setText2(String.format("%.1fx", speed));
	}
	
	public void setDuration(Duration duration) {
		this.duration.setText2(String.format("%02d:%02d:%02d:%02d", duration.toDaysPart(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()));
	}
	
	public void setFPS(double fps) {
		this.fps.setText2(String.format("%.1f fps", fps));
	}
	
	public void setPictures(int pictures) {
		this.pictures.setText2(String.format("%d Fotos", pictures));
	}
	
	public void setShape(double width, double height) {
		this.width = width;
		this.height = height;
		
		updateShape();
	}
	
	public void setMode(int mode) {
		System.out.println(String.format("file:pics/mode%02d.png", mode));
		mode_pic.setImage2(new Image(String.format("file:pics/mode%02d.png", mode)));
	}
	
	public void update() {
		if(local_from_date != null && local_to_date != null && local_from_time != null && local_to_time != null) {
			double from_epoch = local_from_date.toEpochSecond(local_from_time, ZoneOffset.of("+2"));
			double to_epoch = local_to_date.toEpochSecond(local_to_time, ZoneOffset.of("+2"));
			double now_epoch = LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.of("+2"));

			lb.setFactor((now_epoch - from_epoch)/(to_epoch - from_epoch));
		}
	}
	
	public void updateShape() {
		backg.setWidth2(width);
		backg.setHeight2(height);
		backg.setArcHeight(height * arc_factor);
		backg.setArcWidth(height * arc_factor);
		
		rl.setSize(height * 0.2 * remove_factor);
		b.setShape(rl.getWidth(), rl.getHeight());
		b.setPosition(Positioning.positioning(positioning, 8)[0] * width - height * (1 - mode_factor) / 2, (Positioning.positioning(positioning, 8)[1] - (1 - mode_factor) / 2) * height);
		
		mode_pic.setX2(width * Positioning.positioning(positioning, 0)[0] + height / 2);
		mode_pic.setY2(height * Positioning.positioning(positioning, 0)[1] + height / 2);
		mode_pic.setFitWidth2(height * mode_factor);
		mode_pic.setFitHeight2(height * mode_factor);
		
		double left_line = width * Positioning.positioning(positioning, 0)[0] + height;
		double right_line = width * Positioning.positioning(positioning, 2)[0] - rl.getWidth() - height * (1 - mode_factor);
		
		double y1 = height * Positioning.positioning(positioning, 0)[1] + height * 0.125;
		double y2 = y1 + height * 0.25;
		double y3 = y2 + height * 0.25;
		double y4 = y3 + height * 0.25;
		
		double text_size = height * 0.25 * text_factor;
		
		date_from.setX2(left_line);
		date_from.setY2(y1);
		date_from.setSize(text_size);
		
		date_to.setX2(right_line);
		date_to.setY2(y1);
		date_to.setSize(text_size);
		
		date_line.setX2((date_from.getX() + date_from.getBoundsInLocal().getWidth() + date_to.getX()) / 2);
		date_line.setY2(y1);
		date_line.setSize(text_size);
		
		lb.setPosition(left_line, y2);
		lb.setShape(right_line - left_line, height * 0.25 * loading_bar_factor);
		
		speed.setX2(left_line);
		speed.setY2(y3);
		speed.setSize(text_size);
		
		duration.setX2(right_line);
		duration.setY2(y3);
		duration.setSize(text_size);
		
		fps.setX2(left_line);
		fps.setY2(y4);
		fps.setSize(text_size);
		
		pictures.setX2(right_line);
		pictures.setY2(y4);
		pictures.setSize(text_size);
	}
}
