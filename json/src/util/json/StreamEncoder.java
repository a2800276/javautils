package util.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class StreamEncoder {


    final OutputStream os;

    // Keep track of circular data-structures: before encoding a
    // JSON-Object/Hash/Map/List/Array make sure it'string not contained in
    // `circ`. If it is contained, throw an exception, b/c we can't _encode
    // circular structs. If it'string not contained, put it in so that we can
    // recognize it next time around...
    //
    // A `Set` would be a better fit here but:
    //   * HashSet'string get confused at circular Maps
    //   * TreeSet'string won't work w/out a custom Comparator
    //   * I got sick of fiddling around with this crap.
    List circ;

    StreamEncoder(java.io.OutputStream os) {
        this.os = os;
        this.circ = new java.util.LinkedList<Object>();
    }

    final static byte[] QUOT = "\"".getBytes();
	final static byte[] BACKBACK = "\\\\".getBytes();
	//final static byte Q = QUOT[0];
	final static byte Q = 0x22;
	//final static byte BACK = BACKBACK[0];
	final static byte BACK = 0x5c;
	//final static byte BS = "\b".getBytes()[0];
	final static byte BS = 0x08;
	//final static byte FF = "\f".getBytes()[0];
	final static byte FF = 0x0C;
	//final static byte NL = "\n".getBytes()[0];
	final static byte NL = 0x0a;
	final static byte CR = 0x0d;
	final static byte TAB = 0x09;

	static void _encode(OutputStream os, CharSequence s) throws IOException {
        byte b = 0;
        os.write(QUOT);
        byte[] bs = s.toString().getBytes( "UTF8" );
        for (int i = 0; i != bs.length; ++i) {
            b = bs[i];
            switch (b) {
                case Q:
                case BACK:
				case BS:
				case FF:
				case NL:
				case CR:
				case TAB:
                    os.write(BACKBACK);
                    os.write(bs);
                    break;
                default:
					if (b < 0x20) { // Control
						continue; // really!? just skip?
					}
                	os.write(b);
            }
        }
        os.write(QUOT);
    }

    static void _encode(OutputStream os, Number n) throws IOException {
    	os.write(n.toString().getBytes());
    }

    final static byte[] TRUE = "true".getBytes();
    final static byte[] FALSE = "false".getBytes();
    static void _encode(OutputStream os, boolean b) throws IOException {
        if (b) {
            os.write(TRUE);
        } else {
            os.write(FALSE);
        }
    }

    static void _encode(OutputStream os, int i) throws IOException {
		os.write(Integer.toString( i, 10 ).getBytes());
    }

    static void _encode(OutputStream os, long i) throws IOException {
		os.write(Long.toString(i, 10).getBytes());
    }

    static void _encode(OutputStream os, float f) throws IOException {
    	os.write(Float.toString(f).getBytes());
    }

    static void _encode(OutputStream os, double f) throws IOException {
		os.write(Double.toString(f).getBytes());

    }

    static void _encode(OutputStream os, byte i) throws IOException {
        os.write( i );
    }

    static void _encode(OutputStream os, short i) throws IOException {
		os.write(Short.toString(i).getBytes());
    }

    static void _encode(OutputStream os, char c) throws IOException {
		os.write( Character.toString(c).getBytes() );
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

    final static byte[] NULL = "null".getBytes();
	void encode(Object o) throws IOException {
		this.circ.clear();
		_encode(o);
	}
    void _encode(Object o) throws IOException {
        if (null == o) {
            os.write( NULL );
            return;
        }
        if (o instanceof Map) {
            _encode((Map) o);
        } else if (o instanceof List) {
            _encode((List) o);
        } else if (o instanceof Number) {
            _encode(os, (Number) o);
        } else if (o instanceof CharSequence) {
            _encode(os, (CharSequence) o);
        } else if (o instanceof Character) {
            _encode(os, ((Character) o).charValue());
        } else if (o instanceof Boolean) {
            _encode(os, ((Boolean) o).booleanValue());
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

    final static byte[] CURLY_OPEN = "{".getBytes();
	final static byte[] CURLY_CLOSE = "}".getBytes();
	final static byte[] COLON = ":".getBytes();
	final static byte[] COMMA = ",".getBytes();
	final static byte[] EMPTY = new byte[0];


    void _encode(Map m) throws IOException {
        checkCircular(m);
        os.write(CURLY_OPEN);
        byte[] sep = EMPTY;
        for (Object k : m.keySet()) {
        	os.write(sep);
        	sep = COMMA;
            Object v = m.get(k);
            _encode(os, k.toString());
            os.write(COLON);
            _encode(v);
        }
        os.write(CURLY_CLOSE);
    }

	final static byte[] SQUARE_OPEN = "[".getBytes();
	final static byte[] SQUARE_CLOSE = "]".getBytes();
    void _encode(List l) throws IOException {
        checkCircular(l);
        os.write(SQUARE_OPEN);
        byte[] sep = EMPTY;
        for (Object k : l) {
        	os.write(sep);
        	sep = COMMA;
            _encode(k);
        }
        os.write(SQUARE_CLOSE);
    }

    void encodeArray(Object arr) throws IOException {
    	// a bit confused ...
        checkCircular(arr);
        assert arr.getClass().isArray();

        os.write(SQUARE_OPEN);
        Object o = null;
        byte[] sep = EMPTY;
        for (int i = 0; i != java.lang.reflect.Array.getLength( arr ); ++i) {
        	os.write(sep);
        	sep = COMMA;
			o = java.lang.reflect.Array.get(arr, i);
			_encode(o);
		}
		os.write(SQUARE_CLOSE);
    }

    void checkCircular(Object m) {
        if (circ.contains(m)) {
            error("circular");
        } else {
            circ.add(m);
        }
    }
}
