package eu.the5zig.mod.manager.itunes;

public class ITunesTrack {

	private String id;
	private String name;
	private String artist;
	private double length;
	private String artwork;

	private transient String image;

	public ITunesTrack() {
	}

	public ITunesTrack(String id, String name, String artist, double length) {
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.length = length;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getArtist() {
		return artist;
	}

	public double getLength() {
		return length;
	}

	public String getArtwork() {
		return artwork;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean hasTrackInformation() {
		return id != null && name != null && artist != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ITunesTrack that = (ITunesTrack) o;

		return id != null ? id.equals(that.id) : that.id == null;

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "ITunesTrack{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", artist='" + artist + '\'' +
				", length=" + length +
				'}';
	}
}
