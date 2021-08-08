package io.github.gaeqs.javayoutubedownloader.decrypt;

/**
 * Represents a decrypt algorithm. The Decrypt implementations are used to decrypt the signature code of protected streams.
 */
public interface Decrypt {

    /**
     * Decrypts the signature code inside the instance.
     *
     * @param jsUrl     the url of the js file containing the decrypt method.
     * @param signature the signature to decrypt.
     * @return the decrypted code.
     */
    String decrypt(String jsUrl, String signature);

}
