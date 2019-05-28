package eu.the5zig.mod.server;

/**
 * Contains all information about the current Minecraft server of the player.
 */
public interface IServer {

	/**
	 * @return the host of the current server.
	 */
	String getHost();

	/**
	 * @return the port of the current server.
	 */
	int getPort();

	/**
	 * @return the time the player has joined on this server in milliseconds.
	 */
	long getLastTimeJoined();

	/**
	 * @return true, if the rendering of potion effects is allowed by the current server.
	 */
	boolean isRenderPotionEffects();

	/**
	 * Sets, whether the rendering of potion effects is allowed by the current server.
	 *
	 * @param renderPotionEffects true, if the rendering of potion effects should be allowed by the current server.
	 */
	void setRenderPotionEffects(boolean renderPotionEffects);

	/**
	 * @return true, if the rendering of the players equipped items is allowed by the current server.
	 */
	boolean isRenderArmor();

	/**
	 * Sets, whether the rendering of the players equipped items is allowed by the current server.
	 *
	 * @param renderArmor true, if the rendering of the players equipped items should be allowed by the current server.
	 */
	void setRenderArmor(boolean renderArmor);

	/**
	 * @return true, if the rendering of the potion indicator vignette is allowed by the current server.
	 */
	boolean isRenderPotionIndicator();

	/**
	 * Sets, whether the rendering of the potion indicator vignette is allowed by the current server.
	 *
	 * @param renderPotionIndicator true, if the rendering of the potion indicator vignette should be allowed by the current server.
	 */
	void setRenderPotionIndicator(boolean renderPotionIndicator);

	/**
	 * @return true, if the rendering of the players saturation is allowed by the current server.
	 */
	boolean isRenderSaturation();

	/**
	 * Sets, whether the rendering of the players saturation is allowed by the current server.
	 *
	 * @param renderSaturation true, if the rendering of the players saturation should be allowed by the current server.
	 */
	void setRenderSaturation(boolean renderSaturation);

	/**
	 * @return true, if the current server allows auto reconnecting.
	 */
	boolean isAutoReconnecting();

	/**
	 * Sets, whether auto reconnecting is allowed by the current server.
	 *
	 * @param autoReconnecting true, if auto reconnecting should be allowed by the current server.
	 */
	void setAutoReconnecting(boolean autoReconnecting);

}
