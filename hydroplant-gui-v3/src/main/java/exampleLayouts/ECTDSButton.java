package exampleLayouts;

import javafx.scene.paint.Color;

public class ECTDSButton extends DashboardButton {
	Color c_optimal = Color.rgb(145, 255, 147);
	Color c_suboptimal = Color.rgb(219, 255, 223);

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
		unit.setText2("µS/cm");
	}

	public void setECorTDS(String option) {
		ec_or_tds = option;
		if (option.equals("ec")) {
			title.setText2("EC");
			unit.setText2("µS/cm");
			information.setText2(String.format("%.0f", ec));
		} else {
			information.setText2(String.format("%.0f", tds));
			title.setText2("TDS");
			unit.setText2("ppm");
		}
	}

	public void setTDS(double tds) {
		this.tds = tds;
		if (ec_or_tds.equals("tds")) {
			information.setText2(String.format("%.0f", tds));
			if (tds <= t_optimal + t_tol && tds >= t_optimal - t_tol) {
				bg_rec.setFill(c_optimal);
			} else if (tds >= t_max) {
				bg_rec.setFill(c_suboptimal);
			} else if (tds <= t_min) {
				bg_rec.setFill(c_suboptimal);
			} else if (tds < t_max && tds > t_optimal + t_tol) {
				double tds_perc = ((tds - (t_optimal + t_tol))) / (t_max - (t_optimal + t_tol));
				bg_rec.setFill(Color.rgb(
						(int) ((1 - tds_perc) * c_optimal.getRed() * 255 + tds_perc * c_suboptimal.getRed() * 255),
						(int) ((1 - tds_perc) * c_optimal.getGreen() * 255 + tds_perc * c_suboptimal.getGreen() * 255),
						(int) ((1 - tds_perc) * c_optimal.getBlue() * 255 + tds_perc * c_suboptimal.getBlue() * 255)));
			} else {
				double tds_perc = (tds - t_min) / ((t_optimal - t_tol) - t_min);
				bg_rec.setFill(Color.rgb(
						(int) ((1 - tds_perc) * c_suboptimal.getRed() * 255 + tds_perc * c_optimal.getRed() * 255),
						(int) ((1 - tds_perc) * c_suboptimal.getGreen() * 255 + tds_perc * c_optimal.getGreen() * 255),
						(int) ((1 - tds_perc) * c_suboptimal.getBlue() * 255 + tds_perc * c_optimal.getBlue() * 255)));
			}
		} else if(ec_or_tds.equals("ec")) {
			this.setEC(tds / 500);
		}

	}

	public void setEC(double ec) {
		ec = ec * 1000;					// from mS to uS
		this.ec = ec;
		if (ec_or_tds.equals("ec")) {
			information.setText2(String.format("%.0f", ec));
			if (ec <= e_optimal + e_tol && ec >= e_optimal - e_tol) {
				bg_rec.setFill(c_optimal);
			} else if (ec >= e_max) {
				bg_rec.setFill(c_suboptimal);
			} else if (ec <= e_min) {
				bg_rec.setFill(c_suboptimal);
			} else if (ec < e_max && ec > e_optimal + e_tol) {
				double ec_perc = ((ec - (e_optimal + e_tol))) / (e_max - (e_optimal + e_tol));
				bg_rec.setFill(Color.rgb(
						(int) ((1 - ec_perc) * c_optimal.getRed() * 255 + ec_perc * c_suboptimal.getRed() * 255),
						(int) ((1 - ec_perc) * c_optimal.getGreen() * 255 + ec_perc * c_suboptimal.getGreen() * 255),
						(int) ((1 - ec_perc) * c_optimal.getBlue() * 255 + ec_perc * c_suboptimal.getBlue() * 255)));
			} else {
				double ec_perc = (ec - e_min) / ((e_optimal - e_tol) - e_min);
				bg_rec.setFill(Color.rgb(
						(int) ((1 - ec_perc) * c_suboptimal.getRed() * 255 + ec_perc * c_optimal.getRed() * 255),
						(int) ((1 - ec_perc) * c_suboptimal.getGreen() * 255 + ec_perc * c_optimal.getGreen() * 255),
						(int) ((1 - ec_perc) * c_suboptimal.getBlue() * 255 + ec_perc * c_optimal.getBlue() * 255)));
			}
		} else if(ec_or_tds.equals("tds")) {
			this.setTDS(ec * 500);
		}
	}

	public void setTDSs(double min, double opt, double tol, double max) {
		this.t_optimal = opt;
		this.t_min = min;
		this.t_max = max;
		this.t_tol = tol; // Toleranz des optimalen Wasser TDS-Werts +-

		this.e_optimal = opt * 2;
		this.e_min = min * 2;
		this.e_max = max * 2;
		this.e_tol = tol * 2; // Toleranz des optimalen Wasser EC-Werts +-
	}

	public void setECs(double min, double opt, double tol, double max) {
		this.e_optimal = opt * 1000;
		this.e_min = min * 1000;
		this.e_max = max * 1000;
		this.e_tol = tol * 1000; // Toleranz des optimalen Wasser EC-Werts +-

		this.t_optimal = opt * 500;
		this.t_min = min * 500;
		this.t_max = max * 500;
		this.t_tol = tol * 500; // Toleranz des optimalen Wasser TDS-Werts +-
	}
}
