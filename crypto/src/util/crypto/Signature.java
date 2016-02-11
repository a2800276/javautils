package util.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Utility to generate Signatures. The signature algorithm can either be explicitly
 * indicated using the <code>Signature.Algorithm</code> enum, in which case the provided
 * key to be used in the signature must match. Alternatively, the hash to use in the
 * signature may be provided and the pk mechanism will be derived from the key used. In
 * this case, you must ensure that hash algorithm is supported, e.g. providing a
 * SHA512 hash with a DSA key will yield an Exception.
 *
 * @see KeyPair.PublicKey#verify, KeyPair.PrivateKey#sign
 */
public class Signature {

	public static byte[] sign(Algorithm algorithm, KeyPair.PrivateKey pk, byte[]... data) {
		java.security.Signature sig;
		try {
			sig = java.security.Signature.getInstance(algorithm.toString());
			sig.initSign(pk.getJCAPrivateKey());
			for (byte[] bytes : data) {
				sig.update(bytes);
			}
			return sig.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			throw new RuntimeException(e);
		}

	}

	public static byte[] sign(Hash.Algorithm algorithm, KeyPair.PrivateKey pk, byte[]... data) {
		return sign(pk.getSignatureAlgorithm(algorithm), pk, data);
	}

	public static boolean verify(Algorithm algorithm, KeyPair.PublicKey pub, byte[] signature, byte[]... data) {
		java.security.Signature sig;
		try {
			sig = java.security.Signature.getInstance(algorithm.toString());
			sig.initVerify(pub.getJCAPublicKey());
			for (byte[] bytes : data) {
				sig.update(bytes);
			}
			return sig.verify(signature);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verify(Hash.Algorithm algorithm, KeyPair.PublicKey pub, byte[] signature, byte[]... data) {
		return verify(pub.getSignatureAlgorithm(algorithm), pub, signature, data);
	}

	public enum Algorithm {
		SHA1withDSA,
		SHA1withRSA,
		SHA256withRSA,
		NONEwithECDSA,
		SHA1withECDSA,
		SHA256withECDSA,
		SHA384withECDSA,
		SHA512withECDSA
	}


}
