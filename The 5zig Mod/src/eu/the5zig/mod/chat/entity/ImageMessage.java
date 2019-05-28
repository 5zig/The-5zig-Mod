package eu.the5zig.mod.chat.entity;

public class ImageMessage extends FileMessage {

	public ImageMessage(Conversation conversation, int id, String username, String message, long time, MessageType type) {
		super(conversation, id, username, message, time, type);
	}

	public ImageMessage(Conversation conversation, int id, String username, ImageData imageData, long time, MessageType type) {
		super(conversation, id, username, imageData, time, type);
	}

	@Override
	protected Class<? extends FileData> getDataClass() {
		return ImageData.class;
	}

	public static class ImageData extends FileData {

		private int width;
		private int height;
		private int realWidth, realHeight;

		public ImageData() {
			super();
			width = 100;
			height = 50;
		}

		public ImageData(Status status) {
			super(status);
			width = 100;
			height = 50;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getRealWidth() {
			return realWidth;
		}

		public void setRealWidth(int realWidth) {
			this.realWidth = realWidth;
		}

		public int getRealHeight() {
			return realHeight;
		}

		public void setRealHeight(int realHeight) {
			this.realHeight = realHeight;
		}
	}
}
