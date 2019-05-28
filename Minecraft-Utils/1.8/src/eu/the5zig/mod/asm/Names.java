package eu.the5zig.mod.asm;

public class Names {

	public static Name _static = new Name("<clinit>", "()V");
	public static Name minecraft = new Name("bsu");
	public static Name startGame = new Name("aj", "()V");
	public static Name shutdown = new Name("g", "()V");
	public static Name resourcePackList = new Name("aw");
	public static Name dispatchKeypresses = new Name("X", "()V");
	public static Name crashReport = new Name("b");
	public static Name displayCrashReport = new Name("c", "(L" + crashReport.getName() + ";)V");
	public static Name tick = new Name("r", "()V");
	public static Name leftClickMouse = new Name("at", "()V");
	public static Name rightClickMouse = new Name("au", "()V");

	public static Name guiMainMenu = new Name("bxq");
	public static Name guiButton = new Name("bug", "(IIILjava/lang/String;)V");
	public static Name guiButtonExtended = new Name(guiButton.getName(), "(IIIIILjava/lang/String;)V");
	public static Name fontRenderer = new Name("bty");
	public static Name guiTextfield = new Name("bul", "(IL" + fontRenderer.getName() + ";IIII)V");
	public static Name setWorldAndResolution = new Name("a", "(L" + minecraft.getName() + ";II)V");
	public static Name insertSingleMultiplayerButton = new Name("b", "(II)V");
	public static Name actionPerformed = new Name("a", "(L" + guiButton.getName() + ";)V");
	public static Name initGui = new Name("b", "()V");
	public static Name buttonList = new Name("n", "Ljava/util/List;");
	public static Name width = new Name("l", "I");

	public static Name chatComponent = new Name("ho");
	public static Name packetBuffer = new Name("hd");

	public static Name guiIngame = new Name("btz");
	public static Name guiIngameForge = new Name("net.minecraftforge.client.GuiIngameForge");
	public static Name renderGameOverlay = new Name("a", "(F)V");
	public static Name scaledResolution = new Name("buf");
	public static Name renderFood = new Name("d", "(L" + scaledResolution.getName() + ";)V");
	public static Name renderFoodForge = new Name("renderFood", "(II)V");
	public static Name renderHotbar = new Name("a", "(L" + scaledResolution.getName() + ";F)V");
	public static Name renderGameOverlayForge = new Name("renderHUDText", "(II)V");
	public static Name renderChatForge = new Name("renderChat", "(II)V");
	public static Name ingameTick = new Name("c", "()V");
	public static Name renderVignette = new Name("a", "(FLbuf;)V");

	public static Name guiIngameMenu = new Name("bwy");

	public static Name glStateManager = new Name("cjm");
	public static Name glColor = new Name("c", "(FFFF)V");

	public static Name guiAchievement = new Name("bxt");
	public static Name updateAchievementWindow = new Name("a", "()V");

	public static Name guiChat = new Name("bvx");
	public static Name guiChatNew = new Name("buh");
	public static Name handleMouseInput = new Name("k", "()V");
	public static Name guiClosed = new Name("m", "()V");
	public static Name keyTyped = new Name("a", "(CI)V");
	public static Name mouseClicked = new Name("a", "(III)V");
	public static Name drawScreen = new Name("a", "(IIF)V");
	public static Name setChatLine = new Name("a", "(L" + chatComponent.getName() + ";IIZ)V");
	public static Name guiChatLine = new Name("bsq");
	public static Name drawChat = new Name("a", "(I)V");
	public static Name clearChat = new Name("a", "()V");

	public static Name gameSettings = new Name("bto");
	public static Name gameOption = new Name("btr");
	public static Name getKeyBinding = new Name("c", "(L" + gameSettings.getName() + "$a;)Ljava/lang/String;");

	public static Name networkManager = new Name("gr");
	public static Name netHandlerPlayClient = new Name("cee");
	public static Name packetPayload = new Name("ji");
	public static Name packetHeaderFooter = new Name("lm");
	public static Name packetChat = new Name("iz");
	public static Name packetSetSlot = new Name("jh");
	public static Name itemStack = new Name("amj");
	public static Name openContainer = new Name("ayc");
	public static Name packetPlayerInfo = new Name("kh");
	public static Name packetPlayerInfoAction = new Name("kj");
	public static Name packetPlayerInfoItem = new Name("kk");
	public static Name handleCustomPayload = new Name("a", "(L" + packetPayload.getName() + ";)V");
	public static Name handlePacketPlayerListHeaderFooter = new Name("a", "(L" + packetHeaderFooter.getName() + ";)V");
	public static Name handlePacketChat = new Name("a", "(L" + packetChat.getName() + ";)V");
	public static Name handlePacketSetSlot = new Name("a", "(L" + packetSetSlot.getName() + ";)V");
	public static Name packetTitle = new Name("lj");
	public static Name handlePacketTitle = new Name("a", "(L" + packetTitle.getName() + ";)V");
	public static Name packetTeleport = new Name("ii");
	public static Name handlePacketTeleport = new Name("a", "(L" + packetTeleport.getName() + ";)V");

	public static Name guiGameOver = new Name("bwl");
	public static Name guiGameOverInit = new Name("<init>", "()V");

	public static Name guiScreen = new Name("bxf");
	public static Name isCtrlKeyDown = new Name("q", "()Z");
	public static Name displayScreen = new Name("a", "(L" + guiScreen.getName() + ";)V");
	public static Name drawWorldBackground = new Name("b_", "(I)V");

	public static Name guiOptions = new Name("bwv");

	public static Name serverData = new Name("cew");
	public static Name guiConnecting = new Name("bwb");
	public static Name guiConnectingInit1 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";L" + serverData.getName() + ";)V");
	public static Name guiConnectingInit2 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";Llava/lang/String;I)V");
	public static Name guiDisconnected = new Name("bwo");
	public static Name guiDisconnectedInit = new Name("<init>", "(L" + guiScreen.getName() + ";Ljava/lang/String;L" + chatComponent.getName() + ";)V");

	public static Name abstractClientPlayer = new Name("cil");
	public static Name abstractClientPlayerInit = new Name("<init>", "(Laqu;Lcom/mojang/authlib/GameProfile;)V");
	public static Name resourceLocation = new Name("oa");
	public static Name getResourceLocation = new Name("k", "()L" + resourceLocation.getName() + ";");
	public static Name getFOVModifier = new Name("o", "()F");

	public static Name entityPlayerSP = new Name("cio");
	public static Name sendChatMessage = new Name("e", "(Ljava/lang/String;)V");

	public static Name guiMultiplayer = new Name("bzp");
	public static Name serverSelectionList = new Name("bzu");

	public static Name guiResourcePacks = new Name("bzx");

	public static Name guiSelectWorld = new Name("bxg");

	public static Name renderItem = new Name("cqh");
	public static Name renderItemPerson = new Name("a", "(Lamj;Lxm;Lcmz;)V");
	public static Name renderItemInventory = new Name("a", "(Lamj;II)V");

	public static Name guiEditSign = new Name("bzm");
	public static Name tileSign = new Name("bdj");

	public static Name rendererLivingEntity = new Name("cqv");
	public static Name canRenderName = new Name("a", "(Lxm;)Z");

}
