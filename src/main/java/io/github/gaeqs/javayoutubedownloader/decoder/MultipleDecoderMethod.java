package io.github.gaeqs.javayoutubedownloader.decoder;

import io.github.gaeqs.javayoutubedownloader.JavaYoutubeDownloader;

/**
 * This enum is used in the method {@link JavaYoutubeDownloader#decode(String, MultipleDecoderMethod, String...)}.
 * If you use the option {@link #AND} all decoders will be executed, adding all their results to a common list.
 * If you use the option {@link #OR} the method will only return the first non-empty result of the multi-decoder.
 */
public enum MultipleDecoderMethod {

    OR, AND

}
