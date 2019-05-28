package eu.the5zig.mod.gui.ingame;

public class PotionEffectImpl implements PotionEffect {

	private final String name;
	private final int time;
	private final String timeString;
	private final int amplifier;
	private final int iconIndex;
	private final boolean good;
	private final boolean hasParticles;
	private final int liquidColor;

	public PotionEffectImpl(String name, int time, String timeString, int amplifier, int iconIndex, boolean good, boolean hasParticles, int liquidColor) {
		this.name = name;
		this.time = time;
		this.timeString = timeString;
		this.amplifier = amplifier;
		this.iconIndex = iconIndex;
		this.good = good;
		this.hasParticles = hasParticles;
		this.liquidColor = liquidColor;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public String getTimeString() {
		return timeString;
	}

	@Override
	public int getAmplifier() {
		return amplifier;
	}

	@Override
	public int getIconIndex() {
		return iconIndex;
	}

	@Override
	public boolean isGood() {
		return good;
	}

	@Override
	public boolean hasParticles() {
		return hasParticles;
	}

	@Override
	public int getLiquidColor() {
		return liquidColor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PotionEffectImpl that = (PotionEffectImpl) o;

		return name != null ? name.equals(that.name) : that.name == null;

	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public int compareTo(PotionEffect o) {
		int compare = Boolean.valueOf(o.isGood()).compareTo(isGood());
		return compare == 0 ? Integer.valueOf(getTime()).compareTo(o.getTime()) : compare;
	}
}
