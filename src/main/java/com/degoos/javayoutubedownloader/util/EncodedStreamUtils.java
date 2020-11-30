package com.degoos.javayoutubedownloader.util;

import com.alibaba.fastjson.JSONObject;
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
	 * @throws UnsupportedEncodingException whether the encoding is not supported.
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

	public static void addEncodedStreams(JSONObject json, Collection<EncodedStream> collection, String urlEnconding)
			throws UnsupportedEncodingException {

		int iTag = json.getInteger("itag");

		if (json.containsKey("signatureCipher")) {
			String cipher = json.getString("signatureCipher").replace("\\u0026", "&");
			String[] pairs = cipher.split("&");

			String encodedUrl = null;
			String signature = null;

			int equalsIndex;
			String key, value;
			for (String pair : pairs) {
				equalsIndex = pair.indexOf('=');
				key = pair.substring(0, equalsIndex);
				value = pair.substring(equalsIndex + 1);

				if (key.equals("url")) {
					encodedUrl = value;
				} else if (key.equals("s")) {
					signature = value;
				}
			}

			if (encodedUrl == null) {
				System.err.println("Encoded URL is null.");
				return;
			}
			encodedUrl = URLDecoder.decode(encodedUrl, urlEnconding);
			if (!encodedUrl.contains("signature") && !encodedUrl.contains("&sig=") && !encodedUrl.contains("&lsign=")) {
				if (signature != null) {
					signature = URLDecoder.decode(signature, urlEnconding);
				}
				collection.add(new EncodedStream(iTag, encodedUrl, signature));
			} else {
				collection.add(new EncodedStream(iTag, encodedUrl));
			}
		} else {
			if (!json.containsKey("url")) return;
			collection.add(new EncodedStream(iTag, json.getString("url")));
		}
	}

}
