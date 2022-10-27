package gui;

import java.util.ArrayList;

public class SceneBundle {
	Scene scene;
	ArrayList<SceneDependency> deps = new ArrayList<SceneDependency>();

	public SceneBundle(Scene szene) {
		this.scene = szene;
	}

	public void addDep(int trigger, SceneBundle new_scene, int loading_mode) {
		deps.add(new SceneDependency(trigger, new_scene, loading_mode));
	}

	public void addDep(int trigger, SceneBundle new_scene) {
		deps.add(new SceneDependency(trigger, new_scene));
	}

	public Scene getScene() {
		return scene;
	}
}