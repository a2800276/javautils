package util.crypto;

import org.testng.annotations.Test;

import java.util.LinkedList;

import static org.testng.Assert.assertTrue;

public class PublicKeyTest {

	@Test
	public void testVerifyRSA() throws Exception {
		LinkedList<KeyPair.PublicKey> keys = SignatureTest.loadRSAPublicKeys();
		for (int i = 0; i != keys.size(); ++i) {
			KeyPair.PublicKey pub = keys.get(i);
			assertTrue(pub.verify(Hash.Algorithm.SHA1, SignatureTest.rsa_signatures[i * 2], SignatureTest.rsa_message));
			assertTrue(pub.verify(Hash.Algorithm.SHA256, SignatureTest.rsa_signatures[i * 2 + 1], SignatureTest.rsa_message));
		}
	}

	@Test
	public void testVerifyECDSA() throws Exception {
		// TODO rewrite test to not used p224 (i.e. figure out why no 224 in JRE)
		LinkedList<KeyPair.PublicKey> keys = SignatureTest.loadECPublicKeys();
		for (int i = 1; i != keys.size(); ++i) {
			KeyPair.PublicKey pub = keys.get(i);
			assertTrue(pub.verify(Hash.Algorithm.SHA1, SignatureTest.ecc_signatures[i * 4], SignatureTest.ecc_message));
			assertTrue(pub.verify(Hash.Algorithm.SHA256, SignatureTest.ecc_signatures[i * 4 + 1], SignatureTest.ecc_message));
			assertTrue(pub.verify(Hash.Algorithm.SHA384, SignatureTest.ecc_signatures[i * 4 + 2], SignatureTest.ecc_message));
			assertTrue(pub.verify(Hash.Algorithm.SHA512, SignatureTest.ecc_signatures[i * 4 + 3], SignatureTest.ecc_message));
		}
	}
}