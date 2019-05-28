package eu.the5zig.mod.modules.items.system;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.manager.einslive.EinsLiveManager;
import eu.the5zig.mod.manager.einslive.ITunesSearchResultEntry;
import eu.the5zig.mod.modules.AbstractModuleItem;
import eu.the5zig.mod.render.Base64Renderer;
import eu.the5zig.mod.render.RenderLocation;
import eu.the5zig.mod.util.ScrollingText;

public class EinsLive extends AbstractModuleItem {

	private static final Base64Renderer base64Renderer = new Base64Renderer(The5zigMod.EINSLIVE_LOCATION, 60, 60);

	private ScrollingText scrollingTrack;
	private ScrollingText scrollingArtist;

	@Override
	public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
		Gui.drawRect(x, y, x + getWidth(dummy), y + getHeight(dummy) - 2, EinsLiveManager.EINSLIVE_COLOR_BACKGROUND);

		EinsLiveManager einsLiveManager = The5zigMod.getDataManager().getEinsLiveManager();
		ITunesSearchResultEntry track = einsLiveManager.getCurrentTrack();

		int trackLeft = x;
		int trackTop = y;
		int trackRight = x + getWidth(dummy) - 4;
		int albumSize = getHeight(dummy) - 2;
		// Album Preview
		if (track != null && track.getTrackName() != null && track.getTrackId() != 0) {
			if (track.getImage() != null && !track.getImage().equals(base64Renderer.getBase64String())) {
				base64Renderer.setBase64String(track.getImage(), "itunes/track_" + track.getTrackId());
			} else if (track.getImage() == null && base64Renderer.getBase64String() != null) {
				base64Renderer.reset();
			}
		} else if (base64Renderer.getBase64String() != null) {
			base64Renderer.reset();
		}
		base64Renderer.renderImage(trackLeft, trackTop, albumSize, albumSize);

		// Track Info
		int infoLeft = trackLeft + albumSize + 4;
		int infoTop = trackTop + 3;
		int maxInfoWidth = trackRight - infoLeft;
		String trackName = track != null && track.getTrackName() != null ? track.getTrackName() : null;
		String artistName = track != null && track.getArtistName() != null ? track.getArtistName() : null;

		if (trackName != null && (scrollingTrack == null || !trackName.equals(scrollingTrack.getText()))) {
			scrollingTrack = new ScrollingText(trackName, maxInfoWidth, 8, EinsLiveManager.EINSLIVE_COLOR_BACKGROUND, 0xffffff);
		} else if (scrollingTrack != null && trackName == null) {
			scrollingTrack = null;
		}
		if (artistName != null && (scrollingArtist == null || !artistName.equals(scrollingArtist.getText()))) {
			scrollingArtist = new ScrollingText(artistName, maxInfoWidth, 6, EinsLiveManager.EINSLIVE_COLOR_BACKGROUND, 0xffffff);
		} else if (scrollingArtist != null && artistName == null) {
			scrollingArtist = null;
		}
		if (scrollingTrack != null) {
			scrollingTrack.setChild(scrollingArtist);
			scrollingTrack.setScale(getRenderSettings().getScale());
			scrollingTrack.render(infoLeft, infoTop);
		}
		if (scrollingArtist != null) {
			scrollingArtist.setParent(scrollingTrack);
			scrollingArtist.setScale(getRenderSettings().getScale());
			scrollingArtist.render(infoLeft, infoTop + 9);
		}
	}

	@Override
	public int getWidth(boolean dummy) {
		return 100;
	}

	@Override
	public int getHeight(boolean dummy) {
		return 22;
	}
}
