import com.google.common.collect.Lists;
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

	private ata item;

	public WrappedItemStack(ata item) {
		this.item = item;
	}

	@Override
	public int getAmount() {
		return item.C();
	}

	@Override
	public int getMaxDurability() {
		return item.h();
	}

	@Override
	public int getCurrentDurability() {
		return item.g();
	}

	@Override
	public String getKey() {
		return getResourceKey(item);
	}

	@Override
	public String getDisplayName() {
		return item.q().d();
	}

	@Override
	public List<String> getLore() {
		List<String> result = Lists.newArrayList();
		for (ij chatComponent : item.a(((Variables) MinecraftFactory.getVars()).getPlayer(), aui.a.a)) {
			result.add(chatComponent.getString());
		}
		return result;
	}

	@Override
	public int getHealAmount() {
		return item.b() instanceof asr ? ((asr) item.b()).e(item) : 0;
	}

	@Override
	public float getSaturationModifier() {
		return item.b() instanceof asr ? ((asr) item.b()).e(item) : 0;
	}

	@Override
	public void render(int x, int y, boolean withGenericAttributes) {
		if (item == null)
			return;
		((Variables) MinecraftFactory.getVars()).renderItem(item, x, y);

		if (withGenericAttributes) {
			for (aet modifier : aet.values()) {
				Multimap<String, afl> multimap = item.a(modifier);
				for (Map.Entry<String, afl> entry : multimap.entries()) {
					afl attribute = entry.getValue();
					double value = attribute.d();
					if (ITEM_MODIFIER_UUID.equals(attribute.a())) {
						if (((Variables) MinecraftFactory.getVars()).getPlayer() == null) {
							value += 1;
							value += (double) awc.a(item, afa.a);
						} else {
							value += ((Variables) MinecraftFactory.getVars()).getPlayer().a(ang.f).b();
							value += (double) awc.a(item, afa.a);
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

	public static String getResourceKey(ata item) {
		pc resourceLocation = asw.f.b(item.b());
		return resourceLocation == null ? null : resourceLocation.toString();
	}
}
