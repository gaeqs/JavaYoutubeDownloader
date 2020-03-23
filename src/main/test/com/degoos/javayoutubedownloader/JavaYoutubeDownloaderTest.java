package com.degoos.javayoutubedownloader;

import com.degoos.javayoutubedownloader.decoder.MultipleDecoderMethod;
import com.degoos.javayoutubedownloader.stream.YoutubeVideo;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaYoutubeDownloaderTest {

	private static final int SLEEP = 0;

	@Test
	public void htmlTest() throws MalformedURLException, InterruptedException {
		Thread.sleep(SLEEP);
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=PNK8TmaRSQY",
				MultipleDecoderMethod.AND, "html");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		System.out.println("URLs:");
		video.getStreamOptions().forEach(target -> System.out.println(target.getType() + "\n" + target.getUrl()));
	}

	@Test
	public void embeddedTest() throws MalformedURLException, InterruptedException {
		Thread.sleep(SLEEP);
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=PNK8TmaRSQY",
				MultipleDecoderMethod.AND, "embedded");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getType() + "\n" + target.getUrl()));
	}

	@Test
	public void htmlTestOldVideo() throws MalformedURLException, InterruptedException {
		Thread.sleep(SLEEP);
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=l_jUBScR1RA",
				MultipleDecoderMethod.AND, "html");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		System.out.println("URLs:");
		video.getStreamOptions().forEach(target -> System.out.println(target.getType() + "\n" + target.getUrl()));
	}

	@Test
	public void embeddedTestOldVideo() throws MalformedURLException, InterruptedException {
		Thread.sleep(SLEEP);
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=l_jUBScR1RA",
				MultipleDecoderMethod.AND, "embedded");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getType() + "\n" + target.getUrl()));
	}

	@Test
	public void htmlTestProtected() throws MalformedURLException, InterruptedException {
		Thread.sleep(SLEEP);
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=kJQP7kiw5Fk",
				MultipleDecoderMethod.AND, "html");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getType() + "\n" + target.getUrl()));
	}

	@Test
	public void embeddedTestProtected() throws MalformedURLException, InterruptedException {
		Thread.sleep(SLEEP);
		YoutubeVideo video = JavaYoutubeDownloader.decode("https://www.youtube.com/watch?v=Nx-DvH41Tjo",
				MultipleDecoderMethod.AND, "embedded");
		assertNotNull(video, "Video is null.");
		assertFalse(video.getStreamOptions().isEmpty(), "Video options list is empty.");
		video.getStreamOptions().forEach(target -> System.out.println(target.getType() + "\n" + target.getUrl()));
	}

}