package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import eu.the5zig.mod.chat.party.PartyManager;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

public class PacketPartyInviteDelete implements Packet {

	private UUID partyOwnerId;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.partyOwnerId = PacketBuffer.readUUID(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		for (Iterator<PartyManager.PartyOwner> iterator = The5zigMod.getPartyManager().getPartyInvitations().iterator(); iterator.hasNext(); ) {
			if (iterator.next().getUniqueId().equals(partyOwnerId)) {
				iterator.remove();
			}
		}
	}

}
