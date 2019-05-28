package eu.the5zig.mod.manager.itunes.com;
import com.jacob.com.Dispatch;

/**
 * Represents a URL track.
 *
 * A URL track references a network audio stream.
 */
public class ITURLTrack extends IITTrack {

    public ITURLTrack (Dispatch d) {
        super(d);
    }

    /**
     * Returns the URL of the stream represented by this track.
     * @return The URL of the stream represented by this track.
     */
    public String getURL () {
        return Dispatch.get(object, "URL").getString();
    }

    /**
     * Set the URL of the stream represented by this track.
     * @param url The URL of the stream represented by this track.
     */
    public void setURL (String url) {
        Dispatch.call(object, "URL", url);
    }

}
