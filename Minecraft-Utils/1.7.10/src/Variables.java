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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

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
				rightClickMouse = Names.minecraft.load().getDeclaredMethod("am");
				rightClickMouse.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private final bny itemRenderer;
	private bca scaledResolution;
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
		this.itemRenderer = new bny();
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
		return new WrappedTextfield((bcd) handle);
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
		return new WrappedGui((bdw) lastScreen);
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
		return getMinecraftScreen() instanceof bct;
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
			displayScreen(new bct());
		}
		bcd chatField = getChatField();
		chatField.a(chatField.b() + text);
	}

	private bcd getChatField() {
		bct chatGUI = (bct) getMinecraftScreen();
		bcd chatField;
		if (Transformer.FORGE) {
			try {
				chatField = (bcd) forgeChatField.get(chatGUI);
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
		return new fq(prefix).a((fj) originalChatComponent);
	}

	@Override
	public boolean isSignGUIOpened() {
		return getMinecraftScreen() instanceof bfx;
	}

	@Override
	public void registerKeybindings(List<IKeybinding> keybindings) {
		bal[] currentKeybindings = getGameSettings().as;
		bal[] customKeybindings = new bal[keybindings.size()];
		for (int i = 0; i < keybindings.size(); i++) {
			customKeybindings[i] = (bal) keybindings.get(i);
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
		getMinecraft().X().a(bso.a(new ResourceLocation(domain, sound), pitch));
	}

	@Override
	public int getFontHeight() {
		return getFontrenderer().a;
	}

	public bjn getServerData() {
		return getMinecraft().E();
	}

	@Override
	public void resetServer() {
		getMinecraft().a((bjn) null);
	}

	@Override
	public String getServer() {
		bjn serverData = getServerData();
		if (serverData == null)
			return null;
		return serverData.b;
	}

	@Override
	public List<NetworkPlayerInfo> getServerPlayers() {
		List<NetworkPlayerInfo> result = Lists.newArrayList();
		for (Object wrapped : getPlayer().a.b) {
			result.add(new WrappedNetworkPlayerInfo((bjl) wrapped));
		}
		return result;
	}

	@Override
	public boolean isPlayerListShown() {
		return (getMinecraft().u.ah.d()) && ((!getMinecraft().F()) || (getServerPlayers().size() > 1));
	}

	@Override
	public void setFOV(float fov) {
		getGameSettings().aF = fov;
	}

	@Override
	public float getFOV() {
		return getGameSettings().aF;
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
		return brp.a(location, values);
	}

	@Override
	public void displayScreen(Gui gui) {
		if (gui == null)
			displayScreen((bdw) null);
		else
			displayScreen(gui.getHandle());
	}

	@Override
	public void displayScreen(Object gui) {
		getMinecraft().a((bdw) gui);
	}

	@Override
	public void joinServer(String host, int port) {
		if (getWorld() != null) {
			getWorld().F();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bcx((bdw) getMinecraftScreen(), getMinecraft(), new bjn(host, host + ":" + port)));
	}

	@Override
	public void joinServer(Object parentScreen, Object serverData) {
		if (serverData == null) {
			return;
		}
		if (getWorld() != null) {
			getWorld().F();
		}

		MinecraftFactory.getClassProxyCallback().resetServer();
		displayScreen(new bcx((bdw) parentScreen, getMinecraft(), (bjn) serverData));
	}

	@Override
	public void disconnectFromWorld() {
		if (getWorld() != null) {
			getWorld().F();
		}
		getMinecraft().a((bjf) null);
		displayScreen(new bee());
	}

	@Override
	public IServerPinger getServerPinger() {
		return new ServerPinger();
	}

	@Override
	public long getSystemTime() {
		return bao.K();
	}

	public bao getMinecraft() {
		return bao.B();
	}

	public bbu getFontrenderer() {
		return getMinecraft().l;
	}

	public bbj getGameSettings() {
		return getMinecraft().u;
	}

	public bjk getPlayer() {
		return getMinecraft().h;
	}

	public bjf getWorld() {
		return getMinecraft().f;
	}

	public bbv getGuiIngame() {
		return getMinecraft().r;
	}

	@Override
	public boolean isSpectatingSelf() {
		return true;
	}

	@Override
	public PlayerGameMode getGameMode() {
		return getMinecraft().c.b() ? PlayerGameMode.SURVIVAL : PlayerGameMode.CREATIVE;
	}

	public zs getOpenContainer() {
		return getPlayer().bn;
	}

	@Override
	public String getOpenContainerTitle() {
		if (!(getOpenContainer() instanceof aad))
			return null;
		return ((aad) getOpenContainer()).e().b();
	}

	@Override
	public void closeContainer() {
		getPlayer().q();
	}

	@Override
	public String getSession() {
		return getMinecraft().M().d();
	}

	@Override
	public String getUsername() {
		return getMinecraft().M().c();
	}

	@Override
	public Proxy getProxy() {
		return getMinecraft().O();
	}

	@Override
	public GameProfile getGameProfile() {
		return getMinecraft().M().e();
	}

	@Override
	public String getFPS() {
		return getMinecraft().B.split(" fps")[0];
	}

	@Override
	public boolean isPlayerNull() {
		return getPlayer() == null;
	}

	@Override
	public boolean isTerrainLoading() {
		return getMinecraftScreen() instanceof bdu;
	}

	@Override
	public double getPlayerPosX() {
		return getPlayer().s;
	}

	@Override
	public double getPlayerPosY() {
		return getPlayer().C.b;
	}

	@Override
	public double getPlayerPosZ() {
		return getPlayer().u;
	}

	@Override
	public float getPlayerRotationYaw() {
		return getPlayer().y;
	}

	@Override
	public float getPlayerRotationPitch() {
		return getPlayer().z;
	}

	@Override
	public int getPlayerChunkX() {
		return qh.c(getPlayer().s) >> 4;
	}

	@Override
	public int getPlayerChunkY() {
		return qh.c(getPlayer().t) >> 4;
	}

	@Override
	public int getPlayerChunkZ() {
		return qh.c(getPlayer().u) >> 4;
	}

	@Override
	public int getPlayerChunkRelX() {
		return qh.c(getPlayer().s) & 15;
	}

	@Override
	public int getPlayerChunkRelY() {
		return qh.c(getPlayer().t) & 15;
	}

	@Override
	public int getPlayerChunkRelZ() {
		return qh.c(getPlayer().u) & 15;
	}

	@Override
	public boolean hasTargetBlock() {
		return getMinecraft().t != null && getMinecraft().t.a.ordinal() == 1 && getMinecraft().t.f != null;
	}

	@Override
	public int getTargetBlockX() {
		return getMinecraft().t.b;
	}

	@Override
	public int getTargetBlockY() {
		return getMinecraft().t.c;
	}

	@Override
	public int getTargetBlockZ() {
		return getMinecraft().t.d;
	}

	@Override
	public ResourceLocation getTargetBlockName() {
		int x = qh.c(getPlayer().s);
		int y = qh.c(getPlayer().t);
		int z = qh.c(getPlayer().u);
		String[] name = aji.c.c(getWorld().a(x, y, z)).split(":");
		return new ResourceLocation(name[0], name[1]);
	}

	@Override
	public boolean isFancyGraphicsEnabled() {
		return bao.w();
	}

	@Override
	public String getBiome() {
		int i20 = qh.c(getPlayer().s);
		int i21 = qh.c(getPlayer().t);
		int i22 = qh.c(getPlayer().u);
		if (!getWorld().d(i20, i21, i22)) {
			return null;
		}
		apx localObject = getMinecraft().f.d(i20, i22);
		return localObject.a(i20 & 0xF, i22 & 0xF, getMinecraft().f.v()).af;
	}

	@Override
	public int getLightLevel() {
		int i20 = qh.c(getPlayer().s);
		int i21 = qh.c(getPlayer().t);
		int i22 = qh.c(getPlayer().u);
		if (!getWorld().d(i20, i21, i22)) {
			return 0;
		}
		apx localObject = getMinecraft().f.d(i20, i22);
		return localObject.b(i20 & 15, i21, i22 & 15, 0);
	}

	@Override
	public String getEntityCount() {
		return getMinecraft().r().split(" |,")[1];
	}

	@Override
	public boolean isRidingEntity() {
		return getPlayer().m != null;
	}

	@Override
	public int getFoodLevel() {
		return getPlayer().bQ().a();
	}

	@Override
	public float getSaturation() {
		return getPlayer().bQ().e();
	}

	@Override
	public float getHealth(Object entity) {
		if (!(entity instanceof sv))
			return -1;
		return ((sv) entity).aS();
	}

	@Override
	public float getPlayerHealth() {
		return getPlayer().aS();
	}

	@Override
	public float getPlayerMaxHealth() {
		return (float) getPlayer().a(yj.a).e();
	}

	@Override
	public int getPlayerArmor() {
		return getPlayer().aV();
	}

	@Override
	public int getAir() {
		return getPlayer().ar();
	}

	@Override
	public boolean isPlayerInsideWater() {
		return getPlayer().a(awt.h);
	}

	@Override
	public float getResistanceFactor() {
		float referenceDamage = 100;
		int i1 = 25 - getPlayer().aV();
		float d1 = referenceDamage * (float) i1;
		referenceDamage = d1 / 25.0F;

		if (getPlayer().a(rv.m)) {
			int resistanceAmplifier = (getPlayer().b(rv.m).c() + 1) * 5;
			int i = 25 - resistanceAmplifier;
			float d = referenceDamage * (float) i;
			referenceDamage = d / 25.0F;
		}

		if (referenceDamage <= 0.0F) {
			return 100;
		} else {
			int enchantmentModifierDamage = afv.a(getPlayer().ak(), ro.j);
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
		for (rw potionEffect : getActivePlayerPotionEffects()) {
			rv potion = getPotionByEffect(potionEffect);
			if (potion != null && potion.f()) {
				return wrapPotionEffect(potionEffect);
			}
		}
		for (rw potionEffect : getActivePlayerPotionEffects()) {
			rv potion = getPotionByEffect(potionEffect);
			if (potion != null && !potion.f()) {
				return wrapPotionEffect(potionEffect);
			}
		}

		return null;
	}

	@Override
	public List<PotionEffect> getActivePotionEffects() {
		List<PotionEffect> result = new ArrayList<PotionEffect>(getActivePlayerPotionEffects().size());
		for (rw potionEffect : getActivePlayerPotionEffects()) {
			result.add(wrapPotionEffect(potionEffect));
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<? extends PotionEffect> getDummyPotionEffects() {
		return DUMMY_POTIONS;
	}

	private PotionEffectImpl wrapPotionEffect(rw potionEffect) {
		rv potion = getPotionByEffect(potionEffect);
		return new PotionEffectImpl(potion == null ? "" : potionEffect.f(), potionEffect.b(), rv.a(potionEffect), potionEffect.c() + 1, potion == null ? -1 : potion.e(),
				potion == null || !potion.f(), true, potion == null ? 0 : potion.j());
	}

	@Override
	public int getPotionEffectIndicatorHeight() {
		return 0;
	}

	@Override
	public boolean isHungerPotionActive() {
		return getPlayer().a(rv.s);
	}

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

	public Collection<rw> getActivePlayerPotionEffects() {
		return getPlayer().aQ();
	}

	public rv getPotionByEffect(rw potionEffect) {
		return rv.a[potionEffect.a()];
	}

	public add getPlayerItemInHand() {
		return getPlayer().be();
	}

	public add getArmorItemBySlot(int slot) {
		return getPlayer().bm.d(slot);
	}

	@Override
	public ItemStack getItemByName(String resourceName) {
		return new WrappedItemStack(new add((adb) adb.e.a(resourceName)));
	}

	@Override
	public ItemStack getItemByName(String resourceName, int amount) {
		return new WrappedItemStack(new add((adb) adb.e.a(resourceName), amount));
	}

	@Override
	public int getItemCount(String key) {
		int count = 0;
		for (add itemStack : getPlayer().bm.a) {
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
		return getPlayer().bm.c;
	}

	@Override
	public void setSelectedHotbarSlot(int slot) {
		getPlayer().bm.c = slot;
		getNetworkManager().a(new jl(slot));
	}

	@Override
	public void onRightClickMouse() {
		try {
			rightClickMouse.invoke(getMinecraft());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void renderItem(add itemStack, int x, int y) {
		bam.c();
		GL11.glEnable(GL11.GL_BLEND);
		GL14.glBlendFuncSeparate(770, 771, 1, 0);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.a(getFontrenderer(), getTextureManager(), itemStack, x, y);
		itemRenderer.c(getFontrenderer(), getTextureManager(), itemStack, x, y);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		bam.a();
	}

	@Override
	public void updateScaledResolution() {
		this.scaledResolution = new bca(getMinecraft(), getWidth(), getHeight());
	}

	@Override
	public int getScaledWidth() {
		return scaledResolution.a();
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
		return false;
	}

	@Override
	public boolean shouldDrawHUD() {
		return getPlayerController().b();
	}

	private bje getPlayerController() {
		return getMinecraft().c;
	}

	@Override
	public String[] getHotbarKeys() {
		String[] result = new String[9];
		bal[] hotbarBindings = getGameSettings().ar;
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
		if (getMinecraft().n == null || !(getMinecraft().n instanceof GuiHandle))
			return null;
		return ((GuiHandle) getMinecraft().n).getChild();
	}

	@Override
	public Object getMinecraftScreen() {
		return getMinecraft().n;
	}

	@Override
	public void messagePlayer(String message) {
		messagePlayer(ChatComponentBuilder.fromLegacyText(message));
	}

	public void messagePlayer(fq chatComponent) {
		boolean cancel = MinecraftFactory.getClassProxyCallback().shouldCancelChatMessage(chatComponent.d().replace(ChatColor.RESET.toString(), ""), chatComponent);
		if (!cancel) {
			getPlayer().a(chatComponent);
		}
	}

	@Override
	public void sendMessage(String message) {
		getPlayer().a.a(new ir(message));
	}

	@Override
	public boolean hasNetworkManager() {
		return getNetworkManager() != null;
	}

	@Override
	public void sendCustomPayload(String channel, ByteBuf payload) {
		if (getNetworkManager() != null) {
			getNetworkManager().a(new iz(channel, new et(payload)));
		}
	}

	@Override
	public boolean isLocalWorld() {
		return hasNetworkManager() && getNetworkManager().c();
	}

	private ej getNetworkManager() {
		return getMinecraft().v() != null ? getMinecraft().v().b() : null;
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
		return getTextureManager().a(name, new bpq(image));
	}

	@Override
	public void bindTexture(Object resourceLocation) {
		if (resourceLocation instanceof bpq) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, (((bpq)resourceLocation).b()));
		} else {
			getTextureManager().a((bqx) resourceLocation);
		}
	}

	@Override
	public void deleteTexture(Object resourceLocation) {
		if (resourceLocation instanceof bqx) {
			getTextureManager().c((bqx) resourceLocation);
		}
	}

	@Override
	public Object createDynamicImage(Object resourceLocation, int width, int height) {
		bpq dynamicImage = new bpq(width, height);
		getTextureManager().a((ResourceLocation) resourceLocation, dynamicImage);
		return dynamicImage;
	}

	@Override
	public Object getTexture(Object resourceLocation) {
		return getTextureManager().b((bqx) resourceLocation);
	}

	@Override
	public void fillDynamicImage(Object dynamicImage, BufferedImage image) {
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), ((bpq) dynamicImage).d(), 0, image.getWidth());
		((bpq) dynamicImage).a();
	}

	@Override
	public void renderPotionIcon(int index) {
		getGuiIngame().b(0, 0, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
	}

	public bqf getTextureManager() {
		return getMinecraft().P();
	}

	@Override
	public void renderTextureOverlay(int x1, int x2, int y1, int y2) {
		bmh tessellator = bmh.a;
		MinecraftFactory.getVars().bindTexture(bbw.b);
		GLUtil.color(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32.0F;
		tessellator.b();
		tessellator.a(4210752, y2);
		tessellator.a(x1, y2, 0.0D, 0.0D, y2 / f1);
		tessellator.a((x1 + x2), y2, 0.0D, x2 / f1, y2 / f1);
		tessellator.a(4210752, x2);
		tessellator.a((x1 + x2), y1, 0.0D, x2 / f1, y1 / f1);
		tessellator.a(x1, y1, 0.0D, 0.0D, y1 / f1);
		tessellator.a();
	}

	@Override
	public void setIngameFocus() {
		getMinecraft().n();
	}

	@Override
	public MouseOverObject calculateMouseOverDistance(double maxDistance) {
		if (getMinecraft().i == null || getWorld() == null)
			return null;
		azu objectMouseOver = getMinecraft().i.a(maxDistance, 1f);
		double var3 = maxDistance;
		azw entityPosition = getMinecraft().i.l(1f);

		if (objectMouseOver != null) {
			var3 = objectMouseOver.f.d(entityPosition);
		}

		azw look = getMinecraft().i.j(1f);
		azw mostFarPoint = entityPosition.c(look.a * maxDistance, look.b * maxDistance, look.c * maxDistance);
		sa pointedEntity = null;
		azw hitVector = null;
		List<sa> entitiesWithinAABBExcludingEntity = getWorld().b(getMinecraft().i, getMinecraft().i.C.a(look.a * maxDistance, look.b * maxDistance, look.c * maxDistance).b(1.0, 1.0, 1.0));
		double distance = var3;

		for (sa entity : entitiesWithinAABBExcludingEntity) {
			if (entity.R()) {
				float collisionBorderSize = entity.af();
				azt axisAlignedBB = entity.C.b((double) collisionBorderSize, (double) collisionBorderSize, (double) collisionBorderSize);
				azu intercept = axisAlignedBB.a(entityPosition, mostFarPoint);
				if (axisAlignedBB.a(entityPosition)) {
					if (distance >= 0.0D) {
						pointedEntity = entity;
						hitVector = intercept == null ? entityPosition : intercept.f;
						distance = 0.0D;
					}
				} else if (intercept != null) {
					double distanceToHitVec = entityPosition.d(intercept.f);
					if (distanceToHitVec < distance || distance == 0.0D) {
						if (entity == getMinecraft().i.m) {
							if (distance == 0.0D) {
								pointedEntity = entity;
								hitVector = intercept.f;
							}
						} else {
							pointedEntity = entity;
							hitVector = intercept.f;
							distance = distanceToHitVec;
						}
					}
				}
			}
		}

		if (pointedEntity != null && (distance < var3 || objectMouseOver == null)) {
			objectMouseOver = new azu(pointedEntity, hitVector);
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
		bac scoreboard = getWorld().W();
		if (scoreboard == null) {
			return null;
		}
		azx objective = scoreboard.a(1);
		if (objective == null) {
			return null;
		}
		String displayName = objective.d();
		Collection<azz> scores = scoreboard.i(objective);
		HashMap<String, Integer> lines = Maps.newHashMap();
		for (azz score : scores) {
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
			Object minecraftInstance = minecraft.getMethod("B").invoke(null);
			Field aB = minecraft.getDeclaredField("aq");
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
		return getMinecraft().ab();
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
		getMinecraft().g();
	}

}
