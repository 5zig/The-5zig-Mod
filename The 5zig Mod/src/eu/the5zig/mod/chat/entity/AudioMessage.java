package eu.the5zig.mod.chat.entity;

public class AudioMessage extends FileMessage {

	public AudioMessage(Conversation conversation, int id, String username, String message, long time, MessageType type) {
		super(conversation, id, username, message, time, type);
	}

	public AudioMessage(Conversation conversation, int id, String username, FileData fileData, long time, MessageType type) {
		super(conversation, id, username, fileData, time, type);
	}

	@Override
	protected Class<? extends FileData> getDataClass() {
		return AudioData.class;
	}

	public static class AudioData extends FileData {

		public AudioData() {
			super();
		}

		public AudioData(Status status) {
			super(status);
		}

	}
}
