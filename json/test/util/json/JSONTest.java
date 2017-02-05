package util.json;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class JSONTest {
    @Test(expectedExceptions = JSONException.class)
    public static void testInvalid() {
        String json = "{{\"a\":19560954609845.4456456}:1,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
        Object o = JSON.parse(json);
    }

    @Test(expectedExceptions = JSONException.class)
    public static void testNoComma() {
        // too liberal behaviour, key value don't need to be separated by comma
        String json = "{\"a\" 19560954609845.4456456 \"b\" [1 2 3] \"dindong\" {\"b\" 12}}";
        JSON.parse(json);
    }
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testInvalidFloat() {
		String json = "{\"a\":19560954.609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
		JSON.parse(json);
	}
    @Test
    public static void testState() {
        String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";

        JSON j = new JSON();
        byte[] a = json.getBytes();
        byte[] b = new byte[1];
        for (int i = 0; /*i!=a.length*/ ; ++i) {
            System.arraycopy(a, i, b, 0, 1); /* heh? whydidothat? */
            j.parse(b);
            if (j.done()) break; /* to test this I guess */
                           /* should provide parse(byte[], off, len) */
        }

        Object o1 = JSON.parse(json);
        assertEquals(o1, j.obj());
    }
	@Test
    public static void testRndFail() {
        String json = "{\"key\":\"value\":\"value\"}";
        try {
            JSON.parse(json);
            fail("testRndFail missed incorrect COLON");
        } catch (JSONException e) {
			assertTrue( true );
        }
        json = "[,,,]";
        try {
            JSON.parse(json);
            fail("testRndFail missed incorrect COMMA");
        } catch (JSONException e) {
        	assertTrue( true );
        }

    }

    @Test(expectedExceptions = JSONException.class)
	public static void testReuse() {
		byte[] bs = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}".getBytes();
		JSON json = new JSON(  );
		json.parse( bs );
		json.parse( bs );

	}
	@Test
	public static void testReuseReset() {
		byte[] bs = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}".getBytes();
		JSON json = new JSON(  );
		json.parse( bs );
		json.reset();
		json.parse( bs );

	}
//    public static void main(String[] args) {
//        testInvalid();
//        testNoComma();
//        testState();
//        testRndFail();
//    }

    static void p(Object o) {
        System.out.println(o);
    }
}


