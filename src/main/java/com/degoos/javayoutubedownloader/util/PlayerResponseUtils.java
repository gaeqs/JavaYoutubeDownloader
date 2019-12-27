package com.degoos.javayoutubedownloader.util;

import com.degoos.javayoutubedownloader.stream.EncodedStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerResponseUtils {

	public static final String STREAMING_DATA_JSON_PARAMETER = "streamingData";
	public static final String FORMATS_JSON_PARAMETER = "formats";

	public static void addPlayerResponseStreams(String json, Collection<EncodedStream> streams, String urlEncoding) {
		//Player response data.
		JSONObject obj = new JSONObject(json);
		if (obj.has(STREAMING_DATA_JSON_PARAMETER)) {
			obj = obj.getJSONObject(STREAMING_DATA_JSON_PARAMETER);
			if (obj.has(FORMATS_JSON_PARAMETER)) {
				addJSONStreams(obj.getJSONArray(FORMATS_JSON_PARAMETER), streams, urlEncoding);
			}

		}
	}


	private static void addJSONStreams(JSONArray array, Collection<EncodedStream> streams, String urlEncoding) {
		array.forEach(target -> {
			if (!(target instanceof JSONObject)) return;
			JSONObject obj = (JSONObject) target;
			try {
				if (obj.has("cipher")) {
					List<EncodedStream> list = new ArrayList<>();
					EncodedStreamUtils.addEncodedStreams(URLDecoder.decode(obj.getString("cipher"), urlEncoding), list, urlEncoding);
					list.forEach(stream -> System.out.println(stream.getUrl() + "\n - " + stream.getSignature().orElse(null)));
					streams.addAll(list);
				}
				if (obj.has("url")) {
					int iTag = obj.getInt("itag");
					String url = URLDecoder.decode(obj.getString("url"), urlEncoding);
					streams.add(new EncodedStream(iTag, url));
				}
			} catch (Exception e) {
				System.err.println(obj);
				System.err.println("Error while parsing url.");
				e.printStackTrace();
			}
		});
	}

}
