package io.github.gaeqs.javayoutubedownloader.stream;

import io.github.gaeqs.javayoutubedownloader.JavaYoutubeDownloader;
import io.github.gaeqs.javayoutubedownloader.decrypt.Decrypt;
import io.github.gaeqs.javayoutubedownloader.decrypt.HTML5SignatureDecrypt;
import io.github.gaeqs.javayoutubedownloader.exception.StreamEncodedException;
import io.github.gaeqs.javayoutubedownloader.util.Validate;
import io.github.gaeqs.javayoutubedownloader.tag.ITagMap;

import java.net.URL;
import java.util.Optional;

/**
 * Represents a {@link StreamOption} that is not decoded. You can created the decoded {@link StreamOption}
 * using the method {@link #decode(String, boolean)}.
 * The iTag and the url cannot be null.
 */
public class EncodedStream {

	private static final String DEFAULT_JS_SCRIPT = "https://youtube.com/yts/jsbin/player_ias-vflEO2H8R/en_US/base.js";
	private static final String SIGNATURE_PARAMETER = "&sig=";

	private static final Decrypt DECRYPT = new HTML5SignatureDecrypt();

	private int iTag;
	private String url;
	private String signature;

	private StreamOption decodedStream;

	/**
	 * Creates an EncodedStream using an iTag and an url.
	 *
	 * @param iTag the iTag.
	 * @param url  the url.
	 */
	public EncodedStream(int iTag, String url) {
		this(iTag, url, null);
	}

	/**
	 * Creates an EncodedStream using an iTag, an url and a signature, or null.
	 *
	 * @param iTag      the iTag.
	 * @param url       the url.
	 * @param signature the signature code, or null.
	 */
	public EncodedStream(int iTag, String url, String signature) {
		Validate.notNull(iTag, "iTag cannot be null!");
		Validate.notNull(url, "url cannot be null!");
		this.iTag = iTag;
		this.url = url;
		this.signature = signature;
		this.decodedStream = null;
	}

	/**
	 * Returns the iTag of the stream. You can receive more data from the iTag using {@link JavaYoutubeDownloader#getITagMap()}
	 * and {@link ITagMap#get(Object)}.
	 *
	 * @return the iTag.
	 */
	public int getiTag() {
		return iTag;
	}

	/**
	 * Returns the base url of the stream. This url may vary from the decoded version,
	 * as the decoded one may have the signature parameter.
	 *
	 * @return the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the encrypted signature code, if present. The signature code is decrypted when the method
	 * {@link #decode(String, boolean)} is executed.
	 *
	 * @return the encrypted signature code.
	 */
	public Optional<String> getSignature() {
		return Optional.ofNullable(signature);
	}

	/**
	 * Returns whether this encoded stream has a signature code.
	 *
	 * @return whether this encoded stream has a signature code.
	 */
	public boolean hasSignature() {
		return signature != null;
	}

	/**
	 * Returns the decoded stream if the method {@link #decode(String, boolean)} was previously executed, or
	 * throws a {@link StreamEncodedException} if no decoded stream is found.
	 *
	 * @return the decoded stream.
	 * @throws StreamEncodedException if no decoded stream is found. Use the method {@link #decode(String, boolean)}
	 *                                to generate one.
	 */
	public StreamOption getDecodedStream() {
		if (decodedStream == null) throw new StreamEncodedException();
		return decodedStream;
	}

	/**
	 * Generates a {@link StreamOption} decoding the data of this EncodedStream. A decode may fail, so
	 * the method returns whether the process was successful.
	 *
	 * @param jsUrl           the youtube JS file url, or null if you don't have one. This is used to decrypt the
	 *                        signature using the online algorithm.
	 * @param checkConnection whether the method will check if the decoded URL is accessible. If it's not the created
	 *                        decoded stream will be deleted, and this method will return false.
	 * @return whether the decode was successful.
	 */
	public boolean decode(String jsUrl, boolean checkConnection) {
		if (decodedStream != null) return true;
		boolean created;
		if (!hasSignature()) {
			created = decodeSimple();
		} else {
			created = decodeComplex(jsUrl);
		}
		if (!created) return false;
		if (!checkConnection) return true;
		if (!decodedStream.checkConnection()) {
			System.out.println("Error checking connection! (Signature not working)\n" + decodedStream.getUrl());
			decodedStream = null;
			return false;
		}
		return true;
	}

	private boolean decodeSimple() {
		try {
			decodedStream = new StreamOption(new URL(url), JavaYoutubeDownloader.getITagMap().get(iTag));
			return true;
		} catch (IllegalArgumentException ex) {
			if (JavaYoutubeDownloader.getITagMap().get(iTag) == null) {
				if (iTag > 393 && iTag <= 399) return false; //Unknown streams.
				System.err.println("Couldn't find the StreamType for the iTag " + iTag);
			} else ex.printStackTrace();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private boolean decodeComplex(String jsUrl) {
		String decryptedSignature = DECRYPT.decrypt(jsUrl == null ? DEFAULT_JS_SCRIPT : jsUrl, signature);
		try {
			decodedStream = new StreamOption(new URL(url + SIGNATURE_PARAMETER + decryptedSignature),
					JavaYoutubeDownloader.getITagMap().get(iTag));
		} catch (IllegalArgumentException ex) {
			if (JavaYoutubeDownloader.getITagMap().get(iTag) == null) {
				if (iTag > 393 && iTag <= 399) return false; //Unknown streams.
				System.err.println("Couldn't find the StreamType for the iTag " + iTag);
			} else ex.printStackTrace();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
