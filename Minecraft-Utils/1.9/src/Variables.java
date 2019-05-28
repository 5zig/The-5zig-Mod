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

	private bcx scaledResolution;
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
		return new WrappedTextfield((bdd) handle);
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
		return new WrappedGui((bfb) lastScreen);
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
		return getMinecraftScreen() instanceof bee;
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
			displayScreen(new bee());
		}
		bdd chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private bdd getChatField() {
		bee chatGUI = (bee) getMinecraftScreen();
		bdd chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (bdd) forgeChatField.get(chatGUI);
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
		return new fa(prefix).a((eu) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof bgn;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		bcc[] currentKeybindings = getGameSettings().al;
		bcc[] customKeybindings = new bcc[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (bcc) keybindings.get(i);
		}
		getGameSettings().al = Utils.concat(currentKeybindings, customKeybindings);

		getGameSettings().a();
	}

	@Override
	public void playSound(String sound, float pitch) {
		playSound("minecraft", sound, pitch);
	}

	@Override
	public void playSound(String domain, String sound, float pitch) {
		getMinecraft().U().a(bye.a(new nf(new ResourceLocation(domain, sound)), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public bkx getServerData() {
		return getMinecraft().C();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((bkx) null);
	}

	@Override
	public String getServer() {
		bkx serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (bkv wrapped : getPlayer().d.d()) {
			result.add(new WrappedNetworkPlayerInfo(wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getGameSettings().ad.e()) && ((!getMinecraft().E()) || (getServerPlayers().size() > 1));
	}

	@Override
	public void setFOV(float fov) {
		getGameSettings().aw = fov;
	}

	@Override
	public float getFOV() {
		return getGameSettings().aw;
	}

	@Override
	public void setSmoothCamera(boolean smoothCamera) {
		getGameSettings().au = smoothCamera;
	}

	@Override
	public boolean isSmoothCamera() {
		return getGameSettings().au;
	}

	@Override
	public String translate(String location, Object... values) {
		return bwo.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((bfb) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((bfb) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().M();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bei((bfb) getMinecraftScreen(), getMinecraft(), new bkx(host, host + ":" + port, false)));
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
		displayScreen(new bei((bfb) parentScreen, getMinecraft(), (bkx) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		boolean isOnIntegratedServer = getMinecraft().D();
		boolean isConnectedToRealms = getMinecraft().ai();
		if (getWorld() != null) {
			getWorld().M();
		}
		getMinecraft().a((bku) null);
		if (isOnIntegratedServer) {
			displayScreen(new bfi());
		} else if (isConnectedToRealms) {
			RealmsBridge realmsBridge = new RealmsBridge();
			realmsBridge.switchToRealms(new bfi());
		} else {
			displayScreen(new bgr(new bfi()));
		}
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return bcf.I();
	}

	public bcf getMinecraft() {
		return bcf.z();
	}

	public bct getFontrenderer() {
		return getMinecraft().k;
	}

	public bch getGameSettings() {
		return getMinecraft().u;
	}

	public bmt getPlayer() {
		return getMinecraft().h;
	}

	public bku getWorld() {
		return getMinecraft().f;
	}

	public bcu getGuiIngame() {
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
		return getPlayer().bt;
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
		return getMinecraftScreen() instanceof bfa;
	}

	@Override
	public double getPlayerPosX() {
		return getSpectatingEntity().p;
	}

	@Override
	public double getPlayerPosY() {
		return getSpectatingEntity().bl().b;
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
		cj blockPosition = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		cj blockPosition = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		cj blockPosition = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.r() >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		cj blockPosition = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.p() & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		cj blockPosition = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		return blockPosition.q() & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		cj blockPosition = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
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
		cj blockPosition = getMinecraft().t.a();
		return ResourceLocation.fromObfuscated(akf.h.b(getWorld().o(blockPosition).t()));
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return bcf.w();
	}

	@Override
	public String getBiome() {
		cj localdt = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt)) {
			return null;
		}
		ase localObject = getMinecraft().f.f(localdt);
		return localObject.a(localdt, getMinecraft().f.A()).l();
	}

	@Override
	public int getLightLevel() {
		cj localdt = new cj(getPlayerPosX(), getPlayerPosY(), getPlayerPosZ());
		if (!getWorld().e(localdt)) {
			return 0;
		}
		ase localObject = getMinecraft().f.f(localdt);
		return localObject.a(localdt, 0);
	}

	@Override
	public String getEntityCount() {
		return getMinecraft().g.h().split(" |,")[1];
	}

	@Override
	public boolean isRidingEntity() {
		return getSpectatingEntity().by() != null;
	}

	@Override
	public int getFoodLevel() {
		return isSpectatingSelf() ? ((zj) getSpectatingEntity()).cS().a() : getPlayer().cS().a();
	}

	@Override
	public float getSaturation() {
		return isSpectatingSelf() ? ((zj) getSpectatingEntity()).cS().e() : 0;
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof sa))
			return -1;
		return ((sa) entity).bQ();
	}

	@Override
	public float getPlayerHealth() {
		return getPlayer().bQ();
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(yt.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().bT();
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
		referenceDamage = ra.a(referenceDamage, (float) getPlayer().bT());

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
			if (potionEffect.getIconIndex() < 0 || !potionEffect.hasParticles())
				continue;
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
		return getPlayer().cb() == null ? null : new WrappedItemStack(getPlayer().cb());
	}

	@Override
	public ItemStack getItemInOffHand() {
		return getPlayer().cc() == null ? null : new WrappedItemStack(getPlayer().cc());
	}

	@Override
	public ItemStack getItemInArmorSlot(int slot) {
		return getArmorItemBySlot(slot) == null ? null : new WrappedItemStack(getArmorItemBySlot(slot));
	}

	public Collection<rl> getActivePlayerPotionEffects() {
		return getPlayer().bO();
	}

	public rk getPotionByEffect(rl potionEffect) {
		return potionEffect.a();
	}

	public adq getArmorItemBySlot(int slot) {
		return getPlayer().br.g(slot);
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
		for (adq itemStack : getPlayer().br.a) {
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
		return getPlayer().br.d;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().br.d = slot;
		getNetworkManager().a(new jb(slot));
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
		bcd.c();
		GLUtil.enableBlend();
		GLUtil.tryBlendFuncSeparate(770, 771, 1, 0);
		brz itemRenderer = getMinecraft().ad();
		itemRenderer.b(itemStack, x, y);
		itemRenderer.a(getFontrenderer(), itemStack, x, y);
		GLUtil.disableBlend();
		bcd.a();
	}

	@Override
	public void updateScaledResolution() {
		this.scaledResolution = new bcx(getMinecraft());
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
		return getGameSettings().aq;
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
		bcc[] hotbarBindings = getGameSettings().ak;
		for (int i = 0; i < Math.min(result.length, hotbarBindings.length); i++) {
			result[i] = getKeyDisplayStringShort(hotbarBindings[i].j());
		}
		return result;
	}

	@Override
	public String getKeyDisplayStringShort(int key) {
		return key < 0 ? "M" + (key + 101) : (key < 256 ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase());
	}

	private bkt getPlayerController() {
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

	public void messagePlayer(eu chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.d().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().d.a(new ij(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new iq(channel, new em(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private ek getNetworkManager() {
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
		return getTextureManager().a(name, new bux(image));
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof bux) {
			bni.i(((bux)resourceLocation).b());
		} else {
			getTextureManager().a((kk) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
		if (resourceLocation instanceof kk) {
			getTextureManager().c((kk) resourceLocation);
		}
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		bux dynamicImage = new bux(width, height);
		getTextureManager().a((ResourceLocation) resourceLocation, dynamicImage);
		return dynamicImage;
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((kk) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), ((bux) dynamicImage).e(), 0, image.getWidth());
		((bux) dynamicImage).d();
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public bvi getTextureManager() {
		return getMinecraft().N();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		bnu var4 = bnu.a();
		bmz var5 = var4.c();
		bindTexture(bcu.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		var5.a(7, bvp.i);
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
		bbi objectMouseOver = getSpectatingEntity().a(maxDistance, 1f);
		double var3 = maxDistance;
		bbj entityPosition = getSpectatingEntity().g(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.c.f(entityPosition);
		}

		bbj look = getSpectatingEntity().f(1f);
		bbj mostFarPoint = entityPosition.b(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance);
		rr pointedEntity = null;
		bbj hitVector = null;
		List<rr> entitiesWithinAABBExcludingEntity = getWorld().a(getSpectatingEntity(),
				getSpectatingEntity().bl().a(look.b * maxDistance, look.c * maxDistance, look.d * maxDistance).b(1.0, 1.0, 1.0), Predicates.and(rv.e, new Predicate<rr>() {
					@Override
					public boolean apply(rr entity) {
						return entity != null && entity.ap();
					}
				}));
		double distance = var3;

		for (rr entity : entitiesWithinAABBExcludingEntity) {
			bbh axisAlignedBB = entity.bl().g(entity.aA());
			bbi intercept = axisAlignedBB.a(entityPosition, mostFarPoint);
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
			objectMouseOver = new bbi(pointedEntity, hitVector);
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
		bbp scoreboard = getWorld().ad();
		if (scoreboard == null) {
			return null;
		}
		bbl objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d();
		Collection<bbn> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (bbn score : scores) {
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
