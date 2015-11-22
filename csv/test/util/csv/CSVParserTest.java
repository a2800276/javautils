package util.csv;


import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;


public class CSVParserTest {

	static final String BASIC_TEST = "one;two;three";
	static final String BASIC_TEST2 = "one;two;three\none;two;three\none;two;three";
	static final String COMMENT_TEST = "one;two;three\n#one;two;three\none;two;three";
	static final String COMMENT_TEST2 = "one;two;three\n//one;two;three\none;two;three";
	static final String COMMENT_TEST3 = "one;two;three\none;two;three\n//one;two;three";
	static final String DELIM_TEST = "one,two,three";
	static final String ESC_TEST = "one\\,two\\,three,two,three";
	static final String ESC2_TEST = "one\\,two\\,three,two,three\\";
	static final String PATTERN_TEST = "one;two;four\neins;zwei;vier";
	static final String PATTERN_TEST2 = "one;two;four\nthree;zwei;drei";
	static final String [] PATTERN = {".*", "two|zwei", ".*r$"};

	@Test
	public void testBasics() {
		CSVParser parser = new CSVParser(BASIC_TEST);
		List<String> res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one", res.get(0));
		assertEquals("three", res.get(2));

		parser = new CSVParser(BASIC_TEST, 3);
		res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one", res.get(0));
		assertEquals("three", res.get(2));
		assertFalse(parser.hasErrors());

		parser = new CSVParser(BASIC_TEST, 4);
		res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one", res.get(0));
		assertEquals("three", res.get(2));
		assertTrue(parser.hasErrors());

		parser = new CSVParser(BASIC_TEST, 2);
		res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one", res.get(0));
		assertEquals("three", res.get(2));
		assertTrue(parser.hasErrors());
		assertEquals(parser.getErrorMessage(), "line   1 : incorrect number of fields, expected 2, was 3\n\t" + BASIC_TEST + "\n");

		parser = new CSVParser(BASIC_TEST2);
		parser.setComment("//");
		int count =0;
		while ((res = parser.getNextLine()) != null) {
			++count;
			assertEquals(3, res.size());
			assertEquals("one", res.get(0));
			assertEquals("three", res.get(2));
		}
		assertEquals(3, count);
		assertFalse(parser.hasErrors());


	}

	@Test public void testPattern () {
		CSVParser parser = new CSVParser(PATTERN_TEST);
		parser.setChecks(PATTERN);
		List<String>res;
		int count =0;
		while ((res = parser.getNextLine()) != null) {
			++count;
			assertEquals(3, res.size());
		}
		assertEquals(2, count);
		assertFalse(parser.hasErrors());

		parser = new CSVParser(PATTERN_TEST2);
		parser.setChecks(PATTERN);
		count =0;
		while ((res = parser.getNextLine()) != null) {
			++count;
			assertEquals(3, res.size());
		}
		assertEquals(2, count);
		assertTrue(parser.hasErrors());
		assertEquals(parser.getErrorMessage(), "line   2 : incorrect format for field 3, value \"drei\" expected to match /.*r$/\n\tthree;zwei;drei\n");

	}

	@Test public void testComment () {
		CSVParser parser = new CSVParser(COMMENT_TEST);
		List<String>res;
		int count =0;
		while ((res = parser.getNextLine()) != null) {
			++count;
			assertEquals(3, res.size());
			assertEquals("one", res.get(0));
			assertEquals("three", res.get(2));
		}
		assertEquals(2, count);
		assertFalse(parser.hasErrors());

		parser = new CSVParser(COMMENT_TEST2);
		parser.setComment("//");
		count =0;
		while ((res = parser.getNextLine()) != null) {
			++count;
			assertEquals(3, res.size());
			assertEquals("one", res.get(0));
			assertEquals("three", res.get(2));
		}
		assertEquals(2, count);
		assertFalse(parser.hasErrors());

		parser = new CSVParser(COMMENT_TEST3);
		parser.setComment("//");
		count =0;
		while ((res = parser.getNextLine()) != null) {
			++count;
			assertEquals(3, res.size());
			assertEquals("one", res.get(0));
			assertEquals("three", res.get(2));
		}
		assertEquals(2, count);
		assertFalse(parser.hasErrors());
	}

	@Test public void testDelimiter () {
		CSVParser parser = new CSVParser(DELIM_TEST);
		parser.setDelimeter(',');
		List<String> res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one", res.get(0));
		assertEquals("three", res.get(2));
	}
	@Test public void testEscape () {
		CSVParser parser = new CSVParser(ESC_TEST);
		parser.setDelimeter(',');
		List<String> res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one,two,three", res.get(0));
		assertEquals("three", res.get(2));

		parser = new CSVParser(ESC2_TEST);
		parser.setDelimeter(',');
		res = parser.getNextLine();
		assertEquals(3, res.size());
		assertEquals("one,two,three", res.get(0));
		assertEquals("three", res.get(2));
		assertTrue(parser.hasErrors());

		assertEquals(parser.getErrorMessage(), "line   1 : csv line ends in delimeter!\n\t" + ESC2_TEST + "\n");

	}


}
