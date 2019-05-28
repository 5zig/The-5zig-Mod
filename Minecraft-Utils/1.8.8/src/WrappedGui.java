import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.IWrappedGui;

public class WrappedGui extends IWrappedGui {

	private axu child;

	public WrappedGui(axu gui) {
		this.child = gui;
	}

	@Override
	public void initGui() {
		MinecraftFactory.getVars().displayScreen(child);
	}

	@Override
	protected void actionPerformed(IButton button) {
	}

	@Override
	public Object getWrapped() {
		return child;
	}
}
