import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import org.apache.commons.lang3.StringUtils;

public class WrappedNetworkPlayerInfo implements NetworkPlayerInfo {

	private bjl wrapped;
	private final GameProfile gameProfile;

	public WrappedNetworkPlayerInfo(bjl wrapped) {
		this.wrapped = wrapped;
		if (!StringUtils.isBlank(getDisplayName())) {
			this.gameProfile = new GameProfile(null, getDisplayName());
		} else {
			this.gameProfile = null;
		}
	}

	@Override
	public GameProfile getGameProfile() {
		return gameProfile;
	}

	@Override
	public String getDisplayName() {
		return wrapped.a;
	}

	@Override
	public void setDisplayName(String displayName) {
		MinecraftFactory.getClassProxyCallback().getLogger().warn("Display names cannot be modified in Minecraft 1.7.10!");
	}

	@Override
	public int getPing() {
		return wrapped.b;
	}

}
