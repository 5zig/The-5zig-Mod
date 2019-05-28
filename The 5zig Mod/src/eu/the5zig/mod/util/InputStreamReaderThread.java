package eu.the5zig.mod.util;

import eu.the5zig.mod.installer.ProcessCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamReaderThread extends Thread {

	private final InputStream inputStream;
	private final ProcessCallback callback;

	public InputStreamReaderThread(String name, InputStream inputStream, ProcessCallback callback) {
		super(name);
		this.inputStream = inputStream;
		this.callback = callback;
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				callback.log(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ignored) {
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
}
