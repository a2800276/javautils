package util.io;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatWidthException;

import static org.testng.Assert.*;

/**
 * Created by a2800276 on 2015-11-22.
 */
public class IOTest {
	String streamContent = "This is a \n three line \r\n long stream\n";
	InputStream exceptionalIs;
	private InputStream is;

	@BeforeMethod
	public void setUp() throws Exception {
		is = new InputStream() {
			byte[] bs = streamContent.getBytes();
			int i = 0;

			@Override
			public int read() throws IOException {
				return i < bs.length ? bs[i++] : -1;
			}
		};

		exceptionalIs = new InputStream() {
			@Override
			public int read() throws IOException {
				throw new IOException();
			}
		};

	}

	@Test
	public void testReadLines() throws Exception {

		class Wrapper {
			int i;
			String line;
			boolean done;
			boolean informedOfException;
		}
		final Wrapper w = new Wrapper();

		IO.LineCallback cb = new IO.LineCallback() {
			@Override
			public void line(String line, int lineNumber) {
				super.line(line, lineNumber);
				w.i = lineNumber;
				w.line = line;
			}

			@Override
			public void done() {
				super.done();
				w.done = true;
			}

			@Override
			public void exceptionEncountered(IOException ioe) {
				super.exceptionEncountered(ioe);
				w.informedOfException = true;
			}
		};

		IO.readLines(is, cb);
		assertTrue(w.done);
		assertEquals(w.i, 3);
		assertEquals(w.line, " long stream");

		boolean caught = false;
		try {
			IO.readLines(exceptionalIs, cb);
		} catch (IO.WrappedException e) {
			caught = true;
		}
		assertTrue(caught);
		assertTrue(w.informedOfException);


	}

	@Test
	public void testReadAll() throws Exception {
		byte[] bs = IO.readAll(is);
		assertEquals(bs, streamContent.getBytes());
		setUp();
		bs = IO.readAll(is, 1024 * 1024);
		assertEquals(bs, streamContent.getBytes());
		setUp();
		bs = IO.readAll(is, 1);
		assertEquals(bs, streamContent.getBytes());
		boolean caught = false;
		try {
			IO.readAll(is, 0);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		caught = false;

		try {
			IO.readAll(exceptionalIs);
		} catch (IO.WrappedException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testReadAllString() throws Exception {
		assertEquals(IO.readAllString(is), streamContent);
		boolean caught = false;
		try {
			IO.readAllString(exceptionalIs);
		} catch (IO.WrappedException e) {
			caught = true;
		}
		assertTrue(caught);
	}


	@Test
	public void testClose() throws Exception {
		Closeable c = new Closeable() {
			int count;

			@Override
			public void close() throws IOException {
				if (count == 1) {
					count++;
					throw new IOException();
				}
				if (count == 2) {
					// no special reason, just an exotic RTE
					throw new IllegalFormatWidthException(0);
				}
				count++;
			}
		};
		// make sure nothing happens
		assertFalse(IO.close(null));
		assertTrue(IO.close(c));
		assertFalse(IO.close(c));
		boolean caught = false;
		try {
			IO.close(c);
		} catch (IllegalFormatWidthException t) {
			caught = true;
		}
		assertTrue(caught);

	}

	@Test
	public void testFilenameRelativeTo() throws Exception {
		assertEquals("../test", IO.filenameRelativeTo("test", "../bbbbb"));
		assertEquals("/test", IO.filenameRelativeTo("/test", "../bbbbb"));

		assertEquals("/bla/bla/test", IO.filenameRelativeTo("test", "/bla/bla/bbbbb"));
		assertEquals("/bla/bla/../test", IO.filenameRelativeTo("../test", "/bla/bla/bbbbb"));
	}

	@Test
	public void testGetPath() throws Exception {
		assertEquals(IO.getPath("/etc/passwd"), "/etc");
		assertEquals(IO.getPath(null), null);
		assertEquals(IO.getPath(""), "");
		assertEquals(IO.getPath("bla"), "bla");
	}
}