package io.github.gaeqs.javayoutubedownloader.exception;

public class InvalidYoutubeURL extends RuntimeException {


    public InvalidYoutubeURL(String url) {
        super(url + " is an invalid Youtube URL.");
    }
}
