package eu.the5zig.mod.asm;

public class Names {

	public static Name _static = new Name("<clinit>", "()V");
	public static Name minecraft = new Name("bao");
	public static Name startGame = new Name("ag", "()V");
	public static Name shutdown = new Name("e", "()V");
	public static Name dispatchKeypresses = new Name("aa", "()V");
	public static Name resourcePackList = new Name("ap");
	public static Name crashReport = new Name("b");
	public static Name displayCrashReport = new Name("c", "(L" + crashReport.getName() + ";)V");
	public static Name tick = new Name("p", "()V");
	public static Name leftClickMouse = new Name("al", "()V");
	public static Name rightClickMouse = new Name("am", "()V");

	public static Name guiMainMenu = new Name("bee");
	public static Name guiButton = new Name("bcb", "(IIILjava/lang/String;)V");
	public static Name guiButtonExtended = new Name(guiButton.getName(), "(IIIIILjava/lang/String;)V");
	public static Name fontRenderer = new Name("bbu");
	public static Name guiTextfield = new Name("bcd", "(IL" + fontRenderer.getName() + ";IIII)V");
	public static Name setWorldAndResolution = new Name("a", "(L" + minecraft.getName() + ";II)V");
	public static Name insertSingleMultiplayerButton = new Name("b", "(II)V");
	public static Name actionPerformed = new Name("a", "(L" + guiButton.getName() + ";)V");
	public static Name initGui = new Name("b", "()V");
	public static Name buttonList = new Name("n", "Ljava/util/List;");
	public static Name width = new Name("l", "I");

	public static Name chatComponent = new Name("fj");
	public static Name packetBuffer = new Name("et");

	public static Name guiIngame = new Name("bbv");
	public static Name guiIngameForge = new Name("net.minecraftforge.client.GuiIngameForge");
	public static Name renderGameOverlay = new Name("a", "(FZII)V");
	public static Name renderFood = new Name("a", "(II)V");
	public static Name renderFoodForge = new Name("renderFood", "(II)V");
	public static Name renderGameOverlayForge = new Name("renderHUDText", "(II)V");
	public static Name renderChatForge = new Name("renderChat", "(II)V");
	public static Name renderHotbarForge = new Name("renderHotbar", "(IIF)V");
	public static Name ingameTick = new Name("c", "()V");
	public static Name renderVignette = new Name("a", "(FII)V");

	public static Name guiAchievement = new Name("beh");
	public static Name updateAchievementWindow = new Name("a", "()V");

	public static Name guiChat = new Name("bct");
	public static Name guiChatNew = new Name("bcc");
	public static Name handleMouseInput = new Name("k", "()V");
	public static Name guiClosed = new Name("m", "()V");
	public static Name keyTyped = new Name("a", "(CI)V");
	public static Name mouseClicked = new Name("a", "(III)V");
	public static Name drawScreen = new Name("a", "(IIF)V");
	public static Name setChatLine = new Name("a", "(L" + chatComponent.getName() + ";IIZ)V");
	public static Name guiChatLine = new Name("bak");
	public static Name drawChat = new Name("a", "(I)V");
	public static Name clearChat = new Name("a", "()V");

	public static Name guiIngameMenu = new Name("bdp");

	public static Name gameSettings = new Name("bbj");
	public static Name gameOption = new Name("bbm");
	public static Name getKeyBinding = new Name("c", "(L" + gameSettings.getName() + "$a;)Ljava/lang/String;");

	public static Name networkManager = new Name("ej");
	public static Name netHandlerPlayClient = new Name("bjb");
	public static Name packetPayload = new Name("gr");
	public static Name packetChat = new Name("gj");
	public static Name packetSetSlot = new Name("gq");
	public static Name itemStack = new Name("add");
	public static Name openContainer = new Name("amd");
	public static Name packetPlayerInfo = new Name("ho");
	public static Name handleCustomPayload = new Name("a", "(L" + packetPayload.getName() + ";)V");
	public static Name handlePacketChat = new Name("a", "(L" + packetChat.getName() + ";)V");
	public static Name handlePacketSetSlot = new Name("a", "(L" + packetSetSlot.getName() + ";)V");
	public static Name packetTeleport = new Name("fu");
	public static Name handlePacketTeleport = new Name("a", "(L" + packetTeleport.getName() + ";)V");

	public static Name guiGameOver = new Name("bdc");
	public static Name guiGameOverInit = new Name("<init>", "()V");

	public static Name guiScreen = new Name("bdw");
	public static Name isCtrlKeyDown = new Name("q", "()Z");
	public static Name displayScreen = new Name("a", "(L" + guiScreen.getName() + ";)V");
	public static Name drawWorldBackground = new Name("c_", "(I)V");

	public static Name guiOptions = new Name("bdm");

	public static Name serverData = new Name("bjn");
	public static Name guiConnecting = new Name("bcx");
	public static Name guiConnectingInit1 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";L" + serverData.getName() + ";)V");
	public static Name guiConnectingInit2 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";Llava/lang/String;I)V");
	public static Name guiDisconnected = new Name("bdf");
	public static Name guiDisconnectedInit = new Name("<init>", "(L" + guiScreen.getName() + ";Ljava/lang/String;L" + chatComponent.getName() + ";)V");

	public static Name abstractClientPlayer = new Name("blg");
	public static Name abstractClientPlayerInit = new Name("<init>", "(Lahb;Lcom/mojang/authlib/GameProfile;)V");
	public static Name resourceLocation = new Name("bqx");
	public static Name hasCape = new Name("n", "()Z");
	public static Name getResourceLocation = new Name("s", "()L" + resourceLocation.getName() + ";");

	public static Name entityClientPlayerMP = new Name("bjk");
	public static Name sendChatMessage = new Name("a", "(Ljava/lang/String;)V");

	public static Name entityPlayerSP = new Name("blk");
	public static Name getFOVModifier = new Name("t", "()F");

	public static Name guiMultiplayer = new Name("bfz");
	public static Name serverSelectionList = new Name("bge");

	public static Name guiResourcePacks = new Name("bgf");

	public static Name guiSelectWorld = new Name("bdx");

	public static Name guiEditSign = new Name("bfx");
	public static Name tileSign = new Name("apm");

	public static Name rendererLivingEntity = new Name("boh");
	public static Name canRenderName = new Name("b", "(Lsv;)Z");

}
