import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Names;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.ingame.IGui2ndChat;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.minecraft.ChatColor;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Gui2ndChat implements IGui2ndChat {

	private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	private static final Joiner NEWLINE_STRING_JOINER = Joiner.on("\\n");

	private final List<String> sentMessages = Lists.newArrayList();
	private final List<GuiChatLine> chatLines = Lists.newArrayList();
	private final List<GuiChatLine> singleChatLines = Lists.newArrayList();
	private int scrollPos;
	private boolean isScrolled;

	private static Method clickChatComponent;

	public Gui2ndChat() {
	}

	static {
		if (Transformer.FORGE) {
			try {
				clickChatComponent = Names.guiScreen.load().getDeclaredMethod("func_175276_a", Names.chatComponent.load());
				clickChatComponent.setAccessible(true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void draw(int updateCounter) {
//		ij component = new iq("Test");
//		ez style = new ez();
//		style.a(new ij(ij.a.a, new iq("Hover!")));
//		style.a(new hy(hy.a.a, "http://google.de"));
//		component.a(style);
//		printChatMessage(component);
//		((Variables) MinecraftFactory.getVars()).getGuiIngame().d().a(component);

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
				int width = xp.f((float) this.getChatWidth() / chatScale);
				GLUtil.pushMatrix();
				if (Transformer.FORGE) {
					GLUtil.translate(scaledWidth - getChatWidth(), MinecraftFactory.getVars().getScaledHeight() - 28.0F, 0.0F);
				} else {
					GLUtil.translate(scaledWidth - getChatWidth(), 8.0F, 0.0F);
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
							c = xp.a(c, 0.0D, 1.0D);
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
									Gui.drawRect(x, y - 9, x + width + xp.d(4f * chatScale), y, alpha / 2 << 24);
								}
								highlightChatLine(chatLine.getChatComponent(), MinecraftFactory.getClassProxyCallback().is2ndChatTextLeftbound() ? x :
										(int) ((getChatWidth() - MinecraftFactory.getVars().getStringWidth(optStripColor(chatLine.getChatComponent().getString())) * chatScale) / chatScale), y - 9, alpha);
								String text = chatLine.getChatComponent().getString();
								GLUtil.enableBlend();
								if (MinecraftFactory.getClassProxyCallback().is2ndChatTextLeftbound()) {
									MinecraftFactory.getVars().drawString(text, x, y - 8, 16777215 + (alpha << 24));
								} else {
									MinecraftFactory.getVars().drawString(text, x + width - MinecraftFactory.getVars().getStringWidth(optStripColor(text)), y - 8,
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
						Gui.drawRect(width + 1, -var19, width + 3, -var19 - var12, color + (alpha << 24));
						Gui.drawRect(width + 3, -var19, width + 2, -var19 - var12, 13421772 + (alpha << 24));
					}
				}

				GLUtil.popMatrix();
			}
		}
	}

	@Override
	public void printChatMessage(String message) {
		this.printChatMessage(ChatComponentBuilder.fromLegacyText(message));
	}

	@Override
	public void printChatMessage(Object chatComponent) {
		if (!(chatComponent instanceof ij))
			throw new IllegalArgumentException(chatComponent.getClass().getName() + " != " + ij.class.getName());
		printChatMessage((ij) chatComponent);
	}

	public void printChatMessage(ij chatComponent) {
		this.printChatMessage(chatComponent, 0);
	}

	public void printChatMessage(ij chatComponent, int id) {
		LogManager.getLogger().info("[CHAT2] {}", chatComponent.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
		this.setChatLine(chatComponent, id, ((Variables) MinecraftFactory.getVars()).getGuiIngame().g(), false);
	}

	private void setChatLine(ij chatComponent, int id, int currentUpdateCounter, boolean refresh) {
		if (!refresh && MinecraftFactory.getClassProxyCallback().isShowTimeBeforeChatMessage()) {
			chatComponent = (ij) MinecraftFactory.getClassProxyCallback().getChatComponentWithTime(chatComponent);
		}
		if (id != 0) {
			this.deleteChatLine(id);
		}

		int lineWidth = xp.d((float) this.getChatWidth() / this.getChatScale());
		List<ij> lines = cgl.a(chatComponent, lineWidth, ((Variables) MinecraftFactory.getVars()).getFontrenderer(), false, false);
		boolean var6 = this.isChatOpened();

		ij lineString;
		for (Iterator<ij> iterator = lines.iterator(); iterator.hasNext(); this.singleChatLines.add(0, new GuiChatLine(currentUpdateCounter, lineString, id))) {
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
		ij chatComponent = getChatComponent(mouseX, mouseY);
		if (chatComponent != null && chatComponent.b().i() != null)
			((cjs) MinecraftFactory.getVars().getMinecraftScreen()).a(chatComponent, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int button) {
		try {
			return button == 0 && (clickChatComponent != null ? (Boolean) clickChatComponent.invoke(MinecraftFactory.getVars().getMinecraftScreen(),
					getChatComponent(mouseX, mouseY)) : ((civ) MinecraftFactory.getVars().getMinecraftScreen()).d(getChatComponent(mouseX, mouseY)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void keyTyped(int code) {
		if (code == 201) {
			scroll(((Variables) MinecraftFactory.getVars()).getGuiIngame().f().h() - 1);
		} else if (code == 209) {
			scroll(-((Variables) MinecraftFactory.getVars()).getGuiIngame().f().h() + 1);
		}
	}

	private ij getChatComponent(int mouseX, int mouseY) {
		if (!this.isChatOpened()) {
			return null;
		} else {
			int resolutionScaleFactor = MinecraftFactory.getVars().getScaleFactor();
			float chatScale = this.getChatScale();
			int x = mouseX / resolutionScaleFactor - MinecraftFactory.getVars().getScaledWidth() + getChatWidth();
			int y = mouseY / resolutionScaleFactor - 40;
			x = xp.d((float) x / chatScale);
			y = xp.d((float) y / chatScale);
			if (x >= 0 && y >= 0) {
				int lineCount = Math.min(this.getLineCount(), this.singleChatLines.size());
				if (x <= xp.d((float) this.getChatWidth() / this.getChatScale() + 3 / getChatScale()) && y < MinecraftFactory.getVars().getFontHeight() * lineCount) {
					int lineId = y / MinecraftFactory.getVars().getFontHeight() + this.scrollPos;
					if (lineId >= 0 && lineId < this.singleChatLines.size()) {
						GuiChatLine chatLine = this.singleChatLines.get(lineId);
						int widthCounter = MinecraftFactory.getClassProxyCallback().is2ndChatTextLeftbound() ? 0 :
								(int) ((getChatWidth() - MinecraftFactory.getVars().getStringWidth(optStripColor(chatLine.getChatComponent().getString())) * chatScale) / chatScale);

						for (ij chatComponent : chatLine.getChatComponent()) {
							if (chatComponent instanceof iq) { // ChatComponentText
								widthCounter += MinecraftFactory.getVars().getStringWidth(optStripColor(((iq) chatComponent).f()));
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

	private String optStripColor(String text) {
		return cgl.a(text, false);
	}

	public boolean isChatOpened() {
		return MinecraftFactory.getVars().getMinecraftScreen() instanceof civ;
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
		return xp.d(MinecraftFactory.getClassProxyCallback().get2ndChatWidth());
	}

	public int getChatHeight() {
		return xp.d(this.isChatOpened() ? MinecraftFactory.getClassProxyCallback().get2ndChatHeightFocused() : MinecraftFactory.getClassProxyCallback().get2ndChatHeightUnfocused());
	}

	public float getChatScale() {
		return MinecraftFactory.getClassProxyCallback().get2ndChatScale();
	}

	@Override
	public int getLineCount() {
		return this.getChatHeight() / 9;
	}

	public static void highlightChatLine(ij chatComponent, int x, int y, int alpha) {
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
		for (ij textComponent : chatComponent) {
			int currIndex = builder.length();
			String formattingCode = textComponent.b().k();
			String text = textComponent.d();
			builder += formattingCode + text + ChatColor.RESET;
			text = ChatColor.stripColor(text.toLowerCase(Locale.ROOT));

			for (String search : highlightWords) {
				search = search.replace("%player%", MinecraftFactory.getVars().getGameProfile().getName()).toLowerCase(Locale.ROOT);
				for (int nameIndex = builder.toLowerCase(Locale.ROOT).indexOf(search, currIndex), unformattedIndex = text.indexOf(search); nameIndex != -1 && unformattedIndex != -1;
						nameIndex = builder.toLowerCase(Locale.ROOT).indexOf(search, nameIndex + search.length()), unformattedIndex = text.indexOf(search, unformattedIndex + search.length())) {
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
