import eu.the5zig.mod.gui.elements.IWrappedTextfield;

public class WrappedTextfield implements IWrappedTextfield {

	private bcd handle;

	public WrappedTextfield(bcd handle) {
		this.handle = handle;
	}

	@Override
	public boolean isSelected() {
		return handle.l();
	}
}
