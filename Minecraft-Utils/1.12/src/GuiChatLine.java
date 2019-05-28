public class GuiChatLine {

	private final int updateCounterCreated;
	private final hh lineString;

	/**
	 * int value to refer to existing Chat Lines, can be 0 which means unreferrable
	 */
	private final int chatLineID;

	public GuiChatLine(int updateCounterCreated, hh lineString, int id) {
		this.lineString = lineString;
		this.updateCounterCreated = updateCounterCreated;
		this.chatLineID = id;
	}

	public hh getChatComponent() {
		return this.lineString;
	}

	public int getUpdatedCounter() {
		return this.updateCounterCreated;
	}

	public int getChatLineID() {
		return this.chatLineID;
	}

}
