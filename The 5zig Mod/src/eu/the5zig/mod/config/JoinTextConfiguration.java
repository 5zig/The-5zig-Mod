package eu.the5zig.mod.config;

import java.io.File;

public class JoinTextConfiguration extends Configuration<JoinTexts> {

	public JoinTextConfiguration(File parent) {
		super(new File(parent, "joinTexts.json"), JoinTexts.class);
		for (JoinText joinText : configInstance.getTexts()) {
			joinText.setServer(joinText.getServer()); // update pattern
		}
	}

}
