package util.bytes.hexy;

public class Hexy {

    static final byte[] EMPTY = new byte[0];
    static final char[] hex = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
    };
    static final char[] HEX = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    };

    byte[] bytes;
    Config cfg;
    String indent;


    public Hexy(byte[] bytes) {
        this(bytes, new Config());
    }

    public Hexy(byte[] bytes, Hexy.Config cfg) {
		this(cfg);
		this.bytes = bytes == null ? EMPTY : bytes;
	}

	public Hexy() {
		this(new Config());
	}

	public Hexy(Hexy.Config cfg) {
		this.cfg = cfg;
	}

    public static String toString(byte[] bytes) {
        return new Hexy(bytes).toString();
    }

    public static String toHex(byte b, boolean upper) {
        char[] map = upper ? Hexy.HEX : Hexy.hex;
		char[] strChars = new char[2];
		strChars[0] = map[(b & 0xF0) >> 4];
		strChars[1] = map[(b & 0xF0)];

		return new String(strChars);
	}

    public static String toHex(byte[] bs, boolean upper) {
        char[] map = upper ? Hexy.HEX : Hexy.hex;
        StringBuilder builder = new StringBuilder();
        for (byte b : bs) {
            builder.append(map[(b & 0xF0) >> 4]);
            builder.append(map[b & 0x0F]);
        }
        return builder.toString();
    }

    public static String toString(String bytes) {
        if (null == bytes) {
            return "";
        }
        return toString(bytes.getBytes());
    }

    private static byte[] get(byte[] bytes, int offset, int num) {
        if (offset >= bytes.length) {
            return EMPTY;
        }
        int d = (offset + num) - bytes.length;

        if (d > 0) {
            num -= d;
        }

        byte[] ret = new byte[num];
        System.arraycopy(bytes, offset, ret, 0, num);
        return ret;

        // 012345
        // len = 6
        // offset 3, num 3
        //    345
        // offset 4, num 3
        //     45
        //
        // 0123
        // offset 5, num 1
        //

    }


    static void p(Object o) {
        System.out.println(o);
    }

    private String getIndent() {
        if (null == this.indent) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i != this.cfg.indent; ++i) {
                b.append(" ");
            }
			b.append(this.cfg.prefix);
			this.indent = b.toString();
		}
		return this.indent;
    }

	public String hexDump(byte[] bytes) {
		return getString(bytes);
	}

	/**
	 * @deprecated
	 */
	public String getString(byte[] bytes) {
		bytes = bytes == null ? EMPTY : bytes;
		StringBuilder builder = new StringBuilder();
		String tmp = null;
		for (int i = 0; i < bytes.length; i += this.cfg.width) {
			byte[] bs = get(bytes, i, this.cfg.width);
			builder.append(getIndent());
			if (Numbering.HEX_BYTES == this.cfg.numbering) {
				builder.append(String.format("%07X: ", i));
			} else if (Numbering.BASE_10 == this.cfg.numbering) {
				builder.append(String.format("%07d: ", i));
			}
			if (Format.FOURS == this.cfg.format) {
				for (int j = 0; j < this.cfg.width; j += 2) {
					byte[] printb = get(bs, j, 2);
					tmp = toHex(printb, this.cfg.upper);
					while (4 != tmp.length()) {
						tmp += "  ";
					}
					builder.append(tmp);
					builder.append(" ");
				}
			} else {
				for (int j = 0; j != this.cfg.width; ++j) {
					byte[] printb = get(bs, j, 1);
					tmp = toHex(printb, this.cfg.upper);
					if (2 != tmp.length()) {
						tmp += "  ";
					}
					builder.append(tmp);
					if (Format.TWOS == this.cfg.format) {
						builder.append(" ");
						if (j + 1 == this.cfg.width / 2) {
							builder.append(" ");
						}
					}
				}
			}
			if (Annotate.ASCII == this.cfg.annotate) {
				builder.append(" ");
				for (int j = 0; j != this.cfg.width; ++j) {
					if (j == bs.length) {
						break;
					}
					char b = (char) bs[j];
					if (0x7f > b && 0x19 < b) {
						builder.append(b);
					} else {
						builder.append('.');
					}
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * @deprecated
	 */
	public String getString() {
		return getString(this.bytes);
	}

    public String toString() {
        return this.getString();
    }

    public enum Numbering {
		NONE,
		HEX_BYTES,
		BASE_10
	}

    public enum Format {
        NONE,
        TWOS,
        FOURS
    }

    public enum Annotate {
        NONE,
        ASCII
    }

    public static class Config {
        public int width = 16;
        public Numbering numbering = Numbering.NONE;
        public Format format = Format.TWOS;
        public boolean upper;
        public Annotate annotate = Annotate.ASCII;
        public String prefix = "";
        public int indent = 0;
    }
}
