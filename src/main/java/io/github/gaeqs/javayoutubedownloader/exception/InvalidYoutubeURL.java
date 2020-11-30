package io.github.gaeqs.javayoutubedownloader.exception;

import java.net.URL;

public class InvalidYoutubeURL extends RuntimeException {


	public InvalidYoutubeURL(String url) {
		super(url + " is an invalid Youtube URL.");
	}
}
