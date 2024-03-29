package exampleLayouts;

import gui.Layout;
import gui.constants;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx2.Rectangle2;
import javafx2.Text2;
import standard.Alpha;
import standard.Positioning;

public class DashboardButton extends Layout {
	Rectangle2 bg_rec; // Button layout objects
	Rectangle2 mask;
	Text2 title;
	Text2 information;
	Text2 unit;
	Pane additional_background;

	double virt_sizex, virt_sizey;

	DashboardButton() {
		mask = new Rectangle2();
		bg_rec = new Rectangle2();
		title = new Text2();
		information = new Text2();
		unit = new Text2();

		title.setFill(Color.WHITE);
		information.setFill(Color.WHITE);
		unit.setFill(Color.WHITE);

		title.setStroke(constants.outline_col);
		information.setStroke(constants.outline_col);
		unit.setStroke(constants.outline_col);
		bg_rec.setStroke(constants.outline_col);

		mask.setFill(Color.WHITE);
		mask.setX2(0);
		mask.setY2(0);
		mask.setPos(positioning);

		bg_rec.setX2(0);
		bg_rec.setY2(0);

		additional_background = new Pane();
		additional_background.setClip(mask);

		title.setHorizontalOrientation(TextAlignment.LEFT);
		title.setTextOrigin(VPos.TOP);
		information.setHorizontalOrientation(TextAlignment.CENTER);
		information.setTextOrigin(VPos.CENTER);
		unit.setHorizontalOrientation(TextAlignment.RIGHT);
		unit.setTextOrigin(VPos.BOTTOM);

		addObject(bg_rec);
		addObject(additional_background);
		addObject(title);
		addObject(information);
		addObject(unit);
	}

	public void setTextAlpha(double alpha) {
		title.setAlpha(alpha);
		information.setAlpha(alpha);
		unit.setAlpha(alpha);

		title.setStroke(Alpha.changeAlpha((Color) title.getStroke(), alpha));
		information.setStroke(Alpha.changeAlpha((Color) information.getStroke(), alpha));
		unit.setStroke(Alpha.changeAlpha((Color) unit.getStroke(), alpha));
	}

	public void setStrokeWidth(double d) {
		title.setStrokeWidth(d);
		information.setStrokeWidth(d);
		unit.setStrokeWidth(d);
		bg_rec.setStrokeWidth(d);
	}

	public void setVirtualShape(double btn_width, double btn_height, int positioning) {
		this.virt_sizex = btn_width;
		this.virt_sizey = btn_height;
		this.positioning = positioning;
		bg_rec.setPos(positioning);
		bg_rec.setArcHeight(constants.corner_height_val * btn_height);
		bg_rec.setArcWidth(constants.corner_height_val * btn_height);

		mask.setArcHeight((int) (constants.corner_height_val * btn_height));
		mask.setArcWidth((int) (constants.corner_height_val * btn_height));
		mask.setPos(positioning);

		double[] pos_shift = Positioning.positioning(positioning, 4);

		title.setX2((int) (pos_shift[0] * btn_width - btn_width / 2
				+ constants.titlepos_height_val_x * constants.corner_height_val * btn_height));
		title.setY2((int) (pos_shift[1] * btn_height - btn_height / 2
				+ constants.titlepos_height_val_y * constants.corner_height_val * btn_height));
		title.setSize(constants.title_height_val * btn_height);

		information.setX2((int) Math.floor(pos_shift[0] * btn_width));
		information.setY2((int) Math.floor(pos_shift[1] * btn_height));
		information.setSize(constants.information_height_val * btn_height);

		unit.setX2((int) (pos_shift[0] * btn_width + btn_width / 2
				- constants.titlepos_height_val_x * constants.corner_height_val * btn_height));
		unit.setY2((int) (pos_shift[1] * btn_height + btn_height / 2
				- constants.titlepos_height_val_y * constants.corner_height_val * btn_height));
		unit.setSize((int) (constants.title_height_val * btn_height));

	}

	public void setShape(double btn_width, double btn_height) {
		bg_rec.setWidth2(btn_width);
		bg_rec.setHeight2(btn_height);
		mask.setWidth2(btn_width);
		mask.setHeight2(btn_height);
	}
}