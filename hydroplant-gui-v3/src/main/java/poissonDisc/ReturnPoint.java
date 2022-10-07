package poissonDisc;

import standard.Vector;

public class ReturnPoint {
	Vector pos;
	int mode; // 0: Non-Active, 1: Active, 2: Half-Active
	double rand;

	ReturnPoint(Point p, Vector[] grid) {
		this.pos = grid[p.getPos()];
		this.mode = p.getMode();
		this.rand = p.getRand();
	}

	void setPos(Vector pos) {
		this.pos = pos;
	}

	void setMode(int mode) {
		this.mode = mode;
	}

	public Vector getPos() {
		return pos;
	}

	public int getMode() {
		return mode;
	}

	public double getRand() {
		return rand;
	}

	@Override
	public String toString() {
		return String.format("Pos: %f | %f\nMode: %f\nRandom: %f", pos.x, pos.y, mode, rand);
	}
}