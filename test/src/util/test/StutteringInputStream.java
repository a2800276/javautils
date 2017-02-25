package util.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by a2800276 on 2017-02-25.
 */
public class StutteringInputStream extends ByteArrayInputStream {
	final Random rnd;
	public int countCalled;

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
		return super.read( b, off, rnd.nextInt( len ) );
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
