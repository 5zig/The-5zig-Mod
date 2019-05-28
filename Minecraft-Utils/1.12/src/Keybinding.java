import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.util.IKeybinding;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Keybinding extends bhw implements IKeybinding {

	public Keybinding(String description, int keyCode, String category) {
		super(description, keyCode, category);
		tryRegisterCategory(category);
	}

	public boolean isPressed() {
		return g();
	}

	public int getKeyCode() {
		return j();
	}

	@Override
	public int compareTo(bhw o) {
		return super.a(o);
	}

	private static void tryRegisterCategory(String category) {
		Map<String, Integer> categories;
		try {
			Field field = Keybinding.class.getSuperclass().getDeclaredField(Transformer.FORGE ? "field_193627_d" : "d");
			field.setAccessible(true);
			categories = (Map<String, Integer>) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!categories.containsKey(category)) {
			categories.put(category, categories.size() + 1);
		}
	}
}
