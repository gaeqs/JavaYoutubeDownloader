package com.degoos.javayoutubedownloader.decoder;

import com.degoos.javayoutubedownloader.exception.HTMLExtractionException;
import com.degoos.javayoutubedownloader.stream.EncodedStream;
import com.degoos.javayoutubedownloader.stream.YoutubeVideo;
import com.degoos.javayoutubedownloader.util.EncodedStreamUtils;
import com.degoos.javayoutubedownloader.util.HTMLUtils;
import com.degoos.javayoutubedownloader.util.PlayerResponseUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
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

	private static final Pattern AGE_PATTERN = Pattern.compile("(verify_age)");
	private static final Pattern UNAVAILABLE_PLAYER_PATTERN = Pattern.compile("(unavailable-player)");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("jsbin\\\\/(player(_ias)?-(.+?).js)");
	private static final Pattern TITLE_PATTERN = Pattern.compile("<meta name=\"title\" content=(.*)");
	private static final Pattern MUXED_STREAM_LIST_PATTERN = Pattern.compile("\"url_encoded_fmt_stream_map\":\"([^\"]*)\"");
	private static final Pattern ADAPTIVE_STREAM_LIST_PATTERN = Pattern.compile("\"adaptive_fmts\":\\s*\"([^\"]*)\"");

	private static final String JS_FILE = "https://s.ytimg.com/yts/jsbin/";

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
		checkAge(html);
		checkPlayer(html);

		String title = getTitle(html, url);
		URI player = getHTML5PlayerUri(html);

		String encodedMuxedStreamList = sanityStreamList(fastMatcher(html, MUXED_STREAM_LIST_PATTERN, 1));
		String encodedAdaptiveStreamList = sanityStreamList(fastMatcher(html, ADAPTIVE_STREAM_LIST_PATTERN, 1));
		YoutubeVideo video = new YoutubeVideo(title, null);
		List<EncodedStream> encodedStreams = new LinkedList<>();


		if (html.contains("\"player_response\"")) {
			String player_response = html.substring(html.indexOf("\"player_response\""));
			player_response = player_response.substring(0, player_response.indexOf("}\",")) + "}";
			player_response = player_response.replaceFirst("\"player_response\":\"", "");
			player_response = player_response.replace("\\/", "/")
					.replace("\\\"", "\"")
					.replace("\\\\", "\\");
			PlayerResponseUtils.addPlayerResponseStreams(player_response, encodedStreams, urlEncoding);
		}

		if (encodedMuxedStreamList != null)
			EncodedStreamUtils.addEncodedStreams(encodedMuxedStreamList, encodedStreams, urlEncoding);
		if (encodedAdaptiveStreamList != null)
			EncodedStreamUtils.addEncodedStreams(encodedAdaptiveStreamList, encodedStreams, urlEncoding);

		System.out.println("Before filter: " + encodedStreams.size());
		encodedStreams.removeIf(target -> !target.decode(player, true));
		System.out.println("After filter: " + encodedStreams.size());
		encodedStreams.forEach(target -> video.getStreamOptions().add(target.getDecodedStream()));
		return video;
	}

	public void checkAge(String html) {
		if (AGE_PATTERN.matcher(html).find())
			throw new HTMLExtractionException("Age restriction");
	}

	public void checkPlayer(String html) {
		if (UNAVAILABLE_PLAYER_PATTERN.matcher(html).find())
			throw new HTMLExtractionException("Unavailable player");
	}

	public URI getHTML5PlayerUri(String html) {
		Matcher playerVersionMatch = PLAYER_PATTERN.matcher(html);
		if (playerVersionMatch.find()) {
			try {
				String data = playerVersionMatch.group(1).replace("\\/", "/");
				return new URI(JS_FILE + data);
			} catch (Exception ex) {
				throw new HTMLExtractionException(ex);
			}
		}
		return null;
	}

	private String getTitle(String html, URL url) {
		Matcher titleMatch = TITLE_PATTERN.matcher(html);
		if (titleMatch.find()) {
			String sline = titleMatch.group(1);
			String name = sline.replaceFirst("<meta name=\"title\" content=", "").trim();
			return name.substring(1, name.length() - 2);
		}
		return url.toString();
	}

	private String fastMatcher(String string, Pattern pattern, int group) {
		Matcher matcher = pattern.matcher(string);
		if (matcher.find())
			return matcher.group(group);
		return null;
	}

	private String sanityStreamList(String list) {
		return list == null ? null : list.replace("\\u0026", "&");
	}
}
