package util.crypto;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static util.bytes.bytes.Bytes.h2b;


/**
 * Created by a2800276 on 2015-10-31.
 */
public class HashTest {
    // http://www.nsrl.nist.gov/testdata/
    static final byte[] abc = "abc".getBytes();
    static final byte[] abc_sha1 = h2b("a9993e36 4706816a ba3e2571 7850c26c 9cd0d89d");
    static final byte[] abc_md5 = h2b("90015098 3CD24FB0 D6963F7D 28E17F72");
    static final byte[] abc_sha256 = h2b("BA7816BF 8F01CFEA 414140DE 5DAE2223 B00361A3 96177A9C B410FF61 F20015AD");

    static final byte[] abc_etc = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes();
    static final byte[] abc_etc_pt1 = "abcdbcdecdefdefgefghfghighijhijkijkljkl".getBytes();
    static final byte[] abc_etc_pt2 = "mklmnlmnomnopnopq".getBytes();

    static final byte[] abc_etc_sha1 = h2b("84983e44 1c3bd26e baae4aa1 f95129e5 e54670f1");
    static final byte[] abc_etc_md5 = h2b("8215EF07 96A20BCA AAE116D3 876C664A");
    static final byte[] abc_etc_sha256 = h2b("248D6A61 D20638B8 E5C02693 0C3E6039 A33CE459 64FF2167 F6ECEDD4 19DB06C1");

    static final byte[] a_times_million = new byte[1000000];
    static final byte[] a_s_sha1 = h2b("34aa973c d4c4daa4 f61eeb2b dbad2731 6534016f");
    static final byte[] a_s_md5 = h2b("7707D6AE 4E027C70 EEA2A935 C2296F21");
    static final byte[] a_s_sha256 = h2b("CDC76E5C 9914FB92 81A1C7E2 84D73E67 F1809A48 A497200E 046D39CC C7112CD0");
    static final byte[] abc_sha384 = h2b(
            "CB00753F 45A35E8B B5A03D69 9AC65007 272C32AB 0EDED163 1A8B605A 43FF5BED 8086072B A1E7CC23 58BAECA1 34C825A7"
    );

    // http://csrc.nist.gov/groups/ST/toolkit/documents/Examples/SHA_All.pdf
    static final byte[] abc_sha512 = h2b("DDAF35A1 93617ABA CC417349 AE204131 12E6FA4E 89A97EA2 0A9EEEE6 4B55D39A 2192992A 274FC1A8 36BA3C23 A3FEEBBD 454D4423 643CE80E 2A9AC94F A54CA49F");
    static final byte[] abc_etc2 = "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu".getBytes();
    static final byte[] abc_etc2_sha384 = h2b("09330C33 F71147E8 3D192FC7 82CD1B47 53111B17 3B3B05D2 2FA08086 E3B0F712 FCC7C71A 557E2DB9 66C3E9FA 91746039");
    static final byte[] abc_etc2_sha512 = h2b("8E959B75 DAE313DA 8CF4F728 14FC143F 8F7779C6 EB9F7FA1 7299AEAD B6889018 501D289E 4900F7E4 331B99DE C4B5433A C7D329EE B6DD2654 5E96E55B 874BE909");

    static {
        for (int i = 0; i != 1000000; ++i) {
            a_times_million[i] = 0x61;
        }
    }

    @Test
    public void testMd5() throws Exception {
        assertEquals(Hash.md5(abc), abc_md5);
        assertEquals(Hash.md5(abc_etc), abc_etc_md5);
        assertEquals(Hash.md5(a_times_million), a_s_md5);
    }

    @Test
    public void testSha1() throws Exception {
        assertEquals(Hash.sha1(abc), abc_sha1);
        assertEquals(Hash.sha1(abc_etc), abc_etc_sha1);
        assertEquals(Hash.sha1(a_times_million), a_s_sha1);
    }

    @Test
    public void testSha256() throws Exception {
        assertEquals(Hash.sha256(abc), abc_sha256);
        assertEquals(Hash.sha256(abc_etc), abc_etc_sha256);
        assertEquals(Hash.sha256(a_times_million), a_s_sha256);
    }

    @Test
    public void testSha384() throws Exception {
        assertEquals(Hash.sha384(abc), abc_sha384);
        assertEquals(Hash.sha384(abc_etc2), abc_etc2_sha384);
    }

    @Test
    public void testSha512() throws Exception {
        assertEquals(Hash.sha512(abc), abc_sha512);
        assertEquals(Hash.sha512(abc_etc2), abc_etc2_sha512);
    }

    @Test
    public void testHash() throws Exception {
        assertEquals(Hash.sha1(abc_etc_pt1, abc_etc_pt2), abc_etc_sha1);
        assertEquals(Hash.md5(abc_etc_pt1, abc_etc_pt2), abc_etc_md5);
        assertEquals(Hash.sha256(abc_etc_pt1, abc_etc_pt2), abc_etc_sha256);
    }
}