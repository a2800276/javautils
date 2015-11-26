package util.bytes;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static util.bytes.Bytes.random;

public class BytesTest {


	static final byte[] EMPTY = {};
	static final byte[] FEDCBA9876543210 = {(byte) 0xfe, (byte) 0xdc, (byte) 0xba, (byte) 0x98, (byte) 0x76, (byte) 0x54, (byte) 0x32, (byte) 0x10};

	static boolean checkStartsWith(byte[] prefix, byte[] specimen) {
		if (prefix == null || specimen == null) {
			throw new NullPointerException();
		}
		if (specimen.length < prefix.length) {
			return false;
		}
		for (int i = 0; i != prefix.length; ++i) {
			assertEquals(prefix[i], specimen[i]);
		}
		return true;
	}

	static boolean checkThrowsNPE(CB doer) {
		return checkThrows(NullPointerException.class, doer);
	}

	static boolean checkThrows(Class exceptionClass, CB doer) {
		try {
			doer.go();
			return false;
		} catch (Throwable t) {
			assertEquals(exceptionClass, t.getClass());
			return true;
		}
	}

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

	@Test
	public void testAppend() throws Exception {
		assertEquals(Bytes.append(EMPTY), EMPTY);
		assertEquals(Bytes.append(EMPTY, EMPTY, EMPTY), EMPTY);
		assertEquals(Bytes.append(FEDCBA9876543210, EMPTY), FEDCBA9876543210);
		assertEquals(Bytes.append(EMPTY, FEDCBA9876543210), FEDCBA9876543210);
		assertEquals(Bytes.append(EMPTY, FEDCBA9876543210, EMPTY), FEDCBA9876543210);

		byte[] random1 = random(32);
		byte[] random2 = random(32);
		assertTrue(checkStartsWith(random1, Bytes.append(random1, random2)));
		assertTrue(checkStartsWith(random2, Bytes.append(random2, random1)));
		assertTrue(checkStartsWith(random2, Bytes.append(EMPTY, random2, random1)));
		assertTrue(checkStartsWith(random2, Bytes.append(random2, EMPTY, random1)));
		assertTrue(checkStartsWith(random2, Bytes.append(random2, random1, EMPTY)));


		assertTrue(checkThrowsNPE(new CB() {
			@Override
			public void go() throws Throwable {
				Bytes.append(null);

			}
		}));
		assertTrue(checkThrowsNPE(new CB() {
			@Override
			public void go() throws Throwable {
				Bytes.append(null, random(10), random(10));

			}
		}));
		assertTrue(checkThrowsNPE(new CB() {
			@Override
			public void go() throws Throwable {
				Bytes.append(random(10), null, random(10));

			}
		}));
		assertTrue(checkThrowsNPE(new CB() {
			@Override
			public void go() throws Throwable {
				Bytes.append(random(10), random(10), null);
			}
		}));


	}

	private static abstract class CB {
		public abstract void go() throws Throwable;
	}
}