package util.crypto;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.*;
import static util.bytes.Bytes.h2b;

public class SignatureTest {

	static final byte[] rsa_message = h2b("35683c60341c30effa0dd0cc99166dbb8c09a58a4b001f6bab3992558fadc58d3a7d57f5463adf86cd0951e4e540571705d092d965c7f951de1161bc166fb8da");
	// 35683c60341c30effa0dd0cc99166dbb8c09a58a4b001f6bab3992558fadc58d3a7d57f5463adf86cd0951e4e540571705d092d965c7f951de1161bc166fb8da
	static final byte[][] rsa_signatures = {
			h2b("85253d79dab3604fdddbd629c397636bbff53ac23869c6a46cc866395da031c69b2588f84e007945dfa88866fe9e2ee4ed9dd21253369aaf3e811abb59fd7ca161f0b837c16b7f93cd8df3b343e3ed33b567506428b558750a13764b26adf844ac52e881ccbf4769606d3ec4571f0961884f086a31b71fb7d8288a4ce28bd5be"),
			h2b("8ff565678931627f2eeb66f9d41756f9f7b77a926d9e1ddb87c6bf2b80a33e8bdce6e8ce7ce28706664a86f2690fe9e1fe76b3c7a90c83afada1611713ffc368cd634c071afd48bc7281743b31b675a6bf0de7c30eb7260924ed6466cb81f3f479e0b0aed0d1ae39c233965ad8df2f5879b412b7d44c3cbc9f20b24f6775023c"),
			h2b("4673065140202661b7d2634a0f5406db0589564ef4e9f583455afadba39c3f70386e54f4c3d47cf0ac00b4034fdbe84fcff637eed0598f880a6a956c314c45ea3350d3dcb7fbc504eb35351e1123638601caa11a1e7901a9a81399749f034def0b9339fb6c2b9a09d548932def1ec12aa0604d5ef81ef52a46695e6d6e2ea481144f0ad8b40d13d68cd443b772cfe304eb0efd59e206de052ccf772e5d4315a869efeaf411af3bdadd429398cd8a5b3f7577769575a5ced700617ab5e2f5f7eca39726389e14f7d322c4ee77cf766f1964d11bd408809b2bcdb6305b9e8b0bf641561c9d36fd0ad8bb7cc7f20decb597a448a642b226182e32b15b01b6db5925"),
			h2b("af129a74719735857eabe83a6098ea2907653954d6f1d7e399627ebd0f87eaa49fcd27b3699925b711896c9325325bc6c3bac8a5a476a75161d6f009a2b3a75def43002c9d59747168ee6d072d3c43395865a29291469470f1141a2ca8478ccfe62ece0d1720255580b1957e2ef8bfe43d8f79402cb2e85605fd3e8716020ea19373a949beab6a67a9937493df330d78f7a96f812105adaff7b277359795907414eb86d1d77cd070659064923e6d4fef338934d2652f8fc91347c90acbda3e0462d9cf441256d5babdf4ecca82961428fb3acfd4f26058f16b2ffdd893c6c4271c5ec7f7b2099396ce6c0c766244099fdbe044c7cad5254777ea24c8bd186e23"),
			h2b("4290c0192bebd23856ec8e6d066dcec7dbbea805a15344164d639d1d54f7317be12000babc8d0de3b12c289f4e03822592e11b0be9c7d4ea8b464a5d4bc16519aca386d1e20aa05c59932754c49be8df649fae5d0153e05b8ce0b87c4690a4a4a5c805ec3a91808d072a31a0381ac9e8ea96dc019d7ff65327a1f737dbe28c1658a6acdbf51188e8afcb6add62b6015b88da03e03a30021a15c303282e471bf234f5f01ab33b5a925a433bf54badf63d2a9f2ab1b2a22ded2922998abfcb5c208ff8d3669b071fd33bab6fc04867270f4c582265bf61f316195f6b7965ab3b35b157842a45619d0acbf45566a79df76861aa3aeb97d1b546b531edbb458a23fcca434012a50fdaca45dccb07cc2a29134885213ee4cca4751a3a6276d480c849c512d6f638142e07714fb4aad5234a01975fd8270759f6fc0ace45168ef61ca06654562997721c2a5b9af00ef34b7ae3f7bc9da992d138927283e2117c8b90a2b7a3591fd9bc8dd63775ac892c076041f317051206f2da5c59327b428d1b0969377eb0e8a9ca6f6aa93422c0e0ab7ad4b066a52c8e5cf627f35e35f5fb440638dcf5b5af8ce1856c16095f9bcd3561f3665ed0136d674ef7e25f97c2ea04024642fa4a9104b5524a79c28d0b53bc86eee975866392fa83598aaca2f98fcdbbe3944dccab7c694e10a75ded0d33e5dffd4d9e9714f9d73a5a03f1651e8b69d29c"),
			h2b("3a745e8140b9eb28a1b3b228b7bef03a14b15163b10f2204523c054e013ec8bec73e9987707df842c276d23a0a88bfa24575fc8db9434ef998c5aff0875d3c8d2c390eda924b6b2fac00804e6a4157255732b11d3d4c0c3ce09a9322b6e269ad60c059b815469097bd4784b8e691645fd57b0213f5fbed80e5517fee02cbb1e67ad5baee6dd6f4d8c4dcb0ccc6f87d471edd9e5d8890e005c508c9d377b4f6a8042cadbeb764f4253c47851692ba2dfc92908bf34e2127afd0cbcd1213d7276aec2d9fb0c9812f3b5ef4ccff8fd705fd688ae741e142868f62744e57ff4dbc77191eb327bda30a154e775a9a22cd908034a2b8cbd430039ee412e5282fdb7d44a8aff21277b9bc421721a063522a38d46d147a3b4021ca945d8286944a548a94a1a7bbf6750ff7dd09dc452d261c21b8c6cf43949e3831a874af3f8ff1a09498990108956affd0c0f3125eaba1b571900c3b0e33a7fb61924f967ba70510d38d12b59303e22b332de7f451650db62d0abc51877e2994285f8ae589aa94fa2d039bf81becd15b23dc789dea39f14a73e12ff765f13dd42a2a827d3172aa6aa876444189cbc3746d49a0aa425aff0b12d70faa1d0e72bf3ad4fd9360fdb108919a303f82e316b2e496555668d60e3bdf2a11898655029457d4a21d6fa1f8276b3b876f0904574ba366b15408009ab1e40a71cfad25c315b87e1b56d588989a5496"),
	};
	static final byte[] ecc_message = h2b("6ade217130b24223eb99617cdca9ec72d1ee64e56a6da97aea2e6cd597e3518406c42181188b29d9339aee48499cf1e01024c455f5361d598c3dfffa92cf8d88");
	static final byte[][] ecc_signatures = {
			h2b("303d021c3062f159b9d88da5dafb1543404f22864d2ddfa90c08b2776bceb4c0021d00d261d7c6c485937efbe1028b1cb83eaf54515d0bf79557d9007769bf"),
			h2b("303d021c4b588444c93e60dd88ba49ce0197e2b52182f2380ddc5166e92c5d88021d00f04a5e52ac53f646ee8f3dac880761633ac8bac5117fca56ef5b9be8"),
			h2b("303d021d009f9c75413a69fa678e25514f5ceb2f4b2a3f0739c673ea7367839e58021c122d88a076ee64db854018b4429013f953ba84fe4e98f1eff3df03be"),
			h2b("303d021c3847ccfc2ff4cb89f3c915a0d958e6a23647f0c53b725e56d24a4ff4021d00e0875c6e173e0a78f4c6737f698f09326f2194edb5ed42e40592be7f"),
			// P256
			h2b("3045022100bb3fe5c0258154e9fc754391560e370e0f0f7a586a78b2e95406d2d6c72439790220294359ce2fe366ab62b94dae90219d26a96122e36890200551fd0b1c65d0bf20"),
			h2b("3044022006b40ecdf856b508bfbd22c370902f159034a489d8e112e23ecdb6ef6756354a0220344ff5029b79232e675888bcb2e97e024bb0cdbcedeeda4ea18a0dab0c0d0c4d"),
			h2b("304602210093144ae4eb4fa401d92d8abf87b0944544b299aedd0e7b8049053bd6186e1f53022100843c6b72b603379e9775b3bda2cbd2728fa629691ba78b9ff47e7a718053fbd1"),
			h2b("3044022026c1aeef304c06e7433bf90207abc0dd40eb834d7926e8c3cf1fe74f3fa4967502205ac80e37f09f599718b66ead2ef6ed689aab579ba4ffd408c239b090f3ad0b66"),
			// P384
			h2b("306502301e38b77a3f2206a33d7fc7adbfc602f1dea3a46d13bd826f1b7fec0a0ee6658bcedf980eb8fc2d809fa52219c6125ce3023100cdfcb6beca92d39ffaf5f171a1c6ea54a84523d4720c8e4ed55b6094d62b77752bee762d4e654720bea1cb55cfaf0fde"),
			h2b("3066023100ce0a52626abd1300ed37a79b966d036fe93f453d5a2bea4d1d3833ee96ad768a4a171388da9886677c795573d0de802902310084b34a872259ae22806e1f1432608255dcaafa325482463929c1cca5f1ed49c3d146680f7d81ff6be7f58bcbdbec7368"),
			h2b("3065023100a2bb626b59c7a5daa1043b646cd7f1dfccb778b4a445f50ea0449981c6c18685356054ec3e0893fd1a7f04acef4e89ec02302ea8bf68ed6b2f2df9160f44818fe0c4675b4f6e633d1c2f94ff8328e32dd17b677efae3593ba2629da2799cf5c911ca"),
			h2b("3066023100d281d0b96ebef9de71ca8294f118a11b19b4cb08830982aad95e9d233fe22a52dace093547f67028338e43bbd5ba5b68023100deb704a30cd7263dae3cf9ba50df02f0845035a656df3af776d251f2344d7eaef708df0fa08778807253cc939a0d546f"),
			// P521
			h2b("30818702417d9a2dfee8a0ce99fb9b8241c5ddb2f2e09be02c123c4411c306b60b3a307bf4712937611dcf88378abc9dbc76c8876813905744a6c04d86938976d79cbd801649024201b6852fda9eb316f206e5a7e3e949c6888288f5b26efaf03bcc24b0cdbd4384a4cb639e636c0b9176bc9a39ddb6cbae95b12651a48131ba426430e36c54754c8207"),
			h2b("3081860241608825a09ec38790d707fbe35119a875af514c6b9b5948b18974047ea83f296646a70f1919a24607ca6c8a3c21adab7eb12f28f18c5dae99eadace11830c42e60b02415cefdf4061ed0179c78fda7e86bd81a7fb508cbcdf62f85fb7b124142f65beace307f50028e36fac774fa43830b900f25b563fc64d762cb32b653c3347c6cdf92c"),
			h2b("3081880242008fcf0f659534173d35504e3b1c0d7f1e321f3286165121fedd32901f89794236b1506210ac751036a9c7354c6bce7527f95099e3a57bd6f797a3aebbcdf05d6fa2024200e07119f8c066b612cd2f3161bc9782ed2f93edec36b140f6333b05b7738771c0e8a398973b6aeba2fa3289944b871d4e69ddfc2702fe257d956ec7fbe79a2eb69f"),
			h2b("308187024114fe6f92bbcd8fb6a19eac430498c516afdca29f98414c61b3921aafd7a482c07bba1a6efc8f41079d831515516fb1df4537bc580893fd6743834c50fe1906c8d3024201a941157640bccd8ca632bb472eb4e703b6a8dbfb608eb58df004a38861654ce5d3f175e9ec39dbd908805185e8edc253c585273d4a9ea8d17692a058a1a6c315ee"),
	};
	////////////////////////////////////////////////////////////////////////
	// ECC Sign
	////////////////////////////////////////////////////////////////////////
	static final Signature.Algorithm[] ECAlgorithms = {
			Signature.Algorithm.NONEwithECDSA,
			Signature.Algorithm.SHA1withECDSA,
			Signature.Algorithm.SHA256withECDSA,
			Signature.Algorithm.SHA384withECDSA,
			Signature.Algorithm.SHA512withECDSA
	};
	static String[] rsa_key_fn = {"1024", "2048", "4096"};
	static String[] ecc_key_fn = {"p224", "p256", "p384", "p521"};

