package com.degoos.javayoutubedownloader.util;

import com.degoos.javayoutubedownloader.stream.EncodedStream;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;

public class EncodedStreamUtils {

	/**
	 * Parses an URLEncoded string to {@link EncodedStream}s, and adds them to the given collection.
	 *
	 * @param encodedString the encoded stream.
	 * @param collection    the collection.
	 * @param urlEncoding   the url encoding. UTF-8 is used by default.
	 * @throws UnsupportedEncodingException
	 */
	public static void addEncodedStreams(String encodedString, Collection<EncodedStream> collection, String urlEncoding) throws UnsupportedEncodingException {
		String[] streams = encodedString.trim().split(",");
		String[] pairs;

		int idx;
		String key;

		String url = null;
		String encoding = null;
		int iTag = -1;
		for (String stream : streams) {
			pairs = stream.split("&");
			for (String pair : pairs) {
				idx = pair.indexOf('=');
				if (idx == -1) continue;
				key = URLDecoder.decode(pair.substring(0, idx).toLowerCase(), urlEncoding);
				switch (key) {
					case "url":
						url = URLDecoder.decode(pair.substring(idx + 1), urlEncoding);
						break;
					case "s":
						encoding = URLDecoder.decode(pair.substring(idx + 1), urlEncoding);
						break;
					case "itag":
						key = URLDecoder.decode(pair.substring(idx + 1), urlEncoding);
						if (!NumericUtils.isInteger(key)) continue;
						iTag = Integer.valueOf(key);
						break;
				}
			}
			if (iTag == -1 || url == null) continue;
			collection.add(new EncodedStream(iTag, url, encoding));
		}
	}

}
