import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Names;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.mod.gui.IWrappedGui;
import eu.the5zig.mod.gui.elements.*;
import eu.the5zig.mod.gui.ingame.*;
import eu.the5zig.mod.util.*;
import eu.the5zig.util.Callback;
import eu.the5zig.util.Utils;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.buffer.ByteBuf;
import net.minecraft.realms.RealmsBridge;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.util.*;

public class Variables implements IVariables {

	private static Field forgeChatField;
	private static Method rightClickMouse;

	static {
		if (Transformer.FORGE) {
			try {
				forgeChatField = Names.guiChat.load().getDeclaredField("field_146415_a");
				forgeChatField.setAccessible(true);
				rightClickMouse = Names.minecraft.load().getDeclaredMethod("func_147121_ag");
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				rightClickMouse = Names.minecraft.load().getDeclaredMethod("ax");
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private bcv scaledResolution;
	private IGui2ndChat gui2ndChat = new Gui2ndChat();

	private final ResourceManager resourceManager;
	private Kernel32.SYSTEM_POWER_STATUS batteryStatus;

	private static final List<PotionEffectImpl> DUMMY_POTIONS = Arrays.asList(new PotionEffectImpl("effect.jump", 20, "0:01", 1, 10, true, true, 0x22ff4c),
			new PotionEffectImpl("effect.moveSpeed", 20 * 50, "0:50", 1, 0, true, true, 0x7cafc6));

	public Variables() {
		updateScaledResolution();
		try {
			this.resourceManager = new ResourceManager(getGameProfile());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			Class<?> enumGameSettings = Names.gameOption.load();
			Method setMaxValue = Transformer.FORGE ? enumGameSettings.getDeclaredMethod("func_148263_a", float.class) : enumGameSettings.getDeclaredMethod("a", float.class);
			Field gamma = Transformer.FORGE ? enumGameSettings.getDeclaredField("GAMMA") : enumGameSettings.getDeclaredField("d");
			setMaxValue.invoke(gamma.get(null), 10.0f);
			Field fov = Transformer.FORGE ? enumGameSettings.getDeclaredField("FOV") : enumGameSettings.getDeclaredField("c");
			setMaxValue.invoke(fov.get(null), 130f);
		} catch (Exception e) {
			MinecraftFactory.getClassProxyCallback().getLogger().warn("Could not patch game settings", e);
		}
		if (Utils.getPlatform() == Utils.Platform.WINDOWS) {
			try {
				batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
			} catch (Throwable ignored) {
			}
		}
	}

	@Override
	public void drawString(String string, int x, int y, Object... format) {
		drawString(string, x, y, 0xffffff, format);
	}

	@Override
	public void drawString(String string, int x, int y) {
		drawString(string, x, y, 0xffffff);
	}

	@Override
	public void drawCenteredString(String string, int x, int y) {
		drawCenteredString(string, x, y, 0xffffff);
	}

	@Override
	public void drawCenteredString(String string, int x, int y, int color) {
		drawString(string, (x - getStringWidth(string) / 2), y, color);
	}

	@Override
	public void drawString(String string, int x, int y, int color, Object... format) {
		drawString(String.format(string, format), x, y, color);
	}

	@Override
	public void drawString(String string, int x, int y, int color) {
		drawString(string, x, y, color, true);
	}

	@Override
	public void drawString(String string, int x, int y, int color, boolean withShadow) {
		getFontrenderer().a(string, x, y, color, withShadow);
	}

	@Override
	public List<String> splitStringToWidth(String string, int width) {
		Validate.isTrue(width > 0);
		if (string == null)
			return Collections.emptyList();
		if (string.isEmpty())
			return Collections.singletonList("");
		return getFontrenderer().c(string, width);
	}

	@Override
	public int getStringWidth(String string) {
		return getFontrenderer().a(string);
	}

	@Override
	public String shortenToWidth(String string, int width) {
		if (StringUtils.isEmpty(string))
			return string;
		Validate.isTrue(width > 0);

		boolean changed = false;
		if (getStringWidth(string) > width) {
			while (getStringWidth(string + "...") > width && !string.isEmpty()) {
				string = string.substring(0, string.length() - 1);
				changed = true;
			}
		}
		if (changed)
			string += "...";
		return string;
	}

	@Override
	public IButton createButton(int id, int x, int y, String label) {
		return new Button(id, x, y, label);
	}

	@Override
	public IButton createButton(int id, int x, int y, String label, boolean enabled) {
		return new Button(id, x, y, label, enabled);
	}

	@Override
	public IButton createButton(int id, int x, int y, int width, int height, String label) {
		return new Button(id, x, y, width, height, label);
	}

	@Override
	public IButton createButton(int id, int x, int y, int width, int height, String label, boolean enabled) {
		return new Button(id, x, y, width, height, label, enabled);
	}

	@Override
	public IButton createStringButton(int id, int x, int y, String label) {
		return new StringButton(id, x, y, label);
	}

	@Override
	public IButton createStringButton(int id, int x, int y, int width, int height, String label) {
		return new StringButton(id, x, y, width, height, label);
	}

	@Override
	public IButton createAudioButton(int id, int x, int y, AudioCallback callback) {
		return new AudioButton(id, x, y, callback);
	}

	@Override
	public IButton createIconButton(IResourceLocation resourceLocation, int u, int v, int id, int x, int y) {
		return new IconButton(resourceLocation, u, v, id, x, y);
	}

	@Override
	public ITextfield createTextfield(int id, int x, int y, int width, int height) {
		return new Textfield(id, x, y, width, height);
	}

	@Override
	public ITextfield createTextfield(int id, int x, int y, int width, int height, int maxStringLength) {
		return new Textfield(id, x, y, width, height, maxStringLength);
	}

	@Override
	public IPlaceholderTextfield createTextfield(String placeholder, int id, int x, int y, int width, int height) {
		return new PlaceholderTextfield(placeholder, id, x, y, width, height);
	}

	@Override
	public IPlaceholderTextfield createTextfield(String placeholder, int id, int x, int y, int width, int height, int maxStringLength) {
		return new PlaceholderTextfield(placeholder, id, x, y, width, height, maxStringLength);
	}

	@Override
	public IWrappedTextfield createWrappedTextfield(Object handle) {
		return new WrappedTextfield((bdb) handle);
	}

	@Override
	public <E extends Row> IGuiList<E> createGuiList(Clickable<E> clickable, int width, int height, int top, int bottom, int left, int right, List<E> rows) {
		return new GuiList<E>(clickable, width, height, top, bottom, left, right, rows);
	}

	@Override
	public <E extends Row> IGuiList<E> createGuiListChat(int width, int height, int top, int bottom, int left, int right, int scrollx, List<E> rows, GuiListChatCallback callback) {
		return new GuiListChat<E>(width, height, top, bottom, left, right, scrollx, rows, callback);
	}

	@Override
	public IFileSelector createFileSelector(File currentDir, int width, int height, int left, int right, int top, int bottom, Callback<File> callback) {
		return new FileSelector(currentDir, width, height, left, right, top, bottom, callback);
	}

	@Override
	public IButton createSlider(int id, int x, int y, SliderCallback sliderCallback) {
		return new Slider(id, x, y, sliderCallback);
	}

	@Override
	public IButton createSlider(int id, int x, int y, int width, int height, SliderCallback sliderCallback) {
		return new Slider(id, x, y, width, height, sliderCallback);
	}

	@Override
	public IColorSelector createColorSelector(int id, int x, int y, int width, int height, String label, ColorSelectorCallback callback) {
		return new ColorSelector(id, x, y, width, height, label, callback);
	}

	@Override
	public IOverlay newOverlay() {
		return new Overlay();
	}

	@Override
	public void updateOverlayCount(int count) {
		Overlay.updateOverlayCount(count);
	}

	@Override
	public void renderOverlay() {
		Overlay.renderAll();
	}

	@Override
	public IWrappedGui createWrappedGui(Object lastScreen) {
		return new WrappedGui((bez) lastScreen);
	}

	@Override
	public IKeybinding createKeybinding(String description, int keyCode, String category) {
		return new Keybinding(description, keyCode, category);
	}

	@Override
	public IGui2ndChat get2ndChat() {
		return gui2ndChat;
	}

	@Override
	public boolean isChatOpened() {
		return getMinecraftScreen() instanceof bec;
	}

	@Override
	public String getChatBoxText() {
		if (!isChatOpened()) {
			return null;
		}
		return getChatField().b();
	}

	@Override
	public void typeInChatGUI(String text) {
		if (!isChatOpened()) {
			displayScreen(new bec());
		}
		bdb chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private bdb getChatField() {
		bec chatGUI = (bec) getMinecraftScreen();
		bdb chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (bdb) forgeChatField.get(chatGUI);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			chatField = chatGUI.a;
		}
		return chatField;
	}

	@Override
	public Object getChatComponentWithPrefix(String prefix, Object originalChatComponent) {
		return new fc(prefix).a((ew) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof bgl;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		bca[] currentKeybindings = getGameSettings().am;
		bca[] customKeybindings = new bca[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (bca) keybindings.get(i);
		}
		getGameSettings().am = Utils.concat(currentKeybindings, customKeybindings);

		getGameSettings().a();
	}

	@Override
	public void playSound(String sound, float pitch) {
		playSound("minecraft", sound, pitch);
	}

	@Override
	public void playSound(String domain, String sound, float pitch) {
		getMinecraft().U().a(byc.a(new ng(new ResourceLocation(domain, sound)), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public bkv getServerData() {
		return getMinecraft().C();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((bkv) null);
	}

	@Override
	public String getServer() {
		bkv serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (bkt wrapped : getPlayer().d.d()) {
			result.add(new WrappedNetworkPlayerInfo(wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getGameSettings().ae.e()) && ((!getMinecraft().E()) || (getServerPlayers().size() > 1));
	}

	@Override
	public void setFOV(float fov) {
		getGameSettings().ax = fov;
	}

	@Override
	public float getFOV() {
		return getGameSettings().ax;
	}

	@Override
	public void setSmoothCamera(boolean smoothCamera) {
		getGameSettings().av = smoothCamera;
	}

	@Override
	public boolean isSmoothCamera() {
		return getGameSettings().av;
	}

	@Override
	public String translate(String location, Object... values) {
		return bwl.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((bez) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((bez) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().M();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new beg((bez) getMinecraftScreen(), getMinecraft(), new bkv(host, host + ":" + port, false)));
	}

	@Override
	public void joinServer(Object parentScreen, Object serverData) {
		if (serverData == null) {
			return;
		}
		if (getWorld() != null) {
			getWorld().M();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new beg((bez) parentScreen, getMinecraft(), (bkv) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		boolean isOnIntegratedServer = getMinecraft().D();
		boolean isConnectedToRealms = getMinecraft().ai();
		if (getWorld() != null) {
			getWorld().M();
		}
		getMinecraft().a((bks) null);
		if (isOnIntegratedServer) {
			displayScreen(new bfg());
		} else if (isConnectedToRealms) {
			RealmsBridge realmsBridge = new RealmsBridge();
			realmsBridge.switchToRealms(new bfg());
		} else {
			displayScreen(new bgp(new bfg()));
		}
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return bcd.I();
	}

	public bcd getMinecraft() {
		return bcd.z();
	}

	public bcr getFontrenderer() {
		return getMinecraft().k;
	}

	public bcf getGameSettings() {
		return getMinecraft().u;
	}

	public bmr getPlayer() {
		return getMinecraft().h;
	}

	public bks getWorld() {
		return getMinecraft().f;
	}

	public bcs getGuiIngame() {
		return getMinecraft().r;
	}

	@Override
	public boolean isSpectatingSelf() {
		return getSpectatingEntity() instanceof zj;
	}

	@Override
	public PlayerGameMode getGameMode() {
		return PlayerGameMode.values()[getMinecraft().c.l().a()];
	}

	public rr getSpectatingEntity() {
		return getMinecraft().aa();
	}

	public aau getOpenContainer() {
		return getPlayer().bu;
	}

	@Override
	public String getOpenContainerTitle() {
		if (!(getOpenContainer() instanceof abb))
			return null;
		return ((abb) getOpenContainer()).e().h_();
	}

	@Override
	public void closeContainer() {
		getPlayer().q();
	}

	@Override
	public String getSession() {
		return getMinecraft().K().d();
	}

	@Override
	public String getUsername() {
		return getMinecraft().K().c();
	}

	@Override
	public Proxy getProxy() {
		return getMinecraft().M();
	}

	@Override
	public GameProfile getGameProfile() {
		return getMinecraft().K().e();
	}

	@Override
	public String getFPS() {
		return getMinecraft().D.split(" fps")[0];
	}

	@Override
	public boolean isPlayerNull() {
		return getPlayer() == null;
	}

	@Override
	public boolean isTerrainLoading() {
		return getMinecraftScreen() instanceof bex;
	}

	@Override
	public double getPlayerPosX() {
		return getSpectatingEntity().p;
	}

	@Override
	public double getPlayerPosY() {
		return getSpectatingEntity().bm().b;
	}

	@Override
	public double getPlayerPosZ() {
		return getSpectatingEntity().r;
	}

	@Override
	public float getPlayerRotationYaw() {
		return getSpectatingEntity().x;
	}

	@Override
	public float getPlayerRotationPitch() {
		return getSpectatingEntity().y;
	}

	@Override
	public int getPlayerChunkX() {
		cl blockPosition = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		cl blockPosition = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		cl blockPosition = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.r() >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		cl blockPosition = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		cl blockPosition = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		cl blockPosition = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.r() & 15;
	}

	@Override
	public boolean hasTargetBlock() {
		return getMinecraft().t != null && getMinecraft().t.a.ordinal() == 1 && getMinecraft().t.a() != null;
	}

	@Override
	public int getTargetBlockX() {
		return getMinecraft().t.a().p();
	}

	@Override
	public int getTargetBlockY() {
		return getMinecraft().t.a().q();
	}

	@Override
	public int getTargetBlockZ() {
		return getMinecraft().t.a().r();
	}

	@Override
	public ResourceLocation getTargetBlockName() {
		cl blockPosition = getMinecraft().t.a();
		return ResourceLocation.fromObfuscated(akf.h.b(getWorld().o(blockPosition).t()));
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return bcd.w();
	}

	@Override
	public String getBiome() {
		cl localdt = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt) || localdt.q() < 0 || localdt.q() >= 256) {
			return null;
		}
		asf localObject = getMinecraft().f.f(localdt);
		if (localObject.f()) {
			return null;
		}
		return localObject.a(localdt, getMinecraft().f.A()).l();
	}

	@Override
	public int getLightLevel() {
		cl localdt = new cl(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt) || localdt.q() < 0 || localdt.q() >= 256) {
			return 0;
		}
		asf localObject = getMinecraft().f.f(localdt);
		if (localObject.f()) {
			return 0;
		}
		return localObject.a(localdt, 0);
	}

	@Override
	public String getEntityCount() {
		return getMinecraft().g.h().split(" |,")[1];
	}

	@Override
	public boolean isRidingEntity() {
		return getSpectatingEntity().bz() != null;
	}

	@Override
	public int getFoodLevel() {
		return isSpectatingSelf() ? ((zj) getSpectatingEntity()).cT().a() : getPlayer().cT().a();
	}

	@Override
	public float getSaturation() {
		return isSpectatingSelf() ? ((zj) getSpectatingEntity()).cT().e() : 0;
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof sb))
			return -1;
		return ((sb) entity).bR();
	}

	@Override
	public float getPlayerHealth() {
		return getPlayer().bR();
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(yt.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().bU();
	}

	@Override
	public int getAir() {
		return getPlayer().aP();
	}

	@Override
	public boolean isPlayerInsideWater() {
		return getSpectatingEntity().a(axe.h);
	}

	@Override
	public float getResistanceFactor() {
		float referenceDamage = 100;
		referenceDamage = ra.a(referenceDamage, (float) getPlayer().bU(), (float) getPlayer().a(yt.h).e());

		if (getPlayer().a(rm.k)) {
			int resistanceAmplifier = (getPlayer().b(rm.k).c() + 1) * 5;
			int i = 25 - resistanceAmplifier;
			float d = referenceDamage * (float) i;
			referenceDamage = d / 25.0F;
		}

		if (referenceDamage <= 0.0F) {
			return 100;
		} else {
			int enchantmentModifierDamage = ago.a(getPlayer().aF(), rc.l);
			if (enchantmentModifierDamage > 0) {
				referenceDamage = ra.a(referenceDamage, (float) enchantmentModifierDamage);
			}
			return 100 - referenceDamage;
		}
	}

	@Override
	public PotionEffectImpl getPotionForVignette() {
		for (rl potionEffect : getActivePlayerPotionEffects()) {
			rk potion = getPotionByEffect(potionEffect);
			if (potion.i()) {
				return wrapPotionEffect(potionEffect);
			}
		}
		for (rl potionEffect : getActivePlayerPotionEffects()) {
			rk potion = getPotionByEffect(potionEffect);
			if (!potion.i()) {
				return wrapPotionEffect(potionEffect);
			}
		}

		return null;
	}

	@Override
	public List<PotionEffectImpl> getActivePotionEffects() {
		List<PotionEffectImpl> result = new ArrayList<PotionEffectImpl>(getActivePlayerPotionEffects().size());
		for (rl potionEffect : getActivePlayerPotionEffects()) {
			result.add(wrapPotionEffect(potionEffect));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<PotionEffectImpl> getDummyPotionEffects() {
		return DUMMY_POTIONS;
	}

	private PotionEffectImpl wrapPotionEffect(rl potionEffect) {
		return new PotionEffectImpl(potionEffect.f(), potionEffect.b(), rj.a(potionEffect, 1), potionEffect.c() + 1, potionEffect.a().d(), potionEffect.a().i(), potionEffect.e(),
				potionEffect.a().g());
	}

	@Override
	public int getPotionEffectIndicatorHeight() {
		int result = 0;

		boolean hasGood = false;
		boolean hasBad = false;
		for (PotionEffect potionEffect : getActivePotionEffects()) {
			if (potionEffect.getIconIndex() < 0 || !potionEffect.hasParticles()) {
				continue;
			}
			if (potionEffect.isGood()) {
				hasGood = true;
			} else {
				hasBad = true;
			}
		}
		if ((hasGood && hasBad) || hasBad) {
			return 26 * 2;
		} else if (hasGood) {
			return 26;
		}
		return result;
	}

	@Override
	public boolean isHungerPotionActive() {
		return getPlayer().a(rm.q);
	}

	@Override
	public ItemStack getItemInMainHand() {
		return getPlayer().cc() == null ? null : new WrappedItemStack(getPlayer().cc());
	}

	@Override
	public ItemStack getItemInOffHand() {
		return getPlayer().cd() == null ? null : new WrappedItemStack(getPlayer().cd());
	}

	@Override
	public ItemStack getItemInArmorSlot(int slot) {
		return getArmorItemBySlot(slot) == null ? null : new WrappedItemStack(getArmorItemBySlot(slot));
	}

	public Collection<rl> getActivePlayerPotionEffects() {
		return getPlayer().bP();
	}

	public rk getPotionByEffect(rl potionEffect) {
		return potionEffect.a();
	}

	public adq getArmorItemBySlot(int slot) {
		return getPlayer().bs.g(slot);
	}

	@Override
	public ItemStack getItemByName(String resourceName) {
		return new WrappedItemStack(new adq(ado.d(resourceName)));
	}

	@Override
	public ItemStack getItemByName(String resourceName, int amount) {
		return new WrappedItemStack(new adq(ado.d(resourceName), amount));
	}

	@Override
	public int getItemCount(String key) {
		int count = 0;
		for (adq itemStack : getPlayer().bs.a) {
			if (itemStack == null)
				continue;
			if (key.equals(WrappedItemStack.getResourceKey(itemStack))) {
				count += itemStack.b;
			}
		}
		return count;
	}

	@Override
	public int getSelectedHotbarSlot() {
		return getPlayer().bs.d;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().bs.d = slot;
		getNetworkManager().a(new jc(slot));
	}

	@Override
	public void onRightClickMouse() {
		try {
			rightClickMouse.invoke(getMinecraft());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void renderItem(adq itemStack, int x, int y) {
		bcb.c();
		GLUtil.enableBlend();
		GLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
		brw itemRenderer = getMinecraft().ad();
		itemRenderer.b(itemStack, x, y);
		itemRenderer.a(getFontrenderer(), itemStack, x, y);
		GLUtil.disableBlend();
		bcb.a();
	}

	@Override
	public void updateScaledResolution() {
		this.scaledResolution = new bcv(getMinecraft());
	}

	@Override
	public int getWidth() {
		return getMinecraft().d;
	}

	@Override
	public int getHeight() {
		return getMinecraft().e;
	}

	@Override
	public int getScaledWidth() {
		return scaledResolution.a();
	}

	@Override
	public int getScaledHeight() {
		return scaledResolution.b();
	}

	@Override
	public int getScaleFactor() {
		return scaledResolution.e();
	}

	@Override
	public void drawIngameTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		if (getGuiIngame() != null)
			getGuiIngame().b(x, y, u, v, width, height);
	}

	@Override
	public boolean showDebugScreen() {
		return getGameSettings().ar;
	}

	@Override
	public boolean isPlayerSpectating() {
		return !isSpectatingSelf() || getPlayerController().a();
	}

	@Override
	public boolean shouldDrawHUD() {
		return getPlayerController().b();
	}

	@Override
	public String[] getHotbarKeys() {
		String[] result = new String[9];
		bca[] hotbarBindings = getGameSettings().al;
		for (int i = 0; i < Math.min(result.length, hotbarBindings.length); i++) {
			result[i] = getKeyDisplayStringShort(hotbarBindings[i].j());
		}
		return result;
	}

	@Override
	public String getKeyDisplayStringShort(int key) {
		return key < 0 ? "M" + (key + 101) : (key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase());
	}

	private bkr getPlayerController() {
		return getMinecraft().c;
	}

	@Override
	public Gui getCurrentScreen() {
		if (getMinecraft().m == null || !(getMinecraft().m instanceof GuiHandle))
			return null;
		return ((GuiHandle) getMinecraft().m).getChild();
	}

	@Override
	public Object getMinecraftScreen() {
		return getMinecraft().m;
	}

	@Override
	public void messagePlayer(String message) {
		messagePlayer(ChatComponentBuilder.fromLegacyText(message));
	}

	public void messagePlayer(ew chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.d().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().d.a(new ik(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new ir(channel, new eo(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private em getNetworkManager() {
		return getMinecraft().v() != null ? getMinecraft().v().a() : null;
	}

	@Override
	public IResourceLocation createResourceLocation(String resourcePath) {
		return new ResourceLocation(resourcePath);
	}

	@Override
	public IResourceLocation createResourceLocation(String resourceDomain, String resourcePath) {
		return new ResourceLocation(resourceDomain, resourcePath);
	}

	@Override
	public Object loadDynamicImage(String name, BufferedImage image) {
		return getTextureManager().a(name, new buu(image));
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof buu) {
			bnf.i(((buu)resourceLocation).b());
		} else {
			getTextureManager().a((kl) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
		if (resourceLocation instanceof kl) {
			getTextureManager().c((kl) resourceLocation);
		}
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		buu dynamicImage = new buu(width, height);
		getTextureManager().a((ResourceLocation) resourceLocation, dynamicImage);
		return dynamicImage;
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((kl) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), ((buu) dynamicImage).e(), 0, image.getWidth());
		((buu) dynamicImage).d();
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public bvf getTextureManager() {
		return getMinecraft().N();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		bnr var4 = bnr.a();
		bmx var5 = var4.c();
		bindTexture(bcs.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		var5.a(7, bvm.i);
		var5.b((double) x1, (double) y2, 0.0D).a(0.0D, (double) ((float) y2 / 32.0F)).b(64, 64, 64, 255).d();
		var5.b((double) (x1 + x2), (double) y2, 0.0D).a((double) ((float) x2 / 32.0F), (double) ((float) y2 / 32.0F)).b(64, 64, 64, 255).d();
		var5.b((double) (x1 + x2), (double) y1, 0.0D).a((double) ((float) x2 / 32.0F), (double) ((float) y1 / 32.0F)).b(64, 64, 64, 255).d();
		var5.b((double) x1, (double) y1, 0.0D).a(0.0D, (double) ((float) y1 / 32.0F)).b(64, 64, 64, 255).d();
		var4.b();
	}

	@Override
	public void setIngameFocus() {
		getMinecraft().o();
	}

	@Override
	public MouseOverObject calculateMouseOverDistance(double maxDistance) {
		if (getSpectatingEntity() == null || getWorld() == null)
			return null;
		bbg objectMouseOver = getSpectatingEntity().a(maxDistance, 1f);
		double var3 = maxDistance;
		bbh entityPosition = getSpectatingEntity().g(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.c.f(entityPosition);
		}

		bbh look = getSpectatingEntity().f(1f);
		bbh mostFarPoint = entityPosition.b(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance);
		rr pointedEntity = null;
		bbh hitVector = null;
		List<rr> entitiesWithinAABBExcludingEntity = getWorld().a(getSpectatingEntity(),
				getSpectatingEntity().bm().a(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance).b(1.0, 1.0, 1.0), Predicates.and(rv.e, new Predicate<rr>() {
					@Override
					public boolean apply(rr entity) {
						return entity != null && entity.ap();
					}
				}));
		double distance = var3;

		for (rr entity : entitiesWithinAABBExcludingEntity) {
			bbf axisAlignedBB = entity.bm().g(entity.aA());
			bbg intercept = axisAlignedBB.a(entityPosition, mostFarPoint);
			if (axisAlignedBB.a(entityPosition)) {
				if (distance >= 0.0D) {
					pointedEntity = entity;
					hitVector = intercept == null ? entityPosition : intercept.c;
					distance = 0.0D;
				}
			} else if (intercept != null) {
				double distanceToHitVec = entityPosition.f(intercept.c);
				if (distanceToHitVec < distance || distance == 0.0D) {
					if (entity == getSpectatingEntity().bv()) {
						if (distance == 0.0D) {
							pointedEntity = entity;
							hitVector = intercept.c;
						}
					} else {
						pointedEntity = entity;
						hitVector = intercept.c;
						distance = distanceToHitVec;
					}
				}
			}
		}

		if (pointedEntity != null && (distance < var3 || objectMouseOver == null)) {
			objectMouseOver = new bbg(pointedEntity, hitVector);
		}

		if (objectMouseOver == null)
			return null;
		ObjectType type;
		switch (objectMouseOver.a) {
			case a:
				return null;
			case b:
				type = ObjectType.BLOCK;
				break;
			case c:
				type = ObjectType.ENTITY;
				break;
			default:
				return null;
		}

		return new MouseOverObject(type, type == ObjectType.ENTITY ? pointedEntity : null, distance);
	}

	@Override
	public ScoreboardImpl getScoreboard() {
		if (getWorld() == null) {
			return null;
		}
		bbn scoreboard = getWorld().ad();
		if (scoreboard == null) {
			return null;
		}
		bbj objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d();
		Collection<bbl> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (bbl score : scores) {
			lines.put(score.e(), score.c());
		}
		return new ScoreboardImpl(displayName, lines);
	}

	@Override
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	@Override
	public Kernel32.SYSTEM_POWER_STATUS getBatteryStatus() {
		return batteryStatus;
	}

	@Override
	public InputStream getMinecraftIcon() throws Exception {
		Class<?> minecraft = Thread.currentThread().getContextClassLoader().loadClass(Names.minecraft.getName());
		InputStream is;
		if (Transformer.FORGE) {
			Object minecraftInstance = minecraft.getMethod("func_71410_x").invoke(null);
			Field resourcePackField = minecraft.getDeclaredField("field_110450_ap");
			resourcePackField.setAccessible(true);
			Object resourceManager = resourcePackField.get(minecraftInstance);
			is = (InputStream) resourceManager.getClass().getMethod("func_152780_c", Thread.currentThread().getContextClassLoader().loadClass(Names.resourceLocation.getName())).invoke(
					resourceManager, MinecraftFactory.getVars().createResourceLocation("icons/icon_16x16.png"));
		} else {
			Object minecraftInstance = minecraft.getMethod("z").invoke(null);
			Field aB = minecraft.getDeclaredField("aE");
			aB.setAccessible(true);
			Object resourceManager = aB.get(minecraftInstance);
			is = (InputStream) resourceManager.getClass().getMethod("c", Class.forName(Names.resourceLocation.getName())).invoke(resourceManager,
					MinecraftFactory.getVars().createResourceLocation("icons/icon_32x32.png"));
			if (is == null) {
				is = (InputStream) resourceManager.getClass().getMethod("c", Class.forName(Names.resourceLocation.getName())).invoke(resourceManager,
						MinecraftFactory.getVars().createResourceLocation("icons/icon_16x16.png"));
			}
		}
		return is;
	}

	@Override
	public boolean isMainThread() {
		return getMinecraft().aE();
	}

	@Override
	public File getMinecraftDataDirectory() {
		return getMinecraft().w;
	}

	@Override
	public void dispatchKeypresses() {
		int eventKey = org.lwjgl.input.Keyboard.getEventKey();
		int currentcode = eventKey == 0 ? org.lwjgl.input.Keyboard.getEventCharacter() : eventKey;
		if ((currentcode == 0) || (org.lwjgl.input.Keyboard.isRepeatEvent()))
			return;

		if (org.lwjgl.input.Keyboard.getEventKeyState()) {
			int keyCode = currentcode + (eventKey == 0 ? 256 : 0);
			MinecraftFactory.getClassProxyCallback().fireKeyPressEvent(keyCode);
		}
	}

	@Override
	public void shutdown() {
		getMinecraft().h();
	}
}
