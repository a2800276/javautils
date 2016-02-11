package util.bytes.base64;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class Base64Test {

    // public byte[] decode(String base64) {}

    static boolean arraycmp(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            return false;
        }
        for (int i = 0; i != b1.length; ++i) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }

    static void p(Object o) {
        System.out.println(o);
    }

    static void parr(byte[] arr) {
        for (int i = 0; i != arr.length; ++i) {
            p("" + i + ":" + arr[i]);
        }
    }

    @Test
    public void test() {
        check("bGVhc3VyZS4=".equals(Base64.encode("leasure.".getBytes())));
        check("ZWFzdXJlLg==".equals(Base64.encode("easure.".getBytes())));
        check("YXN1cmUu".equals(Base64.encode("asure.".getBytes())));
        check("c3VyZS4=".equals(Base64.encode("sure.".getBytes())));
        check("TWFu".equals(Base64.encode("Man".getBytes())));
        check("".equals(Base64.encode("".getBytes())));
        check("Zg==".equals(Base64.encode("f".getBytes())));
        check("Zm8=".equals(Base64.encode("fo".getBytes())));
        check("Zm9v".equals(Base64.encode("foo".getBytes())));
        check("Zm9vYg==".equals(Base64.encode("foob".getBytes())));
        check("Zm9vYmE=".equals(Base64.encode("fooba".getBytes())));
        check("Zm9vYmFy".equals(Base64.encode("foobar".getBytes())));

        byte[] neg = {0, -1, -2, -3, -4};
        check("AP/+/fw=".equals(Base64.encode(neg)));
        //p(new String(neg));


    }

    @Test
    public void test2() {
        check(arraycmp("leasure.".getBytes(), Base64.decode("bGVhc3VyZS4=")));
        check(arraycmp("easure.".getBytes(), Base64.decode("ZWFzdXJlLg==")));
        check(arraycmp("asure.".getBytes(), Base64.decode("YXN1cmUu")));
        check(arraycmp("sure.".getBytes(), Base64.decode("c3VyZS4=")));
        check(arraycmp("".getBytes(), Base64.decode("")));
        check(arraycmp("foobar".getBytes(), Base64.decode("Zm9vYmFy")));
        check(arraycmp("fooba".getBytes(), Base64.decode("Zm9vYmE=")));
        check(arraycmp("foob".getBytes(), Base64.decode("Zm9vYg==")));
        check(arraycmp("foo".getBytes(), Base64.decode("Zm9v")));
        check(arraycmp("fo".getBytes(), Base64.decode("Zm8=")));
        check(arraycmp("f".getBytes(), Base64.decode("Zg==")));
        byte[] neg = {0, -1, -2, -3, -4};
        check(arraycmp(neg, Base64.decode("AP/+/fw=")));

        //parr("leasure.".getBytes());
        //parr(Base64.decode("bGVhc3VyZS4="));

    }

    void check(boolean passed) {
        assertTrue(passed);
    }

}

