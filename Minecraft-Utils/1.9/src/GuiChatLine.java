public class GuiChatLine {

	private final int updateCounterCreated;
	private final eu lineString;

	/**
	 * int value to refer to existing Chat Lines, can be 0 which means unreferrable
	 */
	private final int chatLineID;

	public GuiChatLine(int updateCounterCreated, eu lineString, int id)
	{
		this.lineString = lineString;
		this.updateCounterCreated = updateCounterCreated;
		this.chatLineID = id;
	}

	public eu getChatComponent()
	{
		return this.lineString;
	}

	public int getUpdatedCounter()
	{
		return this.updateCounterCreated;
	}

	public int getChatLineID()
	{
		return this.chatLineID;
	}

}
