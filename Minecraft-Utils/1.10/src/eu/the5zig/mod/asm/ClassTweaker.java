package eu.the5zig.mod.asm;

import net.minecraft.client.main.Main;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassTweaker implements ITweaker {

	private List<String> args;
	private File gameDir;
	private File assetsDir;
	private String version;

	public ClassTweaker() {
	}

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String version) {
		this.args = args;
		this.gameDir = gameDir;
		this.assetsDir = assetsDir;
		this.version = version;
		LogWrapper.info("Minecraft Version: " + version);
		try {
			LogWrapper.finest("Checking for Forge");
			Class.forName("net.minecraftforge.client.GuiIngameForge");
			LogWrapper.info("Forge detected!");
			Transformer.FORGE = true;
		} catch (Exception ignored) {
			LogWrapper.info("Forge not found!");
		}
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		classLoader.registerTransformer(Transformer.class.getName());
	}

	@Override
	public String getLaunchTarget() {
		return Main.class.getName();
	}

	@Override
	public String[] getLaunchArguments() {
		ArrayList<String> argumentList = (ArrayList<String>) Launch.blackboard.get("ArgumentList");
		if (argumentList.isEmpty()) {
			if (gameDir != null) {
				argumentList.add("--gameDir");
				argumentList.add(gameDir.getPath());
			}
			if (assetsDir != null) {
				argumentList.add("--assetsDir");
				argumentList.add(assetsDir.getPath());
			}
			argumentList.add("--version");
			argumentList.add(version);
			argumentList.addAll(args);
		}
		return new String[0];
	}

}
