package util.json;

import java.math.BigDecimal;
import java.util.*;

/**
 * Simple JSON parsing for Java.  This class provides a rudimentary
 * callback implementation that may be passed to @see Lexer . In
 * case you are interested in writing your own specialized callback to
 * use with Lexer the callback contained herein may be a good
 * starting point.
 * <p>
 * JSON objects (`{"bla":1}`) are converted to `java.utils.Map'string` (Maps
 * in the interface HashMaps for the implementation), JSON arrrays are
 * converted to `java.util.List'string` (LinkedList for the implementation),
 * JSON Strings become Java Strings, Numbers become `BigDecimal`, `true`
 * and `false` are boolean and null is, well, null.
 * <p>
 *   	Numbers may be configured to be something else, i.e. int, long,
 *   	float or double. This is done by passing the constructor a corresponding
 *      `NumberType` enum value.
 * <p>
 * <h2> Usage </h2> 
 * <p>
 * <code>
 * String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
 * Object    o = JSON.parseJSON(json);
 * </code>
 * <p>
 * In the example above, `o` will be a `java.util.Map` containing three
 * keys, "a", "b" and "dingdong", each Strings. "a"'string value is a
 * BigDecimal, "b"'string an array containing the BigDecimal values 1, 2,
 * and 3 and "dingdong"'string value is another Map ...
 * <p>
 * The intended use case for this is to write non-blocking webservices,
 * this interface is meant to provide the functionality to process any
 * scrap of data that happens to be available on the network. This
 * requires a slightly more elaborate interface than the simple verson
 * above:
 * <p>
 * <code>
 * JSON json = new JSON();
 * while (arr = bytesAvailableFromSomewhere()) {
 *   j.parse(arr);
 *   if (json.done()) break;
 * }
 * Object result = json.obj();
 * </code>
 * <p>
 * <h2> Accepted JSON </h2>
 * <p>
 * This implementation should be able to handle any JSON conforming to
 * JSON as described here (http://json.org).
 * <p>
 * Technically, this parser accepts a superset of JSON that allows
 * redundant ':' and ',' inside of JSON objects and Arrays to be left
 * out, so it would accept:
 * <p>
 * <code>
 * { "a" 19560954609845.4456456 "b" [1 2 3] "dindong" {"b" 12}}
 * </code>
 * <p>
 * as the equivalent of:
 * <p>
 * <code>
 * { "a" : 19560954609845.4456456, "b" : [1 2 3], "dindong" : {"b" 12}}
 * </code>
 */
public class JSON {


	public enum NumberType {
    	 BigDecimal // default
		,Integer
		,Long
		,Float
		,Double
	}

    LexerCB cb;
    Object obj; // result.

	public JSON () {
		this(NumberType.BigDecimal);
	}
	public JSON (NumberType type) {
		this.cb = new LexerCB(type);
	}

    /**
     * Utility method to parse a String containing valid JSON
     */
    public static Object parse(String json) {
    	return parse(json, NumberType.BigDecimal);
    }

	/**
	 * Utility method to parse a String containing valid JSON returning
	 * a mapped object with the indicated number type, it is up to the
	 * caller to ensure that her numbers don't overflow and, e.g. don't
	 * contain decimal points for int's
	 */
	public static Object parse(String json, NumberType type) {
		LexerCB cb = new LexerCB(type);
		Lexer.lexer.lex(json.getBytes(), cb);
		return cb.stack.pop();
	}

	/**
     * Munge up an Object into JSON. There are a bunch of
     * cases this can't handle, for example: just any old stuff.
     * <p>
     * Object passed to this method needs to be:
     * <ul>
     * <li> primitive
     * <li> java.util.Map
     * <li> java.util.List
     * <li> an Array of one of the above
     * </ul>
     */
    public static String jsonify(Object o) {
        Encoder e = new Encoder();
        e.encode(o);
        return e.buf.toString();

        // Of course, there would be a number of ways to encode just any old
        // stuff. Easiest would be calling `toString` on unknown classes and
        // encoding their String representation. OR treat them as an
        // data-containers and encode all their public fields. OR treat them
        // as Beans(tm) [yuckyuckyuck].
        //
        // The best way to go would be some sort of `unknown class Handler`
        // to stuff into the encoder to roll your own strategy for dealing
        // with this... `CustomEncoder` attempts this.
        //
        // I have yet to decide an will do so when I need to.
    }

	/** Use "dynamic" encoding, which is just a term I made up. It means
	 * that Objects that can't be encoded using the default encoding rules
	 * BUT have a `toJSON` method will be encoded using said `toJSON` method.
	 * @param o the object to encode
	 * @return a JSON string
	 */
	public static String jsonifyDynamic(Object o) {
        Encoder e = new DynamicEncoder();
        e.encode(o);
        return e.buf.toString();
    }

	/**
	 * If you want to get super fancy you can write a custom encoder that
	 * really know the classes you want to encode. See the documentation
	 * for `CustomEncoder`, i.e. the source code.
	 * @param o the object to encode
	 * @param enc
	 * @return
	 */
	public static String jsonifyCustom(Object o, CustomEncoder enc) {
        enc.encode(o);
        return enc.buf.toString();
    }

