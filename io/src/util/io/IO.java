package util.io;

import java.io.*;

/**
 * Warning: utilies collected in this class are potentially dangerous.
 *
 * Most of the utilities here are meant to read entire files, even though
 * they operate on generic streams and readers.
 *
 * They are meant soley for local resources (file, getResourceAsStream, ...)
 * in cases you can be fairly certain that:
 * <ul>
 * <li> the resource exists
 * <li> is readable
 * <li> is responsive, i.e. won't block (for too long)
 * <li> performance does not matter
 * <li> you are sitting in front of the computer while the programme is executing.
 * <li> the streams being accessed not connected to a network
 * <li> the resource underlying any stream or filename is not huge.
 * </ul>
 *
 * Basically, they are meant for one-off throw away programs, loading fixtures
 * in unit tests and some such.
 *
 * (with the possible exception of IO#close but I've not made up my mind about
 * that one yet)
 */
public class IO {

	/**
	 * Read a stream line by line, issuing a callback for each encountered
	 * line (counting starts at 1), optionally with the line number. The String presented to the
	 * callback is stripped of the trailing line seperator.
	 *
	 * @param is
	 * @param cb
	 * @see LineCallback
	 */
	public static void readLines(InputStream is, LineCallback cb) {
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		LineNumberReader reader = new LineNumberReader(buf);
		String line;
		try {
			while (null != (line = reader.readLine())) {
				cb.line(line, reader.getLineNumber());
			}
		} catch (IOException e) {
			cb.exceptionEncountered(e);
			throw new WrappedException(e);
		} finally {
			close(is);
			cb.done();
		}
	}

	/**
	 * Read an entire file when reasonable sure that it exists and is
	 * readable. All exceptions encountered are stuffed into a RuntimeException
	 * of class IO.WrappedException in case something goes wrong.
	 *
	 * @param filename
	 * @return
	 */
	public static byte[] readAll(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			return readAll(fis);
		} catch (FileNotFoundException e) {
			throw new WrappedException(e);
		}
	}

	/**
	 * Read an entire file when reasonable sure that it exists and is
	 * readable. All exceptions encountered are stuffed into a RuntimeException
	 * of class IO.WrappedException in case something goes wrong.
	 *
	 * @param filename
	 * @return
	 */
	public static String readAllString(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			// readAll closes
			return readAllString(fis);
		} catch (FileNotFoundException fnf) {
			throw new WrappedException(fnf);
		}
	}

	public static String readAllString(InputStream is) {
		Reader reader = null;
		try {
			reader = new InputStreamReader(is);
			char[] c = new char[1024];
			StringBuilder builder = new StringBuilder();
			int i;
			while (-1 != (i = reader.read(c))) {
				builder.append(c, 0, i);
			}
			return builder.toString();
		} catch (IOException ioe) {
			throw new WrappedException(ioe);
		} finally {
			close(reader);
		}
	}

	/**
	 * Read the stream to the end and closes it.
	 *
	 * @param is
	 * @return the data read from the stream.
	 */
	public static byte[] readAll(InputStream is) {
		return readAll(is, 1024);
	}

	/**
	 * same, but with the possibility to provide a chunkSize
	 * parameter to indicate how much to read at a time. This should
	 * probably be removed, as it suggests that these utilities are
	 * acceptable to use when you need full control over the i/o.
	 *
	 * @param is
	 * @param chunkSize
	 * @return
	 */
	public static byte[] readAll(InputStream is, int chunkSize) {
		if (chunkSize <= 0) {
			throw new IllegalArgumentException("chunk size can't be <1");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bytes = new byte[chunkSize];
		int read;
		try {
			while (-1 != (read = is.read(bytes))) {
				bos.write(bytes, 0, read);
			}
		} catch (IOException e) {
			throw new WrappedException(e);
		} finally {
			close(is);
		}
		return bos.toByteArray();
	}

	/**
	 * Utility to avoid all the stupid hassle of catching IOExceptions
	 * when calling stream.close, checks for null and throws away any
	 * IOException it encounters. In case you're Closeable also throws
	 * some funky unpredictable, undeclared RuntimeException, that will
	 * be passed through.
	 *
	 * Replace this:
	 * <pre>
	 *     try {
	 *         stream.doWhatever()
	 *     } catch(...) {
	 *         ....
	 *     } finally {
	 *         if (stream != null) {
	 *             try {
	 *                 stream.close()
	 *             } catch (IOException ioe) {
	 *                 // yeah, I don't know what to do here either...
	 *             }
	 *         }
	 *     }
	 * </pre>
	 *
	 * With this:
	 *
	 * <pre>
	 *     try {
	 *         stream.doWhatever()
	 *     } catch  (IOException ioe) {
	 *         ... this bit is still up to you ...
	 *     } finally {
	 *         IO.close(stream)
	 *     }
	 *
	 * </pre>
	 *
	 *
	 *
	 *
	 * @param stream
	 * @returns true in case the object was closed without issue, otherwise
	 * false. Only for the morbidly curious, I strongly recommend ignoring
	 * the value
	 */
	public static boolean close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
				return true;
			} catch (IOException e) { /* e.printStackTrace(); */ }
		}
		return false;
	}

	/**
	 * Try to retrieve a filename relative to another filename. Useful for includes, e.g.
	 * say you are scanning through "../somefile" which contains the following:
	 * "./someinclude", from the point of view of the scanning code, the file that is
	 * meant is "../someinclude". This methods rectifies this, unless `filename` is an
	 * absolute filename, in which case it is returned ...
	 */
	public static String filenameRelativeTo(String filename, String relativeTo) {
		File f = new File(filename);
		if (f.isAbsolute()) {
			return filename;
		}
		return getPath(relativeTo) + File.separator + filename;
	}

	/**
	 * Java's getPath does whatever (?), this tries to return the system
	 * dependant path component of the filename
	 * <p/>
	 * This interface sucks. Not sure what this is supposed to
	 * return. Also it's dependant on the plattform you're running
	 * on (File.seperator) so it's basically nondeterministic.
	 * <p/>
	 * Also, I'm no longer sure what grips I had with Java's getPath...
	 * <p/>
	 * Returns null if passed null.
	 */
	public static String getPath(String fn) {
		if (fn == null) {
			return null;
		}
		int i = fn.lastIndexOf(File.separator);
		return (i > -1) ? fn.substring(0, i) : fn;
	}

	public static class WrappedException extends RuntimeException {
		public WrappedException(IOException e) {
			super(e);
		}
	}

	public static abstract class LineCallback {
		/**
		 * Override this in case you are interested in information
		 * about the current line number. Else this is a noop and
		 * just passes the line on.
		 *
		 * @param line
		 * @param lineNumber
		 */
		public void line(String line, int lineNumber) {
			line(line);
		}

		/**
		 * Called once for every line in the file with the
		 * stripped value of the line, i.e. no line seperator.
		 */
		public void line(String line) {
		}

		/**
		 * Override this function in case you want to be informed that the
		 * end of the file has been encountered.
		 */
		public void done() {
		}

		/**
		 * Override this function in case the callback needs to be informed
		 * that an exception was encountered.
		 * @param ioe
		 */
		public void exceptionEncountered(IOException ioe) {}
	}

}
