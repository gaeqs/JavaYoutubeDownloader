# JavaYoutubeDownloader
A simple but powerful Youtube Download API for Java.

<h2><b>What is JYD?</b></h2>
JavaYoutubeDownloader is a small and simple Youtube Stream downloader 
that allows you to download or use any video on the platform in a
few lines.

<h2><b>Installation</b></h2>

You can easily install JYD using maven:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.gaeqs</groupId>
        <artifactId>JavaYoutubeDownloader</artifactId>
        <version>LATEST</version>
    </dependency>
</dependencies>
```

<h2><b>Usage</b></h2>

Using JYD is very easy! This is an example of a method that downloads a video and saves the option with
the best video quality into a file:

```java
public static boolean download(String url, File folder) {
	//Extracts and decodes all streams.
	YoutubeVideo video = JavaYoutubeDownloader.decodeOrNull(url, MultipleDecoderMethod.AND, "html", "embedded");
	//Gets the option with the greatest quality that has video and audio.
	StreamOption option = video.getStreamOptions().stream()
		.filter(target -> target.getType().hasVideo() && target.getType().hasAudio())
		.min(Comparator.comparingInt(o -> o.getType().getVideoQuality().ordinal())).orElse(null);
	//If there is no option, returns false.
	if (option == null) return false;
	//Prints the option type.
	System.out.println(option.getType());
	//Creates the file. folder/title.extension
	File file = new File(folder, video.getTitle() + "." + option.getType().getContainer().toString().toLowerCase());
	//Creates the downloader.
	StreamDownloader downloader = new StreamDownloader(option, file, null);
	//Runs the downloader.
	new Thread(downloader).start();
	return true;
}
```
