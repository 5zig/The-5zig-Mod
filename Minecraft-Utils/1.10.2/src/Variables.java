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

	private bdp scaledResolution;
	private IGui2ndChat gui2ndChat = new Gui2ndChat();

	private final ResourceManager resourceManager;
	private Kernel32.SYSTEM_POWER_STATUS batteryStatus;

	private static final List<PotionEffectImpl> DUMMY_POTIONS = Arrays.asList(new PotionEffectImpl("effect.jump", 20, "0:01", 1, 10, true, true, 0x22ff4c),
			new PotionEffectImpl("effect.moveSpeed", 20 * 50, "0:50", 1, 0, true, true, 0x7cafc6));

	public Variables() {
		eu.the5zig.mod.util.Keyboard.initLegacy(new eu.the5zig.mod.util.Keyboard.KeyboardHandler() {
			@Override
			public boolean isKeyDown(int key) {
				return org.lwjgl.input.Keyboard.isKeyDown(key);
			}

			@Override
			public void enableRepeatEvents(boolean repeat) {
				eu.the5zig.mod.util.Keyboard.enableRepeatEvents(repeat);
			}
		});
		Mouse.init(new Mouse.MouseHandler() {
			@Override
			public boolean isButtonDown(int button) {
				return org.lwjgl.input.Mouse.isButtonDown(button);
			}

			@Override
			public int getX() {
				return org.lwjgl.input.Mouse.getX();
			}

			@Override
			public int getY() {
				return org.lwjgl.input.Mouse.getY();
			}
		});
		Display.init(new Display.DisplayHandler() {
			@Override
			public boolean isActive() {
				return org.lwjgl.opengl.Display.isActive();
			}
		});
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
		return new WrappedTextfield((bdv) handle);
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
		return new WrappedGui((bft) lastScreen);
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
		return getMinecraftScreen() instanceof bew;
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
			displayScreen(new bew());
		}
		bdv chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private bdv getChatField() {
		bew chatGUI = (bew) getMinecraftScreen();
		bdv chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (bdv) forgeChatField.get(chatGUI);
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
		return new fe(prefix).a((ey) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof bhf;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		bcu[] currentKeybindings = getGameSettings().ao;
		bcu[] customKeybindings = new bcu[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (bcu) keybindings.get(i);
		}
		getGameSettings().ao = Utils.concat(currentKeybindings, customKeybindings);

		getGameSettings().a();
	}

	@Override
	public void playSound(String sound, float pitch) {
		playSound("minecraft", sound, pitch);
	}

	@Override
	public void playSound(String domain, String sound, float pitch) {
		getMinecraft().U().a(bzc.a(new ni(new ResourceLocation(domain, sound)), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public blq getServerData() {
		return getMinecraft().C();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((blq) null);
	}

	@Override
	public String getServer() {
		blq serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (blo wrapped : getPlayer().d.d()) {
			result.add(new WrappedNetworkPlayerInfo(wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getGameSettings().ag.e()) && ((!getMinecraft().E()) || (getServerPlayers().size() > 1));
	}

	@Override
	public void setFOV(float fov) {
		getGameSettings().az = fov;
	}

	@Override
	public float getFOV() {
		return getGameSettings().az;
	}

	@Override
	public void setSmoothCamera(boolean smoothCamera) {
		getGameSettings().ax = smoothCamera;
	}

	@Override
	public boolean isSmoothCamera() {
		return getGameSettings().ax;
	}

	@Override
	public String translate(String location, Object... values) {
		return bxl.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((bft) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((bft) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().M();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bfa((bft) getMinecraftScreen(), getMinecraft(), new blq(host, host + ":" + port, false)));
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
		displayScreen(new bfa((bft) parentScreen, getMinecraft(), (blq) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		boolean isOnIntegratedServer = getMinecraft().D();
		boolean isConnectedToRealms = getMinecraft().ah();
		if (getWorld() != null) {
			getWorld().M();
		}
		getMinecraft().a((bln) null);
		if (isOnIntegratedServer) {
			displayScreen(new bga());
		} else if (isConnectedToRealms) {
			RealmsBridge realmsBridge = new RealmsBridge();
			realmsBridge.switchToRealms(new bga());
		} else {
			displayScreen(new bhj(new bga()));
		}
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return bcx.I();
	}

	public bcx getMinecraft() {
		return bcx.z();
	}

	public bdl getFontrenderer() {
		return getMinecraft().k;
	}

	public bcz getGameSettings() {
		return getMinecraft().u;
	}

	public bnn getPlayer() {
		return getMinecraft().h;
	}

	public bln getWorld() {
		return getMinecraft().f;
	}

	public bdm getGuiIngame() {
		return getMinecraft().r;
	}

	@Override
	public boolean isSpectatingSelf() {
		return getSpectatingEntity() instanceof zs;
	}

	@Override
	public PlayerGameMode getGameMode() {
		return PlayerGameMode.values()[getMinecraft().c.l().a()];
	}

	public rw getSpectatingEntity() {
		return getMinecraft().aa();
	}

	public abd getOpenContainer() {
		return getPlayer().bv;
	}

	@Override
	public String getOpenContainerTitle() {
		if (!(getOpenContainer() instanceof abk))
			return null;
		return ((abk) getOpenContainer()).e().h_();
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
		return getSpectatingEntity().bo().b;
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
		cm blockPosition = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		cm blockPosition = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		cm blockPosition = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.r() >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		cm blockPosition = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		cm blockPosition = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		cm blockPosition = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
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
		cm blockPosition = getMinecraft().t.a();
		return ResourceLocation.fromObfuscated(akf.h.b(getWorld().o(blockPosition).t()));
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return bcx.w();
	}

	@Override
	public String getBiome() {
		cm localdt = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt) || localdt.q() < 0 || localdt.q() >= 256) {
			return null;
		}
		asv localObject = getMinecraft().f.f(localdt);
		if (localObject.f()) {
			return null;
		}
		return localObject.a(localdt, getMinecraft().f.A()).l();
	}

	@Override
	public int getLightLevel() {
		cm localdt = new cm(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt) || localdt.q() < 0 || localdt.q() >= 256) {
			return 0;
		}
		asv localObject = getMinecraft().f.f(localdt);
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
		return getSpectatingEntity().bB() != null;
	}

	@Override
	public int getFoodLevel() {
		return isSpectatingSelf() ? ((zs) getSpectatingEntity()).cX().a() : getPlayer().cX().a();
	}

	@Override
	public float getSaturation() {
		return isSpectatingSelf() ? ((zs) getSpectatingEntity()).cX().e() : 0;
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof sg))
			return -1;
		return ((sg) entity).bT();
	}

	@Override
	public float getPlayerHealth() {
		return getHealth(getPlayer());
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(za.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().bX();
	}

	@Override
	public int getAir() {
		return getPlayer().aR();
	}

	@Override
	public boolean isPlayerInsideWater() {
		return getSpectatingEntity().a(axx.h);
	}

	@Override
	public float getResistanceFactor() {
		float referenceDamage = 100;
		referenceDamage = rf.a(referenceDamage, (float) getPlayer().bX(), (float) getPlayer().a(za.h).e());

		if (getPlayer().a(rr.k)) {
			int resistanceAmplifier = (getPlayer().b(rr.k).c() + 1) * 5;
			int i = 25 - resistanceAmplifier;
			float d = referenceDamage * (float) i;
			referenceDamage = d / 25.0F;
		}

		if (referenceDamage <= 0.0F) {
			return 100;
		} else {
			int enchantmentModifierDamage = agx.a(getPlayer().aH(), rh.m);
			if (enchantmentModifierDamage > 0) {
				referenceDamage = rf.a(referenceDamage, (float) enchantmentModifierDamage);
			}
			return 100 - referenceDamage;
		}
	}

	@Override
	public PotionEffectImpl getPotionForVignette() {
		for (rq potionEffect : getActivePlayerPotionEffects()) {
			rp potion = getPotionByEffect(potionEffect);
			if (potion.i()) {
				return wrapPotionEffect(potionEffect);
			}
		}
		for (rq potionEffect : getActivePlayerPotionEffects()) {
			rp potion = getPotionByEffect(potionEffect);
			if (!potion.i()) {
				return wrapPotionEffect(potionEffect);
			}
		}

		return null;
	}

	@Override
	public List<PotionEffectImpl> getActivePotionEffects() {
		List<PotionEffectImpl> result = new ArrayList<PotionEffectImpl>(getActivePlayerPotionEffects().size());
		for (rq potionEffect : getActivePlayerPotionEffects()) {
			result.add(wrapPotionEffect(potionEffect));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<PotionEffectImpl> getDummyPotionEffects() {
		return DUMMY_POTIONS;
	}

	private PotionEffectImpl wrapPotionEffect(rq potionEffect) {
		return new PotionEffectImpl(potionEffect.f(), potionEffect.b(), rp.a(potionEffect, 1), potionEffect.c() + 1, potionEffect.a().d(), potionEffect.a().i(), potionEffect.e(),
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
		return getPlayer().a(rr.q);
	}

	@Override
	public ItemStack getItemInMainHand() {
		return getPlayer().cf() == null ? null : new WrappedItemStack(getPlayer().cf());
	}

	@Override
	public ItemStack getItemInOffHand() {
		return getPlayer().cg() == null ? null : new WrappedItemStack(getPlayer().cg());
	}

	@Override
	public ItemStack getItemInArmorSlot(int slot) {
		return getArmorItemBySlot(slot) == null ? null : new WrappedItemStack(getArmorItemBySlot(slot));
	}

	public Collection<rq> getActivePlayerPotionEffects() {
		return getPlayer().bR();
	}

	public rp getPotionByEffect(rq potionEffect) {
		return potionEffect.a();
	}

	public adz getArmorItemBySlot(int slot) {
		return getPlayer().bt.g(slot);
	}

	@Override
	public ItemStack getItemByName(String resourceName) {
		return new WrappedItemStack(new adz(adx.d(resourceName)));
	}

	@Override
	public ItemStack getItemByName(String resourceName, int amount) {
		return new WrappedItemStack(new adz(adx.d(resourceName), amount));
	}

	@Override
	public int getItemCount(String key) {
		int count = 0;
		for (adz itemStack : getPlayer().bt.a) {
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
		return getPlayer().bt.d;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().bt.d = slot;
		getNetworkManager().a(new je(slot));
	}

	@Override
	public void onRightClickMouse() {
		try {
			rightClickMouse.invoke(getMinecraft());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void renderItem(adz itemStack, int x, int y) {
		bcv.c();
		GLUtil.enableBlend();
		GLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
		bsu itemRenderer = getMinecraft().ad();
		itemRenderer.b(itemStack, x, y);
		itemRenderer.a(getFontrenderer(), itemStack, x, y);
		GLUtil.disableBlend();
		bcv.a();
	}

	@Override
	public void updateScaledResolution() {
		this.scaledResolution = new bdp(getMinecraft());
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
		return getGameSettings().at;
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
		bcu[] hotbarBindings = getGameSettings().an;
		for (int i = 0; i < Math.min(result.length, hotbarBindings.length); i++) {
			result[i] = getKeyDisplayStringShort(hotbarBindings[i].j());
		}
		return result;
	}

	@Override
	public String getKeyDisplayStringShort(int key) {
		return key < 0 ? "M" + (key + 101) : (key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase());
	}

	private blm getPlayerController() {
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

	public void messagePlayer(ey chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.d().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().d.a(new im(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new it(channel, new eq(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private eo getNetworkManager() {
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
		return getTextureManager().a(name, new bvu(image));
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof bvu) {
			bob.i(((bvu)resourceLocation).b());
		} else {
			getTextureManager().a((kn) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
		if (resourceLocation instanceof kn) {
			getTextureManager().c((kn) resourceLocation);
		}
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		bvu dynamicImage = new bvu(width, height);
		getTextureManager().a((ResourceLocation) resourceLocation, dynamicImage);
		return dynamicImage;
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((kn) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), ((bvu) dynamicImage).e(), 0, image.getWidth());
		((bvu) dynamicImage).d();
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public bwf getTextureManager() {
		return getMinecraft().N();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		bon var4 = bon.a();
		bnt var5 = var4.c();
		bindTexture(bdm.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		var5.a(7, bwm.i);
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
		bbz objectMouseOver = getSpectatingEntity().a(maxDistance, 1f);
		double var3 = maxDistance;
		bcb entityPosition = getSpectatingEntity().g(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.c.f(entityPosition);
		}

		bcb look = getSpectatingEntity().f(1f);
		bcb mostFarPoint = entityPosition.b(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance);
		rw pointedEntity = null;
		bcb hitVector = null;
		List<rw> entitiesWithinAABBExcludingEntity = getWorld().a(getSpectatingEntity(),
				getSpectatingEntity().bo().a(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance).b(1.0, 1.0, 1.0), Predicates.and(sa.e, new Predicate<rw>() {
					@Override
					public boolean apply(rw entity) {
						return entity != null && entity.aq();
					}
				}));
		double distance = var3;

		for (rw entity : entitiesWithinAABBExcludingEntity) {
			bby axisAlignedBB = entity.bo().g(entity.aA());
			bbz intercept = axisAlignedBB.b(entityPosition, mostFarPoint);
			if (axisAlignedBB.a(entityPosition)) {
				if (distance >= 0.0D) {
					pointedEntity = entity;
					hitVector = intercept == null ? entityPosition : intercept.c;
					distance = 0.0D;
				}
			} else if (intercept != null) {
				double distanceToHitVec = entityPosition.f(intercept.c);
				if (distanceToHitVec < distance || distance == 0.0D) {
					if (entity == getSpectatingEntity().bz()) {
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
			objectMouseOver = new bbz(pointedEntity, hitVector);
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
		bch scoreboard = getWorld().ad();
		if (scoreboard == null) {
			return null;
		}
		bcd objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d();
		Collection<bcf> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (bcf score : scores) {
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
			Field aB = minecraft.getDeclaredField("aC");
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
