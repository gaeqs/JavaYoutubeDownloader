package com.degoos.javayoutubedownloader.decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.degoos.javayoutubedownloader.stream.EncodedStream;
import com.degoos.javayoutubedownloader.stream.YoutubeVideo;
import com.degoos.javayoutubedownloader.util.EncodedStreamUtils;
import com.degoos.javayoutubedownloader.util.HTMLUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a decoder that uses the HTML5 web of youtube to decode stream options. This decoder is the most
 * safe to use, giving solid results.
 * <p>
 * This protocol won't work if the video has an age restriction, or if it's not accessible in the country
 * of the running machine.
 * <p>
 * Its default name in the {@link DecoderManager} is "html".
 */
public class HTMLDecoder implements Decoder {

	private static final Pattern YT_PLAYER_CONFIG = Pattern.compile(";ytplayer\\.config = (\\{.*?});");

	private static final Pattern MUXED_STREAM_LIST_PATTERN = Pattern.compile("\"url_encoded_fmt_stream_map\":\"([^\"]*)\"");
	private static final Pattern ADAPTIVE_STREAM_LIST_PATTERN = Pattern.compile("\"adaptive_fmts\":\\s*\"([^\"]*)\"");

	private String urlEncoding;

	public HTMLDecoder(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

	public String getUrlEncoding() {
		return urlEncoding;
	}

	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

	@Override
	public YoutubeVideo extractVideo(URL url) throws IOException {
		String html = HTMLUtils.readAll(url);

		Matcher matcher = YT_PLAYER_CONFIG.matcher(html);
		if (!matcher.find()) {
			throw new NullPointerException("Player config not found!");
		}
		String rawConfig = matcher.group(1);
		JSONObject config = JSON.parseObject(rawConfig);
		JSONObject args = config.getJSONObject("args");
		JSONObject response = args.getJSONObject("player_response");

		JSONObject details = response.getJSONObject("videoDetails");
		JSONObject streamingData = response.getJSONObject("streamingData");

		String jsUrl = "https://youtube.com" + config.getJSONObject("assets").getString("js");


		Set<EncodedStream> encodedStreams = new HashSet<>();

		if (streamingData.containsKey("formats")) {
			streamingData.getJSONArray("formats").forEach(o -> parseFormat(o, encodedStreams));
		}
		if (streamingData.containsKey("adaptiveFormats")) {
			streamingData.getJSONArray("adaptiveFormats").forEach(o -> parseFormat(o, encodedStreams));
		}

		YoutubeVideo video = new YoutubeVideo(details.getString("title"), details.getString("author"), null);

		encodedStreams.removeIf(target -> !target.decode(jsUrl, false));
		encodedStreams.forEach(target -> video.getStreamOptions().add(target.getDecodedStream()));

		return video;
	}

	private void parseFormat(Object object, Collection<EncodedStream> collection) {
		if (object instanceof JSONObject) {
			try {
				EncodedStreamUtils.addEncodedStreams((JSONObject) object, collection, urlEncoding);
			} catch (UnsupportedEncodingException e) {
				System.err.println("Error while parsing URL.");
				e.printStackTrace();
			}
		}
	}
}
