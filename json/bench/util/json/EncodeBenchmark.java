package util.json;

import util.benchmark.Benchmark;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Explore the virtues of writing into a StringBuffer or a Stream.
 */
public class EncodeBenchmark {

	static String str ;
	static List<Object> objectList;
//	public static void asIs (Benchmark b) {
//		for (int n = 0; n != b.N; ++n) {
//			for (Object o : objectList) {
//				str = JSON.jsonify( o );
//				//if (str == null) { throw new RuntimeException(  );}
//			}
//		}
//	}
//
	public static void withIOStream (Benchmark b) {
		OutputStream os = new OutputStream() {
			@Override
			public void write(byte[] b) throws IOException {
				return;
			}

			@Override
			public void write(int b) throws IOException {
				return;
			}
		};
		try {
			StreamEncoder enc = new StreamEncoder( os );
			for (int n = 0; n != b.N; ++n) {
				for (Object o : objectList) {
					enc.encode( o );
				}
			}
		} catch (IOException ioe) {
			// ignore?
		}
	}

//	public static void withBufferedStream (Benchmark b) {
//		OutputStream os = new OutputStream() {
//			@Override
//			public void write(byte[] b) throws IOException {
//				return;
//			}
//
//			@Override
//			public void write(int b) throws IOException {
//				return;
//			}
//		};
//		try {
//			StreamEncoder enc = new StreamEncoder( new BufferedOutputStream( os ) );
//			for (int n = 0; n != b.N; ++n) {
//				for (Object o : objectList) {
//					enc.encode( o );
//				}
//			}
//		} catch (IOException ioe) {
//			// ignore?
//		}
//	}
//
//	public static void withWriter (Benchmark b) {
//		OutputStream os = new OutputStream() {
//			@Override
//			public void write(byte[] b) throws IOException {
//				return;
//			}
//
//			@Override
//			public void write(int b) throws IOException {
//				return;
//			}
//		};
//		try {
//			WriterEncoder enc = new WriterEncoder( os );
//			for (int n = 0; n != b.N; ++n) {
//				for (Object o : objectList) {
//					enc.encode( o );
//				}
//			}
//		} catch (IOException ioe) {
//			// ignore?
//		}
//	}
//	public static void withWriter2 (Benchmark b) {
//		OutputStream os = new OutputStream() {
//			@Override
//			public void write(byte[] b) throws IOException {
//				return;
//			}
//
//			@Override
//			public void write(int b) throws IOException {
//				return;
//			}
//		};
//		Writer w = new OutputStreamWriter( os );
//		try {
//			WriterEncoder enc = new WriterEncoder( w );
//			for (int n = 0; n != b.N; ++n) {
//				for (Object o : objectList) {
//					enc.encode( o );
//				}
//			}
//		} catch (IOException ioe) {
//			// ignore?
//		}
//	}
//	public static void withBufferedWriter (Benchmark b) {
//		OutputStream os = new OutputStream() {
//			@Override
//			public void write(byte[] b) throws IOException {
//				return;
//			}
//
//			@Override
//			public void write(int b) throws IOException {
//				return;
//			}
//		};
//		Writer w = new OutputStreamWriter( os );
//		w = new BufferedWriter( w );
//		try {
//			WriterEncoder enc = new WriterEncoder( w );
//			for (int n = 0; n != b.N; ++n) {
//				for (Object o : objectList) {
//					enc.encode( o );
//				}
//			}
//		} catch (IOException ioe) {
//			// ignore?
//		}
//	}
//
//	public static void withIOStreamToString (Benchmark b) {
//		ByteArrayOutputStream os = new ByteArrayOutputStream( 1024 );
//		try {
//			StreamEncoder enc = new StreamEncoder( os );
//			for (int n = 0; n != b.N; ++n) {
//				for (Object o : objectList) {
//					enc.encode( o );
//					enc.os.toString();
//					os.reset();
//				}
//			}
//		} catch (IOException ioe) {
//			// ignore?
//		}
//
//	}

	public static void main (String [] args) throws Throwable{
		objectList = new ArrayList();
		RandomObject ro = new RandomObject( 1 );
		for (int i = 0; i!= 3; ++i) {
			objectList.add(ro.randomMap( 10*(i+1),i ));
			p ("running bench mark with: "+i);
			Benchmark.runBenchmark( EncodeBenchmark.class );
		}
	}
	static void p (Object o) {
		System.out.println(o);
	}

}
