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
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.util.*;

public class Variables implements IVariables, GLFWKeyCallbackI {

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
				rightClickMouse = Names.minecraft.load().getDeclaredMethod(Names.rightClickMouse.getName());
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private final GLFWKeyCallback previousCallback;

	private IGui2ndChat gui2ndChat = new Gui2ndChat();

	private final ResourceManager resourceManager;
	private Kernel32.SYSTEM_POWER_STATUS batteryStatus;

	private static final List<PotionEffectImpl> DUMMY_POTIONS = Arrays.asList(new PotionEffectImpl("effect.jump", 20, "0:01", 1, 10, true, true, 0x22ff4c),
			new PotionEffectImpl("effect.moveSpeed", 20 * 50, "0:50", 1, 0, true, true, 0x7cafc6));

	public Variables() {
		Keyboard.init(new Keyboard.KeyboardHandler() {
			@Override
			public boolean isKeyDown(int key) {
				return GLFW.glfwGetKey(getMinecraft().f.j(), key) == 1;
			}

			@Override
			public void enableRepeatEvents(boolean repeat) {
				getMinecraft().v.a(repeat);
			}
		});
		Mouse.init(new Mouse.MouseHandler() {
			@Override
			public boolean isButtonDown(int button) {
				return GLFW.glfwGetMouseButton(getMinecraft().f.j(), button) == 1;
			}

			@Override
			public int getX() {
				return (int) getMinecraft().u.e();
			}

			@Override
			public int getY() {
				return (int) getMinecraft().u.f();
			}
		});
		Display.init(new Display.DisplayHandler() {
			@Override
			public boolean isActive() {
				return GLFW.glfwGetWindowAttrib(getMinecraft().f.j(), GLFW.GLFW_FOCUSED) == 1;
			}
		});
		previousCallback = GLFW.glfwSetKeyCallback(getMinecraft().f.j(), this);
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
	public void invoke(long window, int key, int scanCode, int action, int modifiers) {
		previousCallback.invoke(window, key, scanCode, action, modifiers);
		MinecraftFactory.getClassProxyCallback().fireKeyPressEvent(key);
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
		if (withShadow) {
			getFontrenderer().a(string, x, y, color);
		} else {
			getFontrenderer().b(string, x, y, color);
		}
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
		return new WrappedTextfield((cgn) handle);
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
		return new WrappedGui((cjs) lastScreen);
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
		return getMinecraftScreen() instanceof civ;
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
			displayScreen(new civ());
		}
		cgn chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private cgn getChatField() {
		civ chatGUI = (civ) getMinecraftScreen();
		cgn chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (cgn) forgeChatField.get(chatGUI);
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
		return new iq(prefix).a((ij) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof cli;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		cfe[] currentKeybindings = getGameSettings().aw;
		cfe[] customKeybindings = new cfe[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (cfe) keybindings.get(i);
		}
		getGameSettings().aw = Utils.concat(currentKeybindings, customKeybindings);

		getGameSettings().a();
	}

	@Override
	public void playSound(String sound, float pitch) {
		playSound("minecraft", sound, pitch);
	}

	@Override
	public void playSound(String domain, String sound, float pitch) {
		getMinecraft().N().a(dfi.a(new wh(new ResourceLocation(domain, sound)), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public cqy getServerData() {
		return getMinecraft().v();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((cqy) null);
	}

	@Override
	public String getServer() {
		cqy serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (cqw wrapped : getPlayer().d.e()) {
			result.add(new WrappedNetworkPlayerInfo(wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getGameSettings().al.d()) && ((!getMinecraft().x()) || (getServerPlayers().size() > 1));
	}

	@Override
	public void setFOV(float fov) {
		getGameSettings().aH = fov;
	}

	@Override
	public float getFOV() {
		return (float) getGameSettings().aH;
	}

	@Override
	public void setSmoothCamera(boolean smoothCamera) {
		getGameSettings().aF = smoothCamera;
	}

	@Override
	public boolean isSmoothCamera() {
		return getGameSettings().aF;
	}

	@Override
	public String translate(String location, Object... values) {
		return ddz.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((cjs) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((cjs) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().S();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new ciz((cjs) getMinecraftScreen(), getMinecraft(), new cqy(host, host + ":" + port, false)));
	}

	@Override
	public void joinServer(Object parentScreen, Object serverData) {
		if (serverData == null) {
			return;
		}
		if (getWorld() != null) {
			getWorld().S();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new ciz((cjs) parentScreen, getMinecraft(), (cqy) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		boolean isOnIntegratedServer = getMinecraft().w();
		boolean isConnectedToRealms = getMinecraft().Z();
		if (getWorld() != null) {
			getWorld().S();
		}
		getMinecraft().a((cqv) null);
		if (isOnIntegratedServer) {
			displayScreen(new cjx());
		} else if (isConnectedToRealms) {
			RealmsBridge realmsBridge = new RealmsBridge();
			realmsBridge.switchToRealms(new cjx());
		} else {
			displayScreen(new clm(new cjx()));
		}
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return (long) (GLFW.glfwGetTime() * 1000.0);
	}

	public cfi getMinecraft() {
		return cfi.s();
	}

	public cfz getFontrenderer() {
		return getMinecraft().l;
	}

	public cfl getGameSettings() {
		return getMinecraft().t;
	}

	public csy getPlayer() {
		return getMinecraft().i;
	}

	public cqv getWorld() {
		return getMinecraft().g;
	}

	public cga getGuiIngame() {
		return getMinecraft().q;
	}

	@Override
	public boolean isSpectatingSelf() {
		return getSpectatingEntity() instanceof aoc;
	}

	@Override
	public PlayerGameMode getGameMode() {
		return PlayerGameMode.values()[getMinecraft().e.k().a()];
	}

	public aeo getSpectatingEntity() {
		return getMinecraft().S();
	}

	public apr getOpenContainer() {
		return getPlayer().bE;
	}

	@Override
	public String getOpenContainerTitle() {
		if (!(getOpenContainer() instanceof apv))
			return null;
		return ((apv) getOpenContainer()).d().N_().d();
	}

	@Override
	public void closeContainer() {
		getPlayer().w_();
	}

	@Override
	public String getSession() {
		return getMinecraft().B().d();
	}

	@Override
	public String getUsername() {
		return getMinecraft().B().c();
	}

	@Override
	public Proxy getProxy() {
		return getMinecraft().D();
	}

	@Override
	public GameProfile getGameProfile() {
		return getMinecraft().B().e();
	}

	@Override
	public String getFPS() {
		return getMinecraft().z.split(" fps")[0];
	}

	@Override
	public boolean isPlayerNull() {
		return getPlayer() == null;
	}

	@Override
	public boolean isTerrainLoading() {
		return getMinecraftScreen() instanceof cbm;
	}

	@Override
	public double getPlayerPosX() {
		return getSpectatingEntity().q;
	}

	@Override
	public double getPlayerPosY() {
		return getSpectatingEntity().bD().b;
	}

	@Override
	public double getPlayerPosZ() {
		return getSpectatingEntity().s;
	}

	@Override
	public float getPlayerRotationYaw() {
		return getSpectatingEntity().y;
	}

	@Override
	public float getPlayerRotationPitch() {
		return getSpectatingEntity().z;
	}

	@Override
	public int getPlayerChunkX() {
		ej blockPosition = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.o() >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		ej blockPosition = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		ej blockPosition = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		ej blockPosition = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.o() & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		ej blockPosition = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		ej blockPosition = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() & 15;
	}

	@Override
	public boolean hasTargetBlock() {
		return getMinecraft().s != null && getMinecraft().s.a.ordinal() == 1 && getMinecraft().s.a() != null;
	}

	@Override
	public int getTargetBlockX() {
		return getMinecraft().s.a().o();
	}

	@Override
	public int getTargetBlockY() {
		return getMinecraft().s.a().p();
	}

	@Override
	public int getTargetBlockZ() {
		return getMinecraft().s.a().q();
	}

	@Override
	public ResourceLocation getTargetBlockName() {
		ej blockPosition = getMinecraft().s.a();
		//getWorld().a_(getSpectatingEntity().a(20.0D, 0.0F, cdr.c).a()).c();
		return ResourceLocation.fromObfuscated(bfx.e.b(getWorld().a_(blockPosition).c()));
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return cfi.p();
	}

	@Override
	public String getBiome() {
		ej localdt = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().D(localdt) || localdt.p() < 0 || localdt.p() >= 256) {
			return null;
		}
		bna localObject = getMinecraft().g.l(localdt);
		if (localObject.s()) {
			return null;
		}
		return String.valueOf(ayn.aK.b(localObject.i(localdt)));
	}

	@Override
	public int getLightLevel() {
		ej localdt = new ej(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().D(localdt) || localdt.p() < 0 || localdt.p() >= 256) {
			return 0;
		}
		bna localObject = getMinecraft().g.l(localdt);
		if (localObject.s()) {
			return 0;
		}
		return localObject.a(localdt, 0, localObject.z().t.g());
	}

	@Override
	public String getEntityCount() {
		return getMinecraft().h.h().split("[ ,]")[1];
	}

	@Override
	public boolean isRidingEntity() {
		return getSpectatingEntity().bU() != null;
	}

	@Override
	public int getFoodLevel() {
		return isSpectatingSelf() ? ((aoc) getSpectatingEntity()).dw().a() : getPlayer().dw().a();
	}

	@Override
	public float getSaturation() {
		return isSpectatingSelf() ? ((aoc) getSpectatingEntity()).dw().e() : 0;
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof aev))
			return -1;
		return ((aev) entity).cq();
	}

	@Override
	public float getPlayerHealth() {
		return getHealth(getPlayer());
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(ang.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().ct();
	}

	@Override
	public int getAir() {
		return getPlayer().bg();
	}

	@Override
	public boolean isPlayerInsideWater() {
		return getSpectatingEntity().a(wv.a) || getAir() < 300;
	}

	@Override
	public float getResistanceFactor() {
		float referenceDamage = 100;
		referenceDamage = adv.a(referenceDamage, (float) getPlayerArmor(), (float) getPlayer().a(ang.h).e());

		if (getPlayer().a(aej.k)) {
			int resistanceAmplifier = (getPlayer().b(aej.k).c() + 1) * 5;
			int i = 25 - resistanceAmplifier;
			float d = referenceDamage * (float) i;
			referenceDamage = d / 25.0F;
		}

		if (referenceDamage <= 0.0F) {
			return 100;
		} else {
			int enchantmentModifierDamage = awc.a(getPlayer().aT(), adx.n);
			if (enchantmentModifierDamage > 0) {
				referenceDamage = adv.a(referenceDamage, (float) enchantmentModifierDamage);
			}
			return 100 - referenceDamage;
		}
	}

	@Override
	public PotionEffectImpl getPotionForVignette() {
		for (aeh potionEffect : getActivePlayerPotionEffects()) {
			aeg potion = getPotionByEffect(potionEffect);
			if (potion.k()) {
				return wrapPotionEffect(potionEffect);
			}
		}
		for (aeh potionEffect : getActivePlayerPotionEffects()) {
			aeg potion = getPotionByEffect(potionEffect);
			if (!potion.k()) {
				return wrapPotionEffect(potionEffect);
			}
		}

		return null;
	}

	@Override
	public List<PotionEffectImpl> getActivePotionEffects() {
		List<PotionEffectImpl> result = new ArrayList<PotionEffectImpl>(getActivePlayerPotionEffects().size());
		for (aeh potionEffect : getActivePlayerPotionEffects()) {
			result.add(wrapPotionEffect(potionEffect));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<PotionEffectImpl> getDummyPotionEffects() {
		return DUMMY_POTIONS;
	}

	private PotionEffectImpl wrapPotionEffect(aeh potionEffect) {
		String timeString;
		if (potionEffect.h()) {
			timeString = "**:**";
		} else {
			int var2 = d((float) potionEffect.b());
			timeString = xu.a(var2);
		}
		return new PotionEffectImpl(potionEffect.g(), potionEffect.b(), timeString, potionEffect.c() + 1, potionEffect.a().f(), potionEffect.a().k(), potionEffect.e(),
				potionEffect.a().i());
	}

	public static int d(float var0) {
		int var1 = (int) var0;
		return var0 < (float) var1 ? var1 - 1 : var1;
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
		if (hasBad) {
			return 26 * 2;
		} else if (hasGood) {
			return 26;
		}
		return result;
	}

	@Override
	public boolean isHungerPotionActive() {
		return getPlayer().a(aej.q);
	}

	@Override
	public ItemStack getItemInMainHand() {
		if (getPlayer().cB() == null) {
			return null;
		}
		WrappedItemStack wrappedItemStack = new WrappedItemStack(getPlayer().cB());
		return "minecraft:air".equals(wrappedItemStack.getKey()) ? null : wrappedItemStack;
	}

	@Override
	public ItemStack getItemInOffHand() {
		if (getPlayer().cC() == null) {
			return null;
		}
		WrappedItemStack wrappedItemStack = new WrappedItemStack(getPlayer().cC());
		return "minecraft:air".equals(wrappedItemStack.getKey()) ? null : wrappedItemStack;
	}

	@Override
	public ItemStack getItemInArmorSlot(int slot) {
		if (getArmorItemBySlot(slot) == null) {
			return null;
		}
		WrappedItemStack wrappedItemStack = new WrappedItemStack(getArmorItemBySlot(slot));
		return "minecraft:air".equals(wrappedItemStack.getKey()) ? null : wrappedItemStack;
	}

	public Collection<aeh> getActivePlayerPotionEffects() {
		return getPlayer().cn();
	}

	public aeg getPotionByEffect(aeh potionEffect) {
		return potionEffect.a();
	}

	public ata getArmorItemBySlot(int slot) {
		return getPlayer().bB.f(slot);
	}

	@Override
	public ItemStack getItemByName(String resourceName) {
		return new WrappedItemStack(new ata(asw.f.c(new ResourceLocation(resourceName))));
	}

	@Override
	public ItemStack getItemByName(String resourceName, int amount) {
		return new WrappedItemStack(new ata(asw.f.c(new ResourceLocation(resourceName)), amount));
	}

	@Override
	public int getItemCount(String key) {
		int count = 0;
		for (ata itemStack : getPlayer().bB.a) {
			if (itemStack == null)
				continue;
			if (key.equals(WrappedItemStack.getResourceKey(itemStack))) {
				count += itemStack.C();
			}
		}
		return count;
	}

	@Override
	public int getSelectedHotbarSlot() {
		return getPlayer().bB.d;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().bB.d = slot;
		getNetworkManager().a(new nj(slot));
	}

	@Override
	public void onRightClickMouse() {
		try {
			rightClickMouse.invoke(getMinecraft());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void renderItem(ata itemStack, int x, int y) {
		cfg.c();
		GLUtil.enableBlend();
		GLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
		cyw itemRenderer = getMinecraft().V();
		itemRenderer.b(itemStack, x, y);
		itemRenderer.a(getFontrenderer(), itemStack, x, y);
		GLUtil.disableBlend();
		cfg.a();
	}

	@Override
	public void updateScaledResolution() {
	}

	@Override
	public int getWidth() {
		return getMinecraft().f.l();
	}

	@Override
	public int getHeight() {
		return getMinecraft().f.m();
	}

	@Override
	public int getScaledWidth() {
		return getMinecraft().f.p();
	}

	@Override
	public int getScaledHeight() {
		return getMinecraft().f.q();
	}

	@Override
	public int getScaleFactor() {
		return (int) getMinecraft().f.t();
	}

	@Override
	public void drawIngameTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		if (getGuiIngame() != null)
			getGuiIngame().b(x, y, u, v, width, height);
	}

	@Override
	public boolean showDebugScreen() {
		return getGameSettings().aB;
	}

	@Override
	public boolean isPlayerSpectating() {
		return !isSpectatingSelf() || getPlayerController().j();
	}

	@Override
	public boolean shouldDrawHUD() {
		return getPlayerController().a();
	}

	@Override
	public String[] getHotbarKeys() {
		String[] result = new String[9];
		cfe[] hotbarBindings = getGameSettings().at;
		for (int i = 0; i < Math.min(result.length, hotbarBindings.length); i++) {
			result[i] = hotbarBindings[i].j();
		}
		return result;
	}

	@Override
	public String getKeyDisplayStringShort(int key) {
		return key < 0 ? "M" + (key + 101) : (key < 256 ? GLFW.glfwGetKeyName(key, -1) : String.format("%c", (char) (key - 256)).toUpperCase());
	}

	private cqu getPlayerController() {
		return getMinecraft().e;
	}

	@Override
	public Gui getCurrentScreen() {
		if (!(getMinecraft().m instanceof GuiHandle)) {
			return null;
		}
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

	public void messagePlayer(ij chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.getString().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().d.a(new mh(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new mp(new ResourceLocation(channel), new hy(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private hw getNetworkManager() {
		return getMinecraft().o() != null ? getMinecraft().o().a() : null;
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
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
			InputStream in = new ByteArrayInputStream(os.toByteArray());
			return getTextureManager().a(name, new dcr(dcw.a(in)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof dcr) {
			ctp.i(((dcr) resourceLocation).c());
		} else {
			getTextureManager().a((pc) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		throw new UnsupportedOperationException("not available in MC 1.13+");
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((pc) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		throw new UnsupportedOperationException("not available in MC 1.13+");
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public ddd getTextureManager() {
		return getMinecraft().E();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		cub var4 = cub.a();
		ctf var5 = var4.c();
		bindTexture(cga.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		var5.a(7, ddk.o);
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
		cdq objectMouseOver = getSpectatingEntity().a(maxDistance, 1f, cdr.a);
		double var3 = maxDistance;
		cdt entityPosition = getSpectatingEntity().f(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.c.f(entityPosition);
		}

		cdt look = getSpectatingEntity().f(1f);
		cdt mostFarPoint = entityPosition.b(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance);
		aeo pointedEntity = null;
		cdt hitVector = null;
		List<aeo> entitiesWithinAABBExcludingEntity = getWorld().a(getSpectatingEntity(),
				getSpectatingEntity().bD().a(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance).b(1.0, 1.0, 1.0), aer.e.and(aeo::aD));
		double distance = var3;

		for (aeo entity : entitiesWithinAABBExcludingEntity) {
			cdp axisAlignedBB = entity.bD().g(entity.aJ());
			cdq intercept = axisAlignedBB.b(entityPosition, mostFarPoint);
			if (axisAlignedBB.b(entityPosition)) {
				if (distance >= 0.0D) {
					pointedEntity = entity;
					hitVector = intercept == null ? entityPosition : intercept.c;
					distance = 0.0D;
				}
			} else if (intercept != null) {
				double distanceToHitVec = entityPosition.f(intercept.c);
				if (distanceToHitVec < distance || distance == 0.0D) {
					if (entity == getSpectatingEntity().bU()) {
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
			objectMouseOver = new cdq(pointedEntity, hitVector);
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
		cer scoreboard = getWorld().F();
		if (scoreboard == null) {
			return null;
		}
		ceo objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d().getString();
		Collection<ceq> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (ceq score : scores) {
			lines.put(score.e(), score.b());
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
			Object minecraftInstance = minecraft.getMethod(Names.getMinecraft.getName()).invoke(null);
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
		return getMinecraft().ax();
	}

	@Override
	public File getMinecraftDataDirectory() {
		return getMinecraft().w;
	}

	@Override
	public void dispatchKeypresses() {
		// already handled above using glfw...
	}

	@Override
	public void shutdown() {
		getMinecraft().h();
	}
}
