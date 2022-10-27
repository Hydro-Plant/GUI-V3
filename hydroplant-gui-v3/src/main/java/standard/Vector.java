package standard;

public class Vector {
	public double x;
	public double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector() {
		this.x = 0;
		this.y = 0;
	}

	public Vector add(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y);
	}

	public Vector sub(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y);
	}

	public Vector mult(double other) {
		return new Vector(this.x * other, this.y * other);
	}

	public double dist(Vector other) {
		return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
	}

	public Vector setMag(double mag) {
		double old_mag = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
		return new Vector(this.x * mag / old_mag, this.y * mag / old_mag);
	}

	public static Vector fromAngle(double angle) {
		return new Vector(Math.cos(angle), Math.sin(angle));
	}

	@Override
	public String toString() {
		return ("{ " + x + "; " + y + " }");
	}

}