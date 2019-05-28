package eu.the5zig.mod.chat.network.filetransfer;

public class FileReceiveManager {

	private int parts;
	private int chunkSize;
	private long totalLength;

	public FileReceiveManager(int parts, int chunkSize, long totalLength) {
		this.parts = parts;
		this.chunkSize = chunkSize;
		this.totalLength = totalLength;
	}
}
