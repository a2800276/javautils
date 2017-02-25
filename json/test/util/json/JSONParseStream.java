package util.json;

import org.testng.annotations.Test;
import util.test.RandomObject;
import util.test.StutteringInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.testng.Assert.*;

public class JSONParseStream {
	static RandomObject rnd = new RandomObject();

	@Test(expectedExceptions = JSONException.class)
	public static void testInvalid() {
		String json = "{{\"a\":19560954609845.4456456}:1,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
		ByteArrayInputStream is = new StutteringInputStream( json.getBytes() );
		JSON.parseStream( is );
	}

	@Test(expectedExceptions = JSONException.class)
	public static void testNoComma() {
		// too liberal behaviour, key value don't need to be separated by comma
		String json = "{\"a\" 19560954609845.4456456 \"b\" [1 2 3] \"dindong\" {\"b\" 12}}";
		ByteArrayInputStream is = new StutteringInputStream( json.getBytes() );
		JSON.parseStream( is );
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public static void testInvalidFloat() {
		String json = "{\"a\":19560954.609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
		ByteArrayInputStream is = new StutteringInputStream( json.getBytes() );
		JSON.parseStream( is );
	}

	@Test(expectedExceptions = JSONException.class)
	public static void testIncomplete() {
		String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}";
		ByteArrayInputStream is = new StutteringInputStream( json.getBytes() );
		JSON.parseStream( is );
	}

	@Test
	public static void testState() throws IOException {
		Object testObject = rnd.randomMap( 10, 3 );

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamEncoder enc = new StreamEncoder( bos );
		enc.encode( testObject );
		byte[] bs = bos.toByteArray();

		Object res0 = JSON.parseStream( new ByteArrayInputStream( bs ) );
		StutteringInputStream sis = new StutteringInputStream( bs );
		Object res1 = JSON.parseStream( sis );

		assertTrue( sis.countCalled != 1 );
		assertEquals( testObject, res0 );
		assertEquals( testObject, res1 );
	}

	@Test
	public static void testRndFail() {
		StutteringInputStream is = new StutteringInputStream( "{\"key\":\"value\":\"value\"}".getBytes() );
		try {
			JSON.parseStream( is );
			fail( "testRndFail missed incorrect COLON" );
		} catch (JSONException e) {
			assertTrue( true );
		}
		is = new StutteringInputStream( "[,,,]".getBytes() );
		try {
			JSON.parseStream( is );
			fail( "testRndFail missed incorrect COMMA" );
		} catch (JSONException e) {
			assertTrue( true );
		}

	}

	@Test(expectedExceptions = JSONException.class)
	public static void testReuse() {
		byte[] bs = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}".getBytes();
		StutteringInputStream is = new StutteringInputStream( bs );
		JSON json = new JSON();
		json.parse( is );
		is.reset();
		json.parse( is );
	}

	@Test
	public static void testReuseReset() {
		byte[] bs = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}".getBytes();
		StutteringInputStream is = new StutteringInputStream( bs );
		JSON json = new JSON();
		json.parse( bs );
		json.reset();
		is.reset();
		json.parse( bs );

	}
//    public static void main(String[] args) {
//        testInvalid();
//        testNoComma();
//        testState();
//        testRndFail();
//    }

	static void p(Object o) {
		System.out.println( o );
	}
}


