package eu.the5zig.mod.asm;

public class Names {

	public static Name _static = new Name("<clinit>", "()V");
	public static Name minecraft = new Name("bes");
	public static Name startGame = new Name("an", "()V");
	public static Name resourcePacks = new Name("aB");
	public static Name shutdown = new Name("h", "()V");
	public static Name dispatchKeypresses = new Name("W", "()V");
	public static Name crashReport = new Name("b");
	public static Name displayCrashReport = new Name("c", "(L" + crashReport.getName() + ";)V");
	public static Name tick = new Name("t", "()V");
	public static Name leftClickMouse = new Name("aw", "()V");
	public static Name rightClickMouse = new Name("ax", "()V");

	public static Name guiMainMenu = new Name("bhv");
	public static Name guiButton = new Name("bfm", "(IIILjava/lang/String;)V");
	public static Name guiButtonExtended = new Name(guiButton.getName(), "(IIIIILjava/lang/String;)V");
	public static Name fontRenderer = new Name("bfg");
	public static Name guiTextfield = new Name("bfq", "(IL" + fontRenderer.getName() + ";IIII)V");
	public static Name setWorldAndResolution = new Name("a", "(L" + minecraft.getName() + ";II)V");
	public static Name insertSingleMultiplayerButton = new Name("b", "(II)V");
	public static Name actionPerformed = new Name("a", "(L" + guiButton.getName() + ";)V");
	public static Name initGui = new Name("b", "()V");
	public static Name buttonList = new Name("n", "Ljava/util/List;");
	public static Name width = new Name("l", "I");

	public static Name chatComponent = new Name("fb");
	public static Name packetBuffer = new Name("et");

	public static Name guiIngame = new Name("bfh");
	public static Name guiIngameForge = new Name("net.minecraftforge.client.GuiIngameForge");
	public static Name renderGameOverlay = new Name("a", "(F)V");
	public static Name scaledResolution = new Name("bfk");
	public static Name renderPotionEffectIndicator = new Name("a", "(L" + scaledResolution.getName() + ";)V");
	public static Name renderFood = new Name("d", "(L" + scaledResolution.getName() + ";)V");
	public static Name renderHotbar = new Name("a", "(L" + scaledResolution.getName() + ";F)V");
	public static Name renderGameOverlayForge = new Name("renderHUDText", "(II)V");
	public static Name ingameTick = new Name("c", "()V");
	public static Name renderVignette = new Name("b", "(FL" + scaledResolution.getName() + ";)V");
	public static Name renderChatForge = new Name("renderChat", "(II)V");

	public static Name guiIngameMenu = new Name("bhk");

	public static Name glStateManager = new Name("bqg");
	public static Name glColor = new Name("c", "(FFFF)V");

	public static Name guiAchievement = new Name("bhy");
	public static Name updateAchievementWindow = new Name("a", "()V");

	public static Name guiChat = new Name("bgr");
	public static Name guiChatNew = new Name("bfn");
	public static Name handleMouseInput = new Name("k", "()V");
	public static Name guiClosed = new Name("m", "()V");
	public static Name keyTyped = new Name("a", "(CI)V");
	public static Name mouseClicked = new Name("a", "(III)V");
	public static Name drawScreen = new Name("a", "(IIF)V");
	public static Name setChatLine = new Name("a", "(L" + chatComponent.getName() + ";IIZ)V");
	public static Name guiChatLine = new Name("beo");
	public static Name drawChat = new Name("a", "(I)V");
	public static Name clearChat = new Name("a", "()V");

	public static Name gameSettings = new Name("beu");
	public static Name gameOption = new Name(gameSettings.getName() + "$a");
	public static Name getKeyBinding = new Name("c", "(L" + gameSettings.getName() + "$a;)Ljava/lang/String;");

	public static Name networkManager = new Name("er");
	public static Name netHandlerPlayClient = new Name("bno");
	public static Name packetPayload = new Name("go");
	public static Name packetHeaderFooter = new Name("ih");
	public static Name packetChat = new Name("gf");
	public static Name packetSetSlot = new Name("gm");
	public static Name itemStack = new Name("afj");
	public static Name openContainer = new Name("acl");
	public static Name packetPlayerInfo = new Name("hg");
	public static Name handleCustomPayload = new Name("a", "(L" + packetPayload.getName() + ";)V");
	public static Name handlePacketPlayerListHeaderFooter = new Name("a", "(L" + packetHeaderFooter.getName() + ";)V");
	public static Name handlePacketChat = new Name("a", "(L" + packetChat.getName() + ";)V");
	public static Name handlePacketSetSlot = new Name("a", "(L" + packetSetSlot.getName() + ";)V");
	public static Name packetTitle = new Name("ie");
	public static Name handlePacketTitle = new Name("a", "(L" + packetTitle.getName() + ";)V");
	public static Name packetTeleport = new Name("hh");
	public static Name handlePacketTeleport = new Name("a", "(L" + packetTeleport.getName() + ";)V");

	public static Name guiGameOver = new Name("bgz");
	public static Name guiGameOverInit = new Name("<init>", "(L" + chatComponent.getName() + ";)V");

	public static Name guiScreen = new Name("bho");
	public static Name isCtrlKeyDown = new Name("q", "()Z");
	public static Name displayScreen = new Name("a", "(L" + guiScreen.getName() + ";)V");
	public static Name drawWorldBackground = new Name("d_", "(I)V");

	public static Name guiOptions = new Name("bhi");

	public static Name serverData = new Name("bnt");
	public static Name guiConnecting = new Name("bgv");
	public static Name guiConnectingInit1 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";L" + serverData.getName() + ";)V");
	public static Name guiConnectingInit2 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";Llava/lang/String;I)V");
	public static Name guiDisconnected = new Name("bhc");
	public static Name guiDisconnectedInit = new Name("<init>", "(L" + guiScreen.getName() + ";Ljava/lang/String;L" + chatComponent.getName() + ";)V");

	public static Name abstractClientPlayer = new Name("bpp");
	public static Name abstractClientPlayerInit = new Name("<init>", "(Lajs;Lcom/mojang/authlib/GameProfile;)V");
	public static Name resourceLocation = new Name("kq");
	public static Name getResourceLocation = new Name("o", "()L" + resourceLocation.getName() + ";");
	public static Name getFOVModifier = new Name("u", "()F");

	public static Name entityPlayerSP = new Name("bps");
	public static Name sendChatMessage = new Name("g", "(Ljava/lang/String;)V");

	public static Name guiMultiplayer = new Name("bjf");
	public static Name serverSelectionList = new Name("bjj");

	public static Name guiResourcePacks = new Name("bjm");

	public static Name guiSelectWorld = new Name("bka");
	public static Name worldList = new Name("bkc");

	public static Name renderItem = new Name("bvh");
	public static Name renderItemPerson = new Name("a", "(L" + itemStack.getName() + ";Lsw;Lbrq$b;Z)V");
	public static Name renderItemInventory = new Name("a", "(L" + itemStack.getName() + ";IILcbh;)V");

	public static Name guiEditSign = new Name("bjb");
	public static Name tileSign = new Name("asv");

	public static Name rendererLivingEntity = new Name("bvl");
	public static Name canRenderName = new Name("a", "(Lsw;)Z");

}
