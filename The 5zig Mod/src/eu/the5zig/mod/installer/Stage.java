package eu.the5zig.mod.installer;

public enum Stage {

	EXTRACT_SOURCES(0, "Extracting Sources to Library File."),
	COPY_MINECRAFT(.25f, "Copying Minecraft Version."),
	APPLY_OPTIFINE_PATCHES(.5f, "Trying to apply Optifine patches."),
	COPY_OTHER_MODS(.55f, "Installing other Mods."),
	UPDATE_LAUNCHER_FILES(.95f, "Updating launcher files.");

	private float startPercentage;
	private String message;

	Stage(float startPercentage, String message) {
		this.startPercentage = startPercentage;
		this.message = message;
	}

	public float getStartPercentage() {
		return startPercentage;
	}

	public void setStartPercentage(float startPercentage) {
		this.startPercentage = startPercentage;
	}

	public String getMessage() {
		return message;
	}
}
