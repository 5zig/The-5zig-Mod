import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Names;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.ITextfield;
import eu.the5zig.mod.manager.SearchEntry;
import eu.the5zig.util.Callback;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ClassProxy {

	private static final Field byteBuf;
	private static Field buttonList;
	private static final Field serverList;
	private static final Field resourcePackList;
	private static final Field worldData;
	private static final Field worldDataList;

	private static final UUID SPRINT_MODIFIER_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

	private static boolean tryFix = false;

	private ClassProxy() {
	}

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Class<?> c = classLoader.loadClass(Names.packetBuffer.getName());
			byteBuf = Transformer.FORGE ? c.getDeclaredField("field_150794_a") : c.getDeclaredField("a");
			byteBuf.setAccessible(true);

			if (Transformer.FORGE) {
				buttonList = classLoader.loadClass(Names.guiScreen.getName()).getDeclaredField("field_146292_n");
				buttonList.setAccessible(true);

				serverList = clq.class.getDeclaredField("field_148198_l");
				worldData = cmr.class.getDeclaredField("field_186786_g");
				worldDataList = cms.class.getDeclaredField("field_186799_w");
				resourcePackList = cgv.class.getDeclaredField("v");
			} else {
				serverList = clq.class.getSuperclass().getDeclaredField("v");
				resourcePackList = cgv.class.getDeclaredField("v");
				worldData = cmr.class.getDeclaredField("i");
				worldDataList = cms.class.getSuperclass().getDeclaredField("v");
			}
			serverList.setAccessible(true);
			resourcePackList.setAccessible(true);
			worldData.setAccessible(true);
			worldDataList.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		/*if (Transformer.FORGE) {
			// Forge mod list
			try {
				Class<?> metadataClass = classLoader.loadClass("net.minecraftforge.fml.common.ModMetadata");
				Object metadata = metadataClass.newInstance();
				metadataClass.getField("modId").set(metadata, "the5zigmod");
				metadataClass.getField("name").set(metadata, "The 5zig Mod");
				metadataClass.getField("version").set(metadata, Version.VERSION);
				metadataClass.getField("description").set(metadata, "A mod that enhances your Minecraft PvP experience by displaying all kinds of useful stats.");
				metadataClass.getField("url").set(metadata, "https://5zig.eu");
				metadataClass.getField("authorList").set(metadata, Collections.singletonList("5zig"));
				Class<?> modContainerClass = classLoader.loadClass("net.minecraftforge.fml.common.DummyModContainer");
				Object modContainer = modContainerClass.getConstructor(metadataClass).newInstance(metadata);

				Class<?> loaderClass = classLoader.loadClass("net.minecraftforge.fml.common.Loader");
				Object loader = loaderClass.getMethod("instance").invoke(null);
				Field modListField = loaderClass.getDeclaredField("mods");
				modListField.setAccessible(true);
				List list = Lists.newArrayList((List) modListField.get(loader));
				list.add(modContainer);
				modListField.set(loader, ImmutableList.copyOf(list));
				Class<?> loadControllerClass = classLoader.loadClass("net.minecraftforge.fml.common.LoadController");
				Field modControllerField = loaderClass.getDeclaredField("modController");
				modControllerField.setAccessible(true);
				Object modController = modControllerField.get(loader);
				List activeModList = (List) loadControllerClass.getMethod("getActiveModList").invoke(modController);
				activeModList.add(modContainer);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}*/
	}

	public static void patchMainMenu(Object instance, int paramInt1, int paramInt2, boolean isForge) {
		cjx guiMainMenu = (cjx) instance;
		if (!MinecraftFactory.getClassProxyCallback().isShowLastServer())
			return;
		String lastServer;
		IButton button;
		String server = MinecraftFactory.getClassProxyCallback().getLastServer();
		int x = guiMainMenu.m / 2 + 2;
		int y = paramInt1 + paramInt2;
		if (server != null) {
			String[] parts = server.split(":");
			String host = parts[parts.length - 2];
			int port = Integer.parseInt(parts[parts.length - 1]);
			if (port == 25565) {
				lastServer = host;
			} else {
				lastServer = host + ":" + port;
			}
			lastServer = MinecraftFactory.getVars().shortenToWidth(lastServer, 88);
			button = new Button(99, x, y, 98, 20, lastServer) {
				@Override
				protected void onClick(double mouseX, double mouseY) {
					String server = MinecraftFactory.getClassProxyCallback().getLastServer();
					if (server == null)
						return;
					String[] parts = server.split(":");
					String host = parts[parts.length - 2];
					int port = Integer.parseInt(parts[parts.length - 1]);
					MinecraftFactory.getVars().joinServer(host, port);
				}
			};
		} else {
			button = MinecraftFactory.getVars().createButton(98, x, y, 98, 20, MinecraftFactory.getClassProxyCallback().translate("menu.no_last_server"));
		}
		guiMainMenu.a((cgj) button);
		List<cgj> list;
		if (Transformer.FORGE) {
			try {
				list = ((List<cgj>) buttonList.get(guiMainMenu));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiMainMenu.o;
		}

		for (cgj b : list) {
			int id = b.k;
			if (id == 2) {
				b.f = 98;
			}
		}
	}

	public static void guiMainActionPerformed(Object b) {
		cgj button = (cgj) b;
		if (button.k == 99) {
			String server = MinecraftFactory.getClassProxyCallback().getLastServer();
			if (server == null)
				return;
			String[] parts = server.split(":");
			String host = parts[parts.length - 2];
			int port = Integer.parseInt(parts[parts.length - 1]);
			MinecraftFactory.getVars().joinServer(host, port);
		}
	}

	public static IButton getThe5zigModButton(Object instance) {
		cjs guiScreen = (cjs) instance;
		return new Button(42, guiScreen.m / 2 - 155, guiScreen.n / 6 + 24 - 6, 150, 20, MinecraftFactory.getClassProxyCallback().translate("menu.the5zigMod")) {
			@Override
			protected void onClick(double mouseX, double mouseY) {
				MinecraftFactory.getClassProxyCallback().displayGuiSettings(new WrappedGui(guiScreen));
			}
		};
	}

	public static void guiOptionsActionPerformed(Object instance, Object b) {
		cjs guiScreen = (cjs) instance;
		cgj button = (cgj) b;
		if (button.k == 42) {
			MinecraftFactory.getClassProxyCallback().displayGuiSettings(new WrappedGui(guiScreen));
		}
	}

	public static IButton getMCPVPButton(cjs guiScreen) {
		return MinecraftFactory.getVars().createButton(9, guiScreen.m / 2 - 23, guiScreen.n - 28, 46, 20, MinecraftFactory.getClassProxyCallback().translate("menu.mcpvp"));
	}

	public static void guiMultiplayerActionPerformed(cjs guiScreen, cgj button) {
		if (button.k == 9) {
//			The5zigMod.getVars().displayScreen(new ServerListMCPvP(guiScreen));
		}
	}

	public static void setupPlayerTextures(GameProfile gameProfile) {
		((Variables) MinecraftFactory.getVars()).getResourceManager().loadPlayerTextures(gameProfile);
	}

	public static boolean onRenderItemPerson(Object instance, Object itemStackObject, Object entityPlayerObject, Object cameraTransformTypeObject, boolean leftHand) {
		return ((Variables) MinecraftFactory.getVars()).getResourceManager().renderInPersonMode(instance, itemStackObject, entityPlayerObject, cameraTransformTypeObject, leftHand);
	}

	public static boolean onRenderItemInventory(Object instance, Object itemStackObject, int x, int y) {
		return ((Variables) MinecraftFactory.getVars()).getResourceManager().renderInInventory(instance, itemStackObject, x, y);
	}

	public static ByteBuf packetBufferToByteBuf(Object packetBuffer) {
		try {
			return (ByteBuf) byteBuf.get(packetBuffer);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static void appendCategoryToCrashReport(Object crashReport) {
		((b) crashReport).g().a("The 5zig Mod Version", MinecraftFactory.getClassProxyCallback().getVersion());
		((b) crashReport).g().a("The 5zig Mod Plugins", MinecraftFactory.getClassProxyCallback().getModList());
		((b) crashReport).g().a("Forge", Transformer.FORGE);
		((b) crashReport).g().a("GUI", MinecraftFactory.getVars().getCurrentScreen());
	}

	public static void publishCrashReport(Throwable cause, File crashFile) {
		if (MinecraftFactory.getClassProxyCallback() != null) {
			MinecraftFactory.getClassProxyCallback().launchCrashHopper(cause, crashFile);
		}
	}

	public static void handleGuiDisconnectedDraw(Object instance) {
		cjs gui = (cjs) instance;
		MinecraftFactory.getClassProxyCallback().checkAutoreconnectCountdown(gui.m, gui.n);
	}

	public static void setServerData(Object serverData) {
		String host = ((cqy) serverData).b;
		if (!"5zig.eu".equalsIgnoreCase(host) && !"5zig.net".equalsIgnoreCase(host)) {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(serverData);
		} else {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(null);
		}
	}

	public static void fixOptionButtons(Object instance) {
		if (!tryFix)
			return;
		cjs guiScreen = (cjs) instance;
		tryFix = false;
		List<cgj> list;
		if (Transformer.FORGE) {
			try {
				list = ((List<cgj>) buttonList.get(guiScreen));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiScreen.o;
		}
		for (cgj button : list) {
			if (button.k != 42 && button.h == guiScreen.m / 2 - 155 && button.i == guiScreen.n / 6 + 24 - 6) {
				button.h = guiScreen.m / 2 + 5;
				button.f = 150;
			}
		}
	}

	public static void handleGuiResourcePackInit(Object instance, Object listObject, Object listObject2) {
		cme gui = (cme) instance;
		List<cmf> list;
		List<cmf> list2;
		try {
			list = (List<cmf>) ClassProxy.resourcePackList.get(listObject);
			list2 = (List<cmf>) ClassProxy.resourcePackList.get(listObject2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Comparator<cmf> comparator = new Comparator<cmf>() {
			@Override
			public int compare(cmf o1, cmf o2) {
				return o1.h().toLowerCase(Locale.ROOT).compareTo(o2.h().toLowerCase(Locale.ROOT));
			}
		};
		MinecraftFactory.getClassProxyCallback().addSearch(
				new SearchEntry<cmf>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, gui.m / 2 - 200, gui.n - 70, 170, 16),
						list, comparator) {
					@Override
					public boolean filter(String text, cmf o) {
						return o.h().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || o.h().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}
				},
				new SearchEntry<cmf>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9992, gui.m / 2 + 8, gui.n - 70, 170, 16),
						list2, new Comparator<cmf>() {
					@Override
					public int compare(cmf o1, cmf o2) {
						return 0;
					}
				}) {
					@Override
					public boolean filter(String text, cmf o) {
						return o.h().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || o.h().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}

					@Override
					protected int getAddIndex() {
						return 1;
					}
				});
	}

	public static void handleGuiMultiplayerInit(Object instance, Object serverSelectionListInstance) {
		final clm guiMultiplayer = (clm) instance;
		final clq serverSelectionList = (clq) serverSelectionListInstance;
		final List<clq.a> list;
		try {
			list = (List<clq.a>) ClassProxy.serverList.get(serverSelectionList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiMultiplayer.m - 305) / 2 + 6,
				guiMultiplayer.n - 84, 170, 16);
		final SearchEntry<clq.a> searchEntry = new SearchEntry<clq.a>(textfield, list) {
			@Override
			public void draw() {
				super.draw();
				if (serverSelectionList.g() >= list.size()) {
					serverSelectionList.c(-1);
				}
			}

			@Override
			public boolean filter(String text, clq.a entry) {
				if (!(entry instanceof clp)) { return false; }
				clp serverListEntry = (clp) entry;
				return serverListEntry.e().a.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || serverListEntry.e().b.toLowerCase(Locale.ROOT).contains(
						text.toLowerCase(Locale.ROOT));
			}
		};
		Callback<clq.a> enterCallback = new Callback<clq.a>() {
			@Override
			public void call(clq.a callback) {
				guiMultiplayer.b(0);
				guiMultiplayer.i();
				searchEntry.reset();
			}
		};
		searchEntry.setEnterCallback(enterCallback);
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void handleGuiSelectWorldInit(Object instance, List l) {
		final cmq guiSelectWorld = (cmq) instance;
		final cms guiList = (cms) l.get(0);
		List<cmr> list;
		try {
			list = (List<cmr>) worldDataList.get(guiList);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiSelectWorld.m - 220) / 2 + 6,
				guiSelectWorld.n - 84, 170, 16);
		final SearchEntry<cmr> searchEntry = new SearchEntry<cmr>(textfield, list) {
			@Override
			public boolean filter(String text, cmr saveFormatComparator) {
				try {
					cbu worldData = (cbu) ClassProxy.worldData.get(saveFormatComparator);
					return worldData.a().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || worldData.b().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		searchEntry.setEnterCallback(new Callback<cmr>() {
			@Override
			public void call(cmr callback) {
				guiList.c(0);
				cmr f = guiList.h();
				if (f != null)
					f.a();
			}
		});
		searchEntry.setComparator(new Comparator<cmr>() {
			@Override
			public int compare(cmr saveFormatComparator1, cmr saveFormatComparator2) {
				try {
					cbu worldData1 = (cbu) ClassProxy.worldData.get(saveFormatComparator1);
					cbu worldData2 = (cbu) ClassProxy.worldData.get(saveFormatComparator2);
					return Long.valueOf(worldData2.e()).compareTo(worldData1.e());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void renderSnow() {
		if (MinecraftFactory.getVars().getMinecraftScreen() instanceof cjx) {
			MinecraftFactory.getClassProxyCallback().renderSnow(((cjx) MinecraftFactory.getVars().getMinecraftScreen()).m, ((cjx) MinecraftFactory.getVars().getMinecraftScreen()).n);
		}
	}

	public static float getCustomFOVModifier(Object object) {
		csv playerInstance = (csv) object;

		float modifier = 1.0F;
		if (playerInstance.bV.b) {
			// is Flying
			modifier *= 1.1F;
		}
		afk movementSpeedAttribute = playerInstance.a(ang.d);
		if (movementSpeedAttribute.a(SPRINT_MODIFIER_UUID) != null) {
			modifier = (float) ((double) modifier * (movementSpeedAttribute.b() * 1.30000001192092896D / playerInstance.bV.b() + 1.0) / 2.0D);
		}
		if (playerInstance.bV.b() == 0.0F || Float.isNaN(modifier) || Float.isInfinite(modifier)) {
			modifier = 1.0F;
		}

		if (playerInstance.cT() && playerInstance.cW().b() == atb.g) {
			// is using bow
			int itemInUseDuration = playerInstance.cY();
			float itemInUseDurationSeconds = (float) itemInUseDuration / 20.0F;
			if (itemInUseDurationSeconds > 1.0F) {
				itemInUseDurationSeconds = 1.0F;
			} else {
				itemInUseDurationSeconds *= itemInUseDurationSeconds;
			}

			modifier *= 1.0F - itemInUseDurationSeconds * 0.15F;
		}

		return modifier;
	}

	public static String[] getSignText(Object signTile) {
		ij[] lines = ((bjt) signTile).a;
		String[] result = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			result[i] = lines[i].d();
		}
		return result;
	}

	public static void setSignText(Object signTile, String[] text) {
		for (int i = 0; i < text.length; i++) {
			ij[] components = ((bjt) signTile).a;
			if (!Objects.equal(components[i].d(), text[i])) {
				components[i] = new iq(text[i]);
			}
		}
	}

}