	static LinkedList<KeyPair.PublicKey> loadRSAPublicKeys() throws IOException {
		LinkedList<KeyPair.PublicKey> l = new LinkedList<>();
		for (String bit : rsa_key_fn) {
			InputStream stream = ClassLoader.class.getResourceAsStream("/keys/rsa_key_" + bit + ".x509");
			l.add(KeyPair.PublicKey.loadX509(stream));
		}
		return l;
	}

	static LinkedList<KeyPair.PublicKey> loadECPublicKeys() throws IOException {
		LinkedList<KeyPair.PublicKey> l = new LinkedList<>();
		for (String curve : ecc_key_fn) {
			InputStream stream = ClassLoader.class.getResourceAsStream("/keys/" + curve + "_key.x509");
			l.add(KeyPair.PublicKey.loadX509(stream));
		}
		return l;
	}

	@Test
	public void testSignVerifyRoundtrip() throws Exception {
		for (Signature.Algorithm a : Signature.Algorithm.values()) {
			KeyPair pair = getKeyPair(a);
			byte[] random = a == Signature.Algorithm.NONEwithECDSA ? Random.random(64) : Random.random(1024);
			byte[] sig = Signature.sign(a, pair.getPrivateKey(), random);
			assertTrue(Signature.verify(a, pair.getPublicKey(), sig, random));
		}
	}

