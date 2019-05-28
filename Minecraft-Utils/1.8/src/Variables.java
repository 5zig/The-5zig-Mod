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
import eu.the5zig.mod.gui.ingame.IGui2ndChat;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.gui.ingame.PotionEffectImpl;
import eu.the5zig.mod.gui.ingame.ScoreboardImpl;
import eu.the5zig.mod.util.*;
import eu.the5zig.util.Callback;
import eu.the5zig.util.Utils;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.buffer.ByteBuf;
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
				forgeChatField = Thread.currentThread().getContextClassLoader().loadClass(Names.guiChat.getName()).getDeclaredField("field_146415_a");
				forgeChatField.setAccessible(true);
				rightClickMouse = Names.minecraft.load().getDeclaredMethod("func_147121_ag");
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				rightClickMouse = Names.minecraft.load().getDeclaredMethod("au");
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private buf scaledResolution;
	private IGui2ndChat gui2ndChat = new Gui2ndChat();

	private final ResourceManager resourceManager;

	private static final List<PotionEffectImpl> DUMMY_POTIONS = Arrays.asList(new PotionEffectImpl("potion.jump", 20, "0:01", 1, 10, true, true, 0x22ff4c),
			new PotionEffectImpl("potion.moveSpeed", 20 * 50, "0:50", 1, 0, true, true, 0x7cafc6));

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
	public IButton createIconButton(IResourceLocation resourceLocation, int u, int v, int id, int x, int y) {
		return new IconButton(resourceLocation, u, v, id, x, y);
	}

	@Override
	public IButton createAudioButton(int id, int x, int y, AudioCallback callback) {
		return new AudioButton(id, x, y, callback);
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
		return new WrappedTextfield((bul) handle);
	}

	@Override
	public <E extends Row> GuiList<E> createGuiList(Clickable<E> clickable, int width, int height, int top, int bottom, int left, int right, List<E> rows) {
		return new GuiList<E>(clickable, width, height, top, bottom, left, right, rows);
	}

	@Override
	public <E extends Row> GuiList<E> createGuiListChat(int width, int height, int top, int bottom, int left, int right, int scrollx, List<E> rows, GuiListChatCallback callback) {
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
		return new WrappedGui((bxf) lastScreen);
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
		return getMinecraftScreen() instanceof bvx;
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
		if (!isChatOpened())
			return;
		bul chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private bul getChatField() {
		bvx chatGUI = (bvx) getMinecraftScreen();
		bul chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (bul) forgeChatField.get(chatGUI);
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
		return new hy(prefix).a((ho) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof bzm;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		bsr[] currentKeybindings = getGameSettings().at;
		bsr[] customKeybindings = new bsr[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (bsr) keybindings.get(i);
		}
		getGameSettings().at = Utils.concat(currentKeybindings, customKeybindings);

		getGameSettings().a();
	}

	@Override
	public void playSound(String sound, float pitch) {
		playSound("minecraft", sound, pitch);
	}

	@Override
	public void playSound(String domain, String sound, float pitch) {
		getMinecraft().U().a(cxy.a(new oa(domain, sound), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public cew getServerData() {
		return getMinecraft().C();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((cew) null);
	}

	@Override
	public String getServer() {
		cew serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (Object wrapped : getPlayer().a.d()) {
			result.add(new WrappedNetworkPlayerInfo((ces) wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getMinecraft().t.aj.d()) && ((!getMinecraft().E()) || (getServerPlayers().size() > 1));
	}

	@Override
	public void setFOV(float fov) {
		getGameSettings().aD = fov;
	}

	@Override
	public float getFOV() {
		return getGameSettings().aD;
	}

	@Override
	public void setSmoothCamera(boolean smoothCamera) {
		getGameSettings().aB = smoothCamera;
	}

	@Override
	public boolean isSmoothCamera() {
		return getGameSettings().aB;
	}

	@Override
	public String translate(String location, Object... values) {
		return cwc.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((bxf) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((bxf) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().H();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bwb((bxf) getMinecraftScreen(), getMinecraft(), new cew(host, host + ":" + port)));
	}

	@Override
	public void joinServer(Object parentScreen, Object serverData) {
		if (serverData == null) {
			return;
		}
		if (getWorld() != null) {
			getWorld().H();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bwb((bxf) parentScreen, getMinecraft(), (cew) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		if (getWorld() != null) {
			getWorld().H();
		}
		getMinecraft().a((cen) null);
		displayScreen(new bxq());
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return bsu.I();
	}

	public bsu getMinecraft() {
		return bsu.z();
	}

	public bty getFontrenderer() {
		return getMinecraft().k;
	}

	public bto getGameSettings() {
		return getMinecraft().t;
	}

	public cfg getPlayer() {
		return getMinecraft().h;
	}

	public cen getWorld() {
		return getMinecraft().f;
	}

	public btz getGuiIngame() {
		return getMinecraft().q;
	}

	@Override
	public boolean isSpectatingSelf() {
		return getSpectatingEntity() instanceof ahd;
	}

	@Override
	public PlayerGameMode getGameMode() {
		return PlayerGameMode.values()[getMinecraft().c.l().a()];
	}

	public wv getSpectatingEntity() {
		return getMinecraft().aa();
	}

	public aib getOpenContainer() {
		return getPlayer().bi;
	}

	@Override
	public String getOpenContainerTitle() {
		if (!(getOpenContainer() instanceof aim))
			return null;
		return ((aim) getOpenContainer()).e().d_();
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
		return getMinecraft().A.split(" fps")[0];
	}

	@Override
	public boolean isPlayerNull() {
		return getPlayer() == null;
	}

	@Override
	public boolean isTerrainLoading() {
		return getMinecraftScreen() instanceof bxd;
	}

	@Override
	public double getPlayerPosX() {
		return getSpectatingEntity().s;
	}

	@Override
	public double getPlayerPosY() {
		return getSpectatingEntity().aQ().b;
	}

	@Override
	public double getPlayerPosZ() {
		return getSpectatingEntity().u;
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
		dt blockPosition = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.n() >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		dt blockPosition = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.o() >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		dt blockPosition = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		dt blockPosition = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.n() & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		dt blockPosition = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.o() & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		dt blockPosition = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() & 15;
	}

	@Override
	public boolean hasTargetBlock() {
		return getMinecraft().s != null && getMinecraft().s.a.ordinal() == 1 && getMinecraft().s.a() != null;
	}

	@Override
	public int getTargetBlockX() {
		return getMinecraft().s.a().n();
	}

	@Override
	public int getTargetBlockY() {
		return getMinecraft().s.a().o();
	}

	@Override
	public int getTargetBlockZ() {
		return getMinecraft().s.a().p();
	}

	@Override
	public ResourceLocation getTargetBlockName() {
		dt blockPosition = getMinecraft().s.a();
		return ResourceLocation.fromObfuscated((oa) atr.c.c(getWorld().p(blockPosition).c()));
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return bsu.v();
	}

	@Override
	public String getBiome() {
		dt localdt = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt)) {
			return null;
		}
		bfh localObject = getMinecraft().f.f(localdt);
		return localObject.a(localdt, getMinecraft().f.v()).ah;
	}

	@Override
	public int getLightLevel() {
		dt localdt = new dt(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt)) {
			return 0;
		}
		bfh localObject = getMinecraft().f.f(localdt);
		return localObject.a(localdt, 0);
	}

	@Override
	public String getEntityCount() {
		return getMinecraft().g.g().split(" |,")[1];
	}

	@Override
	public boolean isRidingEntity() {
		return getSpectatingEntity().m != null;
	}

	@Override
	public int getFoodLevel() {
		return isSpectatingSelf() ? ((ahd) getSpectatingEntity()).ck().a() : getPlayer().ck().a();
	}

	@Override
	public float getSaturation() {
		return isSpectatingSelf() ? ((ahd) getSpectatingEntity()).ck().e() : 0;
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof xm))
			return -1;
		return ((xm) entity).bm();
	}

	@Override
	public float getPlayerHealth() {
		return getPlayer().bm();
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(afs.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().bq();
	}

	@Override
	public int getAir() {
		return getPlayer().aA();
	}

	@Override
	public boolean isPlayerInsideWater() {
		return getSpectatingEntity().a(bof.h);
	}

	@Override
	public float getResistanceFactor() {
		float referenceDamage = 100;
		int i1 = 25 - getPlayer().bq();
		float d1 = referenceDamage * (float) i1;
		referenceDamage = d1 / 25.0F;

		if (getPlayer().a(wp.m)) {
			int resistanceAmplifier = (getPlayer().b(wp.m).c() + 1) * 5;
			int i = 25 - resistanceAmplifier;
			float d = referenceDamage * (float) i;
			referenceDamage = d / 25.0F;
		}

		if (referenceDamage <= 0.0F) {
			return 100;
		} else {
			int enchantmentModifierDamage = aph.a(getPlayer().at(), wh.k);
			if (enchantmentModifierDamage > 20) {
				enchantmentModifierDamage = 20;
			}
			if (enchantmentModifierDamage > 0) {
				int k = 25 - enchantmentModifierDamage;
				float f = referenceDamage * (float) k;
				referenceDamage = f / 25.0F;
			}
			return 100 - referenceDamage;
		}
	}

	@Override
	public PotionEffectImpl getPotionForVignette() {
		for (wq potionEffect : getActivePlayerPotionEffects()) {
			wp potion = getPotionByEffect(potionEffect);
			if (potion != null && potion.g()) {
				return wrapPotionEffect(potionEffect);
			}
		}
		for (wq potionEffect : getActivePlayerPotionEffects()) {
			wp potion = getPotionByEffect(potionEffect);
			if (potion != null && !potion.g()) {
				return wrapPotionEffect(potionEffect);
			}
		}

		return null;
	}

	@Override
	public List<PotionEffectImpl> getActivePotionEffects() {
		List<PotionEffectImpl> result = new ArrayList<PotionEffectImpl>(getActivePlayerPotionEffects().size());
		for (wq potionEffect : getActivePlayerPotionEffects()) {
			result.add(wrapPotionEffect(potionEffect));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<PotionEffectImpl> getDummyPotionEffects() {
		return DUMMY_POTIONS;
	}

	@Override
	public int getPotionEffectIndicatorHeight() {
		return 0;
	}

	private PotionEffectImpl wrapPotionEffect(wq potionEffect) {
		wp potion = getPotionByEffect(potionEffect);
		return new PotionEffectImpl(potion == null ? "" : potionEffect.g(), potionEffect.b(), wp.a(potionEffect), potionEffect.c() + 1, potion == null ? -1 : potion.f(),
				potion == null || !potion.g(), true, potion == null ? 0 : potion.k());
	}

	@Override
	public boolean isHungerPotionActive() {
		return getPlayer().a(wp.s);
	}

	@Override
	public ItemStack getItemInMainHand() {
		return getPlayerItemInHand() == null ? null : new WrappedItemStack(getPlayerItemInHand());
	}

	@Override
	public ItemStack getItemInOffHand() {
		return null;
	}

	@Override
	public ItemStack getItemInArmorSlot(int slot) {
		return getArmorItemBySlot(slot) == null ? null : new WrappedItemStack(getArmorItemBySlot(slot));
	}

	public Collection<wq> getActivePlayerPotionEffects() {
		return getPlayer().bk();
	}

	public wp getPotionByEffect(wq potionEffect) {
		return wp.a[potionEffect.a()];
	}

	public amj getPlayerItemInHand() {
		return getPlayer().bY();
	}

	public amj getArmorItemBySlot(int slot) {
		return getPlayer().bg.e(slot);
	}

	@Override
	public ItemStack getItemByName(String resourceName) {
		return new WrappedItemStack(new amj(alq.d(resourceName)));
	}

	@Override
	public ItemStack getItemByName(String resourceName, int amount) {
		return new WrappedItemStack(new amj(alq.d(resourceName), amount));
	}

	@Override
	public int getItemCount(String key) {
		int count = 0;
		for (amj itemStack : getPlayer().bg.a) {
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
		return getPlayer().bg.c;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().bg.c = slot;
		getNetworkManager().a(new ms(slot));
	}

	@Override
	public void onRightClickMouse() {
		try {
			rightClickMouse.invoke(getMinecraft());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void renderItem(amj itemStack, int x, int y) {
		bss.c();
		cjm.l();
		cjm.a(770, 771, 1, 0);
		cqh itemRenderer = getMinecraft().af();
		itemRenderer.b(itemStack, x, y);
		itemRenderer.a(getFontrenderer(), itemStack, x, y);
		cjm.k();
		bss.a();
		cjm.c();
	}

	@Override
	public void updateScaledResolution() {
		this.scaledResolution = new buf(getMinecraft(), getWidth(), getHeight());
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
		return getGameSettings().ay;
	}

	@Override
	public boolean isPlayerSpectating() {
		return !isSpectatingSelf() || getPlayerController().a();
	}

	@Override
	public boolean shouldDrawHUD() {
		return getPlayerController().b();
	}

	private cem getPlayerController() {
		return getMinecraft().c;
	}

	@Override
	public String[] getHotbarKeys() {
		String[] result = new String[9];
		bsr[] hotbarBindings = getGameSettings().as;
		for (int i = 0; i < Math.min(result.length, hotbarBindings.length); i++) {
			result[i] = getKeyDisplayStringShort(hotbarBindings[i].i());
		}
		return result;
	}

	@Override
	public String getKeyDisplayStringShort(int key) {
		return key < 0 ? "M" + (key + 101) : (key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase());
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

	public void messagePlayer(hy chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.d().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().a.a(new lu(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new mc(channel, new hd(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private gr getNetworkManager() {
		return getMinecraft().t() != null ? getMinecraft().t().a() : null;
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
		return getTextureManager().a(name, new ctp(image));
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof ctp) {
			cjm.i(((ctp)resourceLocation).b());
		} else {
			getTextureManager().a((oa) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
		if (resourceLocation instanceof oa) {
			getTextureManager().c((oa) resourceLocation);
		}
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		ctp dynamicImage = new ctp(width, height);
		getTextureManager().a((ResourceLocation) resourceLocation, dynamicImage);
		return dynamicImage;
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((oa) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), ((ctp) dynamicImage).e(), 0, image.getWidth());
		((ctp) dynamicImage).d();
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public cug getTextureManager() {
		return getMinecraft().N();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		ckx tessellator = ckx.a();
		civ var6 = tessellator.c();
		MinecraftFactory.getVars().bindTexture(bub.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32.0F;
		var6.b();
		var6.a(4210752, y2);
		var6.a(x1, y2, 0.0D, 0.0D, y2 / f1);
		var6.a((x1 + x2), y2, 0.0D, x2 / f1, y2 / f1);
		var6.a(4210752, x2);
		var6.a((x1 + x2), y1, 0.0D, x2 / f1, y1 / f1);
		var6.a(x1, y1, 0.0D, 0.0D, y1 / f1);
		tessellator.b();
	}

	@Override
	public void setIngameFocus() {
		getMinecraft().n();
	}

	@Override
	public MouseOverObject calculateMouseOverDistance(double maxDistance) {
		if (getSpectatingEntity() == null || getWorld() == null)
			return null;

		bru objectMouseOver = getSpectatingEntity().a(maxDistance, 1f);
		double var3 = maxDistance;
		brw entityPosition = getSpectatingEntity().e(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.c.f(entityPosition);
		}

		brw look = getSpectatingEntity().d(1f);
		brw mostFarPoint = entityPosition.b(look.a * maxDistance, look.b * maxDistance, look.c * maxDistance);
		wv pointedEntity = null;
		brw hitVector = null;
		List<wv> entitiesWithinAABBExcludingEntity = getWorld().b(getSpectatingEntity(),
				getSpectatingEntity().aQ().a(look.a * maxDistance, look.b * maxDistance, look.c * maxDistance).b(1.0, 1.0, 1.0));
		double distance = var3;

		for (wv entity : entitiesWithinAABBExcludingEntity) {
			if (entity.ad()) {
				float collisionBorderSize = entity.ao();
				brt axisAlignedBB = entity.aQ().b((double) collisionBorderSize, (double) collisionBorderSize, (double) collisionBorderSize);
				bru intercept = axisAlignedBB.a(entityPosition, mostFarPoint);
				if (axisAlignedBB.a(entityPosition)) {
					if (distance >= 0.0D) {
						pointedEntity = entity;
						hitVector = intercept == null ? entityPosition : intercept.c;
						distance = 0.0D;
					}
				} else if (intercept != null) {
					double distanceToHitVec = entityPosition.f(intercept.c);
					if (distanceToHitVec < distance || distance == 0.0D) {
						if (entity == getSpectatingEntity().m) {
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
		}

		if (pointedEntity != null && (distance < var3 || objectMouseOver == null)) {
			objectMouseOver = new bru(pointedEntity, hitVector);
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
		bsd scoreboard = getWorld().Z();
		if (scoreboard == null) {
			return null;
		}
		bry objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d();
		Collection<bsa> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (bsa score : scores) {
			lines.put(score.e(), score.c());
		}
		return new ScoreboardImpl(displayName, lines);
	}

	@Override
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	@Override
	public ISystemPowerStatus getBatteryStatus() {
		return null;
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
			Field aB = minecraft.getDeclaredField("ax");
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
		return getMinecraft().aH();
	}

	@Override
	public File getMinecraftDataDirectory() {
		return getMinecraft().v;
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
		getMinecraft().g();
	}

}
