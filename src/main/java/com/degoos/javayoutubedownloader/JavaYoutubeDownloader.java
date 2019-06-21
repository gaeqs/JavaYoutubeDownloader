package com.degoos.javayoutubedownloader;

import com.degoos.javayoutubedownloader.decoder.Decoder;
import com.degoos.javayoutubedownloader.decoder.DecoderManager;
import com.degoos.javayoutubedownloader.decoder.MultipleDecoderMethod;
import com.degoos.javayoutubedownloader.stream.YoutubeVideo;
import com.degoos.javayoutubedownloader.tag.ITagMap;
import com.degoos.javayoutubedownloader.util.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The main class of the API. Here you can access to the {@link ITagMap} or the {@link DecoderManager}, and
 * execute multi-decoder stream extractions.
 */
public class JavaYoutubeDownloader {

	private static DecoderManager decoderManager = new DecoderManager();

	/**
	 * Returns the {@link ITagMap}. This map is used to get the {@link com.degoos.javayoutubedownloader.tag.StreamType}
	 * associated to a given iTag.
	 *
	 * @return the {@link ITagMap}.
	 * @see ITagMap
	 * @see com.degoos.javayoutubedownloader.tag.StreamType
	 */
	public static ITagMap getITagMap() {
		return ITagMap.MAP;
	}

	/**
	 * Returns the {@link DecoderManager}. With it you can get or add {@link Decoder}s.
	 *
	 * @return the {@link DecoderManager}.
	 * @see Decoder
	 * @see DecoderManager
	 */
	public static DecoderManager getDecoderManager() {
		return decoderManager;
	}

	/**
	 * It does the same as {@link #decode(String, MultipleDecoderMethod, String...)}, but it returns {@code null}
	 * if a exception is thrown.
	 *
	 * @param url      the url.
	 * @param method   the method to use.
	 * @param decoders the decoders to use.
	 * @return the video, or null of an exception is thrown.
	 * @see #decode(URL, MultipleDecoderMethod, String...)
	 */
	public static YoutubeVideo decodeOrNull(String url, MultipleDecoderMethod method, String... decoders) {
		try {
			return decode(url, method, decoders);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 *  It does the same as {@link #decode(URL, MultipleDecoderMethod, String...)}, but it returns {@code null}
	 * if a exception is thrown.
	 *
	 * @param url      the url.
	 * @param method   the method to use.
	 * @param decoders the decoders to use.
	 * @return the video, or null of an exception is thrown.
	 * @see #decode(URL, MultipleDecoderMethod, String...)
	 */
	public static YoutubeVideo decodeOrNull(URL url, MultipleDecoderMethod method, String... decoders) {
		try {
			return decode(url, method, decoders);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * It does the same as {@link #decode(URL, MultipleDecoderMethod, String...)}, but it parses the given url
	 * to an {@link URL} instance before.
	 *
	 * @param url      the url.
	 * @param method   the method to use.
	 * @param decoders the decoders to use.
	 * @return the video, or null of an exception is thrown.
	 * @throws MalformedURLException whether the url is malformed.
	 * @see #decode(URL, MultipleDecoderMethod, String...)
	 */
	public static YoutubeVideo decode(String url, MultipleDecoderMethod method, String... decoders) throws MalformedURLException {
		Validate.notNull(url, "url cannot be null!");
		return decode(new URL(url), method, decoders);
	}

	/**
	 * Creates a {@link YoutubeVideo} using several decoders.
	 * <p>
	 * Decoders are given by their name in the {@link DecoderManager}. If a decoder is not found it will be
	 * ignored.
	 * If no decoders are defined in the parameter an {@link IllegalArgumentException} is thrown.
	 * <p>
	 * If none of the decoders are able to create a {@link YoutubeVideo} instance, an {@link IllegalStateException} is thrown.
	 * The {@link YoutubeVideo} instance may exists, even if it has no {@link com.degoos.javayoutubedownloader.stream.StreamOption}s.
	 * This indicates that at least one of the decoders was executed successfully, but it wasn't able to find any stream.
	 * <p>
	 * The given {@link MultipleDecoderMethod} modifies the behaviour of the algorithm. If the {@link MultipleDecoderMethod} is
	 * {@link MultipleDecoderMethod#AND} all decoders will be executed. If the {@link MultipleDecoderMethod} is
	 * {@link MultipleDecoderMethod#OR} the decoders will be executed in order until a non-empty {@link YoutubeVideo}
	 * instance is created.
	 *
	 * @param url      the video's {@link URL}
	 * @param method   the {@link MultipleDecoderMethod}.
	 * @param decoders the {@link Decoder}s.
	 * @return the {@link YoutubeVideo} instance.
	 * @throws IllegalArgumentException if any of the arguments is null or if the decoder list is empty.
	 * @throws IllegalStateException    if none of the decoders was able to create a {@link YoutubeVideo} instance.
	 */
	public static YoutubeVideo decode(URL url, MultipleDecoderMethod method, String... decoders) {
		Validate.notNull(url, "url cannot be null!");
		Validate.notNull(method, "method cannot be null!");
		Validate.notNull(decoders, "decoders cannot be null!");
		if (decoders.length == 0) throw new IllegalArgumentException("There are no decoders defined!");
		YoutubeVideo video = null;
		YoutubeVideo current;

		Optional<Decoder> decoder;
		for (String string : decoders) {
			decoder = decoderManager.getDecoder(string);
			if (!decoder.isPresent()) continue;
			try {
				current = decoder.get().extractVideo(url);
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
			if (video == null) {
				video = current;
			} else {
				video.merge(current);
			}
			if (!video.getStreamOptions().isEmpty() && method == MultipleDecoderMethod.OR) return video;
		}
		if (video == null) throw new IllegalStateException("Couldn't get any video instance");
		return video;
	}
}
