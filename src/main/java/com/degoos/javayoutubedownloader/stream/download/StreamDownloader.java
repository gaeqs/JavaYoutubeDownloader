package com.degoos.javayoutubedownloader.stream.download;

import com.degoos.javayoutubedownloader.exception.DownloadException;
import com.degoos.javayoutubedownloader.stream.StreamOption;
import com.degoos.javayoutubedownloader.util.HTMLUtils;
import com.degoos.javayoutubedownloader.util.Validate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

/**
 * A StreamDownloader downloads the given {@link StreamOption} and stores it into the given {@link File}.
 * A {@link StreamDownloaderNotifier} can be given to know the status of the download.
 * <p>
 * As it implements the {@link Runnable} interface, the StreamDownloader can be used easily in threads.
 */
public class StreamDownloader implements Runnable {

	private static final int BUFFER_SIZE = 1024 << 2;

	private StreamOption option;
	private File target;
	private StreamDownloaderNotifier notifier;

	private int length, count;
	private DownloadStatus status;

	public StreamDownloader(StreamOption option, File target, StreamDownloaderNotifier notifier) {
		Validate.notNull(option, "Option cannot be null!");
		Validate.notNull(target, "Target cannot be null!");
		this.option = option;
		this.target = target;
		this.notifier = notifier;
		this.length = 0;
		this.count = 0;
		this.status = DownloadStatus.READY;
	}

	public StreamOption getOption() {
		return option;
	}

	public File getTarget() {
		return target;
	}

	public StreamDownloaderNotifier getNotifier() {
		return notifier;
	}

	public void setNotifier(StreamDownloaderNotifier notifier) {
		this.notifier = notifier;
	}

	public int getLength() {
		return length;
	}

	public int getCount() {
		return count;
	}

	public DownloadStatus getStatus() {
		return status;
	}

	@Override
	public void run() {
		if (status == DownloadStatus.DOWNLOADING) throw new RuntimeException("This downloader is already running!");
		status = DownloadStatus.DOWNLOADING;
		RandomAccessFile randomAccessFile = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) option.getUrl().openConnection();
			connection.setRequestProperty("User-Agent", HTMLUtils.USER_AGENT);
			connection.setDoInput(true);
			if (!target.createNewFile()) throw new DownloadException("File couldn't be created");
			randomAccessFile = new RandomAccessFile(target, "rw");
			byte[] bytes = new byte[BUFFER_SIZE];
			BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
			HTMLUtils.check(connection);

			length = connection.getContentLength();
			count = 0;

			int read;
			if (notifier != null) notifier.onStart(this);
			while ((read = bufferedInputStream.read(bytes)) > 0) {
				randomAccessFile.write(bytes, 0, read);
				count += read;

				if (notifier != null) notifier.onDownload(this);

				if (Thread.interrupted())
					throw new DownloadException("Thread interrupted");
			}
			bufferedInputStream.close();
			if (notifier != null) notifier.onFinish(this);
		} catch (Exception ex) {
			if (notifier != null)
				notifier.onError(this, ex);
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
