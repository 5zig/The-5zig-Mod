import eu.the5zig.mod.gui.elements.IWrappedTextfield;

public class WrappedTextfield implements IWrappedTextfield {

	private bdd handle;

	public WrappedTextfield(bdd handle) {
		this.handle = handle;
	}

	@Override
	public boolean isSelected() {
		return handle.m();
	}
}
