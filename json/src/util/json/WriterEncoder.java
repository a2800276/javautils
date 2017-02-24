package util.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class WriterEncoder {


    final Writer writer;

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

    WriterEncoder(Writer w) {
		this.writer = w;
		this.circ = new java.util.LinkedList<Object>();
	}
    WriterEncoder(OutputStream os) {
        this(new OutputStreamWriter( os ));
    }

    final static char QUOT = '"';
	final static char[] BACKBACK = {'\\', '\\'};
	//final static byte Q = QUOT[0];
	//final static byte BACK = BACKBACK[0];
	final static char BACK = '\\';
	//final static byte BS = "\b".getBytes()[0];
	final static char BS = '\b';
	//final static byte FF = "\f".getBytes()[0];
	final static char FF = '\f';
	//final static byte NL = "\n".getBytes()[0];
	final static char NL = '\n';
	final static char CR = '\r';
	final static char TAB = '\t';

	static void _encode(Writer writer, CharSequence s) throws IOException {
        char c = 0;
        writer.write(QUOT);
        byte[] bs = s.toString().getBytes( "UTF8" );
        for (int i = 0; i != s.length(); ++i) {
            c = s.charAt( i );
            switch (c) {
                case QUOT:
                case BACK:
				case BS:
				case FF:
				case NL:
				case CR:
				case TAB:
                    writer.write(BACKBACK);
                    writer.write(c);
                    break;
                default:
					if (c < 0x20) { // Control
						continue; // really!? just skip?
					}
                	writer.write(c);
            }
        }
        writer.write(QUOT);
    }

    static void _encode(Writer writer, Number n) throws IOException {
    	writer.write(n.toString());
    }

    final static char[] TRUE = "true".toCharArray();
    final static char[] FALSE = "false".toCharArray();
    static void _encode(Writer writer, boolean b) throws IOException {
        if (b) {
            writer.write(TRUE);
        } else {
            writer.write(FALSE);
        }
    }

    static void _encode(Writer writer, int i) throws IOException {
		writer.write(Integer.toString( i, 10 ));
    }

    static void _encode(Writer writer, long i) throws IOException {
		writer.write(Long.toString(i, 10));
    }

    static void _encode(Writer writer, float f) throws IOException {
    	writer.write(Float.toString(f));
    }

    static void _encode(Writer writer, double f) throws IOException {
		writer.write(Double.toString(f));

    }

    static void _encode(Writer writer, byte i) throws IOException {
        writer.write( i );
    }

    static void _encode(Writer writer, short i) throws IOException {
		writer.write(Short.toString(i));
    }

    static void _encode(Writer writer, char c) throws IOException {
		writer.write( c );
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

    final static char[] NULL = "null".toCharArray();
	void encode(Object o) throws IOException {
		this.circ.clear();
		_encode(o);
	}
    void _encode(Object o) throws IOException {
        if (null == o) {
            writer.write( NULL );
            return;
        }
        if (o instanceof Map) {
            _encode((Map) o);
        } else if (o instanceof List) {
            _encode((List) o);
        } else if (o instanceof Number) {
            _encode( writer, (Number) o);
        } else if (o instanceof CharSequence) {
        	writer.append( (CharSequence) o);
        } else if (o instanceof Character) {
        	writer.write((Character)o);
        } else if (o instanceof Boolean) {
            _encode( writer, ((Boolean) o).booleanValue());
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

    final static char CURLY_OPEN = '{';
	final static char CURLY_CLOSE = '}';
	final static char COLON = ':';
	final static char COMMA = ',';


    void _encode(Map m) throws IOException {
        checkCircular(m);
        writer.write(CURLY_OPEN);
        char sep = 0;
        for (Object k : m.keySet()) {
        	if (sep != 0) {writer.write(sep);} else { sep = COMMA;}
            Object v = m.get(k);
            _encode( writer, k.toString());
            writer.write(COLON);
            _encode(v);
        }
        writer.write(CURLY_CLOSE);
    }

	final static char SQUARE_OPEN = '[';
	final static char SQUARE_CLOSE = ']';
    void _encode(List l) throws IOException {
        checkCircular(l);
        writer.write(SQUARE_OPEN);
       	char sep = 0;
        for (Object k : l) {
			if (sep != 0) {writer.write(sep);} else { sep = COMMA;}
            _encode(k);
        }
        writer.write(SQUARE_CLOSE);
    }

    void encodeArray(Object arr) throws IOException {
    	// a bit confused ...
        checkCircular(arr);
        assert arr.getClass().isArray();

        writer.write(SQUARE_OPEN);
        Object o = null;
		char sep = 0;
		for (int i = 0; i != java.lang.reflect.Array.getLength( arr ); ++i) {
			if (sep != 0) {writer.write(sep);} else { sep = COMMA;}
			o = java.lang.reflect.Array.get(arr, i);
			_encode(o);
		}
		writer.write(SQUARE_CLOSE);
    }

    void checkCircular(Object m) {
        if (circ.contains(m)) {
            error("circular");
        } else {
            circ.add(m);
        }
    }
}
