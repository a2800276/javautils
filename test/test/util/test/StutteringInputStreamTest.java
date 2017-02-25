package util.test;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;

/**
 * Created by a2800276 on 2017-02-25.
 */
public class StutteringInputStreamTest {
	RandomObject rnd = new RandomObject(  );

	@Test
	public void testRead() throws Exception {
		byte [] bs = rnd.randomBytes( 4096 );
		ByteArrayInputStream is = new StutteringInputStream(bs);
		int num = 0;
		int read, nread = 0;
		for (int i = 0; i!= 10; ++i) {
			while (-1 != (read = is.read( bs ))) {
				num += 1;
				nread += read;
			}
			is.reset();
		}
		assertEquals(4096 * 10, nread);
		assertEquals( 44, num ); // should always have same result with same seed.
	}


}