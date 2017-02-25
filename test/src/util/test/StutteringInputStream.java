package util.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Utility class to help emulate "real world" conditions during testing.
 * ByteArrayOutputStream will typically return all requested bytes, or all
 * the bytes remaining in the underlying array. This implementation will
 * return between 0 and the number of requested bytes.
 *
 * If the size of the requested bytes is very large, i.e. the buffer passed
 * in is byte[4096] but the underlying array is only 100 bytes long, chances
 * are all bytes will be returned anyway. To avoid this behaviour, you
 * can set `max` to specify the maximum number of bytes to return for
 * each call.
 *
 * Created by a2800276 on 2017-02-25.
 */
public class StutteringInputStream extends ByteArrayInputStream {
	final Random rnd;
	public int countCalled;
	public int max = 0;

	public StutteringInputStream(byte[] buf, long seed) {
		this ( buf, 0, buf.length, seed );
	}

	public StutteringInputStream(byte[] buf, int offset, int length, long seed) {
		super( buf, offset, length );
		this.rnd = new Random( seed );

	}

	public StutteringInputStream(byte[] buf) {
		this( buf, 0 );
	}

	public StutteringInputStream(byte[] buf, int offset, int length) {
		this( buf, offset, length , 0);
	}


	@Override
	public int read(byte[] b, int off, int len) {
		countCalled++;
		int actual_len = rnd.nextInt( len );
		if (max > 0) {
			actual_len = max < actual_len ? max : actual_len;
		}
		return super.read( b, off, actual_len );
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}

	@Override
	public synchronized void reset() {
		super.reset();
		countCalled = 0;
	}
}
