package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GuiFriendRequests extends Gui {

	private IGuiList friendRequestList;
	private int selected = 0;

	public GuiFriendRequests(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		addButton(MinecraftFactory.getVars().createButton(200, getWidth() / 2 - 155, getHeight() - 32, 150, 20, MinecraftFactory.getVars().translate("gui.done")));

		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 + 5, getHeight() - 32, 68, 20, I18n.translate("friend_requests.accept")));
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 + 77, getHeight() - 32, 68, 20, I18n.translate("friend_requests.deny")));

		initRowList();
	}

	protected void initRowList() {
		friendRequestList = The5zigMod.getVars().createGuiList(null, getWidth(), getHeight(), 50, getHeight() - 50, 0, getWidth(), The5zigMod.getFriendManager().getFriendRequests());
		friendRequestList.setRowWidth(200);
		friendRequestList.setScrollX(getWidth() - 15);
		addGuiList(friendRequestList);

		if (selected < 0)
			selected = 0;
		friendRequestList.setSelectedId(selected);
	}

	private void enableDisableButtons() {
		boolean enabled = !The5zigMod.getFriendManager().getFriendRequests().isEmpty();

		getButtonById(1).setEnabled(enabled);
		getButtonById(2).setEnabled(enabled);
	}

	@Override
	protected void tick() {
		enableDisableButtons();
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (The5zigMod.getFriendManager().getFriendRequests().isEmpty()) {
			drawCenteredString(I18n.translate("friend_requests.none"), getWidth() / 2, getHeight() / 2 - 20);
		}
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 1 || button.getId() == 2) {
			User selectedRow = (User) friendRequestList.getSelectedRow();
			if (selectedRow == null)
				return;
			The5zigMod.getFriendManager().handleFriendRequestResponse(selectedRow.getUniqueId(), button.getId() == 1);
			enableDisableButtons();
		}
	}

	@Override
	public String getTitleKey() {
		return "friend_requests.title";
	}

}
