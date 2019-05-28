package eu.the5zig.mod.gui;

import com.google.common.collect.Lists;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IFileSelector;
import eu.the5zig.mod.gui.elements.ITextfield;
import eu.the5zig.mod.util.FileSelectorCallback;
import eu.the5zig.mod.util.Keyboard;
import eu.the5zig.util.Callback;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GuiFileSelector extends Gui {

	private IFileSelector fileSelector;
	private FileSelectorCallback callback;
	private final File startDirectory;
	private String[] allowedExtensions;
	private int tabIndex = -1;
	private List<String> fileNames = Lists.newArrayList();

	public GuiFileSelector(Gui lastScreen, FileSelectorCallback callback, String... extensions) {
		this(lastScreen, callback, new File(System.getProperty("user.home", "Desktop")), extensions);
	}

	public GuiFileSelector(Gui lastScreen, FileSelectorCallback callback, File startDirectory, String... extensions) {
		super(lastScreen);
		this.callback = callback;
		this.startDirectory = startDirectory;
		this.allowedExtensions = extensions;
	}

	@Override
	public void initGui() {
		fileSelector = The5zigMod.getVars().createFileSelector(startDirectory, getWidth(), getHeight(), getWidth() / 2 - 150, getWidth() / 2 + 160, 60, getHeight() / 6 + 150,
				new Callback<File>() {
					@Override
					public void call(File callback) {
						selectFile(callback);
					}
				});
		ITextfield textfield = The5zigMod.getVars().createTextfield(1, getWidth() / 2 - 150, 38, 250, 18, 200);
		textfield.setSelected(false);
		textfield.setText(fileSelector.getCurrentDir() == null ? "" : fileSelector.getCurrentDir().getAbsolutePath() + "\\");
		addTextField(textfield);
		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 + 105, 37, 50, 20, I18n.translate("file_selector.open")));
		addButton(The5zigMod.getVars().createIconButton(The5zigMod.ITEMS, 16, 0, 2, getWidth() / 2 - 172, 61));

		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 - 152, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.cancel")));
		addButton(The5zigMod.getVars().createButton(100, getWidth() / 2 + 2, getHeight() / 6 + 168, 150, 20, I18n.translate("file_selector.select")));
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 1) {
			ITextfield textfield = getTextfieldById(1);
			if (textfield.getText().isEmpty()) {
				fileSelector.updateDir(null);
			} else {
				File file = new File(textfield.getText());
				if (!file.exists() || !file.isDirectory())
					return;
				fileSelector.updateDir(file);
			}
		}
		if (button.getId() == 2) {
			fileSelector.goUp();
		}
		if (button.getId() == 100) {
			selectFile(fileSelector.getSelectedFile());
		}
	}

	private void selectFile(File selectedFile) {
		if (selectedFile == null)
			return;
		if (!selectedFile.isFile()) {
			fileSelector.updateDir(selectedFile);
			return;
		}
		boolean allow = false;
		String name = FilenameUtils.getExtension(fileSelector.getSelectedFile().getName());
		for (String extension : allowedExtensions) {
			if (name.equalsIgnoreCase(extension)) {
				allow = true;
				break;
			}
		}
		if (!allow)
			return;
		The5zigMod.getVars().displayScreen(lastScreen);
		callback.onDone(selectedFile);
	}

	@Override
	protected void tick() {
		boolean enable = fileSelector.getSelectedFile() != null;
		if (fileSelector.getSelectedFile() != null && fileSelector.getSelectedFile().isFile()) {
			enable = false;
			String name = FilenameUtils.getExtension(fileSelector.getSelectedFile().getName());
			for (String extension : allowedExtensions) {
				if (name.equalsIgnoreCase(extension)) {
					enable = true;
					break;
				}
			}
			getButtonById(100).setLabel(I18n.translate("file_selector.select"));
		} else {
			getButtonById(100).setLabel(I18n.translate("file_selector.open"));
		}
		getButtonById(100).setEnabled(enable);

		ITextfield textfield = getTextfieldById(1);
		if (!textfield.isFocused()) {
			File currentDir = fileSelector.getCurrentDir();
			if (currentDir == null) {
				textfield.setText("");
			} else {
				textfield.setText(currentDir.getAbsolutePath());
			}
		}
	}

	@Override
	protected void onKeyType(char character, int key) {
		if (key == Keyboard.KEY_TAB) {
			ITextfield textfield = getTextfieldById(1);
			if (tabIndex != -1) {
				tabIndex++;
				String string = fileNames.get(tabIndex % fileNames.size());
				textfield.setText(string);
			} else {
				fileNames.clear();
				tabIndex = -1;
				File currentFile = new File(textfield.getText());
				File currentDir = textfield.getText().endsWith("\\") ? currentFile : currentFile.getParentFile();
				if (currentDir == null) {
					return;
				}
				File[] files = currentDir.listFiles();
				if (files == null) {
					return;
				}
				String text = textfield.getText();

				for (File file : files) {
					if (file.isDirectory() && file.getAbsolutePath().toLowerCase(Locale.ROOT).startsWith(text.toLowerCase(Locale.ROOT))) {
						fileNames.add(file.getAbsolutePath());
					}
				}
				if (!fileNames.isEmpty()) {
					tabIndex++;
					String string = fileNames.get(tabIndex % fileNames.size());
					textfield.setText(string);
					if (fileNames.size() == 1) {
						tabIndex = -1;
					}
				}
			}
		} else {
			tabIndex = -1;
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground();
		fileSelector.draw(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void handleMouseInput() {
		fileSelector.handleMouseInput();
	}

	@Override
	public String getTitleName() {
		return callback.getTitle();
	}

}
