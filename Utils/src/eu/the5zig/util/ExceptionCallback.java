package eu.the5zig.util;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface ExceptionCallback<T> {

	void call(T callback, Throwable throwable);

}
