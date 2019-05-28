import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.asm.Transformer;
import eu.the5zig.mod.gui.IOverlay;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;

public class Overlay implements IOverlay {

	Overlay() {
	}

	public static void updateOverlayCount(int count) {
	}

	public static void renderAll() {
	}

	@Override
	public void displayMessage(String title, String subtitle) {
		Toast.create(ChatColor.YELLOW + title, subtitle, null);
	}

	@Override
	public void displayMessage(String title, String subtitle, Object uniqueReference) {
		Toast.create(ChatColor.YELLOW + title, subtitle, uniqueReference);
	}

	@Override
	public void displayMessage(String message) {
		displayMessage("The 5zig Mod", message);
	}

	@Override
	public void displayMessage(String message, Object uniqueReference) {
		displayMessage("The 5zig Mod", message, uniqueReference);
	}

	@Override
	public void displayMessageAndSplit(String message) {
		displayMessageAndSplit(message, null);
	}

	@Override
	public void displayMessageAndSplit(String message, Object uniqueReference) {
		List<String> split = MinecraftFactory.getVars().splitStringToWidth(message, MinecraftFactory.getClassProxyCallback().getOverlayTexture() == 2 ? 142 : 150);
		String title = null;
		String subTitle = null;
		for (int i = 0; i < split.size(); i++) {
			if (i == 0)
				title = split.get(0);
			if (i == 1)
				subTitle = split.get(1);
		}
		displayMessage(title, subTitle, uniqueReference);
	}

	private static class Toast implements chp {

		private static final chp.a ENUM_SHOW, ENUM_HIDE;

		static {
			try {
				Class<?> enumClass = Thread.currentThread().getContextClassLoader().loadClass("chp" + "$a");
				ENUM_SHOW = (chp.a) enumClass.getField(Transformer.FORGE ? "SHOW" : "a").get(null);
				ENUM_HIDE = (chp.a) enumClass.getField(Transformer.FORGE ? "HIDE" : "b").get(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private boolean updateTime = true;
		private long startedTime;
		private final Object uniqueReference;
		private String title;
		private String subTitle;

		private Toast(Object uniqueReference, String title, String subTitle) {
			this.uniqueReference = uniqueReference;
			this.title = title;
			this.subTitle = subTitle;
		}

		private void update(String title, String subTitle) {
			this.title = title;
			this.subTitle = subTitle;
			this.startedTime = System.currentTimeMillis();
			this.updateTime = true;
		}

		@Override
		public chp.a a(chq var1, long l) {
			if (updateTime) {
				startedTime = l;
				updateTime = false;
			}

			MinecraftFactory.getVars().bindTexture(a);
			ctp.d(1.0F, 1.0F, 1.0F);
			int overlayTexture = MinecraftFactory.getClassProxyCallback().getOverlayTexture();
			var1.b(0, 0, 0, overlayTexture * 32, 160, 32);
			MinecraftFactory.getVars().drawString(title, overlayTexture == 2 ? 16 : 6, 7);
			MinecraftFactory.getVars().drawString(subTitle, overlayTexture == 2 ? 16 : 6, 18);

			return l - startedTime < 5000L ? ENUM_SHOW : ENUM_HIDE;
		}

		@Override
		public Object b() {
			return uniqueReference == null ? b : uniqueReference;
		}

		public static void create(String title, String subTitle, Object uniqueReference) {
			chq toastFactory = ((Variables) MinecraftFactory.getVars()).getMinecraft().ag();

			if (uniqueReference == null) {
				toastFactory.a(new Toast(null, title, subTitle));
			} else {
				Toast toast = toastFactory.a(Toast.class, uniqueReference);
				if (toast == null) {
					toastFactory.a(new Toast(uniqueReference, title, subTitle));
				} else {
					toast.update(title, subTitle);
				}
			}
		}
	}

}