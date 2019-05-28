import eu.the5zig.mod.gui.elements.IWrappedTextfield;

public class WrappedTextfield implements IWrappedTextfield {

	private bje handle;

	public WrappedTextfield(bje handle) {
		this.handle = handle;
	}

	@Override
	public boolean isSelected() {
		return handle.m();
	}
}
