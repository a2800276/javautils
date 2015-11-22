package util.bytes.bytes;

import java.util.Arrays;


public class ByteUtils {
    public static byte[] h2b(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            String h = hex.substring(i, i + 2);
            bytes[i / 2] = (byte) Integer.parseInt(h, 16);
        }
        return bytes;
    }

    public static String b2h(byte[] bytes) {
        if (bytes == null) {
            return "nothing received :<";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b & 0xff));
        }
        return sb.toString();
    }

    public static String b2s(byte[] bytes) {
        StringBuffer buf = new StringBuffer();
        for (byte b : bytes) {
            int i = b & 0xff;
            if (i >= 0x20 && i < 0x7F) {
                buf.append((char) i);
            } else {
                buf.append('.');
            }
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        p(b2s(h2b("5448884063169011D1711201158090354F")));
    }

    public static String i2h(int i) {
        return Integer.toHexString(i);
    }

    public static String[] dissect(byte[] bytes, int[] lens) {

        int sum = 0;
        for (int j : lens) {
            sum += j;
        }
        if (bytes == null || bytes.length != sum) {
            return null;
        }

        String[] res = new String[lens.length];

        for (int i = 0, pos = 0; i != lens.length; ++i) {
            res[i] = b2h(Arrays.copyOfRange(bytes, pos, lens[i]));
            pos += lens[i];
        }
        return res;
    }

    public static void p(Object o) {
        System.out.println(o);
    }

    public static void p(byte[] bytes) {
        p(b2h(bytes));
    }

}
