package eu.the5zig.mod.chat.network.filetransfer;

public class FileTransferException extends Exception {

	public FileTransferException() {
	}

	public FileTransferException(String message) {
		super(message);
	}

	public FileTransferException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileTransferException(Throwable cause) {
		super(cause);
	}

}
