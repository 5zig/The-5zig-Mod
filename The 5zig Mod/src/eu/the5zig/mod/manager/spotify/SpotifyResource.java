package eu.the5zig.mod.manager.spotify;

/**
 * A class representing a resource (eg. track, artist or album information)
 */
public class SpotifyResource {

	/**
	 * The name of the resource.
	 */
	private String name;
	/**
	 * The Spotify-URI of the resource.
	 */
	private String uri;
	/**
	 * The location of the resource.
	 */
	private Location location;

	public SpotifyResource() {
	}

	public SpotifyResource(String name, String uri, Location location) {
		this.name = name;
		this.uri = uri;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		if (uri == null || !uri.startsWith("spotify:")) {
			return null;
		}
		String[] split = uri.split(":");
		if (split.length != 3) {
			return null;
		}
		return split[2];
	}

	public String getUri() {
		return uri;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		SpotifyResource that = (SpotifyResource) o;

		if (name != null ? !name.equals(that.name) : that.name != null)
			return false;
		if (uri != null ? !uri.equals(that.uri) : that.uri != null)
			return false;
		return location != null ? location.equals(that.location) : that.location == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (uri != null ? uri.hashCode() : 0);
		result = 31 * result + (location != null ? location.hashCode() : 0);
		return result;
	}

	public class Location {
		private String og;

		public String getOg() {
			return og;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Location location = (Location) o;

			return og != null ? og.equals(location.og) : location.og == null;

		}

		@Override
		public int hashCode() {
			return og != null ? og.hashCode() : 0;
		}
	}

}
