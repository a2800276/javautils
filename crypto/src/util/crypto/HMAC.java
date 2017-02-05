package util.crypto;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static util.crypto.Random.random;

/**
 * Utilities to generate HMACs.
 * <p>
 * The size of the keying material used for HMAC should ideally be identical to the block
 * size of the underlying algorithm. (but does not need to be)
 * <p>
 * link https://tools.ietf.org/html/rfc2104
 * link http://csrc.nist.gov/publications/fips/fips198-1/FIPS-198-1_final.pdf FIPS-198-1
 */
public class HMAC {

	public static byte[] generateKey(Hash.Algorithm a) {
		return random(a.blockSizeBytes());
	}

	public static SecretKey generateJCAKey(Hash.Algorithm a) {
		byte[] keyBytes = generateKey(a);
		return new SecretKeySpec(keyBytes, a.jcaHmacName());
	}

	/**
	 * @throws WrappedException wrapping a NoSuchAlgorithmException in the unlikely event the underlying
	 *                          JCA does not provided the Algorithm or wrapping a InvalidKeyException if using incorrect
	 *                          keying material.
	 */
	public static byte[] hmacMD5(byte[] key, byte[]... data) {
		return hmac(Hash.Algorithm.MD5, key, data);
	}

	/**
	 * @throws WrappedException wrapping a NoSuchAlgorithmException in the unlikely event the underlying
	 *                          JCA does not provided the Algorithm or wrapping a InvalidKeyException if using incorrect
	 *                          keying material.
	 */
	public static byte[] hmacSHA1(byte[] key, byte[]... data) {
		return hmac(Hash.Algorithm.SHA1, key, data);
	}

	/**
	 * @throws WrappedException wrapping a NoSuchAlgorithmException in the unlikely event the underlying
	 *                          JCA does not provided the Algorithm or wrapping a InvalidKeyException if using incorrect
	 *                          keying material.
	 */
	public static byte[] hmacSHA256(byte[] key, byte[]... data) {
		return hmac(Hash.Algorithm.SHA256, key, data);
	}

	/**
	 * @throws WrappedException wrapping a NoSuchAlgorithmException in the unlikely event the underlying
	 *                          JCA does not provided the Algorithm or wrapping a InvalidKeyException if using incorrect
	 *                          keying material.
	 */
	public static byte[] hmacSHA384(byte[] key, byte[]... data) {
		return hmac(Hash.Algorithm.SHA384, key, data);
	}

	/**
	 * @throws WrappedException wrapping a NoSuchAlgorithmException in the unlikely event the underlying
	 *                          JCA does not provided the Algorithm or wrapping a InvalidKeyException if using incorrect
	 *                          keying material.
	 */
	public static byte[] hmacSHA512(byte[] key, byte[]... data) {
		return hmac(Hash.Algorithm.SHA512, key, data);
	}

	/**
	 * @throws WrappedException wrapping a NoSuchAlgorithmException in the unlikely event the underlying
	 *                          JCA does not provided the Algorithm or wrapping a InvalidKeyException if using incorrect
	 *                          keying material.
	 */
	public static byte[] hmac(Hash.Algorithm algo, byte[] key, byte[]... data) {
		try {
			Key k = new SecretKeySpec(key, algo.jcaHmacName());
			Mac mac = Mac.getInstance(algo.jcaHmacName());
			mac.init(k);
			for (byte[] bytes : data) {
				mac.update(bytes);
			}
			return mac.doFinal();
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new WrappedException(e);
		}

	}
}
