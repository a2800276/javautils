package util.json;

import util.benchmark.Benchmark;

/**
 * Determine advantages of LinkedList, ArrayList, etc.
 * Created by a2800276 on 2017-02-05.
 */
public class StaticBenchmark {



	static byte[] JSONbytes = "[{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}]".getBytes();
	public static void asIs (Benchmark b) {
		JSON.LexerCB cb = new JSON.LexerCB();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			cb.reset();
			Lexer.lexer.lex(JSONbytes, cb);
		}
	}

	public static void onlyStatic (Benchmark b) {
		JSON.LexerCB cb = new JSON.LexerCB();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			cb.reset();
			StaticLexer.lex(JSONbytes, cb);
		}
	}
	public static void asIsDummyCB (Benchmark b) {
		Lexer.CB cb = new Lexer.CB(){
			@Override
			void tok(Lexer.Token tok) { }

			@Override
			void tok(String s) { }

			@Override
			void numberToken(CharSequence cs) { }
		};
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			cb.reset();
			Lexer.lexer.lex(JSONbytes, cb);
		}
	}
	public static void staticDummyCB (Benchmark b) {
		Lexer.CB cb = new Lexer.CB(){
			@Override
			void tok(Lexer.Token tok) { }

			@Override
			void tok(String s) { }

			@Override
			void numberToken(CharSequence cs) { }
		};
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			cb.reset();
			StaticLexer.lex(JSONbytes, cb);
		}
	}

	// Again, no significant difference.
	//
    // 	running with 606 bytes of json per invocation
    //          asIs	  200000	      8295 ns/op	  73.06 MB/s
    //    onlyStatic	  200000	      7455 ns/op	  81.29 MB/s
    //   asIsDummyCB	  200000	      5515 ns/op	 109.88 MB/s
    // staticDummyCB	  200000	      5598 ns/op	 108.25 MB/s
    // running with 1180 bytes of json per invocation
    //          asIs	  200000	     12776 ns/op	  92.36 MB/s
    //    onlyStatic	  200000	     12857 ns/op	  91.78 MB/s
    //   asIsDummyCB	  200000	     10570 ns/op	 111.64 MB/s
    // staticDummyCB	  200000	     10672 ns/op	 110.57 MB/s
    // running with 7124 bytes of json per invocation
    //          asIs	   20000	     74544 ns/op	  95.57 MB/s
    //    onlyStatic	   20000	     76134 ns/op	  93.57 MB/s
    //   asIsDummyCB	   50000	     64692 ns/op	 110.12 MB/s
    // staticDummyCB	   50000	     65133 ns/op	 109.38 MB/s
    // running with 25519 bytes of json per invocation
    //          asIs	   10000	    277322 ns/op	  92.02 MB/s
    //    onlyStatic	   10000	    269811 ns/op	  94.58 MB/s
    //   asIsDummyCB	   10000	    233410 ns/op	 109.33 MB/s
    // staticDummyCB	   10000	    233851 ns/op	 109.13 MB/s
    // running with 78077 bytes of json per invocation
    //          asIs	    2000	    847202 ns/op	  92.16 MB/s
    //    onlyStatic	    2000	    844732 ns/op	  92.43 MB/s
    //   asIsDummyCB	    2000	    712694 ns/op	 109.55 MB/s
    // staticDummyCB	    2000	    732701 ns/op	 106.56 MB/s
	public static void main (String [] args) throws Throwable{
		RandomObject ro = new RandomObject( 1 );
		for (int i = 0; i!= 5; ++i) {
			JSONbytes = JSON.jsonify(
					ro.randomMap( 10*(i+1), i )
			).getBytes();
			p("running with "+JSONbytes.length+" bytes of json per invocation");
			Benchmark.runBenchmark( StaticBenchmark.class );
		}
	}
	static void p (Object o) {
		System.out.println(o);
	}

}
