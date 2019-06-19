package com.degoos.javayoutubedownloader.exception;

public class HTMLExtractionException extends RuntimeException {

	public HTMLExtractionException(String message) {
		super(message);
	}

	public HTMLExtractionException (Exception ex) {
		super(ex);
	}

}
