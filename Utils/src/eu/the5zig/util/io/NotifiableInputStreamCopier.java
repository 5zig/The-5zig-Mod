package eu.the5zig.util.io;

import eu.the5zig.util.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NotifiableInputStreamCopier implements Runnable {

	private final InputStream[] src;
	private final File dest;
	private final Callback<Float> callback;

	private NotifiableInputStreamCopier(Callback<Float> callback, InputStream[] src, File dest) {
		this.callback = callback;
		this.src = src;
		this.dest = dest;
		run();
	}

	@Override
	public void run() {
		long fileSize = 0;
		for (InputStream is : src) {
			try {
				fileSize += is.available();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (InputStream is : src) {
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(dest);
				byte[] buf = new byte[8192];
				int bytesRead;
				while ((bytesRead = is.read(buf)) > 0) {
					output.write(buf, 0, bytesRead);
					if (callback != null)
						callback.call((float) dest.length() / (float) fileSize);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static NotifiableInputStreamCopier copy(Callback<Float> callback, InputStream[] src, File dest) {
		return new NotifiableInputStreamCopier(callback, src, dest);
	}


}
