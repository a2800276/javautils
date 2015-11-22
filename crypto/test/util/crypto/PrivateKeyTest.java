package util.crypto;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by a2800276 on 2015-11-16.
 */
public class PrivateKeyTest {

    @Test
    public void testSignRSA() throws Exception {
        KeyPair pair = KeyPair.generateKeyPair(KeyPair.Algorithm.RSA);
        byte[] random = Random.random(128);
        byte[] sig = pair.getPrivateKey().sign(Hash.Algorithm.SHA1, random);
        assertTrue(Signature.verify(Signature.Algorithm.SHA1withRSA, pair.getPublicKey(), sig, random));
        sig = pair.getPrivateKey().sign(Hash.Algorithm.SHA256, random);
        assertTrue(Signature.verify(Signature.Algorithm.SHA256withRSA, pair.getPublicKey(), sig, random));

        boolean caught = false;
        try {
            pair.getPrivateKey().sign(Hash.Algorithm.SHA512, random);
        } catch (WrappedException e) {
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testSignDSA() throws Exception {
        KeyPair pair = KeyPair.generateKeyPair(KeyPair.Algorithm.DSA);
        byte[] random = Random.random(256);
        byte[] sig = pair.getPrivateKey().sign(Hash.Algorithm.SHA1, random);
        assertTrue(Signature.verify(Signature.Algorithm.SHA1withDSA, pair.getPublicKey(), sig, random));

        boolean caught = false;
        try {
            pair.getPrivateKey().sign(Hash.Algorithm.SHA256, random);
        } catch (WrappedException e) {
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testSignECDSA() throws Exception {
        byte[] random = Random.random(128);
        for (KeyPair.Algorithm a : KeyPair.Algorithm.values()) {
            switch (a) {
                case RSA:
                case DSA:
                    continue;
            }

            KeyPair pair = KeyPair.generateKeyPair(a);

            for (Hash.Algorithm hashA : Hash.Algorithm.values()) {
                switch (hashA) {
                    case MD5:
                        boolean caught = false;
                        try {
                            pair.getPrivateKey().sign(hashA, random);
                        } catch (WrappedException e) {
                            caught = true;
                        }
                        assertTrue(caught);
                        continue;
                }
                byte[] sig = pair.getPrivateKey().sign(hashA, random);
                Signature.Algorithm sigA = pair.getPrivateKey().getSignatureAlgorithm(hashA);
                assertTrue(Signature.verify(sigA, pair.getPublicKey(), sig, random));
            }
        }
    }
}