package eu.the5zig.mod.manager;

import com.google.common.collect.Lists;
import eu.the5zig.mod.listener.Listener;

import java.util.List;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ChatTypingManager extends Listener {

	private UUID typingTo = null;
	private final List<UUID> typingFrom = Lists.newArrayList();

	/**
	 * Returns the Friend the Player is currently typing to or {@code null} if there is not Friend the Player is typing to.
	 *
	 * @return if the Friend the Player is currently typing to or {@code null} if there is not Friend the Player is typing to.
	 */
	public UUID getTypingTo() {
		return typingTo;
	}

	/**
	 * Sets the Friend the Player is currently typing to.
	 *
	 * @param typingTo The Friend the Player is currently typing to.
	 */
	public void setTypingTo(UUID typingTo) {
		this.typingTo = typingTo;
	}

	/**
	 * Adds a Friend to the typing List.
	 *
	 * @param friend The Friend that should be added.
	 * @return If the Friend already existed in the typing List and if not so, if it has been successfully been inserted.
	 */
	public boolean addToTyping(UUID friend) {
		synchronized (typingFrom) {
			return !typingFrom.contains(friend) && typingFrom.add(friend);
		}
	}

	/**
	 * Removes a Friend from the typing List.
	 *
	 * @param friend The Friend that should be removed.
	 * @return If the Friend has been successfully removed from the List.
	 */
	public boolean removeFromTyping(UUID friend) {
		synchronized (typingFrom) {
			return typingFrom.remove(friend);
		}
	}

	/**
	 * Returns if the Friend is currently typing to the Player.
	 *
	 * @param friend The Friend of the Player.
	 * @return If the Friend is currently typing to the Player.
	 */
	public boolean isTyping(UUID friend) {
		synchronized (typingFrom) {
			return typingFrom.contains(friend);
		}
	}
}
