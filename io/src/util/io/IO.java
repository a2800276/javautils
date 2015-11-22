package util.io;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by a2800276 on 2015-11-22.
 */
public class IO {
	public static byte[] readAllClose(InputStream is) {
		return readAllClose(is, 1024 * 1024);

	}

	public static byte[] readAllClose(InputStream is, int chunkSize) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bytes = new byte[chunkSize];
		int read;
		try {
			while (-1 != (read = is.read(bytes))) {
				bos.write(bytes, 0, read);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(is);
		}
		return bos.toByteArray();
	}

	public static void close(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) { /* e.printStackTrace(); */ }
		}
	}
}