	private KeyPair getKeyPair(Signature.Algorithm a) {
		switch (a) {
			case SHA1withDSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.DSA);
			case SHA1withRSA:
			case SHA256withRSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.RSA);
			case NONEwithECDSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.P224);
			case SHA1withECDSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.P256);
			case SHA256withECDSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.P384);
			case SHA384withECDSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.P521);
			case SHA512withECDSA:
				return KeyPair.generateKeyPair(KeyPair.Algorithm.P521);
			default:
				throw new RuntimeException("cannot happen.");
		}
	}

	////////////////////////////////////////////////////////////////////////
	// RSA Sign
	////////////////////////////////////////////////////////////////////////
	@Test
	public void testRSASign() throws Exception {
		List<KeyPair.PrivateKey> keys = loadRSAPrivateKeys();
		for (int i = 0; i != keys.size(); ++i) {
			KeyPair.PrivateKey key = keys.get(i);
			assertEquals(Signature.sign(Signature.Algorithm.SHA1withRSA, key, rsa_message), rsa_signatures[i * 2]);
			assertEquals(Signature.sign(Hash.Algorithm.SHA1, key, rsa_message), rsa_signatures[i * 2]);
			assertEquals(Signature.sign(Signature.Algorithm.SHA256withRSA, key, rsa_message), rsa_signatures[i * 2 + 1]);
			assertEquals(Signature.sign(Hash.Algorithm.SHA256, key, rsa_message), rsa_signatures[i * 2 + 1]);

		}
	}

	private List<KeyPair.PrivateKey> loadRSAPrivateKeys() throws IOException {
		LinkedList<KeyPair.PrivateKey> l = new LinkedList<>();
		for (String bit : rsa_key_fn) {
			InputStream stream = ClassLoader.class.getResourceAsStream("/keys/rsa_key_" + bit + ".pkcs8");
			l.add(KeyPair.PrivateKey.loadPKCS8(stream));
		}
		return l;
	}

	////////////////////////////////////////////////////////////////////////
	// RSA Verify
	////////////////////////////////////////////////////////////////////////
	@Test
	public void testRSAVerify() throws Exception {
		LinkedList<KeyPair.PublicKey> keys = loadRSAPublicKeys();
		for (int i = 0; i != keys.size(); ++i) {
			KeyPair.PublicKey pub = keys.get(i);
			assertTrue(Signature.verify(Signature.Algorithm.SHA1withRSA, pub, rsa_signatures[i * 2], rsa_message));
			assertTrue(Signature.verify(Hash.Algorithm.SHA1, pub, rsa_signatures[i * 2], rsa_message));
			assertTrue(Signature.verify(Signature.Algorithm.SHA256withRSA, pub, rsa_signatures[i * 2 + 1], rsa_message));
			assertTrue(Signature.verify(Hash.Algorithm.SHA256, pub, rsa_signatures[i * 2 + 1], rsa_message));
		}
	}

	@Test
	public void testECCSign() throws Exception {
		List<KeyPair.PrivateKey> privateKeys = loadECCPrivateKeys();
		List<KeyPair.PublicKey> publicKeys = loadECPublicKeys();

		for (int i = 0; i != privateKeys.size(); ++i) {
			// (EC) DSA signatures aren't deterministic. Just make sure round trip, etc. works
			KeyPair.PrivateKey privateKey = privateKeys.get(i);
			KeyPair.PublicKey pub = publicKeys.get(i);
			for (Signature.Algorithm a : ECAlgorithms) {
				byte[] sig1 = Signature.sign(a, privateKey, ecc_message);
				byte[] sig2 = Signature.sign(a, privateKey, ecc_message);
				assertNotEquals(sig1, sig2);
				assertTrue(Signature.verify(a, pub, sig1, ecc_message));
				assertTrue(Signature.verify(a, pub, sig2, ecc_message));
			}
		}
	}

	private List<KeyPair.PrivateKey> loadECCPrivateKeys() throws IOException {
		LinkedList<KeyPair.PrivateKey> l = new LinkedList<>();
		for (String curve : ecc_key_fn) {
			InputStream stream = ClassLoader.class.getResourceAsStream("/keys/" + curve + "_key.pkcs8");
			l.add(KeyPair.PrivateKey.loadPKCS8(stream));
		}
		return l;
	}

	////////////////////////////////////////////////////////////////////////
	// ECDSA Verify
	////////////////////////////////////////////////////////////////////////
	@Test
	public void testECDSAVerify() throws Exception {
		LinkedList<KeyPair.PublicKey> keys = loadECPublicKeys();
		for (int i = 0; i != keys.size(); ++i) {
			KeyPair.PublicKey pub = keys.get(i);
			assertTrue(Signature.verify(Signature.Algorithm.SHA1withECDSA, pub, (ecc_signatures[i * 4]), ecc_message));
			assertTrue(Signature.verify(Hash.Algorithm.SHA1, pub, (ecc_signatures[i * 4]), ecc_message));
			assertTrue(Signature.verify(Signature.Algorithm.SHA256withECDSA, pub, (ecc_signatures[i * 4 + 1]), ecc_message));
			assertTrue(Signature.verify(Hash.Algorithm.SHA256, pub, (ecc_signatures[i * 4 + 1]), ecc_message));
			assertTrue(Signature.verify(Signature.Algorithm.SHA384withECDSA, pub, (ecc_signatures[i * 4 + 2]), ecc_message));
			assertTrue(Signature.verify(Hash.Algorithm.SHA384, pub, (ecc_signatures[i * 4 + 2]), ecc_message));
			assertTrue(Signature.verify(Signature.Algorithm.SHA512withECDSA, pub, (ecc_signatures[i * 4 + 3]), ecc_message));
			assertTrue(Signature.verify(Hash.Algorithm.SHA512, pub, (ecc_signatures[i * 4 + 3]), ecc_message));
		}
	}
}