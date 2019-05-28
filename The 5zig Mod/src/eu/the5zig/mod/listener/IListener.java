package eu.the5zig.mod.listener;

import io.netty.buffer.ByteBuf;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface IListener {

	void onTick();

	void onKeyPress(int code);

	void onServerJoin(String host, int port);

	/**
	 * Called, when a MC|Brand Payload has been received. Very effective way to detect server switches!
	 */
	void onServerConnect();

	void onServerDisconnect();

	void onPayloadReceive(String channel, ByteBuf packetData);

	/**
	 * Handles all received chat messages.
	 *
	 * @param message The message that has been received.
	 */
	boolean onServerChat(String message);

	/**
	 * Handles all received chat messages.
	 *
	 * @param message       The Chat Message as String
	 * @param chatComponent The Chat Message as internal Chat Component
	 * @return true, if the message should not be displayed
	 */
	boolean onServerChat(String message, Object chatComponent);

	boolean onActionBar(String message);

	void onPlayerListHeaderFooter(String header, String footer);

	void onTitle(String title, String subTitle);

}