package exampleLayouts;

import javafx.scene.paint.Color;

public class ECTDSButton extends DashboardButton {
	Color c_optimal = Color.rgb(219, 255, 223);
	Color c_suboptimal = Color.rgb(255, 50, 50);

	double t_optimal = 0;
	double t_tol = 0;
	double t_max = 0;
	double t_min = 0;
	
	double e_optimal = 0;
	double e_tol = 0;
	double e_max = 0;
	double e_min = 0;

	double ec;
	double tds;
	
	String ec_or_tds = "ec";

	public ECTDSButton() {
		super();
		this.tds = 0;
		this.ec = 0;
		title.setText2("EC");
		unit.setText2("mS/cm");
	}
	
	public void setECorTDS(String option) {
		ec_or_tds = option;
		if(option == "ec") {
			title.setText2("EC");
			unit.setText2("mS/cm");
			information.setText2(String.format("%.1f", ec));
		} else {
			information.setText2(String.format("%.1f", tds));
			title.setText2("TDS");
			unit.setText2("ppm");
		}
	}

	public void setTDS(double tds) {
		this.tds = tds;
		if(ec_or_tds == "tds") information.setText2(String.format("%.1f", tds));
		if (tds <= t_optimal + t_tol && tds >= t_optimal - t_tol) {
			bg_rec.setFill(c_optimal);
		} else if (tds >= t_max) {
			bg_rec.setFill(c_suboptimal);
		} else if (tds <= t_min) {
			bg_rec.setFill(c_suboptimal);
		} else if (tds < t_max && tds > t_optimal + t_tol) {
			double tds_perc = ((tds - (t_optimal + t_tol))) / (t_max - (t_optimal + t_tol));
			bg_rec.setFill(
					Color.rgb((int) ((1 - tds_perc) * c_optimal.getRed() * 255 + tds_perc * c_suboptimal.getRed() * 255),
							(int) ((1 - tds_perc) * c_optimal.getGreen() * 255 + tds_perc * c_suboptimal.getGreen() * 255),
							(int) ((1 - tds_perc) * c_optimal.getBlue() * 255 + tds_perc * c_suboptimal.getBlue() * 255)));
		} else {
			double tds_perc = (tds - t_min) / ((t_optimal - t_tol) - t_min);
			bg_rec.setFill(
					Color.rgb((int) ((1 - tds_perc) * c_suboptimal.getRed() * 255 + tds_perc * c_optimal.getRed() * 255),
							(int) ((1 - tds_perc) * c_suboptimal.getGreen() * 255 + tds_perc * c_optimal.getGreen() * 255),
							(int) ((1 - tds_perc) * c_suboptimal.getBlue() * 255 + tds_perc * c_optimal.getBlue() * 255)));
		}
	}
	
	public void setEC(double ec) {
		this.ec = ec;
		if(ec_or_tds == "ec") information.setText2(String.format("%.1f", ec));
		if (ec <= e_optimal + e_tol && ec >= e_optimal - e_tol) {
			bg_rec.setFill(c_optimal);
		} else if (ec >= e_max) {
			bg_rec.setFill(c_suboptimal);
		} else if (ec <= e_min) {
			bg_rec.setFill(c_suboptimal);
		} else if (ec < e_max && ec > e_optimal + e_tol) {
			double ec_perc = ((ec - (e_optimal + e_tol))) / (e_max - (e_optimal + e_tol));
			bg_rec.setFill(
					Color.rgb((int) ((1 - ec_perc) * c_optimal.getRed() * 255 + ec_perc * c_suboptimal.getRed() * 255),
							(int) ((1 - ec_perc) * c_optimal.getGreen() * 255 + ec_perc * c_suboptimal.getGreen() * 255),
							(int) ((1 - ec_perc) * c_optimal.getBlue() * 255 + ec_perc * c_suboptimal.getBlue() * 255)));
		} else {
			double ec_perc = (ec - e_min) / ((e_optimal - e_tol) - e_min);
			bg_rec.setFill(
					Color.rgb((int) ((1 - ec_perc) * c_suboptimal.getRed() * 255 + ec_perc * c_optimal.getRed() * 255),
							(int) ((1 - ec_perc) * c_suboptimal.getGreen() * 255 + ec_perc * c_optimal.getGreen() * 255),
							(int) ((1 - ec_perc) * c_suboptimal.getBlue() * 255 + ec_perc * c_optimal.getBlue() * 255)));
		}
	}

	public void setTDSs(double min, double opt, double max, double tol) {
		this.t_optimal = opt;
		this.t_min = min;
		this.t_max = max;
		this.t_tol = tol; // Toleranz des optimalen Wasser TDS-Werts +-
	}
	
	public void setECs(double min, double opt, double max, double tol) {
		this.e_optimal = opt;
		this.e_min = min;
		this.e_max = max;
		this.e_tol = tol; // Toleranz des optimalen Wasser EC-Werts +-
	}
}
