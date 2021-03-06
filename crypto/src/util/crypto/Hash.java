package util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilities to deal with hash generation.
 * Created by a2800276 on 2015-10-30.
 */
public class Hash {
	/**
	 * MD5
	 *
	 * @param data input to the digest function
	 * @throws WrappedException if underlying JCA does not provide md5
	 */
	public static byte[] md5(byte[]... data) {
		return hash(Algorithm.MD5, data);
	}

	/**
	 * SHA1
	 *
	 * @param data input to the digest function
	 * @throws WrappedException if underlying JCA does not provide SHA1
	 */
	public static byte[] sha1(byte[]... data) {
		return hash(Algorithm.SHA1, data);
	}

	/**
	 * SHA256
	 *
	 * @throws WrappedException if underlying JCA does not provide SHA256
	 */
	public static byte[] sha256(byte[]... data) {
		return hash(Algorithm.SHA256, data);
	}

	/**
	 * SHA384
	 *
	 * @param data input to the digest function
	 * @throws WrappedException if underlying JCA does not provide SHA384
	 */
	public static byte[] sha384(byte[]... data) {
		return hash(Algorithm.SHA384, data);
	}

	/**
	 * SHA512
	 *
	 * @param data input to the digest function
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
	 * @param algorithm the hash algorithm to call.
	 * @param data      input
	 * @return the calculated digest
	 */
	public static byte[] hash(Algorithm algorithm, byte[]... data) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm.jcaName());
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
		SHA512;

		public int blockSizeBits() {
			switch (this) {
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

		public int digestSizeBits() {
			switch (this) {
				case MD5:
					return 128;
				case SHA1:
					return 160;
				case SHA256:
					return 256;
				case SHA384:
					return 384;
				case SHA512:
					return 512;
				default:
					throw new IllegalArgumentException("really should not happen");
			}
		}

		public int blockSizeBytes() {
			return blockSizeBits() >>> 3;
		}

		public int digestSizeBytes() {
			return digestSizeBits() >>> 3;
		}

		public String jcaName() {
			switch (this) {
				case MD5:
					return "MD5";
				case SHA1:
					return "SHA-1";
				case SHA256:
					return "SHA-256";
				case SHA384:
					return "SHA-384";
				case SHA512:
					return "SHA-512";
				default:
					throw new IllegalArgumentException("invalid algorithm");
			}
		}

		public String jcaHmacName() {
			switch (this) {
				case MD5:
					return "HmacMD5";
				case SHA1:
					return "HmacSHA1";
				case SHA256:
					return "HmacSHA256";
				case SHA384:
					return "HmacSHA384";
				case SHA512:
					return "HmacSHA512";
				default:
					throw new IllegalArgumentException("invalid algorithm");
			}
		}
	}


}
