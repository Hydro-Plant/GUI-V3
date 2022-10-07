package javafx2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import standard.Positioning;

public class ImageView2 extends ImageView {
	private int positioning = 0;
	private double posx = 0;
	private double posy = 0;

	public int test_id = 0;

	private void updateImgView() {
		if (super.getImage() != null) {
			double realWidth;
			double realHeight;

			if (super.getFitHeight() <= 0 && super.getFitWidth() <= 0) {
				realWidth = super.getImage().getWidth();
				realHeight = super.getImage().getHeight();
			} else if (!super.isPreserveRatio()) {
				if (super.getFitWidth() > 0)
					realWidth = super.getFitWidth();
				else
					realWidth = super.getImage().getWidth();
				if (super.getFitHeight() > 0)
					realHeight = super.getFitHeight();
				else
					realHeight = super.getImage().getHeight();
			} else {
				double aspectRatio = super.getImage().getWidth() / super.getImage().getHeight();
				if (super.getFitHeight() > 0 && super.getFitWidth() > 0) {
					realWidth = Math.min(super.getFitWidth(), super.getFitHeight() * aspectRatio);
					realHeight = Math.min(super.getFitHeight(), super.getFitWidth() / aspectRatio);
				} else if (super.getFitHeight() > 0) {
					realWidth = super.getFitHeight() * aspectRatio;
					realHeight = super.getFitHeight();
				} else {
					realWidth = super.getFitWidth();
					realHeight = super.getFitWidth() / aspectRatio;
				}
			}

			if (super.getImage() != null) {
				super.setX(posx + Positioning.positioning(positioning, 0)[0] * realWidth);
				super.setY(posy + Positioning.positioning(positioning, 0)[1] * realHeight);
			}
		}
	}

	public void setPos(int positioning) {
		this.positioning = positioning;
		updateImgView();
	}

	public int getPos() {
		return positioning;
	}

	public void setX2(double x) {
		posx = x;
		updateImgView();
	}

	public void setY2(double y) {
		posy = y;
		updateImgView();
	}

	public void setImage2(Image img) {
		super.setImage(img);
		updateImgView();
	}

	public void setFitWidth2(double value) {
		super.setFitWidth(value);
		updateImgView();
	}

	public void setFitHeight2(double value) {
		super.setFitHeight(value);
		updateImgView();
	}

	public void setPreserveRatio2(boolean value) {
		super.setPreserveRatio(value);
		updateImgView();
	}
}
