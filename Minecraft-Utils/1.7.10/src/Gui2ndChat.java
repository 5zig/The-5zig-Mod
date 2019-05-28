import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.ingame.IGui2ndChat;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.Utils;
import eu.the5zig.util.minecraft.ChatColor;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Mouse;
import tv.twitch.chat.ChatUserInfo;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Decompiled from avt.class
 */
public class Gui2ndChat implements IGui2ndChat, bcv {

	private static final Set ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");
	private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	private static final Joiner NEWLINE_STRING_JOINER = Joiner.on("\\n");

	private final List<String> sentMessages = Lists.newArrayList();
	private final List<GuiChatLine> chatLines = Lists.newArrayList();
	private final List<GuiChatLine> singleChatLines = Lists.newArrayList();
	private int scrollPos;
	private boolean isScrolled;

	private URI link;
	private Object chatInstance;

	public Gui2ndChat() {
	}

	@Override
	public void draw(int updateCounter) {
		int scaledWidth = MinecraftFactory.getVars().getScaledWidth();
		if (MinecraftFactory.getClassProxyCallback().is2ndChatVisible()) {
			int lineDisplayCount = this.getLineCount();
			boolean chatOpened = false;
			int totalChatLines = 0;
			int chatLineCount = this.singleChatLines.size();
			float opacity = MinecraftFactory.getClassProxyCallback().get2ndChatOpacity() * 0.9F + 0.1F;
			if (chatLineCount > 0) {
				if (this.isChatOpened()) {
					chatOpened = true;
				}

				float chatScale = this.getChatScale();
				int width = qh.f((float) this.getChatWidth() / chatScale);
				GLUtil.pushMatrix();
				if (Transformer.FORGE) {
					GLUtil.translate(scaledWidth - getChatWidth() - 6.0f * chatScale, MinecraftFactory.getVars().getScaledHeight() - 28.0F, 0.0F);
				} else {
					GLUtil.translate(scaledWidth - getChatWidth() - 6.0f * chatScale, 20.0F, 0.0F);
				}
				GLUtil.scale(chatScale, chatScale, 1.0F);

				int lineIndex;
				int ticksPassed;
				int alpha;
				for (lineIndex = 0; lineIndex + this.scrollPos < this.singleChatLines.size() && lineIndex < lineDisplayCount; ++lineIndex) {
					GuiChatLine chatLine = this.singleChatLines.get(lineIndex + this.scrollPos);
					if (chatLine != null) {
						ticksPassed = updateCounter - chatLine.getUpdatedCounter();
						if (ticksPassed < 200 || chatOpened) {
							double c = (double) ticksPassed / 200.0D;
							c = 1.0D - c;
							c *= 10.0D;
							c = qh.a(c, 0.0D, 1.0D);
							c *= c;
							alpha = (int) (255.0D * c);
							if (chatOpened) {
								alpha = 255;
							}

							alpha = (int) ((float) alpha * opacity);
							++totalChatLines;
							if (alpha > 3) {
								int x = 0;
								int y = -lineIndex * 9;
								if (!MinecraftFactory.getClassProxyCallback().isChatBackgroundTransparent()) {
									Gui.drawRect(x, y - 9, x + width + qh.d(4f * chatScale), y, alpha / 2 << 24);
								}
								highlightChatLine(chatLine.getChatComponent(), MinecraftFactory.getClassProxyCallback().is2ndChatTextLeftbound() ? x :
										(int) ((getChatWidth() - MinecraftFactory.getVars().getStringWidth(strip(chatLine.getChatComponent().d(), false)) * chatScale) / chatScale), y - 9, alpha);
								String text = chatLine.getChatComponent().d();
								GLUtil.enableBlend();
								if (MinecraftFactory.getClassProxyCallback().is2ndChatTextLeftbound()) {
									MinecraftFactory.getVars().drawString(text, x, y - 8, 16777215 + (alpha << 24));
								} else {
									MinecraftFactory.getVars().drawString(text, x + width - MinecraftFactory.getVars().getStringWidth(text), y - 8,
											16777215 + (alpha << 24));
								}
								GLUtil.disableAlpha();
								GLUtil.disableBlend();
							}
						}
					}
				}

				if (chatOpened) {
					int fontHeight = MinecraftFactory.getVars().getFontHeight();
					GLUtil.translate(-3.0F, 0.0F, 0.0F);
					int visibleLineHeight = chatLineCount * fontHeight + chatLineCount;
					int totalLineHeight = totalChatLines * fontHeight + totalChatLines;
					int var19 = this.scrollPos * totalLineHeight / chatLineCount;
					int var12 = totalLineHeight * totalLineHeight / visibleLineHeight;
					if (visibleLineHeight != totalLineHeight) {
						alpha = var19 > 0 ? 170 : 96;
						int color = this.isScrolled ? 13382451 : 3355562;
						Gui.drawRect(width + 6, -var19, width + 8, -var19 - var12, color + (alpha << 24));
						Gui.drawRect(width + 8, -var19, width + 7, -var19 - var12, 13421772 + (alpha << 24));
					}
				}

				GLUtil.popMatrix();
			}
		}
	}

