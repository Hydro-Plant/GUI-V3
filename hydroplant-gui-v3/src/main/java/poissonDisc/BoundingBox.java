package poissonDisc;

import standard.Vector;

public class BoundingBox {
	Vector position;
	Vector size;

	double pos_change_x;
	double pos_change_y;

	BoundingBox(Vector position, int positioning, Vector size) {
		this.position = position;
		positioning_calc(positioning);
		this.size = size;
	}

	public BoundingBox(int positionx, int positiony, int positioning, int sizex, int sizey) {
		this.position = new Vector(positionx, positiony);
		positioning_calc(positioning);
		this.size = new Vector(sizex, sizey);
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public void setPosition(Vector position, int positioning) {
		this.position = position;
		positioning_calc(positioning);
	}

	public void setPosition(int positionx, int positiony) {
		this.position = new Vector(positionx, positiony);
	}

	public void setPosition(int positionx, int positiony, int positioning) {
		this.position = new Vector(positionx, positiony);
		positioning_calc(positioning);
	}

	public void setSize(Vector size) {
		this.size = size;
	}

	public void setSize(int sizex, int sizey) {
		this.size = new Vector(sizex, sizey);
	}

	public boolean intersecting(Vector position) {
		return intersecting((int) position.x, (int) position.y);
	}

	public boolean intersecting(int positionx, int positiony) {
		/*
		 * println("X Boundry"); println(-pos_change_x * size.x + position.x); println((1 - pos_change_x) *
		 * size.x + position.x); println("Y Boundry"); println(-pos_change_y * size.y + position.y);
		 * println((1 - pos_change_y) * size.y + position.y); println("\n--------------------\n");
		 */
		return positionx >= -pos_change_x * size.x + position.x && positionx <= (1 - pos_change_x) * size.x + position.x
				&& positiony >= -pos_change_y * size.y + position.y
				&& positiony <= (1 - pos_change_y) * size.y + position.y;
	}

	public Vector closest(Vector position) {
		Vector result = new Vector();

		result.x = Math.max(this.position.x - pos_change_x * this.size.x,
				Math.min(position.x, this.position.x + (1 - pos_change_x) * this.size.x));
		result.y = Math.max(this.position.y - pos_change_y * this.size.y,
				Math.min(position.y, this.position.y + (1 - pos_change_y) * this.size.y));
		return result;
	}

	public Vector closest(int positionx, int positiony) {
		Vector result = new Vector();
		result.x = Math.max(this.position.x - pos_change_x * size.x,
				Math.min(positionx, this.position.x + (1 - pos_change_x) * this.size.x));
		result.y = Math.max(this.position.y - pos_change_y * size.y,
				Math.min(positiony, this.position.y + (1 - pos_change_y) * this.size.y));
		return result;
	}

	public int getBoundry(int b) { // 0: Top, 1: Right, 2: Bottom, 3: Left
		switch (b) {
		case 0:
			return (int) (position.y - pos_change_y * size.y);
		case 1:
			return (int) (position.x + (1 - pos_change_x) * size.x);
		case 2:
			return (int) (position.y + (1 - pos_change_y) * size.y);
		case 3:
			return (int) (position.x - pos_change_x * size.x);
		}
		return -1;
	}

	@Override
	public String toString() {
		return String.format("Top: %d\nRight: %d\nBottom: %d\nLeft: %d", getBoundry(0), getBoundry(1), getBoundry(2),
				getBoundry(3));
	}

	public void positioning_calc(int positioning) {
		switch (positioning) { // Von Position 0 ausgehend
		case 0:
			pos_change_x = 0;
			pos_change_y = 0;
			break;
		case 1:
			pos_change_x = 1f / 2f;
			pos_change_y = 0;
			break;
		case 2:
			pos_change_x = 1;
			pos_change_y = 0;
			break;
		case 3:
			pos_change_x = 0;
			pos_change_y = 1f / 2f;
			break;
		case 4:
			pos_change_x = 1f / 2f;
			pos_change_y = 1f / 2f;
			break;
		case 5:
			pos_change_x = 1;
			pos_change_y = 1f / 2f;
			break;
		case 6:
			pos_change_x = 0;
			pos_change_y = 1;
			break;
		case 7:
			pos_change_x = 1f / 2f;
			pos_change_y = 1;
			break;
		case 8:
			pos_change_x = 1;
			pos_change_y = 1;
			break;
		}
	}
}