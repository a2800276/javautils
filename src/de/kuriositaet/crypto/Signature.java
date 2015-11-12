package de.kuriositaet.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Utility to generate Signatures.
 *
 * Created by a2800276 on 2015-11-02.
 */
public class Signature {

    public static byte[] sign(Algorithm algorithm, KeyPair.PrivateKey pk, byte[]... data) {
        java.security.Signature sig;
        try {
            sig = java.security.Signature.getInstance(algorithm.toString());
            sig.initSign(pk.pk);
            for (byte[] bytes : data) {
                sig.update(bytes);
            }
            return sig.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean verify(Algorithm algorithm, KeyPair.PublicKey pub, byte[] signature, byte[]... data) {
        java.security.Signature sig;
        try {
            sig = java.security.Signature.getInstance(algorithm.toString());
            sig.initVerify(pub.getPublicKey());
            for (byte[] bytes : data) {
                sig.update(bytes);
            }
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    enum Algorithm {
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