	public void clearChatMessages() {
		this.singleChatLines.clear();
		this.chatLines.clear();
		this.sentMessages.clear();
	}

	@Override
	public void printChatMessage(String message) {
		this.printChatMessage(ChatComponentBuilder.fromLegacyText(message));
	}

	@Override
	public void printChatMessage(Object chatComponent) {
		if (!(chatComponent instanceof fj))
			throw new IllegalArgumentException(chatComponent.getClass().getName() + " != " + fj.class.getName());
		printChatMessage((fj) chatComponent);
	}

	public void printChatMessage(fj chatComponent) {
		this.printChatMessage(chatComponent, 0);
	}

	public void printChatMessage(fj chatComponent, int id) {
		LogManager.getLogger().info("[CHAT2] {}", chatComponent.c());
		this.setChatLine(chatComponent, id, ((Variables) MinecraftFactory.getVars()).getGuiIngame().c(), false);
	}

	private void setChatLine(fj chatComponent, int id, int currentUpdateCounter, boolean refresh) {
		if (!refresh && MinecraftFactory.getClassProxyCallback().isShowTimeBeforeChatMessage()) {
			chatComponent = (fj) MinecraftFactory.getClassProxyCallback().getChatComponentWithTime(chatComponent);
		}
		if (id != 0) {
			this.deleteChatLine(id);
		}

		int lineWidth = qh.d((float) this.getChatWidth() / this.getChatScale());
		List<fj> lines = strip(chatComponent, lineWidth, false, false);
		boolean var6 = this.isChatOpened();

		fj lineString;
		for (Iterator<fj> iterator = lines.iterator(); iterator.hasNext(); this.singleChatLines.add(0, new GuiChatLine(currentUpdateCounter, lineString, id))) {
			lineString = iterator.next();
			if (var6 && this.scrollPos > 0) {
				this.isScrolled = true;
				this.scroll(1);
			}
		}

		while (this.singleChatLines.size() > MinecraftFactory.getClassProxyCallback().getMaxChatLines()) {
			this.singleChatLines.remove(this.singleChatLines.size() - 1);
		}

		if (!refresh) {
			this.chatLines.add(0, new GuiChatLine(currentUpdateCounter, chatComponent, id));

			while (this.chatLines.size() > MinecraftFactory.getClassProxyCallback().getMaxChatLines()) {
				this.chatLines.remove(this.chatLines.size() - 1);
			}
		}

	}

	public static String strip(String paramString, boolean paramBoolean) {
		if ((paramBoolean) || (((Variables) MinecraftFactory.getVars()).getGameSettings().n)) {
			return paramString;
		}
		return a.a(paramString);
	}

