package eu.the5zig.mod.listener;

import io.netty.buffer.ByteBuf;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Listener implements IListener {

	@Override
	public void onTick() {
	}

	@Override
	public void onKeyPress(int code) {
	}

	@Override
	public void onServerJoin(String host, int port) {
	}

	@Override
	public void onServerConnect() {
	}

	@Override
	public void onServerDisconnect() {
	}

	@Override
	public void onPayloadReceive(String channel, ByteBuf packetData) {
	}

	/**
	 * Handles all received chat messages.
	 *
	 * @param message The message that has been received.
	 * @return true, if the message should be ignored.
	 */
	@Override
	public boolean onServerChat(String message) {
		return false;
	}

	@Override
	public boolean onServerChat(String message, Object chatComponent) {
		return false;
	}

	@Override
	public boolean onActionBar(String message) {
		return false;
	}

	@Override
	public void onPlayerListHeaderFooter(String header, String footer) {
	}

	@Override
	public void onTitle(String title, String subTitle) {
	}
}
