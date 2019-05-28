package eu.the5zig.mod.manager.spotify;

public enum SpotifyError {

	INVALID_OAUTH_TOKEN(4102),
	EXPIRED_OAUTH_TOKEN(4103),
	INVALID_CSRF_TOKEN(4107),
	OAUTH_TOKEN_INVALID_FOR_USER(4108),
	NO_USER_LOGGED_IN(4110),
	UNKNOWN(-1);

	private int id;

	SpotifyError(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static SpotifyError byId(int id) {
		for (SpotifyError spotifyError : values()) {
			if (spotifyError.getId() == id) {
				return spotifyError;
			}
		}
		return null;
	}
}
