package com.degoos.javayoutubedownloader.stream;

import com.degoos.javayoutubedownloader.util.Validate;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Youtube video. An instance of this class contains the title, the author and all available streams
 * of the represented video.
 * {@link com.degoos.javayoutubedownloader.decoder.Decoder}s return an instance of this class with all information you need.
 *
 * @see com.degoos.javayoutubedownloader.decoder.Decoder
 */
public class YoutubeVideo {

	private String title;
	private String author;
	private List<StreamOption> streamOptions;

	/**
	 * Creates a video using the title and the author. The stream list will be empty.
	 *
	 * @param title  the title.
	 * @param author the author.
	 */
	public YoutubeVideo(String title, String author) {
		this(title, author, null);
	}

	/**
	 * Creates a video using the title, the author and the stream options. If the stream list is
	 * {@code null} an empty {@link LinkedList} will be created.
	 *
	 * @param title         the title.
	 * @param author        the author.
	 * @param streamOptions the stream list.
	 */
	public YoutubeVideo(String title, String author, List<StreamOption> streamOptions) {
		Validate.notNull(title, "Title cannot be null!");
		this.title = title;
		this.author = author;
		this.streamOptions = streamOptions == null ? new LinkedList<>() : streamOptions;
	}

	/**
	 * Returns the title of the video.
	 *
	 * @return the title of the video.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the author of the video, or {@link Optional#empty()} if not present.
	 * Embedded decoders can give the author name, while HTML decoders cannot.
	 *
	 * @return the author of the video.
	 */
	public Optional<String> getAuthor() {
		return Optional.ofNullable(author);
	}

	/**
	 * Returns a mutable {@link List} with all available {@link StreamOption} of this video.
	 *
	 * @return the mutable {@link List}.
	 * @see StreamOption
	 */
	public List<StreamOption> getStreamOptions() {
		return streamOptions;
	}

	/**
	 * Adds all stream options of the given YoutubeVideo to this.
	 * This method is used by multi-decoder extractions.
	 *
	 * @param video the given video.
	 */
	public void merge(YoutubeVideo video) {
		streamOptions.addAll(video.streamOptions);
	}
}
