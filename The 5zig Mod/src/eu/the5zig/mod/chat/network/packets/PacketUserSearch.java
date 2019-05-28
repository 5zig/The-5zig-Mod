package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import eu.the5zig.mod.gui.GuiAddFriend;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketUserSearch implements Packet {

	private Type type;
	private User[] users;

	public PacketUserSearch(Type type, User[] users) {
		this.type = type;
		this.users = users;
	}

	public PacketUserSearch() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		type = PacketBuffer.readEnum(buffer, Type.class);
		users = new User[PacketBuffer.readVarIntFromBuffer(buffer)];
		for (int i = 0; i < users.length; i++) {
			users[i] = PacketBuffer.readUser(buffer);
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeEnum(buffer, type);
		PacketBuffer.writeVarIntToBuffer(buffer, users.length);
		for (User user : users) {
			PacketBuffer.writeString(buffer, user.getUsername());
		}
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		if (type == Type.KEYWORD) {
			if (!(The5zigMod.getVars().getCurrentScreen() instanceof GuiAddFriend)) {
				return;
			}

			GuiAddFriend gui = (GuiAddFriend) The5zigMod.getVars().getCurrentScreen();
			if (gui.getTextfieldById(1).getText().length() >= 3) {
				gui.rows.clear();
				for (User user : users) {
					gui.rows.add(new GuiAddFriend.ProfileRow(user));
				}
			}
		} else if (type == Type.FRIEND_LIST) {
			int newSuggestions = The5zigMod.getFriendManager().addSuggestions(users);
			if (newSuggestions > 0) {
				The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.YELLOW + I18n.translate("friend.invite.suggestions.new", users.length));
			}
		}
	}

	public enum Type {
		KEYWORD, FRIEND_LIST
	}
}
