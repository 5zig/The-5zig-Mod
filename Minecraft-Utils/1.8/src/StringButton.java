import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class StringButton extends Button {

	public StringButton(int id, int x, int y, String label) {
		super(id, x, y, label);
	}

	public StringButton(int id, int x, int y, int width, int height, String label) {
		super(id, x, y, width, height, label);
	}

	@Override
	public void draw(int paramInt1, int paramInt2) {
		if (!this.m) {
			return;
		}

		cjm.c(1.0F, 1.0F, 1.0F, 1.0F);

		this.n = ((paramInt1 >= this.h) && (paramInt2 >= this.i) && (paramInt1 < this.h + this.f) && (paramInt2 < this.i + this.g));

		cjm.l();
		cjm.a(770, 771, 1, 0);
		cjm.b(770, 771);

		b(((Variables) MinecraftFactory.getVars()).getMinecraft(), paramInt1, paramInt2);

		int i2 = 14737632;
		if (!this.l) {
			i2 = 10526880;
		} else if (this.n) {
			i2 = 16777120;
		}
		Gui.drawCenteredString(this.j, this.h + this.f / 2, this.i + (this.g - 8) / 2, i2);
	}

}
