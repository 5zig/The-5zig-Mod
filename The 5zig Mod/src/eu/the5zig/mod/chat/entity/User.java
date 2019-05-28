package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.Row;

import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class User implements Row {

	protected String username;
	protected final UUID uuid;

	public User(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public int getLineHeight() {
		return 18;
	}

	@Override
	public void draw(int x, int y) {
		Gui currentScreen = The5zigMod.getVars().getCurrentScreen();
		Gui.drawCenteredString(getUsername(), currentScreen.getWidth() / 2, y + 2);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;

		User user = (User) o;

		return uuid != null ? uuid.equals(user.uuid) : user.uuid == null;
	}

	@Override
	public int hashCode() {
		return uuid != null ? uuid.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", uuid=" + uuid +
				'}';
	}
}
