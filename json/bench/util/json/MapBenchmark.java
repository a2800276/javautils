package util.json;

import util.benchmark.Benchmark;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Determine advantages of :
 * ConcurrentHashMap,
 * ConcurrentSkipListMap,
 * HashMap,
 * Hashtable,
 * LinkedHashMap,
 * TreeMap,
 * WeakHashMap
 * Created by a2800276 on 2017-02-05.
 */
public class MapBenchmark {
	// ArrrayList, LinkedList, Stack, Vector

	static byte[] JSONbytes = "[{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}},{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}]".getBytes();

	static JSON.LexerCB WithHashMap(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new HashMap(  );
			}
		};
	}

	public static void hashMap(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithHashMap();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithHashtable(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new Hashtable(  );
			}
		};
	}
	public static void hashttable(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithHashtable();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithLinkedHashMap(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new LinkedHashMap();
			}
		};
	}
	public static void linkedHashMap(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithLinkedHashMap();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithTreeMap(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new TreeMap(  );
			}
		};
	}
	public static void treeMap(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithTreeMap();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithWeakHashMap(){
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new WeakHashMap(  );
			}
		};
	}
	public static void weakHashMap(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithWeakHashMap();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithConcurrentHashMap() {
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new ConcurrentHashMap();
			}
		};
	}
	public static void concurrentHashMap(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithConcurrentHashMap();
		b.setBytes( JSONbytes.length );
		for (int n = 0; n != b.N; ++n) {
			json.reset();
			json.parse( JSONbytes );
		}
	}

	static JSON.LexerCB  WithConcurrentSkipListMap() {
		return new JSON.LexerCB( JSON.NumberType.BigDecimal ) {
			@Override
			Map map() {
				return new ConcurrentSkipListMap();
			}
		};
	}
	public static void concurrentSkipListMap(Benchmark b) {
		JSON json = new JSON();
		json.cb = WithConcurrentSkipListMap();
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
	// Again, no clear winner, at least nothing that would justify replacing
	// default HashMap with something more exotic.
	//
    // running with 871 bytes of json per invocation
    //               hashMap	  100000	     11088 ns/op	  78.55 MB/s
    //            hashttable	  200000	     11601 ns/op	  75.08 MB/s
    //         linkedHashMap	  200000	      9717 ns/op	  89.64 MB/s
    //               treeMap	  200000	      9870 ns/op	  88.25 MB/s
    //           weakHashMap	  200000	     10209 ns/op	  85.32 MB/s
    //     concurrentHashMap	  100000	     10529 ns/op	  82.72 MB/s
    // concurrentSkipListMap	  200000	      9343 ns/op	  93.22 MB/s
    //                  asIs	  200000	      8954 ns/op	  97.27 MB/s
    // running with 1528 bytes of json per invocation
    //               hashMap	  100000	     17348 ns/op	  88.08 MB/s
    //            hashttable	  100000	     18408 ns/op	  83.01 MB/s
    //         linkedHashMap	  100000	     17392 ns/op	  87.86 MB/s
    //               treeMap	  100000	     17372 ns/op	  87.96 MB/s
    //           weakHashMap	  100000	     16998 ns/op	  89.89 MB/s
    //     concurrentHashMap	  100000	     19400 ns/op	  78.76 MB/s
    // concurrentSkipListMap	  100000	     18863 ns/op	  81.01 MB/s
    //                  asIs	  100000	     16539 ns/op	  92.39 MB/s
    // running with 7817 bytes of json per invocation
    //               hashMap	   20000	     91084 ns/op	  85.82 MB/s
    //            hashttable	   20000	     90593 ns/op	  86.29 MB/s
    //         linkedHashMap	   20000	     88220 ns/op	  88.61 MB/s
    //               treeMap	   20000	     87940 ns/op	  88.89 MB/s
    //           weakHashMap	   20000	     96570 ns/op	  80.95 MB/s
    //     concurrentHashMap	   10000	    100419 ns/op	  77.84 MB/s
    // concurrentSkipListMap	   20000	     97404 ns/op	  80.25 MB/s
    //                  asIs	   20000	     87902 ns/op	  88.93 MB/s
    // running with 20416 bytes of json per invocation
    //               hashMap	   10000	    226022 ns/op	  90.33 MB/s
    //            hashttable	   10000	    229269 ns/op	  89.05 MB/s
    //         linkedHashMap	   10000	    226456 ns/op	  90.15 MB/s
    //               treeMap	   10000	    229411 ns/op	  88.99 MB/s
    //           weakHashMap	   10000	    227177 ns/op	  89.87 MB/s
    //     concurrentHashMap	    5000	    243821 ns/op	  83.73 MB/s
    // concurrentSkipListMap	   10000	    240669 ns/op	  84.83 MB/s
    //                  asIs	   10000	    225493 ns/op	  90.54 MB/s
    // running with 90223 bytes of json per invocation
    //               hashMap	    2000	   1001857 ns/op	  90.06 MB/s
    //            hashttable	    2000	   1021196 ns/op	  88.35 MB/s
    //         linkedHashMap	    2000	   1009548 ns/op	  89.37 MB/s
    //               treeMap	    2000	   1021947 ns/op	  88.29 MB/s
    //           weakHashMap	    2000	    996251 ns/op	  90.56 MB/s
    //     concurrentHashMap	    2000	   1065310 ns/op	  84.69 MB/s
    // concurrentSkipListMap	    2000	   1063685 ns/op	  84.82 MB/s
    //                  asIs	    2000	   1009037 ns/op	  89.41 MB/s
	public static void main (String [] args) throws Throwable{
		RandomObject ro = new RandomObject( );
		for (int i = 0; i!= 5; ++i) {
			JSONbytes = JSON.jsonify(
					ro.randomMap( 10*(i+1), i )
			).getBytes();
			p("running with "+JSONbytes.length+" bytes of json per invocation");
			Benchmark.runBenchmark( MapBenchmark.class );
		}
	}
	static void p (Object o) {
		System.out.println(o);
	}

}
