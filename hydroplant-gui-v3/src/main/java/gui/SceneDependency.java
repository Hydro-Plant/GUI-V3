package gui;

public class SceneDependency {
	int trigger;
	int loading_mode;
	SceneBundle new_scene;

	SceneDependency(int trigger, SceneBundle new_scene, int loading_mode) {
		this.trigger = trigger;
		this.new_scene = new_scene;
		this.loading_mode = loading_mode;
	}

	SceneDependency(int trigger, SceneBundle new_scene) {
		this.trigger = trigger;
		this.new_scene = new_scene;
		this.loading_mode = 0;
	}
}