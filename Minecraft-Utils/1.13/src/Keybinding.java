import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.util.IKeybinding;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Keybinding extends cfe implements IKeybinding {

	private static final Map<String, Integer> keyMap = new HashMap<>();

	public Keybinding(String description, int keyCode, String category) {
		super(description, keyCode, category);
		tryRegisterCategory(category);
	}

	public boolean isPressed() {
		return f();
	}

	public int getKeyCode() {
		return keyMap.getOrDefault(h().d(), -1);
	}

	@Override
	public int compareTo(cfe o) {
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

	static {
		keyMap.put("key.keyboard.0", 48);
		keyMap.put("key.keyboard.1", 49);
		keyMap.put("key.keyboard.2", 50);
		keyMap.put("key.keyboard.3", 51);
		keyMap.put("key.keyboard.4", 52);
		keyMap.put("key.keyboard.5", 53);
		keyMap.put("key.keyboard.6", 54);
		keyMap.put("key.keyboard.7", 55);
		keyMap.put("key.keyboard.8", 56);
		keyMap.put("key.keyboard.9", 57);
		keyMap.put("key.keyboard.a", 65);
		keyMap.put("key.keyboard.b", 66);
		keyMap.put("key.keyboard.c", 67);
		keyMap.put("key.keyboard.d", 68);
		keyMap.put("key.keyboard.e", 69);
		keyMap.put("key.keyboard.f", 70);
		keyMap.put("key.keyboard.g", 71);
		keyMap.put("key.keyboard.h", 72);
		keyMap.put("key.keyboard.i", 73);
		keyMap.put("key.keyboard.j", 74);
		keyMap.put("key.keyboard.k", 75);
		keyMap.put("key.keyboard.l", 76);
		keyMap.put("key.keyboard.m", 77);
		keyMap.put("key.keyboard.n", 78);
		keyMap.put("key.keyboard.o", 79);
		keyMap.put("key.keyboard.p", 80);
		keyMap.put("key.keyboard.q", 81);
		keyMap.put("key.keyboard.r", 82);
		keyMap.put("key.keyboard.s", 83);
		keyMap.put("key.keyboard.t", 84);
		keyMap.put("key.keyboard.u", 85);
		keyMap.put("key.keyboard.v", 86);
		keyMap.put("key.keyboard.w", 87);
		keyMap.put("key.keyboard.x", 88);
		keyMap.put("key.keyboard.y", 89);
		keyMap.put("key.keyboard.z", 90);
		keyMap.put("key.keyboard.f1", 290);
		keyMap.put("key.keyboard.f2", 291);
		keyMap.put("key.keyboard.f3", 292);
		keyMap.put("key.keyboard.f4", 293);
		keyMap.put("key.keyboard.f5", 294);
		keyMap.put("key.keyboard.f6", 295);
		keyMap.put("key.keyboard.f7", 296);
		keyMap.put("key.keyboard.f8", 297);
		keyMap.put("key.keyboard.f9", 298);
		keyMap.put("key.keyboard.f10", 299);
		keyMap.put("key.keyboard.f11", 300);
		keyMap.put("key.keyboard.f12", 301);
		keyMap.put("key.keyboard.f13", 302);
		keyMap.put("key.keyboard.f14", 303);
		keyMap.put("key.keyboard.f15", 304);
		keyMap.put("key.keyboard.f16", 305);
		keyMap.put("key.keyboard.f17", 306);
		keyMap.put("key.keyboard.f18", 307);
		keyMap.put("key.keyboard.f19", 308);
		keyMap.put("key.keyboard.f20", 309);
		keyMap.put("key.keyboard.f21", 310);
		keyMap.put("key.keyboard.f22", 311);
		keyMap.put("key.keyboard.f23", 312);
		keyMap.put("key.keyboard.f24", 313);
		keyMap.put("key.keyboard.f25", 314);
		keyMap.put("key.keyboard.num.lock", 282);
		keyMap.put("key.keyboard.keypad.0", 320);
		keyMap.put("key.keyboard.keypad.1", 321);
		keyMap.put("key.keyboard.keypad.2", 322);
		keyMap.put("key.keyboard.keypad.3", 323);
		keyMap.put("key.keyboard.keypad.4", 324);
		keyMap.put("key.keyboard.keypad.5", 325);
		keyMap.put("key.keyboard.keypad.6", 326);
		keyMap.put("key.keyboard.keypad.7", 327);
		keyMap.put("key.keyboard.keypad.8", 328);
		keyMap.put("key.keyboard.keypad.9", 329);
		keyMap.put("key.keyboard.keypad.add", 334);
		keyMap.put("key.keyboard.keypad.decimal", 330);
		keyMap.put("key.keyboard.keypad.enter", 335);
		keyMap.put("key.keyboard.keypad.equal", 336);
		keyMap.put("key.keyboard.keypad.multiply", 332);
		keyMap.put("key.keyboard.keypad.divide", 331);
		keyMap.put("key.keyboard.keypad.subtract", 333);
		keyMap.put("key.keyboard.down", 264);
		keyMap.put("key.keyboard.left", 263);
		keyMap.put("key.keyboard.right", 262);
		keyMap.put("key.keyboard.up", 265);
		keyMap.put("key.keyboard.apostrophe", 39);
		keyMap.put("key.keyboard.backslash", 92);
		keyMap.put("key.keyboard.comma", 44);
		keyMap.put("key.keyboard.equal", 61);
		keyMap.put("key.keyboard.grave.accent", 96);
		keyMap.put("key.keyboard.left.bracket", 91);
		keyMap.put("key.keyboard.minus", 45);
		keyMap.put("key.keyboard.period", 46);
		keyMap.put("key.keyboard.right.bracket", 93);
		keyMap.put("key.keyboard.semicolon", 59);
		keyMap.put("key.keyboard.slash", 47);
		keyMap.put("key.keyboard.space", 32);
		keyMap.put("key.keyboard.tab", 258);
		keyMap.put("key.keyboard.left.alt", 342);
		keyMap.put("key.keyboard.left.control", 341);
		keyMap.put("key.keyboard.left.shift", 340);
		keyMap.put("key.keyboard.left.win", 343);
		keyMap.put("key.keyboard.right.alt", 346);
		keyMap.put("key.keyboard.right.control", 345);
		keyMap.put("key.keyboard.right.shift", 344);
		keyMap.put("key.keyboard.right.win", 347);
		keyMap.put("key.keyboard.enter", 257);
		keyMap.put("key.keyboard.escape", 256);
		keyMap.put("key.keyboard.backspace", 259);
		keyMap.put("key.keyboard.delete", 261);
		keyMap.put("key.keyboard.end", 269);
		keyMap.put("key.keyboard.home", 268);
		keyMap.put("key.keyboard.insert", 260);
		keyMap.put("key.keyboard.page.down", 267);
		keyMap.put("key.keyboard.page.up", 266);
		keyMap.put("key.keyboard.caps.lock", 280);
		keyMap.put("key.keyboard.pause", 284);
		keyMap.put("key.keyboard.scroll.lock", 281);
		keyMap.put("key.keyboard.menu", 348);
		keyMap.put("key.keyboard.print.screen", 283);
		keyMap.put("key.keyboard.world.1", 161);
		keyMap.put("key.keyboard.world.2", 162);
	}
}
