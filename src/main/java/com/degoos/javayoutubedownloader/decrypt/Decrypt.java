package com.degoos.javayoutubedownloader.decrypt;

/**
 * Represents a decrypt algorithm. The Decrypt implementations are used to decrypt the signature code of protected streams.
 */
public interface Decrypt {

	/**
	 * Decrypts the signature code inside the instance.
	 *
	 * @return the decrypted code.
	 */
	String decrypt();

}