	public static List<fj> strip(fj paramho, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
		int i = 0;
		fq localhy1 = new fq("");
		ArrayList<fj> localArrayList1 = Lists.newArrayList();
		ArrayList<fj> localArrayList2 = Lists.newArrayList(paramho);
		for (int j = 0; j < localArrayList2.size(); j++) {
			fj localho = localArrayList2.get(j);
			String str1 = localho.e();
			int k = 0;
			if (str1.contains("\n")) {
				int m = str1.indexOf('\n');
				Object localObject2 = str1.substring(m + 1);
				str1 = str1.substring(0, m + 1);
				fq localhy2 = new fq((String) localObject2);
				localhy2.a(localho.b().m());
				localArrayList2.add(j + 1, localhy2);
				k = 1;
			}
			Object localObject1 = strip(localho.b().j() + str1, paramBoolean2);
			Object localObject2 = ((String) localObject1).endsWith("\n") ? ((String) localObject1).substring(0, ((String) localObject1).length() - 1) : localObject1;
			int n = MinecraftFactory.getVars().getStringWidth((String) localObject2);
			fq localhy3 = new fq((String) localObject2);
			localhy3.a(localho.b().m());
			if (i + n > paramInt) {
				String str2 = ((Variables) MinecraftFactory.getVars()).getFontrenderer().a((String) localObject1, paramInt - i, false);
				Object localObject3 = str2.length() < ((String) localObject1).length() ? ((String) localObject1).substring(str2.length()) : null;
				if ((localObject3 != null) && (((String) localObject3).length() > 0)) {
					int i1 = str2.lastIndexOf(" ");
					if ((i1 >= 0) && (((Variables) MinecraftFactory.getVars()).getFontrenderer().a(((String) localObject1).substring(0, i1)) > 0)) {
						str2 = ((String) localObject1).substring(0, i1);
						if (paramBoolean1) {
							i1++;
						}
						localObject3 = ((String) localObject1).substring(i1);
					} else if ((i > 0) && (!((String) localObject1).contains(" "))) {
						str2 = "";
						localObject3 = localObject1;
					}
					fq localhy4 = new fq((String) localObject3);
					localhy4.a(localho.b().m());
					localArrayList2.add(j + 1, localhy4);
				}
				localObject1 = str2;
				n = ((Variables) MinecraftFactory.getVars()).getFontrenderer().a((String) localObject1);
				localhy3 = new fq((String) localObject1);
				localhy3.a(localho.b().m());
				k = 1;
			}
			if (i + n <= paramInt) {
				i += n;

				localhy1.a(localhy3);
			} else {
				k = 1;
			}
			if (k != 0) {
				localArrayList1.add(localhy1);
				i = 0;
				localhy1 = new fq("");
			}
		}
		localArrayList1.add(localhy1);
		return localArrayList1;
	}

	@Override
	public void clear() {
		sentMessages.clear();
		singleChatLines.clear();
		chatLines.clear();
		resetScroll();
	}

	@Override
	public void refreshChat() {
		this.singleChatLines.clear();
		this.resetScroll();

		for (int i = this.chatLines.size() - 1; i >= 0; --i) {
			GuiChatLine chatLine = this.chatLines.get(i);
			this.setChatLine(chatLine.getChatComponent(), chatLine.getChatLineID(), chatLine.getUpdatedCounter(), true);
		}

	}

	public List<String> getSentMessages() {
		return this.sentMessages;
	}

