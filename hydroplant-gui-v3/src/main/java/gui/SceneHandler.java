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
			for (int x = 0; x < active_scene.deps.size(); x++) {
				if (active_scene.deps.get(x).trigger == event) {
					int new_mode = active_scene.deps.get(x).loading_mode;
					active_scene = active_scene.deps.get(x).new_scene;
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

	public void mouseClick(double mousex, double mousey) {
		active_scene.scene.mouseClick(mousex, mousey);
	}
}
