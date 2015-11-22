package de.kuriositaet.util.crypto;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by a2800276 on 2015-10-31.
 */
public class HMACTest {

    final static byte[] dd_50 = new byte[50];
    final static byte[] cd_50 = new byte[50];
    final static byte[] aa_80 = new byte[80];
    final static byte[] aa_131 = new byte[131];

    static {
        for (int i = 0; i != 50; ++i) {
            dd_50[i] = (byte) 0xdd;
        }
    }

    static {
        for (int i = 0; i != 50; ++i) {
            cd_50[i] = (byte) 0xcd;
        }
    }

    static {
        for (int i = 0; i != 80; ++i) {
            aa_80[i] = (byte) 0xaa;
        }
        for (int i = 0; i != 131; ++i) {
            aa_131[i] = (byte) 0xaa;
        }
    }

    // Test Vectors:
    // MD5 SHA1 : https://tools.ietf.org/html/rfc2202
    // SHA2 ... : https://tools.ietf.org/html/rfc4231
    byte[][] results_hithere_16 = {
            Util.h2b("9294727a3638bb1c13f48ef8158bfc9d"), // md5
            null,
            null,
            null,
            null

    };
    byte[][] results_hithere = {
            null,
            Util.h2b("b617318655057264e28bc0b6fb378c8ef146be00"), // sha1
            Util.h2b("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7"), // sha256
            Util.h2b("afd03944d84895626b0825f4ab46907f 15f9dadbe4101ec682aa034c7cebc59c faea9ea9076ede7f4af152e8b2fa9cb6"), // sha384
            Util.h2b("87aa7cdea5ef619d4ff0b4241a1d6cb0 2379f4e2ce4ec2787ad0b30545e17cde daa833b7d6b8a702038b274eaea3f4e4 be9d914eeb61f1702e696c203a126854"), // sha512
    };
    byte[][] results_jefe = {
            Util.h2b("750c783e6ab0b503eaa86e310a5db738"), //md5
            Util.h2b("effcdf6ae5eb2fa2d27416d5f184df9c259a7c79"), //sha1
            Util.h2b("5bdcc146bf60754e6a042426089575c75a003f089d2739839dec58b964ec3843"), // sha256
            Util.h2b("af45d2e376484031617f78d2b58a6b1b 9c7ef464f5a01b47e42ec3736322445e 8e2240ca5e69e2c78b3239ecfab21649"), //sha384
            Util.h2b("164b7a7bfcf819e2e395fbe73b56e0a3 87bd64222e831fd610270cd7ea250554 9758bf75c05a994a6d034f65f8f0e6fd caeab1a34d4a6b4b636e070a38bce737") // sha256
    };
    byte[][] results_dd_50_md5 = {
            Util.h2b("56be34521d144c88dbb8c733f0e8b3f6"),
            null,
            null,
            null,
            null
    };
    byte[][] results_dd_50 = {
            null,
            Util.h2b("125d7342b9ac11cd91a39af48aa17b4f63f175d3"),
            Util.h2b("773ea91e36800e46854db8ebd09181a7 2959098b3ef8c122d9635514ced565fe"),
            Util.h2b("88062608d3e6ad8a0aa2ace014c8a86f 0aa635d947ac9febe83ef4e55966144b 2a5ab39dc13814b94e3ab6e101a34f27"),
            Util.h2b("fa73b0089d56a284efb0f0756c890be9 b1b5dbdd8ee81a3655f83e33b2279d39 bf3e848279a722c806b485a47e67c807 b946a337bee8942674278859e13292fb"),
    };
    byte[][] results_cd_50 = {
            Util.h2b("697eaf0aca3a3aea3a75164746ffaa79"),
            Util.h2b("4c9007f4026250c6bc8414f9bf50c86c2d7235da"),
            Util.h2b("82558a389a443c0ea4cc819899f2083a 85f0faa3e578f8077a2e3ff46729665b"),
            Util.h2b("3e8a69b7783c25851933ab6290af6ca7 7a9981480850009cc5577c6e1f573b4e 6801dd23c4a7d679ccf8a386c674cffb"),
            Util.h2b("b0ba465637458c6990e5a8c5f61d4af7 e576d97ff94b872de76f8050361ee3db a91ca5c11aa25eb4d679275cc5788063 a5f19741120c4f2de2adebeb10a298dd")
    };
    byte[][] results_c0c = {
            null,
            Util.h2b("4c1a03424b55e07fe7f27be1d58bb9324a9a5a04"),
            Util.h2b("a3b6167473100ee06e0c796c2955552b"),
            Util.h2b("3abf34c3503b2a23a46efc619baef897"),
            Util.h2b("415fad6271580a531d4179bc891d87a6")
    };
    byte[][] results_c0c_md5 = {
            Util.h2b("56461ef2342edc00f9bab995690efd4c"),
            null,
            null,
            null,
            null

    };
    byte[][] results_aa_80 = {
            Util.h2b("6b1ab7fe4bd7bf8f0b62e6ce61b9d0cd"),
            Util.h2b("aa4ae5e15272d00e95705637ce8a3b55ed402112"),
            null,
            null,
            null
    };
    byte[][] results_aa_131 = {
            null,
            null,
            Util.h2b("60e431591ee0b67f0d8a26aacbf5b77f 8e0bc6213728c5140546040f0ee37f54"),
            Util.h2b("4ece084485813e9088d2c63a041bc5b4 4f9ef1012a2b588f3cd11f05033ac4c6 0c2ef6ab4030fe8296248df163f44952"),
            Util.h2b("80b24263c7c1a3ebb71493c1dd7be8b4 9b46d1f41b4aeec1121b013783f8f352 6b56d037e05f2598bd0fd2215d6a1e52 95e64f73f63f0aec8b915a985d786598"),
    };
    byte[][] results_aa_80_b = {
            Util.h2b("6f630fad67cda0ee1fb1f562db3aa53e"),
            Util.h2b("e8e99d0f45237d786d6bbaa7965c7808bbff1a91"),
            null,
            null,
            null
    };
    byte[][] results_aa_131_b = {
            null,
            null,
            Util.h2b("9b09ffa71b942fcb27635fbcd5b0e944 bfdc63644f0713938a7f51535c3a35e2"),
            Util.h2b("6617178e941f020d351e2f254e8fd32c 602420feb0b8fb9adccebb82461e99c5 a678cc31e799176d3860e6110c46523e"),
            Util.h2b("e37b6a775dc87dbaa4dfa9f96e5e3ffd debd71f8867289865df5a32d20cdc944 b6022cac3c4982b10d5eeb55c3e4de15 134676fb6de0446065c97440fa8c6a58"),
    };
    TestCase[] tcs = {
            new TestCase(
                    Util.h2b("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b"),
                    "Hi There".getBytes(),
                    results_hithere_16
            ),
            new TestCase(
                    Util.h2b("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b"),
                    "Hi There".getBytes(),
                    results_hithere
            ),
            new TestCase(
                    "Jefe".getBytes(),
                    "what do ya want for nothing?".getBytes(),
                    results_jefe
            ),
            new TestCase(
                    Util.h2b("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
                    dd_50,
                    results_dd_50_md5
            ),
            new TestCase(
                    Util.h2b("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),
                    dd_50,
                    results_dd_50
            ),
            new TestCase(
                    Util.h2b("0102030405060708090a0b0c0d0e0f10 111213141516171819"),
                    cd_50,
                    results_cd_50
            ),
//            new TestCase(
//                    h2b("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c"),
//                    "Test With Truncation".getBytes(),
//                    results_c0c
//            ),
//            new TestCase(
//                    h2b("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c"),
//                    "Test With Truncation".getBytes(),
//                    results_c0c_md5
//            ),
            new TestCase(
                    aa_80,
                    "Test Using Larger Than Block-Size Key - Hash Key First".getBytes(),
                    results_aa_80
            ),
            new TestCase(
                    aa_131,
                    "Test Using Larger Than Block-Size Key - Hash Key First".getBytes(),
                    results_aa_131
            ),
            new TestCase(
                    aa_80,
                    "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data".getBytes(),
                    results_aa_80_b
            ),
            new TestCase(
                    aa_131,
                    "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.".getBytes(),
                    results_aa_131_b
            ),

    };

    @Test
    public void testGenerateKey() throws Exception {
        for (Hash.Algorithm a : Hash.Algorithm.values()) {
            assertEquals(HMAC.generateKey(a).length, Hash.blockSizeBytes(a));
        }
    }

    @Test
    public void testHmacMD5() throws Exception {
        int tc_num = 0;
        for (TestCase t : tcs) {
            if (null != t.digests[tc_num]) {
                //p(HMAC.hmacMD5(t.key, t.data));
                //p("--");
                //p(t.digests[tc_num]);
                assertEquals(HMAC.hmacMD5(t.key, t.data), t.digests[tc_num]);
            }
        }

    }

    @Test
    public void testHmacSHA1() throws Exception {
        int tc_num = 1;
        for (TestCase t : tcs) {
            if (null != t.digests[tc_num]) {
                assertEquals(HMAC.hmacSHA1(t.key, t.data), t.digests[tc_num]);
            }
        }

    }

    @Test
    public void testHmacSHA256() throws Exception {
        int tc_num = 2;
        for (TestCase t : tcs) {
            if (null != t.digests[tc_num]) {
                //p(HMAC.hmacSHA256(t.key, t.data));
                //p("--");
                //p(t.digests[tc_num]);
                assertEquals(HMAC.hmacSHA256(t.key, t.data), t.digests[tc_num]);
            }
        }


    }

    @Test
    public void testHmacSHA384() throws Exception {

        int tc_num = 3;
        for (TestCase t : tcs) {
            if (null != t.digests[tc_num]) {
                assertEquals(HMAC.hmacSHA384(t.key, t.data), t.digests[tc_num]);
            }
        }
    }

    @Test
    public void testHmacSHA512() throws Exception {
        int tc_num = 4;
        for (TestCase t : tcs) {
            if (null != t.digests[tc_num]) {
                assertEquals(HMAC.hmacSHA512(t.key, t.data), t.digests[tc_num]);
            }
        }
    }

    @Test
    public void testHmac() throws Exception {
        byte[] data = {0, 0, 0};
        for (Hash.Algorithm a : Hash.Algorithm.values()) {
            // make sure they're all considered.
            byte[] key = HMAC.generateKey(a);
            HMAC.hmac(a, key, data);
        }
    }

    class TestCase {
        byte[] key;
        byte[] data;
        byte[][] digests;

        TestCase(byte[] key, byte[] data, byte[][] digests) {
            this.key = key;
            this.data = data;
            this.digests = digests;
        }
    }

}