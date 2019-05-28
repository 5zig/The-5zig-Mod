package eu.the5zig.mod.chat.network.packets;


import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PacketPartyInvite implements Packet {

	private User partyOwner;
	private List<UUID> members;

	public PacketPartyInvite(List<UUID> members) {
		this.members = members;
	}

	public PacketPartyInvite() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		partyOwner = PacketBuffer.readUser(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		int size = members.size();
		PacketBuffer.writeVarIntToBuffer(buffer, size);
		for (int i = 0; i < size; i++) {
			PacketBuffer.writeUUID(buffer, members.get(i));
		}
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		if (The5zigMod.getPartyManager().addPartyInvitation(partyOwner)) {
			The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.YELLOW + I18n.translate("party.messages.invited", partyOwner.getUsername()));
		}
	}

	@Override
	public String toString() {
		return "PacketPartyInvite{" + "partyOwner=" + partyOwner + ", members=" + members + '}';
	}
}
