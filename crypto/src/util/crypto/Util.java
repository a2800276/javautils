package util.crypto;

import java.io.*;

import static java.lang.Character.digit;

/**
 * Created by a2800276 on 2015-10-31.
 */
public class Util {
    final protected static byte[] hex = "0123456789abcdef".getBytes();
    private static final byte[] EMPTY = {};
    private static final java.util.regex.Pattern WS = java.util.regex.Pattern.compile("\\s+");
    public static PrintStream log = System.out;

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

    public static void p(byte[] bytes) {
        p(b2h(bytes));
    }

    public static void p(Object[] os) {
        for (Object o : os) {
            p(o);
        }
    }

    public static void p(Object o) {
        log.println(o);
    }

    public static byte[] readAllClose(InputStream is) {
        return readAllClose(is, 1024 * 1024);

    }

    public static byte[] readAllClose(InputStream is, int chunkSize) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[chunkSize];
        int read;
        try {
            while (-1 != (read = is.read(bytes))) {
                bos.write(bytes, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(is);
        }
        return bos.toByteArray();
    }

    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) { /* e.printStackTrace(); */ }
        }
    }

}
