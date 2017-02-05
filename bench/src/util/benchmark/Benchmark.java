package util.benchmark;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Benchmark is a small utility to quickly and easily run simple
 * Benchmarks on Java code. The code is based entirely on the benchmark
 * utility from Go (see http://golang.org/pkg/testing/).
 * <p>
 * In order to write benchmarks, you need to provide a class that has
 * methods with signatures of the form:
 * <p>
 * <pre> public void methodNameIrrelevant (Benchmark b) {...} </pre>
 *
 * The benchmarks in the class you wrote will be executed when the
 * following command is run:
 *
 * <pre> java -jar Benchmark
 *  {name.of.class.containing.my.BenchmarkMethods} </pre>
 * <p>
 * ( The above seems to have some classloader issues ... )
 * <p>
 * Alternatively, you can run benchmarks within Java code by calling:
 * <p>
 * <pre> Benchmark.runBenchmark(MyBenchMark.class); </pre>
 *
 * The Benchmark utility will vary the number of times to run the
 * benchmark until it runs long enough to be measured accurately.
 *
 * The number of times that a benchmark is supposed to run is signaled
 * to your benchmark function by the field <code> N </code> in the
 * <code>Benchmark</code> argument to the function.
 *
 * N.B. it is the responsibility of the benchmark function you provide
 * to run the code <code>N</code> number of times and not up to the
 * Benchmark utility! A typical benchmark function will look like this:
 *
 * <pre>
 *      public static void string (Benchmark b) {
 *        for (int n =0; n!= b.N; ++n) {
 *          String sum = "";
 *          for (int i = 1; i!= 1000; ++i) {
 *            sum += i;
 *          }
 *          if (n == 0) {
 *            b.setBytes(sum.getBytes().length);
 *          }
 *        }
 *      }
 *
 *  </pre>
 * <p>
 * If <code>setBytes(...)</code> is called in the benchmark test, the
 * benchmarks will display information about the throughput (MB/s) of the code
 * as well as the the average time per operation. The value passed to
 * <code>setBytes(...)</code> is the number of bytes processed in a
 * single iteration, not how many bytes were processed in
 * <code>b.N</code> loop iterations.
 * <p>
 * In case the benchmark test requires lengthy setup that should be
 * ignored, you can stop and restart the internal timer like this:
 * <p>
 * <pre>
 *    public void myBenchmark (Benchmark b) {
 *      b.stopTimer();
 *      reallyLengthySetup();
 *      b.startTimer();
 *      for (int i =0; i!= b.N, ++i) {
 *        ... whatever ...
 *      }
 *    }
 *  </pre>
 * <p>
 * This class contains three sample benchmarks measuring the
 * performance of string concatenation of <code>String</code>,
 * <code>StringBuffer</code> and <code>StringBuilder</code>. These can
 * be run as follows:
 * <p>
 * <pre>
 *     $ java -jar lib/benchmark.jar benchmark.Benchmark
 *            string	     500	   6523698 ns/op	   0.44 MB/s
 *      stringBuffer	   50000	     38646 ns/op	  74.76 MB/s
 *     stringBuilder	   50000	     32237 ns/op	  89.62 MB/s
 *  </pre>
 * <p>
 * The results printed are:
 * <p>
 * <ul>
 * <li>name of the benchmark method</li>
 * <li>number of times the utility ran the benchmark</li>
 * <li>number of ns per loop</li>
 * <li>number of MB processed per second</li>
 * </ul>
 * <p>
 * The tool may also be call as follows:
 * <p>
 * <pre>
 *     $ java -jar lib/benchmark.jar benchmark.Benchmark stringB.*
 *      stringBuffer	   50000	     38646 ns/op	  74.76 MB/s
 *     stringBuilder	   50000	     32237 ns/op	  89.62 MB/s
 *  </pre>
 * <p>
 * The final argument is an optional regular expression, if provided,
 * only benchmark methods matching this expression are run.
 */
public class Benchmark {
    /**
     * the number of times the Benchmark utility expects you to perform
     * whatever it is you're benchmarking.
     * <p>
     * A typical benchmark method will look like this:
     * <p>
     * <pre>
     *   public void benchmarkMethod (Benchmark b) {
     *      for (int i = 0; i != b.N; ++i) {
     *        ... my benchmark code ...
     *      }
     *   }
     * </pre>
     * <p>
     * The responsibility of running the benchmark <code>N</code> is up to
     * you, the the utility just tells you the value of N...
     */
    public int N;

    InternalBenchmark internal;
    long ns;
    long bytes;
    long start;

    static long min(long x, long y) {
        return x > y ? y : x;
    }

    static long max(long x, long y) {
        return x < y ? y : x;
    }

    // roundDown10 rounds a number down to the nearest power of 10.
    static int roundDown10(int n) {
        int tens = 0;
        while (n > 10) {
            n /= 10;
            ++tens;
        }
        int result = 1;
        for (int i = 0; i < tens; ++i) {
            result *= 10;
        }
        return result;

    }

    // roundUp rounds x up to a number of the form [1eX, 2eX, 5eX].
    static int roundUp(int n) {
        int base = roundDown10(n);
        if (n < (2 * base)) {
            return 2 * base;
        }
        if (n < (5 * base)) {
            return 5 * base;
        }
        return 10 * base;
    }

    static boolean hasBenchmarkParam(Method m) {
        Class[] paramTypes = m.getParameterTypes();
        if (paramTypes == null || paramTypes.length != 1) {
            return false;
        }
        return paramTypes[0].equals(Benchmark.class);
    }

    public static void runBenchmark(final Class c, String regexp) throws InstantiationException, IllegalAccessException {
        Pattern pattern = null;
        if (null != regexp) {
            pattern = Pattern.compile(regexp);
        }
        List<Method> list = new ArrayList<Method>();
        int maxName = 0;
        for (Method m : c.getMethods()) {
            if (m.getReturnType() != Void.TYPE || !hasBenchmarkParam(m)) {
                continue;
            }
            if (null != pattern) {
                Matcher matcher = pattern.matcher(m.getName());
                if (!matcher.matches()) {
                    continue;
                }
            }
            list.add(m);
            maxName = m.getName().length() > maxName ? m.getName().length() : maxName;
        }
        for (final Method m : list) {
            InternalBenchmark ib = new InternalBenchmark() {
                Object o = c.newInstance();

                void runBenchmark(Benchmark b) {
                    try {
                        m.invoke(o, b);
                    } catch (Throwable iae) {
                        throw new RuntimeException(iae);
                    }
                }
            };
            ib.name = m.getName();
            Benchmark b = new Benchmark();
            b.internal = ib;
            BenchmarkResult res = b.run();
            System.out.println(String.format("%" + maxName + "s\t%s", ib.name, res));
        }
    }

    public static void runBenchmark(final Class c) throws InstantiationException, IllegalAccessException {
        runBenchmark(c, null);
    }

    /**
     * Example:
     * <p>
     * <pre>
     *   public static void stringBuilder (Benchmark b) {
     *     for (int n =0; n!= b.N; ++n) {
     *       StringBuilder sum = new StringBuilder();
     *       for (int i = 1; i!= 1000; ++i) {
     *         sum.append(i);
     *       }
     *       if (n == 0) {
     *         b.setBytes(sum.length());
     *       }
     *     }
     *   }
     * </pre>
     */
    public static void stringBuilder(Benchmark b) {
        for (int n = 0; n != b.N; ++n) {
            StringBuilder sum = new StringBuilder();
            for (int i = 1; i != 1000; ++i) {
                sum.append(i);
            }
            if (n == 0) {
                b.setBytes(sum.length());
            }
        }
    }

    /**
     * Example:
     * <p>
     * <pre>
     *
     *   public static void string (Benchmark b) {
     *     for (int n =0; n!= b.N; ++n) {
     *       String sum = "";
     *       for (int i = 1; i!= 1000; ++i) {
     *         sum += i;
     *       }
     *       if (n == 0) {
     *         b.setBytes(sum.getBytes().length);
     *       }
     *     }
     *   }
     * </pre>
     */
    public static void string(Benchmark b) {
        for (int n = 0; n != b.N; ++n) {
            String sum = "";
            for (int i = 1; i != 1000; ++i) {
                sum += i;
            }
            if (n == 0) {
                b.setBytes(sum.getBytes().length);
            }
        }
    }

    static void usage() {
        System.err.println("usage: [jre] Benchmark <name of benchmark class> [optional regexp]");
        System.exit(1);
    }

    public static void main(String[] args) throws Throwable {
        String regexp = null;
        if (args.length != 1 && args.length != 2) {
            usage();
        }
        if (args.length == 2) {
            regexp = args[1];
        }
        Class c = Class.forName(args[0]);
        runBenchmark(c, regexp);
    }

    //
    // Reflection stuff to load benchmarks
    //

    /**
     * starts timing a test.  This function is called automatically
     * before a benchmark starts, but it can also used to resume timing after
     * a call to StopTimer.
     */
    public void startTimer() {
        this.start = System.nanoTime();
    }

    /**
     * stops timing a test.  This can be used to pause the timer
     * while performing complex initialization that you don't
     * want to measure.
     */
    public void stopTimer() {
        if (this.start > 0) {
            this.ns += System.nanoTime() - this.start;
        }
        this.start = 0;
    }

    /**
     * stops the timer and sets the elapsed benchmark time to zero.
     */
    public void resetTimer() {
        this.start = 0;
        this.ns = 0;
    }


    // Test Benchmarks ...

    /**
     * records the number of bytes processed in a single operation.
     * If this is called, the benchmark will report ns/op and MB/s.
     */
    public void setBytes(long n) {
        this.bytes = n;
    }

    long nsPerOp() {
        if (this.N <= 0) {
            return 0;
        }
        return this.ns / this.N;
    }

    // runN runs a single benchmark for the specified number of iterations.
    void runN(int n) {
        this.N = n;
        this.resetTimer();
        this.startTimer();
        this.internal.runBenchmark(this);
        this.stopTimer();
    }

    /**
     * <code>run</code> times the benchmark function.  It gradually increases the number
     * of benchmark iterations until the benchmark runs for a second in order
     * to get a reasonable measurement.  It prints timing information in this form:
     * <p>
     * <pre>
     * 		testing.BenchmarkHello	100000		19 ns/op
     * </pre>
     */
    public BenchmarkResult run() {
        int n = 1;
        // Run the benchmark for a single iteration in case it's expensive.
        this.runN(n);
        // Run the benchmark for at least a second.
        while (this.ns < 1e9 && n < 1e9) {
            long last = n;
            // Predict iterations/sec.
            if (this.nsPerOp() == 0) {
                n = (int) 1e9;
            } else {
                n = (int) (1e9 / this.nsPerOp());
            }
            // Run more iterations than we think we'll need for a second (1.5x).
            // Don't grow too fast in case we had timing errors previously.
            // Be sure to run at least one more than last time.
            n = (int) max(min(n + n / 2, 100 * last), last + 1);
            // Round up to something easy to read.
            n = roundUp(n);
            this.runN(n);
        }
        return new BenchmarkResult(this.N, this.ns, this.bytes);
    }

    /**
     * Example:
     * <p>
     * <pre>
     *   public void stringBuffer (Benchmark b) {
     *     for (int n =0; n!= b.N; ++n) {
     *       StringBuffer sum = new StringBuffer();
     *       for (int i = 1; i!= 1000; ++i) {
     *         sum.append(i);
     *       }
     *       if (n == 0) {
     *         b.setBytes(sum.length());
     *       }
     *     }
     *   }
     * </pre>
     */
    public void stringBuffer(Benchmark b) {
        for (int n = 0; n != b.N; ++n) {
            StringBuffer sum = new StringBuffer();
            for (int i = 1; i != 1000; ++i) {
                sum.append(i);
            }
            if (n == 0) {
                b.setBytes(sum.length());
            }
        }
    }



}

abstract class InternalBenchmark {
	String name;

	abstract void runBenchmark(Benchmark b);
}

class BenchmarkResult {

	int n;
	long ns;
	long bytes;

	BenchmarkResult(int n, long ns, long bytes) {
		this.n = n;
		this.ns = ns;
		this.bytes = bytes;
	}

	long nsPerOp() {
		return this.n <= 0 ? 0 : this.ns / this.n;
	}

	public String toString() {
		long ns = this.nsPerOp();
		String mb = "";
		if (ns > 0 && this.bytes > 0) {
			mb = String.format("\t%7.2f MB/s", (this.bytes / 1e6) / (ns / 1e9));
		}
		return String.format("%8d\t%10d ns/op%s", this.n, ns, mb);
	}
}
