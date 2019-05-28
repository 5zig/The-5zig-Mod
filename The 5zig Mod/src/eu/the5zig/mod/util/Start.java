package eu.the5zig.mod.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eu.the5zig.util.Utils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Main entry point for debugging Minecraft in Intellij IDEA.
 */
public class Start {

	public static void main(String[] args) throws IOException {
		OptionParser optionParser = new OptionParser();
		optionParser.allowsUnrecognizedOptions();
		OptionSpec<String> uuidSpec = optionParser.accepts("uuid").withRequiredArg();
		OptionSet optionSet = optionParser.parse(args);

		if (optionSet.has(uuidSpec)) {
			File launcherProfilesFile = new File(getMinecraftDirectory(), "launcher_profiles.json");
			JsonObject root = new JsonParser().parse(IOUtils.toString(launcherProfilesFile.toURI())).getAsJsonObject();
			String selectedUser = optionSet.valueOf(uuidSpec);
			JsonObject authenticationDatabase = null;
			for (Map.Entry<String, JsonElement> entry : root.get("authenticationDatabase").getAsJsonObject().entrySet()) {
				if (!entry.getValue().isJsonObject()) {
					continue;
				}
				if (entry.getValue().getAsJsonObject().getAsJsonObject("profiles").has(selectedUser)) {
					authenticationDatabase = entry.getValue().getAsJsonObject();
				}
			}

			Validate.notNull(authenticationDatabase, "Profile " + selectedUser + " not found!");
			String accessToken = authenticationDatabase.get("accessToken").getAsString();
			String displayName = authenticationDatabase.getAsJsonObject("profiles").getAsJsonObject(selectedUser).get("displayName").getAsString();
//			JsonObject authenticationDatabase = root.get("authenticationDatabase").getAsJsonObject().entrySet().iterator().next().getValue().getAsJsonObject();
//			String accessToken = authenticationDatabase.get("accessToken").getAsString();
//			String displayName = authenticationDatabase.get("displayName").getAsString();

			System.out.println("Launching Minecraft as " + displayName + " (token:" + accessToken + ":" + selectedUser + ")");

			Launch.main(Utils.concat(new String[]{"--assetsDir", "assets", "--userProperties", "{}", "--username", displayName, "--accessToken", accessToken, "--tweakClass",
					"eu.the5zig.mod.asm.ClassTweaker"}, args));
		} else {
			Launch.main(Utils.concat(
					new String[]{"--assetsDir", "assets", "--userProperties", "{}", "--username", "Steve", "--uuid", "00000000000000000000000000000000", "--accessToken", "0", "--tweakClass",
							"eu.the5zig.mod.asm.ClassTweaker"}, args));
		}
	}

	public static File getMinecraftDirectory() {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (Utils.getPlatform()) {
			case LINUX:
			case SOLARIS:
				workingDirectory = new File(userHome, ".minecraft/");
				break;
			case WINDOWS:
				String applicationData = System.getenv("APPDATA");
				if (applicationData != null) {
					workingDirectory = new File(applicationData, ".minecraft/");
				} else {
					workingDirectory = new File(userHome, ".minecraft/");
				}
				break;
			case MAC:
				workingDirectory = new File(userHome, "Library/Application Support/minecraft");
				break;
			default:
				workingDirectory = new File(userHome, "minecraft/");
		}
		return workingDirectory;
	}

}
