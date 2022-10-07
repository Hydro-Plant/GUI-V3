package poissonDisc;

import standard.Random;

class Point {
	int grid_pos;
	int mode; // 0: Non-Active, 1: Active, 2: Half-Active
	double rand;

	Point(int pos, int mode) {
		this.grid_pos = pos;
		this.mode = mode;
		this.rand = Random.rand(1);
	}

	void setPos(int pos) {
		this.grid_pos = pos;
	}

	void setMode(int mode) {
		this.mode = mode;
	}

	int getPos() {
		return grid_pos;
	}

	int getMode() {
		return mode;
	}

	double getRand() {
		return rand;
	}
}
