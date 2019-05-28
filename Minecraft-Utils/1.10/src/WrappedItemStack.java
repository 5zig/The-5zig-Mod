import com.google.common.collect.Multimap;
import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.util.GLUtil;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class WrappedItemStack implements ItemStack {

	private static final UUID ITEM_MODIFIER_UUID = UUID.fromString("cb3f55d3-645c-4f38-a497-9c13a33db5cf");

	private adz item;

	public WrappedItemStack(adz item) {
		this.item = item;
	}

	@Override
	public int getAmount() {
		return item.b;
	}

	@Override
	public int getMaxDurability() {
		return item.j();
	}

	@Override
	public int getCurrentDurability() {
		return item.h();
	}

	@Override
	public String getKey() {
		return getResourceKey(item);
	}

	@Override
	public String getDisplayName() {
		return item.q();
	}

	@Override
	public List<String> getLore() {
		return item.a(((Variables) MinecraftFactory.getVars()).getPlayer(), false);
	}

	@Override
	public int getHealAmount() {
		return item.b() instanceof adt ? ((adt) item.b()).h(item) : 0;
	}

	@Override
	public float getSaturationModifier() {
		return item.b() instanceof adt ? ((adt) item.b()).i(item) : 0;
	}

	@Override
	public void render(int x, int y, boolean withGenericAttributes) {
		if (item == null)
			return;
		((Variables) MinecraftFactory.getVars()).renderItem(item, x, y);

		if (withGenericAttributes) {
			for (sb modifier : sb.values()) {
				Multimap<String, st> multimap = item.a(modifier);
				for (Map.Entry<String, st> entry : multimap.entries()) {
					st attribute = entry.getValue();
					double value = attribute.d();
					if (ITEM_MODIFIER_UUID.equals(attribute.a())) {
						if (((Variables) MinecraftFactory.getVars()).getPlayer() == null) {
							value += 1;
							value += (double) agx.a(item, sk.a);
						} else {
							value += ((Variables) MinecraftFactory.getVars()).getPlayer().a(za.e).b();
							value += (double) agx.a(item, sk.a);
						}
					}
					if (entry.getKey().equals("generic.attackDamage") || entry.getKey().equals("generic.armor")) {
						GLUtil.disableDepth();
						GLUtil.pushMatrix();
						GLUtil.translate(x + 8, y + 10, 1);
						GLUtil.scale(0.7f, 0.7f, 0.7f);
						MinecraftFactory.getVars().drawString(ChatColor.BLUE + "+" + Math.round(value), 0, 0);
						GLUtil.popMatrix();
						GLUtil.enableDepth();
					}
				}
			}
		}
	}

	public static String getResourceKey(adz item) {
		kn resourceLocation = ado.g.b(item.b());
		return resourceLocation == null ? null : resourceLocation.toString();
	}
}
