package eu.the5zig.mod.gui;

public abstract class IWrappedGui extends Gui {

	public IWrappedGui() {
		super(null);
	}

	public abstract Object getWrapped();

}
