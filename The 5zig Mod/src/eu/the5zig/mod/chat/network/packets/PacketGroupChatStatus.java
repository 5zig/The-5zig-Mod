package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.GroupMember;
import eu.the5zig.mod.chat.entity.Conversation;
import eu.the5zig.mod.chat.entity.ConversationGroupChat;
import eu.the5zig.mod.chat.entity.Group;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketGroupChatStatus implements Packet {

	private int groupId;
	private GroupAction groupAction;
	private User user;
	private String player;
	private UUID uuid;
	private boolean enabled;

	public PacketGroupChatStatus(int groupId, GroupAction action, String player) {
		this.groupId = groupId;
		this.groupAction = action;
		this.player = player;
	}

	public PacketGroupChatStatus(int groupId, GroupAction action, UUID player) {
		this.groupId = groupId;
		this.groupAction = action;
		this.uuid = player;
	}

	public PacketGroupChatStatus(int groupId, GroupAction action, UUID player, boolean enabled) {
		this.groupId = groupId;
		this.groupAction = action;
		this.uuid = player;
		this.enabled = enabled;
	}

	public PacketGroupChatStatus() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.groupId = PacketBuffer.readVarIntFromBuffer(buffer);
		int ordinal = PacketBuffer.readVarIntFromBuffer(buffer);
		if (ordinal < 0 || ordinal >= GroupAction.values().length)
			throw new IllegalArgumentException("Received Integer is out of enum range");
		this.groupAction = GroupAction.values()[ordinal];
		if (groupAction == GroupAction.ADD_PLAYER || groupAction == GroupAction.REMOVE_PLAYER || groupAction == GroupAction.OWNER)
			this.user = PacketBuffer.readUser(buffer);
		if (groupAction == GroupAction.ADMIN) {
			this.uuid = PacketBuffer.readUUID(buffer);
			this.enabled = buffer.readBoolean();
		}
		if (groupAction == GroupAction.CHANGE_NAME)
			this.player = PacketBuffer.readString(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, groupId);
		PacketBuffer.writeVarIntToBuffer(buffer, groupAction.ordinal());
		if (groupAction == GroupAction.ADD_PLAYER || groupAction == GroupAction.CHANGE_NAME)
			PacketBuffer.writeString(buffer, player);
		if (groupAction == GroupAction.REMOVE_PLAYER || groupAction == GroupAction.OWNER)
			PacketBuffer.writeUUID(buffer, uuid);
		if (groupAction == GroupAction.ADMIN) {
			PacketBuffer.writeUUID(buffer, uuid);
			buffer.writeBoolean(enabled);
		}
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		Group group = The5zigMod.getGroupChatManager().getGroup(groupId);
		if (group == null)
			return;
		switch (groupAction) {
			case ADD_PLAYER:
				group.addMember(user);
				break;
			case REMOVE_PLAYER:
				group.removeMember(user.getUniqueId());
				break;
			case ADMIN:
				group.getMember(uuid).setType(enabled ? GroupMember.ADMIN : GroupMember.MEMBER);
				break;
			case OWNER:
				group.getMember(group.getOwner().getUniqueId()).setType(GroupMember.MEMBER);
				group.setOwner(user);
				group.getMember(user.getUniqueId()).setType(GroupMember.OWNER);
				break;
			case CHANGE_NAME:
				group.setName(player);
				for (Conversation conversation : The5zigMod.getConversationManager().getConversations()) {
					if (!(conversation instanceof ConversationGroupChat))
						return;
					ConversationGroupChat c = (ConversationGroupChat) conversation;
					if (c.getGroupId() == groupId)
						c.setName(player);
				}
				break;
			default:
				break;
		}
	}

	public enum GroupAction {
		ADD_PLAYER, REMOVE_PLAYER, OWNER, ADMIN, CHANGE_NAME
	}
}
