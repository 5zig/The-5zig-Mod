package eu.the5zig.mod.manager.iloveradio;

public class ILoveRadioTrack {

	private final String title;
	private final String artist;
	private String image;

	public ILoveRadioTrack(String title, String artist) {
		this.title = title;
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ILoveRadioTrack that = (ILoveRadioTrack) o;

		if (title != null ? !title.equals(that.title) : that.title != null)
			return false;
		return artist != null ? artist.equals(that.artist) : that.artist == null;

	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + (artist != null ? artist.hashCode() : 0);
		return result;
	}
}