    /**
     * Parse whatever bits of JSON you have available to you.
     */
    public void parse(byte[] arr) {
        parse(arr, 0, arr.length);
    }

    public void parse(byte[] arr, int off, int len) {
        Lexer.lexer.lex(arr, off, len, this.cb);
    }

    /**
     * Returns whether the parser is in a consistent, balanced state.
     * Once the parser is `done` passing further data to it via `parse`
     * will trigger an Exception.
     */
    public boolean done() {
        return this.cb.done;
    }

	/**
	 * reset the parser to reuse the instance once a complete JSON object has been parsed.
	 * */
	public void reset() {
		cb.reset();
	}

    /**
     * Retrieve the results of the parse. You need to ensure that the
     * complete JSON object has been passed to parse, else this will throw
     * and Exception. Ideally, call `done()` before trying to retrieve the
     * results
     */
    public Object obj() {
        if (!done()) {
            throw new JSONException("not done!");
        }
        if (null == this.obj) {
            this.obj = this.cb.stack.pop();
        }
        return this.obj;
    }

    public static class LexerCB extends Lexer.CB {

    	public LexerCB (NumberType type) {
    		this.type = type;
		}
		public LexerCB() {
    		this(NumberType.BigDecimal);
		}
		public void reset() {
    		super.reset();
    		this.stack = new Stack();
    		this.done = false;
    		this.expectNextCommaOrRCurly = false;
		}
        Stack<Object> stack = new Stack<Object>();
        boolean done;
        boolean expectNextCommaOrRCurly;
		NumberType type;

        void tok(Lexer.Token t) {

            if (done) {
                error();
            }
            if (expectNextCommaOrRCurly) {
                switch (t) {
                    case RCURLY:
                    case COMMA:
                        expectNextCommaOrRCurly = false;
                        break;
                    default:
                        error("unbalanced key value pairs");
                }
            }
            switch (t) {
                case LCURLY:
                    stack.push(map());
                    break;
                case LSQUARE:
                    stack.push(list());
                    break;
                case RCURLY:
                    if (!(stack.peek() instanceof Map)) {
                        error("misplaced }");
                    }
                    Map m = (Map) stack.pop();
                    stash(m);
                    break;
                case RSQUARE:
                    if (!(stack.peek() instanceof List)) {
                        error("misplaced ]");
                    }
                    List l = (List) stack.pop();
                    stash(l);
                    break;
                case TRUE:
                    stash(true);
                    break;
                case FALSE:
                    stash(false);
                    break;
                case NULL:
                    stash(null);
                    break;
                case COMMA:
                    // if (!(stack.peek() instanceof List)) {
                    //  error("misplaced ,");
                    // }
                    break;
                case COLON:
                    if (!(stack.peek() instanceof Key)) {
                        error("misplaced :");
                    }
                    break;
                default:
                    error();
            }
        }

        void tok(String s) {
            if (done) {
                error();
            }
            if (stack.peek() instanceof Map) {
                stack.push(new Key(s));
            } else {
                stash(s);
            }
        }

        void numberToken (CharSequence cs) {
			Object o;
        	switch (type) {
				case BigDecimal:
					o = new BigDecimal(cs.toString());
					break;
				case Double:
					o = Double.parseDouble( cs.toString() );
					break;
				case Float:
					o = Float.parseFloat( cs.toString() );
					break;
				case Integer:
					o = Integer.parseInt( cs.toString(), 10 );
					break;
				case Long:
					o = Long.parseLong( cs.toString(), 10 );
					break;
				default:
					throw new JSONException( "cannot happen: unknown number type" );
			}
			stash(o);
		}

        void stash(Object o) {
            // stack is empty, done
            if (0 == stack.size()) {
                done = true;
                stack.push(o);
                return;
            }
            Object top = stack.peek();
            if (top instanceof List) {
                ((List<Object>) top).add(o);
            } else if (top instanceof Key) {
                String key = ((Key) stack.pop()).string;
                assert stack.size() > 0;
                assert stack.peek() instanceof Map;
                ((Map<String, Object>) stack.peek()).put(key, o);
                expectNextCommaOrRCurly = true;
            } else {
                error("unexpected: " + o.getClass().getName() + " after: " + (stack.peek().getClass().getName()));
            }
        }

        Map map() {
            return new HashMap();
        }

        List list() {
            return new LinkedList();
        }

        void error() {
            error("calling parser in done state. did you forget to call reset()?");
        }

        void error(String m) {
            throw new JSONException(m);
        }
    }

    static class Key {
        String string;

        // Internal Marker class to keep track of keys and values on the
        // stack of the parser. A `Key` object may only be placed on top of
        // a `Map` (JSON Object). Encountering a `COLON` should only happen
        // when there is a `Key` on top of the stack, etc.
        Key(String string) {
            this.string = string;
        }
    }

}
