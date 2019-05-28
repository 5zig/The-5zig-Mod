import eu.the5zig.mod.gui.elements.IWrappedTextfield;

public class WrappedTextfield implements IWrappedTextfield {

	private bul handle;

	public WrappedTextfield(bul handle) {
		this.handle = handle;
	}

	@Override
	public boolean isSelected() {
		return handle.m();
	}
}