	public void addToSentMessages(String message) {
		if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
			this.sentMessages.add(message);
		}

	}

	@Override
	public void resetScroll() {
		this.scrollPos = 0;
		this.isScrolled = false;
	}

	@Override
	public void scroll(int scroll) {
		this.scrollPos += scroll;
		int chatLines = this.singleChatLines.size();
		if (this.scrollPos > chatLines - this.getLineCount()) {
			this.scrollPos = chatLines - this.getLineCount();
		}

		if (this.scrollPos <= 0) {
			this.scrollPos = 0;
			this.isScrolled = false;
		}

	}

	@Override
	public void drawComponentHover(int mouseX, int mouseY) {
		fj chatComponent = getChatComponent(Mouse.getX(), Mouse.getY());
		if (chatComponent != null && chatComponent.b().i() != null) {
			fl localfl = chatComponent.b().i();
			Object localObject;
			if (localfl.a() == fm.c) {
				localObject = null;
				dy localdy = eb.a(localfl.b().c());
				if ((localdy != null) && ((localdy instanceof dh))) {
					localObject = add.a((dh) localdy);
				}
				if (localObject != null) {
					((bdw) MinecraftFactory.getVars().getMinecraftScreen()).a((add) localObject, mouseX, mouseY);
				} else {
					((bdw) MinecraftFactory.getVars().getMinecraftScreen()).a(a.m + "Invalid Item!", mouseX, mouseY);
				}
			} else if (localfl.a() == fm.a) {
				((bdw) MinecraftFactory.getVars().getMinecraftScreen()).a(Splitter.on("\n").splitToList(localfl.b().d()), mouseX, mouseY);
			} else if (localfl.a() == fm.b) {
				localObject = pp.a(localfl.b().c());
				if (localObject != null) {
					fj localfj2 = ((ph) localObject).e();
					fr localfr = new fr("stats.tooltip.type." + (((ph) localObject).d() ? "achievement" : "statistic"));
					localfr.b().b(true);
					String str = (localObject instanceof pb) ? ((pb) localObject).f() : null;
					ArrayList localArrayList = Lists.newArrayList(localfj2.d(), localfr.d());
					if (str != null) {
						localArrayList.addAll(((Variables) MinecraftFactory.getVars().getMinecraftScreen()).getFontrenderer().c(str, 150));
					}
					((bdw) MinecraftFactory.getVars().getMinecraftScreen()).a(localArrayList, mouseX, mouseY);
				} else {
					((bdw) MinecraftFactory.getVars().getMinecraftScreen()).a(a.m + "Invalid statistic/achievement!", mouseX, mouseY);
				}
			}
			GLUtil.disableLighting();
		}
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		return mouseClicked(getChatComponent(Mouse.getX(), Mouse.getY()));
	}

	private boolean mouseClicked(fj localfj) {
		if (localfj != null) {
			fh localfh = localfj.b().h();
			if (localfh != null) {
				if (bdw.r()) {
					((bct) MinecraftFactory.getVars().getMinecraftScreen()).a.b(localfj.e());
				} else if (localfh.a() == fi.a) {
					try {
						URI localURI = new URI(localfh.b());
						if (!ALLOWED_PROTOCOLS.contains(localURI.getScheme().toLowerCase(Locale.ROOT))) {
							throw new URISyntaxException(localfh.b(), "Unsupported protocol: " + localURI.getScheme().toLowerCase(Locale.ROOT));
						}
						if (((Variables) MinecraftFactory.getVars()).getGameSettings().p) {
							link = localURI;
							chatInstance = MinecraftFactory.getVars().getMinecraftScreen();
							((Variables) MinecraftFactory.getVars()).getMinecraft().a(new bcu(this, localfh.b(), 0, false));
						} else {
							Utils.openURL(localURI);
						}
					} catch (URISyntaxException localURISyntaxException) {
						MinecraftFactory.getClassProxyCallback().getLogger().error("Can't open url for " + localfh, localURISyntaxException);
					}
				} else {
					Object localObject;
					if (localfh.a() == fi.b) {
						localObject = new File(localfh.b()).toURI();
						Utils.openURL((URI) localObject);
					} else if (localfh.a() == fi.e) {
						((bct) MinecraftFactory.getVars().getMinecraftScreen()).a.a(localfh.b());
					} else if (localfh.a() == fi.c) {
						Utils.openURL(localfh.b());
					} else if (localfh.a() == fi.d) {
						localObject = ((Variables) MinecraftFactory.getVars()).getMinecraft().Z().a(localfh.b());
						if (localObject != null) {
							MinecraftFactory.getVars().displayScreen(new bgs(((Variables) MinecraftFactory.getVars()).getMinecraft().Z(), (ChatUserInfo) localObject));
						} else {
							MinecraftFactory.getClassProxyCallback().getLogger().error("Tried to handle twitch user but couldn't find them!");
						}
					} else {
						MinecraftFactory.getClassProxyCallback().getLogger().error("Don't know how to handle " + localfh);
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void keyTyped(int code) {
		if (code == 201) {
			scroll(getLineCount() - 1);
		} else if (code == 209) {
			scroll(-getLineCount() + 1);
		}
	}

	private fj getChatComponent(int mouseX, int mouseY) {
		if (!this.isChatOpened()) {
			return null;
		} else {
			int resolutionScaleFactor = MinecraftFactory.getVars().getScaleFactor();
			float chatScale = this.getChatScale();
			int x = mouseX / resolutionScaleFactor - MinecraftFactory.getVars().getScaledWidth() + getChatWidth() + 6;
			int y = mouseY / resolutionScaleFactor - 27;
			x = qh.d((float) x / chatScale);
			y = qh.d((float) y / chatScale);
			if (x >= 0 && y >= 0) {
				int lineCount = Math.min(this.getLineCount(), this.singleChatLines.size());
				if (x <= qh.d((float) this.getChatWidth() / this.getChatScale() + 3 / getChatScale()) && y < MinecraftFactory.getVars().getFontHeight() * lineCount) {
					int lineId = y / MinecraftFactory.getVars().getFontHeight() + this.scrollPos;
					if (lineId >= 0 && lineId < this.singleChatLines.size()) {
						GuiChatLine chatLine = this.singleChatLines.get(lineId);
						int widthCounter = MinecraftFactory.getClassProxyCallback().is2ndChatTextLeftbound() ? 0 :
								(int) ((getChatWidth() - MinecraftFactory.getVars().getStringWidth(strip(chatLine.getChatComponent().d(), false)) * chatScale) / chatScale);

						for (Object chatComponentObject : chatLine.getChatComponent()) {
							fj chatComponent = (fj) chatComponentObject;
							if (chatComponent instanceof fq) { // ChatComponentText
								widthCounter += MinecraftFactory.getVars().getStringWidth(strip(((fq) chatComponent).g(), false));
								if (widthCounter > x) {
									return chatComponent;
								}
							}
						}
					}

					return null;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	public boolean isChatOpened() {
		return MinecraftFactory.getVars().getMinecraftScreen() instanceof bct;
	}

	/**
	 * finds and deletes a Chat line by ID
	 */
	public void deleteChatLine(int id) {
		Iterator<GuiChatLine> iterator = this.singleChatLines.iterator();
		GuiChatLine chatLine;

		while (iterator.hasNext()) {
			chatLine = iterator.next();

			if (chatLine.getChatLineID() == id) {
				iterator.remove();
			}
		}

		iterator = this.chatLines.iterator();

		while (iterator.hasNext()) {
			chatLine = iterator.next();

			if (chatLine.getChatLineID() == id) {
				iterator.remove();
				break;
			}
		}
	}

	public int getChatWidth() {
		return qh.d(MinecraftFactory.getClassProxyCallback().get2ndChatWidth());
	}

	public int getChatHeight() {
		return qh.d(this.isChatOpened() ? MinecraftFactory.getClassProxyCallback().get2ndChatHeightFocused() : MinecraftFactory.getClassProxyCallback().get2ndChatHeightUnfocused());
	}

	public float getChatScale() {
		return MinecraftFactory.getClassProxyCallback().get2ndChatScale();
	}

	@Override
	public int getLineCount() {
		return this.getChatHeight() / 9;
	}

	/**
	 * confirmClicked
	 *
	 * @param accepted true, if the callback has been executed successfully.
	 * @param i
	 */
	@Override
	public void a(boolean accepted, int i) {
		if (i == 0) {
			if (accepted) {
				Utils.openURL(link);
			}
			link = null;
			MinecraftFactory.getVars().displayScreen(chatInstance);
			chatInstance = null;
		}
	}

	public static void highlightChatLine(fj chatComponent, int x, int y, int alpha) {
		List<String> highlightWords;
		boolean onlyWordMatch;
		String chatSearchText = MinecraftFactory.getClassProxyCallback().getChatSearchText();
		if (!Strings.isNullOrEmpty(chatSearchText)) {
			onlyWordMatch = false;
			highlightWords = ImmutableList.of(chatSearchText);
		} else {
			onlyWordMatch = true;
			highlightWords = MinecraftFactory.getClassProxyCallback().getHighlightWords();
		}
		if (highlightWords.isEmpty()) {
			return;
		}
		String builder = "";
		for (Object object : chatComponent) {
			fj textComponent = (fj) object;
			int currIndex = builder.length();
			String formattingCode = textComponent.b().j();
			String text = textComponent.e();
			builder += formattingCode + text + ChatColor.RESET;
			text = ChatColor.stripColor(text.toLowerCase(Locale.ROOT));

			for (String search : highlightWords) {
				search = search.replace("%player%", MinecraftFactory.getVars().getGameProfile().getName()).toLowerCase(Locale.ROOT);
				for (int nameIndex = builder.toLowerCase(Locale.ROOT).indexOf(search, currIndex), unformattedIndex = text.indexOf(search); nameIndex != -1 && unformattedIndex != -1;
						nameIndex = builder.toLowerCase(Locale.ROOT).indexOf(search, nameIndex + search.length()), unformattedIndex =
								text.indexOf(search, unformattedIndex + search.length())) {
					if (onlyWordMatch) {
						if (unformattedIndex > 0) {
							char previousChar = Character.toLowerCase(text.charAt(unformattedIndex - 1));
							if ((previousChar >= 'a' && previousChar <= 'z') || (previousChar >= '0' && previousChar <= '9')) {
								continue;
							}
						}
						if (unformattedIndex + search.length() < text.length()) {
							char nextChar = text.charAt(unformattedIndex + search.length());
							if ((nextChar >= 'a' && nextChar <= 'z') || (nextChar >= '0' && nextChar <= '9')) {
								continue;
							}
						}
					}
					int offset = MinecraftFactory.getVars().getStringWidth(builder.substring(0, nameIndex));
					int width = MinecraftFactory.getVars().getStringWidth(formattingCode + builder.substring(nameIndex, nameIndex + search.length()));
					Gui.drawRect(x + offset, y, x + offset + width, y + MinecraftFactory.getVars().getFontHeight(), MinecraftFactory.getClassProxyCallback().getHighlightWordsColor() + (Math.min(0x80, alpha) << 24));
				}
			}
		}
	}
}