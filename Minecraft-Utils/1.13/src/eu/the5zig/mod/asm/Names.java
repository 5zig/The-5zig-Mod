package eu.the5zig.mod.asm;

public class Names {

	public static Name _static = new Name("<clinit>", "()V");
	public static Name minecraft = new Name("cfi");
	public static Name getMinecraft = new Name("s", "()L" + minecraft.getName() + ";");
	public static Name stichResources = new Name("f", "()V");
	public static Name shutdown = new Name("h", "()V");
	public static Name crashReport = new Name("b");
	public static Name displayCrashReport = new Name("c", "(L" + crashReport.getName() + ";)V");
	public static Name tick = new Name("m", "()V");
	public static Name leftClickMouse = new Name("at", "()V");
	public static Name rightClickMouse = new Name("au", "()V");

	public static Name guiMainMenu = new Name("cjx");
	public static Name guiButton = new Name("cgj", "(IIILjava/lang/String;)V");
	public static Name guiButtonExtended = new Name(guiButton.getName(), "(IIIIILjava/lang/String;)V");
	public static Name fontRenderer = new Name("cfz");
	public static Name guiTextfield = new Name("cgn", "(IL" + fontRenderer.getName() + ";IIII)V");
	public static Name setWorldAndResolution = new Name("a", "(L" + minecraft.getName() + ";II)V");
	public static Name insertSingleMultiplayerButton = new Name("b", "(II)V");
	public static Name actionPerformed = new Name("a", "(L" + guiButton.getName() + ";)V");
	public static Name initGui = new Name("c", "()V");
	public static Name buttonList = new Name("o", "Ljava/util/List;");
	public static Name width = new Name("m", "I");

	public static Name chatComponent = new Name("ij");
	public static Name packetBuffer = new Name("hy");

	public static Name guiIngame = new Name("cga");
	public static Name guiIngameForge = new Name("net.minecraftforge.client.GuiIngameForge");
	public static Name renderGameOverlay = new Name("a", "(F)V");
	public static Name renderPotionEffectIndicator = new Name("a", "()V");
	public static Name renderFood = new Name("d", "()V");
	public static Name renderHotbar = new Name("a", "()V");
	public static Name renderGameOverlayForge = new Name("renderHUDText", "(II)V");
	public static Name ingameTick = new Name("c", "()V");
	public static Name renderVignette = new Name("b", "(F)V");
	public static Name renderChatForge = new Name("renderChat", "(II)V");

	public static Name guiIngameMenu = new Name("cjo");

	public static Name glStateManager = new Name("ctp");
	public static Name glColor = new Name("c", "(FFFF)V");

	public static Name guiChat = new Name("civ");
	public static Name guiChatNew = new Name("cgk");
	public static Name handleMouseInput = new Name("k", "()V");
	public static Name guiClosed = new Name("m", "()V");
	public static Name keyTyped = new Name("charTyped", "(CI)Z");
	public static Name keyPressed = new Name("keyPressed", "(III)Z");
	public static Name mouseClicked = new Name("mouseClicked", "(DDI)Z");
	public static Name drawScreen = new Name("a", "(IIF)V");
	public static Name setChatLine = new Name("a", "(L" + chatComponent.getName() + ";IIZ)V");
	public static Name guiChatLine = new Name("cfb");
	public static Name drawChat = new Name("a", "(I)V");
	public static Name clearChat = new Name("a", "()V");

	public static Name gameSettings = new Name("cfl");
	public static Name gameOption = new Name(gameSettings.getName() + "$a");
	public static Name getKeyBinding = new Name("c", "(L" + gameSettings.getName() + "$a;)Ljava/lang/String;");

