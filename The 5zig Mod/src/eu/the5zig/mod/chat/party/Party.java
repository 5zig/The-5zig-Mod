package eu.the5zig.mod.chat.party;

import eu.the5zig.mod.chat.GroupMember;
import eu.the5zig.mod.chat.entity.Conversation;
import eu.the5zig.mod.chat.entity.ConversationGroupChat;
import eu.the5zig.mod.chat.entity.Message;
import eu.the5zig.mod.chat.entity.User;

import java.util.List;

public class Party {

	private User owner;
	private long created;
	private String server;
	private List<GroupMember> members;

	private final Conversation partyConversation;

	public Party(User owner, long created, String server, List<GroupMember> members) {
		this.owner = owner;
		this.created = created;
		this.server = server;
		this.members = members;
		this.partyConversation = new ConversationGroupChat(0, 0, owner.getUsername(), created, true, Message.MessageStatus.PENDING, Conversation.Behaviour.DEFAULT);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public List<GroupMember> getMembers() {
		return members;
	}

	public void setMembers(List<GroupMember> members) {
		this.members = members;
	}

	public Conversation getPartyConversation() {
		return partyConversation;
	}
}
