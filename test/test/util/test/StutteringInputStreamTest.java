package util.test;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by a2800276 on 2017-02-25.
 */
public class StutteringInputStreamTest {
	RandomObject rnd = new RandomObject(  );

	@Test
	public void testRead() throws Exception {
		byte [] bs = rnd.randomBytes( 4096 );
		StutteringInputStream is = new StutteringInputStream(bs);
		int num = 0;
		int read, nread = 0;
		for (int i = 0; i!= 10; ++i) {
			while (-1 != (read = is.read( bs ))) {
				num += 1;
				nread += read;
			}
			is.reset();
		}
		assertEquals(nread,4096 * 10);
		assertEquals(num, 31 ); // should always have same result with same seed.

		is.reset();
		is.max = 100;
		num = 0;
		read = 0;
		nread = 0;
		for (int i = 0; i!= 10; ++i) {
			while (-1 != (read = is.read( bs ))) {
				num += 1;
				nread += read;
			}
			is.reset();
		}
		assertEquals(nread, 4096 * 10);
		assertEquals( num, 426 ); // should always have same result with same seed.
	}


}