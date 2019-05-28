package eu.the5zig.util;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Container<V> {

	private V value;

	public Container(V value) {
		this.value = value;
	}

	public Container() {
	}

	public void setValue(V value) {
		this.value = value;
	}

	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
}
