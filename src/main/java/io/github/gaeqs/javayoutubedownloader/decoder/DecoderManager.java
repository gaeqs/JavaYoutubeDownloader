package io.github.gaeqs.javayoutubedownloader.decoder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Represents a decoder manager. This class is used to store all decoders implemented in the API.
 * Default decoders (HTML and Embedded) are automatically added when a instance is created.
 * To add your own decoders you can use the method {@link #addDecoder(String, Decoder)}.
 *
 * @see Decoder
 */
public class DecoderManager {

	private static final String DEFAULT_ENCODING = "UTF-8";

	private Map<String, Decoder> decoders;

	public DecoderManager() {
		decoders = new ConcurrentHashMap<>();
		loadDefaults();
	}

	private void loadDefaults() {
		decoders.put("html", new HTMLDecoder(DEFAULT_ENCODING));
		decoders.put("embedded", new EmbeddedDecoder(DEFAULT_ENCODING));
	}

	/**
	 * Returns the decoder associated to the given name.
	 *
	 * @param name the name of the decoder.
	 * @return the decoder, of {@link Optional#empty()} if not present.
	 */
	public Optional<Decoder> getDecoder(String name) {
		return Optional.ofNullable(decoders.get(name));
	}

	/**
	 * Adds a decoder to the manager.
	 *
	 * @param name    the decoder's name.
	 * @param decoder the decoder.
	 */
	public void addDecoder(String name, Decoder decoder) {
		decoders.put(name, decoder);
	}

	/**
	 * Returns a mutable {@link Map} with all decoders and their names.
	 *
	 * @return the map.
	 */
	public Map<String, Decoder> getDecoders() {
		return decoders;
	}
}
