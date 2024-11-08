package util.json;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class EncoderTest {
    static void p(Object o) {
        System.out.println(o);
    }

    @Test
    public void testBaseTypes() {

        StringBuilder b = new StringBuilder();
        Encoder.encode(b, true);
        Encoder.encode(b, 1);
        Encoder.encode(b, (long) 1);
        Encoder.encode(b, (float) 1.0);
        Encoder.encode(b, 1.0);
        Encoder.encode(b, (byte) 1);
        Encoder.encode(b, (short) 1);
        Encoder.encode(b, '1');
        assertEquals(b.toString(), "true111.01.01149");
    }

    @Test
    public void testMap() {
        String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
        Map m = (Map) JSON.parse(json);
        assertTrue(m.containsKey("a"));
        assertTrue(m.containsKey("b"));
        assertTrue(m.containsKey("dindong"));

        assertEquals( m.keySet().size(), 3 );

        p("should look the same (equality is checked in code.)");
        p(json);
        p(JSON.jsonify(m));

        assertEquals(m, JSON.parse(JSON.jsonify(m)));
    }

    @Test
    public void testArray() {
        int[] is = {1, 2, 3};
        String r = JSON.jsonify(is);
        assertEquals(r, "[1,2,3]");
    }

    @Test
    public void testCircular() {
        Map map = new java.util.HashMap();
        map.put("bla", map);
        boolean caught = false;
        try {
            p(JSON.jsonify(map));
        } catch (JSONException re) {
            assertEquals(re.getMessage(), "circular");
            caught = true;
        }
        assertTrue(caught);

        Map map2 = new java.util.HashMap();
        map = new java.util.HashMap();
        map.put("bla", "bla");

        map2.put("bla", map);
        map2.put("blub", map);
        caught = false;
        try {
            p(JSON.jsonify(map2));
        } catch (JSONException re) {
            assertEquals(re.getMessage(), "circular");
            caught = true;
        }
        assertFalse(caught);
    }

    @Test
    public void testNonBaseType() {
        try {
            JSON.jsonify(System.out);
            assertTrue(false);
        } catch (JSONException re) {
            assertEquals(re.getMessage(), "unexpected object: class java.io.PrintStream");
        }
    }

    @Test
    public void testBoolean() {
        Map map = new HashMap();
        map.put("bla", true);
        String json = JSON.jsonify(map);
        assertEquals(json, "{\"bla\":true}");
    }
}
