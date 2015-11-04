package de.kuriositaet.crypto;

import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static de.kuriositaet.crypto.Util.h2b;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by a2800276 on 2015-11-02.
 */
public class SignatureTest {

    final byte[] rsa_message = h2b("35683c60341c30effa0dd0cc99166dbb8c09a58a4b001f6bab3992558fadc58d3a7d57f5463adf86cd0951e4e540571705d092d965c7f951de1161bc166fb8da");
    // 35683c60341c30effa0dd0cc99166dbb8c09a58a4b001f6bab3992558fadc58d3a7d57f5463adf86cd0951e4e540571705d092d965c7f951de1161bc166fb8da
    final byte[][] rsa_signatures = {
            h2b("85253d79dab3604fdddbd629c397636bbff53ac23869c6a46cc866395da031c69b2588f84e007945dfa88866fe9e2ee4ed9dd21253369aaf3e811abb59fd7ca161f0b837c16b7f93cd8df3b343e3ed33b567506428b558750a13764b26adf844ac52e881ccbf4769606d3ec4571f0961884f086a31b71fb7d8288a4ce28bd5be"),
            h2b("8ff565678931627f2eeb66f9d41756f9f7b77a926d9e1ddb87c6bf2b80a33e8bdce6e8ce7ce28706664a86f2690fe9e1fe76b3c7a90c83afada1611713ffc368cd634c071afd48bc7281743b31b675a6bf0de7c30eb7260924ed6466cb81f3f479e0b0aed0d1ae39c233965ad8df2f5879b412b7d44c3cbc9f20b24f6775023c"),
            h2b("4673065140202661b7d2634a0f5406db0589564ef4e9f583455afadba39c3f70386e54f4c3d47cf0ac00b4034fdbe84fcff637eed0598f880a6a956c314c45ea3350d3dcb7fbc504eb35351e1123638601caa11a1e7901a9a81399749f034def0b9339fb6c2b9a09d548932def1ec12aa0604d5ef81ef52a46695e6d6e2ea481144f0ad8b40d13d68cd443b772cfe304eb0efd59e206de052ccf772e5d4315a869efeaf411af3bdadd429398cd8a5b3f7577769575a5ced700617ab5e2f5f7eca39726389e14f7d322c4ee77cf766f1964d11bd408809b2bcdb6305b9e8b0bf641561c9d36fd0ad8bb7cc7f20decb597a448a642b226182e32b15b01b6db5925"),
            h2b("af129a74719735857eabe83a6098ea2907653954d6f1d7e399627ebd0f87eaa49fcd27b3699925b711896c9325325bc6c3bac8a5a476a75161d6f009a2b3a75def43002c9d59747168ee6d072d3c43395865a29291469470f1141a2ca8478ccfe62ece0d1720255580b1957e2ef8bfe43d8f79402cb2e85605fd3e8716020ea19373a949beab6a67a9937493df330d78f7a96f812105adaff7b277359795907414eb86d1d77cd070659064923e6d4fef338934d2652f8fc91347c90acbda3e0462d9cf441256d5babdf4ecca82961428fb3acfd4f26058f16b2ffdd893c6c4271c5ec7f7b2099396ce6c0c766244099fdbe044c7cad5254777ea24c8bd186e23"),
            h2b("4290c0192bebd23856ec8e6d066dcec7dbbea805a15344164d639d1d54f7317be12000babc8d0de3b12c289f4e03822592e11b0be9c7d4ea8b464a5d4bc16519aca386d1e20aa05c59932754c49be8df649fae5d0153e05b8ce0b87c4690a4a4a5c805ec3a91808d072a31a0381ac9e8ea96dc019d7ff65327a1f737dbe28c1658a6acdbf51188e8afcb6add62b6015b88da03e03a30021a15c303282e471bf234f5f01ab33b5a925a433bf54badf63d2a9f2ab1b2a22ded2922998abfcb5c208ff8d3669b071fd33bab6fc04867270f4c582265bf61f316195f6b7965ab3b35b157842a45619d0acbf45566a79df76861aa3aeb97d1b546b531edbb458a23fcca434012a50fdaca45dccb07cc2a29134885213ee4cca4751a3a6276d480c849c512d6f638142e07714fb4aad5234a01975fd8270759f6fc0ace45168ef61ca06654562997721c2a5b9af00ef34b7ae3f7bc9da992d138927283e2117c8b90a2b7a3591fd9bc8dd63775ac892c076041f317051206f2da5c59327b428d1b0969377eb0e8a9ca6f6aa93422c0e0ab7ad4b066a52c8e5cf627f35e35f5fb440638dcf5b5af8ce1856c16095f9bcd3561f3665ed0136d674ef7e25f97c2ea04024642fa4a9104b5524a79c28d0b53bc86eee975866392fa83598aaca2f98fcdbbe3944dccab7c694e10a75ded0d33e5dffd4d9e9714f9d73a5a03f1651e8b69d29c"),
            h2b("3a745e8140b9eb28a1b3b228b7bef03a14b15163b10f2204523c054e013ec8bec73e9987707df842c276d23a0a88bfa24575fc8db9434ef998c5aff0875d3c8d2c390eda924b6b2fac00804e6a4157255732b11d3d4c0c3ce09a9322b6e269ad60c059b815469097bd4784b8e691645fd57b0213f5fbed80e5517fee02cbb1e67ad5baee6dd6f4d8c4dcb0ccc6f87d471edd9e5d8890e005c508c9d377b4f6a8042cadbeb764f4253c47851692ba2dfc92908bf34e2127afd0cbcd1213d7276aec2d9fb0c9812f3b5ef4ccff8fd705fd688ae741e142868f62744e57ff4dbc77191eb327bda30a154e775a9a22cd908034a2b8cbd430039ee412e5282fdb7d44a8aff21277b9bc421721a063522a38d46d147a3b4021ca945d8286944a548a94a1a7bbf6750ff7dd09dc452d261c21b8c6cf43949e3831a874af3f8ff1a09498990108956affd0c0f3125eaba1b571900c3b0e33a7fb61924f967ba70510d38d12b59303e22b332de7f451650db62d0abc51877e2994285f8ae589aa94fa2d039bf81becd15b23dc789dea39f14a73e12ff765f13dd42a2a827d3172aa6aa876444189cbc3746d49a0aa425aff0b12d70faa1d0e72bf3ad4fd9360fdb108919a303f82e316b2e496555668d60e3bdf2a11898655029457d4a21d6fa1f8276b3b876f0904574ba366b15408009ab1e40a71cfad25c315b87e1b56d588989a5496"),
    };
    String[] key_fn = {"1024", "2048", "4096"};

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
                return KeyPair.generateKeyPair(KeyPair.Algorithm.secp256r1);
            case SHA1withECDSA:
                return KeyPair.generateKeyPair(KeyPair.Algorithm.X9_62_c2tnb431r1);
            case SHA256withECDSA:
                return KeyPair.generateKeyPair(KeyPair.Algorithm.sect163k1);
            case SHA384withECDSA:
                return KeyPair.generateKeyPair(KeyPair.Algorithm.X9_62_prime239v3);
            case SHA512withECDSA:
                return KeyPair.generateKeyPair(KeyPair.Algorithm.secp160k1);
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
            assertEquals(Signature.sign(Signature.Algorithm.SHA256withRSA, key, rsa_message), rsa_signatures[i * 2 + 1]);

        }
    }

    private List<KeyPair.PrivateKey> loadRSAPrivateKeys() throws IOException {
        LinkedList<KeyPair.PrivateKey> l = new LinkedList<>();
        for (String bit : key_fn) {
            FileInputStream fis = new FileInputStream("./test/keys/rsa_key_" + bit + ".pkcs8");
            l.add(KeyPair.PrivateKey.loadPKCS8(fis));
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
            assertTrue(Signature.verify(Signature.Algorithm.SHA256withRSA, pub, rsa_signatures[i * 2 + 1], rsa_message));
        }
    }

    private LinkedList<KeyPair.PublicKey> loadRSAPublicKeys() throws IOException {
        LinkedList<KeyPair.PublicKey> l = new LinkedList<>();
        for (String bit : key_fn) {
            FileInputStream fis = new FileInputStream("./test/keys/rsa_key_" + bit + ".x509");
            l.add(KeyPair.PublicKey.loadX509(fis));
        }
        return l;
    }
}