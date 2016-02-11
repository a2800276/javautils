package util.bytes.hexy;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class HexyTest {
	final static byte[] HEX = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
	final static byte[] ONE_OVER = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x00};
	final static byte[] ONE = {(byte) 0xFF};

	static void p(Object o) {
		System.out.println(o);
	}

	@Test
	public void testStaticToString() throws Exception {
		assertEquals("", Hexy.toString(Hexy.EMPTY));
		assertEquals("", Hexy.toString((byte[]) null));
		assertEquals("", Hexy.toString((String) null));
		assertEquals("00 01 02 03 04 05 06 07  08 09 0a 0b 0c 0d 0e 0f  ................\n", Hexy.toString(HEX));
	}

	@Test
	public void testToString() throws Exception {
		Hexy tester = new Hexy();

		assertEquals(Hexy.toString(Hexy.EMPTY), tester.hexDump(Hexy.EMPTY));
		assertEquals(Hexy.toString((byte[]) null), tester.hexDump(null));
		assertEquals(Hexy.toString(HEX), tester.hexDump(HEX));
	}

	@Test
	public void testWidth() throws Exception {
		Hexy tester = new Hexy();
		assertEquals("ff                                                .\n", tester.hexDump(ONE));
		assertEquals("00 01 02 03 04 05 06 07  08 09 0a 0b 0c 0d 0e 0f  ................\n00                                                .\n", Hexy.toString(ONE_OVER));

		Hexy.Config cfg = new Hexy.Config();
		cfg.width = 8;
		tester = new Hexy(cfg);
		assertEquals("00 01 02 03  04 05 06 07  ........\n08 09 0a 0b  0c 0d 0e 0f  ........\n", tester.hexDump(HEX));

		cfg.width = 14;
		tester = new Hexy(cfg);
		assertEquals("00 01 02 03 04 05 06  07 08 09 0a 0b 0c 0d  ..............\n0e 0f                                       ..\n", tester.hexDump(HEX));

	}

	@Test
	public void testAnnotate() throws Exception {
		Hexy.Config cfg = new Hexy.Config();
		cfg.annotate = Hexy.Annotate.NONE;
		Hexy tester = new Hexy(cfg);
		assertEquals("ff                                               \n", tester.hexDump(ONE));

		cfg.annotate = Hexy.Annotate.NONE;
		cfg.width = 8;
		assertEquals("00 01 02 03  04 05 06 07 \n08 09 0a 0b  0c 0d 0e 0f \n", tester.hexDump(HEX));

	}

	@Test
	public void testFormat() throws Exception {
		Hexy.Config cfg = new Hexy.Config();
		cfg.format = Hexy.Format.FOURS;
		Hexy tester = new Hexy(cfg);
		assertEquals("0001 0203 0405 0607 0809 0a0b 0c0d 0e0f  ................\n", tester.hexDump(HEX));
		cfg.format = Hexy.Format.NONE;
		tester = new Hexy(cfg);
		assertEquals("000102030405060708090a0b0c0d0e0f ................\n", tester.hexDump(HEX));
	}

	@Test
	public void testNumbering() throws Exception {
		Hexy.Config cfg = new Hexy.Config();
		cfg.numbering = Hexy.Numbering.HEX_BYTES;
		cfg.format = Hexy.Format.FOURS;
		Hexy tester = new Hexy(cfg);
		assertEquals("0000000: 0001 0203 0405 0607 0809 0a0b 0c0d 0e0f  ................\n", tester.hexDump(HEX));

		cfg.format = Hexy.Format.NONE;
		assertEquals("0000000: 000102030405060708090a0b0c0d0e0f ................\n0000010: 00                               .\n", tester.hexDump(ONE_OVER));
	}

	@Test
	public void testUpper() throws Exception {
		Hexy.Config cfg = new Hexy.Config();
		cfg.upper = true;
		Hexy hexy = new Hexy(cfg);
		assertEquals("00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F  ................\n", hexy.hexDump(HEX));

	}

	@Test
	public void testPrefix() {
		Hexy.Config cfg = new Hexy.Config();
		cfg.upper = true;
		cfg.prefix = "Tim2000! ->>>";
		Hexy hexy = new Hexy(cfg);
		assertEquals("Tim2000! ->>>00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F  ................\n", hexy.hexDump(HEX));
	}

	@Test
	public void testIndent() {
		Hexy.Config cfg = new Hexy.Config();
		cfg.upper = true;
		cfg.indent = 4;
		Hexy hexy = new Hexy(cfg);
		assertEquals("    00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F  ................\n", hexy.hexDump(HEX));
		cfg.prefix = "Tim2000! ->>>";
		hexy = new Hexy(cfg);
		cfg.indent = 3;
		assertEquals("   Tim2000! ->>>00 01 02 03 04 05 06 07  08 09 0A 0B 0C 0D 0E 0F  ................\n", hexy.hexDump(HEX));
	}
}