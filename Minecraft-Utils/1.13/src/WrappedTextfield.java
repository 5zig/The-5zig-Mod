import eu.the5zig.mod.gui.elements.IWrappedTextfield;

public class WrappedTextfield implements IWrappedTextfield {

	private cgn handle;

	public WrappedTextfield(cgn handle) {
		this.handle = handle;
	}

	@Override
	public boolean isSelected() {
		return handle.m();
	}
}
