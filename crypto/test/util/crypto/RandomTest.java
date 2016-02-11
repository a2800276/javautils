package util.crypto;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RandomTest {

	@Test
	public void testRandom() throws Exception {
		byte[] bytes = Random.random(0);
		assertEquals(bytes.length, 0);
		bytes = Random.random(-1);
		assertEquals(bytes.length, 0);
		bytes = Random.random(1024);
		assertEquals(bytes.length, 1024);
	}
}