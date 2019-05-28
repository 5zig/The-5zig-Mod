package eu.the5zig.mod.installer;

public abstract class ProcessCallback {

	private int currentStage = 0;

	public void setStage(Stage stage) {
		message(Stage.values()[currentStage = stage.ordinal()].getMessage() + " (" + (currentStage + 1) + "/" + Stage.values().length + ")");
	}

	public void setProgress(float progress) {
		float startPercentage = Stage.values()[currentStage].getStartPercentage();
		float endPercentage = currentStage == Stage.values().length - 1 ? 1 : Stage.values()[currentStage + 1].getStartPercentage();
		progress(startPercentage + (endPercentage - startPercentage) * progress);
	}

	protected abstract void progress(float percentage);

	protected abstract void message(String message);

	public void log(String message) {
		System.out.println(message);
	}

}
