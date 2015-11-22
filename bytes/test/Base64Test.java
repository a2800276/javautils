/*
Copyright (c) 2011 Tim Becker. All rights reserved.
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE SOFTWARE. 
*/


package de.kuriositaet.utils.base64;

import junit.framework.TestCase;
import org.junit.Test;

public class Base64Test extends TestCase {

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

