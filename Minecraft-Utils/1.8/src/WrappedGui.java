import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.IWrappedGui;

public class WrappedGui extends IWrappedGui {

	private bxf child;

	public WrappedGui(bxf gui) {
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
