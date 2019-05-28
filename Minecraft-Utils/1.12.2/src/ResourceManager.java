import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.ingame.resource.CapeResource;
import eu.the5zig.mod.gui.ingame.resource.IResourceManager;
import eu.the5zig.mod.gui.ingame.resource.ItemModelResource;
import eu.the5zig.mod.gui.ingame.resource.PlayerResource;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.*;

public class ResourceManager implements IResourceManager {

	private static final int PLAYER_RESOURCE_VERSION = 2;

	private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("5zig Texture Downloader #%d").setDaemon(true)
			.build());
	private static final String BASE_URL = "http://textures.5zig.net/";
	private static final Gson gson = new Gson();

	private final Object guiCameraTransform;
	private final FaceBakery faceBakery = new FaceBakery();

	private final GameProfile playerProfile;
	private PlayerResource ownPlayerResource;
	private final Cache<UUID, PlayerResource> playerResources = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
	private final Cache<Integer, String> moduleIds = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();

	public ResourceManager(GameProfile playerProfile) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
		guiCameraTransform = Thread.currentThread().getContextClassLoader().loadClass(bwc.class.getName() + (Transformer.FORGE ? "$TransformType" : "$b")).getDeclaredField(
				Transformer.FORGE ? "GUI" : "g").get(null);
		this.playerProfile = playerProfile;
	}

	public void loadPlayerTextures(final GameProfile gameProfile) {
		if (gameProfile == null || gameProfile.getId() == null) {
			return;
		}
		PlayerResource playerResource;
		if (playerProfile.getName().equals(gameProfile.getName())) {
			if (ownPlayerResource != null) {
				playerResource = ownPlayerResource;
			} else {
				playerResource = ownPlayerResource = loadPlayerResource(playerProfile);
			}
		} else {
			playerResource = playerResources.getIfPresent(gameProfile.getId());
			if (playerResource != null) {
				MinecraftFactory.getClassProxyCallback().getLogger().debug("Loaded player resource textures from cache for player " + gameProfile.getName());
			} else {
				playerResources.put(gameProfile.getId(), loadPlayerResource(gameProfile));
			}
		}
		if (playerResource == null) {
			return;
		}

		if (playerResource.getItemModelResources() != null) {
			for (ItemModelResource itemModelResource : playerResource.getItemModelResources()) {
				ResourceLocation resourceLocation = (ResourceLocation) itemModelResource.getResourceLocation();
				if (((Variables) MinecraftFactory.getVars()).getTextureManager().b(resourceLocation) == null) {
					((Variables) MinecraftFactory.getVars()).getTextureManager().a(resourceLocation, (SimpleTexture) itemModelResource.getSimpleTexture());
				}
			}
		}
	}

	private PlayerResource loadPlayerResource(final GameProfile gameProfile) {
		final MinecraftProfileTexture minecraftProfileTexture = new MinecraftProfileTexture(
				BASE_URL + "textures/" + PLAYER_RESOURCE_VERSION + "/" + Utils.getUUIDWithoutDashes(gameProfile.getId()), new HashMap<String, String>());
		final PlayerResource playerResource = new PlayerResource();

		final ResourceLocation capeLocation = new ResourceLocation("the5zigmod", "capes/" + gameProfile.getId());
		final SimpleTickingTexture capeTexture;
		cds texture = ((Variables) MinecraftFactory.getVars()).getTextureManager().b(capeLocation);
		if (texture instanceof SimpleTickingTexture) {
			capeTexture = (SimpleTickingTexture) texture;
			capeTexture.setBufferedImage(null);
		} else {
			capeTexture = new SimpleTickingTexture(capeLocation);
			((Variables) MinecraftFactory.getVars()).getTextureManager().a(capeLocation, capeTexture);
		}

		EXECUTOR_SERVICE.execute(new Runnable() {
			@Override
			public void run() {
				MinecraftFactory.getClassProxyCallback().getLogger().debug("Loading player resource textures from {} for player {}", minecraftProfileTexture.getUrl(), gameProfile.getName());

				HttpURLConnection connection = null;
				BufferedReader reader = null;
				try {
					connection = (HttpURLConnection) (new URL(minecraftProfileTexture.getUrl())).openConnection(MinecraftFactory.getVars().getProxy());
					connection.setDoInput(true);
					connection.setDoOutput(false);
					connection.connect();

					if (connection.getResponseCode() != 200) {
						return;
					}
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String line = reader.readLine();

					TextureData data = gson.fromJson(line, TextureData.class);
					if (data.animatedCape != null && !data.animatedCape.isEmpty()) {
						try {
							BufferedImage cape = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(data.animatedCape)));
							capeTexture.setBufferedImage(cape);

							playerResource.setCapeResource(new CapeResource(capeLocation, capeTexture));
						} catch (Exception e) {
							MinecraftFactory.getClassProxyCallback().getLogger().error("Could not parse cape for " + gameProfile.getId(), e);
						}
					} else if (data.cape != null && !data.cape.isEmpty()) {
						try {
							BufferedImage cape = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(data.cape)));
							capeTexture.setBufferedImage(cape);

							playerResource.setCapeResource(new CapeResource(capeLocation, capeTexture));
						} catch (Exception e) {
							MinecraftFactory.getClassProxyCallback().getLogger().error("Could not parse cape for " + gameProfile.getId(), e);
						}
					}
					if (data.models != null) {
						playerResource.setItemModelResources(Lists.<ItemModelResource>newArrayList());
						List<TextureData.Model> models = data.models;
						for (TextureData.Model model : models) {
							if (StringUtils.isEmpty(model.itemName) || StringUtils.isEmpty(model.texture)) {
								continue;
							}
							try {
								ain item = ail.g.c(new ResourceLocation(model.itemName));
								if (item != null) {
									final ResourceLocation modelLocation = new ResourceLocation(
											"item-models/" + gameProfile.getId() + "/" + model.itemName + (StringUtils.isNotEmpty(model.render) ? "/" + model.render.toLowerCase(
													Locale.ROOT) : "") + ".png");
									final SimpleTexture modelTexture = new SimpleTexture();
									if (((Variables) MinecraftFactory.getVars()).getTextureManager().b(modelLocation) == null) {
										((Variables) MinecraftFactory.getVars()).getTextureManager().a(modelLocation, modelTexture);
									}

									BufferedImage modelImage = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(model.texture)));
									modelTexture.setBufferedImage(modelImage);

									String modelData = model.modelId == null || model.modelId == 0 ? model.model : getModelIdData(model.modelId);
									ItemModelResource.Render render = null;
									if (StringUtils.isNotEmpty(model.render)) {
										try {
											render = ItemModelResource.Render.valueOf(model.render);
										} catch (IllegalArgumentException e) {
											MinecraftFactory.getClassProxyCallback().getLogger().warn("Could not parse render type for model with item " + model.itemName + "!", e);
										}
									}
									if (StringUtils.isNotEmpty(modelData)) {
										playerResource.getItemModelResources().add(
												new ItemModelResource(modelLocation, item, render, createModel(new String(Base64.decodeBase64(modelData), Charsets.UTF_8)), modelTexture));
									}
								}
							} catch (Exception e) {
								MinecraftFactory.getClassProxyCallback().getLogger().error("Could not parse item model for " + gameProfile.getId(), e);
							}
						}
					}
				} catch (Exception e) {
					MinecraftFactory.getClassProxyCallback().getLogger().error("Couldn\'t download http texture", e);
				} finally {
					IOUtils.closeQuietly(reader);
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		});

		return playerResource;
	}

	private String getModelIdData(final Integer modelId) {
		synchronized (moduleIds) {
			try {
				return moduleIds.get(modelId, new Callable<String>() {
					@Override
					public String call() throws Exception {
						HttpURLConnection connection = null;
						BufferedReader reader = null;
						try {
							connection = (HttpURLConnection) new URL(BASE_URL + "models/" + PLAYER_RESOURCE_VERSION + "/" + modelId).openConnection();
							if (connection.getResponseCode() != 200) {
								return null;
							}
							reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
							return reader.readLine();
						} finally {
							IOUtils.closeQuietly(reader);
							if (connection != null) {
								connection.disconnect();
							}
						}
					}
				});
			} catch (ExecutionException e) {
				MinecraftFactory.getClassProxyCallback().getLogger().warn("Could not load model data with id " + modelId, e);
				return null;
			}
		}
	}

	@Override
	public void updateOwnPlayerTextures() {
		ownPlayerResource = null;
		loadPlayerTextures(MinecraftFactory.getVars().getGameProfile());
	}

	@Override
	public Object getOwnCapeLocation() {
		return getCapeLocation(ownPlayerResource);
	}

	@Override
	public void cleanupTextures() {
		MinecraftFactory.getClassProxyCallback().getLogger().debug("Cleaning up player textures...");
		for (PlayerResource playerResource : playerResources.asMap().values()) {
			if (playerResource.getCapeResource() != null) {
				((SimpleTickingTexture) playerResource.getCapeResource().getSimpleTexture()).setBufferedImage(null);
			}
		}
		playerResources.invalidateAll();
		ownPlayerResource = null;
	}

	@Override
	public Object getCapeLocation(Object player) {
		GameProfile profile = ((bua) player).da();
		if (playerProfile.getName().equals(profile.getName())) {
			return getCapeLocation(ownPlayerResource);
		} else {
			return getCapeLocation(playerResources.getIfPresent(profile.getId()));
		}
	}

	private Object getCapeLocation(PlayerResource playerResource) {
		if (playerResource != null && playerResource.getCapeResource() != null) {
			CapeResource capeResource = playerResource.getCapeResource();
			return ((SimpleTickingTexture) capeResource.getSimpleTexture()).getCurrentResource();
		}
		return null;
	}

	private cfy createModel(String model) {
		bvu modelBlock = bvu.a(model);

		return bakeModel(modelBlock);
	}

	private cfy bakeModel(bvu modelBlock) {
		cgf.a bakedModelBuilder = (new cgf.a(modelBlock, modelBlock.g())).a(TextureAtlasSprite.MISSING_NO);

		for (bvq blockPart : modelBlock.a()) {
			for (fa face : blockPart.c.keySet()) {
				bvr blockPartFace = blockPart.c.get(face);
				if (blockPartFace.b == null) {
					bakedModelBuilder.a(faceBakery.a(blockPart.a, blockPart.b, blockPartFace, face, cfz.a, blockPart.d, false, blockPart.e));
				} else {
					bakedModelBuilder.a(cfz.a.a(blockPartFace.b), faceBakery.a(blockPart.a, blockPart.b, blockPartFace, face, cfz.a, blockPart.d, false, blockPart.e));
				}
			}
		}

		return bakedModelBuilder.b();
	}

	private boolean shouldRender(ItemModelResource itemModelResource, aed entityPlayer, aip itemStack) {
		if (itemStack.c() == itemModelResource.getItem()) {
			if (itemStack.c() == air.g && entityPlayer.ce() != null) {
				// bow
				int useCount = entityPlayer.cK() == 0 ? 0 : itemStack.m() - entityPlayer.cK();
				if (useCount >= 18) {
					if (itemModelResource.getRender() == ItemModelResource.Render.BOW_PULLING_2) {
						return true;
					}
				} else if (useCount > 13) {
					if (itemModelResource.getRender() == ItemModelResource.Render.BOW_PULLING_1) {
						return true;
					}
				} else if (useCount > 0) {
					if (itemModelResource.getRender() == ItemModelResource.Render.BOW_PULLING_0) {
						return true;
					}
				}
			} else if (itemStack.c() == air.aZ && entityPlayer.bU != null) {
				// fishing rod
				if (itemModelResource.getRender() == ItemModelResource.Render.FISHING_ROD_CAST) {
					return true;
				}
			} else {
				if (itemModelResource.getRender() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean renderInPersonMode(Object instance, Object itemStackObject, Object entityPlayerObject, Object cameraTransformTypeObject, boolean leftHand) {
		if (!MinecraftFactory.getClassProxyCallback().isRenderCustomModels()) {
			return false;
		}
		aip itemStack = (aip) itemStackObject;
		if (!(entityPlayerObject instanceof aed)) {
			return false;
		}
		aed entityPlayer = (aed) entityPlayerObject;
		bwc.b cameraTransformType = (bwc.b) cameraTransformTypeObject;
		GameProfile profile = entityPlayer.da();
		PlayerResource playerResource = playerProfile.getName().equals(profile.getName()) ? ownPlayerResource : playerResources.getIfPresent(profile.getId());
		if (playerResource == null || playerResource.getItemModelResources() == null) {
			return false;
		}
		for (ItemModelResource itemModelResource : playerResource.getItemModelResources()) {
			if (shouldRender(itemModelResource, entityPlayer, itemStack)) {
				render(instance, itemStack, itemModelResource, cameraTransformType, leftHand);
				return true;
			}
		}

		return false;
	}

	private void render(Object instance, aip itemStack, ItemModelResource itemModel, bwc.b transformType, boolean leftHand) {
		MinecraftFactory.getVars().bindTexture(itemModel.getResourceLocation());
		((Variables) MinecraftFactory.getVars()).getTextureManager().b((ResourceLocation) itemModel.getResourceLocation()).b(false, false);
		GLUtil.color(1, 1, 1, 1);
		bus.D();
		bus.a(516, 0.1F);
		bus.m();
		bus.a(bus.r.l, bus.l.j, bus.r.e, bus.l.n);
		bus.G();
		bwc itemCameraTransforms = ((cfy) itemModel.getBakedModel()).e();
		bwc.a(itemCameraTransforms.b(transformType), leftHand);
		if (a(itemCameraTransforms.b(transformType))) {
			bus.a(bus.i.a);
		}

		((bzw) instance).a(itemStack, (cfy) itemModel.getBakedModel());
		bus.a(bus.i.b);
		bus.H();
		bus.E();
		bus.l();
		MinecraftFactory.getVars().bindTexture(itemModel.getResourceLocation());
		((Variables) MinecraftFactory.getVars()).getTextureManager().b((ResourceLocation) itemModel.getResourceLocation()).a();
	}

	private boolean a(bwb var) {
		return var.d.x < 0.0F ^ var.d.y < 0.0F ^ var.d.z < 0.0F;
	}

	public boolean renderInInventory(Object instance, Object itemStackObject, int x, int y) {
		if (!MinecraftFactory.getClassProxyCallback().isRenderCustomModels()) {
			return false;
		}
		aip itemStack = (aip) itemStackObject;
		if (ownPlayerResource == null || ownPlayerResource.getItemModelResources() == null) {
			return false;
		}
		for (ItemModelResource itemModel : ownPlayerResource.getItemModelResources()) {
			bud player = ((Variables) MinecraftFactory.getVars()).getPlayer();
			if (shouldRender(itemModel, player, itemStack) && (itemModel.getRender() == null || player.bv.i() == itemStack)) {
				bus.G();
				MinecraftFactory.getVars().bindTexture(itemModel.getResourceLocation());
				((Variables) MinecraftFactory.getVars()).getTextureManager().b((ResourceLocation) itemModel.getResourceLocation()).b(false, false);
				bus.D();
				bus.e();
				bus.a(516, 0.1F);
				bus.m();
				bus.a(bus.r.l, bus.l.j);
				bus.c(1.0F, 1.0F, 1.0F, 1.0F);
				a((bzw) instance, x, y, ((cfy) itemModel.getBakedModel()).b());
				((cfy) itemModel.getBakedModel()).e().a((bwc.b) guiCameraTransform);
				((bzw) instance).a(itemStack, (cfy) itemModel.getBakedModel());
				bus.d();
				bus.E();
				bus.g();
				bus.H();
				MinecraftFactory.getVars().bindTexture(itemModel.getResourceLocation());
				((Variables) MinecraftFactory.getVars()).getTextureManager().b((ResourceLocation) itemModel.getResourceLocation()).a();

				return true;
			}
		}

		return false;
	}

	private void a(bzw instance, int var, int var1, boolean var2) {
		bus.c((float) var, (float) var1, 100.0F + instance.a);
		bus.c(8.0F, 8.0F, 0.0F);
		bus.b(1.0F, -1.0F, 1.0F);
		bus.b(16F, 16F, 16F);
		if (var2) {
			bus.f();
		} else {
			bus.g();
		}
	}
}
