package util.json;


import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class NumberTypeTest {
    static final String NUMBER_JSON = "[0,1,2,3]";
    static final String INT_OVERFLOW = "[1,2, 8589934592]"; // 2^33
	static final String LONG_OVERFLOW = "[1,2, -18446744073709551616]"; // 2^64
	static final String INT_SCIENTIFIC = "[1,2, 8E10]"; // 2^33
	static final String INT_DECIMAL = "[1,2, 8.10]"; // 2^33
	// float: infinity, double in precision...
	static final String SUPER_LONG_FLOAT = "[123456789098765432123567890987654321234567890987654321237890.123567890987654321235678909876543212456789012356789012345678098765432345678]";

	static final double SuperLong = 123456789098765432123567890987654321234567890987654321237890.123567890987654321235678909876543212456789012356789012345678098765432345678;

    @Test
    public static void testDefault() {
    	List os = (List)JSON.parse( NUMBER_JSON );
    	assertEquals( os.size(), 4 );
    	for (int i = 0; i!= os.size(); ++i) {
    	    Object o = os.get(i);
    	    assertTrue(o instanceof BigDecimal);
    	    assertEquals( new BigDecimal(i) , (BigDecimal)o );
        }
    }
	@Test
	public static void testDefaultExplicit() {
		List os = (List)JSON.parse( NUMBER_JSON, JSON.NumberType.BigDecimal );
		assertEquals( os.size(), 4 );
		for (int i = 0; i!= os.size(); ++i) {
			Object o = os.get(i);
			assertTrue(o instanceof BigDecimal);
			assertEquals( new BigDecimal(i) , (BigDecimal)o );
		}
	}
	@Test
	public static void testFloat() {
		List os = (List)JSON.parse( NUMBER_JSON, JSON.NumberType.Float );
		assertEquals( os.size(), 4 );
		for (int i = 0; i!= os.size(); ++i) {
			Object o = os.get(i);
			assertTrue(o instanceof Float);
			assertEquals( 1.0f * i , (float)o );
		}

		os = (List)JSON.parse( SUPER_LONG_FLOAT, JSON.NumberType.Float );
		float f = (float)os.get(0);
		assertEquals( f, Float.POSITIVE_INFINITY );

		os = (List)JSON.parse( INT_OVERFLOW, JSON.NumberType.Float );
		Float value = (Float)os.get(2);
		assertEquals( value.longValue(), 8589934592L );
	}
	@Test
	public static void testDouble() {
		List os = (List)JSON.parse( NUMBER_JSON, JSON.NumberType.Double );
		assertEquals( os.size(), 4 );
		for (int i = 0; i!= os.size(); ++i) {
			Object o = os.get(i);
			assertTrue(o instanceof Double);
			assertEquals( 1.0 * i, (double)o );
		}
		JSON.parse( SUPER_LONG_FLOAT, JSON.NumberType.Double );

		os = (List)JSON.parse( SUPER_LONG_FLOAT, JSON.NumberType.Double );
		double f = (double)os.get(0);
		assertEquals( f, SuperLong );

		os = (List)JSON.parse( INT_OVERFLOW, JSON.NumberType.Double );
		Double value = (Double)os.get(2);
		assertEquals( value.longValue(), 8589934592L );
	}
	@Test
	public static void testInt() {
		List os = (List)JSON.parse( NUMBER_JSON, JSON.NumberType.Integer );
		assertEquals( os.size(), 4 );
		for (int i = 0; i!= os.size(); ++i) {
			Object o = os.get(i);
			assertTrue(o instanceof Integer);
			assertEquals( i , (int)o );
		}
    }
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testIntOverflow() {
		List os = (List)JSON.parse( INT_OVERFLOW, JSON.NumberType.Integer );
	}
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testIntScientific() {
		List os = (List)JSON.parse( INT_SCIENTIFIC, JSON.NumberType.Integer );
	}
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testIntFloat() {
		List os = (List)JSON.parse( INT_DECIMAL, JSON.NumberType.Integer );
	}
	@Test
	public static void testLong() {
		List os = (List)JSON.parse( NUMBER_JSON, JSON.NumberType.Long );
		assertEquals( os.size(), 4 );
		for (int i = 0; i!= os.size(); ++i) {
			Object o = os.get(i);
			assertTrue(o instanceof Long);
			assertEquals( 0L+i , (long)o );
		}
	}
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testLongOverflow() {
		List os = (List)JSON.parse( LONG_OVERFLOW, JSON.NumberType.Long );
	}
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testLongScientific() {
		List os = (List)JSON.parse( INT_SCIENTIFIC, JSON.NumberType.Long );
	}
	@Test(expectedExceptions = NumberFormatException.class)
	public static void testLongFloat() {
		List os = (List)JSON.parse( INT_DECIMAL, JSON.NumberType.Long );
	}

}
