package eu.the5zig.mod.chat.network;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.packets.Packet;
import eu.the5zig.mod.chat.network.packets.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NetworkEncoder extends MessageToByteEncoder<Packet> {

	private NetworkManager networkManager;

	public NetworkEncoder(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
		PacketBuffer.writeVarIntToBuffer(byteBuf, networkManager.getProtocol().getPacketId(packet));
		packet.write(byteBuf);
		The5zigMod.logger.debug(The5zigMod.networkMarker, "OUT| {} ({} bytes)", packet.toString(), byteBuf.readableBytes());
		The5zigMod.getDataManager().getNetworkStats().onPacketSend(byteBuf);
	}
}
