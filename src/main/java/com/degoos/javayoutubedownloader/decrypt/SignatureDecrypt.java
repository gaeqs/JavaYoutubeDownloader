package com.degoos.javayoutubedownloader.decrypt;

//LAST VERSION 19/6/19

import java.net.URI;

/**
 * This decrypt algorithm is an offline version of the {@link HTML5SignatureDecrypt}'s algorithm.
 * Is suitable to changes, so the {@link com.degoos.javayoutubedownloader.stream.EncodedStream#decode(URI, boolean)} method
 * tries first the {@link HTML5SignatureDecrypt}'s algorithm.
 */
public class SignatureDecrypt implements Decrypt {

	public char[] sig;

	public SignatureDecrypt(String signature) {
		this.sig = signature.toCharArray();
	}


	//Reverses the array.
	public void h1() {
		char t;
		for (int i = 0; i < sig.length / 2; i++) {
			t = sig[i];
			sig[i] = sig[sig.length - i - 1];
			sig[sig.length - i - 1] = t;
		}
	}

	public void s3(int i) {
		char[] array = new char[sig.length - i];
		System.arraycopy(sig, i, array, 0, sig.length - i);
		sig = array;
	}

	private void sd(int i) {
		char c = sig[0];
		sig[0] = sig[i % sig.length];
		sig[i % sig.length] = c;
	}

	@Override
	public String decrypt() {
		h1();
		s3(3);
		h1();
		sd(65);
		h1();
		s3(1);
		h1();
		sd(20);
		return new String(sig);
	}
}
