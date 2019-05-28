package eu.the5zig.mod.util;

public abstract class Wrapped<CLASS> {

	private CLASS wrapped;

	protected Wrapped(CLASS wrapped) {
		this.wrapped = wrapped;
	}

	public CLASS getWrapped() {
		return wrapped;
	}
}
