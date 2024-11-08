package util.json;

import java.util.List;
import java.util.Map;

public class Encoder {

    StringBuilder buf;

    // Keep track of circular data-structures: before encoding a
    // JSON-Object/Hash/Map/List/Array make sure it'string not contained in
    // `circ`. If it is contained, throw an exception, b/c we can't encode
    // circular structs. If it'string not contained, put it in so that we can
    // recognize it next time around...
    //
    // A `Set` would be a better fit here but:
    //   * HashSet'string get confused at circular Maps
    //   * TreeSet'string won't work w/out a custom Comparator
    //   * I got sick of fiddling around with this crap.
    List circ;

    Encoder() {
        this.buf = new StringBuilder();
        this.circ = new java.util.LinkedList<Object>();
    }

    static void encode(StringBuilder buf, CharSequence s) {
        char c = 0;
        buf.append('"');
        for (int i = 0; i != s.length(); ++i) {
            c = s.charAt(i);
            if (Character.isISOControl(c)) {
                continue; // really!? just skip?
            }
            switch (c) {
                case '"':
                case '\\':
                case '\b':
                case '\f':
                case '\n':
                case '\r':
                case '\t':
                    buf.append('\\');
                    buf.append(c);
                    continue;
                default:
                    buf.append(c);
            }
        }
        buf.append('"');
    }

    static void encode(StringBuilder buf, Number n) {
        buf.append(n.toString());
    }

    static void encode(StringBuilder buf, boolean b) {
        if (b) {
            buf.append("true");
        } else {
            buf.append("false");
        }
    }

    static void encode(StringBuilder buf, int i) {
        buf.append(i);
    }

    static void encode(StringBuilder buf, long i) {
        buf.append(i);
    }

    static void encode(StringBuilder buf, float i) {
        buf.append(i);
    }

    static void encode(StringBuilder buf, double i) {
        buf.append(i);
    }

    static void encode(StringBuilder buf, byte i) {
        buf.append(i);
    }

    static void encode(StringBuilder buf, short i) {
        buf.append(i);
    }

    static void encode(StringBuilder buf, char c) {
        buf.append((int) c);
    }

    /**
     * override this in subclasses to allow custom encoding
     */
    boolean canEncode(Object o) {
        return true;
    }

    void encodeCustom(Object o) {
        error("unexpected object: " + o.getClass());
    }

    void encode(Object o) {
        if (null == o) {
            buf.append("null");
            return;
        }
        if (o instanceof Map) {
            encode((Map) o);
        } else if (o instanceof List) {
            encode((List) o);
        } else if (o instanceof Number) {
            encode(buf, (Number) o);
        } else if (o instanceof CharSequence) {
            encode(buf, (CharSequence) o);
        } else if (o instanceof Character) {
            encode(buf, ((Character) o).charValue());
        } else if (o instanceof Boolean) {
            encode(buf, ((Boolean) o).booleanValue());
        } else if (o.getClass().isArray()) {
            encodeArray(o);
        } else {
            if (canEncode(o)) {
                encodeCustom(o);
            } else {
                error(o.getClass());
            }
        }
    }

    void error(Object o) {
        throw new JSONException(o.toString());
    }

    void encode(Map m) {
        checkCircular(m);
        buf.append('{');
        for (Object k : m.keySet()) {
            Object v = m.get(k);
            encode(buf, k.toString());
            buf.append(':');
            encode(v);
            buf.append(",");
        }
        buf.setCharAt(buf.length() - 1, '}');
        unCheckCircular();
    }

    void encode(List l) {
        checkCircular(l);
        buf.append('[');
        for (Object k : l) {
            encode(k);
            buf.append(",");
        }
        buf.setCharAt(buf.length() - 1, ']');
        unCheckCircular();
    }

    void encodeArray(Object arr) {
        checkCircular(arr);
        assert arr.getClass().isArray();

        buf.append('[');
        Object o = null;
        for (int i = 0; ; ++i) {
            try {
                o = java.lang.reflect.Array.get(arr, i);
                encode(o);
                buf.append(",");
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                break;
            }
        }
        buf.setCharAt(buf.length() - 1, ']');
        unCheckCircular();
    }

    void checkCircular(Object m) {
        if (circ.contains(m)) {
            error("circular");
        } else {
            circ.add(m);
        }
    }
    void unCheckCircular() {
        circ.remove(circ.size() - 1);
    }
}
