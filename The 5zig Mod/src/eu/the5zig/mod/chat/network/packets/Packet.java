package eu.the5zig.mod.chat.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface Packet {

	void read(ByteBuf buffer) throws IOException;

	void write(ByteBuf buffer) throws IOException;

	void handle();

}