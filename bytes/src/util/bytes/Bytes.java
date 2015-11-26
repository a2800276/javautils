package util.bytes;

import java.util.Arrays;

import static java.lang.Character.digit;


public class Bytes {
    final static byte[] EMPTY = {};
    final static byte[] hex = "0123456789abcdef".getBytes();
    final static java.util.regex.Pattern WS = java.util.regex.Pattern.compile("\\s+");
	private static java.util.Random rnd = new java.util.Random();

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
		StringBuilder buf = new StringBuilder();
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

	/**
	 * Not secure randomness. Use util.crypto.Random.random
	 *
	 * @param count length of the array to return.
	 */
	public static byte[] random(int count) {
		byte[] bs = new byte[count];
		rnd.nextBytes(bs);
		return bs;
	}

	/**
	 * Appends all the provided byte arrays into a single array.
	 */
	public static byte[] append(byte[]... data) {
		if (data == null) {
			throw new NullPointerException();
		}
		int len = 0;
		for (byte[] bs : data) {
			if (null == bs) {
				throw new NullPointerException();
			}
			len += bs.length;
		}
		byte[] joined = new byte[len];
		int pos = 0;
		for (byte[] bs : data) {
			System.arraycopy(bs, 0, joined, pos, bs.length);
			pos += bs.length;
		}
		return joined;
	}

	/**
	 * Split an array into two halfes at `pos`. I.e. `pos` is the
	 * position of the first byte of the second array.
	 *
	 * @param bytes a non empty array to split
	 * @return an array consisting of two byte arrays, one of which may be empty.
	 */
	public static byte[][] split(byte[] bytes, int pos) {
		if (bytes == null || pos < 0 || pos >= bytes.length) {
			throw new IllegalArgumentException("bytes or pos invalid");
		}
		byte[] bs_one = new byte[pos];
		byte[] bs_two = new byte[bytes.length - pos];

		System.arraycopy(bytes, 0, bs_one, 0, pos);
		System.arraycopy(bytes, pos, bs_two, 0, bytes.length - pos);

		byte[][] ret = {bs_one, bs_two};
		return ret;
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