	public static Name networkManager = new Name("hw");
	public static Name netHandlerPlayClient = new Name("cqs");
	public static Name packetPayload = new Name("jy");
	public static Name packetHeaderFooter = new Name("lv");
	public static Name packetChat = new Name("jn");
	public static Name packetChatAction = new Name("ih");
	public static Name packetSetSlot = new Name("jw");
	public static Name itemStack = new Name("ata");
	public static Name openContainer = new Name("apr");
	public static Name packetPlayerInfo = new Name("kr");
	public static Name packetTitle = new Name("ls");
	public static Name packetTeleport = new Name("kt");
	public static Name handleCustomPayload = new Name("a", "(L" + packetPayload.getName() + ";)V");
	public static Name handlePacketPlayerListHeaderFooter = new Name("a", "(L" + packetHeaderFooter.getName() + ";)V");
	public static Name handlePacketChat = new Name("a", "(L" + packetChat.getName() + ";)V");
	public static Name handlePacketSetSlot = new Name("a", "(L" + packetSetSlot.getName() + ";)V");
	public static Name handlePacketTitle = new Name("a", "(L" + packetTitle.getName() + ";)V");
	public static Name handlePacketTeleport = new Name("a", "(L" + packetTeleport.getName() + ";)V");

	public static Name guiGameOver = new Name("cjc");
	public static Name guiGameOverInit = new Name("<init>", "(L" + chatComponent.getName() + ";)V");

	public static Name guiScreen = new Name("cjs");
	public static Name isCtrlKeyDown = new Name("q", "()Z");
	public static Name displayScreen = new Name("a", "(L" + guiScreen.getName() + ";)V");
	public static Name drawWorldBackground = new Name("c_", "(I)V");

	public static Name guiOptions = new Name("cjm");

	public static Name serverData = new Name("cqy");
	public static Name guiConnecting = new Name("ciz");
	public static Name guiConnectingInit1 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";L" + serverData.getName() + ";)V");
	public static Name guiConnectingInit2 = new Name("<init>", "(L" + guiScreen.getName() + ";L" + minecraft.getName() + ";Llava/lang/String;I)V");
	public static Name guiDisconnected = new Name("cjf");
	public static Name guiDisconnectedInit = new Name("<init>", "(L" + guiScreen.getName() + ";Ljava/lang/String;L" + chatComponent.getName() + ";)V");

	public static Name abstractClientPlayer = new Name("csv");
	public static Name abstractClientPlayerInit = new Name("<init>", "(Laxs;Lcom/mojang/authlib/GameProfile;)V");
	public static Name resourceLocation = new Name("pc");
	public static Name getResourceLocation = new Name("j", "()L" + resourceLocation.getName() + ";");
	public static Name getFOVModifier = new Name("o", "()F");

	public static Name entityPlayerSP = new Name("csy");
	public static Name getEntityPlayer = new Name("i", "L" + Names.entityPlayerSP.getName() + ";");
	public static Name sendChatMessage = new Name("f", "(Ljava/lang/String;)V");

	public static Name guiMultiplayer = new Name("clm");
	public static Name serverSelectionList = new Name("clq");

	public static Name guiResourcePacks = new Name("cme");
	public static Name resourcePackList = new Name("cmh");
	public static Name resourcePackList2 = new Name("cmj");

	public static Name guiSelectWorld = new Name("cmq");
	public static Name worldListField = new Name("v");
	public static Name worldList = new Name("cms");

	public static Name renderItem = new Name("cyw");
	public static Name renderItemPerson = new Name("a", "(L" + itemStack.getName() + ";Laex;Lcuz$b;Z)V");
	public static Name renderItemInventory = new Name("a", "(L" + itemStack.getName() + ";IILdep;)V");

	public static Name guiEditSign = new Name("cli");
	public static Name tileSign = new Name("bjt");

	public static Name rendererLivingEntity = new Name("cza");
	public static Name canRenderName = new Name("a", "(Laex;)Z");

	String MINECRAFT_VERSION;


	void updateTo1_13() {
		if (MINECRAFT_VERSION.equals("1.13")) {
			install_mod();
			dont_crash();
		}
	}

	void install_mod(){}
	void dont_crash() {}

}
