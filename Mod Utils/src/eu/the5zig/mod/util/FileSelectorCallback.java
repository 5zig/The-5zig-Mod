package eu.the5zig.mod.util;

import java.io.File;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface FileSelectorCallback {

	void onDone(File file);

	String getTitle();

}
