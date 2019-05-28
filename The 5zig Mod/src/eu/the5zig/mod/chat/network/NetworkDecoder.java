package eu.the5zig.mod.chat.network;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.packets.Packet;
import eu.the5zig.mod.chat.network.packets.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NetworkDecoder extends ByteToMessageDecoder {

	private NetworkManager networkManager;

	public NetworkDecoder(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> objects) throws Exception {
		if (byteBuf.readableBytes() < 1) {
			return;
		}
		The5zigMod.getDataManager().getNetworkStats().onPacketReceive(byteBuf);
		int packetSize = byteBuf.readableBytes();
		int id = PacketBuffer.readVarIntFromBuffer(byteBuf);
		Packet packet = networkManager.getProtocol().getPacket(id);
		packet.read(byteBuf);
		The5zigMod.logger.debug(The5zigMod.networkMarker, "IN | {} ({} bytes)", packet.toString(), packetSize);

		if (byteBuf.readableBytes() > 0) {
			throw new IOException("Packet " + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, " +
					"found " + byteBuf.readableBytes() + " bytes extra whilst reading packet " + packet);
		} else {
			objects.add(packet);
		}
	}
}
