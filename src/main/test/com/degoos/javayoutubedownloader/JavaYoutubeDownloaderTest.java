package com.degoos.javayoutubedownloader;

import com.degoos.javayoutubedownloader.decoder.MultipleDecoderMethod;
import com.degoos.javayoutubedownloader.stream.YoutubeVideo;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaYoutubeDownloaderTest {

	@Test
	public void htmlTest() throws MalformedURLException {
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=PNK8TmaRSQY",
				MultipleDecoderMethod.AND, "html");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		System.out.println("URLs:");
		video.getStreamOptions().forEach(target -> System.out.println(target.getUrl()));
	}

	@Test
	public void embeddedTest() throws MalformedURLException {
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=PNK8TmaRSQY",
				MultipleDecoderMethod.AND, "embedded");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getUrl()));
	}

	@Test
	public void htmlTestOldVideo() throws MalformedURLException {
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=l_jUBScR1RA",
				MultipleDecoderMethod.AND, "html");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		System.out.println("URLs:");
		video.getStreamOptions().forEach(target -> System.out.println(target.getUrl()));
	}

	@Test
	public void embeddedTestOldVideo() throws MalformedURLException {
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=l_jUBScR1RA",
				MultipleDecoderMethod.AND, "embedded");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getUrl()));
	}

	@Test
	public void htmlTestProtected() throws MalformedURLException {
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=Nx-DvH41Tjo",
				MultipleDecoderMethod.AND, "html");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getUrl()));
	}

	@Test
	public void embeddedTestProtected() throws MalformedURLException {
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=Nx-DvH41Tjo",
				MultipleDecoderMethod.AND, "embedded");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getUrl()));
	}

}