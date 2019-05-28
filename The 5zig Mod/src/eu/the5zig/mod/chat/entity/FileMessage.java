package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.The5zigMod;

public abstract class FileMessage extends Message {

	private FileData fileData;
	private float percentage;

	public FileMessage(Conversation conversation, int id, String username, String message, long time, MessageType type) {
		super(conversation, id, username, message, time, type);
		loadData();
	}

	public FileMessage(Conversation conversation, int id, String username, FileData fileData, long time, MessageType type) {
		super(conversation, id, username, "", time, type);
		this.fileData = fileData;
		percentage = fileData.getStatus() == Status.UPLOADED || fileData.getStatus() == Status.DOWNLOADED ? 1 : 0;
		saveData();
	}

	private void loadData() {
		fileData = The5zigMod.gson.fromJson(getMessage(), getDataClass());
		percentage = fileData.getStatus() == Status.UPLOADED || fileData.getStatus() == Status.DOWNLOADED ? 1 : 0;
	}

	public void saveData() {
		setMessage(The5zigMod.gson.toJson(fileData));
	}

	public FileData getFileData() {
		return fileData;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	protected abstract Class<? extends FileData> getDataClass();

	public static abstract class FileData {

		private int fileId;
		private String hash;
		private int status;
		private long length;

		public FileData() {
		}

		public FileData(Status status) {
			this.status = status.ordinal();
		}

		public void setFileId(int fileId) {
			this.fileId = fileId;
		}

		public int getFileId() {
			return fileId;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public Status getStatus() {
			return Status.values()[status];
		}

		public void setStatus(Status status) {
			this.status = status.ordinal();
		}

		public long getLength() {
			return length;
		}

		public void setLength(long length) {
			this.length = length;
		}

		public boolean isOwn() {
			return status >= Status.WAITING.ordinal();
		}
	}

	/**
	 * Status is the first character of the message
	 */
	public enum Status {
		REQUEST,
		REQUEST_ACCEPTED,
		REQUEST_DENIED,
		DOWNLOADING,
		DOWNLOADED,
		DOWNLOAD_FAILED,

		WAITING,
		DENIED,
		ACCEPTED,
		UPLOADING,
		UPLOADED,
		UPLOAD_FAILED,
	}

}
