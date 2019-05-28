import eu.the5zig.mod.gui.elements.IWrappedTextfield;

public class WrappedTextfield implements IWrappedTextfield {

	private bjc handle;

	public WrappedTextfield(bjc handle) {
		this.handle = handle;
	}

	@Override
	public boolean isSelected() {
		return handle.m();
	}
}
