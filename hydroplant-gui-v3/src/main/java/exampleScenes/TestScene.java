package exampleScenes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;

import exampleLayouts.RemoveLayout;
import exampleSceneObjects.TimeLapse;
import gui.Scene;
import gui.constants;
import gui.variables;
import javafx2.Rectangle2;
import sceneObjects.FlatLayout;
import sceneObjects.MiniScene;

public class TestScene extends Scene {
	final double scroll_factor = 0.9;
	
	TimeLapse tl1;
	
	Rectangle2 scroll_clip;
	MiniScene scroll;
	
	public TestScene() {
		scroll = new MiniScene();
		scroll.setPosition(scroll_factor, scroll_factor);
		
		tl1 = new TimeLapse();
		tl1.setShape(1000, 250);
		tl1.setPosition(100, 200, 0);
		tl1.setDate(LocalDate.of(2003, 12, 28), LocalDate.of(2043, 3, 17));
		tl1.setSpeed(32);
		tl1.setDuration(Duration.ofSeconds(34289));
		tl1.setFPS(59.3424);
		tl1.setPictures(435);
		tl1.setTime(LocalTime.of(1, 5), LocalTime.of(5, 23, 46));
		tl1.setMode(2);
		
		addObject(tl1);
	}
	
	public void updateSize() {
		tl1.setOutline(variables.height * constants.height_outline);
	}
	
	public int mouseClick(double mousex, double mousey) {
		return -1;
	}
}
