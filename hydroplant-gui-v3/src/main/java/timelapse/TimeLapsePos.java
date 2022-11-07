package timelapse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TimeLapsePos {
	public ArrayList<String> dates;
	public ArrayList<String> times;
	public ArrayList<Double> pos;
	public ArrayList<Double> angle;
	public ArrayList<Boolean> already_done;
	public int id;
	
	public TimeLapsePos() {
		this.dates = new ArrayList<String>();
		this.times = new ArrayList<String>();
		this.pos = new ArrayList<Double>();
		this.angle = new ArrayList<Double>();
		this.already_done = new ArrayList<Boolean>();
		this.id = -1;
	}
	
	public TimeLapsePos(ArrayList<LocalDate> dates, ArrayList<LocalTime> times, ArrayList<Double> pos, ArrayList<Double> angle, int id) {
		this.id = id;
		this.dates = new ArrayList<String>();
		this.times = new ArrayList<String>();
		this.pos = pos;
		this.angle = angle;
		this.already_done = new ArrayList<Boolean>();
		
		for(int x = 0; x < dates.size(); x++) {
			this.dates.add(dates.get(x).toString());
			this.times.add(times.get(x).toString());
		}
	}
}
