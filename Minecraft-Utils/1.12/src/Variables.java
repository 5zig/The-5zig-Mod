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
				rightClickMouse = Names.minecraft.load().getDeclaredMethod(Names.rightClickMouse.getName());
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private bir scaledResolution;
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
		return new WrappedTextfield((bjc) handle);
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
		return new WrappedGui((bli) lastScreen);
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
		return getMinecraftScreen() instanceof bkl;
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
			displayScreen(new bkl());
		}
		bjc chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private bjc getChatField() {
		bkl chatGUI = (bkl) getMinecraftScreen();
		bjc chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (bjc) forgeChatField.get(chatGUI);
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
		return new ho(prefix).a((hh) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof bmz;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		bhw[] currentKeybindings = getGameSettings().as;
		bhw[] customKeybindings = new bhw[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (bhw) keybindings.get(i);
		}
		getGameSettings().as = Utils.concat(currentKeybindings, customKeybindings);

		getGameSettings().a();
	}

	@Override
	public void playSound(String sound, float pitch) {
		playSound("minecraft", sound, pitch);
	}

	@Override
	public void playSound(String domain, String sound, float pitch) {
		getMinecraft().U().a(cgn.a(new qc(new ResourceLocation(domain, sound)), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public bsc getServerData() {
		return getMinecraft().C();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((bsc) null);
	}

	@Override
	public String getServer() {
		bsc serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (bsa wrapped : getPlayer().d.d()) {
			result.add(new WrappedNetworkPlayerInfo(wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getGameSettings().ah.e()) && ((!getMinecraft().E()) || (getServerPlayers().size() > 1));
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
		return cew.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((bli) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((bli) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().O();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bkp((bli) getMinecraftScreen(), getMinecraft(), new bsc(host, host + ":" + port, false)));
	}

	@Override
	public void joinServer(Object parentScreen, Object serverData) {
		if (serverData == null) {
			return;
		}
		if (getWorld() != null) {
			getWorld().O();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bkp((bli) parentScreen, getMinecraft(), (bsc) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		boolean isOnIntegratedServer = getMinecraft().D();
		boolean isConnectedToRealms = getMinecraft().ah();
		if (getWorld() != null) {
			getWorld().O();
		}
		getMinecraft().a((brz) null);
		if (isOnIntegratedServer) {
			displayScreen(new blp());
		} else if (isConnectedToRealms) {
			RealmsBridge realmsBridge = new RealmsBridge();
			realmsBridge.switchToRealms(new blp());
		} else {
			displayScreen(new bnd(new blp()));
		}
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return bhz.I();
	}

	public bhz getMinecraft() {
		return bhz.z();
	}

	public bin getFontrenderer() {
		return getMinecraft().k;
	}

	public bib getGameSettings() {
		return getMinecraft().t;
	}

	public bub getPlayer() {
		return getMinecraft().h;
	}

	public brz getWorld() {
		return getMinecraft().f;
	}

	public bio getGuiIngame() {
		return getMinecraft().q;
	}

	@Override
	public boolean isSpectatingSelf() {
		return getSpectatingEntity() instanceof aeb;
	}

	@Override
	public PlayerGameMode getGameMode() {
		return PlayerGameMode.values()[getMinecraft().c.l().a()];
	}

	public ve getSpectatingEntity() {
		return getMinecraft().aa();
	}

	public afp getOpenContainer() {
		return getPlayer().by;
	}

	@Override
	public String getOpenContainerTitle() {
		if (!(getOpenContainer() instanceof aft))
			return null;
		return ((aft) getOpenContainer()).e().h_();
	}

	@Override
	public void closeContainer() {
		getPlayer().p();
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
		return getMinecraftScreen() instanceof bez;
	}

	@Override
	public double getPlayerPosX() {
		return getSpectatingEntity().p;
	}

	@Override
	public double getPlayerPosY() {
		return getSpectatingEntity().bw().b;
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
		et blockPosition = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		et blockPosition = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		et blockPosition = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.r() >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		et blockPosition = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		et blockPosition = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		et blockPosition = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.r() & 15;
	}

	@Override
	public boolean hasTargetBlock() {
		return getMinecraft().s != null && getMinecraft().s.a.ordinal() == 1 && getMinecraft().s.a() != null;
	}

	@Override
	public int getTargetBlockX() {
		return getMinecraft().s.a().p();
	}

	@Override
	public int getTargetBlockY() {
		return getMinecraft().s.a().q();
	}

	@Override
	public int getTargetBlockZ() {
		return getMinecraft().s.a().r();
	}

	@Override
	public ResourceLocation getTargetBlockName() {
		et blockPosition = getMinecraft().s.a();
		return ResourceLocation.fromObfuscated(aoq.h.b(getWorld().o(blockPosition).u()));
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return bhz.w();
	}

	@Override
	public String getBiome() {
		et localdt = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt) || localdt.q() < 0 || localdt.q() >= 256) {
			return null;
		}
		axu localObject = getMinecraft().f.f(localdt);
		if (localObject.f()) {
			return null;
		}
		return localObject.a(localdt, getMinecraft().f.C()).l();
	}

	@Override
	public int getLightLevel() {
		et localdt = new et(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt) || localdt.q() < 0 || localdt.q() >= 256) {
			return 0;
		}
		axu localObject = getMinecraft().f.f(localdt);
		if (localObject.f()) {
			return 0;
		}
		return localObject.a(localdt, 0);
	}

	@Override
	public String getEntityCount() {
		return getMinecraft().g.h().split("[ ,]")[1];
	}

	@Override
	public boolean isRidingEntity() {
		return getSpectatingEntity().bJ() != null;
	}

	@Override
	public int getFoodLevel() {
		return isSpectatingSelf() ? ((aeb) getSpectatingEntity()).di().a() : getPlayer().di().a();
	}

	@Override
	public float getSaturation() {
		return isSpectatingSelf() ? ((aeb) getSpectatingEntity()).di().e() : 0;
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof vl))
			return -1;
		return ((vl) entity).cd();
	}

	@Override
	public float getPlayerHealth() {
		return getHealth(getPlayer());
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(adf.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().cg();
	}

	@Override
	public int getAir() {
		return getPlayer().aZ();
	}

	@Override
	public boolean isPlayerInsideWater() {
		return getSpectatingEntity().a(bcu.h);
	}

	@Override
	public float getResistanceFactor() {
		float referenceDamage = 100;
		referenceDamage = un.a(referenceDamage, (float) getPlayer().cg(), (float) getPlayer().a(adf.h).e());

		if (getPlayer().a(uz.k)) {
			int resistanceAmplifier = (getPlayer().b(uz.k).c() + 1) * 5;
			int i = 25 - resistanceAmplifier;
			float d = referenceDamage * (float) i;
			referenceDamage = d / 25.0F;
		}

		if (referenceDamage <= 0.0F) {
			return 100;
		} else {
			int enchantmentModifierDamage = alk.a(getPlayer().aP(), up.n);
			if (enchantmentModifierDamage > 0) {
				referenceDamage = un.a(referenceDamage, (float) enchantmentModifierDamage);
			}
			return 100 - referenceDamage;
		}
	}

	@Override
	public PotionEffectImpl getPotionForVignette() {
		for (uy potionEffect : getActivePlayerPotionEffects()) {
			ux potion = getPotionByEffect(potionEffect);
			if (potion.i()) {
				return wrapPotionEffect(potionEffect);
			}
		}
		for (uy potionEffect : getActivePlayerPotionEffects()) {
			ux potion = getPotionByEffect(potionEffect);
			if (!potion.i()) {
				return wrapPotionEffect(potionEffect);
			}
		}

		return null;
	}

	@Override
	public List<PotionEffectImpl> getActivePotionEffects() {
		List<PotionEffectImpl> result = new ArrayList<PotionEffectImpl>(getActivePlayerPotionEffects().size());
		for (uy potionEffect : getActivePlayerPotionEffects()) {
			result.add(wrapPotionEffect(potionEffect));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<PotionEffectImpl> getDummyPotionEffects() {
		return DUMMY_POTIONS;
	}

	private PotionEffectImpl wrapPotionEffect(uy potionEffect) {
		return new PotionEffectImpl(potionEffect.f(), potionEffect.b(), ux.a(potionEffect, 1), potionEffect.c() + 1, potionEffect.a().d(), potionEffect.a().i(), potionEffect.e(),
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
		return getPlayer().a(uz.q);
	}

	@Override
	public ItemStack getItemInMainHand() {
		if (getPlayer().co() == null) {
			return null;
		}
		WrappedItemStack wrappedItemStack = new WrappedItemStack(getPlayer().co());
		return "minecraft:air".equals(wrappedItemStack.getKey()) ? null : wrappedItemStack;
	}

	@Override
	public ItemStack getItemInOffHand() {
		if (getPlayer().cp() == null) {
			return null;
		}
		WrappedItemStack wrappedItemStack = new WrappedItemStack(getPlayer().cp());
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

	public Collection<uy> getActivePlayerPotionEffects() {
		return getPlayer().ca();
	}

	public ux getPotionByEffect(uy potionEffect) {
		return potionEffect.a();
	}

	public ain getArmorItemBySlot(int slot) {
		return getPlayer().bv.g(slot);
	}

	@Override
	public ItemStack getItemByName(String resourceName) {
		return new WrappedItemStack(new ain(ail.b(resourceName)));
	}

	@Override
	public ItemStack getItemByName(String resourceName, int amount) {
		return new WrappedItemStack(new ain(ail.b(resourceName), amount));
	}

	@Override
	public int getItemCount(String key) {
		int count = 0;
		for (ain itemStack : getPlayer().bv.a) {
			if (itemStack == null)
				continue;
			if (key.equals(WrappedItemStack.getResourceKey(itemStack))) {
				count += itemStack.E();
			}
		}
		return count;
	}

	@Override
	public int getSelectedHotbarSlot() {
		return getPlayer().bv.d;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().bv.d = slot;
		getNetworkManager().a(new lu(slot));
	}

	@Override
	public void onRightClickMouse() {
		try {
			rightClickMouse.invoke(getMinecraft());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void renderItem(ain itemStack, int x, int y) {
		bhx.c();
		GLUtil.enableBlend();
		GLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
		bzu itemRenderer = getMinecraft().ad();
		itemRenderer.b(itemStack, x, y);
		itemRenderer.a(getFontrenderer(), itemStack, x, y);
		GLUtil.disableBlend();
		bhx.a();
	}

	@Override
	public void updateScaledResolution() {
		this.scaledResolution = new bir(getMinecraft());
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
		return getGameSettings().ax;
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
		bhw[] hotbarBindings = getGameSettings().ap;
		for (int i = 0; i < Math.min(result.length, hotbarBindings.length); i++) {
			result[i] = getKeyDisplayStringShort(hotbarBindings[i].j());
		}
		return result;
	}

	@Override
	public String getKeyDisplayStringShort(int key) {
		return key < 0 ? "M" + (key + 101) : (key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase());
	}

	private bry getPlayerController() {
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

	public void messagePlayer(hh chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.d().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().d.a(new la(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new lh(channel, new gy(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private gw getNetworkManager() {
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
		return getTextureManager().a(name, new cde(image));
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof cde) {
			buq.i(((cde) resourceLocation).b());
		} else {
			getTextureManager().a((nd) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
		if (resourceLocation instanceof nd) {
			getTextureManager().c((nd) resourceLocation);
		}
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		cde dynamicImage = new cde(width, height);
		getTextureManager().a((ResourceLocation) resourceLocation, dynamicImage);
		return dynamicImage;
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((nd) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), ((cde) dynamicImage).e(), 0, image.getWidth());
		((cde) dynamicImage).d();
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public cdp getTextureManager() {
		return getMinecraft().N();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		bvc var4 = bvc.a();
		bui var5 = var4.c();
		bindTexture(bio.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		var5.a(7, cdw.i);
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
		bha objectMouseOver = getSpectatingEntity().a(maxDistance, 1f);
		double var3 = maxDistance;
		bhc entityPosition = getSpectatingEntity().f(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.c.f(entityPosition);
		}

		bhc look = getSpectatingEntity().f(1f);
		bhc mostFarPoint = entityPosition.b(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance);
		ve pointedEntity = null;
		bhc hitVector = null;
		List<ve> entitiesWithinAABBExcludingEntity = getWorld().a(getSpectatingEntity(),
				getSpectatingEntity().bw().a(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance).b(1.0, 1.0, 1.0), Predicates.and(vi.e, new Predicate<ve>() {
					@Override
					public boolean apply(ve entity) {
						return entity != null && entity.aq();
					}

					@Override
					public boolean test(ve input) {
						return true;
					}
				}));
		double distance = var3;

		for (ve entity : entitiesWithinAABBExcludingEntity) {
			bgz axisAlignedBB = entity.bw().g(entity.aG());
			bha intercept = axisAlignedBB.b(entityPosition, mostFarPoint);
			if (axisAlignedBB.b(entityPosition)) {
				if (distance >= 0.0D) {
					pointedEntity = entity;
					hitVector = intercept == null ? entityPosition : intercept.c;
					distance = 0.0D;
				}
			} else if (intercept != null) {
				double distanceToHitVec = entityPosition.f(intercept.c);
				if (distanceToHitVec < distance || distance == 0.0D) {
					if (entity == getSpectatingEntity().bF()) {
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
			objectMouseOver = new bha(pointedEntity, hitVector);
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
		bhi scoreboard = getWorld().af();
		if (scoreboard == null) {
			return null;
		}
		bhe objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d();
		Collection<bhg> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (bhg score : scores) {
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
		return getMinecraft().aF();
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
