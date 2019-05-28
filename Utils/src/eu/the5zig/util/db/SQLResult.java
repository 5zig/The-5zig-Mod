package eu.the5zig.util.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class SQLResult<T> {

	private List<T> results = new ArrayList<T>();

	public SQLResult() {
	}

	public void add(T result) {
		results.add(result);
	}

	public T unique() {
		if (results.size() == 0)
			return null;
		return results.get(0);
	}

	public List<T> getAll() {
		return results;
	}

}
