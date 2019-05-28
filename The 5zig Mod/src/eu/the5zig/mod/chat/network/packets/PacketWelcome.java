package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.GuiWelcome;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketWelcome implements Packet {

	@Override
	public void read(ByteBuf buffer) throws IOException {

	}

	@Override
	public void write(ByteBuf buffer) throws IOException {

	}

	@Override
	public void handle() {
		The5zigMod.getScheduler().postToMainThread(new Runnable() {
			@Override
			public void run() {
				The5zigMod.getVars().displayScreen(new GuiWelcome());
			}
		});
	}
}
