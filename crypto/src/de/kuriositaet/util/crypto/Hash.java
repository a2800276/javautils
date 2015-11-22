package de.kuriositaet.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilities to deal with hash generation.
 * Created by a2800276 on 2015-10-30.
 */
public class Hash {
    /**
     * Return the size of the resulting hash in bits.
     *
     * @param algorithm
     * @return
     **/
    public static int blockSizeBits(Algorithm algorithm) {
        switch (algorithm) {
            case MD5:
            case SHA1:
            case SHA256:
                return 512;
            case SHA384:
            case SHA512:
                return 1024;
            default:
                throw new IllegalArgumentException("really should not happen");
        }
    }

    /**
     * Return the size of the resulting hash in bytes.
     *
     * @param algorithm
     * @return
     **/
    public static int blockSizeBytes(Algorithm algorithm) {
        return blockSizeBits(algorithm) >>> 3;
    }

    /**
     * MD5
     *
     * @param data
     * @return hash
     * @throws WrappedException if underlying JCA does not provide md5
     */
    public static byte[] md5(byte[]... data) {
        return hash(Algorithm.MD5, data);
    }

    /**
     * SHA1
     *
     * @param data
     * @return hash
     * @throws WrappedException if underlying JCA does not provide SHA1
     */
    public static byte[] sha1(byte[]... data) {
        return hash(Algorithm.SHA1, data);
    }

    /**
     * SHA256
     * @param data
     * @return hash
     * @throws WrappedException if underlying JCA does not provide SHA256
     */
    public static byte[] sha256(byte[]... data) {
        return hash(Algorithm.SHA256, data);
    }

    /**
     * SHA384
     * @param data
     * @return hash
     * @throws WrappedException if underlying JCA does not provide SHA384
     */
    public static byte[] sha384(byte[]... data) {
        return hash(Algorithm.SHA384, data);
    }

    /**
     * SHA512
     * @param data
     * @return hash
     * @throws WrappedException if underlying JCA does not provide SHA512
     */
    public static byte[] sha512(byte[]... data) {
        return hash(Algorithm.SHA512, data);
    }

    /**
     * Calculate hash of data using the named Algorithm. Preferably, use the
     * named functions for the intended algorithm. This method may become relevant in future
     * in case `exotic` algorithms like SHA3 become available on some plattforms but no
     * everywhere. (This method could then be used to access `unsafe` algorithms.
     *
     * @param algorithm
     * @param data
     * @return
     */
    public static byte[] hash(Algorithm algorithm, byte[]... data) {
        String _algo;
        switch (algorithm) {
            case MD5:
                _algo = "MD5";
                break;
            case SHA1:
                _algo = "SHA-1";
                break;
            case SHA256:
                _algo = "SHA-256";
                break;
            case SHA384:
                _algo = "SHA-384";
                break;
            case SHA512:
                _algo = "SHA-512";
                break;
            default:
                throw new IllegalArgumentException("invalid algorithm");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(_algo);
            for (byte[] bytes : data) {
                digest.update(bytes);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new WrappedException(e);
        }
    }

    /**
     * Supported Hash Algorithms, notably absent is SHA3 ...
     * (TODO: possibly roll you own here ...)
     */
    public enum Algorithm {
        MD5,
        SHA1,
        SHA256,
        SHA384,
        SHA512,
    }
}
