package io.github.gaeqs.javayoutubedownloader.stream;

import io.github.gaeqs.javayoutubedownloader.stream.download.StreamDownloader;
import io.github.gaeqs.javayoutubedownloader.tag.StreamType;
import io.github.gaeqs.javayoutubedownloader.util.HTMLUtils;
import io.github.gaeqs.javayoutubedownloader.util.Validate;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

/**
 * A stream option stores the decoded url and the type of a stream. These instances can be used to
 * download the represented stream using a {@link StreamDownloader}
 * or another method you have.
 */
public class StreamOption {

    private final URL url;
    private final StreamType type;

    /**
     * Created a stream option by an {@link URL} and a {@link StreamType}. None of them can be null.
     *
     * @param url  the decoded url.
     * @param type the stream type.
     */
    public StreamOption(URL url, StreamType type) {
        Validate.notNull(url, "Url cannot be null!");
        Validate.notNull(type, "Type cannot be null!");
        this.url = url;
        this.type = type;
    }

    /**
     * Returns the decoded {@link URL} of the stream.
     *
     * @return the url.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Returns the {@link StreamType} of the stream. This object can give us information about the stream,
     * such as the video and audio quality, the format, or the container.
     * *
     *
     * @return the {@link StreamType}
     */
    public StreamType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{Url:" + url + ", Stream: " + type.toString() + "}";
    }

    /**
     * Checks whether the stream is accessible.
     *
     * @return whether the stream is accessible.
     */
    public boolean checkConnection() {
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", HTMLUtils.USER_AGENT);
            connection.setDoInput(true);
            connection.connect();
            HTMLUtils.check(connection);
            connection.disconnect();
            return true;
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ignore) {
                }
            }
            return false;
        }
    }
}
