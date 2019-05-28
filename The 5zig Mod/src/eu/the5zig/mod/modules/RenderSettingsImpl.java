package eu.the5zig.mod.modules;

public class RenderSettingsImpl implements RenderSettings {

	private final float scale;

	public RenderSettingsImpl(float scale) {
		this.scale = scale;
	}

	@Override
	public float getScale() {
		return scale;
	}

	public static void assign(RenderSettingsImpl renderSettings, AbstractModuleItem abstractModuleItem) {
		abstractModuleItem.renderSettings = renderSettings;
	}
}
