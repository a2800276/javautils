package de.kuriositaet.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static de.kuriositaet.crypto.Random.random;

/**
 * Created by a2800276 on 2015-10-30.
 */
public class HMAC {

    public static byte[] generateKey(Hash.Algorithm a) {
        return random(Hash.blockSizeBytes(a));
    }

    public static byte[] hmacMD5(byte[] key, byte[]... data) {
        return hmac(Hash.Algorithm.MD5, key, data);
    }

    public static byte[] hmacSHA1(byte[] key, byte[]... data) {
        return hmac(Hash.Algorithm.SHA1, key, data);
    }

    public static byte[] hmacSHA256(byte[] key, byte[]... data) {
        return hmac(Hash.Algorithm.SHA256, key, data);
    }

    public static byte[] hmacSHA384(byte[] key, byte[]... data) {
        return hmac(Hash.Algorithm.SHA384, key, data);
    }

    public static byte[] hmacSHA512(byte[] key, byte[]... data) {
        return hmac(Hash.Algorithm.SHA512, key, data);
    }

    public static byte[] hmac(Hash.Algorithm algo, byte[] key, byte[]... data) {
        String jca_algo;
        switch (algo) {
            case MD5:
                jca_algo = "HmacMD5";
                break;
            case SHA1:
                jca_algo = "HmacSHA1";
                break;
            case SHA256:
                jca_algo = "HmacSHA256";
                break;
            case SHA384:
                jca_algo = "HmacSHA384";
                break;
            case SHA512:
                jca_algo = "HmacSHA512";
                break;
            default:
                throw new RuntimeException("invalid: " + algo);
        }
        try {
            Key k = new SecretKeySpec(key, jca_algo);
            Mac mac = Mac.getInstance(jca_algo);
            mac.init(k);
            for (byte[] bytes : data) {
                mac.update(bytes);
            }
            return mac.doFinal();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
