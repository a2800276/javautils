package de.kuriositaet.crypto;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;

/**
 * Test uncompressed point representation.
 * Created by a2800276 on 2015-11-11.
 */
public class ECPublicKeyTest {
    @Test
    public void testToUncompressedCurvePointAndBack() throws Exception {
        for (KeyPair.Algorithm a : KeyPair.Algorithm.values()) {
            switch (a) {
                case DSA:
                case RSA:
                    break;
                default:
                    KeyPair pair = KeyPair.generateKeyPair(a);
                    KeyPair.ECPublicKey pub = (KeyPair.ECPublicKey) pair.getPublicKey();
                    byte[] uc = pub.toUncompressedCurvePoint();
                    ByteArrayInputStream bis = new ByteArrayInputStream(uc);
                    KeyPair.ECPublicKey pub2 = KeyPair.ECPublicKey.loadUncompressedCurvePoints(a, bis);
                    assertEquals(pub, pub2);
            }
        }
    }
}