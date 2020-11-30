package io.github.gaeqs.javayoutubedownloader.decoder;

import io.github.gaeqs.javayoutubedownloader.JavaYoutubeDownloader;
import io.github.gaeqs.javayoutubedownloader.exception.HTMLExtractionException;
import io.github.gaeqs.javayoutubedownloader.stream.YoutubeVideo;
import io.github.gaeqs.javayoutubedownloader.stream.StreamOption;

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
	 * Extracts and decodes all {@link StreamOption}s for the given URL.
	 *
	 * @param url the url.
	 * @return the decoded {@link YoutubeVideo} with all the {@link StreamOption} inside.
	 * @throws IOException whether any IO exception occurs.
	 * @throws HTMLExtractionException whether the decoder is using an HTML algorithm and an exception is thrown.
	 */
	YoutubeVideo extractVideo(URL url) throws IOException;

}
