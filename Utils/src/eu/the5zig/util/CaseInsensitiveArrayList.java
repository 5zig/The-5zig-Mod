package eu.the5zig.util;

import java.util.ArrayList;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class CaseInsensitiveArrayList<T> extends ArrayList<T> {

	@Override
	public boolean contains(Object o) {
		for (T t : this) {
			if (t.equals(o))
				return true;
		}
		return false;
	}

}
