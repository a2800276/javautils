package util.bytes.bytes;

import java.util.Arrays;

import static java.lang.Character.digit;


public class Bytes {
    final static byte[] EMPTY = {};
    final static byte[] hex = "0123456789abcdef".getBytes();
    final static java.util.regex.Pattern WS = java.util.regex.Pattern.compile("\\s+");

    public static String b2h(byte[] bytes) {
        return b2h(bytes, 0, bytes.length);
    }

    public static String b2h(byte[] bytes, int offset, int count) {
        byte[] hexBytes = new byte[count * 2];
        for (int j = 0; j < count; j++) {
            int v = bytes[j + offset] & 0xFF;
            hexBytes[j * 2] = hex[v >>> 4];
            hexBytes[j * 2 + 1] = hex[v & 0x0F];
        }
        return new String(hexBytes);
    }
    /**
     * Converts the provided hex representation of bytes to a
     * byte array, ignoring any whitespace. (java regexp ws: [ \t\n\x0B\f\r]
     * (see: [...].regexp.Pattern javadoc.)
     * Return an empty array if provided an empty String or null.
     *
     * @param hex [a-fA-F0-9]
     * @return bytes represented by the string
     * @throws NumberFormatException if invalid chars are contained in the
     *                               provided String or an uneven number of chars are provided.
     */
    public static byte[] h2b(String hex) {
        if (null == hex || "".equals(hex)) {
            return EMPTY;
        }
        java.util.regex.Matcher m = WS.matcher(hex);
        hex = m.replaceAll("");
        if ((hex.length() % 2) != 0) {
            throw new NumberFormatException("uneven number of hex chars");
        }
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            data[i / 2] = (byte) ((h2b(hex.charAt(i)) << 4) | h2b(hex.charAt(i + 1)));
        }
        return data;
    }

    private static byte h2b(char c) {
        int i = digit(c, 16);
        if (i == -1) {
            throw new NumberFormatException("Invalid hex char: `" + c + "`");
        }
        return (byte) i;
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
