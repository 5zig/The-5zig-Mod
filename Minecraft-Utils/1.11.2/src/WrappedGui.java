import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.IWrappedGui;
import eu.the5zig.mod.gui.elements.IButton;

public class WrappedGui extends IWrappedGui {

	private bho child;

	public WrappedGui(bho gui) {
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
