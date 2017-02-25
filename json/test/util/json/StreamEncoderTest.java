package util.json;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Created by a2800276 on 2017-02-24.
 */
public class StreamEncoderTest {
	@Test
	public void testEncode() throws Exception {
		String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
		Map m = (Map) JSON.parse( json );

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamEncoder enc = new StreamEncoder( os );
		enc.encode( m );

		String json2 = os.toString();
		assertEquals( m, JSON.parse( os.toString() ) );
	}

}