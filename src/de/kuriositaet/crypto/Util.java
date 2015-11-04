package de.kuriositaet.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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
        byte[] bytes = new byte[chunkSize];
        int offset = 0;
        int lenRemaining = chunkSize - offset;
        int read;
        try {
            while (-1 != (read = is.read(bytes, offset, lenRemaining))) {
                offset += read;
                lenRemaining = chunkSize - offset;
                if (lenRemaining <= 0) {
                    chunkSize *= 2;
                    byte[] grown = new byte[chunkSize];
                    System.arraycopy(bytes, 0, grown, 0, bytes.length);
                    lenRemaining = chunkSize - offset;
                    bytes = grown;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close(is);
        if (lenRemaining != 0) {
            int lenRead = bytes.length - lenRemaining;
            byte[] retBytes = new byte[lenRead];
            System.arraycopy(bytes, 0, retBytes, 0, lenRead);
            bytes = retBytes;
        }
        return bytes;
    }

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) { /* e.printStackTrace(); */ }
        }
    }

    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) { /* e.printStackTrace(); */ }
        }
    }
}
