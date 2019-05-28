package eu.the5zig.mod.asm;

public class Names {

	public static Name _static = new Name("<clinit>", "()V");
	public static Name minecraft = new Name("bcf");
	public static Name startGame = new Name("an", "()V");
	public static Name resourcePacks = new Name("aD");
	public static Name shutdown = new Name("h", "()V");
	public static Name dispatchKeypresses = new Name("W", "()V");
	public static Name crashReport = new Name("b");
	public static Name displayCrashReport = new Name("c", "(L" + crashReport.getName() + ";)V");
	public static Name tick = new Name("t", "()V");
	public static Name leftClickMouse = new Name("aw", "()V");
	public static Name rightClickMouse = new Name("ax", "()V");

	public static Name guiMainMenu = new Name("bfi");
	public static Name guiButton = new Name("bcz", "(IIILjava/lang/String;)V");
	public static Name guiButtonExtended = new Name(guiButton.getName(), "(IIIIILjava/lang/String;)V");
	public static Name fontRenderer = new Name("bct");
	public static Name guiTextfield = new Name("bdd", "(IL" + fontRenderer.getName() + ";IIII)V");
	public static Name setWorldAndResolution = new Name("a", "(L" + minecraft.getName() + ";II)V");
	public static Name insertSingleMultiplayerButton = new Name("b", "(II)V");
	public static Name actionPerformed = new Name("a", "(L" + guiButton.getName() + ";)V");
	public static Name initGui = new Name("b", "()V");
	public static Name buttonList = new Name("n", "Ljava/util/List;");
	public static Name width = new Name("l", "I");

	public static Name chatComponent = new Name("eu");
	public static Name packetBuffer = new Name("em");

	public static Name guiIngame = new Name("bcu");
	public static Name guiIngameForge = new Name("net.minecraftforge.client.GuiIngameForge");
	public static Name renderGameOverlay = new Name("a", "(F)V");
	public static Name scaledResolution = new Name("bcx");
	public static Name renderPotionEffectIndicator = new Name("a", "(L" + scaledResolution.getName() + ";)V");
	public static Name renderFood = new Name("d", "(L" + scaledResolution.getName() + ";)V");
	public static Name renderHotbar = new Name("a", "(L" + scaledResolution.getName() + ";F)V");
	public static Name renderGameOverlayForge = new Name("renderHUDText", "(II)V");
	public static Name ingameTick = new Name("c", "()V");
	public static Name renderVignette = new Name("b", "(FL" + scaledResolution.getName() + ";)V");
	public static Name renderChatForge = new Name("renderChat", "(II)V");

	public static Name guiIngameMenu = new Name("bex");

	public static Name glStateManager = new Name("bni");
	public static Name glColor = new Name("c", "(FFFF)V");

	public static Name guiAchievement = new Name("bfl");
	public static Name updateAchievementWindow = new Name("a", "()V");

	public static Name guiChat = new Name("bee");
	public static Name guiChatNew = new Name("bda");
	public static Name handleMouseInput = new Name("k", "()V");
	public static Name guiClosed = new Name("m", "()V");
	public static Name keyTyped = new Name("a", "(CI)V");
	public static Name mouseClicked = new Name("a", "(III)V");
	public static Name drawScreen = new Name("a", "(IIF)V");
	public static Name setChatLine = new Name("a", "(L" + chatComponent.getName() + ";IIZ)V");
	public static Name guiChatLine = new Name("bcb");
	public static Name drawChat = new Name("a", "(I)V");
	public static Name clearChat = new Name("a", "()V");

	public static Name gameSettings = new Name("bch");
	public static Name gameOption = new Name(gameSettings.getName() + "$a");
	public static Name getKeyBinding = new Name("c", "(L" + gameSettings.getName() + "$a;)Ljava/lang/String;");

	public static Name networkManager = new Name("ek");
	public static Name netHandlerPlayClient = new Name("bks");
	public static Name packetPayload = new Name("gh");
	public static Name packetHeaderFooter = new Name("ia");
	public static Name packetChat = new Name("fy");
	public static Name packetSetSlot = new Name("gf");
	public static Name itemStack = new Name("adq");
	public static Name openContainer = new Name("aau");
	public static Name packetPlayerInfo = new Name("gz");
	public static Name handleCustomPayload = new Name("a", "(L" + packetPayload.getName() + ";)V");
	public static Name handlePacketPlayerListHeaderFooter = new Name("a", "(L" + packetHeaderFooter.getName() + ";)V");
	public static Name handlePacketChat = new Name("a", "(L" + packetChat.getName() + ";)V");
	public static Name handlePacketSetSlot = new Name("a", "(L" + packetSetSlot.getName() + ";)V");
	public static Name packetTitle = new Name("hx");
	public static Name handlePacketTitle = new Name("a", "(L" + packetTitle.getName() + ";)V");
	public static Name packetTeleport = new Name("ha");
	public static Name handlePacketTeleport = new Name("a", "(L" + packetTeleport.getName() + ";)V");

	public static Name guiGameOver = new Name("bem");
	public static Name guiGameOverInit = new Name("<init>", "(L" + chatComponent.getName() + ";)V");

	public static Name guiScreen = new Name("bfb");
	public static Name isCtrlKeyDown = new Name("q", "()Z");
	public static Name displayScreen = new Name("a", "(L" + guiScreen.getName() + ";)V");
	public static Name drawWorldBackground = new Name("d_", "(I)V");

	public static Name guiOptions = new Name("bev");

	public static Name serverData = new Name("bkx");
	public static Name guiConnecting = new Name("bei");
	public static Name guiConnectingInit1 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";L" + serverData.getName() + ";)V");
	public static Name guiConnectingInit2 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";Llava/lang/String;I)V");
	public static Name guiDisconnected = new Name("bep");
	public static Name guiDisconnectedInit = new Name("<init>", "(L" + guiScreen.getName() + ";Ljava/lang/String;L" + chatComponent.getName() + ";)V");

	public static Name abstractClientPlayer = new Name("bmq");
	public static Name abstractClientPlayerInit = new Name("<init>", "(Laht;Lcom/mojang/authlib/GameProfile;)V");
	public static Name resourceLocation = new Name("kk");
	public static Name getResourceLocation = new Name("r", "()L" + resourceLocation.getName() + ";");
	public static Name getFOVModifier = new Name("x", "()F");

	public static Name entityPlayerSP = new Name("bmt");
	public static Name sendChatMessage = new Name("g", "(Ljava/lang/String;)V");

	public static Name guiMultiplayer = new Name("bgr");
	public static Name serverSelectionList = new Name("bgv");

	public static Name guiResourcePacks = new Name("bgy");

	public static Name guiSelectWorld = new Name("bhm");

	public static Name renderItem = new Name("brz");
	public static Name renderItemPerson = new Name("a", "(L" + itemStack.getName() + ";Lsa;Lbos$b;Z)V");
	public static Name renderItemInventory = new Name("a", "(L" + itemStack.getName() + ";IILbxo;)V");

	public static Name guiEditSign = new Name("bgn");
	public static Name tileSign = new Name("aqn");

	public static Name rendererLivingEntity = new Name("bsd");
	public static Name canRenderName = new Name("a", "(Lsa;)Z");

}
