package exampleSceneObjects;

import exampleLayouts.TextButtonLayout;
import javafx.scene.paint.Color;
import sceneObjects.Button;

public class TextButton extends Button {
	TextButtonLayout tbl;

	public TextButton(String text) {
		tbl = new TextButtonLayout(text);
		tbl.setBackg(Color.TRANSPARENT);
		this.setDesign(tbl);
		this.setPos(4);
	}

	public void setTextUp(double textUp) {
		tbl.setTextUp(textUp);
	}

	public void setSize(double size) {
		tbl.setSize(size);
	}

	public void setOutline(double outline_width) {
		tbl.setOutline(outline_width);
	}

	@Override
	public void setShape(double sizex, double sizey) {
		super.setShape(sizex, sizey);
		tbl.setShape(sizex, sizey);
	}

	@Override
	public void setPos(int pos) {
		tbl.setPos(pos);
		super.setPos(pos);
	}
}
