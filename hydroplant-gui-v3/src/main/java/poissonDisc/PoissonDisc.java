package poissonDisc;

import java.util.ArrayList;

import standard.Random;
import standard.Vector;

public class PoissonDisc {
	BoundingBox bb;
	double r;
	final double k = 30;
	Vector[] grid;
	double w;
	int rows;
	int cols;
	ArrayList<Integer> active;
	ArrayList<Integer> half_active;
	ArrayList<Point> points;

	int sizex, sizey;

	public PoissonDisc(int sizex, int sizey, double r) {
		this.active = new ArrayList<>();
		this.half_active = new ArrayList<>();
		this.points = new ArrayList<>();
		this.sizex = sizex;
		this.sizey = sizey;
		this.r = r;
		this.w = r / Math.sqrt(2);

		bb = null;

		// Setup
		rows = (int) Math.floor(sizey / w);
		cols = (int) Math.floor(sizex / w);

		grid = new Vector[rows * cols];
		for (int x = 0; x < rows * cols; x++) {
			grid[x] = null;
		}
	}

	public void setFirst() {
		int x0_x;
		int x0_y;

		if (bb == null) {
			x0_x = (int) Math.floor(Random.rand(this.sizex));
			x0_y = (int) Math.floor(Random.rand(this.sizey));
		} else {
			x0_x = (int) Math.floor(Random.rand(bb.getBoundry(3), bb.getBoundry(1)));
			x0_y = (int) Math.floor(Random.rand(bb.getBoundry(0), bb.getBoundry(2)));
		}

		grid[(int) Math.floor(x0_x / w) + (int) Math.floor(x0_y / w) * cols] = new Vector(x0_x, x0_y);
		points.add(new Point((int) Math.floor(x0_x / w) + (int) Math.floor(x0_y / w) * cols, 1));
		active.add((int) Math.floor(x0_x / w) + (int) Math.floor(x0_y / w) * cols);
	}

	public void setActive() {
		for (int x = 0; x < points.size(); x++) {
			if (points.get(x).getMode() == 0) {
				active.add(points.get(x).getPos());
				points.get(x).setMode(1);
			}
		}
	}

	public void subCalcMissing() {
		int i = (int) Math.floor(Random.rand(active.size())); // Choose from active list
		boolean points_found = false; // If one of the test-points was ok
		double str_angle = Random.rand(2 * Math.PI); // Random angle where point search starts

		for (int q = 0; q < k; q++) { // Search new points k times
			double angle = Random.rand(2 * Math.PI / k * q, 2 * Math.PI / k * (q + 1)) + str_angle;
			double magn = Random.rand(r, 2 * r);

			Vector new_point = Vector.fromAngle(angle).setMag(magn).add(grid[active.get(i)]); // New calculated point
			// println(new_point);

			int grid_x = (int) Math.floor(new_point.x / w); // Calculating grid position
			int grid_y = (int) Math.floor(new_point.y / w);
			// print("Test ");
			// println(q);
			if (grid_x >= 0 && grid_x < cols && grid_y >= 0 && grid_y < rows) {
				boolean pos_ok = true;
				start_loop: for (int x = -2; x <= 2; x++) { // Searching points arround new point
					for (int y = -2; y <= 2; y++) {
						if ((grid_x + x) >= 0 && (grid_x + x) < cols && (grid_y + y) >= 0 && (grid_y + y) < rows) { // Is
																													 // chosen
																													 // grid
																													 // cell
																													 // out
																													 // of
																													 // grid
							if (grid[(grid_x + x) + (grid_y + y) * cols] != null) { // Is grid cell not empty
								if (grid[(grid_x + x) + (grid_y + y) * cols].dist(new_point) < r) { // Is point in cell
																									 // too close to new
																									 // cell
									pos_ok = false; // New point is useless
									break start_loop;
								}
							}
						}
					}
				}

				if (pos_ok) { // Point found
					points_found = true;
					grid[grid_x + grid_y * cols] = new_point;
					if (bb == null || bb.intersecting(new_point)) {
						active.add(grid_x + grid_y * cols);
						points.add(new Point(grid_x + grid_y * cols, 1));
						break;
					} else {
						half_active.add(grid_x + grid_y * cols);
						points.add(new Point(grid_x + grid_y * cols, 2));
					}
				}
			}
		}
		if (!points_found) {
			for (Point point : points) {
				if (point.getPos() == active.get(i)) {
					point.setMode(0);
					break;
				}
			}
			active.remove(i);
		}
	}

	public void calcMissing() {
		while (active.size() > 0) {
			subCalcMissing();
		}
	}

	public void calcMissingOnce() {
		if (active.size() > 0) {
			subCalcMissing();
		}
	}

	public void setBoundingBox(BoundingBox bb) {
		this.bb = bb;
	}

	public void deleteBoundingBox() {
		this.bb = null;
	}

	public void updateGrid() {
		this.updateGrid(new Vector(0, 0));
	}

	public void updateGrid(Vector change) {
		Vector[] new_grid = new Vector[rows * cols];
		for (int x = 0; x < rows * cols; x++) {
			new_grid[x] = null;
		}

		active = new ArrayList<>();
		half_active = new ArrayList<>();

		ArrayList<Integer> remover = new ArrayList<>();

		for (int x = 0; x < points.size(); x++) {
			Vector new_pos = grid[points.get(x).getPos()];
			// println(new_pos);
			new_pos = new_pos.add(change);

			int grid_x = (int) Math.floor(new_pos.x / w); // Calculating grid position
			int grid_y = (int) Math.floor(new_pos.y / w);

			if (grid_x >= 0 && grid_x < cols && grid_y >= 0 && grid_y < rows
					&& (bb == null || (bb.intersecting(new_pos) || bb.closest(new_pos).dist(new_pos) < 2 * r))) {
				new_grid[grid_x + grid_y * cols] = new_pos;
				points.get(x).setPos(grid_x + grid_y * cols);

				if (bb != null && !bb.intersecting(new_pos)) {
					half_active.add(grid_x + grid_y * cols);
					points.get(x).setMode(2);
				} else if (points.get(x).getMode() == 2 || points.get(x).getMode() == 1) {
					active.add(grid_x + grid_y * cols);
					points.get(x).setMode(1);
				} else {
					points.get(x).setMode(0);
				}
			} else {
				remover.add(x);
			}
		}

		for (int x = remover.size() - 1; x >= 0; x--) {
			// print("First ");
			// println(points.get(remover.get(x)).getPos());
			// println(points.size());
			points.remove((int) remover.get(x));
			// print("Second ");
			// println(points.get(remover.get(x)).getPos());
			// println(points.size());
		}

		grid = new_grid;
	}

	public ArrayList<ReturnPoint> getPoints() {
		ArrayList<ReturnPoint> return_list = new ArrayList<>();
		for (int x = 0; x < points.size(); x++) {
			return_list.add(new ReturnPoint(points.get(x), grid));
		}
		return return_list;
	}
}
