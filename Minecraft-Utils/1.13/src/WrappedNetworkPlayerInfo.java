import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;

public class WrappedNetworkPlayerInfo implements NetworkPlayerInfo {

	private cqw wrapped;

	public WrappedNetworkPlayerInfo(cqw wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public GameProfile getGameProfile() {
		return wrapped.a();
	}

	@Override
	public String getDisplayName() {
		String displayName = wrapped.l() == null ? null : wrapped.l().d();
		if (displayName != null && displayName.startsWith(ChatColor.YELLOW + "* ")) {
			displayName = displayName.substring(displayName.indexOf("* ") + 2);
		}
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		if (displayName != null && !ChatColor.stripColor(displayName).equals(getGameProfile().getName())) {
			displayName = ChatColor.YELLOW + "* " + ChatColor.RESET + displayName;
		}
		wrapped.a(displayName == null ? null : ChatComponentBuilder.fromLegacyText(displayName));
	}

	@Override
	public int getPing() {
		return wrapped.c();
	}

}
