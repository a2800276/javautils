package util.json;

import util.benchmark.Benchmark;
import util.test.RandomObject;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Determine advantages of LinkedList, ArrayList, etc.
 * Created by a2800276 on 2017-02-05.
 */
public class ListBenchmark {
	// ArrrayList, LinkedList, Stack, Vector

	//static final byte[] JSONbytes = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}".getBytes();
	static byte[] JSONbytes = "[{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}]".getBytes();
	static JSON.LexerCB WithLinkedList(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			List list() {
				return new LinkedList(  );
			}
		};
	}

	public static void linkedList(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithLinkedList();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithArrayList(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			List list() {
				return new ArrayList(  );
			}
		};
	}
	public static void arrayList(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithArrayList();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithArrayList10(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			List list() {
				return new ArrayList(10);
			}
		};
	}
	public static void arrayList10(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithArrayList10();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithStack(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			List list() {
				return new Stack(  );
			}
		};
	}
	public static void stack(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithStack();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithVector(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			List list() {
				return new Vector(  );
			}
		};
	}
	public static void vector(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithStack();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithCow() {
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			List list() {
				return new CopyOnWriteArrayList();
			}
		};
	}
	public static void cow(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithCow();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}
	public static void asIs (Benchmark b) {
		JSON json = new JSON();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	//
	// Fairly surprising that the type of List doesn't make
	// a difference. Below is a single run. Suprisingly over
	// several runs, Vector seems to have a slight edge...
	//
	// The "as is" value is for array list. I'll just
	// keep that as it really doesn't seem to make a measurable
	// difference
	//
    // 	running with 606 bytes of json per invocation
    //       stack	  200000	      9267 ns/op	  65.39 MB/s
    //  linkedList	  200000	      8372 ns/op	  72.38 MB/s
    //   arrayList	  500000	      6786 ns/op	  89.30 MB/s
    // arrayList10	  500000	      6930 ns/op	  87.45 MB/s
    //      vector	  500000	      6809 ns/op	  89.00 MB/s
    //         cow	  500000	      6810 ns/op	  88.99 MB/s
    //        asIs	  500000	      6931 ns/op	  87.43 MB/s
    // running with 1180 bytes of json per invocation
    //       stack	  200000	     13056 ns/op	  90.38 MB/s
    //  linkedList	  200000	     13173 ns/op	  89.58 MB/s
    //   arrayList	  200000	     13241 ns/op	  89.12 MB/s
    // arrayList10	  200000	     13101 ns/op	  90.07 MB/s
    //      vector	  200000	     13195 ns/op	  89.43 MB/s
    //         cow	  200000	     13066 ns/op	  90.31 MB/s
    //        asIs	  200000	     13358 ns/op	  88.34 MB/s
    // running with 7124 bytes of json per invocation
    //       stack	   20000	     76437 ns/op	  93.20 MB/s
    //  linkedList	   20000	     75555 ns/op	  94.29 MB/s
    //   arrayList	   20000	     75960 ns/op	  93.79 MB/s
    // arrayList10	   20000	     76197 ns/op	  93.49 MB/s
    //      vector	   20000	     76353 ns/op	  93.30 MB/s
    //         cow	   20000	     77976 ns/op	  91.36 MB/s
    //        asIs	   20000	     77325 ns/op	  92.13 MB/s
    // running with 25519 bytes of json per invocation
    //       stack	   10000	    274974 ns/op	  92.81 MB/s
    //  linkedList	   10000	    275131 ns/op	  92.75 MB/s
    //   arrayList	   10000	    272626 ns/op	  93.60 MB/s
    // arrayList10	   10000	    287350 ns/op	  88.81 MB/s
    //      vector	   10000	    300383 ns/op	  84.95 MB/s
    //         cow	   10000	    290534 ns/op	  87.83 MB/s
    //        asIs	   10000	    282985 ns/op	  90.18 MB/s
    // running with 78077 bytes of json per invocation
    //       stack	    2000	    866111 ns/op	  90.15 MB/s
    //  linkedList	    2000	    875530 ns/op	  89.18 MB/s
    //   arrayList	    2000	    880080 ns/op	  88.72 MB/s
    // arrayList10	    2000	    858958 ns/op	  90.90 MB/s
    //      vector	    2000	    879265 ns/op	  88.80 MB/s
    //         cow	    2000	    920370 ns/op	  84.83 MB/s
    //        asIs	    2000	    912454 ns/op	  85.57 MB/s
	//
	public static void main (String [] args) throws Throwable{
		RandomObject ro = new RandomObject( 1 );
		for (int i = 0; i!= 5; ++i) {
			JSONbytes = JSON.jsonify(
					ro.randomMap( 10*(i+1), i )
			).getBytes();
			p("running with "+JSONbytes.length+" bytes of json per invocation");
			Benchmark.runBenchmark( ListBenchmark.class );
		}
	}
	static void p (Object o) {
		System.out.println(o);
	}

}
