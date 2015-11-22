package util.crypto;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.security.interfaces.RSAPrivateCrtKey;

import static org.testng.Assert.*;

/**
 * Test KeyPair s
 * Created by a2800276 on 2015-11-02.
 */
public class KeyPairTest {

//    @Test
//    public void testExtractRSAPublicFromPrivate() throws Exception {
//        java.security.KeyPair pair = KeyPair.generateKeyPair(KeyPair.Algorithm.RSA);
//        KeyPair.PrivateKey pk = new KeyPair.PrivateKey(pair.getPrivate());
//        KeyPair.PublicKey pub = pk.getPublicKey();
//        assertEquals(pair.getPublic(), pub.pub);
//    }

//    @Test public void testStore() throws Exception {
//        KeyPair pair = KeyPair.generateKeyPair(KeyPair.Algorithm.RSA);
//        FileOutputStream fos = new FileOutputStream("tim.x509");
//        pair.getPublicKey().storeX509(fos);
//    }

    @Test
    public void testDSALong() throws Exception {
        KeyPair pair = KeyPair.generateDSAKeyPair(Constants.DSA_DEFAULT_KEYSIZE * 2);
        byte[] mes = {};
        Signature.sign(Signature.Algorithm.SHA1withDSA, pair.getPrivateKey(), mes);
    }

    @Test
    public void smallRSAKey() throws Exception {
        KeyPair pair = KeyPair.generateRSAKeyPair(512);
    }

    @Test
    public void testExtractPublicRSAFromPrivate() throws Exception {
        KeyPair pair = KeyPair.generateRSAKeyPair(512);
        assertEquals(KeyPair.getPublicKeyFromRSACRTPrivateKey((RSAPrivateCrtKey) pair.getPrivateKey().getJCAPrivateKey()), pair.getPublicKey().getJCAPublicKey());

    }

    @Test
    public void testLoadPrivateKeyFromPKCS8() throws Exception {
        KeyPair pair = KeyPair.generateKeyPair(KeyPair.Algorithm.P256);
        byte[] pkcs8 = pair.getPrivateKey().toPKCS8();
        ByteArrayInputStream is = new ByteArrayInputStream(pkcs8);
        KeyPair.PrivateKey pk = KeyPair.PrivateKey.loadPKCS8(is);
    }

    @Test
    public void testConstructorBytes() throws Exception {
        KeyPair pair = KeyPair.generateKeyPair(KeyPair.Algorithm.P521);
        KeyPair pair2 = KeyPair.generateKeyPair(KeyPair.Algorithm.P521);
        KeyPair pair3 = new KeyPair(pair.getPrivateKey().toPKCS8(), pair.getPublicKey().toX509());
        assertTrue(pair.equals(pair3));
        assertTrue(pair3.equals(pair));
        assertTrue(pair.equals(pair));
        assertNotEquals(pair, pair2);
    }
}