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

				serverList = bhn.class.getDeclaredField("field_148198_l");
				worldData = bif.class.getDeclaredField("field_186786_g");
				worldDataList = big.class.getDeclaredField("field_186799_w");
			} else {
				serverList = bhn.class.getDeclaredField("v");
				worldData = bif.class.getDeclaredField("g");
				worldDataList = big.class.getDeclaredField("w");
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
		bga guiMainMenu = (bga) instance;
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
		List<bdr> list;
		if (Transformer.FORGE) {
			try {
				list = ((List<bdr>) buttonList.get(guiMainMenu));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiMainMenu.n;
		}
		list.add((bdr) button);

		for (bdr b : list) {
			int id = b.k;
			if (id == 2) {
				b.f = 98;
			}
		}
	}

	public static void guiMainActionPerformed(Object b) {
		bdr button = (bdr) b;
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
		bft guiScreen = (bft) instance;
		return MinecraftFactory.getVars().createButton(42, guiScreen.l / 2 - 155, guiScreen.m / 6 + 24 - 6, 150, 20, MinecraftFactory.getClassProxyCallback().translate("menu.the5zigMod"));
	}

	public static void guiOptionsActionPerformed(Object instance, Object b) {
		bft guiScreen = (bft) instance;
		bdr button = (bdr) b;
		if (button.k == 42) {
			MinecraftFactory.getClassProxyCallback().displayGuiSettings(new WrappedGui(guiScreen));
		}
	}

	public static IButton getMCPVPButton(bft guiScreen) {
		return MinecraftFactory.getVars().createButton(9, guiScreen.l / 2 - 23, guiScreen.m - 28, 46, 20, MinecraftFactory.getClassProxyCallback().translate("menu.mcpvp"));
	}

	public static void guiMultiplayerActionPerformed(bey guiScreen, bdr button) {
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
		bfh gui = (bfh) instance;
		MinecraftFactory.getClassProxyCallback().checkAutoreconnectCountdown(gui.l, gui.m);
	}

	public static void setServerData(Object serverData) {
		String host = ((blq) serverData).b;
		if (!"5zig.eu".equalsIgnoreCase(host) && !"5zig.net".equalsIgnoreCase(host)) {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(serverData);
		} else {
			MinecraftFactory.getClassProxyCallback().setAutoreconnectServerData(null);
		}
	}

	public static void fixOptionButtons(Object instance) {
		if (!tryFix)
			return;
		bft guiScreen = (bft) instance;
		tryFix = false;
		List<bdr> list;
		if (Transformer.FORGE) {
			try {
				list = ((List<bdr>) buttonList.get(guiScreen));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			list = guiScreen.n;
		}
		for (bdr button : list) {
			if (button.k != 42 && button.h == guiScreen.l / 2 - 155 && button.i == guiScreen.m / 6 + 24 - 6) {
				button.h = guiScreen.l / 2 + 5;
				button.f = 150;
			}
		}
	}

	public static void handleGuiResourcePackInit(Object instance, Object listObject, Object listObject2) {
		List list = (List) listObject;
		List list2 = (List) listObject2;
		bhq gui = (bhq) instance;
		Comparator<bhr> comparator = new Comparator<bhr>() {
			@Override
			public int compare(bhr o1, bhr o2) {
				if (!(o1 instanceof bht) || !(o2 instanceof bht))
					return 0;
				bht resourcePackListEntryFound1 = (bht) o1;
				bht resourcePackListEntryFound2 = (bht) o2;
				return resourcePackListEntryFound1.k().d().toLowerCase(Locale.ROOT).compareTo(resourcePackListEntryFound2.k().d().toLowerCase(Locale.ROOT));
			}
		};
		MinecraftFactory.getClassProxyCallback().addSearch(
				new SearchEntry<bhr>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, gui.l / 2 - 200, gui.m - 70, 170, 16),
						list, comparator) {
					@Override
					public boolean filter(String text, bhr o) {
						if (!(o instanceof bht))
							return true;
						bht resourcePackListEntryFound = (bht) o;
						return resourcePackListEntryFound.k().d().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || resourcePackListEntryFound.k().e().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}
				},
				new SearchEntry<bhr>(MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9992, gui.l / 2 + 8, gui.m - 70, 170, 16),
						list2, new Comparator<bhr>() {
					@Override
					public int compare(bhr o1, bhr o2) {
						return 0;
					}
				}) {
					@Override
					public boolean filter(String text, bhr o) {
						if (!(o instanceof bht))
							return true;
						bht resourcePackListEntryFound = (bht) o;
						return resourcePackListEntryFound.k().d().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || resourcePackListEntryFound.k().e().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
					}

					@Override
					protected int getAddIndex() {
						return 1;
					}
				});
	}

	public static void handleGuiMultiplayerInit(Object instance, Object serverSelectionListInstance) {
		final bhj guiMultiplayer = (bhj) instance;
		final bhn serverSelectionList = (bhn) serverSelectionListInstance;
		final List<bhm> list;
		try {
			list = (List<bhm>) ClassProxy.serverList.get(serverSelectionList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiMultiplayer.l - 305) / 2 + 6,
				guiMultiplayer.m - 84, 170, 16);
		final SearchEntry<bhm> searchEntry = new SearchEntry<bhm>(textfield, list) {

			@Override
			public void draw() {
				super.draw();
				if (serverSelectionList.e() >= list.size()) {
					serverSelectionList.c(-1);
				}
			}

			@Override
			public boolean filter(String text, bhm serverListEntry) {
				return serverListEntry.a().a.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || serverListEntry.a().b.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
			}
		};
		Callback<bhm> enterCallback = new Callback<bhm>() {
			@Override
			public void call(bhm callback) {
				guiMultiplayer.b(0);
				guiMultiplayer.f();
				searchEntry.reset();
			}
		};
		searchEntry.setEnterCallback(enterCallback);
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void handleGuiSelectWorldInit(Object instance, List l) {
		final bie guiSelectWorld = (bie) instance;
		final big guiList = (big) l.get(0);
		List<bif> list;
		try {
			list = (List<bif>) worldDataList.get(guiList);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		ITextfield textfield = MinecraftFactory.getVars().createTextfield(MinecraftFactory.getClassProxyCallback().translate("gui.search"), 9991, (guiSelectWorld.l - 220) / 2 + 6,
				guiSelectWorld.m - 84, 170, 16);
		final SearchEntry<bif> searchEntry = new SearchEntry<bif>(textfield, list) {
			@Override
			public boolean filter(String text, bif saveFormatComparator) {
				try {
					bae worldData = (bae) ClassProxy.worldData.get(saveFormatComparator);
					return worldData.a().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || worldData.b().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		searchEntry.setEnterCallback(new Callback<bif>() {
			@Override
			public void call(bif callback) {
				guiList.d(0);
				bif f = guiList.f();
				if (f != null)
					f.a();
			}
		});
		searchEntry.setComparator(new Comparator<bif>() {
			@Override
			public int compare(bif saveFormatComparator1, bif saveFormatComparator2) {
				try {
					bae worldData1 = (bae) ClassProxy.worldData.get(saveFormatComparator1);
					bae worldData2 = (bae) ClassProxy.worldData.get(saveFormatComparator2);
					return Long.valueOf(worldData1.e()).compareTo(worldData2.e());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		MinecraftFactory.getClassProxyCallback().addSearch(searchEntry);
	}

	public static void renderSnow() {
		if (MinecraftFactory.getVars().getMinecraftScreen() instanceof bga) {
			MinecraftFactory.getClassProxyCallback().renderSnow(((bga) MinecraftFactory.getVars().getMinecraftScreen()).l, ((bga) MinecraftFactory.getVars().getMinecraftScreen()).m);
		}
	}

	public static float getCustomFOVModifier(Object object) {
		bnk playerInstance = (bnk) object;

		float modifier = 1.0F;
		if (playerInstance.bL.b) {
			// is Flying
			modifier *= 1.1F;
		}
		ss movementSpeedAttribute = playerInstance.a(za.d);
		if (movementSpeedAttribute.a(SPRINT_MODIFIER_UUID) != null) {
			modifier = (float) ((double) modifier * (movementSpeedAttribute.b() * 1.30000001192092896D / playerInstance.bL.b() + 1.0) / 2.0D);
		}
		if (playerInstance.bL.b() == 0.0F || Float.isNaN(modifier) || Float.isInfinite(modifier)) {
			modifier = 1.0F;
		}

		if (playerInstance.cx() && playerInstance.cA() != null && playerInstance.cA().b() == aeb.f) {
			// is using bow
			int itemInUseDuration = playerInstance.cC();
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
		ey[] lines = ((arc) signTile).a;
		String[] result = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			result[i] = lines[i].d();
		}
		return result;
	}

	public static void setSignText(Object signTile, String[] text) {
		for (int i = 0; i < text.length; i++) {
			ey[] components = ((arc) signTile).a;
			if (!Objects.equal(components[i].d(), text[i])) {
				components[i] = new fe(text[i]);
			}
		}
	}

}
