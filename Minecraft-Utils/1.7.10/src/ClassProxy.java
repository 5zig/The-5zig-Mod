import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Names;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.ITextfield;
import eu.the5zig.mod.manager.SearchEntry;
import eu.the5zig.mod.util.BytecodeAccess;
import eu.the5zig.util.Callback;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ClassProxy {

	private static final Field byteBuf;
	private static Field buttonList;
	private static Field serverList;

	private static final UUID SPRINT_MODIFIER_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

	private static boolean tryFix = false;

	private ClassProxy() {
	}

	static {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Class c = classLoader.loadClass(Names.packetBuffer.getName());
			byteBuf = Transformer.FORGE ? c.getDeclaredField("field_150794_a") : c.getDeclaredField("a");
			byteBuf.setAccessible(true);
			if (Transformer.FORGE) {
				buttonList = classLoader.loadClass(Names.guiScreen.getName()).getDeclaredField("field_146292_n");
				buttonList.setAccessible(true);
			}
			Class<?> aClass = classLoader.loadClass(bge.class.getName());
			if (Transformer.FORGE) {
				serverList = aClass.getDeclaredField("field_148198_l");
			} else {
				serverList = aClass.getDeclaredField("l");
			}
			serverList.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		/*if (Transformer.FORGE) {
			// Forge mod list
			try {
				Class<?> metadataClass = classLoader.loadClass("cpw.mods.fml.common.ModMetadata");
				Object metadata = metadataClass.newInstance();
				metadataClass.getField("modId").set(metadata, "the5zigmod");
				metadataClass.getField("name").set(metadata, "The 5zig Mod");
				metadataClass.getField("version").set(metadata, Version.VERSION);
				metadataClass.getField("description").set(metadata, "A mod that enhances your Minecraft PvP experience by displaying all kinds of useful stats.");
				metadataClass.getField("url").set(metadata, "https://5zig.eu");
				metadataClass.getField("authorList").set(metadata, Collections.singletonList("5zig"));
				Class<?> modContainerClass = classLoader.loadClass("cpw.mods.fml.common.DummyModContainer");
				Object modContainer = modContainerClass.getConstructor(metadataClass).newInstance(metadata);

				Class<?> loaderClass = classLoader.loadClass("cpw.mods.fml.common.Loader");
				Object loader = loaderClass.getMethod("instance").invoke(null);
				Field modListField = loaderClass.getDeclaredField("mods");
				modListField.setAccessible(true);
				List list = Lists.newArrayList((List) modListField.get(loader));
				list.add(modContainer);
				modListField.set(loader, ImmutableList.copyOf(list));

				Class<?> loadControllerClass = classLoader.loadClass("cpw.mods.fml.common.LoadController");
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
		bee guiMainMenu = (bee) instance;
		if (!MinecraftFactory.getClassProxyCallback().isShowLastServer())
			return;
		String lastServer;
		String server = MinecraftFactory.getClassProxyCallback().getLastServer();//The5zigMod.getConfigManager().getLastServerConfig().getLastServer();
		IButton button = MinecraftFactory.getVars().createButton(98, guiMainMenu.l / 2 - 100, paramInt1 + paramInt2 * 2, isForge ? 98 : 200, 20,
				MinecraftFactory.getClassProxyCallback().translate("menu.no_last_server"));
		if (server != null) {
			String[] parts = server.split(":");
			String host = parts[parts.length - 2];
			int port = Integer.parseInt(parts[parts.length - 1]);
			if (port == 25565) {
				lastServer = host;
			} else {
				lastServer = host + ":" + port;
			}
			lastServer = MinecraftFactory.getVars().shortenToWidth(lastServer, isForge ? 88 : 190);
			button = MinecraftFactory.getVars().createButton(99, guiMainMenu.l / 2 - 100, paramInt1 + paramInt2 * 2, isForge ? 98 : 200, 20, lastServer);
		}
		List list;
		if (Transformer.FORGE) {
			try {
				list = (List) buttonList.get(guiMainMenu);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiMainMenu.n;
		}
		list.add(button);

		for (Object object : list) {
			bcb b = (bcb) object;
			int id = b.k;
			if (id == 2) {
				b.f = 98;
			}
			if (id == 14) {
				b.f = 98;
				b.h = guiMainMenu.l / 2 + 2;
				b.i = guiMainMenu.m / 4 + 48 + 24;
			}
		}
	}

	public static void guiMainActionPerformed(Object b) {
		bcb button = (bcb) b;
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
		tryFix = true;
		bdm guiScreen = (bdm) instance;
		return MinecraftFactory.getVars().createButton(42, guiScreen.l / 2 - 155, guiScreen.m / 6 + 48 - 6, 150, 20, MinecraftFactory.getClassProxyCallback().translate("menu.the5zigMod"));
	}

	public static void fixOptionButtons(Object instance) {
		if (!tryFix)
			return;
		bdm guiScreen = (bdm) instance;
		tryFix = false;
		List buttonList;
		if (Transformer.FORGE) {
			try {
				buttonList = ((List) ClassProxy.buttonList.get(guiScreen));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			buttonList = guiScreen.n;
		}
		for (Object o : buttonList) {
			bcb button = (bcb) o;
			if (button.k != 42 && button.h == guiScreen.l / 2 - 155 && button.i == guiScreen.m / 6 + 48 - 6) {
				button.h = guiScreen.l / 2 + 5;
				button.i = guiScreen.m / 6 + 24 - 6;
				button.f = 150;
			}
		}
	}

	public static void guiOptionsActionPerformed(Object instance, Object b) {
		bdm guiScreen = (bdm) instance;
		bcb button = (bcb) b;
		if (button.k == 42) {
			MinecraftFactory.getClassProxyCallback().displayGuiSettings(new WrappedGui(guiScreen));
		}
	}

	public static IButton getMCPVPButton(bdw guiScreen) {
		return MinecraftFactory.getVars().createButton(9, guiScreen.l / 2 - 23, guiScreen.m - 28, 46, 20, MinecraftFactory.getClassProxyCallback().translate("menu.mcpvp"));
	}

	public static void guiMultiplayerActionPerformed(bdw guiScreen, bcb button) {
		if (button.k == 9) {
//			The5zigMod.getVars().displayScreen(new ServerListMCPvP(guiScreen));
		}
	}

	public static void onLoadTextures(Map map) {
	}

	public static void setupPlayerTextures(GameProfile gameProfile) {
		((Variables) MinecraftFactory.getVars()).getResourceManager().loadPlayerTextures(gameProfile);
	}

	public static boolean onRenderItemPerson(Object instance, Object itemStackObject, Object entityPlayerObject, Object cameraTransformTypeObject, boolean leftHand) {
		return false;
	}

	public static boolean onRenderItemInventory(Object instance, Object itemStackObject, int x, int y) {
		return false;
	}

	public static ByteBuf packetBufferToByteBuf(Object packetBuffer) {
		try {
			return (ByteBuf) byteBuf.get(packetBuffer);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@BytecodeAccess
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
		bdw gui = (bdw) instance;
		MinecraftFactory.getClassProxyCallback().checkAutoreconnectCountdown(gui.l, gui.m);
	}

	public static void setServerData(Object serverData) {
		String host = ((bjn) serverData).b;
		if (!"5zig.eu".equalsIgnoreCase(host) && !"5zig.net".equalsIgnoreCase(host)) {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(serverData);
		} else {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(null);
		}
	}

	public static void handlePlayerInfo(Object action, int ping, GameProfile gameProfile) {
	}

	public static void handleGuiResourcePackInit(Object instance, Object listObject, Object listObject2) {
		List list = (List) listObject;
		List list2 = (List) listObject2;
		bgf gui = (bgf) instance;
		Comparator<bgg> comparator = new Comparator<bgg>() {
			@Override
			public int compare(bgg o1, bgg o2) {
				if (!(o1 instanceof bgi) || !(o2 instanceof bgi))
					return 0;
				bgi resourcePackListEntryFound1 = (bgi) o1;
				bgi resourcePackListEntryFound2 = (bgi) o2;
				return resourcePackListEntryFound1.i().d().toLowerCase(Locale.ROOT).compareTo(resourcePackListEntryFound2.i().d().toLowerCase(Locale.ROOT));
			}
		};
		MinecraftFactory.getClassProxyCallback().addSearch(
				new SearchEntry<bgg>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, gui.l / 2 - 200, gui.m - 70, 170, 16),
						list, comparator) {
					@Override
					public boolean filter(String text, bgg o) {
						if (!(o instanceof bgi))
							return true;
						bgi resourcePackListEntryFound = (bgi) o;
						boolean filter = resourcePackListEntryFound.i().d().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
						try {
							filter |= resourcePackListEntryFound.i().e().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
						} catch (NullPointerException ignored) {
							// thanks, Minecraft...
						}
						return filter;
					}
				},
				new SearchEntry<bgg>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9992, gui.l / 2 + 8, gui.m - 70, 170, 16),
						list2, new Comparator<bgg>() {
					@Override
					public int compare(bgg o1, bgg o2) {
						return 0;
					}
				}) {
					@Override
					public boolean filter(String text, bgg o) {
						if (!(o instanceof bgi))
							return true;
						bgi resourcePackListEntryFound = (bgi) o;
						return resourcePackListEntryFound.i().d().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || resourcePackListEntryFound.i().e().toLowerCase(
								Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}

					@Override
					protected int getAddIndex() {
						return 1;
					}
				});
	}

	public static void handleGuiMultiplayerInit(Object instance, Object serverSelectionListInstance) {
		final bfz guiMultiplayer = (bfz) instance;
		final bge serverSelectionList = (bge) serverSelectionListInstance;
		final List<bgc> list;
		try {
			list = (List<bgc>) ClassProxy.serverList.get(serverSelectionList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiMultiplayer.l - 305) / 2 + 6,
				guiMultiplayer.m - 84, 170, 16);
		final SearchEntry<bgc> searchEntry = new SearchEntry<bgc>(textfield, list) {

			@Override
			public void draw() {
				super.draw();
				if (serverSelectionList.k() >= list.size()) {
					serverSelectionList.c(-1);
				}
			}

			@Override
			public boolean filter(String text, bgc serverListEntry) {
				return serverListEntry.a().a.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || serverListEntry.a().b.toLowerCase(Locale.ROOT).contains(
						text.toLowerCase(Locale.ROOT));
			}
		};
		Callback<bgc> enterCallback = new Callback<bgc>() {
			@Override
			public void call(bgc callback) {
				guiMultiplayer.b(0);
				guiMultiplayer.f();
				searchEntry.reset();
			}
		};
		searchEntry.setEnterCallback(enterCallback);
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void handleGuiSelectWorldInit(Object instance, List list) {
		final bdx guiSelectWorld = (bdx) instance;
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiSelectWorld.l - 220) / 2 + 6,
				guiSelectWorld.m - 84, 170, 16);
		final SearchEntry<azf> searchEntry = new SearchEntry<azf>(textfield, list) {
			@Override
			public boolean filter(String text, azf saveFormatComparator) {
				return saveFormatComparator.a().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || saveFormatComparator.b().contains(text.toLowerCase(Locale.ROOT));
			}
		};
		searchEntry.setEnterCallback(new Callback<azf>() {
			@Override
			public void call(azf callback) {
				guiSelectWorld.e(0);
			}
		});
		searchEntry.setComparator(new Comparator<azf>() {
			@Override
			public int compare(azf saveFormatComparator1, azf saveFormatComparator2) {
				return Long.valueOf(saveFormatComparator1.e()).compareTo(saveFormatComparator2.e());
			}
		});
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void renderSnow() {
		if (MinecraftFactory.getVars().getMinecraftScreen() instanceof bee) {
			MinecraftFactory.getClassProxyCallback().renderSnow(((bee) MinecraftFactory.getVars().getMinecraftScreen()).l, ((bee) MinecraftFactory.getVars().getMinecraftScreen()).m);
		}
	}

	public static float getCustomFOVModifier(Object object) {
		blg playerInstance = (blg) object;

		float modifier = 1.0F;
		if (playerInstance.bE.b) {
			// is Flying
			modifier *= 1.1F;
		}
		ti movementSpeedAttribute = playerInstance.a(yj.d);
		if (movementSpeedAttribute.a(SPRINT_MODIFIER_UUID) != null) {
			modifier = (float) ((double) modifier * (movementSpeedAttribute.b() * 1.30000001192092896D / playerInstance.bE.b() + 1.0) / 2.0D);
		}
		if (playerInstance.bE.b() == 0.0F || Float.isNaN(modifier) || Float.isInfinite(modifier)) {
			modifier = 1.0F;
		}

		if (playerInstance.by() && playerInstance.bw().b() == ade.f) {
			// is using bow
			int itemInUseDuration = playerInstance.bz();
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
		return ((apm) signTile).a;
	}

	public static void setSignText(Object signTile, String[] text) {
		((apm) signTile).a = text;
	}

}
