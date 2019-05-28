package eu.the5zig.util.io;

import eu.the5zig.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NotifiableFileCopier {

	private final File[] src;
	private final File dest;
	private final Callback<Float> callback;

	private NotifiableFileCopier(Callback<Float> callback, File[] src, File dest) throws IOException {
		this.callback = callback;
		this.src = src;
		this.dest = dest;
		run();
	}

	public static NotifiableFileCopier copy(Callback<Float> callback, File[] src, File dest) throws IOException {
		return new NotifiableFileCopier(callback, src, dest);
	}

	public static NotifiableFileCopier copy(File[] src, File dest) throws IOException {
		return copy(null, src, dest);
	}

	public void run() throws IOException {
		long fileSize = 0;
		for (File file : src) {
			fileSize += file.length();
		}

		for (File file : src) {
			FileInputStream input = null;
			FileOutputStream output = null;
			try {
				input = new FileInputStream(file);
				output = new FileOutputStream(dest);
				byte[] buf = new byte[8192];
				int bytesRead;
				while ((bytesRead = input.read(buf)) > 0) {
					output.write(buf, 0, bytesRead);
					if (callback != null)
						callback.call((float) dest.length() / (float) fileSize);
				}
			} finally {
				if (input != null)
					input.close();
				if (output != null)
					output.close();
			}
		}
	}

}
