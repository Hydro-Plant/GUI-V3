package timelapse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TimeLapsePos {
	public ArrayList<String> dates;
	public ArrayList<String> times;
	public ArrayList<Double> pos;
	public ArrayList<Double> angle;
	public long at_image = 0;
	public int id;

	public TimeLapsePos() {
		this.dates = new ArrayList<>();
		this.times = new ArrayList<>();
		this.pos = new ArrayList<>();
		this.angle = new ArrayList<>();
		this.id = -1;
	}

	public TimeLapsePos(ArrayList<LocalDate> dates, ArrayList<LocalTime> times, ArrayList<Double> pos, ArrayList<Double> angle, int id) {
		this.id = id;
		this.dates = new ArrayList<>();
		this.times = new ArrayList<>();
		this.pos = pos;
		this.angle = angle;

		for(int x = 0; x < dates.size(); x++) {
			this.dates.add(dates.get(x).toString());
			this.times.add(times.get(x).toString());
		}
	}
}
