package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.GroupMember;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import eu.the5zig.mod.chat.party.Party;
import eu.the5zig.mod.gui.GuiParty;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketPartyInviteResponse implements Packet {

	private UUID partyOwnerId;
	private boolean flag;

	private Party party;

	public PacketPartyInviteResponse(UUID partyOwnerId, boolean accepted) {
		this.partyOwnerId = partyOwnerId;
		this.flag = accepted;
	}

	public PacketPartyInviteResponse() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		long created = buffer.readLong();
		String server = PacketBuffer.readString(buffer);
		User owner = null;
		int size = PacketBuffer.readVarIntFromBuffer(buffer);
		List<GroupMember> members = new ArrayList<GroupMember>(size);
		for (int i = 0; i < size; i++) {
			User member = PacketBuffer.readUser(buffer);
			int type = PacketBuffer.readVarIntFromBuffer(buffer);
			if (type == GroupMember.OWNER)
				owner = member;
			members.add(new GroupMember(member, type));
		}
		if (owner == null)
			owner = new User("unknown", UUID.randomUUID());
		party = new Party(owner, created, server, members);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, partyOwnerId);
		buffer.writeBoolean(flag);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getPartyManager().getPartyInvitations().clear();
		The5zigMod.getPartyManager().setParty(party);
		if (The5zigMod.getVars().getCurrentScreen() instanceof GuiParty) {
			The5zigMod.getVars().getCurrentScreen().initGui0();
		}
	}

	@Override
	public String toString() {
		return "PacketPartyInviteResponse{" + "partyOwnerId=" + partyOwnerId + ", flag=" + flag + ", party=" + party + '}';
	}
}
