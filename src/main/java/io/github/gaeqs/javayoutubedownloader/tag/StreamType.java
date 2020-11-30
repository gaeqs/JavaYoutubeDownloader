package io.github.gaeqs.javayoutubedownloader.tag;

/**
 * Represents a stream type. Each stream has its properties, and they're represented in this class.
 */
public class StreamType {

	private Container container;
	private Encoding videoEncoding, audioEncoding;
	private VideoQuality videoQuality;
	private AudioQuality audioQuality;
	private FormatNote formatNote;
	private FPS fps;

	public StreamType(Container container, Encoding videoEncoding, VideoQuality videoQuality, Encoding audioEncoding, AudioQuality audioQuality, FormatNote formatNote) {
		this.container = container;
		this.videoEncoding = videoEncoding;
		this.videoQuality = videoQuality;
		this.audioEncoding = audioEncoding;
		this.audioQuality = audioQuality;
		this.formatNote = formatNote;
		this.fps = FPS.f30;
	}

	public StreamType(Container container, Encoding videoEncoding, VideoQuality videoQuality, FormatNote formatNote) {
		this.container = container;
		this.videoEncoding = videoEncoding;
		this.videoQuality = videoQuality;
		this.audioEncoding = null;
		this.audioQuality = null;
		this.formatNote = formatNote;
		this.fps = FPS.f30;
	}


	public StreamType(Container container, Encoding audioEncoding, AudioQuality audioQuality, FormatNote formatNote) {
		this.container = container;
		this.audioEncoding = audioEncoding;
		this.audioQuality = audioQuality;
		this.videoEncoding = null;
		this.videoQuality = null;
		this.formatNote = formatNote;
		this.fps = FPS.f30;
	}

	public StreamType(Container container, Encoding videoEncoding, VideoQuality videoQuality, Encoding audioEncoding, AudioQuality audioQuality, FormatNote formatNote, FPS fps) {
		this.container = container;
		this.videoEncoding = videoEncoding;
		this.videoQuality = videoQuality;
		this.audioEncoding = audioEncoding;
		this.audioQuality = audioQuality;
		this.formatNote = formatNote;
		this.fps = fps;
	}

	public StreamType(Container container, Encoding videoEncoding, VideoQuality videoQuality, FormatNote formatNote, FPS fps) {
		this.container = container;
		this.videoEncoding = videoEncoding;
		this.videoQuality = videoQuality;
		this.audioEncoding = null;
		this.audioQuality = null;
		this.formatNote = formatNote;
		this.fps = fps;
	}


	public StreamType(Container container, Encoding audioEncoding, AudioQuality audioQuality, FormatNote formatNote, FPS fps) {
		this.container = container;
		this.audioEncoding = audioEncoding;
		this.audioQuality = audioQuality;
		this.videoEncoding = null;
		this.videoQuality = null;
		this.formatNote = formatNote;
		this.fps = fps;
	}

	/**
	 * Returns the container of the stream.
	 *
	 * @return the container.
	 * @see Container
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * Returns the video encoding of the stream.
	 * It may be null if the stream doesn't have a video channel.
	 *
	 * @return the video encoding.
	 * @see Encoding
	 */
	public Encoding getVideoEncoding() {
		return videoEncoding;
	}

	/**
	 * Returns the audio encoding of the stream.
	 * It may be {@code null} if the stream doesn't have an audio channel.
	 *
	 * @return the audio encoding.
	 * @see Encoding
	 */
	public Encoding getAudioEncoding() {
		return audioEncoding;
	}

	/**
	 * Returns the video quality of the stream.
	 * It may be {@code null} if the stream doesn't have a video channel.
	 *
	 * @return the video quality.
	 * @see VideoQuality
	 */
	public VideoQuality getVideoQuality() {
		return videoQuality;
	}

	/**
	 * Returns the audio quality of the stream.
	 * It may be {@code null} if the stream doesn't have an audio channel.
	 *
	 * @return the audio quality.
	 * @see AudioQuality
	 */
	public AudioQuality getAudioQuality() {
		return audioQuality;
	}

	/**
	 * Returns the format note of the stream. It may be {@code null}.
	 *
	 * @return the format note.
	 * @see FormatNote
	 */
	public FormatNote getFormatNote() {
		return formatNote;
	}

	/**
	 * Returns the frames per second of the stream.
	 *
	 * @return the frames per second.
	 * @see FPS
	 */
	public FPS getFps() {
		return fps;
	}

	/**
	 * Returns whether the stream has a video channel.
	 *
	 * @return whether the stream has a video channel.
	 */
	public boolean hasVideo() {
		return videoQuality != null && videoEncoding != null;
	}

	/**
	 * Returns whether the stream has an audio channel.
	 *
	 * @return whether the stream has an audio channel.
	 */
	public boolean hasAudio() {
		return audioQuality != null && audioEncoding != null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[Video: " + hasVideo() + ", Audio: " + hasAudio() + ", Container: " + container);
		if (hasVideo())
			builder.append(", VEncoding: ").append(videoEncoding).append(", VQuality: ").append(videoQuality);
		if (hasAudio())
			builder.append(", AEncoding: ").append(audioEncoding).append(", AQuality: ").append(audioQuality);
		builder.append(", Format Note: ").append(formatNote).append(", FPS: ").append(fps).append("]");
		return builder.toString();
	}
}
