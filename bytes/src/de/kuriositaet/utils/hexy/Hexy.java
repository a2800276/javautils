package de.kuriositaet.utils.hexy;

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
        this.bytes = bytes == null ? EMPTY : bytes;
        this.cfg = cfg;
    }

    public static String toString(byte[] bytes) {
        return new Hexy(bytes).toString();
    }

    public static String toHex(byte b, boolean upper) {
        char[] map = upper ? Hexy.HEX : Hexy.hex;
        StringBuilder builder = new StringBuilder();

        builder.append(map[(b & 0xF0) >> 4]);
        builder.append(map[b & 0x0F]);

        return builder.toString();
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

    public static void main(String[] args) {
        byte[] bytes = "12345678901234567890".getBytes();
        Hexy h = new Hexy(bytes);
        p(h);
        Hexy.Config cfg = new Hexy.Config();
        cfg.format = Format.FOURS;
        cfg.numbering = Numbering.HEX_BYTES;
        h = new Hexy(bytes, cfg);
        p(h);

        byte[] bs = {(byte) -255, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0x30, 0x31, 0x32, 0x33, 0x39, 0x40, 0x41};
        h = new Hexy(bs);
        p(h);

        cfg.prefix = "bumsi!";
        cfg.indent = 5;
        h = new Hexy(bs, cfg);
        p(h);

        p(Hexy.toString(""));
        p(Hexy.toString("1"));

        for (int i = 0; i != 256; ++i) {
            //p(toHex( (byte)i, false));
        }
    }

    static void p(Object o) {
        System.out.println(o);
    }

    private String getIndent() {
        if (null == this.indent) {
            StringBuilder b = new StringBuilder();
            b.append(this.cfg.prefix);

            for (int i = 0; i != this.cfg.indent; ++i) {
                b.append(" ");
            }
            this.indent = b.toString();
        }
        return this.indent;
    }

    public String getString() {
        StringBuilder builder = new StringBuilder();
        String tmp = null;
        for (int i = 0; i < this.bytes.length; i += this.cfg.width) {
            byte[] bs = get(this.bytes, i, this.cfg.width);

            builder.append(getIndent());
            if (Numbering.HEX_BYTES == this.cfg.numbering) {
                builder.append(String.format("%07X: ", i));
            }
            if (Format.FOURS == this.cfg.format) {
                for (int j = 0; j < this.cfg.width; j += 2) {
                    byte[] printb = get(bs, j, 2);
                    tmp = toHex(printb, this.cfg.upper);
                    if (4 != tmp.length()) {
                        tmp += "  ";
                    }
                    if (4 != tmp.length()) {
                        tmp += "  ";
                    }
                    builder.append(tmp);
                    builder.append(" ");
                    //if (j+2 == this.cfg.width/2) {
                    //  builder.append(" ");
                    //}
                }
            } else {
                for (int j = 0; j != this.cfg.width; ++j) {
                    byte[] printb = get(bs, j, 1);
                    tmp = toHex(printb, this.cfg.upper);
                    if (2 != tmp.length()) {
                        tmp += "  ";
                    }
                    builder.append(tmp);
                    builder.append(" ");
                    if (j + 1 == this.cfg.width / 2) {
                        builder.append(" ");
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

    public String toString() {
        return this.getString();
    }

    public enum Numbering {
        NONE,
        HEX_BYTES
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
