package de.kuriositaet.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by a2800276 on 2015-10-30.
 */
public class Hash {
    public static int blockSizeBits(Algorithm a) {
        switch (a) {
            case MD5:
            case SHA1:
            case SHA256:
                return 512;
            case SHA384:
            case SHA512:
                return 1024;
            default:
                throw new RuntimeException("really should not happen");
        }
    }

    public static int blockSizeBytes(Algorithm a) {
        return blockSizeBits(a) >>> 3;
    }

    public static byte[] md5(byte[]... data) {
        return hash(Algorithm.MD5, data);
    }

    public static byte[] sha1(byte[]... data) {
        return hash(Algorithm.SHA1, data);
    }

    public static byte[] sha256(byte[]... data) {
        return hash(Algorithm.SHA256, data);
    }

    public static byte[] sha384(byte[]... data) {
        return hash(Algorithm.SHA384, data);
    }

    public static byte[] sha512(byte[]... data) {
        return hash(Algorithm.SHA512, data);
    }

    public static byte[] hash(Algorithm algo, byte[]... data) {
        String _algo;
        switch (algo) {
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
                throw new RuntimeException("invalid algorithm");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(_algo);
            for (byte[] bytes : data) {
                digest.update(bytes);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Algorithm {
        MD5,
        SHA1,
        SHA256,
        SHA384,
        SHA512,
    }
}
