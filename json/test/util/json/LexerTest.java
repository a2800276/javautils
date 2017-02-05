package util.json;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class LexerTest {

    static int countTok;
    static int countTokString;
    static int countNumberToken;

    static Lexer.CB cb = new Lexer.CB() {
        void tok(Lexer.Token tok) {
            countTok++;
        }

        void tok(String c) {
            countTokString++;
        }

        void numberToken(CharSequence c) {
            countNumberToken++;
        }
    };

    @Test
    public static void testClean() {
        String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
        byte[] jsona = json.getBytes();


        Lexer.lexer.lex(jsona, cb);
        assertEquals(countTok, 14);
		assertEquals(countTokString, 4);
		assertEquals(countNumberToken, 5);

        countTok = countTokString = countNumberToken = 0;

        json = "{\"a\":\"\\u2603\",\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
        Lexer.lexer.lex(json.getBytes(), cb);
		assertEquals(countTok, 14);
		assertEquals(countTokString, 5);
		assertEquals(countNumberToken, 4);

        countTok = countTokString = countNumberToken = 0;

        json = "{}";
        Lexer.lexer.lex(json.getBytes(), cb);
		assertEquals(countTok, 2);
		assertEquals(countTokString, 0);
		assertEquals(countNumberToken, 0);

        countTok = countTokString = countNumberToken = 0;

        json = "{{},{}}";
        Lexer.lexer.lex(json.getBytes(), cb);
		assertEquals(countTok, 7);
		assertEquals(countTokString, 0);
		assertEquals(countNumberToken, 0);

        countTok = countTokString = countNumberToken = 0;

        json = "[]";
        Lexer.lexer.lex(json.getBytes(), cb);
		assertEquals(countTok, 2);
		assertEquals(countTokString, 0);
		assertEquals(countNumberToken, 0);

        countTok = countTokString = countNumberToken = 0;

    }

    // handled higher up (in the callback converting collected number-chars
	// there are a bunch of pathological cases that COULD be caught in the Lexer but would require
	// more states, namely, afterSign, afterDecimal and afterE and afterESign
	// and it would make sense to benchmark
	//

//    @Test(expectedExceptions = JSONException.class)
//    public static void testFail() {
//        String json = "{\"a\":19560954.609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
//
//        Lexer.lexer.lex(json.getBytes(), cb);
//
//    }

	// For now, the lexer itself doesn't check validity of numbers, allowing any
	// valid number char in any order, e.g. 12345678eE0++45678E
	// see above
	@Test
	public static void testNonsensicalNumber() {
		String json = "[12345678eE0++5678E]";
		countTok = countTokString = countNumberToken = 0;
		Lexer.lexer.lex(json.getBytes(), cb);
		assertEquals(countTok, 2);
		assertEquals(countTokString, 0);
		assertEquals(countNumberToken, 1);
	}

	@Test
    public static void testNewline() {
        String json = "{\"a\":\"\\u2603\",\"b\":[1,2,3],\n\"dindong\":{\"b\":12}}";
		countTok = countTokString = countNumberToken = 0;
        Lexer.lexer.lex(json.getBytes(), cb);
		assertEquals(countTok, 14);
		assertEquals(countTokString, 5);
		assertEquals(countNumberToken, 4);
    }


    static void p(Object o) {
        System.out.println(o);
    }
}
