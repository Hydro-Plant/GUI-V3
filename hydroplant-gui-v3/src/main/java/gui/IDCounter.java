package gui;

public class IDCounter {
	private static long scene_object_id = 0;
	private static long scene_id = 0;

	public static long getSceneID() {
		scene_id++;
		return scene_id - 1;
	}

	public static long getSceneObjectID() {
		scene_object_id++;
		return scene_object_id - 1;
	}
}