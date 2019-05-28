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

				serverList = bjj.class.getDeclaredField("field_148198_l");
				worldData = bkb.class.getDeclaredField("field_186786_g");
				worldDataList = bkc.class.getDeclaredField("field_186799_w");
			} else {
				serverList = bjj.class.getDeclaredField("v");
				worldData = bkb.class.getDeclaredField("g");
				worldDataList = bkc.class.getDeclaredField("w");
			}
			serverList.setAccessible(true);
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
		bhv guiMainMenu = (bhv) instance;
		if (!MinecraftFactory.getClassProxyCallback().isShowLastServer())
			return;
		String lastServer;
		IButton button;
		String server = MinecraftFactory.getClassProxyCallback().getLastServer();
		int x = guiMainMenu.l / 2 + 2;
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
			button = MinecraftFactory.getVars().createButton(99, x, y, 98, 20, lastServer);
		} else {
			button = MinecraftFactory.getVars().createButton(98, x, y, 98, 20, MinecraftFactory.getClassProxyCallback().translate("menu.no_last_server"));
		}
		List<bfm> list;
		if (Transformer.FORGE) {
			try {
				list = ((List<bfm>) buttonList.get(guiMainMenu));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiMainMenu.n;
		}
		list.add((bfm) button);

		for (bfm b : list) {
			int id = b.k;
			if (id == 2) {
				b.f = 98;
			}
		}
	}

	public static void guiMainActionPerformed(Object b) {
		bfm button = (bfm) b;
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
		bho guiScreen = (bho) instance;
		return MinecraftFactory.getVars().createButton(42, guiScreen.l / 2 - 155, guiScreen.m / 6 + 24 - 6, 150, 20, MinecraftFactory.getClassProxyCallback().translate("menu.the5zigMod"));
	}

	public static void guiOptionsActionPerformed(Object instance, Object b) {
		bho guiScreen = (bho) instance;
		bfm button = (bfm) b;
		if (button.k == 42) {
			MinecraftFactory.getClassProxyCallback().displayGuiSettings(new WrappedGui(guiScreen));
		}
	}

	public static IButton getMCPVPButton(bho guiScreen) {
		return MinecraftFactory.getVars().createButton(9, guiScreen.l / 2 - 23, guiScreen.m - 28, 46, 20, MinecraftFactory.getClassProxyCallback().translate("menu.mcpvp"));
	}

	public static void guiMultiplayerActionPerformed(bho guiScreen, bfm button) {
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
		bho gui = (bho) instance;
		MinecraftFactory.getClassProxyCallback().checkAutoreconnectCountdown(gui.l, gui.m);
	}

	public static void setServerData(Object serverData) {
		String host = ((bnt) serverData).b;
		if (!"5zig.eu".equalsIgnoreCase(host) && !"5zig.net".equalsIgnoreCase(host)) {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(serverData);
		} else {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(null);
		}
	}

	public static void fixOptionButtons(Object instance) {
		if (!tryFix)
			return;
		bho guiScreen = (bho) instance;
		tryFix = false;
		List<bfm> list;
		if (Transformer.FORGE) {
			try {
				list = ((List<bfm>) buttonList.get(guiScreen));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiScreen.n;
		}
		for (bfm button : list) {
			if (button.k != 42 && button.h == guiScreen.l / 2 - 155 && button.i == guiScreen.m / 6 + 24 - 6) {
				button.h = guiScreen.l / 2 + 5;
				button.f = 150;
			}
		}
	}

	public static void handleGuiResourcePackInit(Object instance, Object listObject, Object listObject2) {
		List list = (List) listObject;
		List list2 = (List) listObject2;
		bjm gui = (bjm) instance;
		Comparator<bjn> comparator = new Comparator<bjn>() {
			@Override
			public int compare(bjn o1, bjn o2) {
				if (!(o1 instanceof bjp) || !(o2 instanceof bjp))
					return 0;
				bjp resourcePackListEntryFound1 = (bjp) o1;
				bjp resourcePackListEntryFound2 = (bjp) o2;
				return resourcePackListEntryFound1.k().d().toLowerCase(Locale.ROOT).compareTo(resourcePackListEntryFound2.k().d().toLowerCase(Locale.ROOT));
			}
		};
		MinecraftFactory.getClassProxyCallback().addSearch(
				new SearchEntry<bjn>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, gui.l / 2 - 200, gui.m - 70, 170, 16),
						list, comparator) {
					@Override
					public boolean filter(String text, bjn o) {
						if (!(o instanceof bjp))
							return true;
						bjp resourcePackListEntryFound = (bjp) o;
						return resourcePackListEntryFound.k().d().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || resourcePackListEntryFound.k().e().toLowerCase(
								Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}
				},
				new SearchEntry<bjn>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9992, gui.l / 2 + 8, gui.m - 70, 170, 16),
						list2, new Comparator<bjn>() {
					@Override
					public int compare(bjn o1, bjn o2) {
						return 0;
					}
				}) {
					@Override
					public boolean filter(String text, bjn o) {
						if (!(o instanceof bjp))
							return true;
						bjp resourcePackListEntryFound = (bjp) o;
						return resourcePackListEntryFound.k().d().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || resourcePackListEntryFound.k().e().toLowerCase(
								Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}

					@Override
					protected int getAddIndex() {
						return 1;
					}
				});
	}

	public static void handleGuiMultiplayerInit(Object instance, Object serverSelectionListInstance) {
		final bjf guiMultiplayer = (bjf) instance;
		final bjj serverSelectionList = (bjj) serverSelectionListInstance;
		final List<bji> list;
		try {
			list = (List<bji>) ClassProxy.serverList.get(serverSelectionList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiMultiplayer.l - 305) / 2 + 6,
				guiMultiplayer.m - 84, 170, 16);
		final SearchEntry<bji> searchEntry = new SearchEntry<bji>(textfield, list) {
			@Override
			public void draw() {
				super.draw();
				if (serverSelectionList.e() >= list.size()) {
					serverSelectionList.c(-1);
				}
			}

			@Override
			public boolean filter(String text, bji serverListEntry) {
				return serverListEntry.a().a.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || serverListEntry.a().b.toLowerCase(Locale.ROOT).contains(
						text.toLowerCase(Locale.ROOT));
			}
		};
		Callback<bji> enterCallback = new Callback<bji>() {
			@Override
			public void call(bji callback) {
				guiMultiplayer.b(0);
				guiMultiplayer.f();
				searchEntry.reset();
			}
		};
		searchEntry.setEnterCallback(enterCallback);
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void handleGuiSelectWorldInit(Object instance, List l) {
		final bka guiSelectWorld = (bka) instance;
		final bkc guiList = (bkc) l.get(0);
		List<bkb> list;
		try {
			list = (List<bkb>) worldDataList.get(guiList);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiSelectWorld.l - 220) / 2 + 6,
				guiSelectWorld.m - 84, 170, 16);
		final SearchEntry<bkb> searchEntry = new SearchEntry<bkb>(textfield, list) {
			@Override
			public boolean filter(String text, bkb saveFormatComparator) {
				try {
					bbz worldData = (bbz) ClassProxy.worldData.get(saveFormatComparator);
					return worldData.a().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || worldData.b().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		searchEntry.setEnterCallback(new Callback<bkb>() {
			@Override
			public void call(bkb callback) {
				guiList.d(0);
				bkb f = guiList.f();
				if (f != null)
					f.a();
			}
		});
		searchEntry.setComparator(new Comparator<bkb>() {
			@Override
			public int compare(bkb saveFormatComparator1, bkb saveFormatComparator2) {
				try {
					bbz worldData1 = (bbz) ClassProxy.worldData.get(saveFormatComparator1);
					bbz worldData2 = (bbz) ClassProxy.worldData.get(saveFormatComparator2);
					return Long.valueOf(worldData1.e()).compareTo(worldData2.e());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void renderSnow() {
		if (MinecraftFactory.getVars().getMinecraftScreen() instanceof bhv) {
			MinecraftFactory.getClassProxyCallback().renderSnow(((bhv) MinecraftFactory.getVars().getMinecraftScreen()).l, ((bhv) MinecraftFactory.getVars().getMinecraftScreen()).m);
		}
	}

	public static float getCustomFOVModifier(Object object) {
		bpp playerInstance = (bpp) object;

		float modifier = 1.0F;
		if (playerInstance.bK.b) {
			// is Flying
			modifier *= 1.1F;
		}
		tk movementSpeedAttribute = playerInstance.a(aad.d);
		if (movementSpeedAttribute.a(SPRINT_MODIFIER_UUID) != null) {
			modifier = (float) ((double) modifier * (movementSpeedAttribute.b() * 1.30000001192092896D / playerInstance.bK.b() + 1.0) / 2.0D);
		}
		if (playerInstance.bK.b() == 0.0F || Float.isNaN(modifier) || Float.isInfinite(modifier)) {
			modifier = 1.0F;
		}

		if (playerInstance.cy() && playerInstance.cB().c() == afl.g) {
			// is using bow
			int itemInUseDuration = playerInstance.cD();
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
		fb[] lines = ((asv) signTile).a;
		String[] result = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			result[i] = lines[i].d();
		}
		return result;
	}

	public static void setSignText(Object signTile, String[] text) {
		for (int i = 0; i < text.length; i++) {
			fb[] components = ((asv) signTile).a;
			if (!Objects.equal(components[i].d(), text[i])) {
				components[i] = new fh(text[i]);
			}
		}
	}

}
