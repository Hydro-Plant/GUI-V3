package gui;

public class SceneHandler {
	SceneBundle active_scene = null;

	public void setScene(SceneBundle active_scene) {
		this.active_scene = active_scene;
	}

	public boolean handle() {
		int event = active_scene.scene.sceneEvents();
		boolean res = false;
		if (event != -1) {
			for (SceneDependency element : active_scene.deps) {
				if (element.trigger == event) {
					int new_mode = element.loading_mode;
					active_scene = element.new_scene;
					active_scene.scene.loadMode(new_mode);
					active_scene.scene.updateSize();
					res = true;
				}
			}
		}
		if (active_scene != null)
			active_scene.scene.update();
		return res;
	}

	public Scene getActive() {
		return active_scene.scene;
	}

	public void externalButton(int button) {
		active_scene.scene.externalButton(button);
	}

	public void mouseClick() {
		active_scene.scene.mouseClick();
	}
}
