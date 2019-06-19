package com.degoos.javayoutubedownloader.stream.download;

/**
 * StreamDownloaderNotifiers are used to collect information from {@link StreamDownloader}s.
 * They're called when the download starts, when a download loop is completed, when the download
 * is completed and when an exception is thrown.
 */
public interface StreamDownloaderNotifier {

	void onStart(StreamDownloader downloader);

	void onDownload(StreamDownloader downloader);

	void onFinish(StreamDownloader downloader);

	void onError(StreamDownloader downloader, Exception ex);

}
