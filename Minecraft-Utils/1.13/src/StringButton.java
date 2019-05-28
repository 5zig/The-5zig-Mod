import eu.the5zig.mod.gui.Gui;

/**
 * Created by 5zig.
 * All rights reserved � 2015
 */
public class StringButton extends Button {

	public StringButton(int id, int x, int y, String label) {
		super(id, x, y, label);
	}

	public StringButton(int id, int x, int y, int width, int height, String label) {
		super(id, x, y, width, height, label);
	}

	@Override
	public void draw(int var1, int var2) {
		if (!this.m) {
			return;
		}

		cfi var4 = cfi.s();
		ctp.c(1.0F, 1.0F, 1.0F, 1.0F);
		this.n = var1 >= this.h && var2 >= this.i && var1 < this.h + this.f && var2 < this.i + this.g;
		ctp.m();
		ctp.a(ctp.r.l, ctp.l.j, ctp.r.e, ctp.l.n);
		ctp.a(ctp.r.l, ctp.l.j);
		this.a(var4, var1, var2);
		int var7 = 14737632;
		if (!this.l) {
			var7 = 10526880;
		} else if (this.n) {
			var7 = 16777120;
		}

		Gui.drawCenteredString(this.j, this.h + this.f / 2, this.i + (this.g - 8) / 2, var7);
	}

}
