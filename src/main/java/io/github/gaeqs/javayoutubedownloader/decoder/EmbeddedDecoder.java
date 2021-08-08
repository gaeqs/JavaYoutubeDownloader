package io.github.gaeqs.javayoutubedownloader.decoder;

import io.github.gaeqs.javayoutubedownloader.exception.EmbeddedExtractionException;
import io.github.gaeqs.javayoutubedownloader.stream.EncodedStream;
import io.github.gaeqs.javayoutubedownloader.stream.YoutubeVideo;
import io.github.gaeqs.javayoutubedownloader.util.EncodedStreamUtils;
import io.github.gaeqs.javayoutubedownloader.util.HTMLUtils;
import io.github.gaeqs.javayoutubedownloader.util.IdExtractor;
import io.github.gaeqs.javayoutubedownloader.util.PlayerResponseUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a decoder that uses the Youtube's embedded API to decode stream options.
 * Protected videos usually have their embedded API disabled, so if you use this decoder with a protected video
 * a exception will probably be thrown.
 * <p>
 * The embedded protocol bypasses age restrictions and, if the video is compatible, it gives more
 * options than the {@link HTMLDecoder}.
 * <p>
 * Its default name in the {@link DecoderManager} is "embedded".
 */
public class EmbeddedDecoder implements Decoder {

    public static final String DEFAULT_GET_VIDEO_URL = "https://www.youtube.com/get_video_info?video_id=%s";
    public static final String TITLE_PARAMETER = "title";
    public static final String AUTHOR_PARAMETER = "author";
    public static final String MUXED_STREAM_LIST_PARAMETER = "url_encoded_fmt_stream_map";
    public static final String ADAPTIVE_STREAM_LIST_PARAMETER = "adaptive_fmts";

    public static final String PLAYER_RESPONSE_LIST_PARAMETER = "player_response";

    private String urlEncoding;
    private String getVideoUrl;

    public EmbeddedDecoder(String urlEncoding) {
        this.urlEncoding = urlEncoding;
        this.getVideoUrl = DEFAULT_GET_VIDEO_URL;
    }

    public EmbeddedDecoder(String urlEncoding, String getVideoUrl) {
        this.urlEncoding = urlEncoding;
        this.getVideoUrl = getVideoUrl;
    }

    public String getUrlEncoding() {
        return urlEncoding;
    }

    public void setUrlEncoding(String urlEncoding) {
        this.urlEncoding = urlEncoding;
    }

    public String getGetVideoUrl() {
        return getVideoUrl;
    }

    public void setGetVideoUrl(String getVideoUrl) {
        this.getVideoUrl = getVideoUrl;
    }

    public YoutubeVideo extractVideo(URL url) throws IOException {
        URL embeddedUrl = new URL(String.format(getVideoUrl, IdExtractor.extractId(url.toExternalForm())));
        String query = HTMLUtils.readAll(embeddedUrl);
        Map<String, String> queryData = getQueryMap(query);
        checkExceptions(queryData);

        String title = queryData.containsKey(TITLE_PARAMETER) ? decode(queryData.get(TITLE_PARAMETER)) : "null";
        String author = queryData.containsKey(AUTHOR_PARAMETER) ? decode(queryData.get(AUTHOR_PARAMETER)) : "null";
        YoutubeVideo video = new YoutubeVideo(title, author);

        List<EncodedStream> encodedStreams = new LinkedList<>();

        if (queryData.containsKey(PLAYER_RESPONSE_LIST_PARAMETER)) {
            PlayerResponseUtils.addPlayerResponseStreams(decode(queryData.get(PLAYER_RESPONSE_LIST_PARAMETER)),
                    encodedStreams, urlEncoding);
        }

        //Muxed stream data.
        if (queryData.containsKey(MUXED_STREAM_LIST_PARAMETER)) {
            String encodedMuxedStreamList = decode(queryData.get(MUXED_STREAM_LIST_PARAMETER));
            EncodedStreamUtils.addEncodedStreams(encodedMuxedStreamList, encodedStreams, urlEncoding);
        }

        //Adaptive stream data.
        if (queryData.containsKey(ADAPTIVE_STREAM_LIST_PARAMETER)) {
            String encodedAdaptiveStreamList = decode(queryData.get(ADAPTIVE_STREAM_LIST_PARAMETER));
            EncodedStreamUtils.addEncodedStreams(encodedAdaptiveStreamList, encodedStreams, urlEncoding);
        }

        encodedStreams.removeIf(target -> !target.decode(null, true));
        encodedStreams.forEach(target -> video.getStreamOptions().add(target.getDecodedStream()));
        return video;
    }

    private Map<String, String> getQueryMap(String query) {
        HashMap<String, String> map = new HashMap<>();
        query = query.trim();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            map.put(decode(pair.substring(0, idx)), pair.substring(idx + 1));
        }
        return map;
    }

    private String checkExceptions(Map<String, String> queryMap) {
        String status = queryMap.get("status");
        if (status.equals("fail")) {
            String error = queryMap.get("errorcode");
            String reason = queryMap.get("reason");
            if (error.equals("150"))
                throw new EmbeddedExtractionException("Embedding is disabled. Error code " + error + ". Reason: " + reason);
            if (error.equals("100"))
                throw new EmbeddedExtractionException("Video has been deleted. Error code " + error + ". Reason: " + reason);
            throw new EmbeddedExtractionException("Error code " + error + ". Reason: " + reason);
        }
        return status;
    }

    private String decode(String string) {
        try {
            return URLDecoder.decode(string, urlEncoding);
        } catch (UnsupportedEncodingException | NullPointerException e) {
            System.err.println("Error while decoding string " + string);
            e.printStackTrace();
            return string;
        }
    }
}
