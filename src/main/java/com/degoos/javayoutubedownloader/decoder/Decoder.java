package com.degoos.javayoutubedownloader.decoder;

import com.degoos.javayoutubedownloader.JavaYoutubeDownloader;
import com.degoos.javayoutubedownloader.exception.HTMLExtractionException;
import com.degoos.javayoutubedownloader.stream.YoutubeVideo;

import java.io.IOException;
import java.net.URL;

/**
 * Represents a decoder. The decoder allows to extract all stream options from a URL using it's own decode algorithm.
 * Default decoders:
 *
 * @see EmbeddedDecoder
 * @see HTMLDecoder
 * <p>
 * You can access to the default instance of a decoder using {@link JavaYoutubeDownloader#getDecoderManager()}.
 */
public interface Decoder {

	/**
	 * Extracts and decodes all {@link com.degoos.javayoutubedownloader.stream.StreamOption}s for the given URL.
	 *
	 * @param url the url.
	 * @return the decoded {@link YoutubeVideo} with all the {@link com.degoos.javayoutubedownloader.stream.StreamOption} inside.
	 * @throws IOException whether any IO exception occurs.
	 * @throws HTMLExtractionException whether the decoder is using an HTML algorithm and an exception is thrown.
	 */
	YoutubeVideo extractVideo(URL url) throws IOException;

}
