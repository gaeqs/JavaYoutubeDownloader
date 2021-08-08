package io.github.gaeqs.javayoutubedownloader.util;

import io.github.gaeqs.javayoutubedownloader.exception.InvalidYoutubeURL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdExtractor {

    private static final Pattern NORMAL_PATTERN = Pattern.compile("youtube.com/watch?.*v=([^&]*)");
    private static final Pattern SHORTENED_PATTERN = Pattern.compile("youtube.com/v/([^&]*)");

    /**
     * Extracts the id of a youtube video by its url.
     *
     * @param url the url.
     * @return the id.
     * @throws InvalidYoutubeURL whether the url is invalid.
     */
    public static String extractId(String url) {
        Matcher um = NORMAL_PATTERN.matcher(url);
        if (um.find())
            return um.group(1);
        um = SHORTENED_PATTERN.matcher(url);
        if (um.find())
            return um.group(1);
        throw new InvalidYoutubeURL(url);
    }

}
