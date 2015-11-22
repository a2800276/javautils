package util.bytes.bytes;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by a2800276 on 2015-11-22.
 */
public class BytesTest {


	static final byte[] EMPTY = {};
	static final byte[] FEDCBA9876543210 = {(byte) 0xfe, (byte) 0xdc, (byte) 0xba, (byte) 0x98, (byte) 0x76, (byte) 0x54, (byte) 0x32, (byte) 0x10};

	@Test
	public void testB2s() throws Exception {

	}

	@Test
	public void testH2b() throws Exception {
		assertEquals(Bytes.h2b(null), EMPTY);
		assertEquals(Bytes.h2b(""), EMPTY);
		assertEquals(Bytes.h2b("FEDCBA9876543210"), FEDCBA9876543210);
		assertEquals(Bytes.h2b("fedcba9876543210"), FEDCBA9876543210);
		assertEquals(Bytes.h2b("fe dc ba 98 76 54 32 10"), FEDCBA9876543210);
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public void testH2bExceptionG() throws Exception {
		Bytes.h2b("ag");
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public void testH2bExceptionUneven() throws Exception {
		Bytes.h2b("abc");
	}

	@Test
	public void testB2h() throws Exception {
		byte[] bytes = {0x01, 0x02, 0x03};
		assertEquals(Bytes.b2h(bytes), "010203");
		byte[] bytes2 = {(byte) 0xab, (byte) 0xcd, (byte) 0xef};
		assertEquals(Bytes.b2h(bytes2), "abcdef");
		byte[] bytes3 = {0x01, 0x02, 0x03, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
		assertEquals(Bytes.b2h(bytes3, 2, 2), "03ab");
	}
}